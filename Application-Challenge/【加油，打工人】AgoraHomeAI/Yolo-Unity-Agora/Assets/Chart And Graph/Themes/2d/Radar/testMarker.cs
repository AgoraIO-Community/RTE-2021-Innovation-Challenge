#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class testMarker : MonoBehaviour
{
    public GameObject Place;

	// Use this for initialization
	void Start ()
    {
	}

	// Update is called once per frame
	void Update ()
    {
        var radar = GetComponent<RadarChart>();
        string group;
        double amount;
        if(radar.SnapWorldPointToPosition(Input.mousePosition,out group,out amount))
        {
            Vector3 position;
            if(radar.ItemToWorldPosition(group,amount ,out position))
            {
                Place.transform.position = position;
            }
        }
    }
}
