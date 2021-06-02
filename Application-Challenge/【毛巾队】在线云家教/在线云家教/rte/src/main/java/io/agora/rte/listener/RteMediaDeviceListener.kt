package io.agora.rte.listener

interface RteMediaDeviceListener {
    fun onAudioRouteChanged(routing: Int)
}