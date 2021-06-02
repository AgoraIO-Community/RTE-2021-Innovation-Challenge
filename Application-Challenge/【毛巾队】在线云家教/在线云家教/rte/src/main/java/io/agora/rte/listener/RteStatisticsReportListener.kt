package io.agora.rte.listener

import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcChannel

interface RteStatisticsReportListener {
    fun onRtcStats(channel: RtcChannel?, stats: IRtcEngineEventHandler.RtcStats?)

    fun onVideoSizeChanged(channel: RtcChannel?, uid: Int, width: Int, height: Int, rotation: Int)
}