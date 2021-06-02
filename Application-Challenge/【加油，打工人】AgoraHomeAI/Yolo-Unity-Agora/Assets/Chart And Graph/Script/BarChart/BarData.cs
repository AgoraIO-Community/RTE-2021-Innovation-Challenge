#define Graph_And_Chart_PRO
using System;
using ChartAndGraph.DataSource;
using UnityEngine;
using System.Collections.Generic;
using ChartAndGraph.Exceptions;
using System.Linq;

namespace ChartAndGraph
{
    /// <summary>
    /// data source for bar charts
    /// </summary>
    [Serializable]
    public class BarData : AbstractChartData,IInternalBarData, IChartData
    {

        [Serializable]
        class CategoryData
        {
            public string Name;
            public ChartDynamicMaterial Materials;

        }

        [Serializable]
        class DataEntry
        {
            public string GroupName;
            public string ColumnName;
            public double Amount;
        }

        private ChartSparseDataSource mDataSource = new ChartSparseDataSource();
        ChartSparseDataSource IInternalBarData.InternalDataSource { get { return mDataSource; } }
        [SerializeField]
        private CategoryData[] mCategories = new CategoryData[0];
        [SerializeField]
        private string[] mGroups = new string[0];
        [SerializeField]
        private DataEntry[] mData = new DataEntry[0];


        public event Action ProperyUpdated;

        protected void RaisePropertyUpdated()
        {
            if (ProperyUpdated != null)
                ProperyUpdated();
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
        /// set this to true to automatically detect the highest bar value int the chart. This value will be used to scale all other bars
        /// </summary>
        [SerializeField]
        private bool automaticMaxValue = true;

        public void Update()
        {
        }
        /// <summary>
        /// renames a category. throw an exception on error
        /// </summary>
        /// <param name="prevName"></param>
        /// <param name="newName"></param>
        public void RenameCategory(string prevName,string newName)
        {
            mDataSource.Columns[prevName].Name = newName;
            RaisePropertyUpdated();
        }

        /// <summary>
        /// renames a group. throw an exception on error
        /// </summary>
        /// <param name="prevName"></param>
        /// <param name="newName"></param>
        public void RenameGroup(string prevName,string newName)
        {
            mDataSource.Rows[prevName].Name = newName;
            RaisePropertyUpdated();
        }

        /// <summary>
        /// set this to true to automatically detect the highest bar value int the chart. This value will be used to scale all other bars
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
        /// Mannualy set the maximum value , all bars will be scaled based on this value. If a bar value is larger than this value it will be clamped
        /// Note: set AutomaticMaxValue to false in order to use this field
        /// </summary>
        [SerializeField]
        private double maxValue = 10;

        /// <summary>
        /// Mannualy set the maximum value , all bars will be scaled based on this value. If a bar value is larger than this value it will be clamped
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

        public bool HasCategory(string category)
        {
            try
            {
                var col = mDataSource.Columns[category];
                if (col != null)
                    return true;
            }
            catch
            {
            }
            return false;
        }

        /// <summary>
        /// set this to true to automatically detect the lowest bar value int the chart. This value will be used to scale all other bars
        /// </summary>
        [SerializeField]
        private bool automaticMinValue = false;

        /// <summary>
        /// set this to true to automatically detect the lowest bar value int the chart. This value will be used to scale all other bars
        /// </summary>
        public bool AutomaticMinValue
        {
            get { return automaticMinValue; }
            set
            {
                automaticMinValue = value;
                RaisePropertyUpdated();
            }
        }

        /// <summary>
        /// Mannualy set the minimum value , all bars will be scaled based on this value. If a bar value is larger than this value it will be clamped
        /// Note: set AutomaticMinValue to false in order to use this field
        /// </summary>
        [SerializeField]
        private double minValue = 0;

        /// <summary>
        /// Mannualy set the minimum value , all bars will be scaled based on this value. If a bar value is larger than this value it will be clamped
        /// Note: set AutomaticMinValue to false in order to use this field
        /// </summary>
        public double MinValue
        {
            get { return minValue; }
            set
            {
                minValue = value;
                RaisePropertyUpdated();
            }
        }

        void IInternalBarData.Update()
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
            double min = MinValue;
            double? rawMin = mDataSource.getRawMinValue();

            if (AutomaticMinValue && rawMin.HasValue)
                min = rawMin.Value;
            return min;
        }

        public double GetMaxValue()
        {
            double max = MaxValue;
            double? rawMax = mDataSource.getRawMaxValue();

            if (AutomaticMaxValue && rawMax.HasValue)
                max = rawMax.Value;
            return max;

        }

        public string GetCategoryName(int index)
        {
            return mDataSource.Columns[index].Name;
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
                CategoryData data = new CategoryData();
                data.Name = mDataSource.Columns[i].Name;
                data.Materials = mDataSource.Columns[i].Material;
                mCategories[i] = data;
            }
            int totalRows = mDataSource.Rows.Count;
            mGroups = new string[totalRows];
            for(int i=0; i< totalRows; i++)
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
            {
                AddCategory(mCategories[i].Name, mCategories[i].Materials);
            }
            for (int i = 0; i < mGroups.Length; i++)
                AddGroup(mGroups[i]);

            for(int i=0; i< mData.Length; i++)
            {
                try
                {
                    DataEntry entry = mData[i];
                    mDataSource.SetValue(entry.ColumnName, entry.GroupName, entry.Amount);
                }
                catch(Exception)
                {

                }
            }

