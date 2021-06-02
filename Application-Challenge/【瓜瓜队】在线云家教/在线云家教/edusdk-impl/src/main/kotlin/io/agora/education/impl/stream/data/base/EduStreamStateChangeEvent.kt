package io.agora.education.impl.stream.data.base

import io.agora.education.api.stream.data.EduStreamEvent
import io.agora.education.api.stream.data.EduStreamStateChangeType

class EduStreamStateChangeEvent(
        val event: EduStreamEvent,
        val type: EduStreamStateChangeType
) {
}