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
    [Title("Has Group Conditional")]
    [ActionCategory("Graph and Chart - Advanced")]
    [Tooltip("checks if a group is present in a chart")]
    public class CheckGroupAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the category to remove")]
        public FsmString GroupName;

        [UIHint(UIHint.Variable)]
        public FsmBool StoreResult;

        public override void Reset()
        {
            base.Reset();
            GroupName = "";
            StoreResult = false;
        }
        
        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<BarChart>() == null && checkObject.GetComponent<RadarChart>() == null)
                return "Object must be a bar chart type or a radar chart type";
            if (GroupName.Value == "" || GroupName.Value == null)
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
            var chart = ChartObject.GameObject.Value.GetComponent<AnyChart>();
            StoreResult.Value = false;
            if (chart is BarChart)
                StoreResult.Value = ((BarChart)chart).DataSource.HasGroup(GroupName.Value);
            else if (chart is RadarChart)
                StoreResult.Value = ((RadarChart)chart).DataSource.HasGroup(GroupName.Value);
            Finish();
        }
    }
}
#endif