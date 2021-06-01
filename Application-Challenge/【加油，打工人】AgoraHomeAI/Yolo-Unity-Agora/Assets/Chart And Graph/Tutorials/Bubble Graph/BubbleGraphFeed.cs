#define Graph_And_Chart_PRO
using UnityEngine;

namespace ChartAndGraph
{
    class BubbleGraphFeed : MonoBehaviour
    {
        string[] items = new string[] { "A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K" };
        void Start()
        {

            GraphChartBase graph = GetComponent<GraphChartBase>();
            if (graph != null)
            {
                
          //      graph.DataSource.StartBatch();
                graph.DataSource.ClearCategory("Player 1"); 
                graph.DataSource.ClearCategory("Player 2");
                for (int i = 0; i < 10; i++)
                {
                    GraphData.AddPointToCategoryWithLabel(graph, "Player 1", Random.value * 10f, Random.value * 10f, Random.value * 3f, items[i], i.ToString());
                    //   graph.DataSource.AddPointToCategory("Player 1", Random.value * 10f, Random.value * 10f, Random.value *3f);
                    //   graph.DataSource.AddPointToCategory("Player 2", Random.value * 10f, Random.value * 10f, Random.value *3f);
                }
        //        graph.DataSource.EndBatch();
            }
        }

    }
}
