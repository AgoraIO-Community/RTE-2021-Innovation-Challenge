using System.Collections;
using System.Collections.Generic;
using agora_gaming_rtc;
using UnityEngine;
using UnityEngine.UI;
public delegate void YoloDetectedCallback(DeviceItem device,YoloWrapper.BboxContainer con,Texture2D srcTex,int mWidth,int mHeight);
public class ClientsObserver : MonoBehaviour
{
    public RTCVideoItem rtcItem;
    public Transform bkaVideoParent;
    public List<GameObject> allObserver;
    public GameObject prefab;
    public GameObject bigPic;

    GameObject bigPicTemp;
    public Vector2 mis;
    // Start is called before the first frame update
    void Start()
    {
        //   allObserver = new List<GameObject>();

    }

    public void AddObserver(uint uid)
    {

        GameObject go = Instantiate(prefab);
        go.name = uid.ToString();
        go.transform.SetParent(transform);
        go.transform.localScale = new Vector3(1, -1, 1);// Vector3.one;
        go.transform.GetComponent<RectTransform>().anchoredPosition = Vector2.zero;
        go.GetComponent<Button>().onClick.AddListener(delegate
        {
            VideoImageClicked(go.name);
        });
        // set up transform
        VideoSurface videoSurface = go.AddComponent<VideoSurface>();
        // configure videoSurface
        videoSurface.SetForUser(uid);
        videoSurface.SetEnable(true);
        videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
        videoSurface.SetGameFps(30);
        allObserver.Add(go);
        //更新大小
        UpdateSelfSize(allObserver.Count);
    }

    void VideoImageClicked(string name)
    {
        if (bigPicTemp != null)
        {
            if (bigPicTemp.name == name)
            {
                removeObserver(uint.Parse(name));
                AddObserver(uint.Parse(name));
            }
            RemoveBig();
        }
        else
        {
            GameObject go = null;
            for (int i = 0; i < allObserver.Count; i++)
            {
                if (allObserver[i].name == name)
                {
                    go = allObserver[i];
                    break;
                }
                Debug.Log(name);
            }
            if (go != null)
            {
                VideoYoloSurface video1 = go.GetComponent<VideoYoloSurface>();
                DestroyImmediate(video1);
            }

            bigPicTemp = Instantiate(bigPic);
            bigPicTemp.name = name;
            bigPicTemp.transform.SetParent(bkaVideoParent);
            bigPicTemp.GetComponent<RectTransform>().SetAsLastSibling();
            bigPicTemp.GetComponent<RectTransform>().anchoredPosition= bigPic.GetComponent<RectTransform>().anchoredPosition;
            bigPicTemp.transform.localScale = new Vector3(1, -1, 1);
            bigPicTemp.SetActive(true);
            VideoYoloSurface videoSurface = bigPicTemp.AddComponent<VideoYoloSurface>();
            //配置YOLO
            // configure videoSurface
            videoSurface.detectedCallback = bigPicTemp.GetComponent< ShowDetectVideo >(). DetectedCallback;
            videoSurface.SetForUser(uint.Parse(name));
            videoSurface.SetEnable(true);
            videoSurface.SetVideoSurfaceType(AgoraVideoSurfaceType.RawImage);
            videoSurface.SetGameFps(30);
        }
    }
   
    public void RemoveBig()
    {
        if (bigPicTemp != null)
        {
            Destroy(bigPicTemp);
        }


    }
    void UpdateSelfSize(int count)
    {
        RectTransform rectT = GetComponent<RectTransform>();
        //0,50
        //-100,-300
        rectT.offsetMax = Vector2.zero;
        rectT.offsetMin = new Vector2(-70*count, -100);
    }
    public void removeObserver(uint uid)
    {
        GameObject go = null;
        for (int i = 0; i < allObserver.Count; i++)
        {
            if (allObserver[i].name == uid.ToString())
            {
                go = allObserver[i];
                break;
            }
            Debug.Log(uid.ToString());
        }
        if (go != null)
        {
            allObserver.Remove(go);
            DestroyImmediate(go);
        }
        if (bigPicTemp != null && uid + "" == bigPicTemp.name)
            RemoveBig();
        //更新大小
        UpdateSelfSize(allObserver.Count);
    }
    // Update is called once per frame
    void Update()
    {
            if (bigPicTemp == null)
                return;

    }
}
