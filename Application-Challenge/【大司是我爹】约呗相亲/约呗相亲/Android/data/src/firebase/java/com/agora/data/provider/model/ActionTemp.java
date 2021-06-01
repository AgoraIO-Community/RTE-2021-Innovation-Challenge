package com.agora.data.provider.model;

import com.agora.data.model.Action;
import com.google.firebase.firestore.DocumentReference;

public class ActionTemp {
    private DocumentReference memberId;
    private DocumentReference roomId;
    private int action;
    private int status;
    private long createdAt;

    public ActionTemp() {
    }

    public Action create(String objectId) {
        Action action = new Action();
        action.setObjectId(objectId);
        action.setAction(this.action);
        action.setStatus(this.status);
        return action;
    }

    public DocumentReference getMemberId() {
        return memberId;
    }

    public void setMemberId(DocumentReference memberId) {
        this.memberId = memberId;
    }

    public DocumentReference getRoomId() {
        return roomId;
    }

    public void setRoomId(DocumentReference roomId) {
        this.roomId = roomId;
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

    public long getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(long createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ActionTemp{" +
                "memberId=" + memberId +
                ", roomId=" + roomId +
                ", action=" + action +
                ", status=" + status +
                ", createdAt=" + createdAt +
                '}';
    }
}
