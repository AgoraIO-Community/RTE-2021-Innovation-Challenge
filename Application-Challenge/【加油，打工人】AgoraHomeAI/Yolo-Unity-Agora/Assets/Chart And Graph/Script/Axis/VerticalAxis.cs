#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// defines a vertical axis for the chart componenet attached to the same object
    /// </summary>
    public class VerticalAxis : AxisBase
    {
        protected override Action<IInternalUse, bool> Assign
        {
            get
            {
                return (x, clear) =>
                {
                    if (clear)
                    {
                        if (x.VerticalAxis == this)
                            x.VerticalAxis = null;
                    }
                    else
                    {
                        if (x.VerticalAxis != this)
                            x.VerticalAxis = this;
                    }
                };
            }
        }
    }
}
