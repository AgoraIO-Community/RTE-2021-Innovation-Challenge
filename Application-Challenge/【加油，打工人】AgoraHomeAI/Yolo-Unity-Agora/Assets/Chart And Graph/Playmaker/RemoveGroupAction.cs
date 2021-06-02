#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("Remove Group")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Removes a group from a bar or radar chart")]
    public class RemoveGroupAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;
        [Tooltip("The Name of the new group. A chart object cannot have duplicate group names")]
        public FsmString GroupName;

        [Tooltip("If true , and a group with specified name does not exist in the chart , then no error is generated")]
        public FsmBool RemoveOnlyIfExist;

        public override void Reset()
        {
            GroupName = "";
            RemoveOnlyIfExist = true;
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
                if (RemoveOnlyIfExist.Value == false || chart.DataSource.HasGroup(GroupName.Value))
                    chart.DataSource.RemoveGroup(GroupName.Value);
            }
            else
            {
                RadarChart radar = obj.GetComponent<RadarChart>();
                if (radar != null)
                {
                    if (RemoveOnlyIfExist.Value == false || radar.DataSource.HasGroup(GroupName.Value) == false)
                        radar.DataSource.RemoveGroup(GroupName.Value);
                }

            }
            Finish();
        }
    }
}
#endif