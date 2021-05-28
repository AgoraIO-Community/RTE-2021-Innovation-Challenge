import { IAgoraRTCClient } from "agora-rtc-sdk-ng";
import { Vue } from "vue-property-decorator";
import { AgoraRtcVueRemoteUserMediaList } from "~/interface";
export default class DebugModeMethods extends Vue {
    subscribeList: AgoraRtcVueRemoteUserMediaList;
    client: IAgoraRTCClient;
    debugData: {
        [k: string]: any;
    };
    mounted(): void;
    __getTrackStats(): void;
}
