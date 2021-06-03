package io.agora.education.impl.user.data.request

class EduUpdateUserPropertyReq(
        val value: String,
        val cause: MutableMap<String, String>
) {}