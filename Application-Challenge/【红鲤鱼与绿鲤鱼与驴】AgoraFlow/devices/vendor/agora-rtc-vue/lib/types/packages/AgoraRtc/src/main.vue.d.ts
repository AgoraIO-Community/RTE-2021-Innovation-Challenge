import { ClientRole, ConnectionState, IAgoraRTC, IAgoraRTCClient, IAgoraRTCRemoteUser, ILocalTrack, LowStreamParameter, UID } from "agora-rtc-sdk-ng";
import { Vue } from "vue-property-decorator";
import { IRtcData, TMediaType, IDebugData, AgoraRtcVueClientConfig } from "~/interface";
declare const Ar_base: import("vue-class-component/lib/declarations").VueClass<unknown>;
export default class Ar extends Ar_base {
    __checkSystemRequirements: () => boolean;
    __getPlaybackDevices: () => void;
    __getMediaDevices: () => void;
    __getRTCStats: () => void;
    __getSupportedCodec: () => Promise<{
        video: string[];
        audio: string[];
    } | undefined>;
    clientConfig: AgoraRtcVueClientConfig;
    appid: string;
    uid: UID | undefined | null;
    channel: string;
    token: string | null;
    enableDualStream: boolean;
    lowStreamParameter: LowStreamParameter;
    errorHandler: (error: Error, vm: Vue, info: string) => boolean | void;
    /***
     * auto start create client and join channel
     */
    autoStart: boolean;
    debugMode: boolean;
    hasError: boolean;
    demoError: boolean;
    loading: boolean;
    rtcEle: this;
    getRtcData: () => IRtcData;
    errorHandlerProvided: (error: Error, vm: Vue, info: string) => boolean | void;
    debugModeProvided: boolean;
    unsubscribeFunc: (uid: UID, type?: "audio" | "video" | undefined) => Promise<undefined>;
    enableDualStreamChange(newValue: boolean): Promise<void>;
    roleChange(newValue: ClientRole): Promise<void>;
    tokenChange(newValue: string | null, oldValue: string | null): Promise<void>;
    appidChange(newValue: string, oldValue: string): Promise<void>;
    channelChange(newValue: string, oldValue: string): Promise<void>;
    uidChange(newValue: UID | undefined | null, oldValue: UID | undefined | null): Promise<void>;
    get client(): IAgoraRTCClient | null;
    set client(newV: IAgoraRTCClient | null);
    get AgoraRTC(): IAgoraRTC | null;
    set AgoraRTC(newV: IAgoraRTC | null);
    get hasJoin(): boolean;
    set hasJoin(newV: boolean);
    debugData: IDebugData;
    options: {
        appid: string;
        channel: string;
        token: string | null;
    };
    rtcData: IRtcData;
    startLoading: boolean;
    startCallable: boolean;
    leaveCallable: boolean;
    hasClient: boolean;
    beforeCreate(): void;
    mounted(): Promise<void>;
    beforeDestroy(): Promise<void>;
    __importSDK(): Promise<IAgoraRTC | null>;
    __checkDevicesAndCreateClient(): Promise<void>;
    __listenClientEvents(): Promise<void>;
    /***
     * 1. show loading
     * 2. check devices support
     * 3. check codec support
     * 4. listen AgoraRTC Events
     * 5. create Client
     * 6. listen Client events
     * 7. set dual stream
     * 8. join channel
     */
    start(): Promise<{
        result: boolean;
        message: string;
    }>;
    /***
     * - create client
     * - set role
     */
    __createClient(): Promise<undefined>;
    /**
     * join channel &
     * get uid
     */
    __join(): Promise<Error | undefined>;
    /**
     * listen events on client
     */
    __eventListeners(): void;
    unpublish(option?: TMediaType): Promise<void>;
    unsubscribe(uid: UID, type?: TMediaType): Promise<undefined>;
    leave(): Promise<void>;
    /***
     * init component data
     */
    __init(): void;
    /**
     * @public
     */
    getAgoraRtc(): IAgoraRTC | null;
    __registerAgoraRTCEventsCallback(): void;
    callAgoraRTCMethods(...argu: [keyof IAgoraRTC, ...any[]]): Promise<any>;
    getVersion(): string;
    getClient(): IAgoraRTCClient | null;
    getConnectionState(): ConnectionState;
    getLocalTracks(): ILocalTrack[];
    /***
     * @public
     */
    getRemoteUsers(): IAgoraRTCRemoteUser[];
    getUid(): UID | undefined;
}
export {};
