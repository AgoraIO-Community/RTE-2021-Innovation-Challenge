using System;
using System.Collections;
using System.Collections.Generic;
using RenderHeads.Media.AVProVideo;
using UnityEngine;
using UnityEngine.UI;

public class TestYo : MonoBehaviour
{
    YoloWrapper yolo;
    public MediaPlayer player;
    public RawImage rawImg;
    bool tt = false;
    // Start is called before the first frame update
    void Start()
    {
        yolo = new YoloWrapper();
    }

    public void Test()
    {
        tt = true;
    }
    // Update is called once per frame
    void Update()
    {
        if (Input.GetKey(KeyCode.A))
        {
            Test();
        }
        if (tt)
        {
            Texture tex = player.TextureProducer.GetTexture();
            Texture2D frame = player.ExtractFrame(null);
            if (frame != null)
            {
                byte[] imageBytes = frame.EncodeToJPG();
                if (imageBytes != null)
                {
                    var container = yolo.Detect(imageBytes);

                    Debug.LogError(container.size);
                }
            }
        }
    }
}
