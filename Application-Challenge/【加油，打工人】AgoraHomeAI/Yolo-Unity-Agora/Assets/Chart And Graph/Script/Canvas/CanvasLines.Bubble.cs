#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public partial class CanvasLines
    {

        partial void ProcesssPoint(ref Vector4 point, ref float halfSize)
        {
            if (point.w >= 0f)
                halfSize = point.w * 0.5f;
        }

    }
}
