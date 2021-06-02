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
    public class PyramidData : AbstractChartData, IInternalPyramidData, IChartData
    {
        [Serializable]
        internal class CategoryData
        {
            public string Name;
            public ChartDynamicMaterial Materials;
            public String Title;
            public String Text;
            public Sprite Image;

            public float RightSlope;
            public float LeftSlope;
            public float HeightRatio;

            public float Alpha;
            public float Scale;
            public float ShiftX, ShiftY;
            public float PositionBlend;
            public Vector2 Shift { get { return new Vector2(ShiftX, ShiftY); } }
        }

        [Serializable]
        class DataEntry
        {
            public string GroupName;
            public string ColumnName;
            public double Amount;
        }

        public PyramidData()
        {
            mDataSource = new ChartSparseDataSource();
            if (mDataSource.Rows.Count == 0)
                mDataSource.Rows.Add(new DataSource.ChartDataRow("Pyramid"));
        }

        private ChartSparseDataSource mDataSource;
        ChartSparseDataSource IInternalPyramidData.InternalDataSource { get { return mDataSource; } }
        [SerializeField]
        private CategoryData[] mCategories = new CategoryData[0];
        [SerializeField]
        private string[] mGroups = new string[1] { "Pyramid" };
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
                    data.Alpha = ((CategoryData)userData).Alpha;
                    data.HeightRatio = ((CategoryData)userData).HeightRatio;
                    data.Image = ((CategoryData)userData).Image;
                    data.Title = ((CategoryData)userData).Title;
                    data.LeftSlope = ((CategoryData)userData).LeftSlope;
                    data.RightSlope = ((CategoryData)userData).RightSlope;
                    data.Scale = ((CategoryData)userData).Scale;
                    data.ShiftX = ((CategoryData)userData).ShiftX;
                    data.ShiftY = ((CategoryData)userData).ShiftY;
                    data.Text = ((CategoryData)userData).Text;
                    data.PositionBlend = ((CategoryData)userData).PositionBlend;
                }
                else
                {
                    data.Alpha = 1f;
                    data.HeightRatio = 1f;
                    data.Image = null;
                    data.Title = "";
                    data.LeftSlope = -0.5f;
                    data.RightSlope = -0.5f;
                    data.Scale = 1f;
                    data.ShiftX = 0f;
                    data.ShiftY = 0f;
                    data.Text = ((CategoryData)userData).Text;
                    data.PositionBlend = 1f;
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
        public event Action RealtimeProperyUpdated;

        protected void RaiseRealtimePropertyUpdated()
        {
            if (RealtimeProperyUpdated != null)
                RealtimeProperyUpdated();
        }

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
            current.Alpha = ((CategoryData)toSet).Alpha;
            current.HeightRatio = ((CategoryData)toSet).HeightRatio;
            current.Image = ((CategoryData)toSet).Image;
            current.Title = ((CategoryData)toSet).Title;
            current.LeftSlope = ((CategoryData)toSet).LeftSlope;
            current.RightSlope = ((CategoryData)toSet).RightSlope;
            current.Scale = ((CategoryData)toSet).Scale;
            current.ShiftX = ((CategoryData)toSet).ShiftX;
            current.ShiftY = ((CategoryData)toSet).ShiftY;
            current.Text = ((CategoryData)toSet).Text;
            current.PositionBlend = ((CategoryData)toSet).PositionBlend;

            current.Materials = toSet.Materials;
            mDataSource.Columns[category].Material = toSet.Materials;
            RaisePropertyUpdated();
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
                AddCategory(mCategories[i].Name, mCategories[i].Materials, mCategories[i].Title, mCategories[i].Text, mCategories[i].Image, mCategories[i].Alpha, mCategories[i].HeightRatio, mCategories[i].LeftSlope, mCategories[i].RightSlope, mCategories[i].PositionBlend, mCategories[i].Scale, mCategories[i].ShiftX, mCategories[i].ShiftY);
            //            for (int i = 0; i < mGroups.Length; i++)
            //                AddGroup(mGroups[i]);
            if (mDataSource.Rows.Count == 0)
                mDataSource.Rows.Add(new DataSource.ChartDataRow("Pyramid"));
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
        /// Adds a new category to the pyramid chart. Each category has it's own material and name. each category corresponds to one pyr slice
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the material of the category</param>
        public void AddCategory(string name, Material material, String title, String text, Sprite image)
        {
            AddCategory(name, new ChartDynamicMaterial(material),title,text,image);
        }


        /// <summary>
        /// Adds a new category to the pyramid chart. Each category has it's own material and name. each category corresponds to one pie slice
        /// </summary>
        /// <param name="name">the name of the category</param>
        /// <param name="material">the dynamic material of the category. dynamic materials allows setting the material for different events</param>
        public void AddCategory(string name, ChartDynamicMaterial material, String title, String text, Sprite image,float alpha = 1f,float heightRatio = 1f,float leftSlope = 45f,float rightSlope = 45f,float positionBlend = 1f,float scale = 1f, float shiftX = 0f,float shiftY =0f)
        {
            if (title == null)
                title = "";
            if (text == null)
                text = "";

            ChartDataColumn column = new ChartDataColumn(name);
            column.Material = material;
            CategoryData d = new CategoryData();
            d.Title = title;
            d.Text = text;
            d.Image = image;
            d.Alpha = alpha;
            d.HeightRatio = heightRatio;
            d.LeftSlope = leftSlope;
            d.RightSlope = rightSlope;
            d.PositionBlend = positionBlend;
            d.Scale = scale;
            d.ShiftX = shiftX;
            d.ShiftY = shiftY;
            column.UserData = d;
            mDataSource.mColumns.Add(column);
            SetValueInternal(name, "Pyramid", heightRatio);
        }

        public void SetCategoryInfo(string category,string title,string text,Sprite image)
        {
            if (title == null)
                title = "";
            if (text == null)
                text = "";
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.Text = text;
            data.Title = title;
            data.Image = image;
            RaisePropertyUpdated();
        }
        public void SetCategoryAlpha(string category, float alpha)
        {
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.Alpha = alpha;
            RaiseRealtimePropertyUpdated();
        }
        public void SetCategoryContentScale(string category,float scale)
        {
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.Scale = scale;
            RaiseRealtimePropertyUpdated();
        }
        public void SetCategoryOrientation(string category,float positionBlend,float shiftX ,float shiftY)
        {
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.PositionBlend = positionBlend;
            data.ShiftX = shiftX;
            data.ShiftY = shiftY;
            RaiseRealtimePropertyUpdated();
        }
        public void SetCategorySlope(string category,float leftSlop,float rightSlope)
        {
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.LeftSlope = leftSlop;
            data.RightSlope = rightSlope;
            RaiseRealtimePropertyUpdated();
        }
        public void SetCategoryHeightRatio(string category, float heightRatio)
        {
            var col = mDataSource.Columns[category];
            CategoryData data = col.UserData as CategoryData;
            data.HeightRatio = heightRatio;
            SetValueInternal(category, "Pyramid", heightRatio);
            RaiseRealtimePropertyUpdated();
        }

        /// <summary>
        /// clears the pyramid chart data
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
        /// removes a category from the pyramid chart
        /// </summary>
        /// <param name="name">the name of the category to remove</param>
        public void RemoveCategory(string name)
        {
            ChartDataColumn column = mDataSource.Columns[name];
            RemoveSlider(name, "Pyramid");
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
            return mDataSource.GetValue(category, "Pyramid");
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


        protected override void SetValueInternal(string column, string row, double value)
        {
            try
            {
                if(mDataSource.Rows.Count ==0 )
                    mDataSource.Rows.Add(new DataSource.ChartDataRow("Pyramid"));
                mDataSource.SetValue(column, "Pyramid", value);
            }
            catch (ChartException e)
            {
                Debug.LogWarning(e.Message);
            }
        }

    }
}
