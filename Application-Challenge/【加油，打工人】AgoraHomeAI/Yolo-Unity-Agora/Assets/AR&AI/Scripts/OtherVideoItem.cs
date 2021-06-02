using System;
using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Threading;
using RenderHeads.Media.AVProVideo;
using SFB;
using UnityEngine;
using UnityEngine.UI;

public class OtherVideoItem : MonoBehaviour
{
    public VideoType type = VideoType.VideoFile;
    // Use this for initialization
    MediaPlayer mediaPlayer;
    public RawImage rawTex;
    RectTransform rawTexRect;
    public InputField netPath;
    public Text choosePath;
    public GameObject choosePanel;
    private System.IntPtr data = Marshal.AllocHGlobal(1920 * 1080 * 4);
    Texture2D nativeTexture;
    string videoPath = @"E:\UnityDocument\Yolo-Unity-Car\curling\Curling2018.mp4";
    public RectTransform[] spritImgs;
    RectTransform selfRect;
    GameObject mediaGo;
    void Start()
    {
        selfRect = GetComponent<RectTransform>();
        rawTexRect = rawTex.GetComponent<RectTransform>();
        nativeTexture = new Texture2D(1920, 1080, TextureFormat.RGB24, false);
      
    }
    void CreatePreview()
    {
        mediaGo = new GameObject();
        mediaGo.AddComponent<MediaPlayer>();
        mediaPlayer = mediaGo.GetComponent<MediaPlayer>();
        mediaPlayer.OpenVideoFromFile(MediaPlayer.FileLocation.AbsolutePathOrURL, videoPath, false);
        mediaPlayer.Control.Play();
    }
    public void Choose()
    {
        if (type == VideoType.VideoFile)
        {
            WriteResult(StandaloneFileBrowser.OpenFilePanel("Open File", "", "mp4", false));
        }
    }
    //打开一个Video文件
    public void OpenVideoFile()
    {
        if (type == VideoType.NetStream)
        {
            if (netPath.text == "")
            {
                return;
            }
            else
            {
                videoPath = netPath.text.Trim();
            }
        }
        if (type == VideoType.VideoFile)
        {
            try
            {
               
            }
            catch (Exception e)
            {
            }
        }
        CreatePreview();
    }
    public void WriteResult(string[] paths)
    {
        string path;
        if (paths.Length > 1)
        {
            return;
        }
        path = paths[0];
        if (path == null)
            return;
        videoPath = path;
        choosePath.text = videoPath;
    }
   
    public void AddItem()
    {
        DeviceManager.instance.AddDeviceOther("视频监控", mediaPlayer, DeviceStatus.ONLINE, type,videoPath);
        mediaGo = null;
        mediaPlayer = null;
        gameObject.SetActive(false);
        Debug.Log("添加");
    }
    public void Cancel()
    {
        if (mediaPlayer != null)
            mediaPlayer.Control.Stop();
        if(mediaGo)
        Destroy(mediaGo);
        choosePanel.SetActive(true);
        transform.parent.gameObject.SetActive(false);
    }
    // Update is called once per frame
    void Update()
    {

        if (mediaPlayer != null)
        {
            if (mediaPlayer.Info.GetVideoWidth() != nativeTexture.width || mediaPlayer.Info.GetVideoHeight() != nativeTexture.height)
            {
                nativeTexture = new Texture2D(mediaPlayer.Info.GetVideoWidth(), mediaPlayer.Info.GetVideoHeight(), TextureFormat.RGB24, false);
            }
            mediaPlayer.ExtractFrame(nativeTexture);
            if (nativeTexture != null)
            {
                byte[] imageBytes = nativeTexture.EncodeToJPG();
                if (imageBytes != null)
                {
                    var container = RTCGameManager.rtcYoloManager.GetYolo().Detect(imageBytes);
                    DetectedCallback(new DeviceItem(), container, nativeTexture, nativeTexture.width, nativeTexture.height);
                }
            }
            rawTex.texture = nativeTexture;
        }
    }
         public void DetectedCallback(DeviceItem device, YoloWrapper.BboxContainer container, Texture2D srcTex, int mWidth, int mHeight)
    {

        float ratH = mHeight * 1.0f / rawTexRect.sizeDelta.y;
        float ratW = mWidth * 1.0f / rawTexRect.sizeDelta.x;
        int count = 0;
        for (int i = 0; i < container.size; i++)
        {
            string name = RTCGameManager.rtcYoloManager.GetYolo().getVOCName(container.candidates[i].obj_id);
            if (container.candidates[i].w != 0 && container.candidates[i].h != 0)
            {

                spritImgs[count].GetComponent<TrickItem>().SetItem(name, (int)container.candidates[i].w, (int)container.candidates[i].h);
                spritImgs[count].anchoredPosition = new Vector2((container.candidates[i].x + container.candidates[i].w / 2) / ratW, (container.candidates[i].y + container.candidates[i].h / 2) / ratH);// * 2;
                                                                                                                                                                                                        //    spritImgs[count].anchoredPosition = scalePos;
                spritImgs[count].transform.localScale = new Vector3(1 / ratW, -1 / ratH, 1);
                spritImgs[count].gameObject.SetActive(true);
                Debug.Log(container.candidates[i].ToString() + "\r\n");
                count++;
            }
            if (count > spritImgs.Length)
                break;

        }
    }

}
