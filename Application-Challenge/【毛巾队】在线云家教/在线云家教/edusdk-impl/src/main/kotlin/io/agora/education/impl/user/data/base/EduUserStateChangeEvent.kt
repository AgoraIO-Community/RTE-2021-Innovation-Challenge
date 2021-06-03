package io.agora.education.impl.user.data.base

import io.agora.education.api.user.data.EduUserEvent
import io.agora.education.api.user.data.EduUserStateChangeType

internal class EduUserStateChangeEvent(
        val event: EduUserEvent,
        val type: EduUserStateChangeType
) {
}