#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;


namespace HutongGames.PlayMaker.Actions
{
    [Title("Remove Category")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Clears a category from a graph chart")]
    public class ClearGraphCategoryAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the category to remove")]
        public FsmString CategoryName;


        public override void Reset()
        {
            CategoryName = "";
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<GraphChartBase>() == null)
                return "Object must be a graph chart type";
            if (CategoryName.Value == "" || CategoryName.Value == null)
                return "GroupName name cannot be null or empty";
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
            GameObject obj = Fsm.GetOwnerDefaultTarget(ChartObject);
            var chart = obj.GetComponent<GraphChartBase>();
            if (chart.DataSource.HasCategory(CategoryName.Value))
                chart.DataSource.ClearCategory(CategoryName.Value);
            Finish();
        }
    }
}
#endif