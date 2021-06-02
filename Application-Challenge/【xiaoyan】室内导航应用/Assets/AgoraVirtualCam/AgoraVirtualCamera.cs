using System.Collections;
using agora_gaming_rtc;
using UnityEngine;
using UnityEngine.UI;
using static agora_gaming_rtc.ExternalVideoFrame;
using agora_utilities;
using UnityEngine.Networking;

using System.Collections.Generic;
#if (UNITY_2018_3_OR_NEWER && UNITY_ANDROID)
using UnityEngine.Android;
#endif

public class AgoraVirtualCamera : MonoBehaviour
{
    // Use this for initialization
#if (UNITY_2018_3_OR_NEWER && UNITY_ANDROID)
    private ArrayList permissionList = new ArrayList();
#endif

    // PLEASE KEEP THIS App ID IN SAFE PLACE
    // Get your own App ID at https://dashboard.agora.io/
    [Header("Agora Config")]
    // [SerializeField]
    private string AppID = "f57a3cb69ecb4072a80d35a49fe13f64";
    // [SerializeField]
    private string TempToken = "006f57a3cb69ecb4072a80d35a49fe13f64IABEYqbmE7qNI0JthIHGkfGHDhkX0lfDiu4qEhkHnTwIDgx+f9gAAAAAEABZxLUY+mesYAEAAQD5Z6xg";
    [SerializeField]
    private string TokenServerURL = "";
    // [SerializeField]
    private string ChannelName = "test";
    [Header("Env Config")]
    [SerializeField]
    private Camera VirtualCam;
    [SerializeField]
    private GameObject RemoteVideoRoot;
    [SerializeField]
    private GameObject RemoteScreenVideoRoot;
    /*[SerializeField]
    private int ScreenShareUID;*/
    [SerializeField]
    private Text LogText;

    [Header("UI Btn Config")]
    public GameObject JoinBtn;
    public GameObject LeaveBtn;
    public GameObject MicBtn;
    public GameObject QuitBtn;
    public Color ActiveMicColor = Color.green;
    public Color DisabledMicColor = Color.red;

    [Header("Video Encoder Config")]
    [SerializeField]
    private VideoDimensions dimensions = new VideoDimensions
    {
        width = 1280,
        height = 720
    };
    [SerializeField]
    private int bitrate = 1130;
    [SerializeField]
    private FRAME_RATE frameRate = FRAME_RATE.FRAME_RATE_FPS_30;
    [SerializeField]
    private VIDEO_MIRROR_MODE_TYPE mirrorMode = VIDEO_MIRROR_MODE_TYPE.VIDEO_MIRROR_MODE_DISABLED;
    // use bitrate: 2260 for broadcast mode

    // Pixel format
    public static TextureFormat ConvertFormat = TextureFormat.RGBA32;
    public static VIDEO_PIXEL_FORMAT PixelFormat = VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_RGBA;

    private static int ShareCameraMode = 1;  // 0 = unsafe buffer pointer, 1 = renderer image

    // used for setting frame order
    int timeStampCount = 0; // monotonic timestamp counter

    // perspective camera buffer
    private Texture2D BufferTexture;
    // output log
    private Logger logger;

    // uid
    private uint UID = 0; // 0 tells the agora engine to generate the uid

    // reference to the active agora client
    static AgoraInterface client = null;

    // keep track of remote UID
    Dictionary<string, List<uint>> RemoteUIDs = new Dictionary<string, List<uint>>();

    // keep track of channel state
    bool InChannel = false;

    #region --- Life Cycles ---
    void Awake()
    {
        // keep this alive across scenes
        //DontDestroyOnLoad(this.gameObject);
    }

    // Start is called before the first frame update
    void Start()
    {
        CheckAppId();// ensure an AppID is defined

        // if there isn't a join button defined, autojoin
        if (JoinBtn == null || !JoinBtn.activeInHierarchy)
        {
            JoinChannel();
        }
    }

    // Update is called once per frame
    void Update()
    {
        PermissionHelper.RequestMicrophontPermission();
        PermissionHelper.RequestCameraPermission();
    }

    void OnDisable()
    {
        LeaveChannel();
    }

