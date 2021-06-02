package io.agora.education.impl.sync

import android.util.Log
import com.google.gson.Gson
import io.agora.Constants.Companion.APPID
import io.agora.Constants.Companion.AgoraLog
import io.agora.education.impl.util.Convert
import io.agora.base.callback.ThrowableCallback
import io.agora.base.network.BusinessException
import io.agora.education.api.BuildConfig.API_BASE_URL
import io.agora.education.api.EduCallback
import io.agora.education.api.logger.LogLevel
import io.agora.education.api.room.EduRoom
import io.agora.education.api.room.data.EduRoomInfo
import io.agora.education.api.room.data.EduRoomStatus
import io.agora.education.api.statistics.AgoraError
import io.agora.education.impl.ResponseBody
import io.agora.education.impl.cmd.CMDDataMergeProcessor
import io.agora.education.impl.cmd.bean.CMDResponseBody
import io.agora.education.impl.network.RetrofitManager
import io.agora.education.impl.room.EduRoomImpl
import io.agora.education.impl.room.data.response.EduSequenceListRes
import io.agora.education.impl.room.data.response.EduSequenceRes
import io.agora.education.impl.room.data.response.EduSequenceSnapshotRes
import io.agora.education.impl.room.network.RoomService
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList

/**只同步管理channelMsg，不同步peerMsg，因为RTM能保障peerMsg到达，而且peerMsg只和业务有关*/
internal class RoomSyncHelper(private val eduRoom: EduRoom, roomInfo: EduRoomInfo,
                              roomStatus: EduRoomStatus, maxRetry: Int)
    : RoomSyncSession(roomInfo, roomStatus) {

    companion object {
        val TAG = RoomSyncHelper::class.java.simpleName
    }

    private var cache = Cache()
    private var maxRetry = 3

    init {
        this.maxRetry = maxRetry
    }

    private var sequenceRetryCount = 0
    private var snapshotRetryCount = 0

    /**当前是否在同步数据的过程中*/
    private var syncing = false

    /**本地最新的sequenceId*/
    private var lastSequenceId: Int = -1

    /**一个同步流程中的id集合*/
    private var sequenceList = mutableListOf<Int>()

    /**一个同步流程中的cmd数据集合*/
    private val sequenceData: TreeMap<Int, EduSequenceRes<Any>> = TreeMap()

    private fun clearSequence() {
        sequenceList.clear()
        sequenceData.clear()
    }

    /**
     * @return null 成功更新，序号前后衔接，无遗漏
     *         != null 有遗漏，需请求遗漏数据
     * @return pair.first:nextId
     *         pair.second:count*/
    override fun updateSequenceId(cmdResponseBody: CMDResponseBody<Any>): Pair<Int, Int>? {
        val eduSequenceRes = Convert.convertCMDResponseBody(cmdResponseBody)
        if (syncing || (eduRoom as EduRoomImpl).joining) {
            AgoraLog.w("$TAG->join过程中或者同步seq过程中收到的消息均加入缓存")
            cache.add(cmdResponseBody)
        } else {
            when {
                cmdResponseBody.sequence - lastSequenceId == 1 -> {
                    AgoraLog.logMsg("sequence-${cmdResponseBody.sequence}和${lastSequenceId}衔接，" +
                            "传递转发", LogLevel.INFO.value)
                    lastSequenceId = cmdResponseBody.sequence
                    /**传递转发*/
                    eduRoom.cmdDispatch.dispatchMsg(cmdResponseBody)
                }
                cmdResponseBody.sequence - lastSequenceId > 1 -> {
                    AgoraLog.logMsg("sequence-${cmdResponseBody.sequence}和${lastSequenceId}不衔接，" +
                            "返回丢失起始点", LogLevel.INFO.value)
                    sequenceList.add(cmdResponseBody.sequence)
                    sequenceData[cmdResponseBody.sequence] = eduSequenceRes
                    return Pair(lastSequenceId + 1, cmdResponseBody.sequence - lastSequenceId - 1)
                }
                else -> {
                    /**已存在，不处理*/
                }
            }
        }
        return null
    }

    private fun addSequenceData(sequenceListRes: EduSequenceListRes<Any>) {
        synchronized(sequenceList) {
            val iterable = sequenceListRes.list.iterator()
            while (iterable.hasNext()) {
                val sequenceRes = iterable.next()
                sequenceList.add(sequenceRes.sequence)
                sequenceData[sequenceRes.sequence] = sequenceRes
            }
        }
    }

    /**处理缓存中的数据
     * 1:join成功后
     * 2:join成功后的流程中，某一次sync完成后
     * */
    fun handleCache(callback: EduCallback<Unit>) {
        AgoraLog.logMsg("检查并处理缓存数据(处理CMD消息)", LogLevel.INFO.value)
        if (cache.hasCache()) {
            val iterable = cache.list.iterator()
            while (iterable.hasNext()) {
                val element = iterable.next()
                val pair = updateSequenceId(element)
                if (pair != null) {
                    fetchLostSequence(pair.first, pair.second, callback)
                    return
                }
            }
        }
        sequenceList.sort()
        sequenceList?.forEach {
            val cmdRes = Convert.convertEduSequenceRes(sequenceData[it] as EduSequenceRes<Any>)
            if (it > lastSequenceId) {
                lastSequenceId = it
            }
            (eduRoom as EduRoomImpl).cmdDispatch.dispatchMsg(cmdRes)
        }
        cache.clear()
        clearSequence()
    }

    override fun fetchLostSequence(callback: EduCallback<Unit>) {
        fetchLostSequence(lastSequenceId + 1, null, callback)
    }

    /**请求当前丢失的sequence消息
     * @param nextId 查询的起始sequence(当前本地最新的sequence的下一个)
     * @param count 需要查询的条数(为空则是请求全部)*/
    override fun fetchLostSequence(nextId: Int, count: Int?, callback: EduCallback<Unit>) {
        AgoraLog.i("$TAG->根据${nextId}请求丢失数据}")
        syncing = true
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .fetchLostSequences(eduRoom.getLocalUser().userInfo.userToken!!, APPID,
                        roomInfo.roomUuid, nextId, count)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<EduSequenceListRes<Any>>> {
                    override fun onSuccess(res: ResponseBody<EduSequenceListRes<Any>>?) {
                        AgoraLog.i("$TAG->根据${nextId}请求到的丢失数据:${Gson().toJson(res)}")
                        res?.data?.let {
                            /**把缺失的seq数据添加到集合中*/
                            addSequenceData(res.data as EduSequenceListRes<Any>)
                            /**join成功后，自行处理缓存中的数据*/
                            synchronized((eduRoom as EduRoomImpl).joinSuccess) {
                                if (eduRoom.joinSuccess) {
                                    handleCache(callback)
                                }
                            }
                            syncing = false
                        }
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        error?.code?.let {
                            if (error?.code == AgoraError.SEQUENCE_NOT_EXISTS.value) {
                                AgoraLog.e("$TAG->被请求的sequence不存在，清空本地旧缓存，拉全量数据")
                                (eduRoom as EduRoomImpl).clearData()
                                clearSequence()
                                fetchSnapshot(callback)
                            } else {
                                /**请求失败重试*/
                                if (sequenceRetryCount <= maxRetry) {
                                    sequenceRetryCount++
                                    AgoraLog.e("$TAG->请求缺失数据失败,第$snapshotRetryCount 次重试")
                                    fetchLostSequence(nextId, count, callback)
                                } else {
                                    /**彻底失败，恢复原值*/
                                    AgoraLog.e("$TAG->请求缺失数据彻底失败")
                                    sequenceRetryCount = 0
                                    callback.onFailure(error.code, error.message)
                                }
                            }
                        }
                        syncing = true
                    }
                }))
    }

    /**请求快照（拉全量数据）*/
    override fun fetchSnapshot(callback: EduCallback<Unit>) {
        AgoraLog.w("$TAG->请求快照（拉全量数据）")
        syncing = true
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .fetchSnapshot(eduRoom.getLocalUser().userInfo.userToken!!, APPID, roomInfo.roomUuid)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<EduSequenceSnapshotRes>> {
                    override fun onSuccess(res: ResponseBody<EduSequenceSnapshotRes>?) {
                        Log.e(TAG, "请求到的快照数据:${Gson().toJson(res)}")
                        /**因为是全量数据，所以直接全部赋值即可*/
                        res?.data?.let {
                            CMDDataMergeProcessor.syncSnapshotToRoom(eduRoom, it.snapshot)
                            lastSequenceId = it.sequence
                        }
                        syncing = false
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        val error = throwable as? BusinessException
                        error?.code?.let {
                            /**请求失败重试*/
                            if (snapshotRetryCount <= maxRetry) {
                                snapshotRetryCount++
                                AgoraLog.e("$TAG->请求快照失败,第$snapshotRetryCount 次重试")
                                fetchSnapshot(callback)
                            } else {
                                /**彻底失败，恢复原值*/
                                AgoraLog.e("$TAG->请求快照彻底失败")
                                snapshotRetryCount = 0
                                callback.onFailure(error.code, error.message)
                            }
                        }
                        syncing = true
                    }
                }))
    }

    internal class Cache {
        /**缓存的cmd消息*/
//        var list = mutableListOf<CMDResponseBody<Any>>()
        var list = CopyOnWriteArrayList<CMDResponseBody<Any>>()

        fun add(cmdResponseBody: CMDResponseBody<Any>) {
            list.add(cmdResponseBody)
        }

        fun hasCache(): Boolean {
            return list.size > 0
        }

        fun clear() {
            list.clear()
        }
    }
}