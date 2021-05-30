package com.dong.circlelive.posts

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dong.circlelive.base.Timber
import com.dong.circlelive.toast
import kotlinx.coroutines.launch

/**
 * Create by dooze on 2021/5/24  1:39 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class PostsViewModel : ViewModel() {

    val postsLiveData = MutableLiveData<List<Posts>>()

    val stopRefresh = MutableLiveData<Boolean>()

    val isLoading = MutableLiveData<Boolean>()

    val isLoadingMore = MutableLiveData<Boolean>()

    private val posts = mutableListOf<Posts>()


    fun load() {
        if (isLoading.value == true || (posts.isNullOrEmpty() && isLoadingMore.value == true)) return
        if (posts.isNullOrEmpty()) {
            loadMore()
            return
        }
        isLoading.value = true
        viewModelScope.launch {
            try {
                posts.addAll(0, Posts.fetchNew(posts.firstOrNull()?.queryTime ?: System.currentTimeMillis()))
                postsLiveData.value = posts.toList()
            } catch (t: Throwable) {
                Timber.e(t)
                t.toast()
            }
            stopRefresh.value = true
            isLoading.value = false
        }
    }

    fun loadMore() {
        if (isLoadingMore.value == true) return
        isLoadingMore.value = true
        viewModelScope.launch {
            try {
                posts.addAll(Posts.fetchMore(posts.lastOrNull()?.queryTime ?: System.currentTimeMillis()))
                postsLiveData.value = posts.toList()
            } catch (t: Throwable) {
                Timber.e(t)
                t.toast()
            }
            stopRefresh.value = true
            isLoadingMore.value = false
        }

    }
}