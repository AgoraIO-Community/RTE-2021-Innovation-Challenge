package io.agora.education.api.room.data

import io.agora.education.api.user.data.EduUserRole
import io.agora.rte.data.RteChannelMediaOptions

data class RoomMediaOptions(
        var autoSubscribe: Boolean = true,
        var autoPublish: Boolean = true
) {
    /**用户传了primaryStreamId,那么就用他当做streamUuid;如果没传，就是默认值，后端会生成一个streamUuid*/
    var primaryStreamId: Int = DefaultStreamId

    companion object {
        const val DefaultStreamId = 0
    }

    constructor(primaryStreamId: Int) : this() {
        this.primaryStreamId = primaryStreamId
    }

    fun convert(): RteChannelMediaOptions {
        return RteChannelMediaOptions(autoSubscribe, autoSubscribe)
    }

    fun getPublishType(): AutoPublishItem {
        return if (autoPublish) {
            AutoPublishItem.AutoPublish
        } else {
            AutoPublishItem.NoOperation
        }
    }
}

data class RoomJoinOptions(
        val userUuid: String,
        /**用户可以传空,为空则使用roomImpl中默认的userName*/
        var userName: String?,
        val roleType: EduUserRole,
        val mediaOptions: RoomMediaOptions
) {
    fun closeAutoPublish() {
        mediaOptions.autoPublish = false
    }
}

enum class AutoPublishItem(val value: Int) {
    NoOperation(0),
    AutoPublish(1),
    NoAutoPublish(2)
}
