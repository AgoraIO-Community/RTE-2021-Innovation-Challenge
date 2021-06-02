using System.Collections;
using agora_gaming_rtc;
using UnityEngine;
using UnityEngine.UI;
using static agora_gaming_rtc.ExternalVideoFrame;
using UnityEngine.Networking;
using System.Collections.Generic;
using TMPro;
namespace indoorNav{

    public class NavRtcManager: MonoBehaviour
    {
        // Use this for initialization
        private string AppID = "your_appid";
        private string TempToken = "your_token";
        private string ChannelName = "test";
        private Color ActiveMicColor = Color.green;
        private Color DisabledMicColor = Color.red;
        private string TokenServerURL = "http://150.109.60.238:8080";
        private GameObject QuitBtn;
        private GameObject RemoteVideoRoot;
        private GameObject LeaveBtn;
        private GameObject MicBtn;

        [Header("Env Config")]
        [SerializeField] 
        private Camera VirtualCam;
        [SerializeField] 
        private NavRtmChatManager chatManager;

        private VideoDimensions dimensions = new VideoDimensions
        {
            width = 1280,
            height = 720
        };
        private int bitrate = 1130;
        private FRAME_RATE frameRate = FRAME_RATE.FRAME_RATE_FPS_30;
        private VIDEO_MIRROR_MODE_TYPE mirrorMode = VIDEO_MIRROR_MODE_TYPE.VIDEO_MIRROR_MODE_DISABLED;
        public static TextureFormat ConvertFormat = TextureFormat.RGBA32;
        public static VIDEO_PIXEL_FORMAT PixelFormat = VIDEO_PIXEL_FORMAT.VIDEO_PIXEL_RGBA;
        private static int ShareCameraMode = 1;  // 0 = unsafe buffer pointer, 1 = renderer image
        int timeStampCount = 0; // monotonic timestamp counter
        private Texture2D BufferTexture; // perspective camera buffer
        private Logger logger;

        private uint UID = 0; // 0 tells the agora engine to generate the uid

        static NavRtcInterface client = null;
        NavPathBehavior pbehavior;
        NavFriendsDisplay friendsDisplay;
        

        public static readonly Dictionary<string, List<uint>> RemoteUIDs = new Dictionary<string, List<uint>>(); // RemotUID to track UID
        public static readonly Dictionary<uint, List<Transform>> items = new Dictionary<uint, List<Transform>>();

        bool InChannel = false;

        void Start()
        {
            pbehavior = this.GetComponent<NavPathBehavior>();
            friendsDisplay = this.GetComponent<NavFriendsDisplay>();
            JoinChannel();
        }
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
        void ReloadAgoraEngine()
        {
            client = GetComponent<NavRtcInterface>();
            if (client != null)
            {
                client.Leave();
                client.UnloadEngine();
                Destroy(client);
                client = null;
            }
            client = gameObject.AddComponent<NavRtcInterface>();
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
            ReloadAgoraEngine();
            client.LoadEngine(AppID); // load engine
            string appidMSG = string.Format("Initializing client with appid: ${0}", AppID);
            // Debug.Log(appidMSG);

            // Set up the texture for rendering POV as a texture

            AddCallbackEvents(); // add custom event handling
            if (VirtualCam.isActiveAndEnabled)
            {
                client.CustomVideo = true;
                int width = Screen.width;
                int height = Screen.height;
            }
            if (TokenServerURL != "")
            {
                client.JoinWithTokenServer(ChannelName, UID, TokenServerURL);
            }
            else
            {
                // joing with or without a token
                client.Join(ChannelName, TempToken, UID);
                string joiningChannelMsg = string.Format("Joining channel: {0}, with uid: {1}", ChannelName, UID);
                Debug.Log(joiningChannelMsg);
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

                    // StartCoroutine(UiUpdate(0.5f));
                }
            }
        }

