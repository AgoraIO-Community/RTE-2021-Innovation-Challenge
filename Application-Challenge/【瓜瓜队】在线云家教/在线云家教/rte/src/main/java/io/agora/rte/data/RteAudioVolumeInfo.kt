package io.agora.rte.data

import io.agora.rtc.IRtcEngineEventHandler

data class RteAudioVolumeInfo(
        var uid: Int,
        var volume: Int,
        var vad: Int,
        var channelId: String) {

    companion object {
        fun convert(info: IRtcEngineEventHandler.AudioVolumeInfo): RteAudioVolumeInfo {
            return RteAudioVolumeInfo(info.uid, info.volume, info.vad, info.channelId)
        }

        fun convert(infos: MutableList<IRtcEngineEventHandler.AudioVolumeInfo>):
                MutableList<RteAudioVolumeInfo> {
            val list = mutableListOf<RteAudioVolumeInfo>()
            infos.forEach {
                val element = RteAudioVolumeInfo(it.uid, it.volume, it.vad, it.channelId)
                list.add(element)
            }
            return list
        }
    }
}