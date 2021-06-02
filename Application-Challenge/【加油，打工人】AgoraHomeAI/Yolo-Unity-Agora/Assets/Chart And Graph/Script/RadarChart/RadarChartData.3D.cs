#define Graph_And_Chart_PRO
using ChartAndGraph.DataSource;
using ChartAndGraph.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public partial class RadarChartData : AbstractChartData, IInternalRadarData, IChartData
    {
        /// <summary>
        /// Adds a new category to the radar chart. Each category has it's own materials and name.
        /// Note: you must also add groups to the radar data.
        /// Example: you can set the chart categories to be "Player 1","Player 2","Player 3" in order to compare player achivments
        /// </summary>
        public void Add3DCategory(string name, PathGenerator linePrefab, Material lineMaterial, float lineThickness, GameObject pointPrefab, Material pointMaterial, float pointSize, Material fillMaterial,int fillSmoothing, float curve, float seperation)
        {
            AddInnerCategory(name, linePrefab, lineMaterial, lineThickness, pointPrefab, pointMaterial, pointSize, fillMaterial, fillSmoothing, curve, seperation);

        }


        public void Set3DCategoryOrientation(string category, float seperation, float curve)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.Seperation = seperation;
                data.Curve = curve;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }
        public void Set3DCategoryFill(string category, Material fillMaterial,int fillSmoothing)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.FillMaterial = fillMaterial;
                if (fillSmoothing < 1)
                    fillSmoothing = 1;
                data.FillSmoothing = fillSmoothing;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }

        public void Set3DCategoryLine(string category, PathGenerator linePrefab, Material lineMaterial, float lineThickness)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.LineMaterial = lineMaterial;
                data.LinePrefab = linePrefab;
                data.LineThickness = lineThickness;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }

        public void Set3DCategoryPoint(string category, GameObject prefab, Material pointMaterial, float pointSize)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.PointPrefab = prefab;
                data.PointMaterial = pointMaterial;
                data.PointSize = pointSize;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }

    }
}
