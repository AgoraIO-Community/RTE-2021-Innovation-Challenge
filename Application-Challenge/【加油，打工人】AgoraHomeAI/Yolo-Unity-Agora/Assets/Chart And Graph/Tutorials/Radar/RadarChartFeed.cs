#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class RadarChartFeed : MonoBehaviour {

	// Use this for initialization
	void Start ()
    {
        var radar = GetComponent<RadarChart>();
        if (radar != null)
        {
            radar.DataSource.SetValue("Player 1", "A", 10);
        }
    }
	
	// Update is called once per frame
	void Update () {

    }
}
