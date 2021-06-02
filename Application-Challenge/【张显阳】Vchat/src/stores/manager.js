import { observable, action, makeAutoObservable } from "mobx";
import React from "react";
import { MobXProviderContext } from "mobx-react";
import AgoraRTM from "agora-rtm-sdk";
import AgoraRTC from "agora-rtc-sdk-ng";
import { VrmModel } from "../utils/vrm";

const APPID = "68e14615de7846b398aeff6dae1762a9";

export class ManagerStore {
    roomid = ""
    uid = Math.random().toString().slice(3, 7)
    vrmList = {}

    constructor() {
        makeAutoObservable(this, {
            roomid: true,
            vrmList: true,
        });

        this.rtmClient = AgoraRTM.createInstance(APPID);
        this.rtcClient = AgoraRTC.createClient({
            codec: "vp8",
            mode: "rtc",
        });
        this.handleRTCEvents();
    }

    setRoomId(id) {
        this.roomid = id;
    }

    async joinRoom() {
        if (!this.roomid) {
            alert("roomid is null");
            return;
        }

        this.rtmChannel = this.rtmClient.createChannel(this.roomid);
        this.handleRTMEvents();
        await this.rtmClient.login({
            uid: this.uid,
        });
        this.rtmChannel.join();
        this.localAudio = await AgoraRTC.createMicrophoneAudioTrack();
        await this.rtcClient.join(APPID, this.roomid, null, this.uid);
        await this.rtcClient.publish(this.localAudio);
    }

    async upload(vrm) {
        let data = {
            name: vrm.name,
            headX: vrm.headX,
            headZ: vrm.headZ,
            headY: vrm.headY,
            mouth: vrm.mouth,
            leftEye: vrm.leftEye,
            rightEye: vrm.rightEye,
        };
        await this.rtmChannel.sendMessage({
            text: JSON.stringify(data),
        });
    }

    handleRTCEvents() {
        this.rtcClient.on("user-published", async (user, type) => {
            const remoteAudio = await this.rtcClient.subscribe(user, "audio");
            remoteAudio.play();
        });
    }

    handleRTMEvents() {
        this.rtmChannel.on("ChannelMessage", (message, id) => {
            let data = JSON.parse(message.text);
            if (!this.vrmList[id]) {
                this.vrmList[id] = new VrmModel();
                this.vrmList[id].initVrm(data.name).catch(alert);
            } else {
                this.vrmList[id].updateData(data);
                if (!this.vrmList[id].scene) {
                    const dom = document.getElementById(id);
                    if (dom) {
                        this.vrmList[id].initScene(dom);
                    }
                }
            }
        });
    }
}

export function useManager() {
    const context = React.useContext(MobXProviderContext);

    return context.store.manager;
}