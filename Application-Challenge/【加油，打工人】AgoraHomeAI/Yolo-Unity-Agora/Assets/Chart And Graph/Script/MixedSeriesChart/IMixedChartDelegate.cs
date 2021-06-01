#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    interface IMixedChartDelegate
    {
        ScrollableAxisChart CreateCategoryView(Type t, ScrollableAxisChart prefab);
        void SetData(Dictionary<string, BaseScrollableCategoryData> data);
        void RealaseChart(ScrollableAxisChart chart);
        void DeactivateChart(ScrollableAxisChart chart);
        void ReactivateChart(ScrollableAxisChart chart);
    }
}
