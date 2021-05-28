using UnityEngine;
using System;
using agora_gaming_rtc;
using VLiveHelper;

public class VideoCharacter : MonoBehaviour {

    private Quaternion rot = Quaternion.identity;
    private Vector3 pos = Vector3.zero;
    private VideoSurface surface;

    void Awake() {
        GameObject go = transform.Find("body/body/screen").gameObject;
        if (go != null) {
            surface = go.AddComponent<VideoSurface>();
            Debug.Log("Find screen node ok");

            MeshFilter mesh = go.GetComponent<MeshFilter>();
            Vector2[] uv = mesh.mesh.uv;
            for (int i = 0; i < uv.Length; i++) {
                uv[i] = new Vector2(1 - uv[i].y, 1 - uv[i].x);
            }
            mesh.mesh.SetUVs(0, uv);
        } else {
            Debug.LogError("Could not find screen node");
        }
    }

    public void SetEnable(bool enable) {
        if (surface != null) {
            surface.SetEnable(true);
        } else {
            Debug.LogError("Could not set enable");
        }
    }

    public void SetForUser(uint uid) {
        if (surface != null) {
            surface.SetForUser(uid);
        } else {
            Debug.LogError("Could not set for user");
        }
    }

    public void OnMessageReceived(byte[] msg) {
        Debug.Log("Receive video character message, len = " + msg.Length);

        Tuple<Quaternion, Vector3> tuple = MessageHelper.UnpackMessage(msg);
        rot = tuple.Item1;
        pos = tuple.Item2;
    }

    void Update() {
        transform.rotation = rot;
        transform.position = pos;
    }
}