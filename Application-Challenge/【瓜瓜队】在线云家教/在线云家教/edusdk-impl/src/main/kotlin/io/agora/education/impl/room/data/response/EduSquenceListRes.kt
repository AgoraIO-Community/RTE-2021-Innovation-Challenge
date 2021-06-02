package io.agora.education.impl.room.data.response

internal class EduSequenceListRes<T>(
        val total: Int,
        val nextId: Int,
        val list: MutableList<EduSequenceRes<T>>
) {
}

internal class EduSequenceRes<T>(
        val sequence: Int,
        val cmd: Int,
        val version: Int,
        val data: T
)