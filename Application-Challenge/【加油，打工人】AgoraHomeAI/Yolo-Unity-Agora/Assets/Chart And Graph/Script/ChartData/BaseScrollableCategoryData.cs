#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public class BaseScrollableCategoryData
    {
        public string Name;
        public bool Enabled = true;
        [HideInInspector]
        public int ViewOrder = -1;
        public double? MaxX, MaxY, MinX, MinY, MaxRadius;
    }
}
