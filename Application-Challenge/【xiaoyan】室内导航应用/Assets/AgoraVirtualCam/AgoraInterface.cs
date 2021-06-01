using UnityEngine;

using agora_gaming_rtc;
using agora_utilities;

public class AgoraInterface : MonoBehaviour
{

    // instance of agora engine
    private IRtcEngine mRtcEngine;
    private string TokenServerUrl;
    private string ChannelName;
    private CHANNEL_PROFILE ChannelProfile = CHANNEL_PROFILE.CHANNEL_PROFILE_COMMUNICATION;
    private CLIENT_ROLE_TYPE BroadcastClientRole = CLIENT_ROLE_TYPE.CLIENT_ROLE_BROADCASTER;
    private uint UID;
    //public Text logText;
    private Logger Logger;
    public bool CustomVideo = false;
    public bool RtcStatsEnabled = false;
    private CONNECTION_STATE_TYPE ConnectionState = CONNECTION_STATE_TYPE.CONNECTION_STATE_DISCONNECTED;

    // default video config
    private VideoEncoderConfiguration videoEncodeConfig = new VideoEncoderConfiguration
    {
        dimensions = new VideoDimensions
        {
            width = 640,
            height = 360
        },
        frameRate = FRAME_RATE.FRAME_RATE_FPS_30,
        bitrate = 600,
        orientationMode = ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE
    };
    // use bitrate: 1200 for broadcast mode

    // misc
    private const float Offset = 100;

    // load agora engine
    public void LoadEngine(string appId)
    {

        Debug.Assert(Logger != null, "Please set the Logger.");
        // Debug.Assert(mRtcEngine == null, "Please free mRtcEngine.");

        // start sdk
        // Logger.UpdateLog("initializeEngine");
        // Logger.UpdateLog(mRtcEngine);

        if (mRtcEngine != null)
        {
            Logger.UpdateLog("Engine exists. Please unload it first!");
            return;
        }
        // Logger.UpdateLog("Engine exists. Please unload it first!");

        // init engine
        mRtcEngine = IRtcEngine.GetEngine(appId);

        // enable log (debug)
        // -- other configs: LOG_FILTER.DEBUG | LOG_FILTER.INFO | LOG_FILTER.WARNING | LOG_FILTER.ERROR | LOG_FILTER.CRITICAL
        // mRtcEngine.SetLogFilter(LOG_FILTER.DEBUG);
    }

    // unload agora engine
    public void UnloadEngine()
    {
        Logger.UpdateLog("calling unloadEngine");

        // delete
        if (mRtcEngine != null)
        {
            mRtcEngine.LeaveChannel();
            mRtcEngine.DisableAudio();
            mRtcEngine.DisableVideo();
            mRtcEngine.DisableVideoObserver();
            IRtcEngine.Destroy();  // Place this call in ApplicationQuit
            mRtcEngine = null;
        }
    }

    public IRtcEngine getEngine()
    {
        return mRtcEngine;
    }

    public void SetLogger(Logger logger)
    {
        this.Logger = logger;
    }

    public string GetSdkVersion()
    {
        string ver = IRtcEngine.GetSdkVersion(); // returns an internal version #
        return ver;
    }

    public void SetVideoEncoderConfig(VideoEncoderConfiguration config)
    {
        this.videoEncodeConfig = config;    
    }

    public void SetChannelProfile(CHANNEL_PROFILE channelProfile)
    {
        this.ChannelProfile = channelProfile;
    }

    public void SetClientRole(CLIENT_ROLE_TYPE clientRole)
    {
        this.BroadcastClientRole = clientRole;
    }

    public void Join(string channel, string token, uint uid)
    {
        Logger.UpdateLog("calling join (channel = " + channel + ")");

        if (mRtcEngine == null)
            return;

        // set channel profile & role
        mRtcEngine.SetChannelProfile(ChannelProfile);
        // set the client role for broadcast channel
        if(ChannelProfile == CHANNEL_PROFILE.CHANNEL_PROFILE_LIVE_BROADCASTING)
        {
            mRtcEngine.SetClientRole(BroadcastClientRole);
        }
        mRtcEngine.EnableWebSdkInteroperability(true); // enable to use with Web Clients

        // set callbacks (optional)
        EnableCallBacks();

        // enable audio and video
        mRtcEngine.EnableAudio(); // disable to avoid feedback loop while testing
        //mRtcEngine.DisableAudio();  // for single room testing only - disable audio to avoid feedback loop
        mRtcEngine.EnableVideo();
        // allow camera output callback
        mRtcEngine.EnableVideoObserver();
        if (CustomVideo)
        {
            //Configure the external video source
            CameraCapturerConfiguration config = new CameraCapturerConfiguration();
            config.preference = CAPTURER_OUTPUT_PREFERENCE.CAPTURER_OUTPUT_PREFERENCE_AUTO;
            config.cameraDirection = CAMERA_DIRECTION.CAMERA_REAR;
            mRtcEngine.SetCameraCapturerConfiguration(config);
            // set the video encoder config
            int s = mRtcEngine.SetVideoEncoderConfiguration(videoEncodeConfig);
            Debug.Assert(s == 0, "RTC set video encoder configuration failed.");

            mRtcEngine.SetExternalVideoSource(true, false);
        }
        // set the channel name
        ChannelName = channel;

        if (token != "")
        {
            Logger.UpdateLog("join by key.");

            mRtcEngine.JoinChannelByKey(token, channel, null, uid);
        }
        else
        {
            // join channel
            mRtcEngine.JoinChannel(channel, null, uid);
        }


        // Optional: if a data stream is required, here is a good place to create it
        //int streamID = mRtcEngine.CreateDataStream(true, true);
        //logger.UpdateLog("initializeEngine done, data stream id = " + streamID);
    }

