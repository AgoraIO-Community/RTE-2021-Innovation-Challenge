#define Graph_And_Chart_PRO
using ChartAndGraph.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [Serializable]
    public partial class GraphData : ScrollableChartData, IInternalGraphData
    {
        private List<DoubleVector3> mTmpDriv = new List<DoubleVector3>();
        partial void CheckExtended(ref bool result);

        bool IsExtended
        {
            get
            {
                bool res = false;
                CheckExtended(ref res);
                return res;
            }
        }

        [Serializable]
        public class CategoryData : BaseScrollableCategoryData
        {
            static List<DoubleVector3> mEmpty = new List<DoubleVector3>();

            public bool IsBezierCurve;
            public int SegmentsPerCurve = 10;
            public List<DoubleVector3> mTmpCurveData = new List<DoubleVector3>();
            public List<DoubleVector3> Data = new List<DoubleVector3>();
            public bool Regenerate = true;
            public bool AllowNonFunctions = false;
            public Vector2[] initialData;
            public List<DoubleVector3> getPoints()
            {
                if (Enabled == false)
                    return mEmpty;
                if (IsBezierCurve == false)
                    return Data;
                if (Regenerate == false)
                    return mTmpCurveData;
                Regenerate = false;
                mTmpCurveData.Clear();
                if (Data.Count <= 0)
                    return mTmpCurveData;
                mTmpCurveData.Add(Data[0]);
                if (Data.Count < 4)
                    return mTmpCurveData;
                int endCount = Data.Count - 1;
                for (int i = 0; i < endCount; i += 3)
                {
                    AddInnerCurve(Data[i], Data[i + 1], Data[i + 2], Data[i + 3]);
                    mTmpCurveData.Add(Data[i + 3]);
                }
                return mTmpCurveData;
            }

            public void AddInnerCurve(DoubleVector3 p1, DoubleVector3 c1, DoubleVector3 c2, DoubleVector3 p2)
            {
                for (int i = 0; i < SegmentsPerCurve; i++)
                {
                    double blend = ((double)i) / (double)SegmentsPerCurve;
                    double invBlend = 1f - blend;
                    DoubleVector3 p = (invBlend * invBlend * invBlend * p1) + (3f * invBlend * invBlend * blend * c1) + (3f * blend * blend * invBlend * c2) + (blend * blend * blend * p2);
                    mTmpCurveData.Add(new DoubleVector3(p.x, p.y, 0f));
                }
            }

            public ChartItemEffect LineHoverPrefab;
            public ChartItemEffect PointHoverPrefab;
            public Material LineMaterial;
            public MaterialTiling LineTiling;
            public double LineThickness = 1f;
            public Material FillMaterial;
            public bool StetchFill = false;
            public Material PointMaterial;
            public double PointSize = 5f;
            public PathGenerator LinePrefab;
            public FillPathGenerator FillPrefab;
            public GameObject DotPrefab;
            public double Depth = 0f;


            public object Store()
            {
                return MemberwiseClone();
            }

            public void Restore(object store)
            {
                var cat = (CategoryData)store;
                LineHoverPrefab = cat.LineHoverPrefab;
                PointHoverPrefab = cat.PointHoverPrefab;
                LineMaterial = cat.LineMaterial;
                LineTiling = cat.LineTiling;
                LineThickness = cat.LineThickness;
                FillMaterial = cat.FillMaterial;
                StetchFill = cat.StetchFill;
                PointMaterial = cat.PointMaterial;
                PointSize = cat.PointSize;
                LinePrefab = cat.LinePrefab;
                FillPrefab = cat.FillPrefab;
                DotPrefab = cat.DotPrefab;
                Depth = cat.Depth;
                IsBezierCurve = cat.IsBezierCurve;
                SegmentsPerCurve = cat.SegmentsPerCurve;
                AllowNonFunctions = cat.AllowNonFunctions;
            }
        }

        class VectorComparer : IComparer<DoubleVector3>
        {
            public int Compare(DoubleVector3 x, DoubleVector3 y)
            {
                if (x.x < y.x)
                    return -1;
                if (x.x > y.x)
                    return 1;
                return 0;

            }
        }

        [Serializable]
        class SerializedCategory
        {
            public string Name;
            public Vector2[] InitialData = new Vector2[0];
            public bool IsBezierCurve;
            public int SegmentsPerCurve = 10;
            [NonCanvasAttribute]
            public double Depth;
            [HideInInspector]
            public DoubleVector3[] data;
            [HideInInspector]
            public double? MaxX, MaxY, MinX, MinY, MaxRadius;
            public ChartItemEffect LineHoverPrefab;
            public ChartItemEffect PointHoverPrefab;
            [NonCanvasAttribute]
            public PathGenerator LinePrefab;
            public Material Material;
            public MaterialTiling LineTiling;
            [NonCanvasAttribute]
            public FillPathGenerator FillPrefab;
            public Material InnerFill;
            public double LineThickness = 1f;
            public bool StetchFill = false;
            [NonCanvasAttribute]
            public GameObject DotPrefab;
            public Material PointMaterial;
            public double PointSize;
            public int ViewOrder;
            [HideInInspector]
            public bool AllowNonFunctionsBeta = false;
        }

        partial class Slider : BaseSlider
        {
#pragma warning disable 0649
#pragma warning disable 0414
            public string category;
            public int from;
            public DoubleVector3 To;
            public DoubleVector3 current;
            public int index;
            private GraphData mParent;
#pragma warning restore 0414
#pragma warning restore 0649
            public override string Category
            {
                get { return category; }
            }

            public Slider(GraphData parent)
            {
                mParent = parent;
            }

            public override DoubleVector2 Max
            {
                get
                {
                    return current.ToDoubleVector2();
                }
            }

            public override int MinIndex
            {
                get { return from; }
            }

            public override DoubleVector2 Min
            {
                get
                {
                    return current.ToDoubleVector2();
                }
            }
        }
        VectorComparer mComparer = new VectorComparer();

        [SerializeField]
        SerializedCategory[] mSerializedData = new SerializedCategory[0];

        public void ClearAndMakeBezierCurve(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)(mData[category]);
            data.IsBezierCurve = true;
            ClearCategory(category);
        }


        public void ClearAndMakeLinear(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)(mData[category]);
            data.IsBezierCurve = false;
            ClearCategory(category);
        }

        event Action<int, string> IInternalGraphData.InternalRealTimeDataChanged
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
        event EventHandler IInternalGraphData.InternalViewPortionChanged
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
        event EventHandler IInternalGraphData.InternalDataChanged
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
        /// Adds a new category to the graph chart. each category corrosponds to a graph line. 
        /// </summary>
        /// <param name="category"></param>
        /// <param name="material"></param>
        /// <param name="innerFill"></param>
        public void AddCategory(string category, Material lineMaterial, double lineThickness, MaterialTiling lineTiling, Material innerFill, bool strechFill, Material pointMaterial, double pointSize)
        {
            if (mData.ContainsKey(category))
                throw new ArgumentException(String.Format("A category named {0} already exists", category));
            CategoryData data = new CategoryData();
            mData.Add(category, data);
            data.Name = category;
            data.LineMaterial = lineMaterial;
            data.LineHoverPrefab = null;
            data.PointHoverPrefab = null;
            data.FillMaterial = innerFill;
            data.LineThickness = lineThickness;
            data.LineTiling = lineTiling;
            data.StetchFill = strechFill;
            data.PointMaterial = pointMaterial;
            data.PointSize = pointSize;
            RaiseDataChanged();
        }
        public bool isCategoryEnabled(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return false;
            }
            CategoryData data = (CategoryData)mData[category];
            return data.Enabled;
        }
        public void SetCategoryEnabled(string category, bool enabled)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.Enabled = enabled;
            RaiseDataChanged();
        }

        public object StoreCategory(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return null;
            }
            CategoryData data = (CategoryData)mData[category];
            return data.Store();
        }

        public void RestoreCategory(string category, object store)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.Restore(store);

        }
        /// <summary>
        /// used intenally , do not call
        /// </summary>
        /// <param name="cats"></param>
        public object[] StoreAllCategoriesinOrder()
        {
            return mData.Values.Where(x => x.ViewOrder >= 0).OrderBy(x => x.ViewOrder).Cast<object>().ToArray();
        }
        /// <summary>
        /// this is a beta method that allows having paths drawn on the graph
        /// </summary>
        /// <param name="category"></param>
        /// <param name="AllowNonFunctions"></param>
        public void ClearAndSetAllowNonFunctions(string category, bool AllowNonFunctions)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.AllowNonFunctions = AllowNonFunctions;
            ClearCategory(category);

        }
        public void Set2DCategoryPrefabs(string category, ChartItemEffect lineHover, ChartItemEffect pointHover)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.LineHoverPrefab = lineHover;
            data.PointHoverPrefab = pointHover;
        }

        protected void AddInnerCategoryGraph(string category, PathGenerator linePrefab, Material lineMaterial, double lineThickness, MaterialTiling lineTiling, FillPathGenerator fillPrefab, Material innerFill, bool strechFill, GameObject pointPrefab, Material pointMaterial, double pointSize, double depth, bool isCurve, int segmentsPerCurve,Vector2[] initialData =null)
        {
            if (mData.ContainsKey(category))
                throw new ArgumentException(String.Format("A category named {0} already exists", category));
            if (depth < 0f)
                depth = 0f;
            CategoryData data = new CategoryData();
            mData.Add(category, data);
            data.Name = category;
            data.LineMaterial = lineMaterial;
            data.FillMaterial = innerFill;
            data.LineThickness = lineThickness;
            data.LineTiling = lineTiling;
            data.StetchFill = strechFill;
            data.PointMaterial = pointMaterial;
            data.PointSize = pointSize;
            data.LinePrefab = linePrefab;
            data.FillPrefab = fillPrefab;
            data.DotPrefab = pointPrefab;
            data.Depth = depth;
            data.IsBezierCurve = isCurve;
            data.SegmentsPerCurve = segmentsPerCurve;
            data.initialData = initialData;
            RaiseDataChanged();
        }

        void SetInitialData(string category, Vector2[] initialData,bool isCurve)
        {
            if (initialData.Length ==0)
                return;
            if(isCurve)
            {
                Vector2 p = initialData[0];
                SetCurveInitialPoint(category,p.x,p.y);
                for (int i = 1; i < initialData.Length; i++)
                    AddLinearCurveToCategory(category, new DoubleVector2(initialData[i]));
                MakeCurveCategorySmooth(category);
            }
            else
            {
                for (int i = 0; i < initialData.Length; i++)
                {
                    Vector2 p = initialData[i];
                    AddPointToCategory(category, p.x,p.y);
                }
            }
        }
        /// <summary>
        /// sets the line style for the category
        /// </summary>
        /// <param name="category"></param>
        /// <param name="lineMaterial"></param>
        /// <param name="lineThickness"></param>
        /// <param name="lineTiling"></param>
        public void SetCategoryLine(string category, Material lineMaterial, double lineThickness, MaterialTiling lineTiling)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.LineMaterial = lineMaterial;
            data.LineThickness = lineThickness;
            data.LineTiling = lineTiling;
            RaiseDataChanged();
        }


        /// <summary>
        /// removed a category from the DataSource. returnes true on success , or false if the category does not exist
        /// </summary>
        /// <param name="category"></param>
        /// <returns></returns>
        public bool RemoveCategory(string category)
        {
            mSliders.RemoveAll(x => (((Slider)x).category == category));
            return mData.Remove(category);
        }

        /// <summary>
        /// sets the point style for the selected category. set material to null for no points
        /// </summary>
        /// <param name="category"></param>
        /// <param name="pointMaterial"></param>
        /// <param name="pointSize"></param>
        public void SetCategoryPoint(string category, Material pointMaterial, double pointSize)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            data.PointMaterial = pointMaterial;
            data.PointSize = pointSize;
            RaiseDataChanged();
        }


        /// <summary>
        /// sets the fill style for the selected category.set the material to null for no fill
        /// </summary>
        /// <param name="category"></param>
        /// <param name="fillMaterial"></param>
        /// <param name="strechFill"></param>
        public void SetCategoryFill(string category, Material fillMaterial, bool strechFill)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }
            CategoryData data = (CategoryData)mData[category];
            data.FillMaterial = fillMaterial;
            data.StetchFill = strechFill;
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
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            mSliders.RemoveAll(x => (((Slider)x).category == category));
            mData[category].MaxX = null;
            mData[category].MaxY = null;
            mData[category].MinX = null;
            mData[category].MinY = null;
            mData[category].MaxRadius = null;
            ((CategoryData)mData[category]).Data.Clear();
            ((CategoryData)mData[category]).Regenerate = true;
            RaiseDataChanged();
        }

        /// <summary> 
        /// adds a point to the category. having the point x,y values as dates
        /// <param name="category"></param>
        /// <param name="x"></param>
        /// <param name="y"></param>
        public void AddPointToCategory(string category, DateTime x, DateTime y, double pointSize = -1f)
        {

            double xVal = ChartDateUtility.DateToValue(x);
            double yVal = ChartDateUtility.DateToValue(y);
            AddPointToCategory(category, (double)xVal, (double)yVal, pointSize);
        }


        /// <summary>
        /// gets the last point for the specified category. returns false if the category is empty , otherwise returns true and assigns the point to the "point" parameter
        /// </summary>
        /// <param name="category"></param>
        /// <param name="point"></param>
        /// <returns></returns>
        public bool GetLastPoint(string category, out DoubleVector3 point)
        {
            CategoryData data = (CategoryData)mData[category];
            List<DoubleVector3> points = data.getPoints();
            point = DoubleVector3.zero;
            if (points.Count == 0)
                return false;
            int index = points.Count - 1;
            point = points[index];
            return true;
        }

        public DoubleVector3 GetPoint(string category, int index)
        {
            CategoryData data = (CategoryData)mData[category];
            List<DoubleVector3> points = data.getPoints();
            if (points.Count == 0)
                return DoubleVector3.zero;
            if (index < 0)
                return points[0];
            if (index >= points.Count)
                return points[points.Count - 1];
            return points[index];

        }

        public static void AddPointToCategoryWithLabel(GraphChartBase chart, string category, DateTime x, double y, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            AddPointToCategoryWithLabel(chart, category, ChartDateUtility.DateToValue(x), y, pointSize, xLabel, yLabel);
        }
        public static void AddPointToCategoryWithLabel(GraphChartBase chart, string category, double x, DateTime y, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            AddPointToCategoryWithLabel(chart, category, x, ChartDateUtility.DateToValue(y), pointSize, xLabel, yLabel);
        }
        public static void AddPointToCategoryWithLabel(GraphChartBase chart, string category, DateTime x, DateTime y, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            AddPointToCategoryWithLabel(chart, category, ChartDateUtility.DateToValue(x), ChartDateUtility.DateToValue(y), pointSize, xLabel, yLabel);
        }



        public static void AddPointToCategoryWithLabel(GraphChartBase chart, string category, double x, double y, double pointSize = -1, string xLabel = null, string yLabel = null)
        {
            DoubleVector3 item = new DoubleVector3(x, y, 0.0);
            chart.VectorValueToStringMap[item] = new KeyValuePair<string, string>(xLabel, yLabel);
            chart.DataSource.AddPointToCategory(category, x, y, pointSize);
        }
        

        /// <summary>
        /// adds a point to the category. having the point x value as date
        /// <param name="category"></param>
        /// <param name="x"></param>
        /// <param name="y"></param>
        public void AddPointToCategory(string category, DateTime x, double y, double pointSize = -1f)
        {
            double xVal = ChartDateUtility.DateToValue(x);
            AddPointToCategory(category, (double)xVal, y, pointSize);
        }

        /// <summary>
        /// adds a point to the category. having the point y value as date
        /// </summary>
        /// <param name="category"></param>
        /// <param name="x"></param>
        /// <param name="y"></param>
        public void AddPointToCategory(string category, double x, DateTime y, double pointSize = -1f)
        {
            double yVal = ChartDateUtility.DateToValue(y);
            AddPointToCategory(category, x, (double)yVal, pointSize);

        }


        public void SetCurveInitialPoint(string category, DateTime x, double y, double pointSize = -1f)
        {
            SetCurveInitialPoint(category,ChartDateUtility.DateToValue(x), y, pointSize);
        }

        public void SetCurveInitialPoint(string category, DateTime x, DateTime y, double pointSize = -1f)
        {
            SetCurveInitialPoint(category, ChartDateUtility.DateToValue(x), ChartDateUtility.DateToValue(y), pointSize);
        }

        public void SetCurveInitialPoint(string category, double x, DateTime y, double pointSize = -1f)
        {
            SetCurveInitialPoint(category, x,ChartDateUtility.DateToValue(y), pointSize);
        }
        public void SetCategoryViewOrder(string category, int viewOrder)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            data.ViewOrder = viewOrder;
            RaiseDataChanged();
        }
        public void SetCurveInitialPoint(string category, double x, double y,double pointSize = -1f)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            if (data.IsBezierCurve == false)
            {
                Debug.LogWarning("Category is not Bezier curve. use AddPointToCategory instead ");
                return;
            }

            if(data.Data.Count > 0)
            {
                Debug.LogWarning("Initial point already set for this category, call is ignored. Call ClearCategory to create a new curve");
                return;
            }
            data.Regenerate = true;
            if (data.MaxRadius.HasValue == false || data.MaxRadius.Value < pointSize)
                data.MaxRadius = pointSize;
            if (data.MaxX.HasValue == false || data.MaxX.Value < x)
                data.MaxX = x;
            if (data.MinX.HasValue == false || data.MinX.Value > x)
                data.MinX = x;
            if (data.MaxY.HasValue == false || data.MaxY.Value < y)
                data.MaxY = y;
            if (data.MinY.HasValue == false || data.MinY.Value > y)
                data.MinY = y;

            DoubleVector3 sizedPoint = new DoubleVector3(x, y, pointSize);
            data.Data.Add(sizedPoint);
            RaiseDataChanged();
        }

        private double min3(double a,double b,double c)
        {
            return Math.Min(a, Math.Min(b, c));
        }

        private double max3(double a, double b, double c)
        {
            return Math.Max(a, Math.Max(b, c));
        }

        private DoubleVector2 max3(DoubleVector2 a, DoubleVector2 b, DoubleVector2 c)
        {
            return new DoubleVector2(max3(a.x, b.x, c.x), max3(a.y, b.y, c.y));
        }

        private DoubleVector2 min3(DoubleVector2 a, DoubleVector2 b, DoubleVector2 c)
        {
            return new DoubleVector2(min3(a.x, b.x, c.x), min3(a.y, b.y, c.y));
        }
        public void MakeCurveCategorySmoothCubic(string category)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            if (data.IsBezierCurve == false)
            {
                Debug.LogWarning("Category is not Bezier curve. use AddPointToCategory instead ");
                return;
            }
            List<DoubleVector3> points = data.Data;
            data.Regenerate = true;
            mTmpDriv.Clear();
            for (int i = 0; i < points.Count; i += 3)
            {
                DoubleVector3 prev = points[Mathf.Max(i - 3, 0)];
                DoubleVector3 next = points[Mathf.Min(i + 3, points.Count - 1)];
                DoubleVector3 diff = next - prev;
                mTmpDriv.Add(diff * 0.25f);
            }

            for (int i = 3; i < points.Count; i += 3)
            {
                int driv = i / 3;
                DoubleVector3 ct1 = points[i - 3] + (DoubleVector3)mTmpDriv[driv - 1];
                DoubleVector3 ct2 = points[i] - (DoubleVector3)mTmpDriv[driv];
                points[i - 2] = ct1;
                points[i - 1] = ct2;
            }
            RaiseDataChanged();
        }
        public void MakeCurveCategorySmooth(string category,float tensor = 0.25f)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            if (data.IsBezierCurve == false)
            {
                Debug.LogWarning("Category is not Bezier curve. use AddPointToCategory instead ");
                return;
            }
            List<DoubleVector3> points = data.Data;
            data.Regenerate = true;
            mTmpDriv.Clear();
            for(int i=0; i< points.Count; i+=3)
            {
                DoubleVector3 prev = points[Mathf.Max(i - 3, 0)];
                DoubleVector3 next = points[Mathf.Min(i + 3, points.Count - 1)];
                DoubleVector3 diff = next - prev;
                mTmpDriv.Add(diff * tensor);
            }

            for (int i = 3; i < points.Count; i+=3)
            {
                int driv = i / 3;
                DoubleVector3 ct1 = points[i - 3] + (DoubleVector3)mTmpDriv[driv - 1];
                DoubleVector3 ct2 = points[i] - (DoubleVector3)mTmpDriv[driv];
                points[i - 2] = ct1;
                points[i - 1] = ct2;
            }
            RaiseDataChanged();
        }

        public void AddLinearCurveToCategory(string category, DoubleVector2 toPoint, double pointSize = -1f)
        {
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            if (data.IsBezierCurve == false)
            {
                Debug.LogWarning("Category is not Bezier curve. use AddPointToCategory instead ");
                return;
            }

            if (data.Data.Count == 0)
            {
                Debug.LogWarning("Initial not set for this category, call is ignored. Call SetCurveInitialPoint to create a new curve");
                return;
            }

            List<DoubleVector3> points = data.Data;
            DoubleVector3 last = points[points.Count - 1];
            DoubleVector3 c1 = DoubleVector3.Lerp(last, toPoint.ToDoubleVector3(), 1f / 3f);
            DoubleVector3 c2 = DoubleVector3.Lerp(last, toPoint.ToDoubleVector3(), 2f / 3f);
            AddCurveToCategory(category, c1.ToDoubleVector2(), c2.ToDoubleVector2(), toPoint, pointSize);
        }

        public void AddCurveToCategory(string category, DoubleVector2 controlPointA, DoubleVector2 controlPointB , DoubleVector2 toPoint,double pointSize = -1f)
        {
            if (!IsExtended && pointSize >= 0f)
            {
                Debug.LogError("Point sizes are not supported in the lite version of Graph and Chart");
                pointSize = -1f;
            }
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            if (data.IsBezierCurve == false)
            {
                Debug.LogWarning("Category is not Bezier curve. use AddPointToCategory instead ");
                return;
            }

            if (data.Data.Count == 0)
            {
                Debug.LogWarning("Initial not set for this category, call is ignored. Call SetCurveInitialPoint to create a new curve");
                return;
            }

            List<DoubleVector3> points = data.Data;
            if (points.Count > 0 && points[points.Count - 1].x > toPoint.x)
            {
                Debug.LogWarning("Curves must be added sequentialy according to the x axis. toPoint.x is smaller then the previous point x value");
                return;
            }
            data.Regenerate = true;
            DoubleVector2 min = min3(controlPointA, controlPointB, toPoint);
            DoubleVector2 max = max3(controlPointA, controlPointB, toPoint);

            if (data.MaxRadius.HasValue == false || data.MaxRadius.Value < pointSize)
                data.MaxRadius = pointSize;
            if (data.MaxX.HasValue == false || data.MaxX.Value < max.x)
                data.MaxX = max.x;
            if (data.MinX.HasValue == false || data.MinX.Value > min.x)
                data.MinX = min.x;
            if (data.MaxY.HasValue == false || data.MaxY.Value < max.y)
                data.MaxY = max.y;
            if (data.MinY.HasValue == false || data.MinY.Value > min.y)
                data.MinY = min.y;

            points.Add(controlPointA.ToDoubleVector3());
            points.Add(controlPointB.ToDoubleVector3());
            points.Add(new DoubleVector3(toPoint.x,toPoint.y,pointSize));
            
            RaiseDataChanged();
        }
        
        /// <summary>
        /// adds a point to the category. The points are sorted by their x value automatically
        /// </summary>
        /// <param name="category"></param>
        /// <param name="point"></param>
        public void AddPointToCategory(string category, double x,double y, double pointSize = -1f)
        {
            
            if (!IsExtended && pointSize >= 0f)
            {
                Debug.LogError("Point sizes are not supported in the lite version of Graph and Chart");
                pointSize = -1f;
            }
            if (mData.ContainsKey(category) == false)
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
                return;
            }

            CategoryData data = (CategoryData)mData[category];
            if(data.IsBezierCurve == true)
            {
                Debug.LogWarning("Category is Bezier curve. use AddCurveToCategory instead ");
                return;
            }

            DoubleVector3 point = new DoubleVector3(x, y,pointSize);
            
            List<DoubleVector3> points = data.Data;

            if (data.MaxRadius.HasValue == false || data.MaxRadius.Value < pointSize)
                data.MaxRadius = pointSize;
            if (data.MaxX.HasValue == false || data.MaxX.Value < point.x)
                data.MaxX = point.x;
            if (data.MinX.HasValue == false || data.MinX.Value > point.x)
                data.MinX = point.x;
            if (data.MaxY.HasValue == false || data.MaxY.Value < point.y)
                data.MaxY = point.y;
            if (data.MinY.HasValue == false || data.MinY.Value > point.y)
                data.MinY = point.y;

            if (points.Count > 0)
            {
                if (data.AllowNonFunctions || points[points.Count - 1].x <= point.x)
                {
                    points.Add(point);
                    RaiseDataChanged();
                    return;
                }
            }
         //   points.Add(point);
            int search = points.BinarySearch(point, mComparer);
            if (search < 0)
                search = ~search;
            points.Insert(search, point);               
            RaiseDataChanged();
        }
        
        
        double IInternalGraphData.GetMaxValue(int axis, bool dataValue)
        {
            return GetMaxValue(axis, dataValue);
        }

        double IInternalGraphData.GetMinValue(int axis, bool dataValue) 
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
                AddInnerCategoryGraph(name,cat.LinePrefab, cat.Material, cat.LineThickness, cat.LineTiling,cat.FillPrefab, cat.InnerFill,cat.StetchFill,cat.DotPrefab,cat.PointMaterial,cat.PointSize,cat.Depth,cat.IsBezierCurve,cat.SegmentsPerCurve,cat.InitialData);
                Set2DCategoryPrefabs(name, cat.LineHoverPrefab, cat.PointHoverPrefab);
                CategoryData data = (CategoryData)mData[name];
                data.AllowNonFunctions = cat.AllowNonFunctionsBeta;
                data.ViewOrder = i;
                if (data.Data == null)
                    data.Data = new List<DoubleVector3>();
                else
                    data.Data.Clear();

                if (cat.InitialData != null)
                    SetInitialData(name, cat.InitialData, cat.IsBezierCurve);

