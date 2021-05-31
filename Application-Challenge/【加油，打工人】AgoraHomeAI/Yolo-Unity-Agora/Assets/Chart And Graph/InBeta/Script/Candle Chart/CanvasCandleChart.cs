#define Graph_And_Chart_PRO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    class CanvasCandleChart : CandleChart
    {
        List<CandleChartData.CandleValue> mClipped = new List<CandleChartData.CandleValue>();
        List<CandleChartData.CandleValue> mTransformed = new List<CandleChartData.CandleValue>();
        List<CandleChartData.CandleValue> mCurrentSeries = new List<CandleChartData.CandleValue>();
        int mTmpCurrentIndex = 0;
        List<int> mUpToIndex = new List<int>();
        List<int> mDownToIndex = new List<int>();
        Dictionary<string, CategoryObject> mCategoryObjects = new Dictionary<string, CategoryObject>();

        StringBuilder mRealtimeStringBuilder = new StringBuilder();
        HashSet<string> mOccupiedCateogies = new HashSet<string>();
        List<int> mTmpToRemove = new List<int>();
        private bool SupressRealtimeGeneration = false;



        [SerializeField]
        private bool fitToContainer = false;
        public bool FitToContainer
        {
            get { return fitToContainer; }
            set
            {
                fitToContainer = value;
                OnPropertyUpdated();
            }
        }

        [SerializeField]
        private ChartMagin fitMargin;
        public ChartMagin FitMargin
        {
            get { return fitMargin; }
            set
            {
                fitMargin = value;
                OnPropertyUpdated();
            }
        }

        protected override float TotalDepthLink
        {
            get
            {
                return 0f;
            }
        }

        public override bool SupportRealtimeGeneration
        {
            get
            {
                return true;
            }
        }

        public override bool IsCanvas
        {
            get
            {
                return true;
            }
        }

        class CategoryObject
        {
            public CanvasChartMesh mItemLabels;
            public CanvasCandle mUp;
            public CanvasCandle mDown;
            public Dictionary<int, string> mCahced = new Dictionary<int, string>();
            public HashSet<CanvasCandle> mOccupiedCandles = new HashSet<CanvasCandle>();
            private CanvasCandleChart mParent;
            /// <summary>
            /// The selected index is hovered about the specified point
            /// </summary>
            public event EventHandlingGraphic.GraphicEvent Hover;
            /// <summary>
            /// The selected index is clicked about the specified point
            /// </summary>
            public event EventHandlingGraphic.GraphicEvent Click;
            /// <summary>
            /// The currently hovered and selected objects are no longer selected or hovered.
            /// </summary>
            public event Action Leave;

            public CategoryObject(CanvasCandleChart parent)
            {
                mParent = parent;
            }
            public void HookEvents()
            {
                HookEvents(mUp, true);
                HookEvents(mDown, false);
            }

            void HookEvents(CanvasCandle candle, bool isUp)
            {
                if (candle == null)
                    return;
                candle.Leave += () => mLeave(candle);
                candle.Hover += (index, type, data, position) => { mHover(candle, mParent.MapIndex(index, isUp), type, data, position); };
                candle.Click += (index, type, data, position) => { mClick(candle, mParent.MapIndex(index, isUp), type, data, position); };
            }

            private void mClick(CanvasCandle candle, int index, int type, object data, Vector2 position)
            {
                mOccupiedCandles.Add(candle);
                if (Click != null)
                    Click(index, type, data, position);

            }

            private void mHover(CanvasCandle candle, int index, int type, object data, Vector2 position)
            {
                mOccupiedCandles.Add(candle);
                if (Hover != null)
                    Hover(index, type, data, position);
            }

            private void mLeave(CanvasCandle candle)
            {
                if (mOccupiedCandles.Remove(candle))
                {
                    if (mOccupiedCandles.Count == 0)
                    {
                        if (Leave != null)
                            Leave();
                    }
                }
            }
        }
        protected override bool ShouldFitCanvas { get { return true; } }
        protected override FitType FitAspectCanvas
        {
            get
            {
                return FitType.Aspect;
            }
        }
        private CanvasCandle CreateDataObject(CandleChartData.CategoryData data, GameObject rectMask)
        {
            GameObject obj = new GameObject("Candles", typeof(RectTransform));
            ChartCommon.HideObject(obj, hideHierarchy);
            obj.AddComponent<ChartItem>();
            RectTransform t = obj.GetComponent<RectTransform>();
            obj.AddComponent<CanvasRenderer>();
            CanvasCandle candles = obj.AddComponent<CanvasCandle>();
            t.SetParent(rectMask.transform, false);
            t.localScale = new Vector3(1f, 1f, 1f);
            t.anchorMin = new Vector2(0f, 0f);
            t.anchorMax = new Vector2(1f, 1f);
            t.sizeDelta = new Vector2(0f, 0f);
            t.anchorMin = new Vector2(0f, 0f);
            t.anchorMax = new Vector2(0f, 0f);

            t.anchoredPosition = Vector3.zero;
            return candles;
        }

        protected override double GetCategoryDepth(string category)
        {
            return 0.0;
        }

        public override void GenerateRealtime()
        {
            if (SupressRealtimeGeneration)
                return;

            base.GenerateRealtime();

            double minX = ((IInternalCandleData)Data).GetMinValue(0, false);
            double minY = ((IInternalCandleData)Data).GetMinValue(1, false);
            double maxX = ((IInternalCandleData)Data).GetMaxValue(0, false);
            double maxY = ((IInternalCandleData)Data).GetMaxValue(1, false);

            double xScroll = GetScrollOffset(0);
            double yScroll = GetScrollOffset(1);
            double xSize = maxX - minX;
            double ySize = maxY - minY;
            double xOut = minX + xScroll + xSize;
            double yOut = minY + yScroll + ySize;

            DoubleVector3 min = new DoubleVector3(xScroll + minX, yScroll + minY);
            DoubleVector3 max = new DoubleVector3(xOut, yOut);

            Rect viewRect = new Rect(0f, 0f, widthRatio, heightRatio);

            ClearBillboardCategories();
            bool edit = false;

            foreach (CandleChartData.CategoryData data in ((IInternalCandleData)Data).Categories)
            {
                CategoryObject obj = null;

                if (mCategoryObjects.TryGetValue(data.Name, out obj) == false)
                    continue;

                mClipped.Clear();
                mTransformed.Clear();
                int refrenceIndex = ClipCandles(data.Data, mClipped);

                TransformCandles(mClipped, mTransformed, viewRect, min, max);

                if (data.Data.Count == 0 && ChartCommon.IsInEditMode)
                {
                    edit = true;
                }

                mTmpToRemove.Clear();
                int range = refrenceIndex + mClipped.Count;
                foreach (int key in obj.mCahced.Keys)
                {
                    if (key < refrenceIndex || key > range)
                        mTmpToRemove.Add(key);
                }

                for (int i = 0; i < mTmpToRemove.Count; i++)
                    obj.mCahced.Remove(mTmpToRemove[i]);

                obj.mCahced.Remove(data.Data.Count - 1); // never store the last point cache , it might be intepolating by the realtime feature

                if (data.Data.Count == 0)
                    continue;

                GenerateItemLabels(true, obj, data, viewRect, refrenceIndex, edit);

                if (obj.mUp != null)
                {
                    FillCurrentSeries(true, refrenceIndex);
                    obj.mUp.Generate(this, viewRect, mCurrentSeries, data.UpCandle);
                }
                if (obj.mDown != null)
                {
                    FillCurrentSeries(false, refrenceIndex);
                    obj.mDown.Generate(this, viewRect, mCurrentSeries, data.DownCandle);
                }
            }
        }

        protected override void ClearChart()
        {
            base.ClearChart();
            ClearBillboard();
            mActiveTexts.Clear();
            mCategoryObjects.Clear();
        }

        void GenerateItemLabels(bool realTime, CategoryObject categoryObj, CandleChartData.CategoryData data, Rect viewRect, int refrenceIndex, bool edit)
        {
            if (mItemLabels != null && mItemLabels.isActiveAndEnabled)
            {
                CanvasChartMesh m = null;
                if (realTime)
                {
                    m = categoryObj.mItemLabels;
                    if (m == null)
                        return;
                    m.Clear();
                }
                else
                {
                    m = new CanvasChartMesh(true);
                    m.RecycleText = true;
                    categoryObj.mItemLabels = m;
                }

                Rect textRect = viewRect;
                textRect.xMin -= 1f;
                textRect.yMin -= 1f;
                textRect.xMax += 1f;
                textRect.yMax += 1f;

                for (int i = 0; i < mTransformed.Count; i++)
                {
                    Vector2 pointValue = mTransformed[i].MidPoint.ToVector2();

                    if (textRect.Contains(pointValue) == false)
                        continue;

                    CandleChartData.CandleValue candleVal = mTransformed[i];
                    int candleIndex = i + refrenceIndex;
                    if (edit == false)
                        candleVal = data.Data[i + refrenceIndex];
                    Vector3 labelPos = ((Vector3)pointValue) + new Vector3(mItemLabels.Location.Breadth, mItemLabels.Seperation, mItemLabels.Location.Depth);
                    if (mItemLabels.Alignment == ChartLabelAlignment.Base)
                        labelPos.y -= (float)mTransformed[i].MidPoint.y;

                    string toSet;
                    if (realTime == false || categoryObj.mCahced.TryGetValue(candleIndex, out toSet) == false)
                    {
                        string Open = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Open, 0.0), mVerticalAxis, false);
                        string Close = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Close, 0.0), mVerticalAxis, false);
                        string High = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.High, 0.0), mVerticalAxis, false);
                        string Low = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Low, 0.0), mVerticalAxis, false);
                        string Start = StringFromAxisFormat(new DoubleVector3(candleVal.Start, candleVal.Open, 0.0), mHorizontalAxis, true);
                        string Duration = StringFromAxisFormat(new DoubleVector3(candleVal.Duration, candleVal.Open, 0.0), mHorizontalAxis, true);
                        FormatItem(mRealtimeStringBuilder, Open, Close, High, Low, Start, Duration);
                        toSet = mItemLabels.TextFormat.Format(mRealtimeStringBuilder.ToString(), data.Name, "");
                        categoryObj.mCahced[candleIndex] = toSet;
                    }
                    labelPos -= new Vector3(CanvasFitOffset.x * TotalWidth, CanvasFitOffset.y * TotalHeight, 0f);
                    BillboardText billboard = m.AddText(this, mItemLabels.TextPrefab, transform, mItemLabels.FontSize, mItemLabels.FontSharpness, toSet, labelPos.x, labelPos.y, labelPos.z, 0f, null);
                    TextController.AddText(billboard);
                    AddBillboardText(data.Name, i + refrenceIndex, billboard);
                }

                if (realTime)
                {
                    m.DestoryRecycled();
                    if (m.TextObjects != null)
                    {
                        foreach (BillboardText text in m.TextObjects)
                        {
                            ((IInternalUse)this).InternalTextController.AddText(text);
                        }
                    }
                }
            }
        }
        bool UpPredicate(CandleChartData.CandleValue x)
        {
            if (x.isUp)
                mUpToIndex.Add(mTmpCurrentIndex);
            mTmpCurrentIndex++;
            return x.isUp;
        }
        bool DownPredicate(CandleChartData.CandleValue x)
        {
            if (x.isUp == false)
                mDownToIndex.Add(mTmpCurrentIndex);
            mTmpCurrentIndex++;
            return !x.isUp;
        }
        void FillCurrentSeries(bool isUp, int refrenceIndex)
        {
            mTmpCurrentIndex = refrenceIndex;
            mCurrentSeries.Clear();
            if (isUp)
            {
                mUpToIndex.Clear();
                mCurrentSeries.AddRange(mTransformed.Where(UpPredicate));
            }
            else
            {
                mDownToIndex.Clear();
                mCurrentSeries.AddRange(mTransformed.Where(DownPredicate));
            }
        }

        int MapIndex(int index, bool isUp)
        {
            if (isUp)
            {
                if (mUpToIndex.Count <= index)
                    return index;
                return mUpToIndex[index];
            }
            if (mDownToIndex.Count <= index)
                return index;
            return mDownToIndex[index];
        }

        public override void InternalGenerateChart()
        {
            if (gameObject.activeInHierarchy == false)
                return;
            base.InternalGenerateChart();

            if (FitToContainer)
            {
                RectTransform trans = GetComponent<RectTransform>();
                widthRatio = trans.rect.width;
                heightRatio = trans.rect.height;
            }
            ClearChart();

            if (Data == null)
                return;
            GenerateAxis(true);

            double minX = ((IInternalCandleData)Data).GetMinValue(0, false);
            double minY = ((IInternalCandleData)Data).GetMinValue(1, false);
            double maxX = ((IInternalCandleData)Data).GetMaxValue(0, false);
            double maxY = ((IInternalCandleData)Data).GetMaxValue(1, false);

            double xScroll = GetScrollOffset(0);
            double yScroll = GetScrollOffset(1);
            double xSize = maxX - minX;
            double ySize = maxY - minY;
            double xOut = minX + xScroll + xSize;
            double yOut = minY + yScroll + ySize;

            DoubleVector3 min = new DoubleVector3(xScroll + minX, yScroll + minY);
            DoubleVector3 max = new DoubleVector3(xOut, yOut);

            Rect viewRect = new Rect(0f, 0f, widthRatio, heightRatio);

            int total = ((IInternalCandleData)Data).TotalCategories + 1;
            bool edit = false;
            ClearBillboard();
            mActiveTexts.Clear();
            int index = 0;
            GameObject mask = CreateRectMask(viewRect);

            foreach (CandleChartData.CategoryData data in ((IInternalCandleData)Data).Categories)
            {
                mClipped.Clear();
                mTransformed.Clear();
                int refrenceIndex = ClipCandles(data.Data, mClipped);

                TransformCandles(mClipped, mTransformed, viewRect, min, max);

                if (data.Data.Count == 0 && ChartCommon.IsInEditMode)
                {
                    int tmpIndex = total - 1 - index;
                    float low = (((float)tmpIndex) / (float)total);
                    float high = (((float)tmpIndex + 1) / (float)total);
                    float y1 = Mathf.Lerp(high, low, 0.6f);
                    float y2 = Mathf.Lerp(high, low, 0.3f);
                    mTransformed.Clear();
                    mTransformed.Add(InterpolateCandleInRect(new CandleChartData.CandleValue(y1, high, low, y2, 0.1, 0.1), viewRect));
                    mTransformed.Add(InterpolateCandleInRect(new CandleChartData.CandleValue(y1, low, high, y2, 0.5, 0.2), viewRect));
                    edit = true;
                    index++;
                }
                CategoryObject categoryObj = new CategoryObject(this);
                CanvasCandle up = CreateDataObject(data, mask);
                CanvasCandle down = CreateDataObject(data, mask);

                FillCurrentSeries(true, refrenceIndex);
                up.Generate(this, viewRect, mCurrentSeries, data.UpCandle);

                FillCurrentSeries(false, refrenceIndex);
                down.Generate(this, viewRect, mCurrentSeries, data.DownCandle);

                string catName = data.Name;

                categoryObj.mUp = up;
                categoryObj.mDown = down;
                categoryObj.HookEvents();

                GenerateItemLabels(false, categoryObj, data, viewRect, refrenceIndex, edit);

                categoryObj.Hover += (idx, t, d, pos) => { Category_Hover(catName, idx, t, d, pos); };
                categoryObj.Click += (idx, t, d, pos) => { Category_Click(catName, idx, t, d, pos); };
                categoryObj.Leave += () => { Category_Leave(catName); };
                mCategoryObjects[catName] = categoryObj;
            }
        }

        void Category_Hover(string category, int index, int type, object data, Vector2 position)
        {
            CandleChartData.CandleValue candle = Data.GetCandle(category, index);
            Dictionary<int, BillboardText> catgoryTexts;
            BillboardText b;

            if (mTexts.TryGetValue(category, out catgoryTexts))
            {
                if (catgoryTexts.TryGetValue(index, out b))
                    SelectActiveText(b);
            }
            Rect selection = new Rect();
            if (data != null && data is Rect)
                selection = (Rect)data;
            OnItemHoverted(new CandleEventArgs(index, type, selection, position, candle, category));
        }

        /// <summary>
        /// handles click of the category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="index"></param>
        /// <param name="type"></param>
        /// <param name="data"></param>
        /// <param name="position"></param>
        void Category_Click(string category, int index, int type, object data, Vector2 position)
        {
            CandleChartData.CandleValue candle = Data.GetCandle(category, index);
            Dictionary<int, BillboardText> catgoryTexts;
            BillboardText b;

            if (mTexts.TryGetValue(category, out catgoryTexts))
            {
                if (catgoryTexts.TryGetValue(index, out b))
                    SelectActiveText(b);
            }

            Rect selection = new Rect();
            if (data != null && data is Rect)
                selection = (Rect)data;
            OnItemSelected(new CandleEventArgs(index, type, selection, position, candle, category));
        }

        void Category_Leave(string cat)
        {
            TriggerActiveTextsOut();
            OnItemLeave(new CandleEventArgs(0, 0, new Rect(), new Vector3(), new CandleChartData.CandleValue(), cat),"none");
        }

        protected override void OnItemHoverted(object userData)
        {
            base.OnItemHoverted(userData); 
            var args = userData as CandleEventArgs;
            mOccupiedCateogies.Add(args.Category);
        }

        protected override void OnItemSelected(object userData)
        {
            base.OnItemSelected(userData);
            var args = userData as CandleEventArgs;
            mOccupiedCateogies.Add(args.Category);
        }

        protected override void OnItemLeave(object userData,string type)
        {
            //base.OnItemLeave(userData);
            CandleEventArgs args = userData as CandleEventArgs;
            if (args == null)
                return;

            string category = args.Category;
            mOccupiedCateogies.Remove(category);
            mOccupiedCateogies.RemoveWhere(x => !Data.HasCategory(x));
            if (mOccupiedCateogies.Count == 0)
            {
                if (NonHovered != null)
                    NonHovered.Invoke();
            }
        }

        internal override void SetAsMixedSeries()
        {
            throw new NotImplementedException();
        }
    }
}
