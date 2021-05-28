import { IAgoraRTCClient } from "agora-rtc-sdk-ng";
import { AgoraRtcVueRemoteUserMedia } from "~/interface";
export default function (player: HTMLElement, media: AgoraRtcVueRemoteUserMedia, debugMode: boolean, client: IAgoraRTCClient, timers: {
    [k: string]: number | null;
}): void;
