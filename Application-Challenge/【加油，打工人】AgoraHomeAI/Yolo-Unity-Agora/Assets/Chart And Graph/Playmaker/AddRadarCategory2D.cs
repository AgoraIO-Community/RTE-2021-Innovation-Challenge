#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("2D Radar Chart Category")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("Adds or updates a category in a 2d radar chart with advanced settings. If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class AddRadarCategory2DAdvanced : FsmStateAction
    {

        [CheckForComponent(typeof(CanvasRadarChart))]
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;

        [ObjectType(typeof(ChartItemEffect))]
        [Tooltip("The prefab of the line part of the chart, or null")]
        public FsmObject LineHoverPrefab;

        [Tooltip("The material used for the line part of the category, or nuWWll ")]
        public FsmMaterial LineMaterial;

        [Tooltip("The thinkness of the 3d graph line")]
        public FsmFloat LineThickness;

        [ObjectType(typeof(ChartItemEffect))]
        [Tooltip("The prefab of the point part of the chart, or null")]
        public FsmObject PointHoverPrefab;

        [Tooltip("The material used for the point part of the category , or null ")]
        public FsmMaterial PointMaterial;

        [Tooltip("The size of the 3d graph point")]
        public FsmFloat PointSize;

        [Tooltip("The material used for the fill part of the category, or null ")]
        public FsmMaterial FillMaterial;



        public override void Reset()
        {
            CategoryName = "";
            LineHoverPrefab = null;
            LineMaterial = null;
            LineThickness = 0.5f;
            PointHoverPrefab = null;
            PointMaterial = null;
            PointSize = 1f;
            FillMaterial = null;
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
            if (radar.DataSource.HasCategory(CategoryName.Value))
            {
                radar.DataSource.SetCategoryFill(CategoryName.Value, FillMaterial.Value);
                radar.DataSource.SetCategoryLine(CategoryName.Value, LineMaterial.Value, LineThickness.Value);
                radar.DataSource.SetCategoryPoint(CategoryName.Value, PointMaterial.Value, PointSize.Value);
                radar.DataSource.SetCategoryHover(CategoryName.Value, LineHoverPrefab.Value as ChartItemEffect, PointHoverPrefab.Value as ChartItemEffect);
            }
            else
            {
                radar.DataSource.AddCategory(CategoryName.Value, null, LineMaterial.Value, LineThickness.Value, null, PointMaterial.Value, PointSize.Value, FillMaterial.Value);
                radar.DataSource.SetCategoryHover(CategoryName.Value, LineHoverPrefab.Value as ChartItemEffect, PointHoverPrefab.Value as ChartItemEffect);
            }
            Finish();
        }
    }
}
#endif