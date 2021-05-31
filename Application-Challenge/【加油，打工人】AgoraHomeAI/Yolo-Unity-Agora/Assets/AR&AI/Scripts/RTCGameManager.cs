using UnityEngine;
using UnityEngine.UI;

using agora_gaming_rtc;
using agora_utilities;


// this is an example of using Agora Unity SDK
// It demonstrates:
// How to enable video
// How to join/leave channel
// 
public class RTCGameManager:MonoBehaviour
{
    public static RTCGameManager rtcYoloManager;
    // instance of agora engine
    private IRtcEngine mRtcEngine;
    private Text MessageText;
    public string appId;
    YoloWrapper yoloWrapper = null;
    private void Awake()
    {
        rtcYoloManager = this;
  
        loadEngine(appId);
    }
    void Start()
    {
        yoloWrapper = new YoloWrapper();
    }
    public  YoloWrapper GetYolo()
    {
        return yoloWrapper;
    }
    // load agora engine
    public void loadEngine(string appId)
    {
        // start sdk
        Debug.Log("initializeEngine");

        if (mRtcEngine != null)
        {
            Debug.Log("Engine exists. Please unload it first!");
            return;
        }
        // init engine
        mRtcEngine = IRtcEngine.GetEngine(appId);
        // enable log
        mRtcEngine.SetLogFilter(LOG_FILTER.DEBUG | LOG_FILTER.INFO | LOG_FILTER.WARNING | LOG_FILTER.ERROR | LOG_FILTER.CRITICAL);
    }
     void Update()
    {
        if (Input.GetKeyUp(KeyCode.Escape))
        {
            Application.Quit();
        }
    }

    // unload agora engine
    public void unloadEngine()
    {
        Debug.Log("calling unloadEngine");

        // delete
        if (mRtcEngine != null)
        {
            IRtcEngine.Destroy();  // Place this call in ApplicationQuit
            mRtcEngine = null;
        }
    }
   
    private void OnApplicationQuit()
    {
        unloadEngine();

        if (yoloWrapper != null)
            yoloWrapper.Dispose();
    }

}
