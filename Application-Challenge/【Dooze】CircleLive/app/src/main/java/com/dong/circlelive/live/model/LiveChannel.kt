package com.dong.circlelive.live.model

import androidx.lifecycle.MutableLiveData
import cn.leancloud.AVObject
import cn.leancloud.AVQuery
import cn.leancloud.AVUser
import cn.leancloud.annotation.AVClassName
import com.dong.circlelive.base.Timber
import com.dong.circlelive.live
import com.dong.circlelive.rtc.LiveRtmChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.ConcurrentHashMap

/**
 * Create by dooze on 2021/5/18  8:06 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */


val subscriberChannels = mutableMapOf<String, SubscriberChannel>()

val subscriberChannelMessages = ConcurrentHashMap<String, LiveMessage>()

val subscriberChannelMessagesLD = MutableLiveData<String>()

val subscriberUpdate = MutableLiveData<Boolean>()

const val MAX_SUBSCRIBER_CHANNEL = 5

class SubscriberChannel(val id: String, val liveChannel: LiveChannel)

class ChannelComparator : Comparator<LiveChannel> {
    override fun compare(o1: LiveChannel?, o2: LiveChannel?): Int {
        if (o1 == null || o2 == null) return 0
        if (o1.isSubscriber && !o2.isSubscriber) return -1
        if (!o1.isSubscriber && o2.isSubscriber) return 1
        return (o2.updateAt - o1.updateAt).toInt()
    }
}

@AVClassName("LiveChannel")
class LiveChannel(private val avObject: AVObject) {

    val creator: AVUser
        get() = avObject.getAVObject("creator")

    val id: String
        get() = avObject.objectId

    val name: String
        get() = avObject.getString("name")

    val desc: String
        get() = avObject.getString("desc")

    val status: Int
        get() = avObject.getInt("status")

    val livingTimes: Long
        get() = avObject.getLong("livingTimes")

    val updateAt: Long
        get() = avObject.updatedAt.time


    val isSelf: Boolean get() = creator.objectId == AVUser.currentUser().objectId

    val isSubscriber: Boolean get() = subscriberChannels.containsKey(id)

    suspend fun subscriber(): Boolean {
        if (subscriberChannels.size >= MAX_SUBSCRIBER_CHANNEL) return false
        withContext(Dispatchers.IO) {
            val avObject = AVObject("SubscribeLiveChannel").apply {
                put("creator", AVUser.currentUser())
                put("liveChannel", avObject)
            }
            avObject.save()
            subscriberChannels[id] = SubscriberChannel(avObject.objectId, this@LiveChannel)
            live.liveRtmChannels[id] = LiveRtmChannel(channelId = id, name).apply {
                startAsync()
            }
        }
        return true
    }

    suspend fun unSubscriber(uiInvoke: () -> Unit) {
        val channel = subscriberChannels.remove(id) ?: return
        uiInvoke.invoke()
        withContext(Dispatchers.IO) {
            AVObject.createWithoutData("SubscribeLiveChannel", channel.id).delete()
            live.liveRtmChannels[id]?.leave()
        }
    }

    fun delete() {
        val cid = id
        avObject.delete()
        subscriberChannels[cid]?.id?.let {
            AVObject.createWithoutData("SubscribeLiveChannel", it).delete()
        }
        subscriberChannels.remove(cid)

    }

    suspend fun incLivingTimes() = withContext(Dispatchers.IO) {
        avObject.increment("livingTimes", 1)
        avObject.saveEventually()
    }

    companion object

}


suspend fun LiveChannel.Companion.syncSubscriberChannel(): List<SubscriberChannel> {
    val user = AVUser.currentUser()
    val avQuery = AVQuery<AVObject>("SubscribeLiveChannel")
    avQuery.cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
    avQuery.include("liveChannel")
    avQuery.orderByDescending("updatedAt")
    avQuery.whereEqualTo("creator", user)
    val list = withContext(Dispatchers.IO) {
        val res = mutableListOf<SubscriberChannel>()
        avQuery.find().forEach {
            val liveChannel = LiveChannel(it.getAVObject("liveChannel") ?: return@forEach)
            val subscriberChannel = SubscriberChannel(it.objectId, liveChannel)
            subscriberChannels[liveChannel.id] = subscriberChannel
            res.add(subscriberChannel)
        }
        res
    }
    subscriberUpdate.postValue(true)
    return list
}

suspend fun LiveChannel.Companion.fetch(owner: AVObject? = null): List<LiveChannel> = withContext(Dispatchers.IO) {
    val avQuery = AVQuery<AVObject>("LiveChannel")
    avQuery.cachePolicy = AVQuery.CachePolicy.NETWORK_ELSE_CACHE
    avQuery.include("creator")
    if (owner != null) {
        avQuery.whereEqualTo("creator", owner)
    }
    avQuery.orderByDescending("updatedAt")
    val list = avQuery.find().map { LiveChannel(it) }.toMutableList()
    list.sortWith(ChannelComparator())
    list
}


suspend fun LiveChannel.Companion.publish(title: String, desc: String): String = withContext(Dispatchers.IO) {
    val avObject = AVObject("LiveChannel").apply {
        put("creator", AVUser.currentUser())
        put("name", title)
        put("desc", desc)
    }
    avObject.save()
    avObject.objectId
}

