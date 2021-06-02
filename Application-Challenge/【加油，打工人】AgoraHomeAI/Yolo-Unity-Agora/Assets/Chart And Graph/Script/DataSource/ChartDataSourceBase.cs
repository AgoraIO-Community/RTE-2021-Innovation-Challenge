#define Graph_And_Chart_PRO
using ChartAndGraph.Common;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    /// base class for all data sources
    /// </summary>
    internal abstract class ChartDataSourceBase
    {
        public class DataValueChangedEventArgs : EventArgs
        {
            public ChartItemIndex ItemIndex { get; private set; }
            public double OldValue { get; private set; }
            public double NewValue { get; private set; }
            public bool MinMaxChanged { get; private set; }
            public DataValueChangedEventArgs(int group,int category,double oldValue,double newValue,bool minMaxChanged)
            {
                ItemIndex = new ChartItemIndex(group, category);
                OldValue = oldValue;
                NewValue = newValue;
                MinMaxChanged = minMaxChanged;
            }
        }

        public event EventHandler DataStructureChanged;
        public event Action<string,int,string,int> ItemsReplaced;
        public event EventHandler<DataValueChangedEventArgs> DataValueChanged;

        protected void OnDataStructureChanged()
        {
            if (DataStructureChanged != null)
                DataStructureChanged(this, EventArgs.Empty);
        }

        protected void OnItemsReplaced(string first, int firstIndex, string second, int secondIndex)
        {
            if (ItemsReplaced != null)
                ItemsReplaced(first, firstIndex, second, secondIndex);
        }

        protected void OnDataValueChanged(DataValueChangedEventArgs data)
        {
            if (DataValueChanged != null)
                DataValueChanged(this, data);
        }

        public abstract double[,] getRawData();
        public abstract ChartColumnCollection Columns { get;}
        public abstract ChartRowCollection Rows { get; }
    }
}
