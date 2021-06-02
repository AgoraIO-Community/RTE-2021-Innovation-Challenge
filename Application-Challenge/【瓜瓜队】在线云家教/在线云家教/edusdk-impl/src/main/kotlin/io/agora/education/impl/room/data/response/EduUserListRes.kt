package io.agora.education.impl.room.data.response

class EduUserListRes(
        var count: Int,
        var total: Int,
        var nextId: String,
        var list: MutableList<EduUserRes>
) {
}

open class EduUserRes(var userUuid: String,
                      var userName: String,
                      var role: String,
                      var muteChat: Int,
                      var updateTime: Long?,
                      var state: Int?) {
}