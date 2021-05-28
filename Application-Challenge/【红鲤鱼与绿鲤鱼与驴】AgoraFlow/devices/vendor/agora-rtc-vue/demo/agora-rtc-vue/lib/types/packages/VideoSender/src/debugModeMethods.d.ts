import { IAgoraRTCClient, ILocalVideoTrack } from "agora-rtc-sdk-ng";
import { Vue } from "vue-property-decorator";
import { IRtcData } from "~/interface";
export default class DebugModeMethods extends Vue {
    track: ILocalVideoTrack | null;
    client: IAgoraRTCClient | null;
    rtcData: IRtcData | null;
    tid: string;
    debugData: any;
    mounted(): void;
    __getTrackStats(): void;
}