    public void Leave()
    {
        if (mRtcEngine == null)
            return;
        mRtcEngine.LeaveChannel();
        Logger.UpdateLog("Leave Channel.");
    }

    public void MuteLocalAudioStream(bool mute)
    {
        if (mRtcEngine == null)
            return;
        mRtcEngine.MuteLocalAudioStream(mute);    
    }

    private void EnableCallBacks()
    {
        if (mRtcEngine == null)
        {
            return;
        }

        mRtcEngine.OnJoinChannelSuccess += OnJoinChannelSuccess;
        mRtcEngine.OnLeaveChannel += OnLeaveChannel;
        mRtcEngine.OnUserJoined += OnUserJoined;
        mRtcEngine.OnUserOffline += OnUserOffline;
        mRtcEngine.OnVolumeIndication += OnVolumeIndication;
        mRtcEngine.OnUserMutedAudio += OnUserMutedAudio;
        mRtcEngine.OnUserMuteVideo += OnUserMuteVideo;
        mRtcEngine.OnWarning += OnWarning;
        mRtcEngine.OnError += OnError;
        mRtcEngine.OnRtcStats += OnRtcStats;
        mRtcEngine.OnAudioRouteChanged += OnAudioRouteChanged;
        mRtcEngine.OnRequestToken += OnTokenPrivilegeDidExpire;
        mRtcEngine.OnConnectionStateChanged += OnConnectionStateChanged;
        mRtcEngine.OnConnectionInterrupted += OnConnectionInterrupted;
        mRtcEngine.OnConnectionLost += OnConnectionLost;
        if (TokenServerUrl != null)
        {
            mRtcEngine.OnTokenPrivilegeWillExpire += OnTokenPrivilegeWillExpire;
        }
    }

    public void EnableVideo(bool pauseVideo)
    {
        if (mRtcEngine != null)
        {
            if (!pauseVideo)
            {
                mRtcEngine.EnableVideo();
            }
            else
            {
                mRtcEngine.DisableVideo();
            }
        }
    }

    public void EnableAudio(bool pauseAudio)
    {
        if (mRtcEngine != null)
        {
            if (!pauseAudio)
            {
                mRtcEngine.EnableAudio();
            }
            else
            {
                mRtcEngine.DisableAudio();
            }
        }
    }

    public void JoinWithTokenServer(string channel, uint uid, string url)
    {
        TokenServerUrl = url;
        ChannelName = channel;
        UID = uid;
        // get token and join channel
        StartCoroutine(TokenRequestHelper.FetchToken(url, channel, uid, this.JoinChannelWithTokenServerResponse));
        string fetchTokenMsg = string.Format("Fetching token for channel: {0}, with uid: {1}", ChannelName, UID);
        Logger.UpdateLog(fetchTokenMsg);
    }

    private void JoinChannelWithTokenServerResponse(string newToken)
    {
        Join(ChannelName, newToken, UID);
        string joiningChannelMsg = string.Format("Token success, joining channel: {0}, with uid: {1}", ChannelName, UID);
        Logger.UpdateLog(joiningChannelMsg);
    }

    private void RenewToken (string newToken)
    {
        mRtcEngine.RenewToken(newToken);
    }

    #region ------  callbacks  -------

    // successfully joined channel
    public void OnJoinChannelSuccess (string channelName, uint uid, int elapsed) 
    {
        string joinSuccessMessage = string.Format("joinChannel callback uid: {0}, channel: {1}, version: {2}", uid, channelName, GetSdkVersion());
        Logger.UpdateLog(joinSuccessMessage);
    }

    public void OnLeaveChannel (RtcStats stats)
    {
        string leaveChannelMessage = string.Format("onLeaveChannel callback duration {0}, tx: {1}, rx: {2}, tx kbps: {3}, rx kbps: {4}", stats.duration, stats.txBytes, stats.rxBytes, stats.txKBitRate, stats.rxKBitRate);
        Logger.UpdateLog(leaveChannelMessage);
    }

