#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    public abstract class BaseSlider
    {
        /// <summary>
        /// Duration of the sliding action
        /// </summary>
        public double Duration { get; set; }

        /// <summary>
        /// Start time of the sliding action
        /// </summary>
        public double StartTime { get; set; }

        /// <summary>
        /// The bottom right edge of the slider bounding box (use for detemining automatic view size and position)
        /// </summary>
        public abstract DoubleVector2 Max { get; }

        /// <summary>
        /// The top left edge of the slider bounding box (use for detemining automatic view size and position)
        /// </summary>
        public abstract DoubleVector2 Min { get; }

        public abstract string Category { get; }
        /// <summary>
        /// 
        /// </summary>
        public abstract int MinIndex { get; }
        /// <summary>
        /// returns true if the slider should be removed after this update. (meaning the slider is done)
        /// </summary>
        /// <returns></returns>
        public virtual bool Update()
        {
            return true;
        }

    }
}
