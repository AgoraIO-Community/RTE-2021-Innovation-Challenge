package com.agora.data.provider;

import androidx.annotation.NonNull;

import com.agora.data.model.Member;
import com.agora.data.model.Room;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Observable;

public interface IMessageSource {

    Observable<Member> joinRoom(@NonNull Room room, @NonNull Member member);

    Completable leaveRoom(@NonNull Room room, @NonNull Member member);

    Completable muteVoice(@NonNull Member member, int muted);

    Completable muteSelfVoice(@NonNull Member member, int muted);

    Completable requestHandsUp(@NonNull Member member);

    Completable agreeHandsUp(@NonNull Member member);

    Completable refuseHandsUp(@NonNull Member member);

    Completable inviteSeat(@NonNull Member member);

    Completable agreeInvite(@NonNull Member member);

    Completable refuseInvite(@NonNull Member member);

    Completable seatOff(@NonNull Member member);

    Observable<List<Member>> getHandUpList();

    int getHandUpListCount();
}
