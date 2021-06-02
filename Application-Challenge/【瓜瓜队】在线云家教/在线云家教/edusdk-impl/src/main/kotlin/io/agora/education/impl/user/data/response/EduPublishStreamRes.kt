package io.agora.education.impl.user.data.response

/**
 * @param streamId 流id
 * @param streamUuid 同一appId下流的唯一id，rtc中uid
 * @param rtcToken 流对应的rtcToken*/
class EduPublishStreamRes constructor(var streamId: String,
                                      var streamUuid: String,
                                      var rtcToken: String) {

}