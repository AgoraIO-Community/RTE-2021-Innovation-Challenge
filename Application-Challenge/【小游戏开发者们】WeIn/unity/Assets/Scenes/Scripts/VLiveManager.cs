using System.Collections.Generic;
using UnityEngine;
using System;
using agora_rtm;

public class VLiveManager : MonoBehaviour
{
    [Header("Rtm Config")]
    [SerializeField]
    private string AppID = "";
    [SerializeField]
    private string Token = "";
    [SerializeField]
    public string UserChannelName = "";
    public string ObjectChannelName = "";
    [SerializeField]
    private string UID = "";

    private RtmClient rtmClient = null;
    private RtmChannel userChannel = null;
    private RtmChannel objectChannel = null;
    private RtmClientEventHandler clientEventHandler;
    private RtmChannelEventHandler userChannelEventHandler;
    private RtmChannelEventHandler objectChannelEventHandler;

    private delegate void OnMessageReceived(byte[] msg);
    class VLiveUserObject {
        public GameObject obj;
        public OnMessageReceived callback;
        internal VLiveUserObject(GameObject obj, OnMessageReceived callback) {
            this.obj = obj;
            this.callback = callback;
        }
    }
    private Dictionary<string, VLiveUserObject> objMap = new Dictionary<string, VLiveUserObject>();
    private UnityEngine.Object virtualCharacterModel1;
    private UnityEngine.Object virtualCharacterModel2;
    private UnityEngine.Object videoCharacterModel;

    private byte inChannel = 0;

    // Start is called before the first frame update
    void Start()
    {
        virtualCharacterModel1 = Resources.Load("actor");
        virtualCharacterModel2 = Resources.Load("actor2");
        videoCharacterModel = Resources.Load("vActor");

        // initialize rtm events
        clientEventHandler = new RtmClientEventHandler();
        userChannelEventHandler = new RtmChannelEventHandler();
        objectChannelEventHandler = new RtmChannelEventHandler();
        // callEventHandler = new RtmCallEventHandler();
        rtmClient = new RtmClient(AppID, clientEventHandler);

        // clientEventHandler.OnQueryPeersOnlineStatusResult = OnQueryPeersOnlineStatusResultHandler;
        clientEventHandler.OnLoginSuccess = OnClientLoginSuccessHandler;
        clientEventHandler.OnLoginFailure = OnClientLoginFailureHandler;
        // clientEventHandler.OnMessageReceivedFromPeer = OnMessageReceivedFromPeerHandler;

        userChannelEventHandler.OnJoinSuccess = OnJoinSuccessHandler;
        userChannelEventHandler.OnJoinFailure = OnJoinFailureHandler;
        userChannelEventHandler.OnLeave = OnLeaveHandler;
        userChannelEventHandler.OnMessageReceived = OnChannelMessageReceivedHandler;
        userChannelEventHandler.OnMemberJoined = OnMemberJoinedHandler;
        userChannelEventHandler.OnMemberLeft = OnMemberLeftHandler;

        objectChannelEventHandler.OnJoinSuccess = OnJoinSuccessHandler;
        objectChannelEventHandler.OnJoinFailure = OnJoinFailureHandler;
        objectChannelEventHandler.OnMemberJoined = OnMemberJoinedHandlerForObject;

        // login
        rtmClient.Login(Token, UID);

        Debug.Log("Rtm init ok");
    }

    private void OnApplicationQuit() {
        if (userChannel != null) {
            userChannel.Dispose();
            userChannel = null;
        }
        if (objectChannel != null) {
            objectChannel.Dispose();
            objectChannel = null;
        }
        if (rtmClient != null) {
            rtmClient.Dispose();
            rtmClient = null;
        }
    }

    public void JoinChannel() {
        userChannel = rtmClient.CreateChannel(UserChannelName, userChannelEventHandler);
        userChannel.Join();
        objectChannel = rtmClient.CreateChannel(ObjectChannelName, objectChannelEventHandler);
        objectChannel.Join();
    }

    void OnClientLoginSuccessHandler(int id) {
        string msg = "client login successful! id = " + id;
        Debug.Log(msg);
        JoinChannel();
    }

    void OnClientLoginFailureHandler(int id, LOGIN_ERR_CODE errorCode) {
        string msg = "client login unsuccessful! id = " + id + " errorCode = " + errorCode;
        Debug.Log(msg);
    }

