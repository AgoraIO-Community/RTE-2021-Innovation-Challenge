using UnityEngine;
using Hi5_Interaction_Core;
using System.Collections.Generic;
using VLiveHelper;

public class VLiveManipulator : MonoBehaviour {
    public GameObject cameraOffset;
    public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
    public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;
    private Hi5_Object_JudgeMent hi5_object_judgeMent_1;
    private Hi5_Object_JudgeMent hi5_object_judgeMent_2;

    private UnityEngine.Object cubeModel;

    public VLiveManager vliveManager;

    private const float MOVING_SCALE_FACTOR = 0.02f;
    private const float MIN_CREATE_THRESHOLD = 0.05f;
    private const float MAX_DELETE_DIST = 5f;
    private const int IDLE = 0;
    private const int MOVING = 1;
    private const int CREATE = 2;
    private const int SELECT = 3;
    private const int DELETE = 4;
    private const int MENU = 5;
    private int state = IDLE;
    
    void Awake() {
        hi5_object_judgeMent_1 = new Hi5_Object_JudgeMent();
        hi5_object_judgeMent_2 = new Hi5_Object_JudgeMent();

        hi5_object_judgeMent_1.Hand = HI5_Left_Human_Collider;
        hi5_object_judgeMent_2.Hand = HI5_Right_Human_Collider;

        cubeModel = Resources.Load("cube");
    }

    private bool isShowingMenu() {
        return hi5_object_judgeMent_1.IsThree() && hi5_object_judgeMent_2.IsThree();
    }

    private bool isSelectMenu() {
        return state == MENU && !(
            hi5_object_judgeMent_2.IsHandIndexPoint() || hi5_object_judgeMent_2.IsTwo()
        );
    }

    private bool isMoving() {
        return hi5_object_judgeMent_2.IsHandIndexPoint();
    }

    private bool isContinueCreatingObject() {
        return state == CREATE && !(hi5_object_judgeMent_1.IsHandFist() && hi5_object_judgeMent_2.IsHandFist());
    }

    private bool isCreateObject() {
        bool isPlane = hi5_object_judgeMent_1.IsFingerPlane() && hi5_object_judgeMent_2.IsFingerPlane();
        float dist = (HI5_Left_Human_Collider.GetThumbAndMiddlePoint() - HI5_Right_Human_Collider.GetThumbAndMiddlePoint()).magnitude;
        return isPlane && ((dist < MIN_CREATE_THRESHOLD) || (state == CREATE));
    }

    private bool isSelectObject() {
        return (hi5_object_judgeMent_2.IsHandFist() || hi5_object_judgeMent_1.IsHandFist()) && state != CREATE;
    }

    private bool isDeleteObject() {
        return hi5_object_judgeMent_1.IsOK() && hi5_object_judgeMent_2.IsHandFist();
    }

    private int getGestureState() {
        if (isContinueCreatingObject() || isCreateObject()) return CREATE;
        else if (isShowingMenu() || isSelectMenu()) return MENU;
        else if (isMoving()) return MOVING;
        else if (isDeleteObject()) return DELETE;
        else if (isSelectObject()) return SELECT;
        else return IDLE;
    }

    private bool canSendMessage() {
        return vliveManager != null && (count % 3 == 0);
    }

    private void onMoving() {
        Vector3 v1 = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position;
        Vector3 v2 = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[1].transform.position;
        Vector3 delta = v1 - v2;
        // delta.y = 0;
        delta.Normalize();
        cameraOffset.transform.position += delta * MOVING_SCALE_FACTOR;
    }

    private void setObjectData() {
        Vector3 p1 = HI5_Left_Human_Collider.mPalm.transform.position;
        Vector3 p2 = HI5_Right_Human_Collider.mPalm.transform.position;
        Vector3 pos = (p1 + p2) / 2;
        creating.transform.position = pos;
        Vector3 size = p2 - p1;
        Vector3 scale = new Vector3(Mathf.Max(0.1f, Mathf.Abs(size.x)), Mathf.Max(0.1f, Mathf.Abs(size.y)), Mathf.Max(0.1f, Mathf.Abs(size.z)));
        creating.transform.localScale = scale;
    }

    private GameObject creating;
    private int objCount = 0;
    private Dictionary<int, VLiveObjectHandler> objMap = new Dictionary<int, VLiveObjectHandler>();
    private byte type = 0;
    private void onCreateObject() {
        Debug.Log("onCreateObject");
        creating = GameObject.Instantiate(cubeModel, new Vector3(.0f, -10f, .0f), Quaternion.identity) as GameObject;
        // creating = GameObject.CreatePrimitive(type);
        creating.name = "Created_" + objCount;
        VLiveObjectHandler handler = creating.AddComponent<VLiveObjectHandler>();
        objMap.Add(objCount++, handler);
        handler.type = type;
        setObjectData();

        byte[] msg = MessageHelper.packAddMessage(creating);
        vliveManager.SendMessageToObjectChannel(msg);
    }

