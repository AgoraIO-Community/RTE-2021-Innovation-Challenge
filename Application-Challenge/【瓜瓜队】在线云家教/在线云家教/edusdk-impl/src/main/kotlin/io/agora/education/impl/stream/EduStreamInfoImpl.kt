package io.agora.education.impl.stream

import io.agora.education.api.stream.data.EduStreamInfo
import io.agora.education.api.stream.data.VideoSourceType
import io.agora.education.api.user.data.EduBaseUserInfo
import io.agora.education.api.user.data.EduUserInfo

internal class EduStreamInfoImpl(
        streamUuid: String,
        streamName: String?,
        videoSourceType: VideoSourceType,
        hasVideo: Boolean,
        hasAudio: Boolean,
        publisher: EduBaseUserInfo,
        var updateTime: Long?
) : EduStreamInfo(streamUuid, streamName, videoSourceType, hasVideo, hasAudio, publisher) {
}
