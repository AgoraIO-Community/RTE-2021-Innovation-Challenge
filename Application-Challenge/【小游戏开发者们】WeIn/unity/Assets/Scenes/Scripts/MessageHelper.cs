using UnityEngine;
using System;

namespace VLiveHelper {
    public class MessageHelper {
        public static Tuple<Quaternion, Vector3> UnpackMessage(byte[] msg) {
            if (msg.Length != 56) {
                Debug.LogError("Unpack failed for invalid length");
                return new Tuple<Quaternion, Vector3>(Quaternion.identity, Vector3.zero);
            }

            for (int i = 28; i < 56; i += 4) {
                byte a = msg[i];
                byte b = msg[i+1];
                msg[i] = msg[i+3];
                msg[i+1] = msg[i+2];
                msg[i+3] = a;
                msg[i+2] = b;
            }

            float qX = BitConverter.ToSingle(msg, 28);
            float qY = BitConverter.ToSingle(msg, 32);
            float qZ = BitConverter.ToSingle(msg, 36);
            float qW = BitConverter.ToSingle(msg, 40);
            float pX = BitConverter.ToSingle(msg, 44);
            float pY = BitConverter.ToSingle(msg, 48);
            float pZ = BitConverter.ToSingle(msg, 52);

            Quaternion rot = new Quaternion(qX, -qY, -qZ, qW) * Quaternion.Euler(0, 180, 0);
            Vector3 pos = new Vector3(-pX, pY, pZ);

            return new Tuple<Quaternion, Vector3>(rot, pos);
        }

        public static void flipByteOrder(byte[] bytes) {
            for (int i = 0; i < bytes.Length / 2; i++) {
                byte b = bytes[i];
                bytes[i] = bytes[bytes.Length - i - 1];
                bytes[bytes.Length - i - 1] = b;
            }
        }

        public static void putInBytes(byte[] msg, int offset, byte[] data) {
            for (int i = 0; i < data.Length; i++) {
                msg[offset + i] = data[i];
            }
        }

        public static void putVectorInBytes(byte[] msg, int offset, Vector3 v) {
            for (int i = 0; i < 3; i++) {
                byte[] fb = BitConverter.GetBytes(v[i]);
                flipByteOrder(fb);
                putInBytes(msg, offset + i * 4, fb);
            }
        }

        public static void putQuaternionInBytes(byte[] msg, int offset, Quaternion q) {
            for (int i = 0; i < 4; i++) {
                byte[] fb = BitConverter.GetBytes(q[i]);
                flipByteOrder(fb);
                putInBytes(msg, offset + i * 4, fb);
            }
        }

        public static byte[] packAddMessage(GameObject go) {
            byte[] res = new byte[44];
            res[0] = 0;
            
            string s = go.name.Substring(8);
            UInt16 id = UInt16.Parse(s);
            byte[] idb = BitConverter.GetBytes(id);
            res[1] = idb[1];
            res[2] = idb[0];
            
            Transform t = go.transform;
            Vector3 pos = t.position;
            putVectorInBytes(res, 3, new Vector3(-pos.x, pos.y, pos.z));

            Quaternion q0 = t.rotation;
            Quaternion q = new Quaternion(q0.x, -q0.y, -q0.z, q0.w) * Quaternion.Euler(0, 180, 0);
            putQuaternionInBytes(res, 15, q);

            putVectorInBytes(res, 31, t.localScale);

            VLiveObjectHandler handler = go.GetComponent<VLiveObjectHandler>();
            res[43] = handler.type;

            return res;
        }

        public static byte[] packAddMessage(GameObject[] objects) {
            int size = 1 + objects.Length * 43;
            byte[] res = new byte[size];
            res[0] = 0;

            int offset = 1;
            foreach (GameObject go in objects) {

                string s = go.name.Substring(8);
                UInt16 id = UInt16.Parse(s);
                byte[] idb = BitConverter.GetBytes(id);
                res[offset] = idb[1];
                res[offset + 1] = idb[0];
                
                Transform t = go.transform;
                Vector3 pos = t.position;
                putVectorInBytes(res, offset + 2, new Vector3(-pos.x, pos.y, pos.z));

                Quaternion q0 = t.rotation;
                Quaternion q = new Quaternion(q0.x, -q0.y, -q0.z, q0.w) * Quaternion.Euler(0, 180, 0);
                putQuaternionInBytes(res, offset + 14, q);

                putVectorInBytes(res, offset + 30, t.localScale);

                VLiveObjectHandler handler = go.GetComponent<VLiveObjectHandler>();
                res[offset + 42] = handler.type;

                offset += 43;
            }

            return res;
        }

        public static byte[] packRemoveMessage(GameObject go) {
            byte[] res = new byte[3];
            res[0] = 1;

            string s = go.name.Substring(8);
            UInt16 id = UInt16.Parse(s);

            byte[] idb = BitConverter.GetBytes(id);
            res[1] = idb[1];
            res[2] = idb[0];
            return res;
        }

        public static byte[] packRemoveMessage(GameObject[] objects) {
            int size = 1 + objects.Length * 2;
            byte[] res = new byte[size];
            res[0] = 1;

            int offset = 1;
            foreach (GameObject go in objects) {
                string s = go.name.Substring(8);
                UInt16 id = UInt16.Parse(s);

                byte[] idb = BitConverter.GetBytes(id);
                res[offset] = idb[1];
                res[offset + 1] = idb[0];
                offset += 2;
            }

            return res;
        }
    }
}