    void OnApplicationPause(bool paused)
    {
        if (client != null)
        {
            client.EnableVideo(paused);
            client.EnableAudio(paused);
        }
    }

    void OnApplicationQuit()
    {
        ShareCameraMode = 0;
        if (client != null)
        {
            client.Leave();
            client.UnloadEngine();
        }
    }

    #endregion

    #region --- Agora Functions ---
    void ReloadAgoraEngine()
    {
        client = GetComponent<AgoraInterface>();
        if (client != null)
        {
            client.Leave();
            client.UnloadEngine();
            Destroy(client);
            client = null;
        }
        client = gameObject.AddComponent<AgoraInterface>();
        client.SetLogger(logger);
        // video config 
        VideoEncoderConfiguration videoEncodeConfig = new VideoEncoderConfiguration
        {
            dimensions = this.dimensions,
            frameRate = this.frameRate,
            bitrate = this.bitrate,
            orientationMode = ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE,
            mirrorMode = this.mirrorMode
        };
        client.SetVideoEncoderConfig(videoEncodeConfig);
    }

    // agora functions
    public void JoinChannel()
    {
        // clean up and create a new one
        ReloadAgoraEngine();

        client.LoadEngine(AppID); // load engine
        // mRtcEngine.JoinChannelByKey(token, channel, "", 0);


        string appidMSG = string.Format("Initializing client with appid: ${0}", AppID);
        logger.UpdateLog(appidMSG);

        // Set up the texture for rendering POV as a texture
        if (VirtualCam.isActiveAndEnabled)
        {
            logger.UpdateLog("Virtual Camera is Active and Enabled, Enable custom video source");
            client.CustomVideo = true;
            int width = Screen.width;
            int height = Screen.height;
        }

        AddCallbackEvents(); // add custom event handling

        if (TokenServerURL != "")
        {
            client.JoinWithTokenServer(ChannelName, UID, TokenServerURL);
        }
        else
        {
            // joing with or without a token
            client.Join(ChannelName, TempToken, UID);
            string joiningChannelMsg = string.Format("Joining channel: {0}, with uid: {1}", ChannelName, UID);
            logger.UpdateLog(joiningChannelMsg);
        }
    }

    public void LeaveChannel()
    {
        if (client != null)
        {
            client.Leave();
        }
        DisableSharing();
        InChannel = false;
        // change mic buttn text and color - help user visualize  they left the channel
        if (MicBtn != null)
        {
            MicBtn.GetComponentInChildren<Text>().text = "MIC";
            MicBtn.GetComponent<Image>().color = Color.white;
        }
        // remove the remote video planes
        if (gameObject.activeInHierarchy)
        {
            if (RemoteVideoRoot?.transform.childCount > 0)
            {
                foreach (Transform child in RemoteVideoRoot.transform)
                {
                    GameObject.Destroy(child.gameObject);
                }

                StartCoroutine(UiUpdate(0.5f));
            }
        }
    }

    public void ToggleMic()
    {

        if (!InChannel)
            return; // only toggle mic when in a channel 

        Text MicBtnText = MicBtn.GetComponentInChildren<Text>();
        Image micBtnImg = MicBtn.GetComponent<Image>();

        if (micBtnImg.color == Color.green)
        {
            client.MuteLocalAudioStream(true);
            MicBtnText.text = "Mic OFF";
            micBtnImg.color = DisabledMicColor;
        }
        else if (micBtnImg.color == Color.red)
        {
            client.MuteLocalAudioStream(false);
            MicBtnText.text = "Mic ON";
            micBtnImg.color = ActiveMicColor;
        }
        else
        {
            client.MuteLocalAudioStream(true); // mute by default
            MicBtnText.text = "- MUTED -";
            MicBtnText.color = Color.white;
            micBtnImg.color = DisabledMicColor;
        }
    }


    // Called by quit button
    public void ExitApp()
    {
#if UNITY_EDITOR
        // Application.Quit() does not work in the editor so
        // UnityEditor.EditorApplication.isPlaying need to be set to false to end the game
        UnityEditor.EditorApplication.isPlaying = false;
#else
        Application.Quit();
#endif
    }

