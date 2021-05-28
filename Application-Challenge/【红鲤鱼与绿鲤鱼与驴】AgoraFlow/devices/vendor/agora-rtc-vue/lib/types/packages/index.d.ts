import { PluginObject } from "vue";
import AgoraRtc from "./AgoraRtc/index";
import AudioReceiver from "./AudioReceiver/index";
import AudioSender from "./AudioSender/index";
import VideoReceiver from "./VideoReceiver/index";
import VideoSender from "./VideoSender/index";
import Player from "#/directives/player";
/**
 * @public
 */
export interface InstallConfig {
    appid: string;
    token?: string | null;
}
/***
 * @public
 */
declare const AgoraRtcVue: PluginObject<InstallConfig>;
/***
 * @public
 */
export { AgoraRtc, AudioReceiver, AudioSender, VideoReceiver, VideoSender, Player };
/**
 * component default api
 */
export default AgoraRtcVue;
