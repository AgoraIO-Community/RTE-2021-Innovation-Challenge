#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph.Common
{
    /// <summary>
    /// and index of a chart item. this include group and category data.
    /// </summary>
    struct ChartItemIndex
    {
        public ChartItemIndex(int group,int category) : this()
        {
            Group = group;
            Category = category;
        }

        public override bool Equals(object obj)
        {
            if (obj is ChartItemIndex)
            {
                ChartItemIndex index = (ChartItemIndex)obj;
                if (index.Group == Group && index.Category == Category)
                    return true;
            }
            return false;
        }

        public override int GetHashCode()
        {
            return Group.GetHashCode() ^ Category.GetHashCode();
        }
        public int Group { get; set; }
        public int Category { get; set; }
    }
}
