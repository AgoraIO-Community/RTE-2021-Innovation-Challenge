#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    interface IInternalBarData
    {
        ChartSparseDataSource InternalDataSource { get; }
        void Update();
        double GetMinValue();
        double GetMaxValue();
        void OnBeforeSerialize();
        void OnAfterDeserialize();
    }
}
