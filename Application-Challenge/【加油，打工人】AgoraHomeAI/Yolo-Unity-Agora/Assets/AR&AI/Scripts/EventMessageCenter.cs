using System.Collections;
using System.Collections.Generic;
using UnityEngine;
public enum EventTypes
{
    Normal,
    Urge,
    Danger
}
public class EventItem{
    public string id;
   public string name;
    public string time;
    public string source;
    public string other;
    public EventTypes types;
}

public class EventMessageCenter : MonoBehaviour
{
    public static EventMessageCenter instance;
    public GameObject itemPre;
    public RectTransform contentRect;
    Queue<EventItem> totalItems;
    List<RectTransform> itemGos;
    float itemHeight=30f;
    int messagePoolCount = 30;
    int currentIndex = 0;
     void Awake()
    {
        instance = this;   
    }
    // Start is called before the first frame update
    void Start()
    {
        itemHeight = itemPre.GetComponent<RectTransform>().sizeDelta.y;
        itemGos = new List<RectTransform>(messagePoolCount);
        totalItems = new Queue<EventItem>();
        for (int i = 0; i < messagePoolCount; i++)
        {
            GameObject go = (GameObject)Instantiate(itemPre);
            go.SetActive(false);
            itemGos.Add(go.GetComponent<RectTransform>());
        }
    }

    public void AddEvent(EventItem item)
    {
        totalItems.Enqueue(item);
    }
    // Update is called once per frame
    void Update()
    {
        if (totalItems.Count > 0)
        {
            currentIndex++;
            if(currentIndex>= messagePoolCount)
            {
                currentIndex = 0;
            }
            itemGos[currentIndex].GetComponent<ItemOne>().InitItem(totalItems.Dequeue());
            itemGos[currentIndex].SetParent(contentRect);
            itemGos[currentIndex].gameObject.SetActive(true);
            itemGos[currentIndex].SetAsFirstSibling();
            AdjustHeight();
        }
    }
    void AdjustHeight()
    {
        Vector2 size = contentRect.sizeDelta;
        contentRect.sizeDelta = new Vector2(size.x, contentRect.childCount* itemHeight);
    }
}




