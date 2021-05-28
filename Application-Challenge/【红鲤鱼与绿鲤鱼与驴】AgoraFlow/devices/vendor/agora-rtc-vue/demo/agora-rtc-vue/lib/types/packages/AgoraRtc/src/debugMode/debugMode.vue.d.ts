import { Vue } from "vue-property-decorator";
import { IDebugData } from "~/interface";
export default class DebugMode extends Vue {
    data: IDebugData;
    get mediaDeviceInfoList(): {
        [k: string]: string;
    };
    get supportedCodec(): {
        [k: string]: string;
    };
    get downlinkNetworkQuality(): string;
    get uplinkNetworkQuality(): string;
}
