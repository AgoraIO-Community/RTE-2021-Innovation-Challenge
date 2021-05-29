package com.dong.circlelive.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dong.circlelive.R
import com.dong.circlelive.base.Timber
import com.dong.circlelive.getString
import kotlinx.coroutines.launch

/**
 * Create by dooze on 2021/5/24  1:48 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class CreatePostViewModel : ViewModel() {

    var isPublishing = false
        private set

    val publishResult = MutableLiveData<PublishResult>()

    fun publish(content: String) {
        if (isPublishing) return
        viewModelScope.launch {
            isPublishing = true
            try {
                Posts.publish(content)
                publishResult.value = PublishResult(null)
            } catch (t: Throwable) {
                Timber.e(t) { "publish post error" }
                publishResult.value = PublishResult(getString(R.string.publish_post_error))
            }
            isPublishing = false
        }
    }
}

data class PublishResult(val error: String?)