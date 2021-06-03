package io.agora.rte.data

import io.agora.rtc.models.ChannelMediaOptions

class RteChannelMediaOptions(autoSubscribeAudio: Boolean, autoSubscribeVideo: Boolean) :
        ChannelMediaOptions() {

    init {
        this.autoSubscribeAudio = autoSubscribeAudio
        this.autoSubscribeVideo = autoSubscribeVideo
    }
}