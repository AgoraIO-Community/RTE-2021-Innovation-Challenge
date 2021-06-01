using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
public class ItemOne : MonoBehaviour
{
    public Text nameT;
    public Text timeT;
    public Text sourceT;
    public Text otherT;
    public Text typeT;
    EventItem selfItem;
    // Start is called before the first frame update
    void Start()
    {
        
    }
    public void InitItem( EventItem item)
    {
        selfItem = item;
        nameT.text = item.name;
        timeT.text = item.time;
        sourceT.text = item.source;
        otherT.text = item.other;
        typeT.text = item.types.ToString() ;
        switch (item.types)
        {
            case EventTypes.Normal:
                ChangeColor(Color.white);
                break;
            case EventTypes.Urge:
                ChangeColor(Color.yellow);
                break;
            case EventTypes.Danger:
                ChangeColor(Color.red);
                break;
        }

    }
    void ChangeColor(Color c)
    {
        nameT.color = c;
        timeT.color =c;
        sourceT.color = c;
        otherT.color =c;
        typeT.color =c;
    }
    // Update is called once per frame
    void Update()
    {
    }
}
