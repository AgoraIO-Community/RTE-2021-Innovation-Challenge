#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public abstract class ChartItemLerpEffect : ChartItemEffect
    {
        const int NoOp = 0;
        const int GrowOp = 1;
        const int ShrinkOp = 2;
        const int GrowShrinkOp = 3;
        /// <summary>
        /// scales the time used in the easing curves
        /// </summary>
        public float TimeScale = 1f;
        /// <summary>
        /// easing function for the grow effect. when the curve is at 0 there will be no change , when the curve is at 1 the change will be equal to the GrowMultiplier property
        /// </summary>
        public AnimationCurve GrowEaseFunction = AnimationCurve.EaseInOut(0f, 0f, 1f, 1f);
        /// <summary>
        /// easing function for the shrink effect. when the curve is at 0 there will be no change , when the curve is at 1 the change will be equal to the GrowMultiplier property
        /// </summary>
        public AnimationCurve ShrinkEaseFunction = AnimationCurve.EaseInOut(1f, 1f, 0f, 0f);


        float mStartTime = 0f;
        float mStartValue = 0f;
        int Operation = NoOp;
        bool mDeactivateOnEnd = false;

        protected override void Start()
        {
            base.Start();
            enabled = false;
        }
        /// <summary>
        /// equivalent to calling Grow and Shrink one after the other
        /// </summary>
        public void GrowAndShrink()
        {
            mStartTime = Time.time;
            mStartValue = Mathf.Clamp(GetStartValue(), 0f, 1f);
            Operation = GrowShrinkOp;
            enabled = true;
        }
        public bool CheckAnimationEnded(float time, AnimationCurve curve)
        {
            if (curve.length == 0)
                return true;
            bool ended = time > curve.keys[curve.length - 1].time;
            if (ended)
            {
                if (mDeactivateOnEnd)
                {
                    RaiseDeactivated();
                    enabled = false;
//                    gameObject.SetActive(false);
                    mDeactivateOnEnd = false;
                }
            }
            return ended;
        }

        private void FixEaseFunction(AnimationCurve curve)
        {
            curve.postWrapMode = WrapMode.Once;
            curve.preWrapMode = WrapMode.Once;
        }

        protected abstract void ApplyLerp(float value);
        protected abstract float GetStartValue();


        void Update()
        {
            float opTime = Time.time - mStartTime;
            opTime *= TimeScale;
            float val;
            switch (Operation)
            {
                case GrowOp:
                    FixEaseFunction(GrowEaseFunction);
                    val = GrowEaseFunction.Evaluate(opTime);
                    val = Mathf.Lerp(mStartValue, 1f, val);
                    ApplyLerp(val);
                    if (CheckAnimationEnded(opTime, GrowEaseFunction))
                    {
                        Operation = NoOp;
                        ApplyLerp(1f);
                        enabled = false;
                    }
                    break;
                case ShrinkOp:
                    FixEaseFunction(ShrinkEaseFunction);
                    val = ShrinkEaseFunction.Evaluate(opTime);
                    val = Mathf.Lerp(mStartValue, 0f, val);
                    ApplyLerp(val);
                    if (CheckAnimationEnded(opTime, ShrinkEaseFunction))
                    {
                        Operation = NoOp;
                        ApplyLerp(0f);
                        enabled = false;
                    }
                    break;
                case GrowShrinkOp:
                    FixEaseFunction(GrowEaseFunction);
                    val = GrowEaseFunction.Evaluate(opTime);
                    val = Mathf.Lerp(mStartValue, 1f, val);
                    ApplyLerp(val);
                    if (CheckAnimationEnded(opTime, GrowEaseFunction))
                    {
                        ApplyLerp(1f);
                        Shrink();
                    }
                    break;
            }
        }

        public override void TriggerOut(bool deactivateOnEnd)
        {
            mDeactivateOnEnd = deactivateOnEnd;
            Shrink();
        }

        public override void TriggerIn(bool deactivateOnEnd)
        {
            mDeactivateOnEnd = deactivateOnEnd;
            Grow();
        }

        /// <summary>
        /// Grows the size of the object
        /// </summary>
        public void Grow()
        {
            mStartTime = Time.time;
            mStartValue = Mathf.Clamp(GetStartValue(), 0f, 1f);
            Operation = GrowOp;
            enabled = true;
        }

        /// <summary>
        /// Shrinks the object back to the original size
        /// </summary>
        public void Shrink()
        {
            mStartTime = Time.time;
            mStartValue = Mathf.Clamp(GetStartValue(), 0f, 1f);
            Operation = ShrinkOp;
            enabled = true;

        }

    }
}
