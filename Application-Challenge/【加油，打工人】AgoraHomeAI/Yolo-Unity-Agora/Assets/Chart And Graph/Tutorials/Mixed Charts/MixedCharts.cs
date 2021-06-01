#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;
using UnityEngine.UI;

public class MixedCharts : MonoBehaviour {

    public BarChart Bar;
    public GraphChartBase Graph;
    // Use this for initialization
	void Start () {

        StartCoroutine(FillGraphWait());
    }
    /// <summary>
    /// This method waits for the bar chart to fill up before filling the graph
    /// </summary>
    /// <returns></returns>
    IEnumerator FillGraphWait()
    {
        yield return new WaitForEndOfFrame();
        yield return new WaitForEndOfFrame();
        FillGraph();
    }

    void FillGraph()
    {
        Graph.DataSource.ClearCategory("Category1");
        for (int i=0; i<Bar.DataSource.TotalCategories; i++)
        {
            string categoryName = Bar.DataSource.GetCategoryName(i);
            for(int j=0; j<Bar.DataSource.TotalGroups; j++)
            {
                string groupName = Bar.DataSource.GetGroupName(j);
                Vector3 position;
                Bar.GetBarTrackPosition(categoryName, groupName, out position); // find the position of the top of the bar chart
                double x, y;
                Graph.PointToClient(position, out x, out y); // convert it to graph coordinates
                Graph.DataSource.AddPointToCategory("Category1", x, Random.value * 10f); // drop the y value and set your own value
                
            }
        }
    }
	// Update is called once per frame
	void Update () {
	
	}
}
