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
    public class PieData : AbstractChartData, IInternalPieData , IChartData
    {
        [Serializable]
        internal class CategoryData
        {
            public string Name;
            public ChartDynamicMaterial Materials;
            [Range(0f, 1f)]
            public float RadiusScale =1f;
            [Range(0f, 1f)]
            public float DepthScale = 1f;
            [Range(0f, 1f)]
            public float DepthOffset = 0f;
        }

        [Serializable]
        class DataEntry
        {
            public string GroupName;
            public string ColumnName;
            public double Amount;
        }

        public PieData()
        {
            mDataSource = new ChartSparseDataSource();
            mDataSource.Rows.Add(new DataSource.ChartDataRow("Pie"));
        }

        private ChartSparseDataSource mDataSource;
        ChartSparseDataSource IInternalPieData.InternalDataSource { get { return mDataSource; } }
        [SerializeField]
        private CategoryData[] mCategories = new CategoryData[0];
        [SerializeField]
        private string[] mGroups = new string[1] { "Pie" };
        [SerializeField]
        private DataEntry[] mData = new DataEntry[0];

        public int TotalCategories { get { return mDataSource.Columns.Count; } }

        public void Update()
        {
            UpdateSliders();
        }

        public string GetCategoryName(int index)
        {
            return mDataSource.Columns[index].Name;
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
                object userData = mDataSource.Columns[i].UserData;
                if (userData != null && userData is CategoryData)
                {
                    data.RadiusScale = ((CategoryData)userData).RadiusScale;
                    data.DepthScale = ((CategoryData)userData).DepthScale;
                    data.DepthOffset = ((CategoryData)userData).DepthOffset; 
                }
                else
                {
                    data.RadiusScale = 1f;
                    data.DepthScale = 1f;
                    data.DepthOffset = 1f;
                }
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

        public event Action ProperyUpdated;

        protected void RaisePropertyUpdated()
        {
            if (ProperyUpdated != null)
                ProperyUpdated();
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
        /// rename a category. throws an exception on error
        /// </summary>
        /// <param name="prevName"></param>
        /// <param name="newName"></param>
        public void RenameCategory(string prevName, string newName)
        {
            mDataSource.Columns[prevName].Name = newName;
            RaisePropertyUpdated();
        }

        public object StoreCategory(string category)
        {
            CategoryData data = (CategoryData)(mDataSource.Columns[category].UserData);
            data.Materials = mDataSource.Columns[category].Material;
            return data;
        }
        
        public void RestoreCategory(string category, object data)
        {
            var toSet = (CategoryData)data;
            CategoryData current = (CategoryData)(mDataSource.Columns[category].UserData);
            current.DepthOffset = toSet.DepthOffset;
            current.Materials = toSet.Materials;
            current.RadiusScale = toSet.RadiusScale;
            current.DepthScale = toSet.DepthScale;
            mDataSource.Columns[category].Material = toSet.Materials;
            RaisePropertyUpdated();
        }

        public void SetCateogryParams(string category, float radiusScale, float depthScale, float depthOffset)
        {
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.RadiusScale = radiusScale;
            data.DepthScale = depthScale;
            data.DepthOffset = depthOffset;
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
                AddCategory(mCategories[i].Name, mCategories[i].Materials, mCategories[i].RadiusScale, mCategories[i].DepthScale,mCategories[i].DepthOffset);
            //            for (int i = 0; i < mGroups.Length; i++)
            //                AddGroup(mGroups[i]);
            mDataSource.Rows.Add(new DataSource.ChartDataRow("Pie"));
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

        private void AddGroup(string name)
        {
            mDataSource.Rows.Add(new ChartDataRow(name));
        }

        /// <summary>
        /// Adds a new category to the pie chart. Each category has it's own material and name. each category corresponds to one pie slice
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the material of the category</param>
        public void AddCategory(string name, Material material)
        {
            AddCategory(name, new ChartDynamicMaterial(material), 1f,1f,0f);
        }

        /// <summary>
        /// clears the pie chart data
        /// </summary>
        public void Clear()
        {
            string[] groups = mDataSource.Columns.Select(x => x.Name).ToArray();
            foreach (string s in groups)
            {
                RemoveCategory(s);
            }
        }
        /// <summary>
        /// Adds a new category to the pie chart. Each category has it's own material and name. each category corresponds to one pie slice
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the dynamic material of the category. dynamic materials allows setting the material for different events</param>
        public void AddCategory(string name, ChartDynamicMaterial material, float radiusScale,float depthScale,float depthOffset)
        {
            radiusScale = Mathf.Clamp(radiusScale, 0f, 1f);
            ChartDataColumn column = new ChartDataColumn(name);
            column.Material = material;
            CategoryData d = new CategoryData();
            d.RadiusScale = radiusScale;
            d.DepthScale = depthScale;
            d.DepthOffset = depthOffset;
            column.UserData = d;
            mDataSource.mColumns.Add(column);
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
        /// <param name="material">the dynamic material of the category. dynamic materials allow setting the material for different events</param>
        public void SetMaterial(string category, ChartDynamicMaterial material)
        {
            mDataSource.Columns[category].Material = material;
            RaisePropertyUpdated();
        }

        /// <summary>
        /// removes a category from the pie chart
        /// </summary>
        /// <param name="name">the name of the category to remove</param>
        public void RemoveCategory(string name)
        {
            ChartDataColumn column = mDataSource.Columns[name];
            RemoveSlider(name, "Pie");
            mDataSource.Columns.Remove(column);
        }

        /// <summary>
        /// gets the value for the specified category
        /// </summary>
        /// <param name="category">the category name</param>
        /// <param name="group">the group name</param>
        /// <returns></returns>
        public double GetValue(string category)
        {
            return mDataSource.GetValue(category, "Pie");
        }

        public bool CheckAnimationEnded(float time, AnimationCurve curve)
        {
            if (curve.length == 0)
                return true;
            return time > curve.keys[curve.length - 1].time;
        }


        /// <summary>
        /// used intenally , do not call
        /// </summary>
        /// <param name="cats"></param>
        public object[] StoreAllCategoriesinOrder()
        {
            return mCategories.ToArray();
        }

        private void FixEaseFunction(AnimationCurve curve)
        {
            curve.postWrapMode = WrapMode.Once;
            curve.preWrapMode = WrapMode.Once;
        }

        public void SlideValue(string category, double slideTo, float timeScale, AnimationCurve curve)
        {
            try
            {
                RemoveSlider(category, "Pie");
                string group = "Pie";
                curve.postWrapMode = WrapMode.Once;
                curve.preWrapMode = WrapMode.Once;
                float time = 0f;
                if (curve.length > 0)
                    time = curve.keys[curve.length - 1].time;
                Slider s = new Slider();
                s.category = category;
                s.group = group;
                s.from = GetValue(category);
                s.to = slideTo;
                s.startTime = Time.time;
                s.timeScale = timeScale;
                s.totalTime = time;
                s.curve = curve;
                mSliders.Add(s);
            }
            catch (ChartException e)
            {
                Debug.LogWarning(e.Message);
            }
        }
        public void SlideValue(string category, double slideTo, float time)
        {
            try
            {
                RemoveSlider(category, "Pie");
                string group = "Pie";
                Slider s = new Slider();
                s.category = category;
                s.group = group;
                s.from = GetValue(category);
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
        /// sets the value for the specified category
        /// </summary>
        /// <param name="category">the category name</param>
        /// <param name="amount">the value of the pie item</param>
        public void SetValue(string category, double amount)
        {
            RemoveSlider(category, "Pie");
            SetValueInternal(category, "Pie", amount);
        }

        protected override void SetValueInternal(string column, string row, double value)
        {
            try
            {
                mDataSource.SetValue(column, "Pie", value);
            }
            catch(ChartException e)
            {
                Debug.LogWarning(e.Message);
            }
        }

    }
}
