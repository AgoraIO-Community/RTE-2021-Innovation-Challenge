#define Graph_And_Chart_PRO
#define CandleChart

using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.Events;


namespace ChartAndGraph
{
    [ExecuteInEditMode]
    public abstract class CandleChart : ScrollableAxisChart
    {
        /// <summary>
        /// a candle chart event
        /// </summary>
        [Serializable]
        public class CandleEvent : UnityEvent<CandleEventArgs>
        {

        }

        [SerializeField]
        [Tooltip("The height ratio of the chart")]
        protected float heightRatio = 300;

        [SerializeField]
        [Tooltip("The width ratio of the chart")]
        protected float widthRatio = 600;


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


        /// <summary>
        /// occures when a point is clicked
        /// </summary>
        public CandleEvent CandleClicked = new CandleEvent();
        /// <summary>
        /// occurs when a point is hovered
        /// </summary>
        public CandleEvent CandleHovered = new CandleEvent();
        /// <summary>
        /// occurs when no candle is hovered any longer
        /// </summary>
        public UnityEvent NonHovered = new UnityEvent();

        public enum CandleThicknessMode
        {
            /// <summary>
            /// the candle size is detemined only by the candle duration paramenter
            /// </summary>
            Fill,
            /// <summary>
            /// the candle is of constant size , regardless of the view size
            /// </summary>
            Constant,
            /// <summary>
            /// the candle size is fixed , but proportional to the view size
            /// </summary>
            Proportional
        }

