import { Vue } from "vue-property-decorator";
import { IRtcData } from "~/interface";
import { IAgoraRTCClient, IAgoraRTC } from "agora-rtc-sdk-ng";
declare const MixinComponent_base: import("vue-class-component/lib/declarations").VueClass<unknown>;
export default class MixinComponent extends MixinComponent_base {
    getRtcData: () => IRtcData;
    rtcEle: Vue;
    debugMode: boolean;
    get rtcData(): IRtcData;
    get client(): IAgoraRTCClient | null;
    set client(newV: IAgoraRTCClient | null);
    get AgoraRTC(): IAgoraRTC | null;
    get hasJoin(): boolean;
}
export {};
