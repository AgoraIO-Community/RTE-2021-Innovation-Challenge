using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

using agora_rtm;

namespace io.agora.rtm.demo
{
    public class RtmChatManager : MonoBehaviour
    {
#pragma warning disable 0649

        [Header("Agora Properties")]
        [SerializeField]
        private string appId = "";
        [SerializeField]
        private string token = "";

        [Header("Application Properties")]
      
        [SerializeField] InputField userNameInput, channelNameInput;
        [SerializeField] InputField channelMsgInputBox;

        [SerializeField] MessageDisplay messageDisplay;

#pragma warning restore 0649


        private RtmClient rtmClient = null;
        private RtmChannel channel;
        private RtmCallManager callManager;

        private RtmClientEventHandler clientEventHandler;
        private RtmChannelEventHandler channelEventHandler;
        private RtmCallEventHandler callEventHandler;

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
            userNameInput.text = PlayerPrefs.GetString("RTM_USER", "");
            channelNameInput.text = PlayerPrefs.GetString("RTM_CHANNEL", "");
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
            clientEventHandler.OnLoginSuccess = OnClientLoginSuccessHandler;
            clientEventHandler.OnLoginFailure = OnClientLoginFailureHandler;
            clientEventHandler.OnMessageReceivedFromPeer = OnMessageReceivedFromPeerHandler;

            channelEventHandler.OnJoinSuccess = OnJoinSuccessHandler;
            channelEventHandler.OnJoinFailure = OnJoinFailureHandler;
            channelEventHandler.OnLeave = OnLeaveHandler;
            channelEventHandler.OnMessageReceived = OnChannelMessageReceivedHandler;

            // Optional, tracking members
            channelEventHandler.OnGetMembers = OnGetMembersHandler;
            channelEventHandler.OnMemberCountUpdated = OnMemberCountUpdatedHandler;
            channelEventHandler.OnMemberJoined = OnMemberJoinedHandler;
            channelEventHandler.OnMemberLeft = OnMemberLeftHandler;

            // image
            clientEventHandler.OnSendMessageResult = OnSendMessageResultHandler;
            clientEventHandler.OnMediaDownloadToFileResult = OnMediaDownloadToFileResultHandler;
            clientEventHandler.OnMediaDownloadToMemoryResult = OnMediaDownloadToMemoryResultHandler;

            // invite
            callEventHandler.OnLocalInvitationAccepted = OnLocalInvitationAcceptedHandler;
            callEventHandler.OnLocalInvitationCanceled = OnLocalInvitationCanceledHandler;
            callEventHandler.OnLocalInvitationFailure = OnLocalInvitationFailureHandler;
            callEventHandler.OnLocalInvitationReceivedByPeer = OnLocalInvitationReceivedByPeerHandler;
            callEventHandler.OnLocalInvitationRefused = OnLocalInvitationRefusedHandler;
            
	        callEventHandler.OnRemoteInvitationAccepted = OnRemoteInvitationAcceptedHandler;
            callEventHandler.OnRemoteInvitationCanceled = OnRemoteInvitationCanceledHandler;
            callEventHandler.OnRemoteInvitationFailure = OnRemoteInvitationFailureHandler;
            callEventHandler.OnRemoteInvitationReceived = OnRemoteInvitationReceivedHandler;
            callEventHandler.OnRemoteInvitationRefused = OnRemoteInvitationRefusedHandler;

            callManager = rtmClient.GetRtmCallManager(callEventHandler);
            // state
            clientEventHandler.OnConnectionStateChanged = OnConnectionStateChangedHandler;

            bool initialized = ShowDisplayTexts();
            if (initialized)
            {
                string ver = RtmClient.GetSdkVersion();
                messageDisplay.AddTextToDisplay("RTM version " + ver + " initialized.", Message.MessageType.Info);
            }
            else
            {
                messageDisplay.AddTextToDisplay("RTM not initialized", Message.MessageType.Info);
            }
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

        #region Button Events
        public void Login()
        {
            UserName = userNameInput.text;

            if (string.IsNullOrEmpty(UserName) || string.IsNullOrEmpty(appId))
            {
                Debug.LogError("We need a username and appId to login");
                return;
            }

            rtmClient.Login(token, UserName);
        }

        public void Logout()
        {
            messageDisplay.AddTextToDisplay(UserName + " logged out of the rtm", Message.MessageType.Info);
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
            ChannelName = channelNameInput.GetComponent<InputField>().text;
            channel = rtmClient.CreateChannel(ChannelName, channelEventHandler);
            ShowCurrentChannelName();
            channel.Join();
        }

        public void LeaveChannel()
        {
            messageDisplay.AddTextToDisplay(UserName + " left the chat", Message.MessageType.Info);
            channel.Leave();
        }

