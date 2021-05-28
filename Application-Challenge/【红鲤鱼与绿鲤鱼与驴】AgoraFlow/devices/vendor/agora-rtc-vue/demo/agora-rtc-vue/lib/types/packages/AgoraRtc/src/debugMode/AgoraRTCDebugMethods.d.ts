import { Vue } from "vue-property-decorator";
import { IAgoraRTCClient } from "agora-rtc-sdk-ng";
import { IDebugData, IRtcData } from "~/interface";
export default class MixinComponent extends Vue {
    client: IAgoraRTCClient | null;
    debugData: IDebugData;
    rtcData: IRtcData;
    __getPlaybackDevices(): Promise<void>;
    __getMediaDevices(): Promise<void>;
    __getSupportedCodec(): Promise<{
        video: string[];
        audio: string[];
    } | undefined>;
    __checkSystemRequirements(): boolean;
}
