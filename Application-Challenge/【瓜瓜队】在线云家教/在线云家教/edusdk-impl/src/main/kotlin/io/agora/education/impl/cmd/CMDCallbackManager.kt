package io.agora.education.impl.cmd

import io.agora.education.api.manager.listener.EduManagerEventListener
import io.agora.education.api.message.EduActionMessage
import io.agora.education.api.message.EduChatMsg
import io.agora.education.api.message.EduMsg
import io.agora.education.api.room.EduRoom
import io.agora.education.api.room.data.EduRoomChangeType
import io.agora.education.api.stream.data.EduStreamEvent
import io.agora.education.api.stream.data.EduStreamStateChangeType
import io.agora.education.api.user.EduUser
import io.agora.education.api.user.data.EduUserEvent
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.api.user.data.EduUserStateChangeType

internal class CMDCallbackManager {

    fun onRoomStatusChanged(eventEdu: EduRoomChangeType, operatorUser: EduUserInfo?, classRoom: EduRoom) {
        classRoom.eventListener?.onRoomStatusChanged(eventEdu, operatorUser, classRoom)
    }

    fun onRoomPropertyChanged(classRoom: EduRoom, cause: MutableMap<String, Any>?) {
        classRoom.eventListener?.onRoomPropertyChanged(classRoom, cause)
    }

    fun onRoomChatMessageReceived(chatMsg: EduChatMsg, classRoom: EduRoom) {
        classRoom.eventListener?.onRoomChatMessageReceived(chatMsg, classRoom)
    }

    fun onRoomMessageReceived(message: EduMsg, classRoom: EduRoom) {
        classRoom.eventListener?.onRoomMessageReceived(message, classRoom)
    }

    fun onRemoteUsersJoined(users: List<EduUserInfo>, classRoom: EduRoom) {
        classRoom.eventListener?.onRemoteUsersJoined(users, classRoom)
    }

    fun onRemoteStreamsAdded(streamEvents: MutableList<EduStreamEvent>, classRoom: EduRoom) {
        classRoom.eventListener?.onRemoteStreamsAdded(streamEvents, classRoom)
    }

    fun onRemoteUsersLeft(userEvents: MutableList<EduUserEvent>, classRoom: EduRoom) {
        userEvents.forEach {
            classRoom.eventListener?.onRemoteUserLeft(it, classRoom)
        }
    }

    fun onRemoteStreamsRemoved(streamEvents: MutableList<EduStreamEvent>, classRoom: EduRoom) {
        classRoom.eventListener?.onRemoteStreamsRemoved(streamEvents, classRoom)
    }

    fun onRemoteUserPropertiesUpdated(userInfo: EduUserInfo, classRoom: EduRoom,
                                      cause: MutableMap<String, Any>?) {
        classRoom.eventListener?.onRemoteUserPropertyUpdated(userInfo, classRoom, cause)
    }

    fun onRemoteStreamsUpdated(streamEvent: EduStreamEvent, type: EduStreamStateChangeType, classRoom: EduRoom) {
        classRoom.eventListener?.onRemoteStreamUpdated(streamEvent, type, classRoom)
    }

    fun onRemoteUserUpdated(userEvent: EduUserEvent, type: EduUserStateChangeType, classRoom: EduRoom) {
        classRoom.eventListener?.onRemoteUserUpdated(userEvent, type, classRoom)
    }

    fun onLocalUserAdded(userInfo: EduUserInfo, eduUser: EduUser) {
        /**本地用户的online数据不回调出去，仅内部处理*/
    }

    fun onLocalUserUpdated(userEvent: EduUserEvent, type: EduUserStateChangeType, eduUser: EduUser) {
        eduUser.eventListener?.onLocalUserUpdated(userEvent, type)
    }

    fun onLocalUserRemoved(userEvent: EduUserEvent, eduUser: EduUser) {
        /**本地用户的offline数据暂不回调出去，后期会在EduUserEventListener中添加
         * onLocalUserLeft回调来处理此消息(为踢人功能预备)*/
    }

    fun onLocalUserPropertyUpdated(userInfo: EduUserInfo, cause: MutableMap<String, Any>?, eduUser: EduUser) {
        eduUser.eventListener?.onLocalUserPropertyUpdated(userInfo, cause)
    }

    fun onLocalStreamAdded(streamEvent: EduStreamEvent, eduUser: EduUser) {
        eduUser.eventListener?.onLocalStreamAdded(streamEvent)
    }

    fun onLocalStreamUpdated(streamEvent: EduStreamEvent, type: EduStreamStateChangeType, eduUser: EduUser) {
        eduUser.eventListener?.onLocalStreamUpdated(streamEvent, type)
    }

    fun onLocalStreamRemoved(streamEvent: EduStreamEvent, eduUser: EduUser) {
        eduUser.eventListener?.onLocalStreamRemoved(streamEvent)
    }


    fun onUserChatMessageReceived(chatMsg: EduChatMsg, listener: EduManagerEventListener?) {
        listener?.onUserChatMessageReceived(chatMsg)
    }

    fun onUserMessageReceived(message: EduMsg, listener: EduManagerEventListener?) {
        listener?.onUserMessageReceived(message)
    }

    fun onUserActionMessageReceived(actionMsg: EduActionMessage, listener: EduManagerEventListener?) {
        listener?.onUserActionMessageReceived(actionMsg)
    }
}