#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// marks an objects as a chart item. chart items are managed by the chart implementation. this should not be used under normal cirtcumstance
    /// </summary>
    public class ChartItem : MonoBehaviour
    {
        public object TagData { get; set; }
    }
}
