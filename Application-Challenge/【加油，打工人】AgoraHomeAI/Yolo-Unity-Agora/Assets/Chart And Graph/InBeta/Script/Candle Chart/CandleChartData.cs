#define Graph_And_Chart_PRO

using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [Serializable]
    public class CandleChartData : ScrollableChartData, IInternalCandleData
    {
        [Serializable]
        public struct CandleValue
        {
            public CandleValue(double open, double high, double low, double close, DateTime start, TimeSpan duration)
            {
                Open = open;
                High = high;
                Low = low;
                Close = close;
                Start = ChartDateUtility.DateToValue(start);
                Duration = ChartDateUtility.TimeSpanToValue(duration);
            }

            public CandleValue(double open, double high, double low, double close, double start, double duration)
            {
                Open = open;
                High = high;
                Low = low;
                Close = close;
                Start = start;
                Duration = duration;
            }

            public double Open;
            public double High;
            public double Low;
            public double Close;
            public double Start;
            public double Duration;

            public bool isUp
            {
                get { return Close > Open; }
            }

            public double End
            {
                get { return Start + Duration; }
            }

            public DoubleVector2 MidPoint
            {
                get { return new DoubleVector2(Start + (Duration * 0.5), (Open + Close) * 0.5); }
            }

            public double Max
            {
                get
                {
                    return Math.Max(Open, Close);
                }
            }

            public double LowBound
            {
                get
                {
                    return Math.Min(Math.Min(High, Low), Math.Min(Open, Close));
                }
            }

            public double HighBound
            {
                get
                {
                    return Math.Max(Math.Max(High, Low), Math.Max(Open, Close));
                }
            }

            public double Min
            {
                get
                {
                    return Math.Min(Open, Close);
                }
            }
        }

        [Serializable]
        public class CandleSettings
        {
            public double LineThickness = 2.0;
            public double CandleThicknessMultiplier = 1.0;
            public double OutlineThickness = 0.0;
            public ChartItemEffect CandleHoverPrefab;
            public Material Outline;
            public Material Line;
            public Material Fill;
            public GameObject CandlePrefab;
        }

        [Serializable]
        public class CategoryData : BaseScrollableCategoryData
        {
            public List<CandleValue> Data = new List<CandleValue>();
            public CandleSettings UpCandle = new CandleSettings();
            public CandleSettings DownCandle = new CandleSettings();
            public double Depth = 0f;
        }

        class CandleComparer : IComparer<CandleValue>
        {
            public int Compare(CandleValue x, CandleValue y)
            {
                if (x.Open < y.Open)
                    return -1;
                if (x.Open > y.Open)
                    return 1;
                return 0;

            }
        }

        [Serializable]
        class SerializedCategory
        {
            public string Name;
            [HideInInspector]
            public CandleValue[] Data;
            [HideInInspector]
            public double? MaxX, MaxY, MinX, MinY;
            public CandleSettings UpCandle = new CandleSettings();
            public CandleSettings DownCandle = new CandleSettings();
            public double Depth = 0f;
        }

        CandleComparer mComparer = new CandleComparer();

        [SerializeField]
        SerializedCategory[] mSerializedData = new SerializedCategory[0];

        event EventHandler IInternalCandleData.InternalViewPortionChanged
        {
            add
            {
                ViewPortionChanged += value;
            }
            remove
            {
                ViewPortionChanged -= value;
            }
        }

        event EventHandler IInternalCandleData.InternalDataChanged
        {
            add
            {
                DataChanged += value;
            }

            remove
            {
                DataChanged -= value;
            }
        }

        /// <summary>
        /// rename a category. throws and exception on error
        /// </summary>
        /// <param name="prevName"></param>
        /// <param name="newName"></param>
        public void RenameCategory(string prevName, string newName)
        {
            if (prevName == newName)
                return;
            if (mData.ContainsKey(newName))
                throw new ArgumentException(String.Format("A category named {0} already exists", newName));
            CategoryData cat = (CategoryData)mData[prevName];
            mData.Remove(prevName);
            cat.Name = newName;
            mData.Add(newName, cat);
            RaiseDataChanged();
        }

        /// <summary>
        /// Adds a new category to the candle chart.
        /// </summary>
        /// <param name="category"></param>
        /// <param name="material"></param>
        /// <param name="innerFill"></param>
        public void AddCategory(string category, CandleSettings up, CandleSettings down, double depth)
        {
            AddCategory3DCandle(category, up, down, 0f);
        }

        /// <summary>
        /// add category to the candle chart
        /// </summary>
        /// <param name="category"></param>
        /// <param name="up"></param>
        /// <param name="down"></param>
        /// <param name="depth"></param>
        public void AddCategory3DCandle(string category, CandleSettings up, CandleSettings down, double depth)
        {
            if (mData.ContainsKey(category))
                throw new ArgumentException(String.Format("A category named {0} already exists", category));
            CategoryData data = new CategoryData();
            mData.Add(category, data);
            data.Name = category;
            data.DownCandle = down;
            data.UpCandle = up;
            data.Depth = depth;
            RaiseDataChanged();
        }

        /// <summary>
        /// removed a category from the DataSource. returnes true on success , or false if the category does not exist
        /// </summary>
        /// <param name="category"></param>
        /// <returns></returns>
        public bool RemoveCategory(string category)
        {
            return mData.Remove(category);
        }

        public void SetDownCandle(string category, CandleSettings down)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.DownCandle = down;
            RaiseDataChanged();
        }

        public void SetUpCandle(string category, CandleSettings up)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.UpCandle = up;
            RaiseDataChanged();
        }


        /// <summary>
        /// sets the depth for a 3d graph category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="depth"></param>
        public void Set3DCategoryDepth(string category, double depth)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }
            if (depth < 0)
                depth = 0f;
            CategoryData data = (CategoryData)mData[category];
            data.Depth = depth;
            RaiseDataChanged();
        }

        /// <summary>
        /// clears all the data for the selected category
        /// </summary>
        /// <param name="category"></param>
        public void ClearCategory(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }
            mData[category].MaxX = null;
            mData[category].MaxY = null;
            mData[category].MinX = null;
            mData[category].MinY = null;
            ((CategoryData)mData[category]).Data.Clear();
            RaiseDataChanged();
        }

        /// <summary>
        /// gets the candle at the specified index for a given category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="index"></param>
        /// <returns></returns>
        public int GetCandleCount(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return 0;
            }
            CategoryData data = (CategoryData)mData[category];
            return data.Data.Count;
        }

        /// <summary>
        /// gets the candle at the specified index for a given category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="index"></param>
        /// <returns></returns>
        public CandleValue GetCandle(string category, int index)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return new CandleValue();
            }
            CategoryData data = (CategoryData)mData[category];
            return data.Data[index];
        }

        /// <summary>
        /// returns the total amount of candles in the given category
        /// </summary>
        /// <param name="category"></param>
        /// <returns></returns>
        public int GetTotalCandlesInCategory(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return 0;
            }

            CategoryData data = (CategoryData)mData[category];
            return data.Data.Count;
        }

        /// <summary>
        /// use this to modify candles in realtime. 
        /// </summary>
        /// <param name="category"></param>
        /// <param name="candle"></param>
        public void ModifyCandleInCategory(string category, int candleIndex, double open, double high, double low, double close)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            List<CandleValue> candles = data.Data;

            if (candleIndex == -1)
                candleIndex = data.Data.Count - 1;
            if (candleIndex < 0 || candleIndex >= candles.Count)
            {
                Debug.LogWarning("Candle index is out of range, call is ignored");
                return;
            }

            double candleMax = Math.Max(Math.Max(open, close), Math.Max(high, low));
            double candleMin = Math.Min(Math.Min(open, close), Math.Min(high, low));

            if (data.MaxY.HasValue == false || data.MaxY.Value < candleMax)
                data.MaxY = candleMax;
            if (data.MinY.HasValue == false || data.MinY.Value > candleMin)
                data.MinY = candleMin;

            CandleValue candle = data.Data[candleIndex];
            candle.Open = open;
            candle.Close = close;
            candle.High = high;
            candle.Low = low;
            data.Data[candleIndex] = candle;
            RaiseRealtimeDataChanged(candleIndex, category);
        }

        /// <summary>
        /// removes a candle from a category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="index"></param>
        public void RemoveCandleInCategory(string category, int candleIndex)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            List<CandleValue> candles = data.Data;

            if (candleIndex < 0 || candleIndex >= candles.Count)
            {
                Debug.LogWarning("Candle index is out of range, call is ignored");
                return;
            }

            data.Data.RemoveAt(candleIndex);
            RaiseRealtimeDataChanged(candleIndex, category);
        }

        /// <summary>
        /// use this to modify candles in realtime. this overload modifies the last candle and can be used for realtime candle data stream
        /// </summary>
        /// <param name="category"></param>
        /// <param name="candle"></param>
        public void ModifyLastCandleInCategory(string category, double open, double high, double low, double close)
        {
            ModifyCandleInCategory(category, -1, open, high, low, close);
        }


        class Slider : BaseSlider
        {
            public string category;
            public double from;
            public int index;
            public double to;
            public double current;
            public double y;
            private CandleChartData mParent;

            public Slider(CandleChartData parent)
            {
                mParent = parent;
            }

            public override DoubleVector2 Max
            {
                get
                {
                    return new DoubleVector2(current, y);
                }
            }

            public override DoubleVector2 Min
            {
                get
                {
                    return new DoubleVector2(current, y);
                }
            }

            public override string Category { get { return category;  } }

            public override int MinIndex
            {
                get { return index; }
            }
            public override bool Update()
            {
                BaseScrollableCategoryData baseData;

                if (mParent.mData.TryGetValue(category, out baseData) == false)
                    return true;

                double time = Time.time;
                time -= StartTime;

                if (Duration <= 0.0001f)
                    time = 1f;
                else
                {
                    time /= Duration;
                    Math.Max(0.0, Math.Min(time, 1.0));
                }

                current = from * (1.0 - time) + to * time;
                if (time >= 1f)
                {
                    mParent.ModifyMinMax(baseData, new DoubleVector3(current, y, 0.0));
                    return true;
                }
                return false;
            }
        }

        /// <summary>
        /// adds a point to the category. The points are sorted by their x value automatically
        /// </summary>
        /// <param name="category"></param>
        /// <param name="point"></param>
        public void AddCandleToCategory(string category, CandleValue candle, float autoScrollSlideTime = 0f)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the chart");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            List<CandleValue> candles = data.Data;

            double start = candle.Start;
            double end = candle.Start + candle.Duration;

            double candleMax = Math.Max(Math.Max(candle.Open, candle.Close), Math.Max(candle.High, candle.Low));
            double candleMin = Math.Min(Math.Min(candle.Open, candle.Close), Math.Min(candle.High, candle.Low));


            if (autoScrollSlideTime <= 0f)
            {
                if (data.MaxX.HasValue == false || data.MaxX.Value < end)
                    data.MaxX = end;
            }
            if (data.MinX.HasValue == false || data.MinX.Value > start)
                data.MinX = start;
            if (data.MaxY.HasValue == false || data.MaxY.Value < candleMax)
                data.MaxY = candleMax;
            if (data.MinY.HasValue == false || data.MinY.Value > candleMin)
                data.MinY = candleMin;

            if (candles.Count > 0)
            {
                if (candles[candles.Count - 1].Start < candle.Start)
                {
                    if (autoScrollSlideTime > 0f)
                    {
                        Slider s = new Slider(this);
                        s.category = category;
                        s.from = candles[candles.Count - 1].End;
                        s.to = end;
                        s.StartTime = Time.time;
                        s.Duration = autoScrollSlideTime;
                        s.y = (candleMax + candleMin) * 0.5;
                        s.index = candles.Count - 1;
                        mSliders.Add(s);
                    }
                    candles.Add(candle);
                    RaiseRealtimeDataChanged(candles.Count-1, category);
                    return;
                }
            }

            int search = candles.BinarySearch(candle, mComparer);
            if (search < 0)
                search = ~search;
            candles.Insert(search, candle);
            RaiseRealtimeDataChanged(search, category);
        }

        double IInternalCandleData.GetMaxValue(int axis, bool dataValue)
        {
            return GetMaxValue(axis, dataValue);
        }

        double IInternalCandleData.GetMinValue(int axis, bool dataValue)
        {
            return GetMinValue(axis, dataValue);
        }

        public override void OnAfterDeserialize()
        {
            if (mSerializedData == null)
                return;
            mData.Clear();
            mSuspendEvents = true;
            for (int i = 0; i < mSerializedData.Length; i++)
            {
                SerializedCategory cat = mSerializedData[i];
                if (cat.Depth < 0)
                    cat.Depth = 0f;
                string name = cat.Name;
                AddCategory3DCandle(name, cat.UpCandle, cat.DownCandle, cat.Depth);
                CategoryData data = (CategoryData)mData[name];
                data.Data.AddRange(cat.Data);
                data.MaxX = cat.MaxX;
                data.MaxY = cat.MaxY;
                data.MinX = cat.MinX;
                data.MinY = cat.MinY;
            }
            mSuspendEvents = false;
        }

        public override void OnBeforeSerialize()
        {
            List<SerializedCategory> serialized = new List<SerializedCategory>();
            foreach (KeyValuePair<string, CategoryData> pair in mData.Select(x => new KeyValuePair<string, CategoryData>(x.Key, (CategoryData)x.Value)))
            {
                SerializedCategory cat = new SerializedCategory();
                cat.Name = pair.Key;
                cat.MaxX = pair.Value.MaxX;
                cat.MinX = pair.Value.MinX;
                cat.MaxY = pair.Value.MaxY;
                cat.MinY = pair.Value.MinY;
                cat.UpCandle = pair.Value.UpCandle;
                cat.DownCandle = pair.Value.DownCandle;
                cat.Depth = pair.Value.Depth;
                cat.Data = pair.Value.Data.ToArray();
                if (cat.Depth < 0)
                    cat.Depth = 0f;
                serialized.Add(cat);
            }
            mSerializedData = serialized.ToArray();
        }


        public override BaseScrollableCategoryData GetDefaultCategory()
        {
            throw new NotImplementedException();
        }

        protected override void InnerClearCategory(string category)
        {
            throw new NotImplementedException();
        }

        protected override void AppendDatum(string category, MixedSeriesGenericValue value)
        {
            throw new NotImplementedException();
        }

        protected override void AppendDatum(string category, IList<MixedSeriesGenericValue> value)
        {
            throw new NotImplementedException();
        }

        protected override bool AddCategory(string category, BaseScrollableCategoryData data)
        {
            throw new NotImplementedException();
        }

        int IInternalCandleData.TotalCategories
        {
            get { return mData.Count; }
        }

        event Action<int,string> IInternalCandleData.InternalRealTimeDataChanged
        {
            add
            {
                RealtimeDataChanged += value;
            }

            remove
            {
                RealtimeDataChanged -= value;
            }
        }
        IEnumerable<CategoryData> IInternalCandleData.Categories
        {
            get
            {
                return mData.Values.Select(x => (CategoryData)x);
            }
        }
    }
}
