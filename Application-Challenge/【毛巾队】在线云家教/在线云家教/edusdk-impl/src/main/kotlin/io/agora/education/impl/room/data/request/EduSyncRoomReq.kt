package io.agora.education.impl.room.data.request

import java.util.*

class EduSyncRoomReq(
        var step: Int
) {
    /**每发起一次syncRoom请求，requestId就更新一次*/
    var requestId: String = UUID.randomUUID().toString()
    /**为空则表示从头开始拉全量数据*/
    var nextId: String? = null
    /**为空则表示从头开始拉增量数据*/
    var nextTs: Long? = null

    constructor(step: Int, requestId: String, nextId: String, nextTs: Long) : this(step) {
        this.requestId = requestId
        this.nextId = nextId
        this.nextTs = nextTs
    }

    /**注意：每发起一次请求，requestId就刷新一次，requestId用于过滤数据*/
    fun updateRequestId(): EduSyncRoomReq {
        this.requestId == UUID.randomUUID().toString()
        return this
    }
}

/**1第一阶段（根据nextId全量）2.第二阶段（根据ts增量）（也可理解为是RTM 重连之后的数据同步阶段）*/
enum class EduSyncStep(var value: Int) {
    FIRST(1),
    SECOND(2)
}

/**同步数据过程是否结束*/
enum class EduSyncFinished(var value: Int) {
    NO(0),
    YES(1)
}