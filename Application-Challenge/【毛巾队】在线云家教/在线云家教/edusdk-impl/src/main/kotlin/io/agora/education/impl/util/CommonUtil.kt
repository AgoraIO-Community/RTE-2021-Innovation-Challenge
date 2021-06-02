package io.agora.education.impl.util

import com.google.gson.Gson
import com.google.gson.JsonObject
import io.agora.education.api.room.EduRoom
import io.agora.education.api.room.data.RoomType
import io.agora.education.impl.BuildConfig
import io.agora.education.impl.room.data.EduRoomInfoImpl

internal class CommonUtil {
    companion object {
        fun buildRtcOptionalInfo(eduRoom: EduRoom): String {
            val info = JsonObject()
            info.addProperty("demo_ver", BuildConfig.VERSION_NAME)
            when ((eduRoom.getRoomInfo() as EduRoomInfoImpl).roomType) {
                RoomType.ONE_ON_ONE -> {
                    info.addProperty("demo_scenario", "One on One Classroom")
                }
                RoomType.SMALL_CLASS -> {
                    info.addProperty("demo_scenario", "Small Classroom")
                }
                RoomType.LARGE_CLASS -> {
                    info.addProperty("demo_scenario", "Lecture Hall")
                }
                RoomType.BREAKOUT_CLASS -> {
                    info.addProperty("demo_scenario", "Breakout Classroom")
                }
            }
            return Gson().toJson(info)
        }
    }
}