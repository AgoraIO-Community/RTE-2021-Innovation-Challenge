import { AgoraRtcVueRemoteUserMediaList, TrackType } from "~/interface";
import { IAgoraRTCClient, UID, IRemoteTrack, IRemoteAudioTrack, IRemoteVideoTrack } from "agora-rtc-sdk-ng";
declare const ArVideoReceiver_base: import("vue-class-component/lib/declarations").VueClass<unknown>;
export default class ArVideoReceiver extends ArVideoReceiver_base {
    /***
     * @declare
     */
    subscribeList: AgoraRtcVueRemoteUserMediaList;
    client: IAgoraRTCClient;
    __getTracksFromAllSubscribeUsersByUidList: (uidList: UID[]) => (IRemoteAudioTrack | IRemoteVideoTrack)[];
    __checkTrackIsPlaying: (track: IRemoteTrack) => boolean;
    __checkTrackIsNotPlaying: (track: IRemoteTrack) => boolean;
    getAllSubscribeUsersUidList: () => UID[];
    debugData: {
        [k: string]: any;
    };
    /***
     * @Prop
     */
    LQStreamClassName: string;
    customizationPlayer: boolean;
    refuse: UID[];
    accept: UID[];
    /***
     * @data
     */
    playList: AgoraRtcVueRemoteUserMediaList;
    HQList: UID[];
    LQList: UID[];
    trackType: TrackType;
    timers: {
        [k: string]: number | null;
    };
    /***
     * @watch
     */
    watchPlayList(newList: AgoraRtcVueRemoteUserMediaList, oldList: AgoraRtcVueRemoteUserMediaList): void;
    /***
     * @methods
     */
    __clearDebugInfoTimer(): void;
    __subscribeChangeHandler(list: AgoraRtcVueRemoteUserMediaList): void;
    __playList(list: AgoraRtcVueRemoteUserMediaList): void;
    __triggerRemoteUserChangeEvent(): void;
    __stop(uidList: UID[]): void;
    __play(uidList: UID[]): void;
    setHQStream(...list: UID[] | [UID[]]): Promise<void>;
    __setHQStreamByUid(uid: UID): Promise<void>;
    __setLQStreamByUid(uid: UID): Promise<void>;
    switchStreamByUid(uid: UID): Promise<void>;
    __firstFrameDecoded(track: IRemoteTrack): void;
}
export {};
