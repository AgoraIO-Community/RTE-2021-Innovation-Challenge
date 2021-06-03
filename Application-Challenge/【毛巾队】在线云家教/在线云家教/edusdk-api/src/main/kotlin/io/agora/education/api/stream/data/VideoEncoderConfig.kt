package io.agora.education.api.stream.data

enum class OrientationMode {
    ADAPTIVE,
    FIXED_LANDSCAPE,
    FIXED_PORTRAIT
}

enum class DegradationPreference {
    MAINTAIN_QUALITY,
    MAINTAIN_FRAME_RATE,
    MAINTAIN_BALANCED
}

data class VideoEncoderConfig(
        var videoDimensionWidth: Int = 360,
        var videoDimensionHeight: Int = 360,
        var frameRate: Int = 15,
        var bitrate: Int = 0,
        var orientationMode: OrientationMode = OrientationMode.ADAPTIVE,
        var degradationPreference: DegradationPreference = DegradationPreference.MAINTAIN_QUALITY
)
