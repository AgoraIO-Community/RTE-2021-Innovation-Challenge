#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    /// base interface for data items (columns and charts)
    /// </summary>
    public interface IDataItem
    {
        String Name { get; set; }
        void CancelNameChange();
        /// <summary>
        /// the first string argument contains the preivous name used by the item
        /// the second object argumnet is the item itself
        /// </summary>
        event Action<string, IDataItem> NameChanged;
    }
}
