#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("Set Value - Pie Chart")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Sets the value of a category and group")]
    public class SetValuePieAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the category to change ")]
        public FsmString CategoryName;

        [Tooltip("The Value to set")]
        public FsmFloat Value;

        [Tooltip("The time of the value change animation, in seconds")]
        public FsmFloat AnimationTime;

        private Coroutine mAnimationWait;

        public override void Reset()
        {
            CategoryName = "";
            Value = 1f;
            AnimationTime = 0f;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<PieChart>() == null)
                return "Object must be a either a pie chart";
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
            var chart = obj.GetComponent<PieChart>();
            if (chart.DataSource.HasCategory(CategoryName.Value) == false)
            {
                Debug.LogError("action error : category does not exist");
                Finish();
                return;
            }

            if (AnimationTime.Value < 0.0001f)
            {
                chart.DataSource.SetValue(CategoryName.Value, Value.Value);
                Finish();
            }
            else
            {
                chart.DataSource.SlideValue(CategoryName.Value, Value.Value, AnimationTime.Value);
                mAnimationWait = StartCoroutine(WaitForAnimation(AnimationTime.Value));
            }
        }

        private IEnumerator WaitForAnimation(float time)
        {
            yield return new WaitForSeconds(time);
            Finish();
            mAnimationWait = null;
        }

        public override void OnExit()
        {
            base.OnExit();
            if (mAnimationWait != null)
                StopCoroutine(mAnimationWait);

        }
    }
}
#endif