using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

using agora_rtm;
using TMPro;
using agora_utilities;

namespace indoorNav
{
    public class NavRtmChatManager : MonoBehaviour
    {
#pragma warning disable 0649

        private string appId = "your_appid";
        private string token = "your_token";

        [Header("Application Properties")]
        // put absolute path like /Users/yournameonmac/Downloads/mono-boad.jpg  in the Inspector

        // [SerializeField] TextMeshProUGUI userNameInput; 
        [SerializeField] NavMessageDisplay messageDisplay;
        [SerializeField] TextMeshProUGUI channelMsgInputBox;
        [SerializeField] Button SendMessageButton;


#pragma warning restore 0649
        private RtmClient rtmClient = null;
        private RtmChannel channel;
        private RtmCallManager callManager;

        private RtmClientEventHandler clientEventHandler;
        private RtmChannelEventHandler channelEventHandler;
        private RtmCallEventHandler callEventHandler;
        uint UID;
        string _userName = "";
        string UserName {
            get { return _userName; }
            set {
                _userName = value;
                PlayerPrefs.SetString("RTM_USER", _userName);
                PlayerPrefs.Save();
            }
        }

        string _channelName = "";
        string ChannelName
        {
            get { return _channelName; }
            set {
                _channelName = value;
                PlayerPrefs.SetString("RTM_CHANNEL", _channelName);
                PlayerPrefs.Save();
            }
        }

        agora_rtm.SendMessageOptions _MessageOptions = new agora_rtm.SendMessageOptions() {
                    enableOfflineMessaging = true,
                    enableHistoricalMessaging = true
	    };

        private void Awake()
        {
            SendMessageButton.interactable = true;

        }

        // Start is called before the first frame update
        void Start()
        {
            clientEventHandler = new RtmClientEventHandler();
            channelEventHandler = new RtmChannelEventHandler();
            callEventHandler = new RtmCallEventHandler();

            rtmClient = new RtmClient(appId, clientEventHandler);
#if UNITY_EDITOR
            rtmClient.SetLogFile("./rtm_log.txt");
#endif

            clientEventHandler.OnQueryPeersOnlineStatusResult = OnQueryPeersOnlineStatusResultHandler;
            clientEventHandler.OnLoginFailure = OnClientLoginFailureHandler;
            clientEventHandler.OnLoginSuccess = OnClientLoginSuccessHandler;



            channelEventHandler.OnJoinSuccess = OnJoinSuccessHandler;
            channelEventHandler.OnJoinFailure = OnJoinFailureHandler;

            // Optional, tracking members
            channelEventHandler.OnMemberCountUpdated = OnMemberCountUpdatedHandler;

            callManager = rtmClient.GetRtmCallManager(callEventHandler);
            // state
            clientEventHandler.OnConnectionStateChanged = OnConnectionStateChangedHandler;
            clientEventHandler.OnTokenExpired = OnTokenExpired;
            // channelMsgInputBox.GetComponentInParent<TMP_InputField>().onValueChanged.AddListener( str => {
            //     AutoCompeleteLogic();
            // } );
            SendMessageButton.onClick.AddListener(() => {
                SendMessageToChannel();
            });
            channelEventHandler.OnMessageReceived = OnChannelMessageReceivedHandler;

            // Login("tbxy09","http://150.109.60.238:8080");
        }
        void OnClientLoginSuccessHandler(int id)
        {
            string msg = "client login successful! id = " + id;
            Debug.Log(msg);
            JoinChannel();
        }

        void OnTokenExpired(int id){
            Debug.Log("Token Expired, ReLogin");
            // Login("tbxy09","http://150.109.60.238:8080");
            
        }

        void OnApplicationQuit()
        {
            if (channel != null)
            {
                channel.Dispose();
                channel = null;
            }
            if (rtmClient != null)
            {
                rtmClient.Dispose();
                rtmClient = null;
            }
        }
        public void Login(string username, string url,uint uid = 0)
        {
            UserName = username;
            UID = uid;
            StartCoroutine(TokenRequestHelper.FetchToken(url, username, uid, this.JoinChannelWithTokenServerResponse,false));
        }

