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
        public void Letsgo(uint uid){
            //start movement on the new path
            // NavMoveit moveRef = items[uid][0].gameObject.GetComponent<NavMoveit>();
            NavMoveit moveRef = getMove(uid);
            moveRef.moveToPath = false;
            moveRef.SetPath(NavWaypointManager.Paths[uid.ToString()]);
        }
        public Transform NavPath( uint uid, Vector3[] endpoints){
            //nav path generate after set destination
            // GameObject path = (GameObject)Instantiate(pathPrefab);
            // path7  will be replace with new destination name
            GameObject path = new GameObject(uid.ToString());

            NavPathManager pathManager = path.AddComponent<NavPathManager>();
            pathManager.name = uid.ToString();
            path.name = uid.ToString();
            pathManager.Create(GenerateWayPoints(endpoints));

            path.AddComponent<NavPathRenderer>();
            path.GetComponent<LineRenderer>().material = new Material(Shader.Find("Sprites/Default"));

            // WaypointManager.AddPath(path);
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
            // GameObject walker2 = (GameObject)Instantiate(walkerPrefab, endpoints[0], rotation);
            // GameObject walker3 = (GameObject)Instantiate(walkerPrefab, endpoints[1], rotation);
            walker.name = "walker"+uid;
            NavMoveit myMove =  walker.GetComponent<NavMoveit>();
            myMove.onStart = false;

            items.Add(uid, new List<Transform> { walker.transform });
            if(items.ContainsKey(uid)){
                items[uid].Add(NavPath(uid, endpoints));
            }
            else{
                Debug.Log("walker uid not in ObjectDic");
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
