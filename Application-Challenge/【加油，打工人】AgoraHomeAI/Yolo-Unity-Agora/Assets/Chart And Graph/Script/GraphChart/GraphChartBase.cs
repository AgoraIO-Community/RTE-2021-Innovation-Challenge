#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.Events;

namespace ChartAndGraph
{
    /// <summary>
    /// the graph chart class.
    /// </summary>
    [ExecuteInEditMode]
    public abstract class GraphChartBase : ScrollableAxisChart
    {
        [SerializeField]
        [Tooltip("The height ratio of the chart")]
        protected float heightRatio = 300;

        [SerializeField]
        [Tooltip("The width ratio of the chart")]
        protected float widthRatio = 600;

        /// <summary>
        /// event arguments for a bar chart event
        /// </summary>
        public class GraphEventArgs
        { 
            
            public GraphEventArgs(int index,Vector3 position, DoubleVector2 value,float magnitude, string category,string xString,string yString)
            {

                Position = position;
                Value = value;
                Category = category;
                XString = xString;
                YString = yString;
                Index = index;
                Magnitude = magnitude;
            }
            public float Magnitude { get; private set; }
            public int Index { get; private set; }
            public string XString { get; private set; }
            public string YString { get; private set; }
            public Vector3 Position { get; private set; }
            public DoubleVector2 Value { get; private set; }
            public string Category { get; private set; }
            public string Group { get; private set; }
        }

        /// <summary>
        /// a graph chart event
        /// </summary>
        [Serializable]
        public class GraphEvent : UnityEvent<GraphEventArgs>
        {

        }

        /// <summary>
        /// occures when a point is clicked
        /// </summary>
        public GraphEvent PointClicked = new GraphEvent();
        /// <summary>
        /// occurs when a point is hovered
        /// </summary>
        public GraphEvent PointHovered = new GraphEvent();
        /// <summary>
        /// occurs when no point is hovered any longer
        /// </summary>
        public UnityEvent NonHovered = new UnityEvent();

        /// <summary>
        /// The height ratio of the chart
        /// </summary>
        [SerializeField]
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
        /// The width ratio of the chart
        /// </summary>
        public float WidthRatio
        {
            get { return widthRatio; }
            set
            {
                widthRatio = value;
                Invalidate();
            }
        }


        [SerializeField]
        private string itemFormat = "<?x>:<?y>";

        public string ItemFormat
        {
            get { return itemFormat; }
            set
            {
                itemFormat = value;
                Invalidate();
            }
        }

        protected bool mRealtimeUpdateIndex = false;
        protected Dictionary<string, int> mMinimumUpdateIndex = new Dictionary<string, int>();
        /// <summary>
        /// the graph data
        /// </summary>
        [HideInInspector]
        [SerializeField]
        protected GraphData Data = new GraphData();

        /// <summary>
        /// Holds the graph chart data. including values and categories
        /// </summary>
        public GraphData DataSource { get { return Data; } }

        protected override LegenedData LegendInfo
        {
            get
            {
                LegenedData data = new LegenedData();
                if (Data == null)
                    return data;
                foreach(var cat in ((IInternalGraphData)Data).Categories)
                {
                    LegenedData.LegenedItem item = new LegenedData.LegenedItem();
                    item.Name = cat.Name;
                    if (cat.FillMaterial != null)
                        item.Material = cat.FillMaterial;
                    else if (cat.LineMaterial != null)
                        item.Material = cat.LineMaterial;
                    else
                        item.Material = cat.PointMaterial;
                    data.AddLegenedItem(item);
                }
                return data;
            }
        }

        protected override IChartData DataLink
        {
            get
            {
                return Data;
            }
        }

        public abstract void ClearCache();

        private void HookEvents()
        {
            ((IInternalGraphData)Data).InternalDataChanged -= GraphChart_InternalDataChanged;
            ((IInternalGraphData)Data).InternalRealTimeDataChanged -= GraphChartBase_InternalRealTimeDataChanged; ;
            ((IInternalGraphData)Data).InternalViewPortionChanged -= GraphChartBase_InternalViewPortionChanged;

            ((IInternalGraphData)Data).InternalViewPortionChanged += GraphChartBase_InternalViewPortionChanged;
            ((IInternalGraphData)Data).InternalDataChanged += GraphChart_InternalDataChanged;
            ((IInternalGraphData)Data).InternalRealTimeDataChanged += GraphChartBase_InternalRealTimeDataChanged; ;
        }

        private void GraphChartBase_InternalViewPortionChanged(object sender, EventArgs e)
        {
            ViewPortionChanged();
        }

        protected abstract void ViewPortionChanged();

        private void GraphChartBase_InternalRealTimeDataChanged(int index,string category)
        {
            if (category == null) // full invalidtion
            {
                mRealtimeUpdateIndex = false;
            }
            if (mRealtimeUpdateIndex)
            {
                mRealtimeUpdateIndex = true;
                int minIndex;
                if (mMinimumUpdateIndex.TryGetValue(category, out minIndex) == false)
                    minIndex = int.MaxValue;
                if (minIndex > index)
                    minIndex = index;
                mMinimumUpdateIndex[category] = minIndex;
            }
            InvalidateRealtime();
        }

