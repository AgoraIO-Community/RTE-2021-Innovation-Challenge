package com.dong.circlelive.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.launch
import androidx.lifecycle.viewModelScope
import cn.leancloud.AVUser
import com.dong.circlelive.R
import com.dong.circlelive.base.Timber
import com.dong.circlelive.db.UserDatabase
import com.dong.circlelive.emClient
import com.dong.circlelive.live.model.LiveChannel
import com.dong.circlelive.live.model.fetch
import com.dong.circlelive.model.emUsername
import com.dong.circlelive.model.toUser
import com.dong.circlelive.posts.Posts
import com.dong.circlelive.posts.fetchMore
import com.dong.circlelive.toast
import com.hyphenate.EMCallBack
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Create by dooze on 2021/5/24  7:20 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class ProfileViewModel(val userId: String) : ViewModel() {

    val avUser = MutableLiveData<AVUser>()

    val isLoading = MutableLiveData<Boolean>()

    val posts = MutableLiveData<List<Posts>>()

    private val postsList = mutableListOf<Posts>()

    val liveChannels = MutableLiveData<List<LiveChannel>>()

    init {
        viewModelScope.launch {
            try {
                avUser.value = withContext(Dispatchers.IO) {
                    val user = AVUser.createWithoutData(AVUser::class.java, userId).fetch() as AVUser
                    UserDatabase.db.userDao().update(user.toUser())
                    user
                }!!
            } catch (t: Throwable) {
                Timber.e(t)
            }
        }
        fetch()
        fetchLiveChannels()
    }

    fun fetchLiveChannels() {
        launch {
            try {
                liveChannels.value = LiveChannel.fetch(AVUser.createWithoutData("_User", userId))
            } catch (t: Throwable) {
                t.toast()
                Timber.e(t) { "fetchLiveChannels error" }
            } finally {
            }
        }
    }

    fun fetch() {
        if (isLoading.value == true) return
        launch {
            isLoading.value = true
            try {
                postsList.addAll(Posts.fetchMore(userId, postsList.lastOrNull()?.queryTime ?: System.currentTimeMillis()))
                posts.value = postsList.toList()
            } catch (t: Throwable) {
                Timber.e(t)
            }
            isLoading.value = false
        }
    }

    fun sendAddFriendInvitation(reason: String) {
        emClient.contactManager().aysncAddContact(avUser.value?.emUsername ?: return, reason, object : EMCallBack {
            override fun onSuccess() {
                launch {
                    toast(R.string.common_send_success)
                }
            }

            override fun onError(code: Int, error: String?) {
                launch {
                    toast(error ?: return@launch)
                }
            }

            override fun onProgress(progress: Int, status: String?) {
            }
        })
    }

    fun deleteChannel(channel: LiveChannel) {
        val list = liveChannels.value?.toMutableList() ?: return
        list.removeAll { it.id == channel.id }
        liveChannels.value = list
        launch(Dispatchers.IO) {
            channel.delete()
        }
    }


}