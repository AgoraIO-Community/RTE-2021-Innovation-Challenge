#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    /// Collection of columns on a data source
    /// </summary>
    internal class ChartColumnCollection : ChartDataSourceBaseCollection<ChartDataColumn>
    {    
        protected override string ItemTypeName
        {
            get { return "category"; }
        }
    }
}
