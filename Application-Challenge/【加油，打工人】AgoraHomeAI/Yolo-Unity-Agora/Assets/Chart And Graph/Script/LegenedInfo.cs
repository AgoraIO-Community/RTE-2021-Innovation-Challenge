#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// Contains data about a chart legend
    /// </summary>
    public class LegenedData
    {
        /// <summary>
        /// Each item has a name and material
        /// </summary>
        public class LegenedItem
        {
            public string Name;
            public Material Material;
        }

        /// <summary>
        /// list of categories each with a name and material
        /// </summary>
        List<LegenedItem> mItems = new List<LegenedItem>();

        public void AddLegenedItem(LegenedItem item)
        {
            mItems.Add(item);
        }

        /// <summary>
        /// returns that data for this legend item
        /// </summary>
        public IEnumerable<LegenedItem> Items
        {
            get { return mItems; }
        }
    }
}
