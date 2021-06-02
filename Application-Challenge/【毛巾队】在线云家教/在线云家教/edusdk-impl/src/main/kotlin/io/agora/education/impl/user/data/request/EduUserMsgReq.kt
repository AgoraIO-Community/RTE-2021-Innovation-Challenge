package io.agora.education.impl.user.data.request

/**发送peerMsg
 * @param userId 接收方的userId*/
internal open class EduUserMsgReq constructor(
        var message: String) {
}

internal class EduUserChatMsgReq(
        msg: String,
        val type: Int) : EduUserMsgReq(msg)