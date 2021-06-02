#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    interface IInternalRadarData 
    {
        ChartSparseDataSource InternalDataSource { get; }
        double GetMinValue();
        double GetMaxValue();
        event EventHandler InternalDataChanged;
        RadarChartData.CategoryData getCategoryData(int i);
    }
}
