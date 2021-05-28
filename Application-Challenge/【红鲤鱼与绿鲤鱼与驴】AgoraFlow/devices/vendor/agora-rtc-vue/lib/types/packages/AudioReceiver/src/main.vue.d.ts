import { AgoraRtcVueRemoteUserMedia, AgoraRtcVueRemoteUserMediaList, TrackType } from "~/interface";
import { IAgoraRTCClient, IAgoraRTCRemoteUser, IRemoteAudioTrack, IRemoteTrack, IRemoteVideoTrack, UID } from "agora-rtc-sdk-ng";
declare const ArAudioReceiver_base: import("vue-class-component/lib/declarations").VueClass<unknown>;
export default class ArAudioReceiver extends ArAudioReceiver_base {
    client: IAgoraRTCClient;
    subscribeList: AgoraRtcVueRemoteUserMediaList;
    trackType: TrackType;
    __getTracksFromAllSubscribeUsersByUidList: (uidList: UID[]) => (IRemoteVideoTrack | IRemoteAudioTrack)[];
    __getUsersFromAllSubscribeUsersByUidList: (uidList: UID[]) => IAgoraRTCRemoteUser[];
    __checkMediaIsNotPlaying: (media: AgoraRtcVueRemoteUserMedia) => boolean;
    debugData: {
        [k: string]: any;
    };
    __checkMediaIsPlaying: (media: AgoraRtcVueRemoteUserMedia) => boolean;
    __checkTrackIsNotPlaying: (track: IRemoteTrack) => boolean;
    __checkTrackIsPlaying: (media: IRemoteTrack) => boolean;
    callMethodOnTrackForRemote: (uidList: UID[], method: keyof IRemoteAudioTrack | keyof IRemoteVideoTrack, ...arg: any[]) => void;
    callMethodForResultOnTrackForRemote: (uidList: UID[], method: keyof IRemoteAudioTrack | keyof IRemoteVideoTrack, ...arg: any[]) => {
        result: any;
        uid: UID;
    }[];
    refuse: UID[];
    accept: UID[];
    /***
     * Methods
     */
    __subscribeChangeHandler(list: AgoraRtcVueRemoteUserMediaList): void;
    __playAll(list: AgoraRtcVueRemoteUserMediaList): void;
    __mediaPlay(media: AgoraRtcVueRemoteUserMedia): void;
    __setVolumeForSomeUsers(volume: number, uidList: UID[]): void;
    setVolumeForSomeRemote(volume: number, uidList: UID[]): void;
    __setVolumeForAllRemote(volume: number): void;
    setVolume(volume: number): void;
    __stop(uidList: UID[]): void;
    __play(uidList: UID[]): void;
    setPlaybackDeviceByUidList(uidList: UID[], deviceId: string): Promise<void>;
    setPlaybackDevice(deviceId: string): Promise<void>;
    __firstFrameDecoded(track: IRemoteTrack): void;
    setAudioFrameCallback(audioFrameCallback: ((buffer: AudioBuffer) => void) | null, frameSize?: number): void;
    getVolumeLevel(): {
        result: number;
        uid: UID;
    }[];
    getUserMuteStatus(): {
        result: boolean;
        uid: UID;
    }[];
}
export {};
