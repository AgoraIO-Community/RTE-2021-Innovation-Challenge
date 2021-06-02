package io.agora.education.impl.cmd.bean

class CMDResponseBody<T>(
        var cmd: Int,
        val version: Int,
        val timestamp: Long,
        val requestId: String?,
        val sequence: Int,
        val data: T) {
}