package io.agora.education.impl.cmd.bean

enum class CMDId(var value: Int) {
    /**频道-房间-开始/结束*/
    RoomStateChange(1),

    /**频道-房间-禁用状态*/
    RoomMuteStateChange(2),

    /**频道-房间-即时聊天--*/
    ChannelMsgReceived(3),

    /**房间属性发生变更*/
    RoomPropertyChanged(4),

    /**频道--自定义消息(可以是用户的信令)*/
    ChannelCustomMsgReceived(99),

    /**频道-用户-进出
     * 有人员进出时会触发此消息(包括进入，离开，踢出)*/
    UserJoinOrLeave(20),

    /**人员信息改变会触发*/
    UserStateChange(21),

    /**人员自定义信息发生改变*/
    UserPropertiedChanged(22),

    /**频道-流-新增/更新/删除*/
    StreamStateChange(40),

    /**频道-白板房间状态*/
    BoardRoomStateChange(60),

    /**频道-白板用户状态*/
    BoardUserStateChange(61),


    /**点对点-用户-私聊*/
    PeerMsgReceived(1),

    /**邀请申请动作消息*/
    ActionMsgReceived(2),

    /**点对点--自定义消息(可以使用户的信令)*/
    PeerCustomMsgReceived(99);
}