        public void SendMessageToChannel()
        {
            string msg = channelMsgInputBox.text;
            string peer = "[channel:" + ChannelName + "]";

            string displayMsg = string.Format("{0}->{1}: {2}", UserName, peer, msg);

            messageDisplay.AddTextToDisplay(displayMsg, Message.MessageType.PlayerMessage);
            channel.SendMessage(rtmClient.CreateMessage(msg));
        }

       

        #region  --Image Send / Receive ---------------------------
        string ImageMediaId { get; set; }
        // Sender will get this assign in callback
        ImageMessage RcvImageMessage { get; set; }

        public void GetImageByMediaId()
        {
            string mediaID = RcvImageMessage.GetMediaId();
            int rc = rtmClient.DownloadMediaToMemory(mediaID, 100023);
            Debug.LogFormat("Download image {0} ---> rc={1}", mediaID, rc);
        }

      

        #endregion
        #endregion


        void ShowCurrentChannelName()
        {
            ChannelName = channelNameInput.GetComponent<InputField>().text;
            Debug.Log("Channel name is " + ChannelName);
        }
        bool ShowDisplayTexts()
        {
            int showLength = 6;
            if (string.IsNullOrEmpty(appId) || appId.Length < showLength)
            {
                Debug.LogError("App ID is not set, please set it in " + gameObject.name);
               
                return false;
            }
            else
            {
               
            }

            if (string.IsNullOrEmpty(token) || token.Length < showLength)
            {
              
            }
            else
            {
               

            }
            return true;
        }

        #region EventHandlers

        void OnQueryPeersOnlineStatusResultHandler(int id, long requestId, PeerOnlineStatus[] peersStatus, int peerCount, QUERY_PEERS_ONLINE_STATUS_ERR errorCode)
        {
            if (peersStatus.Length > 0)
            {
                Debug.Log("OnQueryPeersOnlineStatusResultHandler requestId = " + requestId +
                " peersStatus: peerId=" + peersStatus[0].peerId +
                " online=" + peersStatus[0].isOnline +
                " onlinestate=" + peersStatus[0].onlineState);
                messageDisplay.AddTextToDisplay("User " + peersStatus[0].peerId + " online status = " + peersStatus[0].onlineState, Message.MessageType.Info);
            }
        }

        void OnJoinSuccessHandler(int id)
        {
            string msg = "channel:" + ChannelName + " OnJoinSuccess id = " + id;
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        void OnJoinFailureHandler(int id, JOIN_CHANNEL_ERR errorCode)
        {
            string msg = "channel OnJoinFailure  id = " + id + " errorCode = " + errorCode;
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Error);
        }

        void OnClientLoginSuccessHandler(int id)
        {
            string msg = "client login successful! id = " + id;
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        void OnClientLoginFailureHandler(int id, LOGIN_ERR_CODE errorCode)
        {
            string msg = "client login unsuccessful! id = " + id + " errorCode = " + errorCode;
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Error);
        }