        /// <summary>
        /// format a candle value to the parameter strings
        /// </summary>
        /// <param name="candleVal"></param>
        /// <param name="fractionDigits"></param>
        /// <param name="open"></param>
        /// <param name="high"></param>
        /// <param name="low"></param>
        /// <param name="close"></param>
        public void FormatCandleValue(CandleChartData.CandleValue candleVal, int fractionDigits, out string open, out string high, out string low, out string close)
        {
            
            open = StringFromAxisFormat(new DoubleVector3(candleVal.Start,candleVal.Open,0.0), mVerticalAxis,false);
            close = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Close, 0.0), mVerticalAxis, false);
            high = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.High, 0.0), mVerticalAxis, false);
            low = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Low, 0.0), mVerticalAxis, false);
        }
        /// <summary>
        /// format a candle value to the parameter strings
        /// </summary>
        /// <param name="candleVal"></param>
        /// <param name="fractionDigits"></param>
        /// <param name="start"></param>
        /// <param name="duration"></param>
        public void FormatCandleValue(CandleChartData.CandleValue candleVal, int fractionDigits, out string start, out string duration)
        {
            start = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Open, 0.0), mHorizontalAxis, true);
            duration = StringFromAxisFormat(new DoubleVector3(candleVal.Duration, candleVal.Open, 0.0), mHorizontalAxis, true);
        }

        /// <summary>
        /// format a candle value to the parameter strings
        /// </summary>
        /// <param name="candleVal"></param>
        /// <param name="fractionDigits"></param>
        /// <param name="open"></param>
        /// <param name="high"></param>
        /// <param name="low"></param>
        /// <param name="close"></param>
        /// <param name="start"></param>
        /// <param name="duration"></param>
        public void FormatCandleValue(CandleChartData.CandleValue candleVal, int fractionDigits, out string open, out string high, out string low, out string close, out string start, out string duration)
        {
            FormatCandleValue(candleVal, fractionDigits, out open, out high, out low, out close);
            FormatCandleValue(candleVal, fractionDigits, out start, out duration);
        }

        public class CandleEventArgs
        {
            int mType;

            public CandleEventArgs(int index, int type, Rect selectionRect, Vector3 position, CandleChartData.CandleValue value, string category)
            {
                mType = type;
                Position = position;
                SelectionRect = selectionRect;
                CandleValue = value;
                Category = category;
            }

            /// <summary>
            /// true if this event is triggered for the high portion of the candle
            /// </summary>
            public bool IsHighEvent { get { return mType == 0; } }
            /// <summary>
            /// true if this event is triggered for the low portion of the candle
            /// </summary>
            public bool IsLowEvent { get { return mType == 2; } }
            /// <summary>
            /// true if this event is triggerd for the body portion of the candle (open/close)
            /// </summary>
            public bool IsBodyEvent { get { return mType == 1; } }
            /// <summary>
            /// the rect on the canvas that represents the selected object
            /// </summary>
            public Rect SelectionRect { get; private set; }
            /// <summary>
            /// mouse position
            /// </summary>
            public Vector3 Position { get; private set; }

            /// <summary>
            /// the index of the candle in the data
            /// </summary>
            public int Index { get; private set; }
            /// <summary>
            /// the value of the candle
            /// </summary>
            public CandleChartData.CandleValue CandleValue { get; private set; }
            /// <summary>
            /// the category of the candle
            /// </summary>
            public string Category { get; private set; }

        }



        [SerializeField]
        [Tooltip("Thickness mode for the candle chart")]
        protected CandleThicknessMode thicknessMode = CandleThicknessMode.Constant;


        /// <summary>
        /// Thickness mode for the candle chart
        /// </summary>
        public CandleThicknessMode ThicknessMode
        {
            get { return thicknessMode; }
            set
            {
                thicknessMode = value;
                Invalidate();
            }
        }


        protected override IChartData DataLink
        {
            get
            {
                return Data;
            }
        }

        [SerializeField]
        [Tooltip("Thickness contant for the candle chart , it's meaning depends on the thickness mode")]
        protected float thicknessConstant = 10f;

        /// <summary>
        /// Thickness contant for the candle chart , it's meaning depends on the thickness mode\
        /// Fill - multiply the duration size of the candle by a constant , should be between 0f to 1f
        /// Constant - the pixel size of the candle
        /// Proportional - constant size of the candle in seconds
        /// </summary>
        public float ThicknessConstant
        {
            get { return thicknessConstant; }
            set
            {
                thicknessConstant = value;
                Invalidate();
            }
        }

        protected override float GetScrollingRange(int axis)
        {
            float min = (float)((IInternalCandleData)Data).GetMinValue(axis, false);
            float max = (float)((IInternalCandleData)Data).GetMaxValue(axis, false);
            return max - min;
        }

        [SerializeField]
        [Tooltip("format with the following labels: <?start> , <?duration>,<?open>,<?high>,<?low>,<?close>")]
        private string itemFormat = "O:<?open>,H:<?high>,L:<?low>,C:<?close>";

        /// <summary>
        /// format with the following labels:
        /// <?start>
        /// <?duration>
        /// <?open>
        /// <?close>
        /// <?high>
        /// <?low>
        /// </summary>
        public string ItemFormat
        {
            get { return itemFormat; }
            set
            {
                itemFormat = value;
                Invalidate();
            }
        }

        [SerializeField]
        [Tooltip("Used when using hoverItem component, and the mouse is hovering over the body of the candle,format with the following labels: <?start> , <?duration>,<?open>,<?high>,<?low>,<?close>")]
        private string bodyFormat = "O:<?open>,C:<?close>";

        /// <summary>
        /// format with the following labels:
        /// <?start>
        /// <?duration>
        /// <?open>
        /// <?close>
        /// <?high>
        /// <?low>
        /// </summary>
        public string BodyFormat
        {
            get { return bodyFormat; }
            set
            {
                bodyFormat = value;
                Invalidate();
            }
        }

        [SerializeField]
        [Tooltip("Used when using hoverItem component, and the mouse is hovering over the high line of the candle,format with the following labels: <?start> , <?duration>,<?open>,<?high>,<?low>,<?close>")]
        private string highFormat = "H:<?high>";

        public string HighFormat
        {
            get { return highFormat; }
            set
            {
                highFormat = value;
                Invalidate();
            }
        }

        [SerializeField]
        [Tooltip("Used when using hoverItem component, and the mouse is hovering over the low line of the candle,format with the following labels: <?start> , <?duration>,<?open>,<?high>,<?low>,<?close>")]
        private string lowFormat = "L:<?low>";

        public string LowFormat
        {
            get { return lowFormat; }
            set
            {
                lowFormat = value;
                Invalidate();
            }
        }

        /// <summary>
        /// the candle chart data
        /// </summary>
        [HideInInspector]
        [SerializeField]
        protected CandleChartData Data = new CandleChartData();

        /// <summary>
        /// Holds the candle chart data. including values and categories
        /// </summary>
        public CandleChartData DataSource { get { return Data; } }

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

        /// <summary>
        /// hooks data source events.
        /// </summary>
        protected void HookEvents()
        {
            ((IInternalCandleData)Data).InternalDataChanged -= CandleChart_InternalDataChanged;
            ((IInternalCandleData)Data).InternalDataChanged += CandleChart_InternalDataChanged;

            ((IInternalCandleData)Data).InternalViewPortionChanged -= CandleChart_InternalViewPortionChanged;
            ((IInternalCandleData)Data).InternalViewPortionChanged += CandleChart_InternalViewPortionChanged;


            ((IInternalCandleData)Data).InternalRealTimeDataChanged -= CandleChart_InternalRealTimeDataChanged;
            ((IInternalCandleData)Data).InternalRealTimeDataChanged += CandleChart_InternalRealTimeDataChanged;
        }

        private void CandleChart_InternalViewPortionChanged(object sender, EventArgs e)
        {
            InvalidateRealtime();
        }

        private void CandleChart_InternalRealTimeDataChanged(int index,string str)
        {
            InvalidateRealtime();
        }

        private void CandleChart_InternalDataChanged(object sender, EventArgs e)
        {
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

        protected override LegenedData LegendInfo
        {
            get
            {
                LegenedData data = new LegenedData();
                if (Data == null)
                    return data;
                foreach (var cat in ((IInternalCandleData)Data).Categories)
                {
                    for (int i = 0; i < 2; i++)
                    {
                        LegenedData.LegenedItem item = new LegenedData.LegenedItem();
                        CandleChartData.CandleSettings settings = cat.UpCandle;
                        if (i == 0)
                        {
                            item.Name = cat.Name + " Increasing";
                        }
                        else
                        {
                            item.Name = cat.Name + " Decreasing";
                            settings = cat.DownCandle;
                        }

                        if (settings.Fill != null)
                            item.Material = settings.Fill;
                        else
                        {
                            if (settings.Outline != null)
                                item.Material = settings.Outline;
                            else
                                item.Material = settings.Line;
                        }
                        data.AddLegenedItem(item);
                    }
                }
                return data;
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


        protected override bool HasValues(AxisBase axis)
        {
            if (axis == mVerticalAxis || axis == mHorizontalAxis) // has both horizontal and vertical axis
                return true;
            return false;
        }

        protected override double MaxValue(AxisBase axis)
        {
            if (axis == null)
                return 0.0;
            if (axis == mHorizontalAxis)
                return ((IInternalCandleData)Data).GetMaxValue(0, false);
            if (axis == mVerticalAxis)
                return ((IInternalCandleData)Data).GetMaxValue(1, false);
            return 0.0;
        }

        protected override double MinValue(AxisBase axis)
        {
            if (axis == null)
                return 0.0;
            if (axis == mHorizontalAxis)
                return ((IInternalCandleData)Data).GetMinValue(0, false);
            if (axis == mVerticalAxis)
                return ((IInternalCandleData)Data).GetMinValue(1, false);
            return 0.0;
        }

        void Deflate(ref double start, ref double duration, double amount)
        {
            double center = start + duration * 0.5;
            duration *= amount;
            start = center - duration * 0.5;
        }
        CandleChartData.CandleValue NormalizeCandle(CandleChartData.CandleValue candle, DoubleVector3 min, DoubleVector3 range)
        {
            CandleChartData.CandleValue res = new CandleChartData.CandleValue();
            res.Open = ChartCommon.normalizeInRangeY(candle.Open, min, range);
            res.Close = ChartCommon.normalizeInRangeY(candle.Close, min, range);
            res.High = ChartCommon.normalizeInRangeY(candle.High, min, range);
            res.Low = ChartCommon.normalizeInRangeY(candle.Low, min, range);

            double duration = candle.Duration;
            double start = candle.Start;
            if (ThicknessMode == CandleThicknessMode.Fill)
                Deflate(ref start, ref duration, ThicknessConstant);
            else if (thicknessMode == CandleThicknessMode.Proportional)
                Deflate(ref start, ref duration, ThicknessConstant / duration);
            double candleEnd = start + duration;
            candleEnd = ChartCommon.normalizeInRangeX(candleEnd, min, range);

            res.Start = ChartCommon.normalizeInRangeX(start, min, range);
            res.Duration = candleEnd - res.Start;
            return res;
        }
        public CandleChartData.CandleValue InterpolateCandleInRect(CandleChartData.CandleValue candle, Rect viewRect)
        {
            CandleChartData.CandleValue res = new CandleChartData.CandleValue();
            res.Open = ChartCommon.interpolateInRectY(viewRect, candle.Open);
            res.Close = ChartCommon.interpolateInRectY(viewRect, candle.Close);
            res.High = ChartCommon.interpolateInRectY(viewRect, candle.High);
            res.Low = ChartCommon.interpolateInRectY(viewRect, candle.Low);

            if (res.High < res.Low)
            {
                double tmp = res.High;
                res.High = res.Low;
                res.Low = tmp;
                tmp = res.Open;
                res.Open = res.Close;
                res.Close = tmp;
            }
            double candleEnd = candle.Start + candle.Duration;
            candleEnd = ChartCommon.interpolateInRectX(viewRect, candleEnd);
            double start = ChartCommon.interpolateInRectX(viewRect, candle.Start);
            double duration = candleEnd - start;

            if (start > candleEnd)
            {
                double tmp = start;
                start = candleEnd;
                candleEnd = tmp;
            }
            if (ThicknessMode == CandleThicknessMode.Constant)
            {
                Deflate(ref start, ref duration, ThicknessConstant / duration);
            }

            res.Start = start;
            res.Duration = duration;

            return res;
        }

        StringBuilder mTmpBuilder = new StringBuilder();

        protected string FormatItemWithFormat(string format, string open, string close, string high, string low, string start, string duration)
        {
            FormatItemWithFormat(mTmpBuilder, format, open, close, high, low, start, duration);
            return mTmpBuilder.ToString();

        }
        public string FormatItem(string open, string close, string high, string low, string start, string duration)
        {
            return FormatItemWithFormat(itemFormat, open, close, high, low, start, duration);
        }

        public string FormatLow(string open, string close, string high, string low, string start, string duration)
        {
            return FormatItemWithFormat(lowFormat, open, close, high, low, start, duration);
        }
        public string FormatHigh(string open, string close, string high, string low, string start, string duration)
        {
            return FormatItemWithFormat(highFormat, open, close, high, low, start, duration);
        }
        public string FormatBody(string open, string close, string high, string low, string start, string duration)
        {
            return FormatItemWithFormat(bodyFormat, open, close, high, low, start, duration);
        }

        protected void FormatItem(StringBuilder builder, string open, string close, string high, string low, string start, string duration)
        {
            FormatItemWithFormat(builder, itemFormat, open, close, high, low, start, duration);
        }
        protected void FormatItemWithFormat(StringBuilder builder, string format, string open, string close, string high, string low, string start, string duration)
        {
            builder.Length = 0;
            builder.Append(format);
            builder.Replace("<?open>", open).Replace("<?close>", close).Replace("<?high>", high).Replace("<?low>", low).Replace("<?start>", start).Replace("<?duration>", duration);
        }

        protected void TransformCandles(IList<CandleChartData.CandleValue> candles, List<CandleChartData.CandleValue> output, Rect viewRect, DoubleVector3 min, DoubleVector3 max)
        {
            DoubleVector3 range = max - min;
            if (Math.Abs(range.x) <= 0.0001f || Math.Abs(range.y) < 0.0001f)
                return;
            output.Clear();
            for (int i = 0; i < candles.Count; i++)
            {
                CandleChartData.CandleValue candle = candles[i];
                candle = InterpolateCandleInRect(NormalizeCandle(candle, min, range), viewRect);
                output.Add(candle);
            }
        }

        protected int ClipCandles(IList<CandleChartData.CandleValue> candles, List<CandleChartData.CandleValue> result)
        {
            result.Clear();

            double minX, minY, maxX, maxY, xScroll, yScroll, xSize, ySize, xOut;
            GetScrollParams(out minX, out minY, out maxX, out maxY, out xScroll, out yScroll, out xSize, out ySize, out xOut);
            double direction = 1.0;
            if (minX > maxX)
                direction = -1.0;
            bool prevOut = true;
            int refrenceIndex = 0;

            for (int i = 0; i < candles.Count; i++)
            {
                CandleChartData.CandleValue candle = candles[i];
                double candleEnd = candle.Duration + candle.Start;

                if (candleEnd* direction >= xScroll* direction && candle.Start* direction <= xOut* direction) // if the candle is within range
                {
                    if (prevOut)
                    {
                        refrenceIndex = i;
                        prevOut = false;
                    }
                    result.Add(candle);
                }
            }
            return refrenceIndex;
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
            CandleEventArgs args = userData as CandleEventArgs;
            if (CandleClicked != null)
                CandleClicked.Invoke(args);
        }
        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData);
            CandleEventArgs args = userData as CandleEventArgs;
            if (CandleHovered != null)
                CandleHovered.Invoke(args);

        }

    }
}
