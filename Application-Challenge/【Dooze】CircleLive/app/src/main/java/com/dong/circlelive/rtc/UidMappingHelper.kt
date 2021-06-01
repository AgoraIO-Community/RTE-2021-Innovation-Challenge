package com.dong.circlelive.rtc

import cn.leancloud.AVUser
import com.dong.circlelive.live
import io.agora.rtc.models.UserInfo

class UidMappingHelper {

    private val rtcEngine = requireNotNull(live.rtcEngine)
    private val uidAccountMap = hashMapOf<Int, String>()
    private val pendingEventList = mutableListOf<UidEvent>()

    fun toUserId(uid: Int, eventTag: String? = null): String? {
        if (uid == 0) return AVUser.currentUser().objectId
        if (!uidAccountMap[uid].isNullOrEmpty()) {
            return uidAccountMap[uid]
        }
        val userInfo = UserInfo()
        rtcEngine.getUserInfoByUid(uid, userInfo)
        if (userInfo.userAccount.isNullOrEmpty()) {
            if (eventTag.isNullOrEmpty()) {
                return null
            }
            val event = UidEvent(uid, eventTag)
            pendingEventList.removeAll { it == event }
            pendingEventList.add(event)
            return null
        }
        uidAccountMap[uid] = userInfo.userAccount
        return userInfo.userAccount
    }

    fun mapToUid(userId: String): Int? {
        if (userId == AVUser.currentUser().objectId) return 0
        val entry = uidAccountMap.entries.find {
            it.value == userId
        }
        if (entry != null) {
            return entry.key
        }
        val userInfo = UserInfo()
        val code = rtcEngine.getUserInfoByUserAccount(userId, userInfo)
        return if (code == 0) {
            uidAccountMap[userInfo.uid] = userId
            userInfo.uid
        } else {
            null
        }
    }

    fun onUserInfoUpdated(uid: Int, userInfo: UserInfo, handle: ((event: UidEvent, useId: String) -> Unit)) {
        uidAccountMap[uid] = userInfo.userAccount
        val eventList = pendingEventList.toList()
        if (eventList.isNotEmpty()) {
            eventList.forEach { event ->
                val userId = uidAccountMap[uid]
                if (event.uid == event.uid && !userId.isNullOrEmpty()) {
                    pendingEventList.remove(event)
                    handle.invoke(event, userId)
                }
            }
        }
    }


    companion object {
        const val EVENT_REMOTE_RENDER = "remote_render"
        const val EVENT_REMOTE_STOP_RENDER = "remote_stop_render"
    }
}

data class UidEvent(val uid: Int, val tag: String)
