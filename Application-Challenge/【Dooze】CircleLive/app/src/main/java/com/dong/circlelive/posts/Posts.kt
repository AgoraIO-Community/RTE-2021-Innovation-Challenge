package com.dong.circlelive.posts

import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.AVUser
import com.dong.circlelive.db.UserDatabase
import com.dong.circlelive.model.toUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.*

/**
 * Create by dooze on 2021/5/24  1:41 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
class Posts(val avObject: AVObject) {

    val id: String
        get() = avObject.objectId

    val content: String
        get() = avObject.getString("content")

    val owner: AVUser
        get() = avObject.getAVObject("owner")

    val createdAt: Date
        get() = avObject.createdAt

    val queryTime: Long
        get() = avObject.getLong("queryTime")

    companion object
}


suspend fun Posts.Companion.publish(content: String) = withContext(Dispatchers.IO) {
    AVObject("Posts").apply {
        put("content", content)
        put("owner", AVUser.currentUser())
    }.save()
}


suspend fun Posts.Companion.fetchNew(currentTime: Long) = withContext(Dispatchers.IO) {
    val avQuery = AVQuery<AVObject>("Posts")
    avQuery.cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
    avQuery.include("owner")
    avQuery.orderByDescending("createdAt")
    avQuery.whereGreaterThan("queryTime", currentTime)
    avQuery.find().map {
        kotlin.runCatching {
            UserDatabase.db.userDao().update(it.getAVObject<AVUser>("owner").toUser())
        }
        Posts(it)
    }
}


suspend fun Posts.Companion.fetchMore(currentTime: Long) = withContext(Dispatchers.IO) {
    val avQuery = AVQuery<AVObject>("Posts")
    avQuery.cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
    avQuery.include("owner")
    avQuery.orderByDescending("createdAt")
    avQuery.whereLessThan("queryTime", currentTime)
    avQuery.find().map { Posts(it) }
}


suspend fun Posts.Companion.fetchMore(userId: String, currentTime: Long) = withContext(Dispatchers.IO) {
    val avQuery = AVQuery<AVObject>("Posts")
    avQuery.cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
    avQuery.include("owner")
    avQuery.whereEqualTo("owner", AVUser.createWithoutData(AVUser::class.java, userId))
    avQuery.orderByDescending("createdAt")
    avQuery.whereLessThan("queryTime", currentTime)
    avQuery.find().map { Posts(it) }
}