    public void OnUserJoined (uint uid, int elapsed)
    {
        string userJoinedMessage = string.Format("onUserJoined callback uid {0} {1}", uid, elapsed);
        Logger.UpdateLog(userJoinedMessage);
    }


    public void OnUserOffline (uint uid, USER_OFFLINE_REASON reason)
    {
        string userOfflineMessage = string.Format("onUserOffline callback uid {0} {1}", uid, reason);
        Logger.UpdateLog(userOfflineMessage);

        // remove video stream
        Logger.UpdateLog("onUserOffline: uid = " + uid + " reason = " + reason);
        // this is called in main thread
        GameObject go = GameObject.Find(uid.ToString());
        if (go != null)
        {
            Destroy(go);
        }
    }

    public void OnVolumeIndication (AudioVolumeInfo[] speakers, int speakerNumber, int totalVolume)
    {
        if (speakerNumber == 0 || speakers == null)
        {
            Logger.UpdateLog(string.Format("onVolumeIndication only local {0}", totalVolume));
        }

        for (int idx = 0; idx < speakerNumber; idx++)
        {
            string volumeIndicationMessage = string.Format("{0} onVolumeIndication {1} {2}", speakerNumber, speakers[idx].uid, speakers[idx].volume);
            Logger.UpdateLog(volumeIndicationMessage);
        }
    }

    public void OnUserMutedAudio (uint uid, bool muted)
    {
        string userMutedMessage = string.Format("OnUserMutedAudio callback uid {0} {1}", uid, muted);
        Logger.UpdateLog(userMutedMessage);
    }

    public void OnUserMuteVideo (uint uid, bool muted)
    {
        string userMutedMessage = string.Format("OnUserMuteVideo callback uid {0} {1}", uid, muted);
        Logger.UpdateLog(userMutedMessage);
    }

    public void OnWarning (int warn, string msg)
    {
        string description = IRtcEngine.GetErrorDescription(warn);
        string warningMessage = string.Format("onWarning callback {0} {1} {2}", warn, msg, description);
        Logger.UpdateLog(warningMessage);
    }

    public void OnError (int error, string msg)
    {
        string description = IRtcEngine.GetErrorDescription(error);
        string errorMessage = string.Format("onError callback {0} {1} {2}", error, msg, description);
        Logger.UpdateLog(errorMessage);
    }

    public void OnRtcStats (RtcStats stats)
    {
        if (RtcStatsEnabled)
        {
            string rtcStatsMessage = string.Format("onRtcStats callback duration {0}, tx: {1}, rx: {2}, tx kbps: {3}, rx kbps: {4}, tx(a) kbps: {5}, rx(a) kbps: {6} users {7}",
            stats.duration, stats.txBytes, stats.rxBytes, stats.txKBitRate, stats.rxKBitRate, stats.txAudioKBitRate, stats.rxAudioKBitRate, stats.userCount);
            Logger.UpdateLog(rtcStatsMessage);

            int lengthOfMixingFile = mRtcEngine.GetAudioMixingDuration();
            int currentTs = mRtcEngine.GetAudioMixingCurrentPosition();

            string mixingMessage = string.Format("Mixing File Meta {0}, {1}", lengthOfMixingFile, currentTs);
            Logger.UpdateLog(mixingMessage);
        }
    }

    public void OnAudioRouteChanged (AUDIO_ROUTE route)
    {
        string routeMessage = string.Format("onAudioRouteChanged {0}", route);
        Logger.UpdateLog(routeMessage);
    }

    public void OnTokenPrivilegeDidExpire()
    {
        string requestKeyMessage = string.Format("OnTokenPrivilegeDidExpire");
        Logger.UpdateLog(requestKeyMessage);
    }

    void OnConnectionStateChanged(CONNECTION_STATE_TYPE state, CONNECTION_CHANGED_REASON_TYPE reason)
    {
        ConnectionState = state;
        string connectionStateMessage = string.Format("OnConnectionStateChanged: {0}, with reason {1}", state, reason);
        Logger.UpdateLog(connectionStateMessage);
    }

    public void OnConnectionInterrupted ()
    {
        string interruptedMessage = string.Format("OnConnectionInterrupted");
        Logger.UpdateLog(interruptedMessage);
    }

    public void OnConnectionLost ()
    {
        string lostMessage = string.Format("OnConnectionLost");
        Logger.UpdateLog(lostMessage);
    }

    public void OnTokenPrivilegeWillExpire(string token)
    {
        StartCoroutine(TokenRequestHelper.FetchToken(TokenServerUrl, ChannelName, UID, this.RenewToken));
    }
    #endregion
}
