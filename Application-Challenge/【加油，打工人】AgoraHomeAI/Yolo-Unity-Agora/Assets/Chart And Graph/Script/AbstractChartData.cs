#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// base class for some chart datasources
    /// </summary>
    [Serializable]
    public abstract class AbstractChartData
    {
        protected class Slider
        {
            public string category;
            public string group;
            public double from;
            public double to;
            public float startTime;
            public float totalTime;
            public float timeScale = 1f;
            public AnimationCurve curve;

            public bool UpdateSlider(AbstractChartData data)
            {
                float time = Time.time;
                float elasped = time - startTime;
                elasped *= timeScale;
                if (elasped > totalTime)
                {
                    data.SetValueInternal(category, group, to);
                    return true;
                }
                float factor = elasped / totalTime;
                if (curve != null)
                    factor = curve.Evaluate(factor);
                double newValue = from * (1.0 - factor) + to * factor;
                data.SetValueInternal(category, group, newValue);
                return false;
            }
        }

        protected List<Slider> mSliders = new List<Slider>();

        protected void RemoveSliderForGroup(string group)
        {
            mSliders.RemoveAll(x => { return x.group == group; });
        }

        protected void RemoveSliderForCategory(string category)
        {
            mSliders.RemoveAll(x => { return x.category == category; });
        }

        protected void RemoveSlider(string category,string group)
        {
            mSliders.RemoveAll(x=> { return x.category == category && x.group == group; });
        }

        bool DoSlider(Slider s)
        {
            return s.UpdateSlider(this);
        }

        protected void UpdateSliders()
        {
            mSliders.RemoveAll(DoSlider);
        }

        protected abstract void SetValueInternal(string column, string row, double value);
    }
}
