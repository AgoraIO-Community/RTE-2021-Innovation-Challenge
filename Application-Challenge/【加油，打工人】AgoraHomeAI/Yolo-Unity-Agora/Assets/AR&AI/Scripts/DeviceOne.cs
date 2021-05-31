using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Threading;
using agora_gaming_rtc;
using RenderHeads.Media.AVProVideo;
using UnityEngine;
using UnityEngine.UI;
public class DeviceOne : MonoBehaviour
{
    public Text typeT;
    public Text nameT;
    public Text statusT;
    public RawImage preImg;
    public Texture2D tempTex;
    public bool selected = true;
    VideoType videoType=VideoType.VideoFile;
    DeviceItem device;

  public  Texture2D nativeTexture;
    public Vector3 nativeScale;
    Thread workThread;
    bool start = false;

    MediaPlayer mediaPlayer;
    GameObject mediaGo;
    Toggle toggle;
    Image selfBkg;
    public YoloDetectedCallback detectedCallback;
    // Start is called before the first frame update
    void Start()
    {
        toggle = GetComponent<Toggle>();
        toggle.onValueChanged.AddListener(OnValueChanged);//监听方法是Bool委托
        selfBkg = GetComponent<Image>();
        nativeTexture = new Texture2D(1920, 1080, TextureFormat.RGB24, false);
    }
    private void OnValueChanged(bool value)
    {
        transform.GetChild(0).gameObject.SetActive(value);//获取isOn 的值 控制子物体 的显示隐藏      
        selected = value;
        if(mediaPlayer!=null)
        mediaPlayer.Control.MuteAudio(!selected); 

    }
    public void InitDeviceAgora(DeviceItem item,uint id)
    {
  
        //yoloWrapper = yolo;
        device = item;
        videoType = item.videoType;
        start = true;
        VideoYoloSurface videoSurface = preImg.gameObject.AddComponent<VideoYoloSurface>();
        //配置YOLO
        // configure videoSurface
        videoSurface.detectedCallback = DetectedCallback;
        videoSurface.SetForUser(id);
        videoSurface.SetEnable(true);
        videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
        videoSurface.SetGameFps(30);
    }
    public void InitDeviceOther(DeviceItem item,MediaPlayer player)
    {
        device = item;
        videoType = item.videoType;
        mediaGo = player.gameObject;
        mediaPlayer = player;
        mediaPlayer.Control.MuteAudio(true);
    }
    public Texture2D GetSelfImage()
    {
        return tempTex;
    }
    // Update is called once per frame
    void Update()
    {
        int w = 0, h = 0;
        if (device != null)
        {
            typeT.text = device.videoType.ToString();
            nameT.text = device.name;
            statusT.text = device.status.ToString();
        }
        if (selected)
        {
            selfBkg.color = new Color(1, 1, 0);
        }
        else
        {
            selfBkg.color = new Color(1, 1, 1);
        }
        if (videoType == VideoType.Agora)
        {
            nativeScale = new Vector3(1, -1, 1);
        }
        else
        {
            nativeScale = new Vector3(1, 1, 1);
            if (mediaPlayer != null)
            {
                if(mediaPlayer.Info.GetVideoWidth()!= nativeTexture.width|| mediaPlayer.Info.GetVideoHeight() != nativeTexture.height)
                {
                    nativeTexture= new Texture2D(mediaPlayer.Info.GetVideoWidth(), mediaPlayer.Info.GetVideoHeight(), TextureFormat.RGB24, false);
                }
                 mediaPlayer.ExtractFrame(nativeTexture);
                if (nativeTexture != null&&selected)
                {
                    byte[] imageBytes = nativeTexture.EncodeToJPG();
                    if (imageBytes != null)
                    {
                        var container = RTCGameManager.rtcYoloManager.GetYolo().Detect(imageBytes);
                        if (detectedCallback != null)
                            detectedCallback.Invoke(device,container, nativeTexture, nativeTexture.width, nativeTexture.height);
                    }
                }
                preImg.texture = nativeTexture;
            }
        }
        


    }
    public void DetectedCallback(DeviceItem device, YoloWrapper.BboxContainer container,Texture2D srcTex, int mWidth, int mHeight)
    {
        nativeTexture = srcTex;
        if(detectedCallback!=null)
        detectedCallback.Invoke(device,container, srcTex, mWidth, mHeight);
        for (int i = 0; i < container.size; i++)
        {
            Debug.Log(container.candidates[i].ToString() + "\r\n");
        }
    }
    public void SetSelect(bool sel)
    {
        selected = sel;
    }
    private void OnDestroy()
    {
        if (workThread != null && workThread.IsAlive)
            workThread.Abort();
        //关闭
      //  if(yoloWrapper!=null)
        //   yoloWrapper.Dispose();
    }
}
