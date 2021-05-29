import { IAgoraRTCClient } from "agora-rtc-sdk-ng";
import AgoraRtc from "./main.vue";
interface AgoraRtcExtend extends AgoraRtc {
    client: IAgoraRTCClient;
}
export default function (this: AgoraRtcExtend): void;
export {};