            mDataSource.SuspendEvents = false;
        }

        /// <summary>
        /// Adds a new category to the bar chart. Each category has it's own material and name.
        /// Note: you must also have at least one group on the bar chart
        /// Example: you can set the categories to "Player 1","Player 2","Player 3" and the groups to "Gold","Wood","Oil","Total"
        /// in order to compare the resources the players have gather during a level
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the material of the category</param>
        public void AddCategory(string name, Material material)
        {
            AddCategory(name, new ChartDynamicMaterial(material));
        }

        /// <summary>
        /// clears all groups in the bar chart
        /// </summary>
        public void ClearGroups()
        {
            string[] groups = mDataSource.Rows.Select(x => x.Name).ToArray();
            foreach(string s in groups)
            {
                RemoveGroup(s);
            }
        }

        /// <summary>
        /// sets all values to the value parameted
        /// </summary>
        public void ClearValues(double value = 0.0)
        {
            string[] catgories = mDataSource.Columns.Select(x => x.Name).ToArray();
            string[] groups = mDataSource.Rows.Select(x => x.Name).ToArray();
            foreach (string g in groups)
            {
                foreach (string c in catgories)
                {
                    SetValue(c, g, value);
                }
            }
        }

        /// <summary>
        /// clears alll categories in the bar chart
        /// </summary>
        public void ClearCategories()
        {
            string[] catgories = mDataSource.Columns.Select(x => x.Name).ToArray();
            foreach (string s in catgories)
            {
                RemoveCategory(s);
            }
        }

        /// <summary>
        /// Adds a new category to the bar chart. Each category has it's own material and name.
        /// Note: you must also add groups to the bar data.
        /// Example: you can set the chart categories to be "Player 1","Player 2","Player 3" in order to compare player achivments
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the dynamic material of the category. dynamic materials allows setting the material for different events</param>
        public void AddCategory(string name, ChartDynamicMaterial material,int position)
        {
            ChartDataColumn column = new ChartDataColumn(name);
            column.Material = material;
            mDataSource.mColumns.Insert(position,column);
        }
        /// <summary>
        /// moves the category to a new position
        /// </summary>
        /// <param name="name"></param>
        /// <param name="newPosition"></param>
        public void MoveCategory(string name,int newPosition)
        {
            mDataSource.mColumns.Move(name, newPosition);
        }
        
        public void SwitchCategoryPositions(string firstCategory,string secondCategory)
        {
            mDataSource.mColumns.SwitchPositions(firstCategory, secondCategory);
        }
        /// <summary>
        /// Adds a new category to the bar chart. Each category has it's own material and name.
        /// Note: you must also add groups to the bar data.
        /// Example: you can set the chart categories to be "Player 1","Player 2","Player 3" in order to compare player achivments
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the dynamic material of the category. dynamic materials allows setting the material for different events</param>
        public void AddCategory(string name, ChartDynamicMaterial material)
        {
            ChartDataColumn column = new ChartDataColumn(name);
            column.Material = material;
            mDataSource.mColumns.Add(column);
        }

        public void SetCategoryIndex(string name,int index)
        {
            ChartDataColumn col = mDataSource.mColumns[name];
            double[] values = new double[TotalGroups];
            for(int i=0; i<TotalGroups; i++)
            {
                string g = GetGroupName(i);
                values[i] = GetValue(name, g);
            }
            
            mDataSource.Columns.Remove(col);
            mDataSource.Columns.Insert(index, col);
            for (int i = 0; i < TotalGroups; i++)
            {
                string g = GetGroupName(i);
                SetValue(name, g, values[i]);
            }
        }

        /// <summary>
        /// sets the material for the specified category
        /// </summary>
        /// <param name="category">the name of the category</param>
        /// <param name="material">the material of the category</param>
        public void SetMaterial(string category,Material material)
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
        public void SetMaterial(string category,ChartDynamicMaterial material)
        {
            mDataSource.Columns[category].Material = material;
            RaisePropertyUpdated();
        }

        /// <summary>
        /// removes a category from the bar chart
        /// </summary>
        /// <param name="name">the name of the category to remove</param>
        public void RemoveCategory(string name)
        {
            ChartDataColumn column = mDataSource.Columns[name];
            RemoveSliderForCategory(name);
            mDataSource.Columns.Remove(column);
        }

        /// <summary>
        /// removes a group from the bar chart
        /// </summary>
        /// <param name="name">the name of the group to remove</param>
        public void RemoveGroup(string name)
        {
            ChartDataRow row = mDataSource.Rows[name];
            RemoveSliderForGroup(name);
            mDataSource.Rows.Remove(row);
        }

        /// <summary>
        /// Adds a group to the bar chart. Each group holds a double value for each category.
        /// Note: you must also add at least one category to the bar chart
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
        public double GetValue(string category,string group)
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
        public void RestoreCategory(string name,object obj)
        {
            var cat = (CategoryData)obj;
            SetMaterial(name, cat.Materials);
        }
        public void SlideValue(string category, string group, double slideTo,float totalTime, AnimationCurve curve)
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

        public void SlideValue(string category,string group,double slideTo,float time)
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
        /// <param name="amount">the value fo the bar</param>
        public void SetValue(string category,string group,double amount)
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
    }
}
