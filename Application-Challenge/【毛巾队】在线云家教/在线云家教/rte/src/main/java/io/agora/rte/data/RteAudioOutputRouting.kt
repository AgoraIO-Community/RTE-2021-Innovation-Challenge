package io.agora.rte.data

enum class RteAudioOutputRouting(val value: Int) {
    /*使用默认的音频路由*/
    AUDIO_ROUTE_DEFAULT(-1),

    /*使用耳机为语音路由*/
    AUDIO_ROUTE_HEADSET(0),

    /*使用听筒为语音路由*/
    AUDIO_ROUTE_EARPIECE(1),

    /*使用不带麦的耳机为语音路由*/
    AUDIO_ROUTE_HEADSETNOMIC(2),

    /*使用手机的扬声器为语音路由*/
    AUDIO_ROUTE_SPEAKERPHONE(3),

    /*使用外接的扬声器为语音路由*/
    AUDIO_ROUTE_LOUDSPEAKER(4),

    /*使用蓝牙耳机为语音路由*/
    AUDIO_ROUTE_HEADSETBLUETOOTH(5);

    companion object {
        fun convert(value: Int): RteAudioOutputRouting {
            return when (value) {
                AUDIO_ROUTE_DEFAULT.value -> {
                    return AUDIO_ROUTE_DEFAULT
                }
                AUDIO_ROUTE_HEADSET.value -> {
                    return AUDIO_ROUTE_HEADSET
                }
                AUDIO_ROUTE_EARPIECE.value -> {
                    return AUDIO_ROUTE_EARPIECE
                }
                AUDIO_ROUTE_HEADSETNOMIC.value -> {
                    return AUDIO_ROUTE_HEADSETNOMIC
                }
                AUDIO_ROUTE_SPEAKERPHONE.value -> {
                    return AUDIO_ROUTE_SPEAKERPHONE
                }
                AUDIO_ROUTE_LOUDSPEAKER.value -> {
                    return AUDIO_ROUTE_LOUDSPEAKER
                }
                AUDIO_ROUTE_HEADSETBLUETOOTH.value -> {
                    return AUDIO_ROUTE_HEADSETBLUETOOTH
                }
                else -> {
                    return AUDIO_ROUTE_DEFAULT
                }
            }
        }
    }
}