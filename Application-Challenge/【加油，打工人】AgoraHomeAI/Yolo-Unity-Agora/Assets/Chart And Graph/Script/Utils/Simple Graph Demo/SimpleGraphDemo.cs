#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;
using System;

public class SimpleGraphDemo : MonoBehaviour
{
    public GraphChart Graph;
    public int TotalPoints = 10;
	void Start () {

        if (Graph == null) // the ChartGraph info is obtained via the inspector
            return;
        DateTime x = DateTime.Now.AddDays(TotalPoints); // x is marked as a date
        Graph.DataSource.StartBatch(); // calling StartBatch allows changing the graph data without redrawing the graph for every change
        Graph.DataSource.ClearCategory("Achivments Per Day"); // clear the "test" category. this category is defined using the GraphChart inspector
        for (int i=0; i< TotalPoints; i++)  //add random points to the graph
        {
            Graph.DataSource.AddPointToCategory("Achivments Per Day", x, UnityEngine.Random.value*30f); // each time we call AddPointToCategory 
            x += TimeSpan.FromDays(1);
        }
        Graph.DataSource.EndBatch(); // finally we call EndBatch , this will cause the GraphChart to redraw itself
    }
	
	void Update () {
	
	}
}
