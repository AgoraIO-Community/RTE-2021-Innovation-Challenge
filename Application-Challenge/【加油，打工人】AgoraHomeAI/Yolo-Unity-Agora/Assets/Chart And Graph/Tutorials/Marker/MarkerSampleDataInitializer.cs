#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class MarkerSampleDataInitializer : MonoBehaviour
{

    public GraphChartBase graph;

	// Use this for initialization
	void Start ()
    {
        graph.DataSource.ClearCategory("Player 1");
        var point = FindObjectOfType<MarkerText>();
        var line = FindObjectOfType<MarkerTextLine>();
        double prevY = 0.0;
        for (int i=0; i<10; i++)
        {
            
            double y = Random.value * 10f;
            graph.DataSource.AddPointToCategory("Player 1", i, y);
            if (i == 5)
                point.point = new DoubleVector2(i, y);

            if( i==7 )
            {
                line.point1 = new DoubleVector2(i - 1,prevY);
                line.point2 = new DoubleVector2(i , y);
            }
            prevY = y;
        }
    }
	
	// Update is called once per frame
	void Update () {
	
	}
}
