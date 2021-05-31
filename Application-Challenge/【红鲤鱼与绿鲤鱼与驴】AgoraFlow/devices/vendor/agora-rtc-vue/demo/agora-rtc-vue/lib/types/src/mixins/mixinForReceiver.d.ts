import { IAgoraRTCRemoteUser, IRemoteAudioTrack, IRemoteTrack, IRemoteVideoTrack, UID } from "agora-rtc-sdk-ng";
import { Vue } from "vue-property-decorator";
import { AgoraRtcVueRemoteUserMediaList, AgoraRtcVueRemoteUserMedia, TrackType } from "~/interface";
export default class MixinForReceiver extends Vue {
    subscribeList: AgoraRtcVueRemoteUserMediaList;
    trackType: TrackType;
    __stop: (u: UID[] | undefined) => void;
    __play: (u: UID[] | undefined) => void;
    __isRemoteVideoTrack(track: IRemoteTrack): track is IRemoteVideoTrack;
    __isRemoteAudioTrack(track: IRemoteTrack): track is IRemoteAudioTrack;
    __checkMediaIsNotPlaying(media: AgoraRtcVueRemoteUserMedia): boolean;
    __checkMediaIsPlaying(media: AgoraRtcVueRemoteUserMedia): boolean;
    __checkTrackIsNotPlaying(track: IRemoteTrack): boolean;
    __checkTrackIsPlaying(track: IRemoteTrack): boolean;
    getAllSubscribeUsersUidList(): UID[];
    __getMediasFromAllSubscribeUsersByUidList(...u: []): AgoraRtcVueRemoteUserMediaList;
    __getMediasFromAllSubscribeUsersByUidList(...u: UID[]): AgoraRtcVueRemoteUserMediaList;
    __getMediasFromAllSubscribeUsersByUidList(...u: [UID[]]): AgoraRtcVueRemoteUserMediaList;
    __getUsersFromAllSubscribeUsersByUidList(uidList: UID[]): IAgoraRTCRemoteUser[];
    __getTracksFromAllSubscribeUsersByUidList(uidList: UID[]): (IRemoteAudioTrack | IRemoteVideoTrack)[];
    stopAllRemote(): void;
    stopSomeRemote(uidList: UID[]): void;
    stop(): void;
    playSomeRemote(uidList: UID[]): void;
    playAllRemote(): void;
    play(): void;
    getTracksByUidList(uidList: UID[]): (IRemoteVideoTrack | IRemoteAudioTrack)[];
    getMediaStreamTracksByUidList(uidList: UID[]): MediaStreamTrack[];
    callMethodOnTrackForRemote(uidList: UID[], method: keyof IRemoteAudioTrack | keyof IRemoteVideoTrack, ...arg: any[]): void;
    callMethodForResultOnTrackForRemote(uidList: UID[], method: keyof IRemoteAudioTrack | keyof IRemoteVideoTrack, ...arg: any[]): {
        result: any;
        uid: UID;
    }[];
    getCurrentFrameData(): {
        result: ImageData;
        uid: UID;
    }[];
    getMediaStreamTrack(): {
        result: MediaStreamTrack;
        uid: UID;
    }[];
    getTrackId(): {
        result: string;
        uid: UID;
    }[];
    getUserId(): {
        result: UID;
        uid: UID;
    }[];
}