    void OnJoinSuccessHandler(int id) {
        string msg = "channel:" + UserChannelName + " OnJoinSuccess id = " + id;
        Debug.Log(msg);
        inChannel++;
    }

    void OnJoinFailureHandler(int id, JOIN_CHANNEL_ERR errorCode) {
        string msg = "channel OnJoinFailure  id = " + id + " errorCode = " + errorCode;
        Debug.Log(msg);
        inChannel--;
    }

    void OnLeaveHandler(int id, LEAVE_CHANNEL_ERR errorCode) {
        string msg = "client onleave id = " + id + " errorCode = " + errorCode;
        Debug.Log(msg);
    }
    
    void OnMemberJoinedHandler(int id, RtmChannelMember member) {
        string uid = member.GetUserId();
        
        if (!objMap.ContainsKey(uid)) {
            VLiveUserObject obj = new VLiveUserObject(null, null);
            objMap.Add(uid, obj);
            Debug.Log("Add uid: " + uid + " to object map");
        } else {
            string msg = "Duplicate Join channel, uid = " + uid;
            Debug.LogError(msg);
        }
    }

    void OnMemberJoinedHandlerForObject(int id, RtmChannelMember member) {
    }

    void OnMemberLeftHandler(int id, RtmChannelMember member) {
        string uid = member.GetUserId();
        if (objMap.ContainsKey(uid)) {
            GameObject obj = objMap[uid].obj;
            Destroy(obj);
            objMap.Remove(uid);
            Debug.Log("Remove uid: " + uid + " from object map");
        } else {
            string msg = "Non-exist user leave, id = " + uid;
            Debug.LogError(msg);
        }
    }

    void OnChannelMessageReceivedHandler(int id, string userId, TextMessage message) {
        if (objMap.ContainsKey(userId)) {
            VLiveUserObject vObj = objMap[userId];
            var type = message.GetMessageType();
            if (type == MESSAGE_TYPE.MESSAGE_TYPE_RAW) {
                dispatchRawMessage(vObj, message.GetRawMessageData());
            } else if (type == MESSAGE_TYPE.MESSAGE_TYPE_TEXT) {
                dispatchCreateObj(userId, vObj, message.GetText());
            } else {
                Debug.LogError("Received unknown type message: " + type + " from user " + userId);
            }
        } else {
            Debug.LogError("Received message from non-exist user, id = " + userId);
        }
    }

    public void SendMessageToUserChannel(byte[] msg) {
        if (inChannel < 2) {
            Debug.Log("Not in channel, could not send message");
            return;
        }
        Debug.Log("Sending user message...");
        userChannel.SendMessage(rtmClient.CreateMessage(msg));
    }

    public void SendMessageToObjectChannel(byte[] msg) {
        if (inChannel < 2) {
            Debug.Log("Not in channel, could not send message");
            return;
        }
        Debug.Log("Sending object message...");
        objectChannel.SendMessage(rtmClient.CreateMessage(msg));
    }

    private void dispatchRawMessage(VLiveUserObject vObj, byte[] msg) {
        if (vObj.callback != null) {
            vObj.callback(msg);
        } else {
            Debug.LogError("invalid message");
        }
    }

    private void dispatchCreateObj(string user, VLiveUserObject vObj, string msg) {
        if (msg[0] == '1' || msg[0] == '2') {
            UnityEngine.Object model;
            if (msg[0] == '1') {
                model = virtualCharacterModel1;
            } else {
                model = virtualCharacterModel2;
            }
            GameObject obj = GameObject.Instantiate(model, new Vector3(.0f, -10f, .0f), Quaternion.identity) as GameObject;
            VirtualCharacter script = obj.GetComponent<VirtualCharacter>();
            vObj.obj = obj;
            vObj.callback = script.OnMessageReceived;
            Debug.Log("Create virtual object ok...");
        } else if (msg[0] == '0') {
            GameObject obj = GameObject.Instantiate(videoCharacterModel, new Vector3(.0f, -10f, .0f), Quaternion.identity) as GameObject;
            VideoCharacter script = obj.GetComponent<VideoCharacter>();
            vObj.obj = obj;
            vObj.callback = script.OnMessageReceived;
            script.SetForUser(UInt32.Parse(user));
            script.SetEnable(true);
            Debug.Log("Create video object ok...");
        } else {
            Debug.LogError("unknown create object type: " + msg);
        }
    }
}
