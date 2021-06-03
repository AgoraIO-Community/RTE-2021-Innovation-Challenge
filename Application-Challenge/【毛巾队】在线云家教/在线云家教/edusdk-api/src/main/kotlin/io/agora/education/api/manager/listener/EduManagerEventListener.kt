package io.agora.education.api.manager.listener

import io.agora.education.api.message.EduActionMessage
import io.agora.education.api.message.EduChatMsg
import io.agora.education.api.message.EduMsg

interface EduManagerEventListener {

    fun onUserMessageReceived(message: EduMsg)

    fun onUserChatMessageReceived(chatMsg: EduChatMsg)

    /*一期教育SDK没有这个方法，只是给娱乐使用*/
    fun onUserActionMessageReceived(actionMessage: EduActionMessage)
}