    #endregion

    #region --- Callback handlers ---
    protected virtual void AddCallbackEvents()
    {
        IRtcEngine mRtcEngine = IRtcEngine.QueryEngine();
        mRtcEngine.OnJoinChannelSuccess += OnJoinChannelSuccess;
        mRtcEngine.OnUserJoined += OnUserJoined;
        mRtcEngine.OnUserOffline += OnUserOffline;
    }

    public void OnJoinChannelSuccess(string channelName, uint uid, int elapsed)
    {

        InChannel = true;
        // logger.UpdateLog(string.Format("user added UID: {0}.", uid));
        Debug.Log("user added UID:" + uid);
        if (VirtualCam != null && VirtualCam.isActiveAndEnabled)
        {
            logger.UpdateLog("Enable Virtual Camera Sharing");
            EnableVirtualCameraSharing();
        }
        else
        {
            logger.UpdateLog("ERROR: Failed to find perspective camera.");
        }


        // update mic button color and text - visually show joined channel
        if (MicBtn != null)
        {
            MicBtn.GetComponentInChildren<Text>().text = "MIC ON";
            MicBtn.GetComponent<Image>().color = ActiveMicColor;
        }

        // enable dual stream mode
        IRtcEngine mRtcEngine = IRtcEngine.QueryEngine();
        mRtcEngine.EnableDualStreamMode(true);
    }

    public void OnUserJoined(uint uid, int elapsed)
    {
        // add video streams from all users in the channel 
        // offset the new video plane based on the parent's number of children. 

        //float xOffset = RemoteVideoRoot.transform.childCount * 3.5f;
        //MakeVideoView(uid, RemoteVideoRoot, new Vector3(xOffset, 0, 0), Quaternion.Euler(270, 0, 0));

        // to restrict which user video streams appear 
        // only show users with uid 100-1009 or 49024 (screen share)", 
        // uid 49024 is an arbitrary number that was selected and hardcoded as uid for the screen share stream from the web demo code. This uid can be customized
        string remoteUIDtype;
        Debug.Log("remote user added UID:" + uid);


        if (uid >= 1000 && uid <= 1009)
        {
            // offset the new video plane based on the parent's number of children.    
            float xOffset = RemoteVideoRoot.transform.childCount * -3.69f;
            MakeVideoView(uid, RemoteVideoRoot, new Vector3(xOffset, 0, 0), Quaternion.Euler(270, 180, 0), new Vector3(1.0f, 1.0f, 0.5625f));
            remoteUIDtype = "admin";
        } else if (uid == 49024 && RemoteScreenVideoRoot != null)
        {
            MakeVideoView(uid, RemoteScreenVideoRoot, new Vector3(0, 0, 0), Quaternion.Euler(270, 0, 0), new Vector3(-1.777f,-1.0f, -1.0f));
            remoteUIDtype = "screen";
        }
        else if(uid > 0){
            // offset the new video plane based on the parent's number of children.    
            float xOffset = RemoteVideoRoot.transform.childCount * -3.69f;
            MakeVideoView(uid, RemoteVideoRoot, new Vector3(xOffset, 0, 0), Quaternion.Euler(270, 180, 0), new Vector3(1.0f, 1.0f, 0.5625f));
            remoteUIDtype = "admin";   
        }
        else
        {
            IRtcEngine mRtcEngine = IRtcEngine.QueryEngine();
            // unsubscribe from video & audio streams
            mRtcEngine.MuteRemoteVideoStream(uid, true);
            mRtcEngine.MuteRemoteAudioStream(uid, true);
            remoteUIDtype = "peer";
        }

        // keep track of the remote uids
        logger.UpdateLog($"Make Remote Video UID type:{remoteUIDtype}");
        if (RemoteUIDs.ContainsKey(remoteUIDtype))
        {
            RemoteUIDs[remoteUIDtype].Add(uid);
        } else { 
            RemoteUIDs.Add(remoteUIDtype, new List<uint> { uid });
        }
    }

