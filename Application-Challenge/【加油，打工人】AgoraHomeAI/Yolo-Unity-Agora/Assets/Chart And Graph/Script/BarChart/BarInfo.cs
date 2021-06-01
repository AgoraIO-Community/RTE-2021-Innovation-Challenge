#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    class BarInfo : MonoBehaviour
    {
        internal BarChart.BarObject BarObject {get; set;}

        /// <summary>
        /// gets the item label for this bar, or null if no item label is assigned to it
        /// </summary>
        public GameObject ItemLabel
        {
            get
            {
                if (BarObject == null)
                    return null;
                if (BarObject.ItemLabel == null)
                    return null;
                return BarObject.ItemLabel.UIText;
            }
        }

        public GameObject CategoryLabel
        {
            get
            {
                if (BarObject == null)
                    return null;
                if (BarObject.CategoryLabel == null)
                    return null;
                return BarObject.CategoryLabel.UIText;
            }
        }

        /// <summary>
        /// gets the value of the bar
        /// </summary>
        public double Value
        {
            get
            {
                if (BarObject == null)
                    return 0.0;
                return BarObject.Value;
            }
        }
        /// <summary>
        /// gets the category of the bar
        /// </summary>
        public string Category
        {
            get
            {
                if (BarObject == null)
                    return "";
                return BarObject.Category;
            }
        }

        /// <summary>
        /// gets the group of the bar
        /// </summary>
        public string Group
        {
            get
            {
                if (BarObject == null)
                    return "";
                return BarObject.Group;
            }
        }
    }
}