//                if (cat.data != null)
//                    data.Data.AddRange(cat.data);
                data.MaxX = cat.MaxX; 
                data.MaxY = cat.MaxY;
                data.MinX = cat.MinX;
                data.MinY = cat.MinY;
                data.MaxRadius = cat.MaxRadius;
            }
            mSuspendEvents = false;
        }

        public override void OnBeforeSerialize()
        {
            List<SerializedCategory> serialized = new List<SerializedCategory>();
            foreach (KeyValuePair<string, CategoryData> pair in mData.Select(x=>new KeyValuePair<string, CategoryData>(x.Key,(CategoryData)x.Value)))
            {
                SerializedCategory cat = new SerializedCategory();
                cat.Name = pair.Key;
                cat.MaxX = pair.Value.MaxX;
                cat.MinX = pair.Value.MinX;
                cat.MaxY = pair.Value.MaxY;
                cat.MaxRadius = pair.Value.MaxRadius;
                cat.MinY = pair.Value.MinY;
                cat.LineThickness = pair.Value.LineThickness;
                cat.StetchFill = pair.Value.StetchFill;
                cat.Material = pair.Value.LineMaterial;
                cat.LineHoverPrefab = pair.Value.LineHoverPrefab;
                cat.PointHoverPrefab = pair.Value.PointHoverPrefab;
                cat.LineTiling = pair.Value.LineTiling;
                cat.InnerFill = pair.Value.FillMaterial;
                cat.data = pair.Value.Data.ToArray();
                cat.PointSize = pair.Value.PointSize;
                cat.IsBezierCurve = pair.Value.IsBezierCurve;
                cat.AllowNonFunctionsBeta = pair.Value.AllowNonFunctions;
                cat.SegmentsPerCurve = pair.Value.SegmentsPerCurve;
                cat.PointMaterial = pair.Value.PointMaterial;
                cat.LinePrefab = pair.Value.LinePrefab;
                cat.Depth = pair.Value.Depth;
                cat.DotPrefab = pair.Value.DotPrefab;
                cat.FillPrefab = pair.Value.FillPrefab;
                cat.ViewOrder = pair.Value.ViewOrder;
                cat.InitialData = pair.Value.initialData;
                if (cat.Depth < 0)
                    cat.Depth = 0f;
                serialized.Add(cat);
            }
            mSerializedData = serialized.OrderBy(x=>x.ViewOrder).ToArray();
        }

        protected override void AppendDatum(string category, MixedSeriesGenericValue value)
        {
            throw new NotImplementedException();
        }

        protected override void InnerClearCategory(string category)
        {
            throw new NotImplementedException();
        }

        protected override bool AddCategory(string category, BaseScrollableCategoryData data)
        {
            throw new NotImplementedException();
        }

        protected override void AppendDatum(string category, IList<MixedSeriesGenericValue> value)
        {
            throw new NotImplementedException();
        }

        public override BaseScrollableCategoryData GetDefaultCategory()
        {
            throw new NotImplementedException();
        }

        int IInternalGraphData.TotalCategories
        {
            get { return mData.Count; }
        }

        IEnumerable<CategoryData> IInternalGraphData.Categories
        {
            get
            {
                return mData.Values.Select(x=>(CategoryData)x).OrderBy(x=>x.ViewOrder);
            }
        }
    }
}
