#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    public partial class BarChart
    {
        partial void StackedStage1(ref float elevation, double prevIntep)
        {
            if (Stacked)
                elevation = (float)prevIntep * HeightRatio;
        }

        partial void StackedStage2(ref double prevIntep, double interp)
        {
            if (Stacked)
                prevIntep = interp;
        }
        partial void StackedStage3(ref double interp, double amount, double min, double total)
        {
            if (Stacked)
            {
                interp = (amount - min) / total;
            }
        }
    }
}
