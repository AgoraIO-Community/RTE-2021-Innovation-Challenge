using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class ShowDetectVideo : MonoBehaviour
{
    public RectTransform[] spritImgs;
    RectTransform selfRect;
    // Start is called before the first frame update
    void Start()
    {
        selfRect = GetComponent<RectTransform>();
    }

    // Update is called once per frame
    void Update()
    {
        
    }
  public  void DetectedCallback(DeviceItem device, YoloWrapper.BboxContainer container,Texture2D srcTex, int mWidth, int mHeight)
    {
        for (int k = 0; k < spritImgs.Length; k++)
        {
            spritImgs[k].gameObject.SetActive(false);
        }
        if (selfRect.sizeDelta.x!= mWidth)
        {
            selfRect.sizeDelta = new Vector2(mWidth, mHeight);
            float rat = mHeight*1.0f / selfRect.transform.parent.GetComponent<RectTransform>().sizeDelta.y;
            selfRect.localScale = selfRect.localScale / rat;
        }
        int count = 0;
        for (int i = 0; i < container.size; i++)
        {
            if (container.candidates[i].w != 0 && container.candidates[i].h != 0)
            {
                string name = RTCGameManager.rtcYoloManager.GetYolo().getVOCName(container.candidates[i].obj_id);
                spritImgs[count].GetComponent<TrickItem>().SetItem(name, (int)container.candidates[i].w , (int)container.candidates[i].h );
                spritImgs[count].anchoredPosition = new Vector2( (container.candidates[i].x + container.candidates[i].w / 2), (container.candidates[i].y + container.candidates[i].h / 2));// * 2;
                spritImgs[count].gameObject.SetActive(true);
                count++;
            }
            if (count > spritImgs.Length)
                break;
          //  Debug.Log(container.candidates[i].ToString() + "\r\n");
        }
    }
}
