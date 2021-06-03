package io.agora.education.impl.user.data.request

/**三个参数的状态只要不是disable就等同于enable*/
//class EduRoomMuteStateReq(chatState: EduChatState?, videoState: EduVideoState?, audioState: EduAudioState?) {
//    private var muteChat: String? = null
//    private var muteVideo: String? = null
//    private var muteAudio: String? = null
//
//    init {
//        chatState?.let {
//            var chat: MutableMap<String, Int> = mutableMapOf()
//            chat[EduUserRoleStr.broadcaster.value] = chatState.value
//            chat[EduUserRoleStr.audience.value] = chatState.value
//            this.muteChat = Gson().toJson(chat)
//        }
//        videoState?.let {
//            var video: MutableMap<String, Int> = mutableMapOf()
//            video[EduUserRoleStr.broadcaster.value] = videoState.value
//            video[EduUserRoleStr.audience.value] = videoState.value
//            this.muteVideo = Gson().toJson(video)
//        }
//        audioState?.let {
//            var audio: MutableMap<String, Int> = mutableMapOf()
//            audio[EduUserRoleStr.broadcaster.value] = audioState.value
//            audio[EduUserRoleStr.audience.value] = audioState.value
//            this.muteAudio = Gson().toJson(audio)
//        }
//    }
//}

class EduRoomMuteStateReq(private val muteChat: RoleMuteConfig?, private val muteVideo: RoleMuteConfig?,
                          private val muteAudio: RoleMuteConfig?) {
}


class RoleMuteConfig(val host: String?, val broadcaster: String?, val audience: String?){

}