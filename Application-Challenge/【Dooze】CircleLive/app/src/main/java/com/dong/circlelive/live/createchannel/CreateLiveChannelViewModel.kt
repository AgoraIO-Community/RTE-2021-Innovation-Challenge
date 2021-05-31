package com.dong.circlelive.live.createchannel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.launch
import com.dong.circlelive.R
import com.dong.circlelive.getString
import com.dong.circlelive.live.model.LiveChannel
import com.dong.circlelive.live.model.publish

/**
 * Create by dooze on 2021/5/27  6:10 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class CreateLiveChannelViewModel : ViewModel() {

    val publishError = MutableLiveData<String?>()

    val publishedChannelId = MutableLiveData<String>()

    fun publish(title: String, desc: String) {
        if (title.trim().isEmpty()) {
            publishError.value = getString(R.string.post_too_short)
            return
        }
        launch {
            publishedChannelId.value = LiveChannel.publish(title, desc)
        }.invokeOnCompletion {
            if (it != null) {
                publishError.value = it.message
            }
        }
    }
}