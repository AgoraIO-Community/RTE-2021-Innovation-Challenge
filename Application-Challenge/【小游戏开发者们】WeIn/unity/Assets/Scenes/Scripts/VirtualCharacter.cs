using UnityEngine;
using System;
using VLiveHelper;

public class VirtualCharacter : MonoBehaviour {

    private Quaternion rot = Quaternion.identity;
    private Vector3 pos = Vector3.zero;

    public void OnMessageReceived(byte[] msg) {
        Debug.Log("Receive virtual character message, len = " + msg.Length);

        Tuple<Quaternion, Vector3> tuple = MessageHelper.UnpackMessage(msg);
        rot = tuple.Item1;
        pos = tuple.Item2;
    }
    
    void Update() {
        transform.rotation = rot;
        transform.position = pos;
    }
}