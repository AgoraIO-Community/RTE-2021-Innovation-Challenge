#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// defines a horizontal axis for the chart componenet attached to the same object
    /// </summary>
    public class HorizontalAxis : AxisBase
    {
        protected override Action<IInternalUse, bool> Assign
        {
            get
            {
                return (x, clear) =>
                {
                    if (clear)
                    {
                        if (x.HorizontalAxis == this)
                            x.HorizontalAxis = null;
                    }
                    else
                    {
                        if (x.HorizontalAxis != this)
                            x.HorizontalAxis = this;
                    }
                };
            }
        }
    }
}
