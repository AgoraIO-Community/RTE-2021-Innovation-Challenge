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
    [Title("Pie Chart Category Template")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Adds or updates a category in a Pie chart from a Template.   If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public  class PieCategoryFromTemplate : FsmStateAction
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
            if (checkObject.GetComponent<PieChart>() == null)
                return "Object must be a pie chart";
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
            GameObject template = TemplateObject.Value;
            if (template == null || template.GetComponent<PieChart>() == null || (template.GetComponent<PieChart>().GetType() != pie.GetType()))
            {
                if (pie is CanvasPieChart)
                    template = ((GameObject)Resources.Load("Chart And Graph/DefualtPieCategoryStyle2D"));
                else
                    template = ((GameObject)Resources.Load("Chart And Graph/DefualtPieCategoryStyle3D")); // load default

            }

            var templateComponent = template.GetComponent<PieChart>();
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
            var style = templateComponent.DataSource.StoreCategory(catName);
            if (pie.DataSource.HasCategory(CategoryName.Value) == false)
                pie.DataSource.AddCategory(CategoryName.Value,(Material)null);
            pie.DataSource.RestoreCategory(CategoryName.Value, style);
            Finish();
        }
    }
}
#endif