using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

public class TrickItem : MonoBehaviour
{
    public Text textName;
    string tName="";
  public  RectTransform rt;
    float timeLimie = 1f;
    float time = 0;
    bool active = false;
    // Start is called before the first frame update
    void Start()
    {
    }
  
    public void SetItem(string name,int width,int height)
    {
        tName = name;
        rt.sizeDelta = new Vector2(width,height);
    //    Debug.Log(tName);
        active = true;
    }
    // Update is called once per frame
    void Update()
    {
        textName.text = tName;
        if (active)
        {
            time += Time.deltaTime;
            if (time > timeLimie)
            {
                time = 0;
                active = false;
                gameObject.SetActive(false);

            }

        }
    }
}
