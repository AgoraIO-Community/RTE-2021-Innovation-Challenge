import { IAgoraRTCClient, ILocalAudioTrack } from "agora-rtc-sdk-ng";
import { Vue } from "vue-property-decorator";
export default class DebugMethod extends Vue {
    client: IAgoraRTCClient;
    track: ILocalAudioTrack;
    debugData: {};
    mounted(): void;
    __getTrackStats(): void;
}
