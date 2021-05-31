using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public enum VideoType
{
    Agora,
    VideoFile,
    NetStream,
    WebCam
}
public class VideoTypePanel : MonoBehaviour
{
    VideoType videoType;
    public Dropdown dropDrown;
    public GameObject[] panels;
     int current =0;
    // Start is called before the first frame update
    void Start()
    {
        dropDrown.onValueChanged.AddListener(dropValueChanged);
        videoType = VideoType.Agora;
    }
    public void AddWatcher()
    {
        for (int i = 0; i < panels.Length; i++)
        {
            panels[i].SetActive(false);
        }
        panels[current].SetActive(true);
    }
    void dropValueChanged(int index)
    {
        videoType = (VideoType)index;
        Debug.Log("videoType"+ videoType);
        if (current == index)
            return;
        for (int i = 0; i < panels.Length; i++)
        {
            panels[i].SetActive(false);
        }
        if (current != index)
        {
            current = index;
            panels[index].SetActive(true);
        }

    }
    public void ClosePanel()
    {
        panels[current].SetActive(false);
        current = 0;
        gameObject.SetActive(false);
    }
    // Update is called once per frame
    void Update()
    {
        
    }
}
