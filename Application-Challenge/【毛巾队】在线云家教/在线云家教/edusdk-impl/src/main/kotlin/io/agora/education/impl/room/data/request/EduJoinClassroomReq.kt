package io.agora.education.impl.room.data.request

/**加入房间时的请求参数
 * @param streamUuid 默认流id，如果不设，则使用userUuid
 * @param publishType 用户是否要自动发流(服务器会创建一条流信息并返回)*/
class EduJoinClassroomReq(
        val userName:   String,
        val role:       String,
        val streamUuid: String,
        val publishType: Int
) {
}