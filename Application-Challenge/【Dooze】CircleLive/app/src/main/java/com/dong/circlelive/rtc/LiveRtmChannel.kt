package com.dong.circlelive.rtc

import com.dong.circlelive.Live
import com.dong.circlelive.activities.Activity
import com.dong.circlelive.activities.dispatchEvent
import com.dong.circlelive.base.Timber
import com.dong.circlelive.db.UserDatabase
import com.dong.circlelive.live
import com.dong.circlelive.live.model.LiveMessage
import com.dong.circlelive.live.model.LiveMessageType
import com.dong.circlelive.live.model.toJson
import com.dong.circlelive.live.model.toLiveMessage
import com.dong.circlelive.toast
import io.agora.rtm.*
import kotlinx.coroutines.suspendCancellableCoroutine

/**
 * Create by dooze on 2021/5/26  11:49 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LiveRtmChannel(val channelId: String, val name: String) : RtmChannelListener {

    val rtmClient = requireNotNull(live.rtmClient)

    private val rtmChannel = rtmClient.createChannel(channelId, this)

    private var started = false

    suspend fun start() = suspendCancellableCoroutine<ErrorInfo?> { c ->
        if (started) {
            c.resume(null, null)
            return@suspendCancellableCoroutine
        }
        rtmChannel.join(object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                started = true
                c.resume(null, null)
            }

            override fun onFailure(p0: ErrorInfo?) {
                c.resume(p0, null)
            }
        })
    }

    fun startAsync() {
        if (started) {
            return
        }
        rtmChannel.join(object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                started = true
            }

            override fun onFailure(p0: ErrorInfo?) {
            }
        })
    }

    suspend fun leave() = suspendCancellableCoroutine<ErrorInfo?> { c ->
        rtmChannel.leave(object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                rtmChannel.release()
                c.resume(null, null)
            }

            override fun onFailure(p0: ErrorInfo?) {
                rtmChannel.release()
                c.resume(p0, null)
            }
        })
    }

    suspend fun sendMessage(content: String) = suspendCancellableCoroutine<ErrorInfo?> { c ->
        rtmChannel.sendMessage(rtmClient.createMessage(content), object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                c.resume(null, null)
            }

            override fun onFailure(p0: ErrorInfo?) {
                c.resume(p0, null)
            }
        })
    }


    suspend fun sendMessage(liveMessage: LiveMessage) = suspendCancellableCoroutine<ErrorInfo?> { c ->
        rtmChannel.sendMessage(rtmClient.createMessage(liveMessage.toJson()).apply {

        }, object : ResultCallback<Void> {
            override fun onSuccess(p0: Void?) {
                c.resume(null, null)
            }

            override fun onFailure(p0: ErrorInfo?) {
                c.resume(p0, null)
            }
        })
    }

    suspend fun members() = suspendCancellableCoroutine<List<RtmChannelMember>?> { c ->
        rtmChannel.getMembers(object : ResultCallback<List<RtmChannelMember>> {
            override fun onSuccess(p0: List<RtmChannelMember>) {
                c.resume(p0, null)
            }

            override fun onFailure(p0: ErrorInfo?) {
                c.resume(null, null)
            }
        })
    }

    override fun onMemberCountUpdated(p0: Int) {
        Timber.d(Live.TAG) { "onMemberCountUpdated $p0" }
    }

    override fun onAttributesUpdated(p0: MutableList<RtmChannelAttribute>?) {
        Timber.d(Live.TAG) { "onAttributesUpdated $p0" }

    }

    override fun onMessageReceived(p0: RtmMessage, p1: RtmChannelMember) {
        kotlin.runCatching {
            val msg = p0.text.toLiveMessage()
            UserDatabase.db.activityDao().insertActivity(Activity().apply {
                content = msg.content
                fromAvUserId = msg.userId
                fromUsername = UserDatabase.db.userDao().find(p1.userId)?.username ?: msg.username
                type = Activity.Type.LIVE_INFO.value
                dispatchEvent(channelId)
            })
            if (msg.type == LiveMessageType.LIVING_START.value) {
                toast(msg.content)
            }
        }
        Timber.d(Live.TAG) { "onMessageReceived ${p0.text}  ${p1.userId}" }

    }

    override fun onImageMessageReceived(p0: RtmImageMessage?, p1: RtmChannelMember?) {
        Timber.d(Live.TAG) { "onImageMessageReceived $p0" }

    }

    override fun onFileMessageReceived(p0: RtmFileMessage?, p1: RtmChannelMember?) {
        Timber.d(Live.TAG) { "onFileMessageReceived $p0" }

    }

    override fun onMemberJoined(p0: RtmChannelMember) {
        Timber.d(Live.TAG) { "onMemberJoined $p0" }
        UserDatabase.db.activityDao().insertActivity(Activity().apply {
            fromAvUserId = p0.userId
            fromUsername = UserDatabase.db.userDao().find(p0.userId)?.username ?: "陌生人"
            content = "$fromUsername 进入了Live($name)"
            type = Activity.Type.LIVE_INFO.value
            dispatchEvent(channelId)
        })

    }

    override fun onMemberLeft(p0: RtmChannelMember) {
        Timber.d(Live.TAG) { "onMemberLeft $p0" }
        UserDatabase.db.activityDao().insertActivity(Activity().apply {
            fromAvUserId = p0.userId
            fromUsername = UserDatabase.db.userDao().find(p0.userId)?.username ?: "陌生人"
            content = "$fromUsername 离开了Live($name)"
            type = Activity.Type.LIVE_INFO.value
            dispatchEvent(channelId)
        })
    }
}