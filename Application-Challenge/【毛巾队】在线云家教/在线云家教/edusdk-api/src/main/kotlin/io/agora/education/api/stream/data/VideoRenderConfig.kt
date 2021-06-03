package io.agora.education.api.stream.data

enum class RenderMode(var value: Int) {
    HIDDEN(1),
    FIT(2)
}

data class VideoRenderConfig(
        var renderMode: RenderMode = RenderMode.HIDDEN
)
