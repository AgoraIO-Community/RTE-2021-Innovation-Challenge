using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace indoorNav{
    public class NavPathBehavior : MonoBehaviour
    {
        [SerializeField] 
        public GameObject walkerPrefab;
        // Start is called before the first frame update
        public static readonly Dictionary<uint, List<Transform>> items = new Dictionary<uint, List<Transform>>();

        void Start()
        {

        }
        public void Letsgo(uint goalUID){
            //start movement on the new path

            // bool isFinished = (items[startUID][0].transform.position - items[goalUID][0].transform.position) < 1;
            uint startUID = NavRtcManager.RemoteUIDs["me"][0];
            Vector3[] endpoints = new Vector3[] {items[startUID][0].transform.position,items[goalUID][0].transform.position};
            string name = startUID.ToString() + goalUID.ToString();
            NavMoveit moveRef = getMove(startUID);
            moveRef.moveToPath = false;
            items[startUID].Add(NavPath(name, endpoints));

            moveRef.SetPath(NavWaypointManager.Paths[name]);
        }
        public Transform NavPath( string name, Vector3[] endpoints){
            //nav path generate after set destination
            // GameObject path = (GameObject)Instantiate(pathPrefab);
            // path7  will be replace with new destination name
            GameObject path = new GameObject(name);
            NavPathManager pathManager = path.AddComponent<NavPathManager>();
            pathManager.name = name;
            path.name = name;
            pathManager.Create(GenerateWayPoints(endpoints));

            path.AddComponent<NavPathRenderer>();
            path.GetComponent<LineRenderer>().material = new Material(Shader.Find("Sprites/Default"));

            if(NavWaypointManager.Paths.ContainsKey(name)){
                NavWaypointManager.Paths.Remove(name);
                NavWaypointManager.AddPath(path);
            }
            return path.transform;
        }
        public Transform[] GenerateWayPoints(Vector3[] endpoints){

            Vector3[] positions = endpoints;

            Transform[] waypoints = new Transform[positions.Length];

            //instantiate waypoints
            for (int i = 0; i < positions.Length; i++)
            {
                Debug.Log(" " + positions[i] + " " );
                GameObject newPoint = new GameObject("Waypoint " + i);
                waypoints[i] = newPoint.transform;
                waypoints[i].position = positions[i];
            }
            return waypoints;
        }
        public void CreateWalker(uint uid, Quaternion rotation, Vector3 scale)
        {
            
            // initial position of new user
            Vector3 position = new Vector3(0f + Random.Range(-20f, 20f),0f, 0f + Random.Range(-20f, 20f));
            Vector3[] endpoints = new Vector3[] {new Vector3(-22, 0, -10),position};

            //instantiate walker prefab , walker inital locationlization with random value
            GameObject walker = (GameObject)Instantiate(walkerPrefab, position, rotation);
            walker.name = "walker"+uid;
            NavMoveit myMove =  walker.GetComponent<NavMoveit>();
            myMove.onStart = false;
            // items[uid].Add(NavPath(uid, endpoints));
            if(items.ContainsKey(uid)){
                Debug.Log("there is already worker under this uid!!!!");
            }
            else{
                items.Add(uid, new List<Transform> { walker.transform });
            }
        }
        public NavMoveit getMove(uint uid){
            return items[uid][0].gameObject.GetComponent<NavMoveit>();
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
