using UnityEngine;

public class VLiveObjectHandler : MonoBehaviour {

    private Transform target;
    private Vector3 startTargetPos;
    // private Quaternion startTargetRot;
    private Vector3 startPos;
    // private Quaternion startRot;
    // 0: cubic
    public byte type;

    void Start() {}

    public float CenterDist(Vector3 pos) {
        Vector3 center = gameObject.transform.position;
        Vector3 scale = gameObject.transform.localScale;
        if (pos.x > center.x - scale.x && pos.x < center.x + scale.x
            && pos.y > center.y - scale.y && pos.y < center.y + scale.y
            && pos.z > center.z - scale.z && pos.z < center.z + scale.z) {
                return (pos - center).magnitude;
            }
        return 10000f;
    }

    public void OnBindTransform(Transform t) {
        target = t;
        startTargetPos = target.position;
        // startTargetRot = target.rotation;
        startPos = gameObject.transform.position;
        // startRot = gameObject.transform.rotation;
    }

    public void OnMoving() {
        if (target == null) return;
        gameObject.transform.position = startPos + (target.position - startTargetPos);
        // gameObject.transform.rotation = target.rotation * startTargetRot startRot * ;
    }

    public void UnBind() {
        target = null;
    }
}