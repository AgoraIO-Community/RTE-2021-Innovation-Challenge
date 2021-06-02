package io.agora.education.api.user

import android.view.ViewGroup
import io.agora.education.api.EduCallback
import io.agora.education.api.message.EduChatMsg
import io.agora.education.api.message.EduMsg
import io.agora.education.api.room.data.EduError
import io.agora.education.api.stream.data.*
import io.agora.education.api.user.data.EduLocalUserInfo
import io.agora.education.api.user.data.EduStartActionConfig
import io.agora.education.api.user.data.EduStopActionConfig
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.api.user.listener.EduUserEventListener

interface EduUser {
    var userInfo: EduLocalUserInfo
    var videoEncoderConfig: VideoEncoderConfig

    var eventListener: EduUserEventListener?

    fun initOrUpdateLocalStream(options: LocalStreamInitOptions, callback: EduCallback<EduStreamInfo>)

    fun switchCamera(): EduError

    fun subscribeStream(stream: EduStreamInfo, options: StreamSubscribeOptions, callback: EduCallback<Unit>)

    fun unSubscribeStream(stream: EduStreamInfo, options: StreamSubscribeOptions, callback: EduCallback<Unit>)

    /**新建流信息*/
    fun publishStream(stream: EduStreamInfo, callback: EduCallback<Boolean>)

    /**mute/unmute*/
    fun muteStream(stream: EduStreamInfo, callback: EduCallback<Boolean>)

    /**删除流信息*/
    fun unPublishStream(stream: EduStreamInfo, callback: EduCallback<Boolean>)

    /**发送自定义消息*/
    fun sendRoomMessage(message: String, callback: EduCallback<EduMsg>)

    /**
     * @param user 消息接收方的userInfo*/
    fun sendUserMessage(message: String, user: EduUserInfo, callback: EduCallback<EduMsg>)

    /**发送聊天消息*/
    fun sendRoomChatMessage(message: String, callback: EduCallback<EduChatMsg>)

    fun sendUserChatMessage(message: String, remoteUser: EduUserInfo, callback: EduCallback<EduChatMsg>)

    /*process action
    * 一期教育SDK没有这个方法，只是给娱乐使用*/
    fun startActionWithConfig(config: EduStartActionConfig, callback: EduCallback<Unit>)
    fun stopActionWithConfig(config: EduStopActionConfig, callback: EduCallback<Unit>)

    fun setStreamView(stream: EduStreamInfo, channelId: String, viewGroup: ViewGroup?,
                      config: VideoRenderConfig = VideoRenderConfig()): EduError

    fun setStreamView(stream: EduStreamInfo, channelId: String, viewGroup: ViewGroup?): EduError

    fun setRoomProperty(property: MutableMap.MutableEntry<String, String>,
                        cause: MutableMap<String, String>, callback: EduCallback<Unit>)

    fun setUserProperty(property: MutableMap.MutableEntry<String, String>,
                        cause: MutableMap<String, String>, targetUser: EduUserInfo, callback: EduCallback<Unit>)
}
