#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("3D Graph Chart Category")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("Adds or updates a category in a 3d Graph chart.   If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class AddGraphCategory3DAdvanced : FsmStateAction
    {
        
        [Tooltip("The chart object to perform the operation on")]
        [CheckForComponent(typeof(WorldSpaceGraphChart))]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;


        [Tooltip("The depth of the category in the chart")]
        public FsmFloat Depth;

        [ObjectType(typeof(PathGenerator))]
        [Tooltip("The prefab of the line part of the chart, or null")]
        public FsmObject LinePrefab;

        [Tooltip("The material used for the line part of the category, or null ")]
        public FsmMaterial LineMaterial;
        [Tooltip("The thinkness of the 3d graph line")]
        public FsmFloat LineThickness;

        [Tooltip("The the higher this value is , the more the texture is streched along the line. Set it to the texture pixel size for best reuslts. set it to -1 to avoid texture tiling along the line")]
        public FsmFloat TilingFactor;

        [ObjectType(typeof(FillPathGenerator))]
        [Tooltip("The prefab of the fill part of the chart, or null")]
        public FsmObject FillPrefab;

        [Tooltip("The material used for the fill part of the category, or null ")]
        public FsmMaterial FillMaterial;

        [Tooltip("If true the fill materil is streched , otherwise it is clamped ")]
        public FsmBool StrechFill;

        [Tooltip("The prefab of the point part of the chart, or null")]
        public FsmGameObject PointPrefab;

        [Tooltip("The material used for the point part of the category , or null ")]
        public FsmMaterial PointMaterial;

        [Tooltip("The size of the 3d graph point")]
        public FsmFloat PointSize;

        public override void Reset()
        {
            TilingFactor = -1f;
            CategoryName = "";
            PointSize = 1f;
            Depth = 0f;
            LinePrefab = null;
            LineMaterial = null;
            LineThickness = 0.2f;
            FillPrefab = null;
            FillMaterial = null;
            StrechFill = false;
            PointPrefab = null;
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
            var graph = chart.GetComponent<WorldSpaceGraphChart>();
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

                graph.DataSource.Set3DCategoryDepth(CategoryName.Value, Depth.Value);
                graph.DataSource.Set3DCategoryPrefabs(CategoryName.Value, LinePrefab.Value as PathGenerator, FillPrefab.Value as FillPathGenerator, PointPrefab.Value);
            }
            else
            {
                graph.DataSource.AddCategory3DGraph(CategoryName.Value, LinePrefab.Value as PathGenerator, LineMaterial.Value, LineThickness.Value, m, FillPrefab.Value as FillPathGenerator, FillMaterial.Value, StrechFill.Value, PointPrefab.Value, PointMaterial.Value, PointSize.Value, Depth.Value, false, 20);
            }
            Finish();
        }
    }
}
#endif