    private void onCreatingObject() {
        Debug.Log("onCreatingObject");
        setObjectData();

        if (canSendMessage()) {
            byte[] msg = MessageHelper.packAddMessage(creating);
            vliveManager.SendMessageToObjectChannel(msg);
        }
    }

    private void onCreateEnd() {
        Debug.Log("OnCreateEnd");
    }

    VLiveObjectHandler selected_left = null;
    VLiveObjectHandler selected_right = null;

    private int getPairByPosition(Vector3 position, out VLiveObjectHandler handler) {
        float minDist = 1000f;
        int id = -1;
        handler = null;
        foreach (KeyValuePair<int,VLiveObjectHandler> pair in objMap) {
            float dist = pair.Value.CenterDist(position);
            if (dist < minDist) {
                id = pair.Key;
                handler = pair.Value;
                minDist = dist;
            }
        }
        return id;
    }

    private void onManipulateObject() {
        Debug.Log("Manipulating");
        // process for left hand
        if (hi5_object_judgeMent_1.IsHandFist()) {
            if (selected_left != null) {
                selected_left.OnMoving();
                if (canSendMessage()) {
                    byte[] msg = MessageHelper.packAddMessage(selected_left.gameObject);
                    vliveManager.SendMessageToObjectChannel(msg);
                }
            } else {
                Transform t = HI5_Left_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform;
                VLiveObjectHandler handler;
                if (getPairByPosition(t.position, out handler) != -1) {
                    selected_left = handler;
                    handler.OnBindTransform(t);
                }
            }
        } else {
            if (selected_left != null) {
                selected_left.UnBind();
                selected_left = null;
            }
        }
        // process for right hand
        if (hi5_object_judgeMent_2.IsHandFist()) {
            if (selected_right != null) {
                selected_right.OnMoving();
                if (canSendMessage()) {
                    byte[] msg = MessageHelper.packAddMessage(selected_right.gameObject);
                    vliveManager.SendMessageToObjectChannel(msg);
                }
            } else {
                Transform t = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform;
                VLiveObjectHandler handler;
                if (getPairByPosition(t.position, out handler) != -1) {
                    selected_right = handler;
                    handler.OnBindTransform(t);
                }
            }
        } else {
            if (selected_right != null) {
                selected_right.UnBind();
                selected_right = null;
            }
        }
    }

    private void onManipulatingObjectEnd() {
        Debug.Log("Manipulate End");
        if (selected_left != null) {
            selected_left.UnBind();
            selected_left = null;
        }
        if (selected_right != null) {
            selected_right.UnBind();
            selected_right = null;
        }
    }

    private void onDeleteObject() {
        Debug.Log("onDeleteObject");
        Vector3 pos = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position;
    
        VLiveObjectHandler handler;
        int id = getPairByPosition(pos, out handler);
        if (id != -1) {
            GameObject go = handler.gameObject;

            byte[] msg = MessageHelper.packRemoveMessage(go);
            vliveManager.SendMessageToObjectChannel(msg);

            Debug.Log("Delete object " + go.name);
            Destroy(go);
            objMap.Remove(id);
        }
    }

    public GameObject menu;

    private void onCreateMenu() {
        menu.SetActive(true);
    }

    private void onRemoveMenu() {
        menu.SetActive(false);
        if (hi5_object_judgeMent_2.IsTwo()) {
            type = 1;
        } else {
            type = 0;
        }
    }

    private void processOnStart(int state) {
        Debug.Log("Switch to state " + state);
        switch (state) {
        case CREATE:
            onCreateObject();
            break;
        case SELECT:
            onManipulateObject();
            break;
        case MENU:
            onCreateMenu();
            break;
        case DELETE:
            onDeleteObject();
            break;
        default:
            break;
        }
    }

    private void processOnContinue(int state) {
        switch (state) {
        case CREATE:
            onCreatingObject();
            break;
        case MOVING:
            onMoving();
            break;
        case SELECT:
            onManipulateObject();
            break;
        default:
            break;
        }
    }

    private void processOnEnd(int state) {
        switch (state) {
        case CREATE:
            onCreateEnd();
            break;
        case SELECT:
            onManipulatingObjectEnd();
            break;
        case MENU:
            onRemoveMenu();
            break;
        default:
            break;
        }
    }

    int count = 0;
    void Update() {
        count++;
        int lastState = this.state;
        this.state = getGestureState();

        if (lastState == state) processOnContinue(state);
        else {
            processOnEnd(lastState);
            processOnStart(state);
        }
    }
}