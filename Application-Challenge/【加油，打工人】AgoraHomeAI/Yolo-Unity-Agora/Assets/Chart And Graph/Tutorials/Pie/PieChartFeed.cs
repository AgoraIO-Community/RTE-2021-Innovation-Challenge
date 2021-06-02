#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;
public class PieChartFeed : MonoBehaviour
{
	void Start ()
    {
        PieChart pie = GetComponent<PieChart>();
        if (pie != null)
        {
            pie.DataSource.SlideValue("»À", 50, 10f);
            pie.DataSource.SetValue("Player 2", Random.value * 10);
        }
	}
}
