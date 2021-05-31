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
    [Title("Has Category Conditional")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("checks if a category is present in a chart")]
    public class CheckCategoryAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the category to remove")]
        public FsmString CategoryName;

        [UIHint(UIHint.Variable)]
        public FsmBool StoreResult;

        public override void Reset()
        {
            base.Reset();
            CategoryName = "";
            StoreResult = false;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<AnyChart>() == null)
                return "Object must be a chart type";
            if (CategoryName.Value == "" || CategoryName.Value == null)
                return "CategoryName name cannot be null or empty";
            return null;
        }

        public override void OnEnter()
        {
            string check = ErrorCheck();
            if(check != null)
            {
                Debug.LogError(check);
                return;
            }
            var chart = ChartObject.GameObject.Value.GetComponent<AnyChart>();
            StoreResult.Value = false;
            if (chart is BarChart)
                StoreResult.Value = ((BarChart)chart).DataSource.HasCategory(CategoryName.Value);
            else if (chart is GraphChartBase)
            {
                StoreResult.Value = ((GraphChartBase)chart).DataSource.HasCategory(CategoryName.Value);
            }
            else if(chart is RadarChart)
            {
                StoreResult.Value = ((RadarChart)chart).DataSource.HasCategory(CategoryName.Value);
            }
            else if (chart is PieChart)
            {
                StoreResult.Value = ((PieChart)chart).DataSource.HasCategory(CategoryName.Value);
            }
            Finish();
        }
    }
}
#endif