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

        partial void CheckExtended(ref bool result)
        {
            result = true;
        }

        public void AddCategory3DGraph(string category, PathGenerator linePrefab, Material lineMaterial, double lineThickness, MaterialTiling lineTiling, FillPathGenerator fillPrefab, Material innerFill, bool strechFill, GameObject pointPrefab, Material pointMaterial, double pointSize, double depth, bool isCurve, int segmentsPerCurve)
        {
            AddInnerCategoryGraph( category,  linePrefab,  lineMaterial,  lineThickness,  lineTiling,  fillPrefab,  innerFill,  strechFill,  pointPrefab,  pointMaterial,  pointSize,  depth,  isCurve,  segmentsPerCurve);
        }

        /// <summary>
        /// sets the prefabs for a 3d graph category,
        /// </summary>
        /// <param name="category"></param>
        /// <param name="linePrefab"></param>
        /// <param name="fillPrefab"></param>
        /// <param name="dotPrefab"></param>
        public void Set3DCategoryPrefabs(string category, PathGenerator linePrefab, FillPathGenerator fillPrefab, GameObject dotPrefab)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.LinePrefab = linePrefab;
            data.DotPrefab = dotPrefab;
            data.FillPrefab = fillPrefab;
            RaiseDataChanged();
        }

        /// <summary>
        /// sets the depth for a 3d graph category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="depth"></param>
        public void Set3DCategoryDepth(string category, double depth)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            if (depth < 0)
                depth = 0f;
            CategoryData data = (CategoryData)mData[category];
            data.Depth = depth;
            RaiseDataChanged();
        }

        public static void AddPointToCategoryWithLabelRealtime(GraphChartBase chart, string category, DateTime x, double y, double slideTime = 0, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            AddPointToCategoryWithLabelRealtime(chart, category, ChartDateUtility.DateToValue(x), y, slideTime, pointSize, xLabel, yLabel);
        }

        public static void AddPointToCategoryWithLabelRealtime(GraphChartBase chart, string category, double x, DateTime y, double slideTime = 0, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            AddPointToCategoryWithLabelRealtime(chart, category, x, ChartDateUtility.DateToValue(y), slideTime, pointSize, xLabel, yLabel);
        }

        public static void AddPointToCategoryWithLabelRealtime(GraphChartBase chart, string category, DateTime x, DateTime y, double slideTime = 0, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            AddPointToCategoryWithLabelRealtime(chart, category, ChartDateUtility.DateToValue(x), ChartDateUtility.DateToValue(y), slideTime, pointSize, xLabel, yLabel);
        }

        public static void AddPointToCategoryWithLabelRealtime(GraphChartBase chart, string category, double x, double y, double slideTime = 0, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            bool invalidate = false;
            DoubleVector3 item = new DoubleVector3(x, y, 0.0);
            invalidate = chart.VectorValueToStringMap.Remove(item);
            chart.VectorValueToStringMap[item] = new KeyValuePair<string, string>(xLabel, yLabel);

            if (invalidate)
                chart.ClearCache();

            chart.DataSource.AddPointToCategoryRealtime(category, x, y, slideTime, pointSize);
        }

        public void AddPointToCategoryRealtime(string category, DateTime x, DateTime y, double slideTime = 0f, double pointSize = -1f)
        {
            double xVal = ChartDateUtility.DateToValue(x);
            double yVal = ChartDateUtility.DateToValue(y);
            AddPointToCategoryRealtime(category, (double)xVal, (double)yVal, slideTime, pointSize);
        }

        public void AddPointToCategoryRealtime(string category, DateTime x, double y, double slideTime = 0f, double pointSize = -1f)
        {
            double xVal = ChartDateUtility.DateToValue(x);
            AddPointToCategoryRealtime(category, (double)xVal, y, slideTime, pointSize);
        }

        public void AddPointToCategoryRealtime(string category, double x, DateTime y, double slideTime = 0f, double pointSize = -1f)
        {
            double yVal = ChartDateUtility.DateToValue(y);
            AddPointToCategoryRealtime(category, x, (double)yVal, slideTime, pointSize);
        }
        public void SetCategoryArray(string category,DoubleVector3[] array,int start,int count)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];

            if (data.IsBezierCurve == true)
            {
                Debug.LogWarning("Category is Bezier curve. set arrays is supported only for linear categories ");
                return;
            }

            List<DoubleVector3> points = data.Data;
            points.Clear();
            for(int i=0; i<count; i++)
            {
                points.Add(array[start + i]);
                ModifyMinMax(data, array[start + i]);
            }
            RaiseRealtimeDataChanged(points.Count - 1, category);

        }
        public void AddPointToCategoryRealtime(string category, double x, double y, double slideTime = 0f, double pointSize = -1f)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];

            if (data.IsBezierCurve == true)
            {
                Debug.LogWarning("Category is Bezier curve. use AddCurveToCategory instead ");
                return;
            }

            DoubleVector3 point = new DoubleVector3(x, y, pointSize);
            List<DoubleVector3> points = data.Data;

            if (points.Count > 0)
            {
                if (points[points.Count - 1].x > point.x)
                {
                    Debug.LogWarning("realtime points can only be added at the end of the graph");
                    return;
                }
            }


            if (slideTime <= 0f || points.Count == 0)
            {
                points.Add(point);
                ModifyMinMax(data, point);
            }
            else
            {
                Slider s = new Slider(this);
                s.category = category;
                s.from = points.Count - 1;
                s.index = points.Count;
                s.StartTime = Time.time;
                s.Duration = slideTime;
                s.To = point;
                mSliders.Add(s);
                s.current = points[points.Count - 1];
                points.Add(s.current);
            }
            RaiseRealtimeDataChanged(points.Count - 1, category);
        }
    }
}
