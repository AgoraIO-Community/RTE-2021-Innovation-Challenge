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
    /// <summary>
    /// Holds and manages chart data in a table like fashion, The data in this class can be used with pie and bar chart types.
    /// </summary>
    internal class ChartSparseDataSource : ChartDataSourceBase
    {
        struct KeyElement
        {
            public KeyElement(ChartDataRow row, ChartDataColumn column) : this()
            {
                Row = row;
                Column = column;
            }
            public bool IsIn(IDataItem item)
            {
                if (Row == item || Column == item)
                    return true;
                return false;
            }
            public bool IsInRow(ChartDataRow row)
            {
                return row == Row;
            }
            public bool IsInColumn(ChartDataColumn column)
            {
                return column == Column;
            }

            static public bool operator == (KeyElement a,KeyElement b)
            {
                return a.Equals(b);
            }
            static public bool operator !=(KeyElement a, KeyElement b)
            {
                return !a.Equals(b);
            }
            public override bool Equals(object obj)
            {
                if (obj is KeyElement)
                {
                    KeyElement elem = (KeyElement)obj;
                    if (elem.Row == Row && elem.Column == Column)
                        return true;
                    return false;
                }
                return base.Equals(obj);
            }

            public override int GetHashCode()
            {
                return Column.GetHashCode() ^ Row.GetHashCode();
            }

            public ChartDataRow Row { get; private set; }
            public ChartDataColumn Column { get; private set; }
        }

        public ChartSparseDataSource()
        {
            mColumns = new ChartColumnCollection();
            mRows = new ChartRowCollection();
            mColumns.NameChanged += MColumns_NameChanged;
            mRows.NameChanged += MRows_NameChanged;
            mColumns.OrderChanged += OrderChanged;
            mColumns.ItemsReplaced += MColumns_ItemsReplaced; ;
            mRows.OrderChanged += OrderChanged;
            mColumns.ItemRemoved += Columns_ItemRemoved;
            mRows.ItemRemoved += Rows_ItemRemoved;
        }

        private void MColumns_ItemsReplaced(string first, int firstIndex, string second, int secondIndex)
        {
            mRawData = null;    // data is invalidated.
            mChartDataToIndex.Clear();  // clear the index dictionary
            if (mSuspendEvents == false)
                OnItemsReplaced(first, firstIndex, second, secondIndex);
            else
                mFireEvent = true;

        }

        private void MRows_NameChanged(string arg1, IDataItem arg2)
        {
        }

        private void MColumns_NameChanged(string arg1, IDataItem arg2)
        {
        }

        Dictionary<KeyElement, double> mData = new Dictionary<KeyElement, double>();
        Dictionary<ChartDataItemBase, int> mChartDataToIndex = new Dictionary<ChartDataItemBase, int>();
        KeyValuePair<KeyElement, double>? mMaxValue = null, mMinValue = null;

        bool mFireEvent = false;
        bool mSuspendEvents = false;
        public bool SuspendEvents
        { 
            get
            {
                return mSuspendEvents; 
            }
            set
            {
                if (mSuspendEvents == true && value == false)
                {
                    if (mFireEvent)
                    {
                        OnDataStructureChanged();
                        mFireEvent = false;
                    }
                }
                mSuspendEvents = value;
            }
        }
        double[,] mRawData;
        public ChartColumnCollection mColumns;
        public ChartRowCollection mRows;

        public override ChartColumnCollection Columns
        {
            get
            {
                return mColumns;
            }
        }
        public override ChartRowCollection Rows
        {
            get
            {
                return mRows;
            }
        }
        void OrderChanged(object sender, EventArgs e)
        {
            // the structer of the table has changed
            mRawData = null;    // data is invalidated.
            mChartDataToIndex.Clear();  // clear the index dictionary
            if (mSuspendEvents == false)
                OnDataStructureChanged();
            else
                mFireEvent = true;
        }
        private void FindMinMaxValue()
        {
            mMaxValue = null;
            mMinValue = null;
            KeyValuePair<KeyElement, double>? maxValue = null;
            KeyValuePair<KeyElement, double>? minValue = null;

            foreach(KeyValuePair<KeyElement,double> pair in mData)
            {
                if (maxValue.HasValue == false || maxValue.Value.Value < pair.Value)
                    maxValue = pair;
                if (minValue.HasValue == false || minValue.Value.Value > pair.Value)
                    minValue = pair;
            }

            if (maxValue.HasValue)
                mMaxValue = maxValue.Value;
            if (minValue.HasValue)
                mMinValue = minValue.Value;
        }
        
        public void Clear()
        {
            Columns.Clear();
            Rows.Clear();
        }
        /// <summary>
        /// Raw data is prepared and held as long as the structed or order of the table has not changed
        /// </summary>
        private void PrepareRawData()
        {
            mChartDataToIndex.Clear();
            for (int i=0; i<mColumns.Count; i++)
            {
                ChartDataColumn column = mColumns[i];
                mChartDataToIndex.Add(column, i);
            }
            for(int i=0; i<mRows.Count; i++)
            {
                ChartDataRow row = mRows[i];
                mChartDataToIndex.Add(row, i);
            }
            mRawData = new double[mRows.Count,mColumns.Count];
            foreach(KeyValuePair<KeyElement,double> pair in mData)
            {
                int columnIndex;
                int rowIndex;
                if (mChartDataToIndex.TryGetValue(pair.Key.Column, out columnIndex) == false)
                    continue;
                if (mChartDataToIndex.TryGetValue(pair.Key.Row, out rowIndex) == false)
                    continue;
                mRawData[rowIndex, columnIndex] = pair.Value;
            }
            FindMinMaxValue();
        }

        private bool HasZeroItems()
        {
            int total = mRows.Count* mColumns.Count;
            if (mData.Count < total)
                return true;
            return false;
        }
        internal double? getRawMaxValue()
        {
            EnsureRawData();
            if (mMaxValue.HasValue == false)
                return null;
            double maxValue;
            if (mData.TryGetValue(mMaxValue.Value.Key, out maxValue) == false)
                return null;
            if (HasZeroItems() && maxValue < 0.0)
                return 0.0;
            return maxValue;
        }

        internal double? getRawMinValue()
        {
            EnsureRawData();
            if (mMinValue.HasValue == false)
                return null;
            double minValue;
            if (mData.TryGetValue(mMinValue.Value.Key, out minValue) == false)
                return null;
            if (HasZeroItems() && minValue > 0.0)
                return 0.0;
            return minValue;
        }

        private void ItemRemoved(IDataItem item)
        {
            Dictionary<KeyElement, double> newData = new Dictionary<KeyElement, double>();
            foreach(KeyValuePair<KeyElement,double> pair in mData)
            {
                if (pair.Key.IsIn(item) == false)
                    newData.Add(pair.Key, pair.Value);
            }
            mData = newData;
            mRawData = null;
            if (mSuspendEvents == false)
                OnDataStructureChanged();
            else
                mFireEvent = true;
        }

        void Rows_ItemRemoved(ChartDataRow obj)
        {
            ItemRemoved(obj);
        }

        void Columns_ItemRemoved(ChartDataColumn obj)
        {
            ItemRemoved(obj);
        }
        void EnsureRawData()
        {
            if (mRawData == null)   // dynamically check if the raw data should be regenerated
                PrepareRawData();
        }
        public override double[,] getRawData()
        {
            EnsureRawData();
            return mRawData;
        }
        private bool VerifyMinMaxValue(KeyElement element,double value)
        {
            bool findMinMax = false;
            bool res = false;
            if (mMaxValue.HasValue == false || mMaxValue.Value.Value < value)
            {
                mMaxValue = new KeyValuePair<KeyElement, double>(element, value);
                res = true;
            }
            else
            {
                if (mMaxValue.Value.Key == element)
                    findMinMax = true;
            }
            if (mMinValue.HasValue == false || mMinValue.Value.Value > value)
            {
                mMinValue = new KeyValuePair<KeyElement, double>(element, value);
                res = true;
            }
            else
            {
                if (mMinValue.Value.Key == element)
                    findMinMax = true;
            }
            if (findMinMax)
            {
                FindMinMaxValue();
                res = true;
            }
            return res;
        }
        private void InnerSetValue(ChartDataColumn column, ChartDataRow row, double amount)
        {
            EnsureRawData();
            int columnIndex, rowIndex;
            if (mChartDataToIndex.TryGetValue(column, out columnIndex) == false)
                throw new ChartException("value cannot be set"); // should never happen
            if (mChartDataToIndex.TryGetValue(row, out rowIndex) == false)
                throw new ChartException("value cannot be set"); // should never happen
            mRawData[rowIndex, columnIndex] = amount;

            KeyElement elem = new KeyElement(row, column);
            double oldValue;
            if (mData.TryGetValue(elem, out oldValue) == false)
                oldValue = 0.0;
            mData[elem] = amount;
            bool minMaxChanged = VerifyMinMaxValue(elem, amount);
            if (mSuspendEvents == false)
                OnDataValueChanged(new DataValueChangedEventArgs(rowIndex,columnIndex, 0.0,amount, minMaxChanged));
            else
                mFireEvent = true;
        }

        private double InnerGetValue(ChartDataColumn column,ChartDataRow row)
        {
            KeyElement elem = new KeyElement(row, column);
            double res;
            if (mData.TryGetValue(elem, out res) == false)
                return 0.0;
            return res;
        }

        /// <summary>
        /// Get the value currently for the specified column and row
        /// </summary>
        /// <param name="ColumnName">the name of the column</param>
        /// <param name="RowName"> the name of the row</param>
        /// <returns></returns>
        public double GetValue(String ColumnName,String RowName)
        {
            ChartDataColumn column = mColumns[ColumnName];
            ChartDataRow row = mRows[RowName];
            return InnerGetValue(column, row);
        }

        /// <summary>
        /// Get the value currently for the specified column and row
        /// </summary>
        /// <param name="ColumnName">the name of the column</param>
        /// <param name="rowIndex"> the index of the row</param>
        /// <returns></returns>
        public double GetValue(String ColumnName, int rowIndex)
        {
            ChartDataColumn column = mColumns[ColumnName];
            ChartDataRow row = mRows[rowIndex];
            return InnerGetValue(column, row);
        }
        /// <summary>
        /// Get the value currently for the specified column and row
        /// </summary>
        /// <param name="columnIndex">the index of the column</param>
        /// <param name="rowIndex"> the index of the row</param>
        /// <returns></returns>
        public double GetValue(int columnIndex, int rowIndex)
        {
            ChartDataColumn column = mColumns[columnIndex];
            ChartDataRow row = mRows[rowIndex];
            return InnerGetValue(column, row);
        }

        public void AddLabel(string columnName,int rowIndex,string text)
        {
        }
        /// <summary>
        /// Sets the value for the specified column and row
        /// </summary>
        /// <param name="ColumnName">the name of the column</param>
        /// <param name="RowName"> the name of the row</param>
        /// <param name="amount"> the new value</param>
        public void SetValue(string ColumnName, string RowName, double amount)
        {
            ChartDataColumn column = mColumns[ColumnName];
            ChartDataRow row = mRows[RowName];
            InnerSetValue(column, row, amount);
        }

        /// <summary>
        /// Sets the value for the specified column and row.
        /// </summary>
        /// <param name="ColumnName">the name of the column</param>
        /// <param name="rowIndex">the index of the row</param>
        /// <param name="amount"> the new value</param>
        public void SetValue(String ColumnName, int rowIndex, double amount)
        {
            ChartDataColumn column = mColumns[ColumnName];
            ChartDataRow row = mRows[rowIndex];
            InnerSetValue(column, row, amount);
        }

        /// <summary>
        /// Sets the value for the specified column and row.
        /// </summary>
        /// <param name="columnIndex">the index of the column</param>
        /// <param name="rowIndex">the index of the row</param>
        /// <param name="amount"> the new value</param>
        public void SetValue(int columnIndex, int rowIndex, double amount)
        {
            ChartDataColumn column = mColumns[columnIndex];
            ChartDataRow row = mRows[rowIndex];
            InnerSetValue(column, row, amount);
        }

    }
}
