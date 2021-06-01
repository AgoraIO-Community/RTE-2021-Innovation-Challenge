using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;

namespace indoorNav{

    public class NavFriendsDisplay : MonoBehaviour
    {
        // Start is called before the first frame update
        public Dictionary<uint, List<Transform>> items = new Dictionary<uint, List<Transform>>();
        [SerializeField] 
        public GameObject buttonPrefab;

        NavPathBehavior pathBehavior;
        
        void Start()
        {
            pathBehavior = this.GetComponent<NavPathBehavior>();
        }
        public void UpdateFriends(uint uid){
            GameObject list =  GameObject.Find("Friend List").transform.Find("List").gameObject;
            GameObject fdton = (GameObject)Instantiate(buttonPrefab);
            fdton.transform.Find("Name").GetComponent<TextMeshProUGUI>().SetText(uid.ToString());
            if(list != null){
                fdton.transform.SetParent(list.transform,false);
                items.Add(uid, new List<Transform> { fdton.transform });
                // items[uid].Add(fdton.transform);
            }
            else{
                Debug.Log(("List obj is null"));
            }
            Transform go = items[uid][0].Find("Sub Panel").Find("Nav");
            if (go != null)
            {
                Button navton = go.gameObject.GetComponent<Button>();
                if(navton != null){

                    navton.onClick.AddListener(() =>
                    {
                        pathBehavior.Letsgo(uid);
                    });
                }
                else{
                    Debug.Log("navbutton obj is null");
                }
            }
            else{
                Debug.Log("Nav obj is null");
            }
        }
        public void Clear(uint uid){
            for (int i = 0; i < items[uid].Count; i++)
            {
                GameObject.Destroy(items[uid][i].gameObject);
            }
        }
        // Update is called once per frame
        void Update()
        {
            
        }
    }
}
