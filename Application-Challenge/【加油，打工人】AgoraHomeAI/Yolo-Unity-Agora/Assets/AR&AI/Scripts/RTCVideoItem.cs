using System.Collections;
using System.Collections.Generic;
using agora_gaming_rtc;
using UnityEngine;
using UnityEngine.UI;

public class RTCVideoItem : MonoBehaviour
{
    public InputField inputChanel;
    // instance of agora engine
    private IRtcEngine mRtcEngine;
    private Text MessageText;
    public ClientsObserver observer;
  //  public YoloWrapper yoloWrapper;
    uint currId=0 ;
    // Start is called before the first frame update
    void Start()
    {
        mRtcEngine = IRtcEngine.QueryEngine();
        if (mRtcEngine == null)
        {
            Debug.LogError("请初始化引擎");
        }
    //    yoloWrapper = new YoloWrapper();// RTCGameManager.rtcYoloManager.GetYolo();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
    public void JoinChanel()
    {
        if(inputChanel.text!="")
        join(inputChanel.text.Trim());
    }
    public void join(string channel)
    {
        Debug.Log("calling join (channel = " + channel + ")");

        if (mRtcEngine == null)
            return;
        // set callbacks (optional)
        mRtcEngine.OnJoinChannelSuccess = onJoinChannelSuccess;
        mRtcEngine.OnUserJoined = onUserJoined;
        mRtcEngine.OnUserOffline = onUserOffline;
        mRtcEngine.OnWarning = (int warn, string msg) =>
        {
            Debug.LogWarningFormat("Warning code:{0} msg:{1}", warn, IRtcEngine.GetErrorDescription(warn));
        };
        mRtcEngine.OnError = HandleError;

        // enable video
        mRtcEngine.EnableVideo();
        // allow camera output callback
        mRtcEngine.EnableVideoObserver();

        // join channel
        mRtcEngine.JoinChannel(channel, null, 0);
    }

    public string getSdkVersion()
    {
        string ver = IRtcEngine.GetSdkVersion();
        return ver;
    }
    private void OnDisable()
    {
        Debug.Log("Leave");
      //  Leave();
    }
    public void Leave()
    {
        Debug.Log("calling leave");

        if (mRtcEngine == null|| currId == 0)
            return;
        // leave channel
        mRtcEngine.LeaveChannel();
        // deregister video frame observers in native-c code
        mRtcEngine.DisableVideoObserver();
    }
    public void AddItem()
    {
        Debug.Log("Create item");
        if (currId != 0)
        {
            DeviceManager.instance.AddDeviceAgora("Agora", DeviceStatus.ONLINE, null, currId);
            observer.removeObserver(currId);
            observer.RemoveBig();
        }
    }
    public void Cancel()
    {
        Leave();
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
    // implement engine callbacks
    private void onJoinChannelSuccess(string channelName, uint uid, int elapsed)
    {
        Debug.Log("JoinChannelSuccessHandler: uid = " + uid);
        GameObject textVersionGameObject = GameObject.Find("VersionText");
        textVersionGameObject.GetComponent<Text>().text = "SDK Version : " + getSdkVersion();

    }

    // When a remote user joined, this delegate will be called. Typically
    // create a GameObject to render video on it
    private void onUserJoined(uint uid, int elapsed)
    {
        Debug.Log("onUserJoined: uid = " + uid + " elapsed = " + elapsed);
        currId = uid;
        // this is called in main thread
        observer.AddObserver(uid);


    }
    private const float Offset = 100;

    // When remote user is offline, this delegate will be called. Typically
    // delete the GameObject for this user
    private void onUserOffline(uint uid, USER_OFFLINE_REASON reason)
    {
        // remove video stream
        Debug.Log("onUserOffline: uid = " + uid + " reason = " + reason);
        // this is called in main thread
        GameObject go = GameObject.Find(uid.ToString());
        if (!ReferenceEquals(go, null))
        {
            Object.Destroy(go);
        }
    }
    #region Error Handling
    private int LastError { get; set; }
    private void HandleError(int error, string msg)
    {
        if (error == LastError)
        {
            return;
        }

        msg = string.Format("Error code:{0} msg:{1}", error, IRtcEngine.GetErrorDescription(error));

        switch (error)
        {
            case 101:
                msg += "\nPlease make sure your AppId is valid and it does not require a certificate for this demo.";
                break;
        }

        Debug.LogError(msg);
        if (MessageText != null)
        {
            if (MessageText.text.Length > 0)
            {
                msg = "\n" + msg;
            }
            MessageText.text += msg;
        }

        LastError = error;
    }

    #endregion
}
