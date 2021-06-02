#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("Bar Category")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("Adds or updates a category in a bar chart. If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class AddBarCategoryAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;
        [Tooltip("The material used for the category , make sure to use only canvas material for canvas charts ")]
        public FsmMaterial Material;
        [Tooltip("The color of a bar on a mouse hover for this category")]
        public FsmColor HoverColor;
        [Tooltip("The color of a bar on a mouse click for this category")]
        public FsmColor ClickColor;

        public override void Reset()
        {
            CategoryName = "";
            Material = null;
            HoverColor = Color.white;
            ClickColor = Color.white;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<BarChart>() == null)
                return "Object must be a either a CanvasBarChart chart or a WorldSpaceBarChart chart";

            if (CategoryName.Value == "" || CategoryName.Value == null)
                return "Category name cannot be null or empty";
            if (Material.Value == null)
                return "Material cannot be null";
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
            var bar = chart.GetComponent<BarChart>();
            if(bar.DataSource.HasCategory(CategoryName.Value))
                bar.DataSource.SetMaterial(CategoryName.Value, new ChartDynamicMaterial(Material.Value, HoverColor.Value, ClickColor.Value));
            else
                bar.DataSource.AddCategory(CategoryName.Value, new ChartDynamicMaterial(Material.Value, HoverColor.Value, ClickColor.Value));
            Finish();
        }
    }
}
#endif