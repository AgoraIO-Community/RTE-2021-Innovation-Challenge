using System.Collections;
using System.Collections.Generic;
using System.Runtime.InteropServices;
using System.Threading;
using UnityEngine;
using UnityEngine.UI;

public class Test : MonoBehaviour
{
string fileName= @"E:\UnityDocument\Yolo-Unity-Car\curling\curling114.jpg";

    // Use this for initialization
    YoloWrapper yoloWrapper = null;
    public RawImage rawTex;
	public Texture2D texture;
    public Text text;
    private System.IntPtr data = Marshal.AllocHGlobal(1920 * 1080 * 4);
    Texture2D nativeTexture  ;
    void Start()
    {
        yoloWrapper = new YoloWrapper();
        nativeTexture = new Texture2D(1920, 1080, TextureFormat.RGB24, false);
        InvokeRepeating("TestShow", 1, 1f);
    }
    public void Click()
    {
       //  YoloWrapper.bbox_t[] bbox = yoloWrapper.Detect(fileName);
        YoloWrapper.BboxContainer container = yoloWrapper.Detect(texture.EncodeToJPG());
        rawTex.texture = yoloWrapper.Resize(texture.EncodeToJPG(),100,100);
        Debug.Log("size"+container.size);

        for (int i = 0; i < container.size; i++)
        {
            Debug.Log(container.candidates[i].ToString() + "\r\n");
       }
        Thread tt = new Thread(HHH);
        tt.Start();
    }
    int count = 0;
    void TestShow()
    {
        count++;
       // text.text = count+"";
       
    }
    bool start = false;
    void HHH()
    {
        string vifeo = @"E:\UnityDocument\Yolo-Unity-Car\curling\Curling2018.mp4";
        string httpv = "http://ivi.bupt.edu.cn/hls/cctv6hd.m3u8";
        yoloWrapper.DetectOnNet(vifeo, "video");

    }
   public void setOk()
    {
        start = true;
    }
    // Update is called once per frame
    void Update()
    {
        int w = 0, h = 0;
        if (start)
        {
            YoloWrapper.BboxContainer container = yoloWrapper.UpdateRawData(data, ref w, ref h);
            if (w != 0 || h != 0)
            {
                if ((w != nativeTexture.width || h != nativeTexture.height))
                {
                    nativeTexture = new Texture2D(w, h, TextureFormat.RGB24, false);
                    
                }
                nativeTexture.LoadRawTextureData(data, w * h * 3);
                nativeTexture.Apply();
                rawTex.texture = nativeTexture;
            }
            text.text = count + "|" + container.size + "||" + w + "||" + h;
        }


    }
    private void OnApplicationQuit()
    {
        yoloWrapper.Dispose();
    }
}
