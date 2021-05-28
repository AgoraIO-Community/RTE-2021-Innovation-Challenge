import { UID } from "agora-rtc-sdk-ng";
import { AgoraRtcVueRemoteUserMediaList, IRtcData, TMediaType, TrackType } from "~/interface";
declare const ReceiverBase_base: import("vue-class-component/lib/declarations").VueClass<unknown>;
export default class ReceiverBase extends ReceiverBase_base {
    rtcData: IRtcData;
    MEDIA_TYPE: TMediaType;
    MEDIA_TRACK_NAME: TrackType;
    REFUSE: UID[];
    ACCEPT: UID[];
    unsubscribe: (uid: UID, type?: TMediaType) => Promise<void>;
    subscribedList: UID[];
    subscribeSuccessList: AgoraRtcVueRemoteUserMediaList;
    get meetRequirementMediaList(): AgoraRtcVueRemoteUserMediaList;
    refuseChange(): Promise<void>;
    acceptChange(): Promise<void>;
    publishedListWatch(newList: AgoraRtcVueRemoteUserMediaList): Promise<void>;
    publishedListChange(publishedList: AgoraRtcVueRemoteUserMediaList): Promise<void>;
    __notMeetUIDRequirement(uid: UID): boolean;
}
export {};
