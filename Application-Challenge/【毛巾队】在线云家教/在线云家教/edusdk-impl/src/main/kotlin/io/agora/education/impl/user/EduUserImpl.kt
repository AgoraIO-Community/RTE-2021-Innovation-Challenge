package io.agora.education.impl.user

import android.util.Log
import android.view.SurfaceView
import android.view.ViewGroup
import android.widget.LinearLayout
import com.google.gson.Gson
import io.agora.Constants.Companion.APPID
import io.agora.Constants.Companion.AgoraLog
import io.agora.education.impl.util.Convert
import io.agora.base.callback.ThrowableCallback
import io.agora.base.network.BusinessException
import io.agora.base.network.ResponseBody
import io.agora.education.api.BuildConfig.API_BASE_URL
import io.agora.education.api.EduCallback
import io.agora.education.api.logger.LogLevel
import io.agora.education.api.message.EduChatMsg
import io.agora.education.api.message.EduChatMsgType
import io.agora.education.api.message.EduMsg
import io.agora.education.api.room.data.EduError
import io.agora.education.api.statistics.AgoraError
import io.agora.education.api.stream.data.*
import io.agora.education.api.user.EduUser
import io.agora.education.api.user.data.EduLocalUserInfo
import io.agora.education.api.user.data.EduStartActionConfig
import io.agora.education.api.user.data.EduStopActionConfig
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.api.user.listener.EduUserEventListener
import io.agora.education.impl.network.RetrofitManager
import io.agora.education.impl.room.EduRoomImpl
import io.agora.education.impl.room.data.request.EduUpdateRoomPropertyReq
import io.agora.education.impl.room.network.RoomService
import io.agora.education.impl.stream.EduStreamInfoImpl
import io.agora.education.impl.stream.network.StreamService
import io.agora.education.impl.user.data.request.*
import io.agora.education.impl.user.data.request.EduRoomChatMsgReq
import io.agora.education.impl.user.data.request.EduRoomMsgReq
import io.agora.education.impl.user.data.request.EduUserMsgReq
import io.agora.education.impl.user.network.UserService
import io.agora.rtc.Constants.CLIENT_ROLE_AUDIENCE
import io.agora.rtc.Constants.CLIENT_ROLE_BROADCASTER
import io.agora.rtc.RtcEngine
import io.agora.rtc.video.VideoCanvas
import io.agora.rte.RteEngineImpl

