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
    [Title("Graph Chart Category Template")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Adds or updates a category in a Graph chart from a Template.   If the category already exist , it's settings will be updated. Otherwise it will be created with the settings")]
    public class GraphCategoryFromTemplate : FsmStateAction
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
            if (checkObject.GetComponent<GraphChartBase>() == null)
                return "Object must be a graph chart";
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
            var graph = chart.GetComponent<GraphChartBase>();
            GameObject template = TemplateObject.Value;
            if(template == null || template.GetComponent<GraphChartBase>() == null || (template.GetComponent<GraphChartBase>().GetType() != graph.GetType() ))
            {
                if (graph is GraphChart)
                    template = ((GameObject)Resources.Load("Chart And Graph/DefualtGraphCategoryStyle2D"));
                else
                    template = ((GameObject)Resources.Load("Chart And Graph/DefualtGraphCategoryStyle3D")); // load default

            }
            var templateComponent = template.GetComponent<GraphChartBase>();
            var visualStyles = templateComponent.DataSource.StoreAllCategoriesinOrder();
            if(visualStyles.Length ==0)
            {
                Debug.LogError("No categories in template chart");
                return;
            }
            int index = TemplateIndex.Value;
            if (index < 0)
                index = 0;
            if (index >= visualStyles.Length)
                index = visualStyles.Length - 1;
            var style = visualStyles[index];

            if (graph.DataSource.HasCategory(CategoryName.Value) == false)
                graph.DataSource.AddCategory(CategoryName.Value, null, 0, new MaterialTiling(), null, false, null, 0);
            graph.DataSource.RestoreCategory(CategoryName.Value, style);
            Finish();
        }
    }
}
#endif