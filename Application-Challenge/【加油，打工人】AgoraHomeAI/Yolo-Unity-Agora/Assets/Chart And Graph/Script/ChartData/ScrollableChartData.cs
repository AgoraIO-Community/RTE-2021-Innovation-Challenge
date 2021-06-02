#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public abstract class ScrollableChartData : IChartData, IMixedSeriesProxy
    {
        protected List<BaseSlider> mSliders = new List<BaseSlider>();
        protected Dictionary<string, BaseScrollableCategoryData> mData = new Dictionary<string, BaseScrollableCategoryData>();
        
        protected event EventHandler DataChanged;
        protected event EventHandler ViewPortionChanged;
        protected event Action<int,string> RealtimeDataChanged;

        protected bool mSuspendEvents = false;

        [SerializeField]
        private double automaticVerticalViewGap = 0f;

        /// <summary>
        /// call this to suspend chart redrawing while updating the data of the chart
        /// </summary>
        public void StartBatch()
        {
            mSuspendEvents = true;
        }

        /// <summary>
        /// call this after StartBatch , this will apply all the changed made between the StartBatch call to this call
        /// </summary>
        public void EndBatch()
        {
            mSuspendEvents = false;
            RaiseDataChanged();
        }



        /// set this to true to automatically detect the horizontal size of the graph chart
        /// </summary>
        public double AutomaticVerticallViewGap
        {
            get { return automaticVerticalViewGap; }
            set
            {
                automaticVerticalViewGap = value;
                RestoreDataValues();
                RaiseViewPortionChanged();
            }
        }

        /// <summary>
        /// set this to true to automatically detect the horizontal size of the graph chart
        /// </summary>
        [SerializeField]
        private bool automaticVerticallView = true;

        /// <summary>
        /// set this to true to automatically detect the horizontal size of the graph chart
        /// </summary>
        public bool AutomaticVerticallView
        {
            get { return automaticVerticallView; }
            set
            {
                automaticVerticallView = value;
                RestoreDataValues();
                RaiseViewPortionChanged();
            }
        }

        [SerializeField]
        private double automaticcHorizontaViewGap = 0f;
        /// set this to true to automatically detect the horizontal size of the graph chart
        /// </summary>
        public double AutomaticcHorizontaViewGap
        {
            get { return automaticcHorizontaViewGap; }
            set
            {
                automaticcHorizontaViewGap = value;
                RestoreDataValues();
                RaiseViewPortionChanged();
            }
        }

        /// <summary>
        /// set this to true to automatically detect the horizontal size of the graph chart
        /// </summary>
        [SerializeField]
        private bool automaticHorizontalView = true;

        /// <summary>
        /// set this to true to automatically detect the horizontal size of the graph chart
        /// </summary>
        public bool AutomaticHorizontalView
        {
            get { return automaticHorizontalView; }
            set
            {
                automaticHorizontalView = value;
                RestoreDataValues();
                RaiseViewPortionChanged();
            }
        }

        [SerializeField]
        private double horizontalViewSize = 100;

        /// <summary>
        /// Mannualy set the horizontal view size.
        /// </summary>
        public double HorizontalViewSize
        {
            get { return horizontalViewSize; }
            set
            {
                horizontalViewSize = value;
                RaiseViewPortionChanged();
            }
        }


        [SerializeField]
        private double horizontalViewOrigin = 0;

        /// <summary>
        /// Mannualy set the horizontal view origin. 
        /// </summary>
        public double HorizontalViewOrigin
        {
            get { return horizontalViewOrigin; }
            set
            {
                horizontalViewOrigin = value;
                RaiseViewPortionChanged();
            }
        }

        [SerializeField]
        private double verticalViewSize = 100;

        /// <summary>
        /// Mannualy set the horizontal view size.
        /// </summary>
        public double VerticalViewSize
        {
            get { return verticalViewSize; }
            set
            {
                verticalViewSize = value;
                RaiseViewPortionChanged();
            }
        }

        /// <summary>
        /// clears all graph data
        /// </summary>
        public void Clear()
        {
            mSliders.Clear();
            mData.Clear();
            RaiseDataChanged();
        }

        [SerializeField]
        private double verticalViewOrigin = 0;

        /// <summary>
        /// Mannualy set the horizontal view origin. 
        /// </summary>
        public double VerticalViewOrigin
        {
            get { return verticalViewOrigin; }
            set
            {
                verticalViewOrigin = value;
                RaiseViewPortionChanged();
            }
        }

        public void Update()
        {
          //  bool updated = mSliders.Count > 0;

            mSliders.RemoveAll(x =>
            {
                bool res = x.Update(); 
                if(res == false)
                {
                    RaiseRealtimeDataChanged(x.MinIndex,x.Category);
                }
                return res;
            });             
        }

        /// <summary>
        /// returns true if the data contains a category by the given name
        /// </summary>
        /// <param name="category"></param>
        /// <returns></returns>
        public bool HasCategory(string category)
        {
            return mData.ContainsKey(category);
        }

        protected void ModifyMinMax(BaseScrollableCategoryData data, DoubleVector3 point)
        {
            if (data.MaxRadius.HasValue == false || data.MaxRadius.Value < point.z)
                data.MaxRadius = point.z;
            if (data.MaxX.HasValue == false || data.MaxX.Value < point.x)
                data.MaxX = point.x;
            if (data.MinX.HasValue == false || data.MinX.Value > point.x)
                data.MinX = point.x;
            if (data.MaxY.HasValue == false || data.MaxY.Value < point.y)
                data.MaxY = point.y;
            if (data.MinY.HasValue == false || data.MinY.Value > point.y)
                data.MinY = point.y;
        }

        public virtual double GetMaxValue(int axis, bool dataValue)
        {
            if (dataValue == false)
            {
                if (axis == 0 && automaticHorizontalView == false)
                {
                    double axisAdd = horizontalViewSize;
                    if (Math.Abs(axisAdd) < 0.0001f)
                    {
                        if (axisAdd < 0)
                            axisAdd = -0.0001f;
                        else
                            axisAdd = 0.0001f;
                    }
                    return HorizontalViewOrigin + axisAdd;
                }
                if (axis == 1 && AutomaticVerticallView == false)
                {
                    double axisAdd = verticalViewSize;
                    if (Math.Abs(axisAdd) < 0.0001f)
                    {
                        if (axisAdd < 0)
                            axisAdd = -0.0001f;
                        else
                            axisAdd = 0.0001f;
                    }
                    return VerticalViewOrigin + axisAdd;
                }
            }
            double? res = null;
            double add = 0;
            foreach (BaseScrollableCategoryData cat in mData.Values)
            {
                if (cat.Enabled == false)
                    continue;
                if (cat.MaxRadius.HasValue && add < cat.MaxRadius)
                    add = cat.MaxRadius.Value;
                if (axis == 0)
                {
                    if (res.HasValue == false || (cat.MaxX.HasValue && res.Value < cat.MaxX))
                        res = cat.MaxX;
                }
                else
                {
                    if (res.HasValue == false || (cat.MaxY.HasValue && res.Value < cat.MaxY))
                        res = cat.MaxY;
                }
            }
            for(int i=0; i<mSliders.Count; i++)
            {
                BaseSlider s = mSliders[i];
                if (axis == 0)
                    res = ChartCommon.Max(res, s.Max.x);
                else
                    res = ChartCommon.Max(res, s.Max.y);
            }
            if (res.HasValue == false)
                return 10f;
            double gap = (axis == 0) ? automaticcHorizontaViewGap : automaticVerticalViewGap;
            return res.Value + add + gap;
        }
        
        public virtual double GetMinValue(int axis, bool dataValue)
        {
            if (dataValue == false)
            {
                if (axis == 0 && automaticHorizontalView == false)
                    return horizontalViewOrigin;
                if (axis == 1 && AutomaticVerticallView == false)
                    return verticalViewOrigin;
            }
            double? res = null;
            double add = 0f;

            foreach (BaseScrollableCategoryData cat in mData.Values)
            {
                if (cat.Enabled == false)
                    continue;
                if (cat.MaxRadius.HasValue && add < cat.MaxRadius)
                    add = cat.MaxRadius.Value;

                if (axis == 0)
                {
                    if (res.HasValue == false || (cat.MinX.HasValue && res.Value > cat.MinX))
                        res = cat.MinX;
                }
                else
                {
                    if (res.HasValue == false || (cat.MinY.HasValue && res.Value > cat.MinY))
                        res = cat.MinY;
                }
            }
            for (int i = 0; i < mSliders.Count; i++)
            {
                BaseSlider s = mSliders[i];
                if (axis == 0)
                    res = ChartCommon.Min(res, s.Max.x);
                else
                    res = ChartCommon.Min(res, s.Max.y);
            }           
            if (res.HasValue == false)
                return 0.0;
            double max = GetMaxValue(axis, dataValue);
            if (max == res.Value)
            {
                if (res.Value == 0.0)
                    return -10.0f;
                return 0.0;
            }
            double gap = (axis == 0) ? automaticcHorizontaViewGap : automaticVerticalViewGap;
            return res.Value - add - gap;
        }

        protected void RaiseRealtimeDataChanged(int index,string category)
        {
            if (mSuspendEvents)
                return;
           
            if (RealtimeDataChanged != null)
                RealtimeDataChanged(index, category);
        }

        protected void RaiseViewPortionChanged()
        {
            if (mSuspendEvents)
                return;
            if (ViewPortionChanged != null)
                ViewPortionChanged(this, EventArgs.Empty);
        }
        protected void RaiseDataChanged()
        {
            if (mSuspendEvents)
                return;
            if (DataChanged != null)
                DataChanged(this, EventArgs.Empty);
        }

        public void RestoreDataValues()
        {
            if (automaticHorizontalView)
                RestoreDataValues(0);
            if (AutomaticVerticallView)
                RestoreDataValues(1);
        }

        void RestoreDataValues(int axis)
        {
            if (axis == 0)
            {
                double maxH = GetMaxValue(0, true);
                double minH = GetMinValue(0, true);
                horizontalViewOrigin = minH;
                horizontalViewSize = maxH - minH;
            }
            else
            {
                double maxV = GetMaxValue(1, true);
                double minV = GetMinValue(1, true);
                verticalViewOrigin = minV;
                verticalViewSize = maxV - minV;
            }
        }

        /// <summary>
        /// clears the specified category from any data
        /// </summary>
        /// <param name="category"></param>
        protected abstract void InnerClearCategory(string category);


        /// <summary>
        /// Adds a new category based on a BaseScrollableCategoryData object
        /// </summary>
        /// <param name="category"></param>
        /// <param name="data"></param>
        /// <returns></returns>
        protected abstract bool AddCategory(string category, BaseScrollableCategoryData data);

        protected abstract void AppendDatum(string category, MixedSeriesGenericValue value);
        protected abstract void AppendDatum(string category, IList<MixedSeriesGenericValue> value);

        /// <summary>
        /// instanciates a new category object with default settings. returns nulls if not supported
        /// </summary>
        /// <returns></returns>
        public abstract BaseScrollableCategoryData GetDefaultCategory();
        public abstract void OnAfterDeserialize();
        public abstract void OnBeforeSerialize();

        bool IMixedSeriesProxy.AddCategory(string category, BaseScrollableCategoryData data)
        {
            return AddCategory(category, data);
        }

        bool IMixedSeriesProxy.HasCategory(string catgeory)
        {
            return mData.ContainsKey(catgeory);
        }

        void IMixedSeriesProxy.ClearCategory(string category)
        {
            InnerClearCategory(category);
        }

        void IMixedSeriesProxy.AppendDatum(string category, MixedSeriesGenericValue value)
        {
            AppendDatum(category, value);
        }

        void IMixedSeriesProxy.AppendDatum(string category, IList<MixedSeriesGenericValue> value)
        {
            AppendDatum(category, value);
        }
    }
}
