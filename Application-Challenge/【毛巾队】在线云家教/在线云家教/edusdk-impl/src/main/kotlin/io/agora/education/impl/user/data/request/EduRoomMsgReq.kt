package io.agora.education.impl.user.data.request

/**发送channelMsg*/
internal open class EduRoomMsgReq(val message: String)

internal class EduRoomChatMsgReq(
        message: String,
        val type: Int) : EduRoomMsgReq(message)