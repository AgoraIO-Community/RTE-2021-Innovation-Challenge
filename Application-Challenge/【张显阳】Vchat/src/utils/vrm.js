import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader';
import { VRM, VRMUtils, VRMSchema } from '@pixiv/three-vrm';
import { render } from '@testing-library/react';

export class VrmModel {
    initScene(dom) {
        this.renderer = new THREE.WebGLRenderer();
        this.renderer.setPixelRatio(window.devicePixelRatio);
        this.renderer.setSize(dom.clientWidth, dom.clientHeight);
        dom.appendChild(this.renderer.domElement);

        this.camera = new THREE.PerspectiveCamera( 30.0, dom.clientWidth / dom.clientHeight, 0.1, 20.0 );
        this.camera.position.set( 0.0, 1.47, 0.7 );

        this.scene = new THREE.Scene();

        this.light = new THREE.DirectionalLight( 0xffffff );
        this.light.position.set( 1.0, 1.0, 1.0 ).normalize();
        this.scene.add(this.light);

        if (this.vrm) {
            this.scene.add(this.vrm.scene);
        }
    }

    initVrm(name) {
        this.name = name;
        const loader = new GLTFLoader();
        return new Promise((res, rej) => {
            loader.load(`./models/${name}.vrm`, (gltf) => {

            VRMUtils.removeUnnecessaryJoints( gltf.scene );

            VRM.from(gltf).then(vrm => {
                this.vrm = vrm;
                this.vrm.humanoid.getBoneNode( VRMSchema.HumanoidBoneName.Hips ).rotation.y = Math.PI;
                this.vrm.humanoid.getBoneNode( VRMSchema.HumanoidBoneName.LeftUpperArm).rotation.z = Math.PI * 0.25;
                this.vrm.humanoid.getBoneNode( VRMSchema.HumanoidBoneName.RightUpperArm).rotation.z = -Math.PI * 0.25;
                res();
            });
            }, () => {}, (e) => {
                rej(e);
            });
        });
    }

    updateData(data) {
        this.headX = data.headX;
        this.headY = data.headY;
        this.headZ = data.headZ;
        this.mouth = data.mouth;
        this.leftEye = data.leftEye;
        this.rightEye = data.rightEye;
    }

    updateHead(scaleMesh) {
        if (!this.vrm) {
            return;
        }

        let head_right = scaleMesh[454];
        let head_left = scaleMesh[234];

        let headRight = new THREE.Vector3(head_right[0], 0, head_right[2]);
        let headLeft = new THREE.Vector3(-head_left[0], 0, -head_left[2]);
        
        let leftRight = headRight.add(headLeft);
        let xAxis = new THREE.Vector3(1, 0, 0);

        this.headY = xAxis.angleTo(leftRight);
        this.headY = Math.round(this.headY * 20) / 20;

        if (head_right[2] > head_left[2]) {
            this.headY = -this.headY;
        }

        let headRight2 = new THREE.Vector3(head_right[0], head_right[1], 0);
        let headLeft2 = new THREE.Vector3(head_left[0], head_left[1], 0);
        let leftRight2 = headRight2.sub(headLeft2);
        let yAxis = new THREE.Vector3(0, 1, 0);

        this.headZ = yAxis.angleTo(leftRight2) - (Math.PI / 2);
        this.headZ = Math.round(this.headZ * 20) / 20;

        let head_top = scaleMesh[10];
        let head_bottom = scaleMesh[152];

        let headTop = new THREE.Vector3(0, head_top[1], head_top[2]);
        let headBottom = new THREE.Vector3(0, head_bottom[1], head_bottom[2]);

        let topBottom = headTop.sub(headBottom);
        let yAxis2 = new THREE.Vector3(0, 1, 0);

        this.headX = yAxis2.angleTo(topBottom) - Math.PI;
        if (head_top[2] > head_bottom[2]) {
            this.headX = -this.headX;
        }
    }

    updateEyes(scaleMesh) {
        if (!this.vrm) {
            return;
        }

        let left_top = scaleMesh[159];
        let left_bottom = scaleMesh[145];
        let right_top = scaleMesh[386];
        let right_bottom = scaleMesh[374];

        let std_top = scaleMesh[9];
        let std_bottom = scaleMesh[18];
        let std_distance = getDistance(std_top, std_bottom);

        let left_distance = getDistance(left_top, left_bottom);
        let right_distance = getDistance(right_top, right_bottom);

        this.leftEye = left_distance / std_distance;
        this.rightEye = right_distance / std_distance;

        this.leftEye = 1 - ((Math.min(Math.max(this.leftEye, 0.05), 0.065) - 0.05) / 0.015);
        this.rightEye = 1 - ((Math.min(Math.max(this.rightEye, 0.05), 0.065) - 0.05) / 0.015);

        //this.leftEye = Math.round(this.leftEye * 4) / 4;
        //this.rightEye = Math.round(this.rightEye * 4) / 4;

        if (this.leftEye < 0.1) {
            this.leftEye = 0;
        } else {
            this.leftEye = 1;
        }
        if (this.rightEye < 0.1) {
            this.rightEye = 0;
        } else {
            this.rightEye = 1;
        }
        // console.log("left", left_distance / std_distance, "right", right_distance / std_distance);
    }

    updateMouth(scaleMesh) {
        if (!this.vrm) {
            return;
        }
        let top = scaleMesh[13];
        let bottom = scaleMesh[14];
        let left = scaleMesh[78];
        let right = scaleMesh[308];


        let value = ((3 * getDistance(bottom, top) / getDistance(right, left)) - 0.3);
        this.mouth = Math.min(1.0, Math.max(value, 0.0));
        // console.log("mouth value", value, getDistance(bottom, top), getDistance(right, left));

    }

    render() {
        if (this.vrm && this.scene) {

            this.vrm.blendShapeProxy.setValue(VRMSchema.BlendShapePresetName.A, this.mouth);
            this.vrm.blendShapeProxy.setValue(VRMSchema.BlendShapePresetName.BlinkL, this.leftEye);
            this.vrm.blendShapeProxy.setValue(VRMSchema.BlendShapePresetName.BlinkR, this.rightEye);
            this.vrm.blendShapeProxy.update();

            this.vrm.humanoid.getBoneNode(VRMSchema.HumanoidBoneName.Head).rotation.y = this.headY;
            this.vrm.humanoid.getBoneNode(VRMSchema.HumanoidBoneName.Head).rotation.z = this.headZ;
            this.vrm.humanoid.getBoneNode(VRMSchema.HumanoidBoneName.Head).rotation.x = this.headX;

            this.renderer.render(this.scene, this.camera);
        }
    }
}

const getDistance = (p1, p2) => {
    const a = new THREE.Vector3(p1[0], p1[1], p1[2]);
    const b = new THREE.Vector3(p2[0], p2[1], p2[2]);

    return a.distanceTo(b);
}