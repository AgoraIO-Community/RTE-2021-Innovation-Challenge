package io.agora.rte.data

enum class RteAudioMixingStateCode(val value: Int) {
    /*音乐文件正常播放*/
    MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY(710),

    /*音乐文件暂停播放*/
    MEDIA_ENGINE_AUDIO_EVENT_MIXING_PAUSED(711),

    /*音乐文件停止播放*/
    MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED(713),

    /*音乐文件报错*/
    MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR(714);

    companion object {
        fun convert(value: Int): RteAudioMixingStateCode {
            return when (value) {
                MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY.value -> {
                    return MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY
                }
                MEDIA_ENGINE_AUDIO_EVENT_MIXING_PAUSED.value -> {
                    return MEDIA_ENGINE_AUDIO_EVENT_MIXING_PAUSED
                }
                MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED.value -> {
                    return MEDIA_ENGINE_AUDIO_EVENT_MIXING_STOPPED
                }
                MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR.value -> {
                    return MEDIA_ENGINE_AUDIO_EVENT_MIXING_ERROR
                }
                else -> {
                    return MEDIA_ENGINE_AUDIO_EVENT_MIXING_PLAY
                }
            }
        }
    }
}