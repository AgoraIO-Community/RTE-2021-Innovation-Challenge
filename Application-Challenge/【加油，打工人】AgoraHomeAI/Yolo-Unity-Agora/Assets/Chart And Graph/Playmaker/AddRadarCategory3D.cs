#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("3D Radar Chart Category")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("Adds or updates a category in a 3d radar chart with advanced settings. If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class AddRadarCategory3DAdvanced : FsmStateAction
    {
        [CheckForComponent(typeof(WorldSpaceRadarChart))]
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;

        [ObjectType(typeof(PathGenerator))]
        [Tooltip("The prefab of the line part of the chart, or null")]
        public FsmObject LinePrefab;

        [Tooltip("The material used for the line part of the category, or null ")]
        public FsmMaterial LineMaterial;

        [Tooltip("The thinkness of the 3d graph line")]
        public FsmFloat LineThickness;

        [Tooltip("The prefab of the point part of the chart, or null")]
        public FsmGameObject PointPrefab;

        [Tooltip("The material used for the point part of the category , or null ")]
        public FsmMaterial PointMaterial;

        [Tooltip("The size of the 3d graph point")]
        public FsmFloat PointSize;

        [Tooltip("The material used for the fill part of the category, or null ")]
        public FsmMaterial FillMaterial;

        [Tooltip("The smothing used for the fill part of the category, or null ")]
        public FsmInt FillSmoothing;

        [Tooltip("The curve used for the fill part of the category, or null ")]
        public FsmFloat Curve;

        [Tooltip("The seperation used for the fill part of the category, or null ")]
        public FsmFloat Seperation;

        public override void Reset()
        {
            CategoryName = "";
            LinePrefab = null;
            LineMaterial = null;
            LineThickness = 0.5f;
            PointPrefab = null;
            PointMaterial = null;
            PointSize = 1f;
            FillMaterial = null;
            FillSmoothing = 3;
            Curve = 0f;
            Seperation = 0f;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (CategoryName.Value == "" || CategoryName.Value == null)
                return "Category name cannot be null or empty";
            return null;
        }

        public override void OnEnter()
        {
            string check = ErrorCheck();
            if (check != null)
            {
                Debug.LogError(check);
                return;
            }
            GameObject chart = Fsm.GetOwnerDefaultTarget(ChartObject);
            var radar = chart.GetComponent<RadarChart>();
            if (radar.DataSource.HasCategory(CategoryName.Value) == false)
            {
                radar.DataSource.Set3DCategoryFill(CategoryName.Value, FillMaterial.Value, FillSmoothing.Value);
                radar.DataSource.Set3DCategoryLine(CategoryName.Value, LinePrefab.Value as PathGenerator, LineMaterial.Value,LineThickness.Value);
                radar.DataSource.Set3DCategoryPoint(CategoryName.Value, PointPrefab.Value, PointMaterial.Value, PointSize.Value);
                radar.DataSource.Set3DCategoryOrientation(CategoryName.Value, Seperation.Value, Curve.Value);
            }
            else
            {
                radar.DataSource.Add3DCategory(CategoryName.Value, LinePrefab.Value as PathGenerator, LineMaterial.Value, LineThickness.Value, PointPrefab.Value, PointMaterial.Value, PointSize.Value, FillMaterial.Value, FillSmoothing.Value, Curve.Value, Seperation.Value);
            }
            Finish();
        }
    }
}
#endif