import { ConnectionDisconnectedReason, ConnectionState, IAgoraRTCRemoteUser } from "agora-rtc-sdk-ng";
import { IRtcData, TMediaType } from "~/interface";
import AgoraRtc from "./main.vue";
interface AgoraRtcExtend extends AgoraRtc {
    rtcData: IRtcData;
    hasJoin: boolean;
}
declare const eventHandlerMap: {
    "user-published": (this: AgoraRtcExtend, user: IAgoraRTCRemoteUser, mediaType: TMediaType) => void;
    "user-unpublished": (this: AgoraRtcExtend, user: IAgoraRTCRemoteUser, mediaType: TMediaType) => void;
    "user-joined": (this: AgoraRtcExtend, user: IAgoraRTCRemoteUser) => void;
    "user-left": (this: AgoraRtcExtend, user: IAgoraRTCRemoteUser, reason: string) => void;
    "connection-state-change": (this: AgoraRtcExtend, curState: ConnectionState, revState: ConnectionState, reason?: ConnectionDisconnectedReason | undefined) => void;
};
export default eventHandlerMap;
