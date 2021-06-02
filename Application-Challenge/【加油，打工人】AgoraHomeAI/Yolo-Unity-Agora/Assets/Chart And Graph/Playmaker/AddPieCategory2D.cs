#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("2D Pie Chart Category")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("Adds or updates a category in a 2d pie chart with advanced settings. If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class AddPieCategory2DAdvanced : FsmStateAction
    {
        
        [Tooltip("The chart object to perform the operation on")]
        [CheckForComponent(typeof(CanvasPieChart))]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;

        [Tooltip("The material used for the category , make sure to use only canvas material for canvas charts ")]
        public FsmMaterial Material;

        [Tooltip("The color of a bar on a mouse hover for this category")]
        public FsmColor HoverColor;

        [Tooltip("The color of a bar on a mouse click for this category")]
        public FsmColor ClickColor;

        [Tooltip("a value between 0 and 1. This is the size of this category relative to other categories in the pie")]
        public FsmFloat RadiusScale;
        
        public override void Reset()
        {
            CategoryName = "";
            Material = null;
            HoverColor = Color.white;
            ClickColor = Color.white;
            RadiusScale = 1f;
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
            var pie = chart.GetComponent<PieChart>();
            if (pie.DataSource.HasCategory(CategoryName.Value))
            {
                pie.DataSource.SetMaterial(CategoryName.Value, new ChartDynamicMaterial(Material.Value, HoverColor.Value, ClickColor.Value));
                pie.DataSource.SetCateogryParams(CategoryName.Value, RadiusScale.Value, 1f, 0f);
            }
            else
            {
                pie.DataSource.AddCategory(CategoryName.Value, new ChartDynamicMaterial(Material.Value, HoverColor.Value, ClickColor.Value), RadiusScale.Value, 1f, 0f);
            }
            Finish();
        }
    }
}
#endif
