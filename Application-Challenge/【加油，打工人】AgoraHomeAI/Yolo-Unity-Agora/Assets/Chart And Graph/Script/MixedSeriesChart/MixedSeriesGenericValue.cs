#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// represets a generic datum in a chart. This datum can be a point in a graph , a candle stick, a bar or anything else
    /// </summary>
    public struct MixedSeriesGenericValue
    {
        /// <summary>
        /// the name of the object. Used in item lables
        /// </summary>
        string name;
        /// <summary>
        /// the index of the item. This can be used for example in stacked bar chart. Objects with the same item value are the stacks of the same bar
        /// </summary>
        int index;
        /// <summary>
        /// the index of this item within a parent item. for example bar stacks of the same bar have different subIndex value
        /// </summary>
        int subIndex;
        /// <summary>
        /// defines points that can be used in different ways by different series
        /// </summary>
        double x, y;
        /// <summary>
        /// defines points that can be used in different ways by different series
        /// </summary>
        double x1, y1;
        /// <summary>
        /// defines a size that can be used in different ways by different series
        /// </summary>
        double size;
        /// <summary>
        /// defines the high end of a custom range that can used in diffrent ways by different series
        /// </summary>
        double high;
        /// <summary>
        /// defines the low end of a custom range that can be used in different ways by different series
        /// </summary>
        double low;
        /// <summary>
        /// this can contain just about anything. This value is passed to child prefabs, use this to create custom behaviors such as a chart within a chart etc
        /// </summary>
        object userData;
    }
}
