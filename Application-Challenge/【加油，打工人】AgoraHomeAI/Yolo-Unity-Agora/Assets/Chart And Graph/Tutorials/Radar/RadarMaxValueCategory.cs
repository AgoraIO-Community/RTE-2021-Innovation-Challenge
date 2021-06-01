#define Graph_And_Chart_PRO
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class RadarMaxValueCategory : MonoBehaviour
{
    // Start is called before the first frame update
    void Start()
    {

        var radar = GetComponent<RadarChart>();
        if (radar != null)
        {
            radar.DataSource.SetCategoryMaxValue("Player 1",20);
            radar.DataSource.SetValue("Player 1", "A", 10);
        }
    }

    // Update is called once per frame
    void Update()
    {
        
    }
}
