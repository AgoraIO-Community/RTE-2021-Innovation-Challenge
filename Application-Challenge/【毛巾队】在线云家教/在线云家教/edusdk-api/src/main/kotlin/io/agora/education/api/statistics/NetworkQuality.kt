package io.agora.education.api.statistics

enum class NetworkQuality(var value: Int) {
    UNKNOWN(-1),
    GOOD(1),
    POOR(2),
    BAD(3)
}
