using System.Collections;
using System.Collections.Generic;
using UnityEngine;

using agora_gaming_rtc;
using agora_utilities;
public class NavRtcInterface: MonoBehaviour
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

        if (mRtcEngine != null) return;
        // init engine
        mRtcEngine = IRtcEngine.GetEngine(appId);
    }

    public void UnloadEngine()
    {

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
        if (mRtcEngine == null) return;
        mRtcEngine.SetChannelProfile(ChannelProfile);
        if(ChannelProfile == CHANNEL_PROFILE.CHANNEL_PROFILE_LIVE_BROADCASTING)
        {
            mRtcEngine.SetClientRole(BroadcastClientRole);
        }
        mRtcEngine.EnableWebSdkInteroperability(true); // enable to use with Web Clients
        EnableCallBacks();
        mRtcEngine.DisableAudio(); // disable to avoid feedback loop while testing
        mRtcEngine.EnableVideo();
        mRtcEngine.EnableVideoObserver();
        if (CustomVideo)
        {
            CameraCapturerConfiguration config = new CameraCapturerConfiguration();
            config.preference = CAPTURER_OUTPUT_PREFERENCE.CAPTURER_OUTPUT_PREFERENCE_AUTO;
            config.cameraDirection = CAMERA_DIRECTION.CAMERA_REAR;
            mRtcEngine.SetCameraCapturerConfiguration(config);
            int s = mRtcEngine.SetVideoEncoderConfiguration(videoEncodeConfig);
            Debug.Assert(s == 0, "RTC set video encoder configuration failed.");
            mRtcEngine.SetExternalVideoSource(true, false);
        }
        ChannelName = channel;
        mRtcEngine.JoinChannelByKey(token, channel, null, uid);
    }

    public void Leave()
    {
        if (mRtcEngine == null) return;
        mRtcEngine.LeaveChannel();
    }

    public void MuteLocalAudioStream(bool mute)
    {
        if (mRtcEngine == null) return;
        mRtcEngine.MuteLocalAudioStream(mute);    
    }

    private void EnableCallBacks()
    {
        if (mRtcEngine == null) return;
        mRtcEngine.OnUserOffline += OnUserOffline;
        mRtcEngine.OnVolumeIndication += OnVolumeIndication;
        mRtcEngine.OnError += OnError;
        mRtcEngine.OnRtcStats += OnRtcStats;
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
        StartCoroutine(TokenRequestHelper.FetchToken(url, channel, uid, this.JoinChannelWithTokenServerResponse));
    }

    private void JoinChannelWithTokenServerResponse(string newToken)
    {
        Debug.Log("RTC token" + newToken);
        Join(ChannelName, newToken, UID);
    }

    private void RenewToken (string newToken)
    {
        mRtcEngine.RenewToken(newToken);
    }

    #region ------  callbacks  -------

    public void OnUserOffline (uint uid, USER_OFFLINE_REASON reason)
    {
        string userOfflineMessage = string.Format("onUserOffline callback uid {0} {1}", uid, reason);
        GameObject go = GameObject.Find(uid.ToString());
        if (go != null)
        {
            Destroy(go);
        }
    }

    public void OnVolumeIndication (AudioVolumeInfo[] speakers, int speakerNumber, int totalVolume)
    {
        for (int idx = 0; idx < speakerNumber; idx++)
        {
            string volumeIndicationMessage = string.Format("{0} onVolumeIndication {1} {2}", speakerNumber, speakers[idx].uid, speakers[idx].volume);
        }
    }
    public void OnError (int error, string msg)
    {
        string description = IRtcEngine.GetErrorDescription(error);
        Debug.LogError($"onError callback {error} {msg} {description}");
    }

    public void OnRtcStats (RtcStats stats)
    {
        if (RtcStatsEnabled)
        {
            string rtcStatsMessage = string.Format("onRtcStats callback duration {0}, tx: {1}, rx: {2}, tx kbps: {3}, rx kbps: {4}, tx(a) kbps: {5}, rx(a) kbps: {6} users {7}",
            stats.duration, stats.txBytes, stats.rxBytes, stats.txKBitRate, stats.rxKBitRate, stats.txAudioKBitRate, stats.rxAudioKBitRate, stats.userCount);
            Debug.Log(rtcStatsMessage);

            int lengthOfMixingFile = mRtcEngine.GetAudioMixingDuration();
            int currentTs = mRtcEngine.GetAudioMixingCurrentPosition();

            string mixingMessage = string.Format("Mixing File Meta {0}, {1}", lengthOfMixingFile, currentTs);
            Debug.Log(mixingMessage);
        }
    }
    public void OnTokenPrivilegeWillExpire(string token)
    {
        StartCoroutine(TokenRequestHelper.FetchToken(TokenServerUrl, ChannelName, UID, this.RenewToken));
    }
    #endregion
}
