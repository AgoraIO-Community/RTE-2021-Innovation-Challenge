#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// Enumeration for the format of axis label data. 
    /// </summary>
    public enum AxisFormat
    {
        /// <summary>
        /// format the labels as floating point numbers
        /// </summary>
        Number,
        /// <summary>
        /// format the labels as time of day
        /// </summary>
        Time,
        /// <summary>
        /// format the labels as date only
        /// </summary>
        Date,
        /// <summary>
        /// format the labels as date and time
        /// </summary>
        DateTime,
    }
}