        public void ToggleMic()
        {
            if (!InChannel) return; // only toggle mic when in a channel 

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
        protected virtual void AddCallbackEvents()
        {
            IRtcEngine mRtcEngine = IRtcEngine.QueryEngine();
            mRtcEngine.OnJoinChannelSuccess += OnJoinChannelSuccess;
            mRtcEngine.OnUserJoined += OnUserJoined;
            mRtcEngine.OnJoinChannelSuccess += onTriggerJoinNotify;
            mRtcEngine.OnUserJoined += OnTriggerUserNotify;
            mRtcEngine.OnUserOffline += OnUserOffline;
        }
        

        public void OnJoinChannelSuccess(string channelName, uint uid, int elapsed)
        {

            InChannel = true;
            Debug.Log("user added UID:" + uid);
            chatManager.Login(uid.ToString(),TokenServerURL,uid);
            pbehavior.CreateWalker(uid, Quaternion.Euler(0, 90, 0), new Vector3(-1.777f,-1.0f, -1.0f));
            friendsDisplay.updateProfile(uid);
            // friendsDisplay.UpdateFriends(uid);

            if (VirtualCam != null && VirtualCam.isActiveAndEnabled)
            {
                EnableVirtualCameraSharing();
            }
            else
            {
                Debug.LogError("ERROR: Failed to find perspective camera.");
            }

            if (MicBtn != null)
            {
                MicBtn.GetComponentInChildren<Text>().text = "MIC ON";
                MicBtn.GetComponent<Image>().color = ActiveMicColor;
            }

            // enable dual stream mode
            IRtcEngine mRtcEngine = IRtcEngine.QueryEngine();
            mRtcEngine.EnableDualStreamMode(true);
            string remoteUIDtype;
            remoteUIDtype = "me";
            RemoteUIDs.Add(remoteUIDtype, new List<uint> { uid });
        }

        public void onTriggerJoinNotify(string channelName,uint uid, int elapsed){

            GameObject notify = GameObject.Find("Join Channel Success");
            notify.GetComponent<Animator>().Play("Popup In");
            GameObject textPro = notify.transform.Find("Description").gameObject;

            if(textPro==null){

                Debug.Log("the obj is null");
            }
            else{

                textPro.GetComponent<TextMeshProUGUI>().SetText("join channel success with UID:" + uid);
            }
        }
        public void OnTriggerUserNotify(uint uid, int elapsed){

            GameObject notify = GameObject.Find("New User Added");
            notify.GetComponent<Animator>().Play("Popup In");
            GameObject textPro = notify.transform.Find("Description").gameObject;

            if(textPro==null){

                Debug.Log("the obj is null");
            }
            else{

                textPro.GetComponent<TextMeshProUGUI>().SetText("remote user added UID:" + uid);
            }
        }
        public void VideoShare(uint uid){
            // offset the new video plane based on the parent's number of children.    
            float xOffset = RemoteVideoRoot.transform.childCount * -3.69f;
            MakeVideoView(uid, RemoteVideoRoot, new Vector3(xOffset, 0, 0), Quaternion.Euler(270, 180, 0), new Vector3(1.0f, 1.0f, 0.5625f));
        }
        public void OnUserJoined(uint uid, int elapsed)
        {
            string remoteUIDtype;
            Debug.Log("remote user added UID:" + uid);
            IRtcEngine mRtcEngine = IRtcEngine.QueryEngine();
            mRtcEngine.MuteRemoteVideoStream(uid, true);
            mRtcEngine.MuteRemoteAudioStream(uid, true);
            Vector3 position = new Vector3(0f + Random.Range(-20f, 20f),0f, 0f + Random.Range(-20f, 20f));
            Vector3[] endpoints = new Vector3[] {new Vector3(-22, 0, -10),position};

            pbehavior.CreateWalker(uid, Quaternion.Euler(0, 90, 0), new Vector3(-1.777f,-1.0f, -1.0f));
            friendsDisplay.UpdateFriends(uid);
            NavSwitchManager onButton = NavFriendsDisplay.items[uid][0].Find("Switch").GetComponent<NavSwitchManager>();
            // onButton.onEvent.AddListener(()=>{
            //     VideoShare(uid);
            //     }
            // );
            
            remoteUIDtype = "peer";
            if (RemoteUIDs.ContainsKey(remoteUIDtype)){
                RemoteUIDs[remoteUIDtype].Add(uid);
            }
            else{
                RemoteUIDs.Add(remoteUIDtype, new List<uint> { uid });
            }
            NavInTextDropdown.AddOption(uid.ToString());

        }
        public void Clear(uint uid){
            for (int i = 0; i < items[uid].Count; i++)
            {
                GameObject.Destroy(items[uid][i].gameObject);
            }
        }

        public void OnUserOffline(uint uid, USER_OFFLINE_REASON reason)
        {
            if (RemoteUIDs["peer"].Contains(uid))
            {
                // obj.SetEnable(false);
                RemoteUIDs["peer"].Remove(uid);
                // Clear(uid);
                pbehavior.Clear(uid);
                friendsDisplay.Clear(uid);

            }
            Debug.Log("onUserOffline: update UI");

            // update the position of the remaining children
            // StartCoroutine(UiUpdate(0.5f));
        }

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

        private void MakeVideoView(uint uid, GameObject parentNode, Vector3 position, Quaternion rotation, Vector3 scale)
        {
            Debug.Log("Make Remote Video View for UID: "+ uid);
            GameObject go = GameObject.Find(uid.ToString());
            if (go != null)
            {
                return; // reuse
            }
            // create a GameObject and assign to this new user
            // VideoSurface videoSurface = makePlaneSurface(uid.ToString(), parentNode, position, rotation, scale);
            GameObject thumbView = GameObject.Find("MinimapBorder");
            VideoSurface videoSurface = thumbView.GetComponentInChildren<VideoSurface>();
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
                Debug.LogError("Error: No Render Texture Found. Check Virtual Camera.");
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

            StartCoroutine(PushFrame(bytes, (int)rect.width, (int)rect.height,
            () =>
            {
                bytes = null;
            }));

            RenderTexture.active = null;
        }

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
                rtc.PushVideoFrame(externalVideoFrame);
                // if (timeStampCount % 100 == 0) Debug.Log(" pushVideoFrame(" + timeStampCount + ") size:" + bytes.Length + " => " + a);
            }

            yield return null;
            onFinish();
        }
    }
}