        protected void ClearRealtimeIndexdata()
        {
            mMinimumUpdateIndex.Clear();
            mRealtimeUpdateIndex = true;
        }
        public override void Invalidate()
        {
            base.Invalidate();
            mRealtimeUpdateIndex = false;   // trigger a full invalidation
            mMinimumUpdateIndex.Clear();
        }
        private void GraphChart_InternalDataChanged(object sender, EventArgs e)
        {
            Invalidate();
        }

        protected override void Start()
        { 
            base.Start();
            if (ChartCommon.IsInEditMode == false)
            {
                HookEvents();
            }
            Invalidate();
        }

        protected override void OnValidate()
        {
            base.OnValidate();
            if (ChartCommon.IsInEditMode == false)
            {
                HookEvents();
            }
            Data.RestoreDataValues();
            Invalidate();
        }

        protected override void OnLabelSettingChanged()
        {
            base.OnLabelSettingChanged();
            Invalidate();
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

        protected DoubleVector4 TransformPoint(Rect viewRect,Vector3 point ,DoubleVector2 min, DoubleVector2 range)
        {
            return ChartCommon.interpolateInRect(viewRect, new DoubleVector3((point.x - min.x) / range.x, (point.y - min.y) / range.y));
        }

        protected override void Update()
        {
            base.Update();
        }

        private void UpdateMinMax(DoubleVector3 point,ref double minX,ref double minY,ref double maxX,ref double maxY)
        {
            minX = Math.Min(minX, point.x);
            maxX = Math.Max(maxX, point.x);
            minY = Math.Min(minY, point.y);
            maxY = Math.Max(maxY, point.y);
        }

        protected override float GetScrollingRange(int axis)
        {
            float min = (float)((IInternalGraphData)Data).GetMinValue(axis, false);
            float max = (float)((IInternalGraphData)Data).GetMaxValue(axis, false);
            return max - min;
        }

        private Rect CreateUvRect(Rect completeRect,Rect lineRect)
        {
            if (completeRect.width < 0.0001f || completeRect.height < 0.0001f)
                return new Rect();
            if(float.IsInfinity(lineRect.xMax) || float.IsInfinity(lineRect.xMin) || float.IsInfinity(lineRect.yMin) || float.IsInfinity(lineRect.yMax))
                return new Rect();

            float x = (lineRect.xMin - completeRect.xMin) / completeRect.width;
            float y = (lineRect.yMin - completeRect.yMin) / completeRect.height;
              
            float w = lineRect.width / completeRect.width;
            float h = lineRect.height / completeRect.height;

            return new Rect(x, y, w, h);
        }

        protected int ClipPoints(IList<DoubleVector3> points,List<DoubleVector4> res,out Rect uv)
        {
            double minX, minY, maxX, maxY, xScroll, yScroll, xSize, ySize, xOut;
            GetScrollParams(out minX, out minY, out maxX, out maxY, out xScroll, out yScroll, out xSize, out ySize, out xOut);

            double direction = 1.0;
            if (minX > maxX)
                direction = -1.0;
            bool prevOut = false;
            bool prevIn = false;

            double maxXLocal = double.MinValue, minXLocal = double.MaxValue, maxYLocal = double.MinValue, minYLocal = double.MaxValue;
            minX = double.MaxValue;
            minY = double.MaxValue;
            maxX = double.MinValue;
            maxY = double.MinValue;
            int refrenceIndex = 0;

            for(int i=0; i<points.Count; i++)
            {
                bool pOut = prevOut;
                bool pIn = prevIn;
                prevOut = false;
                prevIn = false;
                DoubleVector3 point = points[i];
                UpdateMinMax(points[i], ref minX, ref minY, ref maxX, ref maxY);

                if (point.x* direction < xScroll* direction || point.x* direction > xOut* direction) // || point.y < yScroll || point.y > yOut)
                {
                    prevOut = true;
                    if (pIn)
                    {
                        res.Add(point.ToDoubleVector4());
                     //   uv = CreateUvRect(new Rect(minX, minY, maxX - minX, maxY - minY), new Rect(minXLocal, minYLocal, maxXLocal - minXLocal, maxYLocal - minYLocal));
                     //   return refrenceIndex;
                    }
                    if(pOut)
                    {
                        if(point.x* direction > xOut* direction && points[i-1].x* direction < xScroll* direction)
                        {
                            UpdateMinMax(points[i - 1], ref minXLocal, ref minYLocal, ref maxXLocal, ref maxYLocal);
                            UpdateMinMax(point, ref minXLocal, ref minYLocal, ref maxXLocal, ref maxYLocal);
                            res.Add(points[i - 1].ToDoubleVector4());
                            res.Add(point.ToDoubleVector4());
                        }
                    }
                }
                else
                {
                    prevIn = true;
                    if (pOut)
                    {
                        refrenceIndex = i - 1;
                        UpdateMinMax(points[i - 1], ref minXLocal, ref minYLocal, ref maxXLocal, ref maxYLocal);
                        res.Add(points[i - 1].ToDoubleVector4());
                    }
                    UpdateMinMax(point, ref minXLocal, ref minYLocal, ref maxXLocal, ref maxYLocal);
                    res.Add(point.ToDoubleVector4());
                }
                    
            }
            for(int i=0; i<res.Count; i++)
            {
                DoubleVector4 p = res[i];
                p.w = p.z;
                p.z = 0f;
                res[i] = p;
            }
            uv = CreateUvRect(new Rect((float)minX, (float)minY, (float)(maxX - minX), (float)(maxY - minY)), new Rect((float)minXLocal, (float)yScroll, (float)(maxXLocal - minXLocal), (float)(ySize)));
            return refrenceIndex;
        }

        protected void TransformPoints(IList<DoubleVector3> points, Rect viewRect, DoubleVector3 min, DoubleVector3 max)
        {
            DoubleVector3 range = max - min;
            if (Math.Abs(range.x) <= 0.0001f || Math.Abs(range.y) < 0.0001f)
                return;
            double radiusMultiplier = Math.Min(viewRect.width / Math.Abs(range.x), viewRect.height / Math.Abs(range.y));
            for (int i = 0; i < points.Count; i++)
            {
                DoubleVector3 point = points[i];
                DoubleVector4 res = ChartCommon.interpolateInRect(viewRect, new DoubleVector3((point.x - min.x) / range.x, (point.y - min.y) / range.y));
                res.z = point.z * radiusMultiplier;
                points[i] = res.ToDoubleVector3();
            }
        }

        protected void TransformPoints(IList<DoubleVector4> points,List<Vector4> output,Rect viewRect, DoubleVector3 min, DoubleVector3 max)
        {
            output.Clear();
            DoubleVector3 range = max - min;
            if (Math.Abs(range.x) <= 0.0001f || Math.Abs(range.y) < 0.0001f)
                return;
            double radiusMultiplier = Math.Min(viewRect.width / Math.Abs(range.x), viewRect.height / Math.Abs(range.y));
            for(int i=0; i<points.Count; i++)
            {
                DoubleVector4 point = points[i];
                DoubleVector4 res = ChartCommon.interpolateInRect(viewRect,new DoubleVector3((point.x - min.x) / range.x, (point.y - min.y) / range.y));
                res.z = 0.0;
                res.w = point.w* radiusMultiplier;
                output.Add(res.ToVector4());
            }
        }

        protected override void ValidateProperties()
        {
            base.ValidateProperties();
            if (heightRatio < 0f)
                heightRatio = 0f;
            if (widthRatio < 0f)
                widthRatio = 0f;
        }

        protected override bool SupportsCategoryLabels
        {
            get
            {
                return false;
            }
        }

        protected override bool SupportsGroupLables
        {
            get
            {
                return false;
            }
        }

        protected override bool SupportsItemLabels
        {
            get
            {
                return true;
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
                return widthRatio;
            }
        }


        protected override float TotalDepthLink
        {
            get
            {
                return 0.0f;
            }
        }

        protected override bool HasValues(AxisBase axis)
        {
            return true; //all axis have values in the graph chart
        }

        protected override double MaxValue(AxisBase axis)
        {
            if (axis == null)
                return 0.0;
            if (axis == mHorizontalAxis)
                return ((IInternalGraphData)Data).GetMaxValue(0, false);
            if (axis == mVerticalAxis)
                return ((IInternalGraphData)Data).GetMaxValue(1, false);
            return 0.0;
        }

        protected override double MinValue(AxisBase axis)
        {
            if (axis == null)
                return 0.0;
            if (axis == mHorizontalAxis)
                return ((IInternalGraphData)Data).GetMinValue(0, false);
            if (axis == mVerticalAxis)
                return ((IInternalGraphData)Data).GetMinValue(1, false);
            return 0.0;
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            GraphEventArgs args = userData as GraphEventArgs;
            if (PointHovered != null)
                PointHovered.Invoke(args);

        }
        StringBuilder mTmpBuilder = new StringBuilder();

        public string FormatItem(double x, double y)
        {
            DoubleVector3 p = new DoubleVector3(x, y);
            string xFormat = StringFromAxisFormat(p, mHorizontalAxis, true);
            string yFormat = StringFromAxisFormat(p, mVerticalAxis, false);

            FormatItem(mTmpBuilder, xFormat, yFormat);
            return mTmpBuilder.ToString();
        }

        public string FormatItem(string x, string y)
        {
            FormatItem(mTmpBuilder, x, y);
            return mTmpBuilder.ToString();
        }
        protected void FormatItem(StringBuilder builder, string x, string y)
        {
            builder.Length = 0;
            builder.Append(itemFormat);
            builder.Replace("<?x>", x).Replace("<?y>", y).Replace("\\n", Environment.NewLine);
        }

        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            GraphEventArgs args = userData as GraphEventArgs;
            if (PointClicked != null)
                PointClicked.Invoke(args);
        }
    }
}
