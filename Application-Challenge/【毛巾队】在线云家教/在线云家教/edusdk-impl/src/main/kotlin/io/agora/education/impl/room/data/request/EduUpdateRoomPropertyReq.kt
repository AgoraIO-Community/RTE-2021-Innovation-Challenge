package io.agora.education.impl.room.data.request

class EduUpdateRoomPropertyReq(
        val value: String,
        val cause: MutableMap<String, String>
) {}