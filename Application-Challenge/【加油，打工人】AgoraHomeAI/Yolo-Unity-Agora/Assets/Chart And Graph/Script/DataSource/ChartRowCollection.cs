#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.DataSource
{
    /// <summary>
    /// row collection in a data source
    /// </summary>
    internal class ChartRowCollection : ChartDataSourceBaseCollection<ChartDataRow>
    {
        protected override string ItemTypeName
        {
            get { return "group"; }
        }
    }
}
