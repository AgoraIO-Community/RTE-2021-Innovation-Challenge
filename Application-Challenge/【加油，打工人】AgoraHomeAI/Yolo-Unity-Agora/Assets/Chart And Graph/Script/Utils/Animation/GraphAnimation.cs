#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

public class GraphAnimation : MonoBehaviour
{
    GraphChartBase graphChart;
    public float AnimationTime = 3f;
    public bool ModifyRange = true;
    Dictionary<String, InnerAnimation> mAnimations = new Dictionary<string, InnerAnimation>();

    class InnerAnimation
    {
        public double maxX, minX, maxY, minY;
        public float totalTime = 3f; 
        public float next = -1f;
        public string category;
        public List<DoubleVector2> points;
        public int index;

        public void Update(GraphChartBase graphChart)
        {
            
            if (graphChart == null || points == null || points.Count == 0)
                return;
            if (index >= points.Count)
                return;
            float leapTime = totalTime / (float)points.Count;
            if (next == -1f)
                next = leapTime*2;
            next += Time.deltaTime;
            if (next >= leapTime)
            {
                int totalLeaps = (int)(next / (leapTime));

                for (int i = 0; i < totalLeaps && index<points.Count; i++)
                {
                    DoubleVector2 point = points[index];
                    graphChart.DataSource.AddPointToCategoryRealtime(category, point.x, point.y, leapTime);
                    ++index;
                }
                next -= leapTime * totalLeaps;
            }
        }
    }

    // Use this for initialization
    void Start()
    {
        graphChart = GetComponent<GraphChartBase>();
    }

    bool IsValidDouble(double val)
    {
        if (double.IsNaN(val))
            return false;
        if (double.IsInfinity(val))
            return false;
        return true;
    }

    public void Animate(string category, List<DoubleVector2> points,float totalTime)
    {
        graphChart = GetComponent<GraphChartBase>();
        if (graphChart == null)
            return;
        if (points == null)
            return;
        if (points.Count == 0)
            return;
        InnerAnimation anim = new InnerAnimation();
        anim.maxX = float.MinValue;
        anim.maxY = float.MinValue;
        anim.minX = float.MaxValue;
        anim.minY = float.MaxValue;

        for (int i = 0; i < points.Count; ++i)
        {
            anim.maxX = Math.Max(points[i].x, anim.maxX);
            anim.maxY = Math.Max(points[i].y, anim.maxY);
            anim.minX = Math.Min(points[i].x, anim.minX);
            anim.minY = Math.Min(points[i].y, anim.minY);
        }

        if (ModifyRange)
        {
            double maxX = anim.maxX;
            double maxY = anim.maxY;
            double minX = anim.minX;
            double minY = anim.minY;
            foreach (InnerAnimation a in mAnimations.Values)
            {
                maxX = Math.Max(maxX, a.maxX);
                maxY = Math.Max(maxY, a.maxY);
                minX = Math.Min(minX, a.minX);
                minY = Math.Min(minY, a.minY);
            }
            IInternalGraphData g = graphChart.DataSource;
            maxX = (float)Math.Max(g.GetMaxValue(0, true),maxX);
            minX = (float)Math.Min(g.GetMinValue(0, true), minX);
            maxY = (float)Math.Max(g.GetMaxValue(1, true), maxY);
            minY = (float)Math.Min(g.GetMinValue(1, true), minY);

            if (IsValidDouble(maxX) && IsValidDouble(maxY) && IsValidDouble(minX) && IsValidDouble(minY))
            {
                graphChart.DataSource.StartBatch();
                graphChart.DataSource.AutomaticHorizontalView = false;
                graphChart.DataSource.AutomaticVerticallView = false;
                graphChart.DataSource.HorizontalViewSize = (maxX - minX);
                graphChart.DataSource.HorizontalViewOrigin = minX;
                graphChart.DataSource.VerticalViewSize = (maxY - minY);
                graphChart.DataSource.VerticalViewOrigin = minY;
                graphChart.DataSource.EndBatch();
            }
        }

        anim.points = points;
        anim.index = 0;
        anim.next = 0;
        anim.totalTime = totalTime;
        anim.category = category;
        graphChart.DataSource.ClearCategory(category);
        mAnimations[category] = anim;
    }

    // Update is called once per frame
    void FixedUpdate()
    {
        graphChart = GetComponent<GraphChartBase>();
        if (graphChart == null)
            return;
        foreach(InnerAnimation anim in mAnimations.Values)
        {
            anim.Update(graphChart);
        }
    }
}
