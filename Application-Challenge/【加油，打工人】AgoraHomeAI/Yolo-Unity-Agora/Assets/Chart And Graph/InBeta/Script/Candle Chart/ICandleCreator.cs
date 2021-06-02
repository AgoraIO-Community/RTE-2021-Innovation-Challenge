#define Graph_And_Chart_PRO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    interface ICandleCreator
    {
        void Generate(CandleChart parent, Rect viewRect, IList<CandleChartData.CandleValue> value, CandleChartData.CandleSettings settings);
    }
}
