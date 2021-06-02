package io.agora.education.impl.role.data

/**
 *  权限规则
    权限表达式= privilige:role，表示对某类角色，有某项操作的权限
    特别说明
    1. 为简化表达式，*表示所有，比如 *:* 表示对所有人有所有权限
    2. local表示自己*/
enum class EduUserRoleStr(var value: String) {
    /**管理员，有最高权限，有对所有对象的所有权限
     *    * : *    */
    administrator("administrator"),
    /**主持人，权限较高，除了管理员，其他角色都可以操作
     *  updateRoomState:local
        updateRoomMute:local
        updateUser: *
        kickUser:createStream: *
        updateStream:deleteStream: *  */
    host         ("host"),
    /**助手，可以操作参与者和观众，但不能操作房间
     *  updateUser:local
        updateUser:broadcaster
        updateUser:audience
        kickUser:local
        kickUser:broadcaster
        kickUser:audience
        createStream:local
        createStream:broadcaster
        createStream:audience
        updateStream:local
        updateStream:broadcaster
        updateStream:audience
        deleteStream:local
        deleteStream:broadcaster
        deleteStream:audience*/
    assistant    ("assistant"),
    /**主播，可以自行发流
     *  updateUser:local
        kickUser:local
        createStream:local
        updateStream:local
        deleteStream:local*/
    broadcaster  ("broadcaster"),
    /**观众，不能自行发流
     *  kickUser:local
        updateStream:local
        deleteStream:local*/
    audience     ("audience")
}

