package io.agora.education.service.bean.request;

import android.text.TextUtils;

import com.google.gson.Gson;

import java.util.Map;
import java.util.Set;

import io.agora.education.api.room.data.RoomCreateOptions;
import io.agora.education.api.room.data.RoomType;
import io.agora.education.service.bean.base.LimitConfig;
import io.agora.education.service.bean.base.RoleConfig;

import static io.agora.education.api.room.data.Property.KEY_ASSISTANT_LIMIT;
import static io.agora.education.api.room.data.Property.KEY_STUDENT_LIMIT;
import static io.agora.education.api.room.data.Property.KEY_TEACHER_LIMIT;

public class RoomCreateOptionsReq {
    private String roomName;
    private RoleConfig roleConfig;
    private Map<String, Object> roomProperties;

    public RoomCreateOptionsReq() {
    }

    public RoomCreateOptionsReq(String roomName, RoleConfig roleConfig) {
        this.roomName = roomName;
        this.roleConfig = roleConfig;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }

    public RoleConfig getRoleConfig() {
        return roleConfig;
    }

    public void setRoleConfig(RoleConfig roleConfig) {
        this.roleConfig = roleConfig;
    }

    public Map<String, Object> getRoomProperties() {
        return roomProperties;
    }

    public void setRoomProperties(Map<String, Object> roomProperties) {
        this.roomProperties = roomProperties;
    }

    public static RoomCreateOptionsReq convertRoomCreateOptions(RoomCreateOptions roomCreateOptions) {
        RoomCreateOptionsReq roomCreateOptionsReq = new RoomCreateOptionsReq();
        roomCreateOptionsReq.roomName = roomCreateOptions.getRoomName();

        RoleConfig mRoleConfig = convertRoleConfig(roomCreateOptions);
        roomCreateOptionsReq.setRoleConfig(mRoleConfig);
        return roomCreateOptionsReq;
    }

    private static RoleConfig convertRoleConfig(RoomCreateOptions roomCreateOptions) {

        RoleConfig roleConfig = new RoleConfig();
        int teacherLimit = 0;
        int studentLimit = 0;
        int assistantLimit = 0;
        Set<Map.Entry<String, String>> set = roomCreateOptions.getRoomProperties().entrySet();
        String value;
        for (Map.Entry<String, String> element : set) {
            switch (element.getKey()) {
                case KEY_TEACHER_LIMIT:
                    value = element.getValue();
                    teacherLimit = TextUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
                    break;
                case KEY_STUDENT_LIMIT:
                    value = element.getValue();
                    studentLimit = TextUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
                    break;
                case KEY_ASSISTANT_LIMIT:
                    value = element.getValue();
                    assistantLimit = TextUtils.isEmpty(value) ? 0 : Integer.parseInt(value);
                    break;
                default:
                    break;
            }
        }
        roleConfig.setHost(new LimitConfig(teacherLimit));
        if (roomCreateOptions.getRoomType() == RoomType.LARGE_CLASS.getValue()) {
            roleConfig.setAudience(new LimitConfig(studentLimit));
        } else if (roomCreateOptions.getRoomType() == RoomType.BREAKOUT_CLASS.getValue()) {
            /**目前，超级小班课情况下，移动端只可能创建大房间，小房间由服务端创建，所以此处学生的角色是audience
             * */
            roleConfig.setAudience(new LimitConfig(studentLimit));
            roleConfig.setAssistant(new LimitConfig(assistantLimit));
        } else {
            roleConfig.setBroadcaster(new LimitConfig(studentLimit));
        }
        return roleConfig;
    }
}