    public void OnUserOffline(uint uid, USER_OFFLINE_REASON reason)
    {
        Debug.Log("onUserOffline: update UI");
        // update the position of the remaining children
        StartCoroutine(UiUpdate(0.5f));
    }
    #endregion

    #region --- misc helper functions ---
    public void SetResolution(VideoDimensions newDimensions, int newBitrate)
    {
        dimensions = newDimensions;
        bitrate = newBitrate;

        VideoEncoderConfiguration videoEncodeConfig = new VideoEncoderConfiguration
        {
            dimensions = this.dimensions,
            frameRate = this.frameRate,
            bitrate = this.bitrate,
            orientationMode = ORIENTATION_MODE.ORIENTATION_MODE_FIXED_LANDSCAPE,
            mirrorMode = this.mirrorMode
        };
        client.SetVideoEncoderConfig(videoEncodeConfig);
    }

    private void CheckAppId()
    {
        logger = new Logger(LogText);
        logger.DebugAssert(AppID.Length > 10, "Please fill in your AppId");     //  Checks that AppID is set.
    }

    private void MakeVideoView(uint uid, GameObject parentNode, Vector3 position, Quaternion rotation, Vector3 scale)
    {
        // logger.UpdateLog(string.Format("Make Remote Video View for UID: {0}.", uid));
        Debug.Log("Make Remote Video View for UID: "+ uid);
        GameObject go = GameObject.Find(uid.ToString());
        if (go != null)
        {
            return; // reuse
        }
        
        // create a GameObject and assign to this new user
        // VideoSurface videoSurface = makePlaneSurface(uid.ToString(), parentNode, position, rotation, scale);
        GameObject thumbView = GameObject.Find("PreviewImage");
        VideoSurface videoSurface = thumbView.GetComponent<VideoSurface>();
        if (videoSurface != null)
        {
            // configure videoSurface
            videoSurface.SetForUser(uid);
            videoSurface.SetEnable(true);
            // videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.Renderer);
            videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
            // videoSurface.SetGameFps(30);
        }
    }

    // VIDEO TYPE 1: 3D Object
    public VideoSurface makePlaneSurface(string goName, GameObject parentNode, Vector3 position, Quaternion rotation, Vector3 scale)
    {
        GameObject go = GameObject.CreatePrimitive(PrimitiveType.Plane);
        // VideoSurface videoSurface = go.AddComponent<RawImage>();
        RawImage image = go.AddComponent<RawImage>();
        if (go == null)
        {
            return null;
        }
        go.name = goName;
        
        go.transform.localScale = scale; // scale the video (4:3)


        // if (parentNode != null)
        if (parentNode == null)
        {
            go.transform.SetParent(parentNode.transform);
            go.transform.localPosition = position;
            // go.transform.localRotation = rotation;
            Debug.Log( parentNode.name + " add video view under");
        }
        else
        {
            Debug.Log("parentNode is null video view");
            GameObject canvas = GameObject.Find("Canvas");

            if (canvas != null)
            {
                go.transform.SetParent(canvas.transform);
            }
            go.transform.localPosition = new Vector3(0, 0, 0f);
            // go.transform.localRotation = Quaternion.Euler(270, 0, 0);
            go.transform.Rotate(0f, 0.0f, 180.0f);
            // go.transform.localScale = Vector2.Scale;
        }

        // configure videoSurface
        VideoSurface videoSurface = go.AddComponent<VideoSurface>();
        return videoSurface;
    }

    IEnumerator UiUpdate(float time)
    {
        yield return new WaitForSeconds(time);

        // update the UI
        for (int i = 0; i < RemoteVideoRoot.transform.childCount; i++)
        {
            float xOffset = -1 * i * 3.69f; // calculate the new position
            RemoteVideoRoot.transform.GetChild(i).localPosition = new Vector3(xOffset, 0, 0); // update the position 
        }
    }
    #endregion


    #region --- Virtual Camera video frame sharing ---
    void EnableVirtualCameraSharing()
    {
        RenderTexture renderTexture = VirtualCam.targetTexture;
        if (renderTexture != null)
        {
            BufferTexture = new Texture2D(renderTexture.width, renderTexture.height, ConvertFormat, false);
            StartCoroutine(CoShareRenderData()); // use co-routine to push frames into the Agora stream
            // StartCoroutine(GetTexture()); // use co-routine to push frames into the Agora stream
        } else
        {
            logger.UpdateLog("Error: No Render Texture Found. Check Virtual Camera.");
        }
    }

