package com.agora.data.model;

import java.io.Serializable;

public class Action implements Serializable {
    private String objectId;
    private Member memberId;
    private Room roomId;
    private int action;
    private int status;

    public Room getRoomId() {
        return roomId;
    }

    public void setRoomId(Room roomId) {
        this.roomId = roomId;
    }

    public Member getMemberId() {
        return memberId;
    }

    public void setMemberId(Member memberId) {
        this.memberId = memberId;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Action action = (Action) o;

        return objectId.equals(action.objectId);
    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    public enum ACTION {
        HandsUp(1), Invite(2);

        private int value;

        ACTION(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum ACTION_STATUS {
        Ing(1), Agree(2), Refuse(3);

        private int value;

        ACTION_STATUS(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}
