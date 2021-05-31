#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    ///  a single column in a data source
    /// </summary>
    internal class ChartDataColumn : ChartDataItemBase
    {
        public ChartDataColumn(String name)
            :base(name)
        {

        }
    }
}
