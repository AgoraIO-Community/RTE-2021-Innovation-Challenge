#define Graph_And_Chart_PRO
using UnityEngine;
using System.Collections;
using ChartAndGraph;

public class BarAnimation : MonoBehaviour
{
    public AnimationCurve Curve = AnimationCurve.Linear(0f, 0f, 1f, 1f);
    public bool AnimateOnStart = true;
   // public bool AnimateOnEnable = true;
    public float AnimationTime = 3f;
    BarChart barChart;

	// Use this for initialization
	void Start ()
    {
        barChart = GetComponent<BarChart>();
        if (AnimateOnStart)
            Animate();
    }
 /*   public void OnEnable()
    {
        barChart = GetComponent<BarChart>();
        if (AnimateOnEnable)
            Animate();
    }*/
    public void Animate()
    {
        if(barChart != null)
        {
            double max = barChart.DataSource.GetMaxValue();
            double min = barChart.DataSource.GetMinValue();
            barChart.DataSource.StartBatch();
            barChart.DataSource.AutomaticMaxValue = false;
            barChart.DataSource.AutomaticMinValue = false;
            barChart.DataSource.MaxValue = max;
            barChart.DataSource.MinValue = min;
            for (int i=0; i<barChart.DataSource.TotalCategories; i++)
                for(int j=0; j<barChart.DataSource.TotalGroups; j++)
                {
                    string category = barChart.DataSource.GetCategoryName(i);
                    string group = barChart.DataSource.GetGroupName(j);
                    double val = barChart.DataSource.GetValue(category, group);
                    barChart.DataSource.SetValue(category, group,0.0);
                    barChart.DataSource.SlideValue(category, group, val, AnimationTime, Curve);
                }
            barChart.DataSource.EndBatch();
        }
    }

	// Update is called once per frame
	void Update () {
	
	}
}
