import { IAgoraRTCClient } from "agora-rtc-sdk-ng";
import { Vue } from "vue-property-decorator";
import { IDebugData } from "~/interface";
export default class MixinComponent extends Vue {
    client: IAgoraRTCClient | null;
    debugData: IDebugData;
    __getRTCStats(): void;
}
