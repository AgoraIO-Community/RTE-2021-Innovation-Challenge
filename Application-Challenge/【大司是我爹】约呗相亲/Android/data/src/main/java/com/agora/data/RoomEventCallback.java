package com.agora.data;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;

import com.agora.data.model.Member;
import com.agora.data.model.Room;

@MainThread
public interface RoomEventCallback {
    void onOwnerLeaveRoom(@NonNull Room room);

    void onLeaveRoom(@NonNull Room room);

    void onMemberJoin(@NonNull Member member);

    void onMemberLeave(@NonNull Member member);

    /**
     * 房间角色变化回调，角色变化指：观众和说话人变化
     *
     * @param isMine 是否是我主动触发的回调，true-我主动触发，false-不是我触发，被动触发。
     */
    void onRoleChanged(boolean isMine, @NonNull Member member);

    /**
     * Audio变化回调，这里变化是指：开麦和禁麦
     *
     * @param isMine 是否是我主动触发的回调，true-我主动触发，false-不是我触发，被动触发。
     */
    void onAudioStatusChanged(boolean isMine, @NonNull Member member);

    void onReceivedHandUp(@NonNull Member member);

    void onHandUpAgree(@NonNull Member member);

    void onHandUpRefuse(@NonNull Member member);

    void onReceivedInvite(@NonNull Member member);

    void onInviteAgree(@NonNull Member member);

    void onInviteRefuse(@NonNull Member member);

    void onEnterMinStatus();

    void onRoomError(int error);
}
