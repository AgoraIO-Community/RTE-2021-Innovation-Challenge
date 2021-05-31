package com.dong.circlelive.activities

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dong.circlelive.base.Timber
import com.dong.circlelive.live.model.LiveMessage
import com.dong.circlelive.live.model.subscriberChannelMessages
import com.dong.circlelive.live.model.subscriberChannelMessagesLD
import com.dong.circlelive.live.model.subscriberChannels

/**
 * Create by dooze on 2021/5/24  10:24 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
@Keep
@Entity(tableName = "Activity")
class Activity {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "localId")
    var localId: Long = 0L

    @ColumnInfo(name = "fromEmUsername")
    var fromEmUsername: String? = null

    @ColumnInfo(name = "fromAvUserId")
    var fromAvUserId: String? = null

    @ColumnInfo(name = "fromUsername")
    var fromUsername: String? = null

    @ColumnInfo(name = "fromUserCoverUrl")
    var fromUserCoverUrl: String? = null

    @ColumnInfo(name = "content")
    var content: String? = null

    @ColumnInfo(name = "type")
    var type: Int = Type.UNKNOWN.value

    @ColumnInfo(name = "status")
    var status: Int = Status.NORMAL.value

    @ColumnInfo(name = "createdAt")
    var createdAt: Long = System.currentTimeMillis()


    enum class Type(val value: Int) {
        UNKNOWN(-1),
        INVITATION(1),
        LIVE_INFO(2)
    }

    enum class Status(val value: Int) {
        NORMAL(0),
        READ(1),
        DONE(10)

    }

}

fun Activity.toLiveMessage(): LiveMessage? {
    return LiveMessage(
        content ?: return null,
        fromAvUserId ?: return null,
        fromEmUsername ?: return null
    )
}

fun Activity.dispatchEvent(channelId: String) {
    if (!subscriberChannels.containsKey(channelId)) return
    LiveMessage(
        content ?: return,
        fromAvUserId ?: return,
        fromUsername ?: return
    ).apply {
        subscriberChannelMessages[channelId] = this
        subscriberChannelMessagesLD.postValue(channelId)
    }
}