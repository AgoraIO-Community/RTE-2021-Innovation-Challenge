#define Graph_And_Chart_PRO
#if PLAYMAKER
using ChartAndGraph;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HutongGames.PlayMaker.Actions
{
    [Title("Append Graph Point")]
    [ActionCategory("Graph and Chart")]
    [Tooltip("Appends a point to a graph chart")]
    public class AppendGraphPointAction : FsmStateAction
    {
        [Tooltip("The chart object to perform the operation on")]
        public FsmOwnerDefault ChartObject;

        [Tooltip("The Name of the category.")]
        public FsmString CategoryName;

        [Tooltip("The time of the value change animation, in seconds")]
        public FsmFloat AnimationTime;

        [Tooltip("The time zone for dates if specified")]
        public DateTimeKind DateTimeKind;

        [Tooltip("the size of the point to add , or -1 for default size")]
        public FsmFloat PointSize;


        public FsmFloat XValueFloat;

        public FsmFloat YValueFloat;

        [Tooltip("the year part of the x value date")]
        public FsmInt XDateYear;

        [Tooltip("the month part of the x value date")]
        public FsmInt XDateMonth;

        [Tooltip("the day part of the x value date")]
        public FsmInt XDateDay;

        [Tooltip("the hour part of the x value date")]
        public FsmInt XDateHour;

        [Tooltip("the minute part of the x value date")]
        public FsmInt XDateMinute;

        [Tooltip("the second part of the x value date")]
        public FsmInt XDateSecond;

        [Tooltip("the year part of the y value date")]
        public FsmInt YDateYear;

        [Tooltip("the month part of the y value date")]
        public FsmInt YDateMonth;

        [Tooltip("the day part of the y value date")]
        public FsmInt YDateDay;

        [Tooltip("the hour part of the y value date")]
        public FsmInt YDateHour;

        [Tooltip("the minute part of the y value date")]
        public FsmInt YDateMinute;

        [Tooltip("the second part of the y value date")]
        public FsmInt YDateSecond;

        private Coroutine mAnimationWait;

       
        public bool XValueIsDate;
        public bool YValueIsDate;

        public override void Reset()
        {
            PointSize = -1;
            CategoryName = "";
            AnimationTime = 0f;
        }

        public override string ErrorCheck()
        {
            GameObject checkObject = Fsm.GetOwnerDefaultTarget(ChartObject);
            if (ChartObject == null || checkObject == null)
                return "Chart object cannot be null";
            if (checkObject.GetComponent<GraphChart>() == null)
                return "Object must be a GraphChart";
            if (CategoryName.Value == "" || CategoryName.Value == null)
                return "CategoryName name cannot be null or empty";
            return null;
        }
        private double GetValue(bool isDate, FsmFloat floatValue,FsmInt year, FsmInt month, FsmInt day ,FsmInt hour ,FsmInt min, FsmInt sec)
        {
            if(isDate)
            {
                DateTime t = new DateTime(year.Value, month.Value, day.Value, hour.Value, min.Value, sec.Value, DateTimeKind);
                return ChartDateUtility.DateToValue(t);
            }
            return floatValue.Value;
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
            var chart = obj.GetComponent<GraphChart>();

            if (chart.DataSource.HasCategory(CategoryName.Value) == false)
            {
                Debug.LogError("action error : category does not exist");
                Finish();
                return;
            }

            double xValue = GetValue(XValueIsDate, XValueFloat, XDateYear, XDateMonth, XDateDay, XDateHour, XDateMinute, XDateSecond);
            double yValue = GetValue(YValueIsDate, YValueFloat, YDateYear, YDateMonth, YDateDay, YDateHour, YDateMinute, YDateSecond);

            if (AnimationTime.Value < 0.0001f)
            {
                chart.DataSource.AddPointToCategoryRealtime(CategoryName.Value,xValue,yValue,0, PointSize.Value);
                Finish();
            }
            else
            {
                chart.DataSource.AddPointToCategoryRealtime(CategoryName.Value, xValue, yValue, AnimationTime.Value, PointSize.Value);
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