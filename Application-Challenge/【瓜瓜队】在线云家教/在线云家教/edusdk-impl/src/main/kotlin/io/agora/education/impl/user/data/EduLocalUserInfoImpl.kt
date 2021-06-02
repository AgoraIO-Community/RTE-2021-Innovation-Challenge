package io.agora.education.impl.user.data

import io.agora.education.api.stream.data.EduStreamEvent
import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.user.data.EduLocalUserInfo
import io.agora.education.api.user.data.EduUserInfo
import io.agora.education.api.user.data.EduUserRole
import io.agora.education.impl.stream.EduStreamInfoImpl

internal class EduLocalUserInfoImpl(
        userUuid: String,
        userName: String,
        role: EduUserRole,
        isChatAllowed: Boolean,
        userToken: String?,
        streams: MutableList<EduStreamEvent>,
        val updateTime: Long?)
    : EduLocalUserInfo(userUuid, userName, role, isChatAllowed, userToken, streams) {
}