        private void JoinChannelWithTokenServerResponse(string newToken)
        {
            rtmClient.Login(newToken, UserName);
            
        }

        public void Logout(uint uid)
        {
            Debug.Log(uid.ToString() + " logged out of the rtm");
            rtmClient.Logout();
        }

        public void ChannelMemberCountButtonPressed()
        {
            if (channel != null)
            {
                channel.GetMembers();
            }
        }

        public void JoinChannel()
        {
            channel = rtmClient.CreateChannel("test", channelEventHandler);
            channel.Join();
        }

        public void LeaveChannel(uint uid)
        {
            channel.Leave();
        }

        public void SendMessageToChannel()
        {
            string txt = channelMsgInputBox.text;
            channel.SendMessage(rtmClient.CreateMessage(txt));
            Vector3 position = new Vector3(0f + Random.Range(-20f, 20f),0f, 0f + Random.Range(-20f, 20f));
            messageDisplay.AppendList(UserName,Random.Range(0f, 10f), position, txt, true);

        }
        void OnChannelMessageReceivedHandler(int id, string userId, TextMessage message)
        {
            Debug.Log("client OnChannelMessageReceived id = " + id + ", from user:" + userId + " text:" + message.GetText());
            // messageDisplay.AddTextToDisplay(userId + ": " + message.GetText(), Message.MessageType.ChannelMessage);
            Vector3 position = new Vector3(0f + Random.Range(-20f, 20f),0f, 0f + Random.Range(-20f, 20f));
            messageDisplay.AppendList(userId,Random.Range(0f, 10f), position, message.GetText());
        }
        bool ShowDisplayTexts()
        {
            int showLength = 6;
            if (string.IsNullOrEmpty(appId) || appId.Length < showLength)
            {
                Debug.LogError("App ID is not set, please set it in " + gameObject.name);
                return false;
            }

            if (string.IsNullOrEmpty(token) || token.Length < showLength)
            {
                Debug.Log("Temp Token is not set, please set it in " + gameObject.name);
            }
            return true;
        }
        void OnQueryPeersOnlineStatusResultHandler(int id, long requestId, PeerOnlineStatus[] peersStatus, int peerCount, QUERY_PEERS_ONLINE_STATUS_ERR errorCode)
        {
            if (peersStatus.Length > 0)
            {
                Debug.Log("OnQueryPeersOnlineStatusResultHandler requestId = " + requestId +
                " peersStatus: peerId=" + peersStatus[0].peerId +
                " online=" + peersStatus[0].isOnline +
                " onlinestate=" + peersStatus[0].onlineState);
                Debug.Log("User " + peersStatus[0].peerId + " online status = " + peersStatus[0].onlineState);
            }
        }
        void OnJoinSuccessHandler(int id)
        {
            Debug.Log("join channel success");
        }
        void OnJoinFailureHandler(int id, JOIN_CHANNEL_ERR errorCode)
        {
            string msg = "channel OnJoinFailure  id = " + id + " errorCode = " + errorCode;
            Debug.Log(msg);
        }
        void OnClientLoginFailureHandler(int id, LOGIN_ERR_CODE errorCode)
        {
            string msg = "client login unsuccessful! id = " + id + " errorCode = " + errorCode;
            Debug.Log(msg);
        }

        void OnMemberCountUpdatedHandler(int id, int memberCount)
        {
            Debug.Log("Member count changed to:" + memberCount);
        }

        void OnConnectionStateChangedHandler(int id, CONNECTION_STATE state, CONNECTION_CHANGE_REASON reason)
        {
            string msg = string.Format("connection state changed id:{0} state:{1} reason:{2}", id, state, reason);
            // Debug.Log(msg);
        }

    }

}
