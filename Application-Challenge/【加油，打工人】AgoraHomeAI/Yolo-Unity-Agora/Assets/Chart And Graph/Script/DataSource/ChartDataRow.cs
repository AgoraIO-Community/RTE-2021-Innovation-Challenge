#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    /// a single data row in a data source
    /// </summary>
    internal class ChartDataRow : ChartDataItemBase
    { 
        public ChartDataRow(String name)
            :base(name)
        {

        }
    }
}
