package io.agora.education.impl.user.data

import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.api.user.data.EduUserRole

internal class EduUserInfoImpl(userUuid: String, userName: String, role: EduUserRole, isChatAllowed: Boolean?, val updateTime: Long?)
    : EduUserInfo(userUuid, userName, role, isChatAllowed) {
}