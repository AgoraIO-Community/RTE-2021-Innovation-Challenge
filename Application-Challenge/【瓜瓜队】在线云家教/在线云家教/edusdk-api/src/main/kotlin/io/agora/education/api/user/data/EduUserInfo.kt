package io.agora.education.api.user.data

import io.agora.education.api.stream.data.EduStreamEvent

enum class EduUserRole(var value: Int) {
    EduRoleTypeInvalid(0),
    TEACHER(1),
    STUDENT(2),
    ASSISTANT(3)
}

open class EduBaseUserInfo(
        val userUuid: String,
        val userName: String,
        val role: EduUserRole) {

    override fun equals(other: Any?): Boolean {
        if (other == null || other !is EduBaseUserInfo) {
            return false
        }
        return (other.userUuid == this.userUuid && other.userName == this.userName && other.role == this.role)
    }

    override fun hashCode(): Int {
        val var10000 = userUuid
        var var1 = (var10000?.hashCode() ?: 0) * 31
        val var10001 = userName
        var1 = (var1 + (var10001?.hashCode() ?: 0)) * 31
        val var2 = role
        var1 = (var1 + (var2?.hashCode() ?: 0)) * 31
        return var1
    }

    fun copy(): EduBaseUserInfo {
        return EduBaseUserInfo(this.userUuid, this.userName, this.role)
    }
}

open class EduUserInfo(
        userUuid: String,
        userName: String,
        role: EduUserRole,
        var isChatAllowed: Boolean?
) : EduBaseUserInfo(userUuid, userName, role) {
    /**用户主流的uuid(无主流则为null)*/
    lateinit var streamUuid: String
    var userProperties: MutableMap<String, Any> = mutableMapOf()
}

open class EduLocalUserInfo(
        userUuid: String,
        userName: String,
        role: EduUserRole,
        isChatAllowed: Boolean?,
        var userToken: String?,
        var streams: MutableList<EduStreamEvent>
) : EduUserInfo(userUuid, userName, role, isChatAllowed)

enum class EduChatState(var value: Int) {
    NotAllow(1),
    Allow(0)
}

enum class EduUserStateChangeType {
    Chat,
}