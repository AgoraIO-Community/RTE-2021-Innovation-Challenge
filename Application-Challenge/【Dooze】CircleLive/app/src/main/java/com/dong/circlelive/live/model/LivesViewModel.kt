package com.dong.circlelive.live.model

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.launch
import androidx.lifecycle.viewModelScope
import com.dong.circlelive.base.Timber
import com.dong.circlelive.toast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * Create by dooze on 2021/5/15  3:52 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class LivesViewModel : ViewModel() {


    val liveChannels = MutableLiveData<List<LiveChannel>>()

    val stopRefresh = MutableLiveData<Boolean>()

    init {
        fetchLiveChannels()
    }

    fun fetchLiveChannels() {
        viewModelScope.launch {
            stopRefresh.value = false
            try {
                liveChannels.value = LiveChannel.fetch()
            } catch (t: Throwable) {
                t.toast()
                Timber.e(t) { "fetchLiveChannels error" }
            } finally {
                stopRefresh.value = true
            }
        }
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