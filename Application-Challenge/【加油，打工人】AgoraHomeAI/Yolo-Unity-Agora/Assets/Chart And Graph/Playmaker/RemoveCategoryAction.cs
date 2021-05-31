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
    [Tooltip("Removes a category from any chart")]
    public class RemoveCategoryAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the category to remove")]
        public FsmString CategoryName;


        [Tooltip("If true , and a category with specified name does not exist in the chart , then no error is generated")]
        public FsmBool RemoveOnlyIfExist;
        public override void Reset()
        {
            CategoryName = "";
            RemoveOnlyIfExist = true;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<AnyChart>() == null )
                return "Object must be a chart type";
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
            var chart = obj.GetComponent<AnyChart>();
            if(chart is BarChart)
            {
                if(RemoveOnlyIfExist.Value == false || ((BarChart)chart).DataSource.HasCategory(CategoryName.Value))
                    ((BarChart)chart).DataSource.RemoveCategory(CategoryName.Value);
            }
            else if(chart is GraphChartBase)
            {
                if (RemoveOnlyIfExist.Value == false || ((GraphChartBase)chart).DataSource.HasCategory(CategoryName.Value))
                    ((GraphChartBase)chart).DataSource.RemoveCategory(CategoryName.Value);
            }
            else if( chart is PieChart)
            {
                if (RemoveOnlyIfExist.Value == false || ((PieChart)chart).DataSource. HasCategory(CategoryName.Value))
                    ((PieChart)chart).DataSource.RemoveCategory(CategoryName.Value);
            }
            else if( chart is RadarChart)
            {
                if (RemoveOnlyIfExist.Value == false || ((RadarChart)chart).DataSource.HasCategory(CategoryName.Value))
                    ((RadarChart)chart).DataSource.RemoveCategory(CategoryName.Value);
            }
            Finish();
        }
    }
}
#endif