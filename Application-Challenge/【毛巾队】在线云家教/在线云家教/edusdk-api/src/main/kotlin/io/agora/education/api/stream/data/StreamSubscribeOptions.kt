package io.agora.education.api.stream.data

enum class VideoStreamType(value: Int) {
    HIGH(0),
    LOW(1)
}

data class StreamSubscribeOptions(
        var subscribeAudio: Boolean = true,
        var subscribeVideo: Boolean = true,
        var videoStreamType: VideoStreamType
)
