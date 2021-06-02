import * as faceLandmarksDetection from '@tensorflow-models/face-landmarks-detection';
import { MediaPipeFaceMesh } from "@tensorflow-models/face-landmarks-detection";
import * as tf from '@tensorflow/tfjs-core';
import '@tensorflow/tfjs-backend-webgl';
import '@tensorflow/tfjs-backend-cpu';

import AgoraRTC from "agora-rtc-sdk-ng";
import EventEmitter from "events";

import * as tfjsWasm from '@tensorflow/tfjs-backend-wasm';


const state = {
    backend: 'webgl',
    maxFaces: 1,
    triangulateMesh: true,
    predictIrises: true
};

class FaceModel {
    async init(localDom) {
        this.emitter = new EventEmitter();
        // init tf face model
        await tf.setBackend("webgl");

        this.model = await faceLandmarksDetection.load(faceLandmarksDetection.SupportedPackages.mediapipeFacemesh, {
            maxFaces: 1,
            modelUrl: "/models/model/face.json",
            detectorModelUrl: "/models/detect/detect.json",
            irisModelUrl: "/models/iris/iris.json",
        });

        this.video = await AgoraRTC.createCameraVideoTrack({
            // use device default config
            encoderConfig: {
            },
        });
        if (localDom) {
            this.video.play(localDom);
        }
    }

    // call in RAF
    async predict() {
        let imageData = null;
        try {
            imageData = this.video.getCurrentFrameData();
        } catch (e) {
            return null;
        }
        const predictions = await this.model.estimateFaces({
            input: imageData,
            returnTensors: false,
            flipHorizontal: false,
            predictIrises: state.predictIrises
        })

        // console.log(predictions);

        return predictions;
    }
}

export const faceModel = new FaceModel();