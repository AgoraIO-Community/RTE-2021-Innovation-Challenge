#define Graph_And_Chart_PRO
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;

public class WaveGraph : MonoBehaviour
{
    public GraphChart Graph;
    public int PointCount = 100;
    DoubleVector3[] array;
    void Start()
    {
        array = new DoubleVector3[PointCount];
        if (Graph == null) // the ChartGraph info is obtained via the inspector
            return;
        Graph.DataSource.StartBatch(); // calling StartBatch allows changing the graph data without redrawing the graph for every change
        Graph.DataSource.ClearCategory("Player 1"); // clear the "Player 1" category. this category is defined using the GraphChart inspector

        for (int i = 0; i < array.Length; i++)  //add random points to the graph
        {
            Graph.DataSource.AddPointToCategory("Player 1", i, Random.value * 20f + 10f); // each time we call AddPointToCategory 
        }

        Graph.DataSource.EndBatch(); // finally we call EndBatch , this will cause the GraphChart to redraw itself
    }

    void Update()
    {
        for(int i=0; i<array.Length; i++)
        {
            array[i] = new DoubleVector3(i, Random.value * 20f, -1.0);
        }
        Graph.DataSource.SetCategoryArray("Player 1", array,0,array.Length); // set the array each frame
    }
}
