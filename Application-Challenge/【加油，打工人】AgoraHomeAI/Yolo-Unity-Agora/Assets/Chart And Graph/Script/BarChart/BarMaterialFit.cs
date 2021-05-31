#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    public enum BarMaterialFit
    {
        /// <summary>
        /// Stretch the material along the bar. 
        /// </summary>
        Stretch,
        /// <summary>
        /// If the bar is small , the material will be cut to match it's size. If the bar is at maximum size , all the material will be visible
        /// </summary>
        Trim
    }
}
