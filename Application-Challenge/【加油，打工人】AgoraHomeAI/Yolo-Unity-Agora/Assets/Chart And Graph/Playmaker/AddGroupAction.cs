#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("Add Group")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Adds a group to a bar or radar chart")]
    public class AddGroupAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new group. A chart object cannot have duplicate group names")]
        public FsmString GroupName;

        [Tooltip("If true , and a group with the same name already exist in the chart , then no error is generated")]
        public FsmBool AddOnlyIfMissing;

        public override void Reset()
        {
            GroupName = "";
            AddOnlyIfMissing = true;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<RadarChart>() == null && checkObject.GetComponent<BarChart>() == null)
                return "Object must be a either a radar chart or bar chart";
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
            GameObject obj = Fsm.GetOwnerDefaultTarget(ChartObject);
            var chart = obj.GetComponent<BarChart>();
            if (chart != null)
            {
                if(AddOnlyIfMissing.Value == false || chart.DataSource.HasGroup(GroupName.Value))
                    chart.DataSource.AddGroup(GroupName.Value);
            }
            else
            {
                RadarChart radar = obj.GetComponent<RadarChart>();
                if (radar != null)
                {
                    if (AddOnlyIfMissing.Value == false || radar.DataSource.HasGroup(GroupName.Value) == false)
                        radar.DataSource.AddGroup(GroupName.Value);
                }

            }
            Finish();
        }
    }
}
#endif