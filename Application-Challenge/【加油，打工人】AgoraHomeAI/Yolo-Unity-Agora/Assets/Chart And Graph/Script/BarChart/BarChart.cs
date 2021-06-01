#define Graph_And_Chart_PRO
using ChartAndGraph.Axis;
using ChartAndGraph.Common;
using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.Serialization;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// base class for bar charts. This class is implemented as CanvasBarChart and WorldSpaceBarChart
    /// </summary>
    [ExecuteInEditMode]
    public abstract partial class BarChart : AxisChart
    {
        private Dictionary<KeyValuePair<string, string>, string> mLabelOverrides = new Dictionary<KeyValuePair<string, string>, string>();

        public void SetLabelOverride(string category, string group, string newText)
        {
            mLabelOverrides[new KeyValuePair<string, string>(category, group)] = newText;
        }

        public void RemoveLabelOverride(string category, string group)
        {
            mLabelOverrides.Remove(new KeyValuePair<string, string>(category, group));
        }

        public void ClearLabelOverride(string category, string group)
        {
            mLabelOverrides.Clear();
        }

        /// <summary>
        /// event arguments for a bar chart event
        /// </summary>
        public class BarEventArgs
        {
            public BarEventArgs(Vector3 topPosition, double value, string category, string group)
            {
                TopPosition = topPosition;
                Value = value;
                Category = category;
                Group = group;
            }

            public Vector3 TopPosition { get; private set; }
            public double Value { get; private set; }
            public string Category { get; private set; }
            public string Group { get; private set; }
        }


        protected override IChartData DataLink
        {
            get
            {
                return Data;
            }
        }

        public enum BarType
        {
            Normal,
            Negative,
#if Graph_And_Chart_PRO
            Stacked
#endif

        }

        protected override void OnBeforeSerializeEvent()
        {
            base.OnBeforeSerializeEvent();
#if Graph_And_Chart_PRO
            if (viewType == BarType.Stacked)
            {
                negativeBars = false;
                stacked = true;
            }
            else
#endif
            if (viewType == BarType.Negative)
            {
                negativeBars = true;
                stacked = false;
            }
            else
            {
                negativeBars = false;
                stacked = false;
            }
        }

        class SwitchAnimationEntry
        {
            public Vector3? toPosition;
            public Vector3 prevPosition;
            public float time;
            public float totalTime;
            public BarObject bar;
        }

        bool mSnapBars = true;
        protected override void OnAfterDeserializeEvent()
        {
            base.OnAfterDeserializeEvent();
#if Graph_And_Chart_PRO
            if (stacked)
                viewType = BarType.Stacked;
            else
#endif
            if (negativeBars)
                viewType = BarType.Negative;
            else
                viewType = BarType.Normal;
        }


        /// <summary>
        /// </summary>
        [SerializeField]
        [Tooltip("")]
        private BarType viewType;

        /// <summary>
        /// height ratio. the width is determined by the properties of the bar chart
        /// </summary>
        public BarType ViewType
        {
            get { return viewType; }
            set
            {
                viewType = value;
#if Graph_And_Chart_PRO
                if (viewType == BarType.Stacked)
                {
                    negativeBars = false;
                    stacked = true;
                }
                else
#endif
                if (viewType == BarType.Negative)
                {
                    negativeBars = true;
                    stacked = false;
                }
                else
                {
                    negativeBars = false;
                    stacked = false;
                }
                Invalidate();
            }
        }

        /// <summary>
        /// </summary>
        [SerializeField]
        [HideInInspector]
        [Tooltip("")]
        private bool negativeBars;

        /// <summary>
        /// height ratio. the width is determined by the properties of the bar chart
        /// </summary>
        protected bool NegativeBars
        {
            get { return negativeBars; }
            set
            {
                negativeBars = value;
                Invalidate();
            }
        }

        [SerializeField]
        [HideInInspector]
        private bool stacked = false;

        /// <summary>
        /// 
        /// </summary>
        protected bool Stacked
        {
            get { return stacked; }
            set { stacked = value; Invalidate(); }
        }

        /// <summary>
        /// a bar chart event
        /// </summary>
        [Serializable]
        public class BarEvent : UnityEvent<BarEventArgs>
        {

        }
        protected override bool ShouldFitCanvas { get { return true; } }
        /// <summary>
        /// occures when a bar is clieck
        /// </summary>
        public BarEvent BarClicked = new BarEvent();
        /// <summary>
        /// occurs when a bar is hovered
        /// </summary>
        public BarEvent BarHovered = new BarEvent();

        /// <summary>
        /// occurs when no bar is hovered any longer
        /// </summary>
        public UnityEvent NonHovered = new UnityEvent();

        /// <summary>
        /// the bar data
        /// </summary>
        [HideInInspector]
        [SerializeField]
        private BarData Data = new BarData();

        /// <summary>
        /// Holds the bar chart data. including values, categories and groups.
        /// </summary>
        public BarData DataSource { get { return Data; } }

        protected abstract GameObject BarPrefabLink { get; }

        internal protected ChartOrientation Orientation = ChartOrientation.Vertical;

        /// <summary>
        /// returns the axis seperation value for this chart. the value can be either 2d or 3d based on the derived class
        /// </summary>
        protected abstract ChartOrientedSize AxisSeperationLink { get; }

        /// <summary>
        /// returns the bar seperation value for this chart. the value can be either 2d or 3d based on the derived class
        /// </summary>
        protected abstract ChartOrientedSize BarSeperationLink { get; }

        /// <summary>
        /// returns the group seperation value for this chart. the value can be either 2d or 3d based on the derived class
        /// </summary>
        protected abstract ChartOrientedSize GroupSeperationLink { get; }

        /// returns the bar size value for this chart. the value can be either 2d or 3d based on the derived class
        protected abstract ChartOrientedSize BarSizeLink { get; }

        /// <summary>
        /// the bars generated for the chart
        /// </summary>
        Dictionary<ChartItemIndex, BarObject> mBars = new Dictionary<ChartItemIndex, BarObject>();
        /// <summary>
        /// the bars generated for the chart
        /// </summary>
        Dictionary<KeyValuePair<string, string>, SwitchAnimationEntry> mSwitchEntries = new Dictionary<KeyValuePair<string, string>, SwitchAnimationEntry>();

        [SerializeField]
        [Tooltip("transition time for switch animations")]
        protected float transitionTimeBetaFeature = -1f;

        /// <summary>
        /// height ratio. the width is determined by the properties of the bar chart
        /// </summary>
        public float TransitionTimeBetaFeature
        {
            get { return transitionTimeBetaFeature; }
            set
            {
                transitionTimeBetaFeature = value;
                Invalidate();
            }
        }

        /// <summary>
        /// height ratio. the width is determined by the properties of the bar chart
        /// </summary>
        [SerializeField]
        [FormerlySerializedAs("totalHeight")]
        [Tooltip("height ratio. the width is determined by the properties of the bar chart")]
        protected float heightRatio;

        /// <summary>
        /// height ratio. the width is determined by the properties of the bar chart
        /// </summary>
        public float HeightRatio
        {
            get { return heightRatio; }
            set
            {
                heightRatio = value;
                Invalidate();
            }
        }

        /// <summary>
        /// the total width of the chart
        /// </summary>
        [SerializeField]
        [HideInInspector]
        private float totalWidth;
        /*public float TotalWidth
        {
            get { return totalWidth; }
        }*/
        /// <summary>
        /// the total depth of the chart
        /// </summary>
        [SerializeField]
        [HideInInspector]
        private float totalDepth;
        /*public float TotalDepth
        {
            get { return totalWidth; }
        }*/

        protected override float TotalDepthLink
        {
            get
            {
                return totalDepth;
            }
        }
        protected override float TotalHeightLink
        {
            get
            {
                return heightRatio;
            }
        }
        protected override float TotalWidthLink
        {
            get
            {
                return totalWidth;
            }
        }

        internal class BarObject
        {
            public string Category;
            public string Group;
            public double Value;
            public float Size;
            public Vector3 InnerPosition;
            public Vector3 SizeDirection = new Vector3(0f, 1f, 0f);
            public Vector3 InitialScale;
            public GameObject TopObject;
            public IBarGenerator Bar;
            public GameObject BarGameObject;
            public BillboardText ItemLabel;
            public BillboardText CategoryLabel;
            public BillboardText GroupLabel;
            public float Elevation;
        }


        class GroupObject
        {
            public float start;
            public float depth;
            public float totalSize;
            public float totalDepth;
            public int rowIndex;
        }

        class LabelPositionInfo
        {
            public LabelPositionInfo(ItemLabelsBase options, BarObject bar)
            {
                Options = options;
                Bar = bar;
            }
            public LabelPositionInfo(ItemLabelsBase options, GroupObject group)
            {
                Options = options;
                Group = group;
            }
            public ItemLabelsBase Options;
            public BarObject Bar;
            public GroupObject Group;
        }

        /// <summary>
        /// 
        /// </summary>
        protected override void Start()
        {
            base.Start();
            if (ChartCommon.IsInEditMode == false)
            {
                HookEvents();
            }
            Invalidate();
        }

        protected override void ValidateProperties()
        {
            base.ValidateProperties();
            if (heightRatio < 0f)
                heightRatio = 0f;
        }

        protected override void Update()
        {
            base.Update();
            if (Data != null)
                ((IInternalBarData)Data).Update();

            UpdateAnimations();
        }
        static List<KeyValuePair<string, string>> toRemove = new List<KeyValuePair<string, string>>();
        void UpdateAnimations()
        {

            toRemove.Clear();
            foreach (var pair in mSwitchEntries)
            {
                BarObject bar = pair.Value.bar;
                if (bar == null)
                    continue;
                var entry = pair.Value;
                if (entry.toPosition.HasValue == false)
                    continue;

                var toPosition = entry.toPosition.Value;

                entry.time -= Time.deltaTime;
                if (entry.time <= 0)
                {
                    toRemove.Add(pair.Key);
                    if (IsCanvas)
                        bar.TopObject.transform.GetComponent<RectTransform>().anchoredPosition = toPosition;
                    else
                        bar.TopObject.transform.localPosition = toPosition;
                }
                else
                {
                    float factor = entry.time / entry.totalTime;

                    if (IsCanvas)
                        bar.TopObject.transform.GetComponent<RectTransform>().anchoredPosition = Vector3.Lerp(toPosition, entry.prevPosition, factor);
                    else
                        bar.TopObject.transform.localPosition = Vector3.Lerp(toPosition, entry.prevPosition, factor);
                }
            }
            for (int i = 0; i < toRemove.Count; i++)
            {
                mSwitchEntries.Remove(toRemove[i]);
            }
        }
        /// <summary>
        /// used internally , do not call this method
        /// </summary>
        protected override void OnValidate()
        {
            base.OnValidate();
            if (Application.isPlaying)
            {
                HookEvents();
            }
            Invalidate();
        }

        /// <summary>
        /// hooks data source events.
        /// </summary>
        void HookEvents()
        {
            Data.ProperyUpdated -= Data_ProperyUpdated;
            Data.ProperyUpdated += Data_ProperyUpdated;
            ((IInternalBarData)Data).InternalDataSource.DataStructureChanged -= MDataSource_DataStructureChanged;
            ((IInternalBarData)Data).InternalDataSource.DataStructureChanged += MDataSource_DataStructureChanged;
            ((IInternalBarData)Data).InternalDataSource.ItemsReplaced -= InternalDataSource_ItemsReplaced;
            ((IInternalBarData)Data).InternalDataSource.ItemsReplaced += InternalDataSource_ItemsReplaced; ;

            ((IInternalBarData)Data).InternalDataSource.DataValueChanged -= MDataSource_DataValueChanged1; ;
            ((IInternalBarData)Data).InternalDataSource.DataValueChanged += MDataSource_DataValueChanged1; ;
        }

        private void InternalDataSource_ItemsReplaced(string from, int fromindex, string to, int toIndex)
        {
            Invalidate();
            if (transitionTimeBetaFeature < 0f)
                return;
            if (mSnapBars == false)
                return;
            mSwitchEntries.Clear();
            mSnapBars = true;
            foreach (var pair in mBars)
            {
                var bar = pair.Value;
                var entry = new SwitchAnimationEntry();
                if (IsCanvas)
                    entry.prevPosition = bar.TopObject.GetComponent<RectTransform>().anchoredPosition;
                else
                    entry.prevPosition = bar.TopObject.transform.localPosition;
                entry.time = transitionTimeBetaFeature;
                entry.totalTime = transitionTimeBetaFeature;
                mSwitchEntries[new KeyValuePair<string, string>(bar.Category, bar.Group)] = entry;
            }
        }

        private void Data_ProperyUpdated()
        {
            Invalidate();
        }

        private void MDataSource_DataValueChanged1(object sender, DataSource.ChartDataSourceBase.DataValueChangedEventArgs e)
        {
            if (e.MinMaxChanged == true || Stacked)
            {
                if (mBars.Count == 0)
                    Invalidate();
                else
                {
                    RefreshAllBars();
                    FixAxisLabels();
                }
            }
            else
                RefreshBar(e);
        }

        partial void StackedStage1(ref float elevation, double prevIntep);
        partial void StackedStage2(ref double prevIntep, double interp);

        /// <summary>
        /// refreshses all bars when the data source is changed
        /// </summary>
        private void RefreshAllBars()
        {
            double min = ((IInternalBarData)Data).GetMinValue();
            double max = ((IInternalBarData)Data).GetMaxValue();
            double[,] data = ((IInternalBarData)Data).InternalDataSource.getRawData();
            int rowCount = data.GetLength(0);
            int columnCount = data.GetLength(1);


            double zero = -min / (max - min);
            zero = Math.Max(0.0, Math.Min(1.0, zero)) * HeightRatio;


            for (int i = 0; i < rowCount; i++)
            {
                double prevIntep = 0.0;
                for (int j = 0; j < columnCount; j++)
                {
                    BarObject bar;

                    if (mBars.TryGetValue(new ChartItemIndex(i, j), out bar))
                    {
                        double amount = data[i, j];
                        double interp = 0f;
                        if (min != max)
                            interp = (amount - min) / (max - min);
                        interp = Math.Max(0.0, Math.Min(1.0, interp));
                        float height = (float)(interp * HeightRatio);
                        float elevation = 0f;
                        StackedStage1(ref elevation, prevIntep);
                        if (NegativeBars)
                        {
                            if (amount < 0)
                            {
                                height = (float)-(zero - height);
                            }
                            else
                                height = (float)(height - zero);
                        }
                        float ySize = (height - elevation) * bar.InitialScale.y;
                        SetBarSize(bar.BarGameObject, new Vector3(BarSizeLink.Breadth * bar.InitialScale.x, ySize, BarSizeLink.Depth * bar.InitialScale.z), (float)elevation);
                        if (bar.Bar != null)
                            bar.Bar.Generate((float)(interp - prevIntep), ySize);
                        StackedStage2(ref prevIntep, interp);
                        bar.Size = height;
                        bar.Value = amount;
                        FixBarLabels(bar);
                    }
                }
            }
            InvokeOnRedraw();
            //refresh all axis text
        }

        /// <summary>
        /// fix the bar labels after the source has changed
        /// </summary>
        /// <param name="bar"></param>
        private void FixBarLabels(BarObject bar)
        {
            if (bar.ItemLabel && mItemLabels != null)
            {
                string text = mItemLabels.TextFormat.Format(ChartAdancedSettings.Instance.FormatFractionDigits(mItemLabels.FractionDigits, bar.Value,CustomNumberFormat), bar.Category, bar.Group);
                string overrideText;
                if (mLabelOverrides.TryGetValue(new KeyValuePair<string, string>(bar.Category, bar.Group), out overrideText))
                    text = overrideText;
                ChartCommon.UpdateTextParams(bar.ItemLabel.UIText, text);

                Vector3 newPos = AlignLabel(mItemLabels, bar.InnerPosition, bar.Size);
                bar.ItemLabel.transform.localPosition = newPos;
            }
            if (bar.GroupLabel && mGroupLabels != null)
            {
                if (mGroupLabels.Alignment == GroupLabelAlignment.BarTop || mGroupLabels.Alignment == GroupLabelAlignment.BarBottom || mGroupLabels.Alignment == GroupLabelAlignment.FirstBar)
                {
                    Vector3 newPos = AlignLabel(mGroupLabels, bar.InnerPosition, bar.Size);
                    bar.GroupLabel.transform.localPosition = newPos;
                }
                else
                {
                    bar.GroupLabel.gameObject.SetActive(false);
                }
            }
            if (bar.CategoryLabel && mCategoryLabels != null)
            {
                Vector3 newPos = AlignLabel(mCategoryLabels, bar.InnerPosition, bar.Size);
                bar.CategoryLabel.transform.localPosition = newPos;
            }
        }

        /// <summary>
        /// refreshes a single bar after it's value has changed
        /// </summary>
        /// <param name="e"></param>
        private void RefreshBar(DataSource.ChartDataSourceBase.DataValueChangedEventArgs e)
        {
            BarObject bar;
            if (mBars.TryGetValue(e.ItemIndex, out bar))
            {
                double min = ((IInternalBarData)Data).GetMinValue();
                double max = ((IInternalBarData)Data).GetMaxValue();
                double zero = -min / (max - min);
                zero = Math.Max(0.0, Math.Min(1.0, zero)) * HeightRatio;
                double interp = 0f;
                if (min != max)
                    interp = (e.NewValue - min) / (max - min);
                interp = Math.Max(0.0, Math.Min(1.0, interp));
                float height = (float)(interp * HeightRatio);
                if (NegativeBars)
                {
                    if (e.NewValue < 0)
                    {
                        height = (float)-(zero - height);
                    }
                    else
                        height = (float)(height - zero);
                }
                float ySize = height * bar.InitialScale.y;
                if (bar.Bar != null)
                    bar.Bar.Generate((float)interp, ySize);
                SetBarSize(bar.BarGameObject, new Vector3(BarSizeLink.Breadth * bar.InitialScale.x, ySize, BarSizeLink.Depth * bar.InitialScale.z), 0);
                bar.Size = height;
                bar.Value = e.NewValue;
                FixBarLabels(bar);
            }
            InvokeOnRedraw();
        }

        private void MDataSource_DataStructureChanged(object sender, EventArgs e)
        {
            mSwitchEntries.Clear();
            Invalidate();
        }

        /// <summary>
        /// This method returns the worldspace point for a specified bar and value. This method is different from GetBarTrackPosition because it is not dependent on the bar value. The value is specified as a paramenter
        /// </summary>
        /// <param name="category"></param>
        /// <param name="group"></param>
        /// <param name="value"></param>
        /// <param name="allowOverflow">set this to false in order to clamp the worldspace point to the bounds of the chart. set this to true in order to ingnore the min max value of the chart , </param>
        /// <param name="point">the resulting point in worldspace coordinates</param>
        /// <returns></returns>
        public bool PointToWorldSpace(string category, string group, double value, bool allowOverflow, out Vector3 point)
        {
            point = Vector3.zero;
            ChartSparseDataSource data = ((IInternalBarData)DataSource).InternalDataSource;
            int categoryIdx, groupIdx;
            if (data.Columns.TryGetIndexByName(category, out categoryIdx) == false)
                return false;
            if (data.Rows.TryGetIndexByName(group, out groupIdx) == false)
                return false;
            BarObject obj;
            if (mBars.TryGetValue(new ChartItemIndex(groupIdx, categoryIdx), out obj) == false)
                return false;
            double min = ((IInternalBarData)Data).GetMinValue();
            double max = ((IInternalBarData)Data).GetMaxValue();
            if (min >= max)
                return false;
            double interp = value / (max - min);
            if (allowOverflow == false)
                interp = Math.Max(0, Math.Min(1, interp));
            point = obj.InnerPosition + obj.SizeDirection * (float)interp;
            return true;
        }

        /// <summary>
        /// This method returns the worldspace point of the bottom of a specified bar. 
        /// </summary>
        /// <param name="category"></param>
        /// <param name="group"></param>
        /// <param name="bottom"></param>
        /// <returns></returns>
        public bool GetBarBottomPosition(string category, string group, out Vector3 bottom)
        {
            bottom = Vector3.zero;
            ChartSparseDataSource data = ((IInternalBarData)DataSource).InternalDataSource;
            int categoryIdx, groupIdx;
            if (data.Columns.TryGetIndexByName(category, out categoryIdx) == false)
                return false;
            if (data.Rows.TryGetIndexByName(group, out groupIdx) == false)
                return false;
            BarObject obj;
            if (mBars.TryGetValue(new ChartItemIndex(groupIdx, categoryIdx), out obj) == false)
                return false;
            bottom = obj.InnerPosition;
            return true;
        }
        /// <summary>
        /// returns the world position for the specified bar value. This can be used to make objects aligned with the bars. The position is at the top of the bar and dependent on it's value
        /// </summary>
        /// <returns>true on success with track position set to the right value. false if the category or group names are wrong</returns>
        public bool GetBarTrackPosition(string category, string group, out Vector3 trackPosition)
        {
            trackPosition = Vector3.zero;
            ChartSparseDataSource data = ((IInternalBarData)DataSource).InternalDataSource;
            int categoryIdx, groupIdx;
            if (data.Columns.TryGetIndexByName(category, out categoryIdx) == false)
                return false;
            if (data.Rows.TryGetIndexByName(group, out groupIdx) == false)
                return false;
            BarObject obj;
            if (mBars.TryGetValue(new ChartItemIndex(groupIdx, categoryIdx), out obj) == false)
                return false;
            trackPosition = GetTopPosition(obj);
            return true;
        }
        /// <summary>
        /// alignes a label to the bar chart
        /// </summary>
        /// <param name="labels"></param>
        /// <param name="innerPosition"></param>
        /// <param name="size"></param>
        /// <returns></returns>
        private Vector3 AlignLabel(GroupLabels labels, Vector3 innerPosition, float size)
        {
            float alignPos = 0f;

            if (labels.Alignment == GroupLabelAlignment.BarTop)
                alignPos += size;
            return new Vector3(
                labels.Location.Breadth,
                alignPos + labels.Seperation,
                labels.Location.Depth);
        }
        /// <summary>
        /// alignes a label to the bar chart
        /// </summary>
        /// <param name="labels"></param>
        /// <param name="innerPosition"></param>
        /// <param name="size"></param>
        /// <returns></returns>
        private Vector3 AlignLabel(AlignedItemLabels labels, Vector3 innerPosition, float size)
        {
            float alignPos = 0f;

            if (labels.Alignment == ChartLabelAlignment.Top)
                alignPos += size;
            return new Vector3(
                labels.Location.Breadth,
                alignPos + labels.Seperation,
                labels.Location.Depth);
        }

        /// <summary>
        /// updates the text lables of the chart
        /// </summary>
        private void UpdateTextLables()
        {
            List<BillboardText> items = TextController.Text;
            if (items == null)
                return;
            for (int i = 0; i < items.Count; ++i)
            {
                BillboardText text = items[i];
                LabelPositionInfo inf = text.UserData as LabelPositionInfo;
                if (inf != null)
                {
                    if (inf.Group != null)
                    {
                        if (mGroupLabels != null)
                        {
                            Vector3 position = AlignGroupLabel(inf.Group);
                            text.transform.localPosition = position;
                            ChartCommon.FixBillboardText(mGroupLabels, text);
                        }
                    }
                    else if (inf.Bar != null && inf.Options is ItemLabels)
                    {
                        Vector3 position = AlignLabel((ItemLabels)inf.Options, inf.Bar.InnerPosition, inf.Bar.Size);
                        text.transform.localPosition = position;
                        ChartCommon.FixBillboardText(inf.Options, text);
                    }
                }
            }
        }

        /// <summary>
        /// Creates a single bar game object using the chart parameters and the bar prefab
        /// </summary>
        /// <param name="innerPosition">the local position of the bar in the chart</param>
        /// <returns>the new bar game object</returns>
        private GameObject CreateBar(Vector3 innerPosition, double value, float size, float elevation, float normalizedSize, string category, string group, int index, int categoryIndex)
        {
            if (BarPrefabLink == null)
            {
                GameObject dummy = new GameObject();
                dummy.AddComponent<ChartItem>();
                dummy.transform.parent = transform;
                return dummy;
            }

            GameObject topLevel = new GameObject();
            topLevel.AddComponent<ChartItem>();
            topLevel.layer = gameObject.layer;
            topLevel.transform.SetParent(transform, false);
            topLevel.transform.localScale = new Vector3(1f, 1f, 1f);
            if (IsCanvas)
            {
                var rect = topLevel.AddComponent<RectTransform>();
                rect.anchorMin = new Vector2(0f, 0f);
                rect.anchorMax = new Vector2(0f, 0f);
                rect.anchoredPosition = innerPosition;
            }
            else
                topLevel.transform.localPosition = innerPosition;

            GameObject obj = (GameObject)GameObject.Instantiate(BarPrefabLink);

            Vector3 initialScale = obj.transform.localScale;
            obj.transform.SetParent(topLevel.transform, false);
            obj.transform.localScale = new Vector3(1f, 1f, 1f);

            CharItemEffectController effect = obj.GetComponent<CharItemEffectController>();
            if (effect == null)
                effect = obj.AddComponent<CharItemEffectController>();
            effect.WorkOnParent = true;
            effect.InitialScale = false;
            BarInfo inf = obj.AddComponent<BarInfo>();
            obj.AddComponent<ChartItem>();
            topLevel.transform.localRotation = Quaternion.identity;
            obj.transform.localRotation = Quaternion.identity;
            obj.transform.localPosition = Vector3.zero;
            ChartCommon.HideObject(obj, hideHierarchy);
            IBarGenerator generator = obj.GetComponent<IBarGenerator>();
            obj.layer = gameObject.layer;   // put the bar on the same layer as this object        
            BarObject barObj = new BarObject();
            ChartCommon.HideObject(topLevel, hideHierarchy);
            barObj.Bar = generator;
            barObj.BarGameObject = obj;
            barObj.InitialScale = initialScale;
            barObj.TopObject = topLevel;
            barObj.InnerPosition = innerPosition;
            barObj.Size = size;
            barObj.Category = category;
            barObj.Group = group;
            barObj.Value = value;
            barObj.Elevation = elevation;
            inf.BarObject = barObj;

            SwitchAnimationEntry ent;
            if (mSwitchEntries.TryGetValue(new KeyValuePair<string, string>(category, group), out ent))
            {
                Vector3 toPosition = innerPosition;

                ent.toPosition = toPosition;
                ent.bar = barObj;
            }
            mBars.Add(new ChartItemIndex(index, categoryIndex), barObj);
            ChartItemEvents[] events = obj.GetComponentsInChildren<ChartItemEvents>();
            for (int i = 0; i < events.Length; ++i)
            {
                if (events[i] == null)
                    continue;
                InternalItemEvents comp = (InternalItemEvents)events[i];
                comp.Parent = this;
                comp.UserData = barObj;
            }

            ChartMaterialController[] materialController = obj.GetComponentsInChildren<ChartMaterialController>();

            for (int i = 0; i < materialController.Length; i++)
            {
                ChartMaterialController m = materialController[i];
                ChartDynamicMaterial mat = Data.GetMaterial(category);
                if (mat != null)
                {
                    m.Materials = mat;
                    m.Refresh();
                }
            }

            float ySize = 1f * size * initialScale.y;
            if (generator != null)
                generator.Generate(normalizedSize, ySize);

            SetBarSize(obj, new Vector3(BarSizeLink.Breadth * initialScale.x, ySize, BarSizeLink.Depth * initialScale.z), elevation);

            if (mItemLabels != null && mItemLabels.isActiveAndEnabled)
            {
                string toSet = mItemLabels.TextFormat.Format(ChartAdancedSettings.Instance.FormatFractionDigits(mItemLabels.FractionDigits, value, CustomNumberFormat),
                       category, group);
                string overrideText;
                if (mLabelOverrides.TryGetValue(new KeyValuePair<string, string>(category, group), out overrideText))
                    toSet = overrideText;

                Vector3 labelPos = AlignLabel(mItemLabels, innerPosition, size + elevation);
                float angle = 45f;
                if (mItemLabels.Alignment == ChartLabelAlignment.Base)
                    angle = -45f;
                BillboardText billboard = ChartCommon.CreateBillboardText(null, mItemLabels.TextPrefab, topLevel.transform, toSet, labelPos.x, labelPos.y, labelPos.z, angle, obj.transform, hideHierarchy, mItemLabels.FontSize, mItemLabels.FontSharpness);
                //                billboard.UserData = 
                //                billboard.UIText.fontSize = ItemLabels.FontSize;
                // billboard.transform.parent =;
                barObj.ItemLabel = billboard;
                TextController.AddText(billboard);
            }

            if (mGroupLabels != null && mGroupLabels.isActiveAndEnabled)
            {
                if (mGroupLabels.Alignment == GroupLabelAlignment.BarBottom || mGroupLabels.Alignment == GroupLabelAlignment.BarTop || (mGroupLabels.Alignment == GroupLabelAlignment.FirstBar && index == 0))
                {
                    Vector3 labelPos = AlignLabel(mGroupLabels, innerPosition, size + elevation);
                    string toSet = mGroupLabels.TextFormat.Format(group, category, group);
                    // float angle = 45f;
                    BillboardText billboard = ChartCommon.CreateBillboardText(null, mGroupLabels.TextPrefab, topLevel.transform, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, obj.transform, hideHierarchy, mGroupLabels.FontSize, mGroupLabels.FontSharpness);
                    barObj.GroupLabel = billboard;
                    TextController.AddText(billboard);
                }
            }
            if (mCategoryLabels != null && mCategoryLabels.isActiveAndEnabled)
            {
                if (mCategoryLabels.VisibleLabels != CategoryLabels.ChartCategoryLabelOptions.FirstOnly ||
                       index == 0)
                {
                    Vector3 labelPos = AlignLabel(mCategoryLabels, innerPosition, size + elevation);
                    string toSet = mCategoryLabels.TextFormat.Format(category, category, group);
                    float angle = 45f;
                    if (mCategoryLabels.Alignment == ChartLabelAlignment.Base)
                        angle = -45f;
                    BillboardText billboard = ChartCommon.CreateBillboardText(null, mCategoryLabels.TextPrefab, topLevel.transform, toSet, labelPos.x, labelPos.y, labelPos.z, angle, obj.transform, hideHierarchy, mCategoryLabels.FontSize, mCategoryLabels.FontSharpness);
                    barObj.CategoryLabel = billboard;
                    TextController.AddText(billboard);
                }
            }

            if (Orientation == ChartOrientation.Horizontal)
                obj.transform.localRotation = Quaternion.Euler(0f, 0, -90f);
            return obj;
        }

        private Vector3 GetTopPosition(BarObject barObj)
        {
            Vector3 local = new Vector3(
               0f,
               barObj.Size,
               0f);
            return barObj.TopObject.transform.TransformPoint(local);
        }

        private BarEventArgs BarObjToEventArgs(BarObject barObj)
        {
            return new BarEventArgs(GetTopPosition(barObj), barObj.Value, barObj.Category, barObj.Group);
        }

        private BarEventArgs UserDataToEventArgs(object userData)
        {
            BarObject barObj = userData as BarObject;
            if (barObj == null)
                return null;
            return BarObjToEventArgs(barObj);
        }

        public override bool SupportRealtimeGeneration
        {
            get
            {
                return false;
            }
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            BarEventArgs args = UserDataToEventArgs(userData);
            if (args != null)
            {
                if (BarHovered != null)
                    BarHovered.Invoke(args);
            }
        }
        protected override void OnNonHoverted()
        {
            base.OnNonHoverted();
            if (NonHovered != null)
                NonHovered.Invoke();
        }
        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            BarEventArgs args = UserDataToEventArgs(userData);
            if (args != null)
            {
                if (BarClicked != null)
                    BarClicked.Invoke(args);
            }
        }

        protected override void OnPropertyUpdated()
        {
            base.OnPropertyUpdated();
            Invalidate();
        }

        protected virtual void SetBarSize(GameObject bar, Vector3 size, float elevation)
        {
            bar.transform.localScale = size;
            Vector3 p = bar.transform.localPosition;
            p.y = elevation;
            bar.transform.localPosition = p;
        }

        /// <summary>
        /// adds a bar to the chart considering the bar orientation and position
        /// </summary>
        /// <param name="depth">the depth of the bar</param>
        /// <param name="orientedPosition">the oriented positions of the bar ( either vertical or horizontal depending on orientation)</param>
        /// <param name="orientedInterp">the oriented interpolator of the bar position. this is a value between [0,1] where orientedInterp*(width or height depending on orientation) = oriented position.  </param>
        private void AddBarToChart(double depth, double value, double prevValue, double orientedPosition, double orientedInterp, string category, string group, int index, int categoryIndex, double zero)
        {
            if (Stacked && orientedInterp < 0.0)
            {

                Debug.LogError("stacked bars should be inserted in acending order. Check out the online mannual for more info");
                return;
            }
            if (Orientation == ChartOrientation.Vertical)
            {
                double height = orientedInterp * HeightRatio;
                double y = 0.0;
                if (NegativeBars)
                {
                    y = zero;
                    if (value < 0.0)
                    {
                        y = zero;
                        height = -(zero - height);
                    }
                    else
                        height = height - zero;
                }
                CreateBar(new Vector3((float)orientedPosition, (float)y, (float)depth), value, (float)height, (float)prevValue * HeightRatio, (float)orientedInterp, category, group, index, categoryIndex);
            }
            else
            {
                double width = orientedInterp * TotalWidth;
                CreateBar(new Vector3(0f, (float)orientedPosition, (float)depth), value, (float)width, (float)prevValue * TotalWidth, (float)orientedInterp, category, group, index, categoryIndex);
            }
        }

        protected override bool HasValues(AxisBase axis)
        {
            if (axis == null)
                return false;
            if (axis == mHorizontalAxis)
                return false;
            if (axis == mVerticalAxis)
                return true;
            return false;
        }

        protected override double MinValue(AxisBase axis)
        {
            if (axis == null)
                return 0.0;
            if (axis == mHorizontalAxis)
                return 0.0;
            if (axis == mVerticalAxis)
                return ((IInternalBarData)Data).GetMinValue();
            return 0.0;
        }

        protected override double MaxValue(AxisBase axis)
        {
            if (axis == null)
                return 0.0;
            if (axis == mHorizontalAxis)
                return 0.0;
            if (axis == mVerticalAxis)
                return ((IInternalBarData)Data).GetMaxValue();
            return 0.0;
        }

        private Vector3 AlignGroupLabel(GroupObject obj)
        {
            float alignPos = 0f;
            float seperation = mGroupLabels.Seperation;
            alignPos += HeightRatio;

            float pos = obj.start;
            float posDepth = obj.depth;
            float addBreadth = mGroupLabels.Location.Breadth;
            float addDepth = mGroupLabels.Location.Depth;

            if (mGroupLabels.Alignment == GroupLabelAlignment.AlternateSides)
            {
                if (obj.rowIndex % 2 != 0)
                {
                    pos += obj.totalSize;
                    posDepth += obj.totalDepth;
                }
                else
                {
                    addBreadth = -addBreadth;
                    addDepth = -addDepth;
                }

            }
            else if (mGroupLabels.Alignment == GroupLabelAlignment.EndOfGroup)
            {
                pos += (obj.totalSize);
                posDepth += (obj.totalDepth);
            }
            else if (mGroupLabels.Alignment == GroupLabelAlignment.Center)
            {
                pos += (obj.totalSize) * 0.5f;
                posDepth += (obj.totalDepth) * 0.5f;
            }
            Vector3 add = new Vector3();
            if (IsCanvas)
                add = -new Vector3(totalWidth * CanvasFitOffset.x, TotalHeight * CanvasFitOffset.y);
            return new Vector3(pos + addBreadth, alignPos + seperation, (float)(posDepth) + addDepth) + add;
        }
        partial void StackedStage3(ref double interp, double amount, double min, double total);
        /// <summary>
        /// 
        /// </summary>
        /// <param name="mesh"></param>
        /// <param name="orientedPosition"></param>
        private void AddRowToChartMesh(double[,] data, int rowIndex, double orientedPosition, double depth)
        {
            if (((IInternalBarData)Data).InternalDataSource == null)
                return;

            int columnCount = data.GetLength(1);
            double barFullSize = BarSeperationLink.Breadth;
            double totalSize = ((columnCount - 1) * (barFullSize));
            double totalDepth = columnCount * BarSeperationLink.Depth;
            double start = orientedPosition - totalSize * 0.5f;
            double min = ((IInternalBarData)Data).GetMinValue();
            double max = ((IInternalBarData)Data).GetMaxValue();
            double total = max - min;
            string group = ((IInternalBarData)Data).InternalDataSource.Rows[rowIndex].Name;

            if (mGroupLabels != null && mGroupLabels.isActiveAndEnabled && (mGroupLabels.Alignment != GroupLabelAlignment.BarBottom && mGroupLabels.Alignment != GroupLabelAlignment.BarTop && mGroupLabels.Alignment != GroupLabelAlignment.FirstBar))
            {

                GroupObject groupObj = new GroupObject();
                groupObj.start = (float)start;
                groupObj.depth = (float)depth;
                groupObj.totalSize = (float)totalSize;
                groupObj.totalDepth = (float)totalDepth;
                groupObj.rowIndex = rowIndex;

                Vector3 position = AlignGroupLabel(groupObj);

                string toSet = mGroupLabels.TextFormat.Format(group, "", "");
                float angle = 45f;
                //                if(mGroupLabels.Alignment == GroupLabelAlignment.)
                BillboardText billboard = ChartCommon.CreateBillboardText(null, mGroupLabels.TextPrefab, transform, toSet, position.x, position.y, position.z, angle, null, hideHierarchy, mGroupLabels.FontSize, mGroupLabels.FontSharpness);
                billboard.UserData = new LabelPositionInfo(mGroupLabels, groupObj);
                TextController.AddText(billboard);
            }
            // double prevValue = 0.0;
            double zero = (-min / total);
            zero = Math.Max(0, Math.Min(1, zero)) * heightRatio;
            double prevInterp = 0.0;
            for (int i = 0; i < columnCount; i++)
            {
                string category = ((IInternalBarData)Data).InternalDataSource.Columns[i].Name;
                double rowInterp = (((float)i) / ((float)columnCount - 1));
                if (double.IsInfinity(rowInterp) || double.IsNaN(rowInterp)) // calculate limit for 0/0
                    rowInterp = 0.0;
                double pos = rowInterp * totalSize;
                double finalDepth = depth + rowInterp * totalDepth;
                pos += start;// - barFullSize * 0.5f;
                double amount = data[rowIndex, i];
                double interp = (amount - min) / total;
                StackedStage3(ref interp, amount, min, total);

                interp = Math.Max(0, Math.Min(1, interp));  // clamp to [0,1]
                AddBarToChart(finalDepth, amount, prevInterp, pos, interp - prevInterp, category, group, rowIndex, i, zero);
                StackedStage2(ref prevInterp, interp);
            }

        }

        protected override void ClearChart()
        {
            base.ClearChart();

            foreach (BarObject bar in mBars.Values)
            {
                if (bar != null)
                {
                    if (bar.Bar != null)
                    {
                        bar.Bar.Clear();
                        MonoBehaviour b = bar.Bar as MonoBehaviour;
                        if (b != null)
                            ChartCommon.SafeDestroy(b.gameObject);
                    }
                    if (bar.TopObject != null)
                        ChartCommon.SafeDestroy(bar.TopObject);
                }
            }
            mBars.Clear();
        }

        protected override void OnLabelSettingChanged()
        {
            base.OnLabelSettingChanged();
            UpdateTextLables();
        }

        protected override void OnAxisValuesChanged()
        {
            base.OnAxisValuesChanged();
            Invalidate();
        }

        protected override void OnLabelSettingsSet()
        {
            base.OnLabelSettingsSet();
            Invalidate();
        }
        protected float MessureWidth()
        {
            if (gameObject.activeInHierarchy == false)
                return 1f;
            if (((IInternalBarData)Data).InternalDataSource == null)
                return 1f;

            double[,] data = ((IInternalBarData)Data).InternalDataSource.getRawData();
            int rowCount = data.GetLength(0);
            int columnCount = data.GetLength(1);
            if (rowCount <= 0 || columnCount <= 0)
                return 1f;
            int rowLimit = rowCount - 1;
            double barGroupSeprationSize = BarSeperationLink.Breadth * (columnCount - 1);
            double barGroupSize = barGroupSeprationSize + BarSizeLink.Breadth;
            double totalSize = GroupSeperationLink.Breadth * rowLimit;
            double chartTotalSize = totalSize + AxisSeperationLink.Breadth * 2f + barGroupSize;
            return (float)chartTotalSize;
        }



        /// <summary>
        /// 
        /// </summary>
        /// <param name="mesh"></param>
        public override void InternalGenerateChart()
        {
            if (gameObject.activeInHierarchy == false)
                return;
            mSnapBars = true;
            base.InternalGenerateChart();
            ClearChart();
            if (((IInternalBarData)Data).InternalDataSource == null)
                return;

            double min = ((IInternalBarData)Data).GetMinValue();
            double max = ((IInternalBarData)Data).GetMaxValue();
            if (max <= min)
                return;

            if (IsCanvas)
            {
                var fitter = GetComponent<BarContentFitter>();
                if (fitter != null && fitter.isActiveAndEnabled)
                    fitter.Match();
            }
            double[,] data = ((IInternalBarData)Data).InternalDataSource.getRawData();
            int rowCount = data.GetLength(0);
            int columnCount = data.GetLength(1);
            if (rowCount <= 0 || columnCount <= 0)
                return;
            int rowLimit = rowCount - 1;
            double barGroupSeprationSize = BarSeperationLink.Breadth * (columnCount - 1);
            double barGroupSize = barGroupSeprationSize + BarSizeLink.Breadth;
            double totalSize = GroupSeperationLink.Breadth * rowLimit;
            double chartTotalSize = totalSize + AxisSeperationLink.Breadth * 2f + barGroupSize;
            if (Orientation == ChartOrientation.Vertical)
                totalWidth = (float)chartTotalSize;
            else
                HeightRatio = (float)chartTotalSize;
            //double orientedDimension = (Orientation == ChartOrientation.Vertical) ? TotalWidth : HeightRatio;
            double depth = AxisSeperationLink.Depth;
            double barGroupSeperationDepth = BarSeperationLink.Depth * (columnCount);
            double barGroupDepthSize = barGroupSeperationDepth + BarSizeLink.Depth;
            double groupDepth = GroupSeperationLink.Depth * rowLimit;
            totalDepth = (float)(groupDepth + AxisSeperationLink.Depth * 2f + barGroupDepthSize);
            GenerateAxis(true);
            double startPosition = AxisSeperationLink.Breadth + barGroupSize * 0.5f;// + (orientedDimension - totalSize)*0.5;
            if (rowLimit == 0)
                AddRowToChartMesh(data, 0, startPosition, depth);
            else
            {
                for (int i = 0; i < rowCount; i++)
                {
                    double orientedPosition = startPosition + ((((double)i) / (double)rowLimit) * totalSize);
                    //orientedPosition += barGroupSeprationSize * 0.5f;
                    AddRowToChartMesh(data, i, orientedPosition, depth);
                    depth += GroupSeperationLink.Depth;
                }
            }
        }

        protected override LegenedData LegendInfo
        {
            get
            {
                LegenedData legend = new LegenedData();
                if (Data == null)
                    return legend;
                foreach (var column in ((IInternalBarData)Data).InternalDataSource.Columns)
                {
                    var item = new LegenedData.LegenedItem();
                    item.Name = column.Name;
                    if (column.Material != null)
                        item.Material = column.Material.Normal;
                    else
                        item.Material = null;
                    legend.AddLegenedItem(item);
                }
                return legend;
            }
        }

        protected override bool SupportsCategoryLabels
        {
            get
            {
                return true;
            }
        }

        protected override bool SupportsGroupLables
        {
            get
            {
                return true;
            }
        }

        protected override bool SupportsItemLabels
        {
            get
            {
                return true;
            }
        }
    }
}
