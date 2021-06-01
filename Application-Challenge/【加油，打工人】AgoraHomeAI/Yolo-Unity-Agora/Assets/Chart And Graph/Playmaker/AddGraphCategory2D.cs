#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("2D Graph Chart Category")] 
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("Adds or updates a category in a 2d Graph chart.   If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class AddGraphCategory2DAdvanced : FsmStateAction
    {
        [CheckForComponent(typeof(GraphChart))]
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;


        [ObjectType(typeof(ChartItemEffect))]
        [Tooltip("The prefab of the line part of the chart, or null")]
        public FsmObject LineHoverPrefab;

        [Tooltip("The material used for the line part of the category, or null ")]
        public FsmMaterial LineMaterial;

        [Tooltip("The thinkness of the 2d graph line")]
        public FsmFloat LineThickness;

        [Tooltip("The the higher this value is , the more the texture is streched along the line. Set it to the texture pixel size for best reuslts. set it to -1 to avoid texture tiling along the line")]
        public FsmFloat TilingFactor;

        [Tooltip("The material used for the fill part of the category, or null ")]
        public FsmMaterial FillMaterial;

        [Tooltip("If true the fill materil is streched , otherwise it is clamped ")]
        public FsmBool StrechFill;

        [ObjectType(typeof(ChartItemEffect))]
        [Tooltip("The prefab of the point part of the chart, or null")]
        public FsmObject PointHoverPrefab;
        [Tooltip("The size of the 2d graph point")]
        public FsmFloat PointSize;
        [Tooltip("The material used for the point part of the category , or null ")]
        public FsmMaterial PointMaterial;

        public override void Reset()
        {
            TilingFactor = -1f;
            CategoryName = "";
            PointSize = 1f;
            LineMaterial = null;
            LineThickness = 0.2f;
            FillMaterial = null;
            StrechFill = false;
            PointMaterial = null;
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
            var graph = chart.GetComponent<GraphChart>();
            MaterialTiling m;
            if (TilingFactor.Value < 0f)
                m = new MaterialTiling();
            else
                m = new MaterialTiling(true, TilingFactor.Value);
            if (graph.DataSource.HasCategory(CategoryName.Value))
            {
                graph.DataSource.SetCategoryPoint(CategoryName.Value, PointMaterial.Value, PointSize.Value);
                graph.DataSource.SetCategoryLine(CategoryName.Value, LineMaterial.Value, LineThickness.Value, m);
                graph.DataSource.SetCategoryFill(CategoryName.Value, FillMaterial.Value, StrechFill.Value);
                graph.DataSource.Set2DCategoryPrefabs(CategoryName.Value, LineHoverPrefab.Value as ChartItemEffect, PointHoverPrefab.Value as ChartItemEffect);
            }
            else
            {
                graph.DataSource.AddCategory(CategoryName.Value, LineMaterial.Value, LineThickness.Value, m, FillMaterial.Value, StrechFill.Value, PointMaterial.Value, PointSize.Value);
                graph.DataSource.Set2DCategoryPrefabs(CategoryName.Value, LineHoverPrefab.Value as ChartItemEffect, PointHoverPrefab.Value as ChartItemEffect);
            }
            Finish();
        }
    }
}
#endif