#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph.Axis
{
    /// <summary>
    /// Axis generator functionallity that is not dependant on the diminetion of the chart
    /// </summary>
    public interface IAxisGenerator
    {
        UnityEngine.Object This();
        GameObject GetGameObject();
        void FixLabels(AnyChart parent);
        void SetAxis(double scrollOffset,AnyChart parent, AxisBase axis, ChartOrientation axisOrientation, bool isSubDivisions);
    }
}
