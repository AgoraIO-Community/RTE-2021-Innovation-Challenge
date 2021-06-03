package io.agora.education.impl.manager

import android.os.Build
import android.util.Base64
import com.google.gson.Gson
import io.agora.Constants.Companion.APPID
import io.agora.Constants.Companion.AgoraLog
import io.agora.Constants.Companion.LOGS_DIR_NAME
import io.agora.Constants.Companion.LOG_APPSECRET
import io.agora.base.callback.ThrowableCallback
import io.agora.base.network.BusinessException
import io.agora.education.api.BuildConfig
import io.agora.education.api.BuildConfig.API_BASE_URL
import io.agora.education.api.EduCallback
import io.agora.education.api.logger.DebugItem
import io.agora.education.api.logger.LogLevel
import io.agora.education.api.manager.EduManager
import io.agora.education.api.manager.EduManagerOptions
import io.agora.education.api.room.EduRoom
import io.agora.education.api.room.data.*
import io.agora.education.api.util.CryptoUtil
import io.agora.education.impl.ResponseBody
import io.agora.education.impl.network.RetrofitManager
import io.agora.education.impl.room.EduRoomImpl
import io.agora.education.impl.room.data.EduRoomInfoImpl
import io.agora.education.impl.room.data.RtmConnectState
import io.agora.education.impl.room.data.response.EduLoginRes
import io.agora.education.impl.room.network.RoomService
import io.agora.education.impl.util.Convert
import io.agora.log.LogManager
import io.agora.log.UploadManager
import io.agora.rte.RteCallback
import io.agora.rte.RteEngineImpl
import io.agora.rte.listener.RteEngineEventListener
import io.agora.rtm.RtmMessage
import io.agora.rtm.RtmStatusCode
import okhttp3.logging.HttpLoggingInterceptor
import java.io.File

