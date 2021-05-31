#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    class ChartItemTextBlend : ChartItemLerpEffect
    {

        Text mText;
        Shadow[] mShadows;
        Dictionary<UnityEngine.Object, float> mInitialValues = new Dictionary<UnityEngine.Object, float>();
        CanvasRenderer mRenderer = null;
        protected override void Start()
        {
            base.Start();
            mText = GetComponent<Text>();
            mShadows = GetComponents<Shadow>();
            foreach(Shadow s in mShadows)
                mInitialValues.Add(s, s.effectColor.a);
            ApplyLerp(0f);

        }
        internal override Quaternion Rotation
        {
            get
            {
                return Quaternion.identity;
            }
        }

        internal override Vector3 ScaleMultiplier
        {
            get
            {
                return new Vector3(1f, 1f, 1f);
            }
        }

        internal override Vector3 Translation
        {
            get
            {
                return Vector3.zero;
            }
        }

        protected override float GetStartValue()
        {
            if (mText != null)
                return mText.color.a;
            return 0f;
        }

        CanvasRenderer EnsureRenderer()
        {
            if (mRenderer == null)
                mRenderer = GetComponent<CanvasRenderer>();
            return mRenderer;
        }

        protected override void ApplyLerp(float value)
        {
            for (int i = 0; i < mShadows.Length; i++)
            {
                Shadow s = mShadows[i];
                float inital;
                if (mInitialValues.TryGetValue(s, out inital) == false)
                    continue;
                Color c = s.effectColor;
                c.a = Mathf.Lerp(0f, inital, value);
                s.effectColor = c;
            }
            if (mText != null)
            {
                Color c = mText.color;
                c.a = Mathf.Clamp(value,0f,1f);
                mText.color = c;
                CanvasRenderer rend = EnsureRenderer();
                if (rend != null)
                {
                    if (value <= 0f)
                    {
                        if (rend.cull == false)
                            rend.cull = true;
                    }
                    else
                    {
                        if (rend.cull == true)
                            rend.cull = false;
                    }
                }
            }
        }
    }
}
