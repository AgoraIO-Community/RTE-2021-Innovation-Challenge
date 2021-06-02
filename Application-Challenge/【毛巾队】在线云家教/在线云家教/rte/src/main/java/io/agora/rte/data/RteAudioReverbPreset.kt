package io.agora.rte.data

enum class RteAudioReverbPreset(val value: Int) {
    /*原声，即关闭本地语音混响*/
    AUDIO_REVERB_OFF(0),

    /*流行*/
    AUDIO_REVERB_POPULAR(1),

    /*R&B*/
    AUDIO_REVERB_RNB(2),

    /*摇滚*/
    AUDIO_REVERB_ROCK(3),

    /*嘻哈*/
    AUDIO_REVERB_HIPHOP(4),

    /*演唱会*/
    AUDIO_REVERB_VOCAL_CONCERT(5),

    /*KTV*/
    AUDIO_REVERB_KTV(6),

    /*录音棚*/
    AUDIO_REVERB_STUDIO(7)
}