internal class EduManagerImpl(
        options: EduManagerOptions
) : EduManager(options), RteEngineEventListener {

    companion object {
        private const val TAG = "EduManagerImpl"

        /**管理所有EduRoom示例的集合*/
        private val eduRooms = mutableListOf<EduRoom>()

        fun addRoom(eduRoom: EduRoom): Boolean {
            return eduRooms.add(eduRoom)
        }

        fun removeRoom(eduRoom: EduRoom): Boolean {
            return eduRooms.remove(eduRoom)
        }
    }

    /**全局的rtm连接状态*/
    private val rtmConnectState = RtmConnectState()

    init {
        options.logFileDir?.let {
            options.logFileDir = options.context.cacheDir.toString().plus(File.separatorChar).plus(LOGS_DIR_NAME)
        }
        LogManager.init(options.logFileDir!!, "AgoraEducation")
        AgoraLog = LogManager("SDK")
        logMessage("${TAG}: 初始化LogManager,log路径为${options.logFileDir}", LogLevel.INFO)
        logMessage("${TAG}: 初始化EduManagerImpl", LogLevel.INFO)
        logMessage("${TAG}: 初始化RteEngineImpl", LogLevel.INFO)
        RteEngineImpl.init(options.context, options.appId, options.logFileDir!!)
        /**为RteEngine设置eventListener*/
        RteEngineImpl.eventListener = this
        APPID = options.appId
        val auth = Base64.encodeToString("${options.customerId}:${options.customerCertificate}"
                .toByteArray(Charsets.UTF_8), Base64.DEFAULT).replace("\n", "").trim()
        RetrofitManager.instance()!!.addHeader("Authorization", CryptoUtil.getAuth(auth))
        RetrofitManager.instance()!!.setLogger(object : HttpLoggingInterceptor.Logger {
            override fun log(message: String) {
                /**OKHttp的log写入SDK的log文件*/
                logMessage(message, LogLevel.INFO)
            }
        })
        logMessage("${TAG}: 初始化EduManagerImpl完成", LogLevel.INFO)
    }

    override fun createClassroom(config: RoomCreateOptions): EduRoom {
        val eduRoomInfo = EduRoomInfoImpl(config.roomType, config.roomUuid, config.roomName)
        val status = EduRoomStatus(EduRoomState.INIT, 0, true, 0)
        val room = EduRoomImpl(eduRoomInfo, status)
        /**设置默认用户名*/
        room.defaultUserName = options.userName
        return room
    }

    fun login(userUuid: String, callback: EduCallback<Unit>) {
        logMessage("${TAG}: 调用login接口", LogLevel.INFO)
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .login(APPID, userUuid)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<EduLoginRes>> {
                    override fun onSuccess(res: ResponseBody<EduLoginRes>?) {
                        logMessage("${TAG}: 成功调用login接口->${Gson().toJson(res)}", LogLevel.INFO)
                        val loginRes = res?.data
                        loginRes?.let {
                            RteEngineImpl.loginRtm(loginRes.userUuid, loginRes.rtmToken,
                                    object : RteCallback<Unit> {
                                        override fun onSuccess(res: Unit?) {
                                            logMessage("${TAG}: 成功登录RTM", LogLevel.INFO)
                                            callback.onSuccess(res)
                                        }

                                        override fun onFailure(code: Int, reason: String?) {
                                            logMessage("${TAG}: 登录RTM失败->code:$code,reason:$reason", LogLevel.ERROR)
                                            callback.onFailure(code, reason)
                                        }
                                    })
                        }
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        error = error ?: BusinessException(throwable?.message)
                        error?.code?.let {
                            logMessage("${TAG}: 调用login接口失败->code:${error?.code}, reason:${error?.message
                                    ?: throwable?.message}", LogLevel.ERROR)
                            callback.onFailure(error?.code, error?.message ?: throwable?.message)
                        }
                    }
                }))
    }

    override fun release() {
        logMessage("${TAG}: 调用release函数退出RTM并释放数据", LogLevel.INFO)
        RteEngineImpl.logoutRtm()
        eduRooms.clear()
    }

    override fun logMessage(message: String, level: LogLevel): EduError {
        when (level) {
            LogLevel.NONE -> {
                AgoraLog.d(message)
            }
            LogLevel.INFO -> {
                AgoraLog.i(message)
            }
            LogLevel.WARN -> {
                AgoraLog.w(message)
            }
            LogLevel.ERROR -> {
                AgoraLog.e(message)
            }
        }
        return EduError(-1, "")
    }

    override fun uploadDebugItem(item: DebugItem, callback: EduCallback<String>): EduError {
        val uploadParam = UploadManager.UploadParam(APPID, BuildConfig.VERSION_NAME, Build.DEVICE,
                Build.VERSION.SDK, "ZIP", "Android", null)
        logMessage("${TAG}: 调用uploadDebugItem函数上传日志，参数->${Gson().toJson(uploadParam)}", LogLevel.INFO)
        UploadManager.upload(options.context, LOG_APPSECRET, API_BASE_URL, options.logFileDir!!, uploadParam,
                object : ThrowableCallback<String> {
                    override fun onSuccess(res: String?) {
                        res?.let {
                            logMessage("${TAG}: 日志上传成功->$res", LogLevel.INFO)
                            callback.onSuccess(res)
                        }
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        error = error ?: BusinessException(throwable?.message)
                        error?.code?.let {
                            logMessage("${TAG}: 日志上传错误->code:${error?.code}, reason:${error?.message
                                    ?: throwable?.message}", LogLevel.ERROR)
                            callback.onFailure(error?.code, error?.message ?: throwable?.message)
                        }
                    }
                })
        return EduError(-1, "")
    }

    override fun onConnectionStateChanged(p0: Int, p1: Int) {
        logMessage("${TAG}: RTM连接状态发生改变->state:$p0,reason:$p1", LogLevel.INFO)
        /**断线重连之后，同步至每一个教室*/
        eduRooms?.forEach {
            if (rtmConnectState.isReconnecting() &&
                    p0 == RtmStatusCode.ConnectionState.CONNECTION_STATE_CONNECTED) {
                logMessage("${TAG}: RTM断线重连，请求教室${it.getRoomInfo().roomUuid}内丢失的消息", LogLevel.INFO)
                (it as EduRoomImpl).syncSession.fetchLostSequence(object : EduCallback<Unit> {
                    override fun onSuccess(res: Unit?) {
                        /*断线重连之后，数据同步成功之后再把重连成功的事件回调出去*/
                        it.eventListener?.onConnectionStateChanged(Convert.convertConnectionState(p0), it)
                    }

                    override fun onFailure(code: Int, reason: String?) {
                        /**无限重试，保证数据同步成功*/
                        it.syncSession.fetchLostSequence(this)
                    }
                })
            } else {
                it.eventListener?.onConnectionStateChanged(Convert.convertConnectionState(p0), it)
            }
        }
        rtmConnectState.lastConnectionState = p0
    }

    override fun onPeerMsgReceived(p0: RtmMessage?, p1: String?) {
        logMessage("${TAG}: 收到点对点消息->${Gson().toJson(p0)}", LogLevel.INFO)
        /**RTM保证peerMsg能到达,不用走同步检查(seq衔接性检查)*/
        p0?.text?.let {
            eduRooms?.forEach {
                (it as EduRoomImpl).cmdDispatch.dispatchPeerMsg(p0.text, eduManagerEventListener)
            }
        }
    }
}
