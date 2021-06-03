package io.agora.rte.listener

interface RteAudioMixingListener {
    fun onAudioMixingFinished()

    fun onAudioMixingStateChanged(state: Int, errorCode: Int)
}