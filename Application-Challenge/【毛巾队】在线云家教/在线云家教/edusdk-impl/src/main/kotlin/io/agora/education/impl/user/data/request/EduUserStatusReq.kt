package io.agora.education.impl.user.data.request

/**
 * 更新用户状态的参数类
 * @param muteChat 1禁 0不禁*/
class EduUserStatusReq(
        val userName: String,
        val muteChat: Int,
        val role: String
) {
}