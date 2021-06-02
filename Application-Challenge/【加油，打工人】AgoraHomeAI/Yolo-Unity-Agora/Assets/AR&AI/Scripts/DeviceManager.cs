using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Threading;
using RenderHeads.Media.AVProVideo;
using UnityEngine;
using UnityEngine.UI;

public enum DeviceStatus {
    NORMAL,
    ERROR,
    ONLINE,
    OFFLINE
}
public class DeviceItem
{
    public DeviceType deviceType;
    public VideoType videoType;
    public string name;
    public DeviceStatus status;
    public string path;
}
public class DeviceManager : MonoBehaviour
{
    public static DeviceManager instance;
    public GameObject itemPre;
    public RectTransform contentRect;
    public RawImage showInMain;
    public string defaultPath;
    public RectTransform[] spritImgs;
    float itemHeight;
    List<DeviceOne> allDevices;
    ToggleGroup togGroup;
    RectTransform showRect;

    private void Awake()
    {
        instance = this;
        
       
    }
    /* 3，HTTP协议直播源
    香港卫视：http://live.hkstv.hk.lxdns.com/live/hks/playlist.m3u8
CCTV1高清：http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8
CCTV3高清：http://ivi.bupt.edu.cn/hls/cctv3hd.m3u8
CCTV5高清：http://ivi.bupt.edu.cn/hls/cctv5hd.m3u8
CCTV5+高清：http://ivi.bupt.edu.cn/hls/cctv5phd.m3u8
CCTV6高清：http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8
*/
    // Start is called before the first frame update
    void Start()
    {
        allDevices = new List<DeviceOne>();
        itemHeight = itemPre.GetComponent<RectTransform>().sizeDelta.y;
        showRect = showInMain.GetComponent<RectTransform>();
      //  AddDeviceOther("门口监控", DeviceStatus.ONLINE, VideoType.VideoFile, defaultPath);
     //   AddDeviceOther("门口监控", DeviceStatus.ONLINE, VideoType.VideoFile, @"C:\Users\Petit\Desktop\2019新XO谢霆锋TVC_20191211_HD.mp4");
     //   AddDeviceOther("门口监控", DeviceStatus.ONLINE, VideoType.VideoFile, "http://ivi.bupt.edu.cn/hls/cctv1hd.m3u8");
        
    }
    public void AddDeviceAgora(string name, DeviceStatus status,string path,uint id)
    {
        DeviceItem dev = new DeviceItem();
        dev.name = "Agora";
        dev.status = DeviceStatus.ONLINE;
        dev.videoType = VideoType.Agora;
        if (path != null)
            dev.path = path;
        GameObject go = (GameObject)Instantiate(itemPre);
        go.GetComponent<DeviceOne>().InitDeviceAgora(dev,id);
        allDevices.Add(go.GetComponent<DeviceOne>());
        go.transform.SetParent(contentRect);
        AdjustHeight();
        go.SetActive(true);
    }
    public void AddDeviceOther(string name,MediaPlayer player, DeviceStatus status, VideoType type, string path)
    {
        DeviceItem dev = new DeviceItem();
        dev.name = name;
        dev.status = DeviceStatus.ONLINE;
        dev.videoType = type;
        if (path != null)
            dev.path = path;
        GameObject go = (GameObject)Instantiate(itemPre);
        go.GetComponent<DeviceOne>().InitDeviceOther(dev,player);
        allDevices.Add(go.GetComponent<DeviceOne>());
        go.transform.SetParent(contentRect);
        AdjustHeight();
        go.SetActive(true);
    }
    // Update is called once per frame
    void Update()
    {
        for(int i=0;i< allDevices.Count; i++)
        {
            if (allDevices[i].selected)
            {
                showInMain.texture = allDevices[i].nativeTexture;
                showInMain.transform.localScale = allDevices[i].nativeScale;
                allDevices[i].detectedCallback = DetectedCallback;
            }
        }
    }
    public void DetectedCallback(DeviceItem device, YoloWrapper.BboxContainer container, Texture2D srcTex, int mWidth, int mHeight)
    {

        float ratH = mHeight * 1.0f / showRect.sizeDelta.y;
        float ratW = mWidth * 1.0f / showRect.sizeDelta.x;
        int count = 0;
        for (int i = 0; i < container.size; i++)
        {
            string name = RTCGameManager.rtcYoloManager.GetYolo().getVOCName(container.candidates[i].obj_id);
            if (container.candidates[i].w != 0 && container.candidates[i].h != 0)
            {

                spritImgs[count].GetComponent<TrickItem>().SetItem(name, (int)container.candidates[i].w, (int)container.candidates[i].h);
                spritImgs[count].anchoredPosition = new Vector2((container.candidates[i].x + container.candidates[i].w / 2)/ ratW, (container.candidates[i].y + container.candidates[i].h / 2)/ ratH);// * 2;
                                                                                                                                                                                          //    spritImgs[count].anchoredPosition = scalePos;
                spritImgs[count].transform.localScale = new Vector3(1/ ratW,-1/ ratH,1);
                spritImgs[count].gameObject.SetActive(true);
            //    Debug.Log(container.candidates[i].ToString() + "\r\n" );
                count++;
            }

            if (container.candidates[i].prob > 0.4f)
            {
                EventItem item = new EventItem();
                item.id = container.candidates[i].obj_id+"";
                item.name = device.name;
                item.source = device.videoType.ToString();
                item.types = EventTypes.Normal;
                if (container.candidates[i].prob>0.55f)
                item.types =EventTypes.Urge;
                if (container.candidates[i].prob > 0.75f)
                    item.types = EventTypes.Danger;
                item.time = System.DateTime.Now.ToString();
                item.other = name;
                EventMessageCenter.instance.AddEvent(item);
            }
            if (count > spritImgs.Length)
                break;

        }
    }

    void AdjustHeight()
    {
        Vector2 size = contentRect.sizeDelta;
        contentRect.sizeDelta = new Vector2(size.x, allDevices.Count * itemHeight);
    }
}
