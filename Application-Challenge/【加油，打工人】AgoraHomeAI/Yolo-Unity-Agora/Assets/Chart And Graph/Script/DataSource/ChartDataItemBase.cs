#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    /// base class for data items (rows and columns) in a data source
    /// </summary>
    internal abstract class ChartDataItemBase : IDataItem
    {
        private string mName;
        private string mPrevName;
        public string Name
        {
            get
            {
                return mName;
            }
            set
            {
                mPrevName = mName;
                mName = value;
                if (NameChanged != null)
                    NameChanged(mPrevName, this);
            }
        }
        public object UserData { get; set; }
        /// <summary>
        /// The material for this data source item
        /// </summary>
        public ChartDynamicMaterial Material { get; set; }

        public ChartDataItemBase(String name)
        {
            mName = name;
        }
        public event Action<string, IDataItem> NameChanged;

        public void CancelNameChange()
        {
            mName = mPrevName;
        }
    }
}
