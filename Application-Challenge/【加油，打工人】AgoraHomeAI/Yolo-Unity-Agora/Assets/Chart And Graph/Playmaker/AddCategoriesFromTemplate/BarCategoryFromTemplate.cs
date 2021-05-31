#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace HutongGames.PlayMaker.Actions
{
    [Title("Bar Chart Category Template")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Adds or updates a category in a Bar chart from a Template.   If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class BarCategoryFromTemplate : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The template object for the chart. Or null for defualt template")]
        public FsmGameObject TemplateObject;

        [Tooltip("The index of the template category")]
        public FsmInt TemplateIndex;

        [Tooltip("The Name of the new category. A chart object cannot have duplicate category names")]
        public FsmString CategoryName;

        public override void Reset()
        {
            CategoryName = "";
            TemplateObject = null;
            TemplateIndex = 0;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<BarChart>() == null)
                return "Object must be a bar chart";
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
            var bar = chart.GetComponent<BarChart>();
            GameObject template = TemplateObject.Value;
            if (template == null || template.GetComponent<BarChart>() == null || (template.GetComponent<BarChart>().GetType() != bar.GetType()))
            {
                if (bar is CanvasBarChart)
                    template = ((GameObject)Resources.Load("Chart And Graph/DefualtBarCategoryStyle2D"));
                else
                    template = ((GameObject)Resources.Load("Chart And Graph/DefualtBarCategoryStyle3D")); // load default

            }

            var templateComponent = template.GetComponent<BarChart>();
            if (templateComponent.DataSource.TotalCategories == 0)
            {
                Debug.LogError("No categories in template chart");
                return;
            }

            int index = TemplateIndex.Value;
            if (index < 0)
                index = 0;
            if (index >= templateComponent.DataSource.TotalCategories)
                index = templateComponent.DataSource.TotalCategories - 1;
            string catName = templateComponent.DataSource.GetCategoryName(index);
            var material = templateComponent.DataSource.GetMaterial(catName);
            if (bar.DataSource.HasCategory(CategoryName.Value) == false)
                bar.DataSource.AddCategory(CategoryName.Value, material);
            else
                bar.DataSource.SetMaterial(CategoryName.Value, material);
            Finish();
        }
    }
}
#endif