#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public class PieInfo : MonoBehaviour
    {
        internal PieChart.PieObject pieObject { get; set; }
        public string Category { get { return pieObject.category; } }
    }
}