        void OnLeaveHandler(int id, LEAVE_CHANNEL_ERR errorCode)
        {
            string msg = "client onleave id = " + id + " errorCode = " + errorCode;
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        void OnChannelMessageReceivedHandler(int id, string userId, TextMessage message)
        {
            Debug.Log("client OnChannelMessageReceived id = " + id + ", from user:" + userId + " text:" + message.GetText());
            messageDisplay.AddTextToDisplay(userId + ": " + message.GetText(), Message.MessageType.ChannelMessage);
        }

        void OnGetMembersHandler(int id, RtmChannelMember[] members, int userCount, GET_MEMBERS_ERR errorCode)
        {
            if (errorCode == GET_MEMBERS_ERR.GET_MEMBERS_ERR_OK)
            {
                messageDisplay.AddTextToDisplay("Total members = " + userCount, Message.MessageType.Info);
                foreach(RtmChannelMember member in members) {
                    messageDisplay.AddTextToDisplay("   member:> " + member.GetUserId(), Message.MessageType.Info);
                }
            } else { 
                messageDisplay.AddTextToDisplay("something is wrong with GetMembers:" + errorCode.ToString(), Message.MessageType.Error);
	        }
        }

        void OnMessageReceivedFromPeerHandler(int id, string peerId, TextMessage message)
        {
            Debug.Log("client OnMessageReceivedFromPeer id = " + id + ", from user:" + peerId + " text:" + message.GetText());
            messageDisplay.AddTextToDisplay(peerId + ": " + message.GetText(), Message.MessageType.PeerMessage);
        }

        void OnMemberCountUpdatedHandler(int id, int memberCount)
        {
            Debug.Log("Member count changed to:" + memberCount);
        }
        void OnMemberJoinedHandler(int id, RtmChannelMember member)
        {
            string msg = "channel OnMemberJoinedHandler member ID=" + member.GetUserId() + " channelId = " + member.GetChannelId();
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        void OnMemberLeftHandler(int id, RtmChannelMember member)
        {
            string msg = "channel OnMemberLeftHandler member ID=" + member.GetUserId() + " channelId = " + member.GetChannelId();
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }


        void OnSendMessageResultHandler(int id, long messageId, PEER_MESSAGE_ERR_CODE errorCode)
        {
            string msg = string.Format("Sent message with id:{0} MessageId:{1} errorCode:{2}", id, messageId, errorCode);
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, errorCode == PEER_MESSAGE_ERR_CODE.PEER_MESSAGE_ERR_OK ? Message.MessageType.Info : Message.MessageType.Error);
        }

       

        void OnMediaDownloadToFileResultHandler(int id, long requestId, DOWNLOAD_MEDIA_ERR_CODE code)
        {
            Debug.LogFormat("Download id:{0} requestId:{1} errorCode:{2}", id, requestId, code);
        }

        void OnMediaDownloadToMemoryResultHandler(int id, long requestId, byte[] memory, long length, DOWNLOAD_MEDIA_ERR_CODE code)
        {
            Debug.Log("OnMediaDownloadToMemoryResultHandler requestId = " + requestId + " ,length = " + length);
            //messageDisplay.AddImageToDisplay(memory, RcvImageMessage.GetWidth(), RcvImageMessage.GetHight());
       
        }

        void OnConnectionStateChangedHandler(int id, CONNECTION_STATE state, CONNECTION_CHANGE_REASON reason)
        {
            string msg = string.Format("connection state changed id:{0} state:{1} reason:{2}", id, state, reason);
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        // --------------------------------------
        void OnLocalInvitationReceivedByPeerHandler(LocalInvitation localInvitation)
        {
            string msg = string.Format("OnLocalInvitationReceived channel:{0}, callee:{1}", localInvitation.GetChannelId(), localInvitation.GetCalleeId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	    }

        void OnLocalInvitationCanceledHandler(LocalInvitation localInvitation)
        { 
            string msg = string.Format("OnLocalInvitationCanceled channel:{0}, callee:{1}", localInvitation.GetChannelId(), localInvitation.GetCalleeId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	    }

        void OnLocalInvitationFailureHandler(LocalInvitation localInvitation, LOCAL_INVITATION_ERR_CODE errorCode)
        {
            string msg = string.Format("OnLocalInvitationFailure channel:{0}, callee:{1} error:{2}", 
		        localInvitation.GetChannelId(), localInvitation.GetCalleeId(), errorCode);
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        void OnLocalInvitationAcceptedHandler(LocalInvitation localInvitation, string response)
        { 
            string msg = string.Format("OnLocalInvitationAccepted channel:{0}, callee:{1}", localInvitation.GetChannelId(), localInvitation.GetCalleeId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	    }
        
        void OnLocalInvitationRefusedHandler(LocalInvitation localInvitation, string response)
        { 
            string msg = string.Format("OnLocalInvitationRefused channel:{0}, callee:{1}", localInvitation.GetChannelId(), localInvitation.GetCalleeId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	
	    }
        void OnRemoteInvitationRefusedHandler(RemoteInvitation remoteInvitation)
        { 
            string msg = string.Format("OnRemoteInvitationRefused channel:{0}, callee:{1}", remoteInvitation.GetChannelId(), remoteInvitation.GetCallerId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	    }

        void OnRemoteInvitationAcceptedHandler(RemoteInvitation remoteInvitation)
        {
            string msg = string.Format("OnRemoteInvitationAccepted channel:{0}, callee:{1}", remoteInvitation.GetChannelId(), remoteInvitation.GetCallerId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }

        void OnRemoteInvitationReceivedHandler(RemoteInvitation remoteInvitation)
        {
            string msg = string.Format("OnRemoteInvitationReceived channel:{0}, callee:{1}", remoteInvitation.GetChannelId(), remoteInvitation.GetCallerId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
        }
        void OnRemoteInvitationFailureHandler(RemoteInvitation remoteInvitation, REMOTE_INVITATION_ERR_CODE errorCode)
        { 
            string msg = string.Format("OnRemoteInvitationFailure channel:{0}, callee:{1}", remoteInvitation.GetChannelId(), remoteInvitation.GetCallerId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	    }
        void OnRemoteInvitationCanceledHandler(RemoteInvitation remoteInvitation)
        { 
            string msg = string.Format("OnRemoteInvitationCanceled channel:{0}, callee:{1}", remoteInvitation.GetChannelId(), remoteInvitation.GetCallerId());
            Debug.Log(msg);
            messageDisplay.AddTextToDisplay(msg, Message.MessageType.Info);
	    }
        #endregion
    }

}
