package com.agora.data.model;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Member implements Serializable, Cloneable {
    private String objectId;
    private Room roomId;
    private Long streamId;
    private User userId;
    private int isSpeaker = 0;
    private int isMuted = 0;
    private int isSelfMuted = 0;

    public Member() {
    }

    public Member(@NonNull User mUser) {
        setUser(mUser);
    }

    public void setUser(@NonNull User mUser) {
        setUserId(mUser);
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public Room getRoomId() {
        return roomId;
    }

    public void setRoomId(Room roomId) {
        this.roomId = roomId;
    }

    public Long getStreamId() {
        return streamId;
    }

    public void setStreamId(Long streamId) {
        this.streamId = streamId;
    }

    public User getUserId() {
        return userId;
    }

    public void setUserId(User userId) {
        this.userId = userId;
    }

    public int getIsSpeaker() {
        return isSpeaker;
    }

    public void setIsSpeaker(int isSpeaker) {
        this.isSpeaker = isSpeaker;
    }

    public int getIsMuted() {
        return isMuted;
    }

    public void setIsMuted(int isMuted) {
        this.isMuted = isMuted;
    }

    public int getIsSelfMuted() {
        return isSelfMuted;
    }

    public void setIsSelfMuted(int isSelfMuted) {
        this.isSelfMuted = isSelfMuted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Member member = (Member) o;

        return objectId.equals(member.objectId);
    }

    @Override
    public int hashCode() {
        return objectId.hashCode();
    }

    @NonNull
    @Override
    public Member clone() {
        try {
            return (Member) super.clone();
        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return new Member();
    }

    @NonNull
    @Override
    public String toString() {
        return "Member{" +
                "objectId='" + objectId + '\'' +
                ", roomId=" + roomId +
                ", streamId=" + streamId +
                ", userId=" + userId +
                ", isSpeaker=" + isSpeaker +
                ", isMuted=" + isMuted +
                ", isSelfMuted=" + isSelfMuted +
                '}';
    }
}
