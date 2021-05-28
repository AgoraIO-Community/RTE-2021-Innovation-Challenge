import { IAgoraRTC } from "agora-rtc-sdk-ng";
export default function (AgoraRTC: IAgoraRTC, method: keyof IAgoraRTC, ...argu: any[]): Promise<any>;
