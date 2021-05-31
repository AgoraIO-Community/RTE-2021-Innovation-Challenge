#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("Set Value - Radar Chart")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Sets the value of a category and group")]
    public class SetValueRadarAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the group")]
        public FsmString CategoryName;

        [Tooltip("The Name of the new category.")]
        public FsmString GroupName;

        [Tooltip("The Value to set")]
        public FsmFloat Value;

        public override void Reset()
        {
            GroupName = "";
            CategoryName = "";
            Value = 1f;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<RadarChart>() == null)
                return "Object must be a either a radar";
            if (GroupName.Value == "" || GroupName.Value == null)
                return "GroupName name cannot be null or empty";
            if (CategoryName.Value == "" || CategoryName.Value == null)
                return "CategoryName name cannot be null or empty";
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
            var chart = obj.GetComponent<RadarChart>();
            chart.DataSource.SetValue(CategoryName.Value, GroupName.Value, Value.Value);
            Finish();
        }
    }
}
#endif