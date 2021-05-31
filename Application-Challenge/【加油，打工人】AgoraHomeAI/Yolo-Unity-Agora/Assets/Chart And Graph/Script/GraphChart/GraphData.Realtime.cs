#define Graph_And_Chart_PRO
using ChartAndGraph.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public partial class GraphData : ScrollableChartData, IInternalGraphData
    {
        partial class Slider : BaseSlider
        {

            public override bool Update()
            {
                BaseScrollableCategoryData baseData;
                CategoryData data;

                if (mParent.mData.TryGetValue(category, out baseData) == false)
                    return true;
                data = (CategoryData)baseData;
                if (data.IsBezierCurve)
                    return false;
                List<DoubleVector3> points = data.Data;

                if (from >= points.Count || index >= points.Count)
                    return true;

                DoubleVector3 fromPoint = points[from];
                DoubleVector3 to = To;
                double time = Time.time;
                time -= StartTime;

                if (Duration <= 0.0001f)
                    time = 1f;
                else
                {
                    time /= Duration;
                    Math.Max(0.0, Math.Min(time, 1.0));
                }
                DoubleVector3 v = DoubleVector3.Lerp(fromPoint, to, time);
                current = v;
                points[index] = v;
                if (time >= 1f)
                {
                    mParent.ModifyMinMax(data, v);
                    return true;
                }

                return false;
            }
        }
    }
}
