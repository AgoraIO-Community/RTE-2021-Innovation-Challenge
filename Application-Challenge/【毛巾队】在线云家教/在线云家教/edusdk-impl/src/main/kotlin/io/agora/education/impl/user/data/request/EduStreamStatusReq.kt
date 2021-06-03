package io.agora.education.impl.user.data.request

/**
 * @param videoSourceType 视频流来源, 0: none, 1: camera, 2: screen
 * @param audioSourceType 音频流来源, 0: none, 1: mic
 * @param videoState 0关 1开 2禁
 * @param audioState 0关 1开 2禁*/
class EduStreamStatusReq(
        val streamName: String?,
        val videoSourceType: Int,
        val audioSourceType: Int,
        val videoState: Int,
        val audioState: Int
) {
}