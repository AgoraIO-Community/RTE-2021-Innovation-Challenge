package io.agora.education.api.room.data

import java.io.Serializable

/**超级小班课中会包含一个大班和N个小班*/
enum class ClassType(var value: Int) {
    Main(0),
    Sub(1)
}

enum class RoomType(var value: Int) {
    ONE_ON_ONE(0),
    SMALL_CLASS(1),
    LARGE_CLASS(2),
    BREAKOUT_CLASS(3)
}

data class Property(
        val key: String,
        val value: String
) : Serializable {
    companion object {
        /**保留字段，请勿在业务中使用*/
        const val CAUSE = "cause"
        const val KEY_TEACHER_LIMIT = "TeacherLimit"
        const val KEY_STUDENT_LIMIT = "StudentLimit"
        const val KEY_ASSISTANT_LIMIT = "AssistantLimit"
    }
}


class RoomCreateOptions(
        var roomUuid: String,
        var roomName: String,
        val roomType: Int
) {
    val roomProperties: MutableMap<String, String> = mutableMapOf()

    init {
        roomProperties[Property.KEY_TEACHER_LIMIT] = when (roomType) {
            RoomType.ONE_ON_ONE.value -> "1"
            RoomType.SMALL_CLASS.value -> "1"
            RoomType.LARGE_CLASS.value -> "1"
            RoomType.BREAKOUT_CLASS.value -> "1"
            /**-1表示不做限制*/
            else -> "-1"
        }
        roomProperties[Property.KEY_STUDENT_LIMIT] = when (roomType) {
            RoomType.ONE_ON_ONE.value -> "1"
            RoomType.SMALL_CLASS.value -> "16"
            RoomType.LARGE_CLASS.value -> "-1"
            RoomType.BREAKOUT_CLASS.value -> "-1"
            else -> "-1"
        }
        roomProperties[Property.KEY_ASSISTANT_LIMIT] = when (roomType) {
            RoomType.ONE_ON_ONE.value -> "0"
            RoomType.SMALL_CLASS.value -> "0"
            RoomType.LARGE_CLASS.value -> "0"
            RoomType.BREAKOUT_CLASS.value -> "1"
            else -> "1"
        }
    }
}

class EduError(
        val code: Int,
        val message: String
) {
}
