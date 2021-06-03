package io.agora.rte.data

enum class RteAudioMixingErrorCode(val value: Int) {
    /*音乐文件打开出错*/
    MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN(701),

    /*音乐文件打开太频繁*/
    MEDIA_ENGINE_AUDIO_ERROR_MIXING_TOO_FREQUENT(702),

    /*音乐文件播放异常中断*/
    MEDIA_ENGINE_AUDIO_EVENT_MIXING_INTERRUPTED_EOF(703);

    companion object {
        fun convert(value: Int): RteAudioMixingErrorCode {
            return when (value) {
                MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN.value -> {
                    return MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN
                }
                MEDIA_ENGINE_AUDIO_ERROR_MIXING_TOO_FREQUENT.value -> {
                    return MEDIA_ENGINE_AUDIO_ERROR_MIXING_TOO_FREQUENT
                }
                MEDIA_ENGINE_AUDIO_EVENT_MIXING_INTERRUPTED_EOF.value -> {
                    return MEDIA_ENGINE_AUDIO_EVENT_MIXING_INTERRUPTED_EOF
                }
                else -> {
                    return MEDIA_ENGINE_AUDIO_ERROR_MIXING_OPEN
                }
            }
        }
    }
}