    void DisableSharing()
    {
        BufferTexture = null;
    }

    IEnumerator CoShareRenderData()
    {
        while (ShareCameraMode == 1)
        {
            yield return new WaitForEndOfFrame();
            ShareRenderTexture();
        }
        yield return null;
    }
    IEnumerator GetTexture() {
        UnityWebRequest www = UnityWebRequestTexture.GetTexture("http://127.0.0.1:5500/dist/");
        yield return www.SendWebRequest();

        if (www.isHttpError) {
            Debug.Log(www.error);
        }
        else {
            Texture myTexture = ((DownloadHandlerTexture)www.downloadHandler).texture;
        }
    }
    private void ShareRenderTexture()
    {
        if (BufferTexture == null) // offlined
        {
            return;
        }
        Camera targetCamera = VirtualCam; // AR Camera
        RenderTexture.active = targetCamera.targetTexture; // the targetTexture holds render texture
        Rect rect = new Rect(0, 0, targetCamera.targetTexture.width, targetCamera.targetTexture.height);
        BufferTexture.ReadPixels(rect, 0, 0);
        BufferTexture.Apply();
        byte[] bytes = BufferTexture.GetRawTextureData();
        // sends the Raw data contained in bytes
        //monoProxy.StartCoroutine(PushFrame(bytes, (int)rect.width, (int)rect.height,
        //() =>
        //{
        //    bytes = null;
        //}));

        StartCoroutine(PushFrame(bytes, (int)rect.width, (int)rect.height,
         () =>
         {
             bytes = null;
         }));

        RenderTexture.active = null;
    }


    /// <summary>
    /// Push frame to the remote client.  This is the same code that does ScreenSharing.
    /// </summary>
    /// <param name="bytes">raw video image data</param>
    /// <param name="width"></param>
    /// <param name="height"></param>
    /// <param name="onFinish">callback upon finish of the function</param>
    /// <returns></returns>
    IEnumerator PushFrame(byte[] bytes, int width, int height, System.Action onFinish)
    {
        if (bytes == null || bytes.Length == 0)
        {
            Debug.LogError("Zero bytes found!!!!");
            yield break;
        }

        IRtcEngine rtc = IRtcEngine.QueryEngine();
        //if the engine is present
        if (rtc != null)
        {
            //Create a new external video frame
            ExternalVideoFrame externalVideoFrame = new ExternalVideoFrame();
            //Set the buffer type of the video frame
            externalVideoFrame.type = ExternalVideoFrame.VIDEO_BUFFER_TYPE.VIDEO_BUFFER_RAW_DATA;
            // Set the video pixel format
            externalVideoFrame.format = PixelFormat; // VIDEO_PIXEL_RGBA
            //apply raw data you are pulling from the rectangle you created earlier to the video frame
            externalVideoFrame.buffer = bytes;

            //Set the width of the video frame (in pixels)
            externalVideoFrame.stride = width;
            //Set the height of the video frame
            externalVideoFrame.height = height;

            //Remove pixels from the sides of the frame
            externalVideoFrame.cropLeft = 10;
            externalVideoFrame.cropTop = 10;
            externalVideoFrame.cropRight = 10;
            externalVideoFrame.cropBottom = 10;
            //Rotate the video frame (0, 90, 180, or 270)
            externalVideoFrame.rotation = 180;
            // increment i with the video timestamp
            //externalVideoFrame.timestamp = System.DateTime.Now.Ticks;
            externalVideoFrame.timestamp = timeStampCount++;
            //Push the external video frame with the frame we just created
            int a = 0;
            rtc.PushVideoFrame(externalVideoFrame);
            // if (timeStampCount % 100 == 0) Debug.Log(" pushVideoFrame(" + timeStampCount + ") size:" + bytes.Length + " => " + a);
        }

        yield return null;
        onFinish();
    }
    #endregion
}
