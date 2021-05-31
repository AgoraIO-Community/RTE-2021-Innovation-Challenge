package com.dong.circlelive.live.model

import androidx.annotation.Keep
import cn.leancloud.AVUser
import com.dong.circlelive.appContext

/**
 * Create by dooze on 2021/5/29  2:00 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

@Keep
data class LiveMessage(
    val content: String,
    val userId: String,
    val username: String,
    val type: Int = LiveMessageType.NORMAL.value,
    val createdTime: Long = System.currentTimeMillis()
) {
    companion object
}

fun LiveMessage.Companion.create(content: String, type: Int = LiveMessageType.NORMAL.value): LiveMessage {
    val user = AVUser.currentUser()
    return LiveMessage(content, user.objectId, user.username, type)
}

fun LiveMessage.toJson(): String {
    return appContext.gson.toJson(this)
}

fun String.toLiveMessage(): LiveMessage = appContext.gson.fromJson(this, LiveMessage::class.java)

enum class LiveMessageType(val value: Int) {
    NORMAL(0),
    LIVING_START(1)
}
