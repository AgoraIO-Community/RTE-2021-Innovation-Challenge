#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    public interface IChartData
    {
        void Update();
        void OnAfterDeserialize();
        void OnBeforeSerialize();
    }
}