internal open class EduUserImpl(
        override var userInfo: EduLocalUserInfo
) : EduUser {
    val TAG = EduUserImpl::class.java.simpleName

    override var videoEncoderConfig = VideoEncoderConfig()

    override var eventListener: EduUserEventListener? = null

    lateinit var eduRoom: EduRoomImpl

    private val surfaceViewList = mutableListOf<SurfaceView>()

    override fun initOrUpdateLocalStream(options: LocalStreamInitOptions, callback: EduCallback<EduStreamInfo>) {
        AgoraLog.i("$TAG->开始初始化和更新本地流:${Gson().toJson(options)}")
        RteEngineImpl.setVideoEncoderConfiguration(
                Convert.convertVideoEncoderConfig(videoEncoderConfig))
        RteEngineImpl.enableVideo()
        /**enableCamera和enableMicrophone控制是否打开摄像头和麦克风*/
        RteEngineImpl.enableLocalMedia(options.enableMicrophone, options.enableCamera)

        /**根据当前配置生成一个流信息*/
        val streamInfo = EduStreamInfoImpl(options.streamUuid, options.streamName, VideoSourceType.CAMERA,
                options.enableCamera, options.enableMicrophone, this.userInfo, System.currentTimeMillis())
        callback.onSuccess(streamInfo)
    }

    override fun switchCamera(): EduError {
        val code = RteEngineImpl.switchCamera()
        AgoraLog.i("$TAG->switchCamera:$code")
        return EduError(code, RteEngineImpl.getError(code))
    }

    override fun subscribeStream(stream: EduStreamInfo, options: StreamSubscribeOptions,
                                 callback: EduCallback<Unit>) {
        /**订阅远端流*/
        val uid: Int = (stream.streamUuid.toLong() and 0xffffffffL).toInt()
        Log.e(TAG, "")
        val code = RteEngineImpl.muteRemoteStream(eduRoom.getRoomInfo().roomUuid, uid, !options.subscribeAudio,
                !options.subscribeVideo)
        AgoraLog.i("$TAG->subscribeStream: audio:${options.subscribeAudio}," +
                "video:${options.subscribeVideo}, code: $code")
        if (code == RteEngineImpl.ok()) {
            callback.onSuccess(Unit)
        } else {
            callback.onFailure(code, RteEngineImpl.getError(code))
        }
    }

    override fun unSubscribeStream(stream: EduStreamInfo, options: StreamSubscribeOptions,
                                   callback: EduCallback<Unit>) {
        val uid: Int = (stream.streamUuid.toLong() and 0xffffffffL).toInt()
        val code = RteEngineImpl.muteRemoteStream(eduRoom.getRoomInfo().roomUuid, uid, !options.subscribeAudio,
                !options.subscribeVideo)
        AgoraLog.i("$TAG->unSubscribeStream: streamUuid: ${stream.streamUuid},audio:${options.subscribeAudio}," +
                "video:${options.subscribeVideo},code: $code")
        if (code == RteEngineImpl.ok()) {
            callback.onSuccess(Unit)
        } else {
            callback.onFailure(code, RteEngineImpl.getError(code))
        }
    }

    override fun publishStream(streamInfo: EduStreamInfo, callback: EduCallback<Boolean>) {
        /**设置角色*/
        RteEngineImpl.setClientRole(eduRoom.getRoomInfo().roomUuid, CLIENT_ROLE_BROADCASTER)
        /**改变流状态的参数*/
        val eduStreamStatusReq = EduStreamStatusReq(streamInfo.streamName, streamInfo.videoSourceType.value,
                AudioSourceType.MICROPHONE.value, if (streamInfo.hasVideo) 1 else 0,
                if (streamInfo.hasAudio) 1 else 0)
        AgoraLog.logMsg("$TAG->新建流: ${Gson().toJson(streamInfo)}", LogLevel.INFO.value)
        RetrofitManager.instance()!!.getService(API_BASE_URL, StreamService::class.java)
                .createStream(APPID, eduRoom.getRoomInfo().roomUuid, userInfo.userUuid,
                        streamInfo.streamUuid, eduStreamStatusReq)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        AgoraLog.logMsg("$TAG->发流状态: streamUuid: ${streamInfo.streamUuid}," +
                                "${streamInfo.hasAudio},${streamInfo.hasVideo}",
                                LogLevel.INFO.value)
                        RteEngineImpl.muteLocalStream(!streamInfo.hasAudio, !streamInfo.hasVideo)
                        RteEngineImpl.publish(eduRoom.getRoomInfo().roomUuid)
                        callback.onSuccess(true)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun muteStream(streamInfo: EduStreamInfo, callback: EduCallback<Boolean>) {
        val index = Convert.streamExistsInList(streamInfo, eduRoom.getCurStreamList())
        if (index > -1) {
            val oldStream = eduRoom.getCurStreamList()[index]
            if (oldStream == streamInfo) {
                AgoraLog.e("$TAG->要更改的流的目的状态不变，直接返回")
                callback.onSuccess(true)
            } else {
                AgoraLog.i("$TAG->开始更新本地存在的流信息,streamUuid: + ${streamInfo.streamUuid}," +
                        "流状态更改至:${streamInfo.hasAudio},${streamInfo.hasVideo}")
                /**设置角色*/
                RteEngineImpl.setClientRole(eduRoom.getRoomInfo().roomUuid, CLIENT_ROLE_BROADCASTER)
                /**改变流状态的参数*/
                val eduStreamStatusReq = EduStreamStatusReq(streamInfo.streamName, streamInfo.videoSourceType.value,
                        AudioSourceType.MICROPHONE.value, if (streamInfo.hasVideo) 1 else 0,
                        if (streamInfo.hasAudio) 1 else 0)
                RteEngineImpl.muteLocalStream(!streamInfo.hasAudio, !streamInfo.hasVideo)
                RteEngineImpl.publish(eduRoom.getRoomInfo().roomUuid)
                RetrofitManager.instance()!!.getService(API_BASE_URL, StreamService::class.java)
                        .updateStreamInfo(APPID, eduRoom.getRoomInfo().roomUuid, userInfo.userUuid,
                                streamInfo.streamUuid, eduStreamStatusReq)
                        .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                            override fun onSuccess(res: ResponseBody<String>?) {
//                                (streamInfo as EduStreamInfoImpl).updateTime = res?.timeStamp
                                AgoraLog.i("$TAG->流信息更新成功,streamUuid: + ${streamInfo.streamUuid}")
                                callback.onSuccess(true)
                            }

                            override fun onFailure(throwable: Throwable?) {
                                AgoraLog.e("$TAG->流信息更新失败,streamUuid: + ${streamInfo.streamUuid}")
                                var error = throwable as? BusinessException
                                callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                        error?.message ?: throwable?.message)
                            }
                        }))
            }
        } else {
            AgoraLog.e("$TAG->要更改的流信息不存在于本地,streamUuid: + ${streamInfo.streamUuid}")
        }
    }

    override fun unPublishStream(streamInfo: EduStreamInfo, callback: EduCallback<Boolean>) {
        Log.e("EduUserImpl", "删除流")
        AgoraLog.i("$TAG->删除流:${Gson().toJson(streamInfo)}")
        RetrofitManager.instance()!!.getService(API_BASE_URL, StreamService::class.java)
                .deleteStream(APPID, eduRoom.getRoomInfo().roomUuid, userInfo.userUuid,
                        streamInfo.streamUuid)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        RteEngineImpl.muteLocalStream(muteAudio = true, muteVideo = true)
                        RteEngineImpl.unpublish(eduRoom.getRoomInfo().roomUuid)
                        /**设置角色*/
                        RteEngineImpl.setClientRole(eduRoom.getRoomInfo().roomUuid, CLIENT_ROLE_AUDIENCE)
                        callback.onSuccess(true)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun sendRoomMessage(message: String, callback: EduCallback<EduMsg>) {
        val roomMsgReq = EduRoomMsgReq(message)
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .sendChannelCustomMessage(APPID, eduRoom.getRoomInfo().roomUuid, roomMsgReq)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        val textMessage = EduMsg(userInfo, message)
                        callback.onSuccess(textMessage)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun sendUserMessage(message: String, remoteUser: EduUserInfo, callback: EduCallback<EduMsg>) {
        val userMsgReq = EduUserMsgReq(message)
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .sendPeerCustomMessage(APPID, eduRoom.getRoomInfo().roomUuid, remoteUser.userUuid, userMsgReq)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        val textMessage = EduMsg(userInfo, message)
                        callback.onSuccess(textMessage)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun sendRoomChatMessage(message: String, callback: EduCallback<EduChatMsg>) {
        val roomChatMsgReq = EduRoomChatMsgReq(message, EduChatMsgType.Text.value)
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .sendRoomChatMsg(eduRoom.getLocalUser().userInfo.userToken!!, APPID,
                        eduRoom.getRoomInfo().roomUuid, roomChatMsgReq)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        val textMessage = EduChatMsg(userInfo, message, EduChatMsgType.Text.value)
                        callback.onSuccess(textMessage)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun sendUserChatMessage(message: String, remoteUser: EduUserInfo, callback: EduCallback<EduChatMsg>) {
        val userChatMsgReq = EduUserChatMsgReq(message, EduChatMsgType.Text.value)
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .sendPeerChatMsg(APPID, eduRoom.getRoomInfo().roomUuid, remoteUser.userUuid, userChatMsgReq)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        val textMessage = EduChatMsg(userInfo, message, EduChatMsgType.Text.value)
                        callback.onSuccess(textMessage)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun startActionWithConfig(config: EduStartActionConfig, callback: EduCallback<Unit>) {
        val startActionReq = EduStartActionReq(config.action.value, config.toUser.userUuid,
                userInfo.userUuid, config.timeout, config.payload)
        RetrofitManager.instance()!!.getService(API_BASE_URL, UserService::class.java)
                .startAction(APPID, config.processUuid, startActionReq)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun stopActionWithConfig(config: EduStopActionConfig, callback: EduCallback<Unit>) {
        val stopAction = EduStopActionReq(config.action.value, config.payload)
        RetrofitManager.instance()!!.getService(API_BASE_URL, UserService::class.java)
                .stopAction(APPID, config.processUuid, stopAction)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    /**
     * @param viewGroup 视频画面的父布局(在UI布局上最好保持独立)*/
    override fun setStreamView(stream: EduStreamInfo, channelId: String, viewGroup: ViewGroup?,
                               config: VideoRenderConfig): EduError {
        val videoCanvas: VideoCanvas
        if (viewGroup == null) {
            /**remove掉当前流对应的surfaceView*/
            val uid: Int = (stream.streamUuid.toLong() and 0xffffffffL).toInt()
            videoCanvas = VideoCanvas(null, config.renderMode.value, uid)
            val iterable = surfaceViewList.iterator()
            while (iterable.hasNext()) {
                val surfaceView = iterable.next()
                if (stream.hashCode() == surfaceView.tag && surfaceView.parent != null) {
                    (surfaceView.parent as ViewGroup).removeView(surfaceView)
                }
                iterable.remove()
            }
        } else {
            checkAndRemoveSurfaceView(stream.hashCode())?.let {
                viewGroup.removeView(it)
            }
            val appContext = viewGroup.context.applicationContext
            val surfaceView = RtcEngine.CreateRendererView(appContext)
            surfaceView.tag = stream.hashCode()
            surfaceView.setZOrderMediaOverlay(true)
            val layoutParams = ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
            surfaceView.layoutParams = layoutParams
            val uid: Int = (stream.streamUuid.toLong() and 0xffffffffL).toInt()
            videoCanvas = VideoCanvas(surfaceView, config.renderMode.value, channelId, uid)
            viewGroup.addView(surfaceView)
            surfaceViewList.add(surfaceView)
        }
        var code: Int
        if (stream.publisher.userUuid == this.userInfo.userUuid) {
            code = RteEngineImpl.setupLocalVideo(videoCanvas)
            if (code == 0) {
                Log.e("EduUserImpl", "setupLocalVideo成功")
            }
        } else {
            code = RteEngineImpl.setupRemoteVideo(videoCanvas)
            if (code == 0) {
                Log.e("EduUserImpl", "setupRemoteVideo成功")
            }
        }
        return EduError(code, RteEngineImpl.getError(code))
    }

    override fun setStreamView(stream: EduStreamInfo, channelId: String, viewGroup: ViewGroup?): EduError {
        /*屏幕分享使用fit模式，尽可能的保持画面完整*/
        val config = if (stream.videoSourceType == VideoSourceType.SCREEN) RenderMode.FIT else
            RenderMode.HIDDEN
        return setStreamView(stream, channelId, viewGroup, VideoRenderConfig(config))
    }

    internal fun removeAllSurfaceView() {
        AgoraLog.w("$TAG->清除所有SurfaceView")
        if (surfaceViewList.size > 0) {
            surfaceViewList.forEach {
                val parent = it.parent;
                if (parent != null && parent is ViewGroup) {
                    parent.removeView(it)
                }
            }
        }
    }

    private fun checkAndRemoveSurfaceView(tag: Int): SurfaceView? {
        for (surfaceView in surfaceViewList) {
            if (surfaceView.tag == tag) {
                surfaceViewList.remove(surfaceView)
                return surfaceView
            }
        }
        return null
    }

    override fun setRoomProperty(property: MutableMap.MutableEntry<String, String>,
                                 cause: MutableMap<String, String>, callback: EduCallback<Unit>) {
        val req = EduUpdateRoomPropertyReq(property.value, cause)
        RetrofitManager.instance()!!.getService(API_BASE_URL, RoomService::class.java)
                .addRoomProperty(APPID, eduRoom.getRoomInfo().roomUuid, property.key, req)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }

    override fun setUserProperty(property: MutableMap.MutableEntry<String, String>,
                                 cause: MutableMap<String, String>, targetUser: EduUserInfo,
                                 callback: EduCallback<Unit>) {
        val req = EduUpdateUserPropertyReq(property.value, cause)
        RetrofitManager.instance()!!.getService(API_BASE_URL, UserService::class.java)
                .addProperty(APPID, eduRoom.getRoomInfo().roomUuid, targetUser.userUuid, property.key,
                        req)
                .enqueue(RetrofitManager.Callback(0, object : ThrowableCallback<ResponseBody<String>> {
                    override fun onSuccess(res: ResponseBody<String>?) {
                        callback.onSuccess(Unit)
                    }

                    override fun onFailure(throwable: Throwable?) {
                        var error = throwable as? BusinessException
                        callback.onFailure(error?.code ?: AgoraError.INTERNAL_ERROR.value,
                                error?.message ?: throwable?.message)
                    }
                }))
    }
}
