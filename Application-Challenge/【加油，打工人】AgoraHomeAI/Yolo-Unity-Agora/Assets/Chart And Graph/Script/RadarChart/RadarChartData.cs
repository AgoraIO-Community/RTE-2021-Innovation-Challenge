#define Graph_And_Chart_PRO
using ChartAndGraph.DataSource;
using ChartAndGraph.Exceptions;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [Serializable]
    public partial class RadarChartData : AbstractChartData, IInternalRadarData, IChartData
    {
        [Serializable]
        internal class CategoryData
        {
            public string Name;
            public float Seperation;
            public float Curve;
            public Material FillMaterial;
            public int FillSmoothing = 5;
            public PathGenerator LinePrefab;
            public ChartItemEffect LineHover;
            public float LineThickness;
            public Material LineMaterial;
            public GameObject PointPrefab;
            public ChartItemEffect PointHover;
            public float PointSize;
            public Material PointMaterial;
            [HideInInspector]
            public double MaxValue = -1f;
        }

        [Serializable]
        class DataEntry
        {
            public string GroupName;
            public string ColumnName;
            public double Amount;
        }

        private ChartSparseDataSource mDataSource = new ChartSparseDataSource();
        ChartSparseDataSource IInternalRadarData.InternalDataSource { get { return mDataSource; } }
        [SerializeField]
        private CategoryData[] mCategories = new CategoryData[0];
        [SerializeField]
        private string[] mGroups = new string[0];
        [SerializeField]
        private DataEntry[] mData = new DataEntry[0];

        private event EventHandler DataChanged;
        public event Action ProperyUpdated;

        protected void RaisePropertyUpdated()
        {
            if (ProperyUpdated != null)
                ProperyUpdated();
        }

        private void RaiseDataChanged()
        {
            if (mDataSource.SuspendEvents)
                return;
            if (DataChanged != null)
                DataChanged(this, EventArgs.Empty);
        }

        /// <summary>
        /// the number of categories in the data source
        /// </summary>
        public int TotalCategories { get { return mDataSource.Columns.Count; } }
        /// <summary>
        /// the number of groups in the data source
        /// </summary>
        public int TotalGroups { get { return mDataSource.Rows.Count; } }
        /// <summary>
        /// set this to true to automatically detect the highest radar value int the chart. This value will be used to scale all other radar entries
        /// </summary>
        [SerializeField]
        private bool automaticMaxValue = true;

        /// <summary>
        /// renames a category. throw an exception on error
        /// </summary>
        /// <param name="prevName"></param>
        /// <param name="newName"></param>
        public void RenameCategory(string prevName, string newName)
        {
            mDataSource.Columns[prevName].Name = newName;
            RaisePropertyUpdated();
        }

        /// <summary>
        /// renames a group. throw an exception on error
        /// </summary>
        /// <param name="prevName"></param>
        /// <param name="newName"></param>
        public void RenameGroup(string prevName, string newName)
        {
            mDataSource.Rows[prevName].Name = newName;
            RaisePropertyUpdated();
        }

        /// <summary>
        /// set this to true to automatically detect the highest radar value int the chart. This value will be used to scale all other radar entries
        /// </summary>
        public bool AutomaticMaxValue
        {
            get { return automaticMaxValue; }
            set
            {
                automaticMaxValue = value;
                RaisePropertyUpdated();
            }
        }
        /// <summary>
        /// Mannualy set the maximum value , all radar entires will be scaled based on this value. If a radar entry value is larger than this value it will be clamped
        /// Note: set AutomaticMaxValue to false in order to use this field
        /// </summary>
        [SerializeField]
        private double maxValue = 10;

        /// <summary>
        /// Mannualy set the maximum value , all radar entires will be scaled based on this value. If a radar entry value is larger than this value it will be clamped
        /// Note: set AutomaticMaxValue to false in order to use this field
        /// </summary>
        public double MaxValue
        {
            get { return maxValue; }
            set
            {
                maxValue = value;
                RaisePropertyUpdated();
            }
        }


        public void Update()
        {
            UpdateSliders();
        }

        /// <summary>
        /// call this to suspend chart redrawing while updating the data of the chart
        /// </summary>
        public void StartBatch()
        {
            mDataSource.SuspendEvents = true;
        }

        /// <summary>
        /// call this after StartBatch , this will apply all the changed made between the StartBatch call to this call
        /// </summary>
        public void EndBatch()
        {
            mDataSource.SuspendEvents = false;
        }

        public double GetMinValue()
        {
            return 0.0;
        }
        public bool HasGroup(string groupName)
        {
            try
            {
                var row = mDataSource.Rows[groupName];
                if (row != null)
                    return true;
            }
            catch
            {
            }
            return false;
        }
        public double GetMaxValue()
        {
            double max = MaxValue;
            double? rawMax = mDataSource.getRawMaxValue();

            if (AutomaticMaxValue && rawMax.HasValue)
                max = rawMax.Value;
            return max;

        }
        public bool HasCategory(string category)
        {
            try
            {
                var col =  mDataSource.Columns[category];
                if (col != null)
                    return true;
            }
            catch
            {
            }
            return false;
        }
        public string GetCategoryName(int index)
        {
            return mDataSource.Columns[index].Name;
        }
        public double GetCategoryMaxValue(int index)
        {
            return ((CategoryData)mDataSource.Columns[index].UserData).MaxValue;
        }
        public void SetCategoryMaxValue(string category,double maxValue)
        {
            ((CategoryData)mDataSource.Columns[category].UserData).MaxValue = maxValue;
        }
        public int GetGroupIndex(String name)
        {
            for (int i = 0; i < mGroups.Length; i++)
                if (mGroups[i] == name)
                    return i;
            throw new Exception("group does not exist");
        }
        public string GetGroupName(int index)
        {
            return mDataSource.Rows[index].Name;
        }
        /// <summary>
        /// used intenally , do not call
        /// </summary>
        /// <param name="cats"></param>
        public object[] StoreAllCategoriesinOrder()
        {
            return mCategories.ToArray();
        }
        public void OnBeforeSerialize()
        {
            int totalColumns = mDataSource.Columns.Count;
            mCategories = new CategoryData[totalColumns];
            for (int i = 0; i < totalColumns; i++)
            {
                CategoryData data = mDataSource.Columns[i].UserData as CategoryData;
                if (data.FillSmoothing < 1)
                    data.FillSmoothing = 1;
                data.Name = mDataSource.Columns[i].Name;
                mCategories[i] = data;
            }

            int totalRows = mDataSource.Rows.Count;
            mGroups = new string[totalRows];
            for (int i = 0; i < totalRows; i++)
                mGroups[i] = mDataSource.Rows[i].Name;

            double[,] raw = mDataSource.getRawData();
            int current = 0;
            mData = new DataEntry[raw.GetLength(0) * raw.GetLength(1)];
            for (int i = 0; i < raw.GetLength(0); ++i)
            {
                for (int j = 0; j < raw.GetLength(1); ++j)
                {
                    DataEntry entry = new DataEntry();
                    entry.ColumnName = mDataSource.Columns[j].Name;
                    entry.GroupName = mDataSource.Rows[i].Name;
                    entry.Amount = raw[i, j];
                    mData[current++] = entry;
                }
            }
        }

        public void OnAfterDeserialize()
        {
            mDataSource = new ChartSparseDataSource();
            mDataSource.SuspendEvents = true;
            mDataSource.Clear();
            if (mCategories == null)
                mCategories = new CategoryData[0];
            if (mGroups == null)
                mGroups = new string[0];
            if (mData == null)
                mData = new DataEntry[0];
            for (int i = 0; i < mCategories.Length; i++)
                AddCategory(mCategories[i].Name, mCategories[i]);
            for (int i = 0; i < mGroups.Length; i++)
                AddGroup(mGroups[i]);

            for (int i = 0; i < mData.Length; i++)
            {
                try
                {
                    DataEntry entry = mData[i];
                    mDataSource.SetValue(entry.ColumnName, entry.GroupName, entry.Amount);
                }
                catch (Exception)
                {

                }
            }

            mDataSource.SuspendEvents = false;
        }

        private void AddCategory(string name, CategoryData data)
        {
            ChartDataColumn column = new ChartDataColumn(name);
            column.UserData = data;
            mDataSource.mColumns.Add(column);
        }


        /// <summary>
        /// clears all groups in the radar chart
        /// </summary>
        public void ClearGroups()
        {
            string[] groups = mDataSource.Rows.Select(x => x.Name).ToArray();
            foreach (string s in groups)
            {
                RemoveGroup(s);
            }
        }

        /// <summary>
        /// clears alll categories in the radar chart
        /// </summary>
        public void ClearCategories()
        {
            string[] groups = mDataSource.Columns.Select(x => x.Name).ToArray();
            foreach (string s in groups)
            {
                RemoveCategory(s);
            }
        }

        /// <summary>
        /// Adds a new category to the radar chart. Each category has it's own materials and name.
        /// Note: you must also add groups to the radar data.
        /// Example: you can set the chart categories to be "Player 1","Player 2","Player 3" in order to compare player achivments
        public void AddCategory(string name, PathGenerator linePrefab, Material lineMaterial, float lineThickness, GameObject pointPrefab, Material pointMaterial, float pointSize, Material fillMaterial)
        {
            AddInnerCategory(name, null, lineMaterial, lineThickness, null, pointMaterial, pointSize, fillMaterial,5, 0f, 0f);
        }

        /// <summary>
        /// Adds a new category to the radar chart. Each category has it's own materials and name.
        /// Note: you must also add groups to the radar data.
        /// Example: you can set the chart categories to be "Player 1","Player 2","Player 3" in order to compare player achivments
        /// </summary>
        protected void AddInnerCategory(string name, PathGenerator linePrefab, Material lineMaterial, float lineThickness, GameObject pointPrefab, Material pointMaterial, float pointSize, Material fillMaterial,int fillSmoothing, float curve, float seperation)
        {
            ChartDataColumn column = new ChartDataColumn(name);
            CategoryData data = new CategoryData();
            data.LinePrefab = linePrefab;
            data.LineMaterial = lineMaterial;
            data.LineThickness = lineThickness;
            data.PointMaterial = pointMaterial;
            data.PointSize = pointSize;
            data.PointPrefab = pointPrefab;
            data.FillMaterial = fillMaterial;
            if (fillSmoothing < 1)
                fillSmoothing = 1;
            data.FillSmoothing = fillSmoothing;
            data.Curve = curve;
            data.Seperation = seperation;
            data.Name = name;
            column.UserData = data;
            mDataSource.mColumns.Add(column);
        }

        public void SetCategoryHover(string category, ChartItemEffect lineHover, ChartItemEffect pointHover)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.LineHover = lineHover;
                data.PointHover = pointHover;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }
 
        protected void SetInnerCategoryFill(string category, Material fillMaterial, int fillSmoothing)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.FillMaterial = fillMaterial;
                if (fillSmoothing < 1)
                    fillSmoothing = 1;
                data.FillSmoothing = fillSmoothing;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }

        protected void SetInnerCategoryLine(string category, PathGenerator linePrefab, Material lineMaterial, float lineThickness)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.LineMaterial = lineMaterial;
                data.LinePrefab = linePrefab;
                data.LineThickness = lineThickness;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }

        protected void SetInnerCategoryPoint(string category, GameObject prefab, Material pointMaterial, float pointSize)
        {
            try
            {
                CategoryData data = mDataSource.Columns[category].UserData as CategoryData;
                if (data == null)
                    throw new Exception("category not set"); // should never happen
                data.PointPrefab = prefab;
                data.PointMaterial = pointMaterial;
                data.PointSize = pointSize;
                RaiseDataChanged();
            }
            catch
            {
                Debug.LogWarning("Invalid category name. Make sure the category is present in the graph");
            }
        }
        public void SetCategoryFill(string category, Material fillMaterial)
        {
            SetInnerCategoryFill(category, fillMaterial,5);
        }
        public void SetCategoryLine(string category, Material lineMaterial, float lineThickness)
        {
            SetInnerCategoryLine(category, null, lineMaterial, lineThickness);
        }
        public void SetCategoryPoint(string category, Material pointMaterial, float pointSize)
        {
            SetInnerCategoryPoint(category, null, pointMaterial, pointSize);
        }
        /// <summary>
        /// sets the material for the specified category
        /// </summary>
        /// <param name="category">the name of the category</param>
        /// <param name="material">the material of the category</param>
        public void SetMaterial(string category, Material material)
        {
            SetMaterial(category, new ChartDynamicMaterial(material));
        }

        internal ChartDynamicMaterial GetMaterial(string category)
        {
            return mDataSource.Columns[category].Material;
        }
        /// <summary>
        /// sets the material for the specified category
        /// </summary>
        /// <param name="category">the name of the category</param>
        /// <param name="material">the dynamic material of the category. dynamic materials allows setting the material for different events</param>
        public void SetMaterial(string category, ChartDynamicMaterial material)
        {
            mDataSource.Columns[category].Material = material;
        }

        /// <summary>
        /// removes a category from the radar chart
        /// </summary>
        /// <param name="name">the name of the category to remove</param>
        public void RemoveCategory(string name)
        {
            ChartDataColumn column = mDataSource.Columns[name];
            RemoveSliderForCategory(name);
            mDataSource.Columns.Remove(column);
        }
        public void RestoreCategory(string category, object data)
        {
            var toSet = (CategoryData)data;
            CategoryData current = (CategoryData)(mDataSource.Columns[category].UserData);
            current.Curve = toSet.Curve;
            current.FillMaterial = toSet.FillMaterial;
            current.FillSmoothing = toSet.FillSmoothing;
            current.LineHover = toSet.LineHover;
            current.LineMaterial = toSet.LineMaterial;
            current.LinePrefab = toSet.LinePrefab;
            current.LineThickness= toSet.LineThickness;
            current.PointHover= toSet.PointHover;
            current.PointMaterial = toSet.PointMaterial;
            current.PointPrefab = toSet.PointPrefab;
            current.PointSize = toSet.PointSize;
            current.Seperation = toSet.Seperation;
            current.MaxValue = toSet.MaxValue;
            RaisePropertyUpdated();
        }
        /// <summary>
        /// removes a group from the radar chart
        /// </summary>
        /// <param name="name">the name of the group to remove</param>
        public void RemoveGroup(string name)
        {
            ChartDataRow row = mDataSource.Rows[name];
            RemoveSliderForGroup(name);
            mDataSource.Rows.Remove(row);
        }

        /// <summary>
        /// Adds a group to the radar chart. Each group holds a double value for each category.
        /// Note: you must also add at least one category to the radar chart
        /// Example: you can set the categories to "Player 1","Player 2","Player 3" and the groups to "Gold","Wood","Oil","Total"
        /// in order to compare the resources the players have gather during a level
        /// </summary>
        /// <param name="name"></param>
        public void AddGroup(string name)
        {
            mDataSource.Rows.Add(new ChartDataRow(name));
        }

        /// <summary>
        /// gets the value for the specified group and category
        /// </summary>
        /// <param name="category">the category name</param>
        /// <param name="group">the group name</param>
        /// <returns></returns>
        public double GetValue(string category, string group)
        {
            return mDataSource.GetValue(category, group);
        }

        public bool CheckAnimationEnded(float time, AnimationCurve curve)
        {
            if (curve.length == 0)
                return true;
            return time > curve.keys[curve.length - 1].time;
        }

        private void FixEaseFunction(AnimationCurve curve)
        {
            curve.postWrapMode = WrapMode.Once;
            curve.preWrapMode = WrapMode.Once;
        }

        private void SlideValue(string category, string group, double slideTo, float totalTime, AnimationCurve curve)
        {
            try
            {
                RemoveSlider(category, group);
                curve.postWrapMode = WrapMode.Once;
                curve.preWrapMode = WrapMode.Once;
                float time = 0f;
                if (curve.length > 0)
                    time = curve.keys[curve.length - 1].time;
                Slider s = new Slider();
                s.category = category;
                s.group = group;
                s.from = GetValue(category, group);
                s.to = slideTo;
                s.startTime = Time.time;
                s.timeScale = time / totalTime;
                s.totalTime = time;
                s.curve = curve;
                mSliders.Add(s);
            }
            catch (ChartException e)
            {
                Debug.LogWarning(e.Message);
            }
        }

        private void SlideValue(string category, string group, double slideTo, float time)
        {
            try
            {
                RemoveSlider(category, group);
                Slider s = new Slider();
                s.category = category;
                s.group = group;
                s.from = GetValue(category, group);
                s.to = slideTo;
                s.startTime = Time.time;
                s.totalTime = time;
                mSliders.Add(s);
            }
            catch (ChartException e)
            {
                Debug.LogWarning(e.Message);
            }
        }

        /// <summary>
        /// sets the value for the specified group and category
        /// </summary>
        /// <param name="category">the category name</param>
        /// <param name="group">the group name</param>
        /// <param name="amount">the value for the radar entry</param>
        public void SetValue(string category, string group, double amount)
        {
            RemoveSlider(category, group);
            SetValueInternal(category, group, amount);
        }

        protected override void SetValueInternal(string category, string group, double amount)
        {
            try
            {
                mDataSource.SetValue(category, group, amount);
            }
            catch (ChartException e)
            {
                Debug.LogWarning(e.Message);
            }
        }

        CategoryData IInternalRadarData.getCategoryData(int i)
        {
            return mDataSource.Columns[i].UserData as CategoryData;
        }

        event EventHandler IInternalRadarData.InternalDataChanged
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
    }
}
