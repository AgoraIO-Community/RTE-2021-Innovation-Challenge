using UnityEngine;
using System;

public class CameraRecorder : MonoBehaviour {
    public VLiveManager objectManager;
    private byte[] identity = CameraRecorder.getBytes(1f);
    private byte[] identityQ = new byte[16];

    static byte[] getBytes(float f) {
        byte[] raw = BitConverter.GetBytes(f);
        byte a = raw[0];
        byte b = raw[1];
        raw[0] = raw[3];
        raw[1] = raw[2];
        raw[2] = b;
        raw[3] = a;
        return raw;
    }

    void Start() {
        Quaternion q = Quaternion.identity;
        for (int i = 0; i < 4; i++) {
            CameraRecorder.getBytes(q[i]).CopyTo(identityQ, i * 4);
        }
    }

    // array:
    // lEyeOpenProbability, rEyeOpenProbability, mouthWeight
    // faceQuaternion (float * 4)
    // objectQuaternion (float * 4)
    // objectPosition (Vector3)
    int count = 0;
    void Update() {
        count++;
        if (count % 3 == 0 && objectManager != null) {
            sendRotationMessage();
        }
    }

    private void sendRotationMessage() {
        byte[] array = new byte[56];
        for (int i = 0; i < 3; i++) {
            identity.CopyTo(array, i * 4);
        }
        
        identityQ.CopyTo(array, 12);
        Quaternion q0 = transform.rotation;
        Quaternion q = new Quaternion(q0.x, -q0.y, -q0.z, q0.w) * Quaternion.Euler(0, 180, 0);
        for (int i = 0; i < 4; i++) {
            CameraRecorder.getBytes(q[i]).CopyTo(array, 28 + i * 4);
        }
        Vector3 p0 = transform.position;
        Vector3 p = new Vector3(-p0.x, p0.y, p0.z);
        for (int i = 0; i < 3; i++) {
            CameraRecorder.getBytes(p[i]).CopyTo(array, 44 + i * 4);
        }
        objectManager.SendMessageToUserChannel(array);
    }
}