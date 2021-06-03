package io.agora.education.api.statistics

enum class ConnectionState(var value: Int) {
    DISCONNECTED(1),
    CONNECTING(2),
    CONNECTED(3),
    RECONNECTING(4),
    ABORTED(5)
}
