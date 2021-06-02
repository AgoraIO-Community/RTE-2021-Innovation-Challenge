package com.dong.circlelive.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.launch
import androidx.lifecycle.viewModelScope
import cn.leancloud.AVUser
import com.dong.circlelive.*
import com.dong.circlelive.activities.Activity
import com.dong.circlelive.base.Timber
import com.dong.circlelive.base.lazyFast
import com.dong.circlelive.db.UserDatabase
import com.dong.circlelive.live.model.LiveChannel
import com.dong.circlelive.live.model.subscriberUpdate
import com.dong.circlelive.live.model.syncSubscriberChannel
import com.dong.circlelive.model.User
import com.dong.circlelive.model.getByName
import com.dong.circlelive.model.toUser
import com.dong.circlelive.rtc.LiveRtmChannel
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMContactListener
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseIM
import com.hyphenate.easeui.domain.EaseUser
import com.hyphenate.easeui.provider.EaseConversationInfoProvider
import com.hyphenate.easeui.provider.EaseUserProfileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Create by dooze on 2021/5/24  10:08 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class MainViewModel : ViewModel() {

    val unreadActivityCount by lazyFast { UserDatabase.db.activityDao().unreadCount(Activity.Status.READ.value) }

    val logout = MutableLiveData<Boolean>()

    private val userMap = mutableMapOf<String, User>()

    fun init() {
        launch {
            try {
                val avUser = AVUser.currentUser()
                UserDatabase.init(avUser.objectId)
                EaseIM.getInstance().init(appContext, EMOptions())
                EaseIM.getInstance().userProvider = EaseUserProfileProvider { username ->
                    val user = userMap[username]
                    if (user == null) {
                        fetchUser(username)
                        EaseUser(username)
                    } else {
                        EaseUser().apply {
                            this.username = user.username
                            this.avatar = user.avatarUrl
                        }
                    }
                }
                EaseIM.getInstance().conversationInfoProvider = EaseConversationInfoProvider {
                    appContext.getDrawable(R.drawable.default_avatar)
                }
                emClient.addConnectionListener(object : EMConnectionListener {
                    override fun onConnected() {
                        Timber.d { "em onConnected" }
                    }

                    override fun onDisconnected(errorCode: Int) {
                        Timber.d { "em onDisconnected $errorCode" }
                    }
                })

                emClient.contactManager().setContactListener(object : EMContactListener {
                    override fun onContactAdded(username: String?) {
                        Timber.d { "onContactAdded $username" }
                    }

                    override fun onContactDeleted(username: String?) {
                        Timber.d { "onContactDeleted $username" }
                    }

                    override fun onContactInvited(username: String?, reason: String?) {
                        Timber.d { "onContactInvited $username  $reason" }
                        username ?: return
                        viewModelScope.launch(Dispatchers.IO) {
                            try {
                                val user = User.getByName(username)
                                UserDatabase.db.activityDao().insertActivity(Activity().apply {
                                    type = Activity.Type.INVITATION.value
                                    fromEmUsername = username
                                    fromAvUserId = user.objectId
                                    fromUsername = user.username
                                    content = "$reason"
                                })
                            } catch (t: Throwable) {
                                Timber.e(t)
                            }
                        }
                    }

                    override fun onFriendRequestAccepted(username: String?) {
                        Timber.d { "onFriendRequestAccepted $username" }
                    }

                    override fun onFriendRequestDeclined(username: String?) {
                        Timber.d { "onFriendRequestDeclined $username " }
                    }
                })
                live.loginRtm(avUser.objectId)
                LiveChannel.syncSubscriberChannel().forEach {
                    val channel = it.liveChannel
                    live.liveRtmChannels[channel.id] = LiveRtmChannel(channel.id, channel.name).apply {
                        start()
                    }
                }
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
    }

    private fun fetchUser(username: String) {
        launch(Dispatchers.IO) {
            val u = UserDatabase.db.userDao().findByEmUsername(username)
            if (u != null) {
                userMap[username] = u
            } else {
                userMap[username] = User.getByName(username).toUser()
            }
            userMap[username]?.let {
                EaseIM.getInstance().userUpdate.postValue(EaseUser(it.username))
            }
        }
    }

    fun markReadActivity() {
        viewModelScope.launch {
            kotlin.runCatching {
                withContext(Dispatchers.IO) {
                    UserDatabase.db.activityDao().apply {
                        val acs = queryAll(Activity.Status.NORMAL.value).toList()
                        if (acs.isNotEmpty()) {
                            acs.forEach {
                                it.status = Activity.Status.READ.value
                            }
                            update(acs)
                        }
                    }
                }
            }
        }
    }

    fun logout() {
        launch {
            try {
                withContext(Dispatchers.IO) {
                    emClient.logout(false)
                    AVUser.logOut()
                    live.finishLive()
                    emClient.logout(true)
                    subscriberUpdate.postValue(false)
                    subscriberChannelMessagesLD.postValue("")
                    subscriberChannelMessages.clear()
                    subscriberChannels.clear()
                }
                logout.value = true
            } catch (t: Throwable) {
                t.toast()
            }
        }
    }

}