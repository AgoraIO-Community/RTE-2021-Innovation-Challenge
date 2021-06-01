#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [Serializable    ]
    public struct DoubleVector2
    {
        public double x, y;
        public DoubleVector2(Vector2 v)
        {
            x = v.x;
            y = v.y;
        }
        public Vector2 ToVector2()
        {
            return new Vector2((float)x, (float)y);
        }
        public DoubleVector3 ToDoubleVector3()
        {
            return new DoubleVector3(x, y);
        }
        public DoubleVector2(double _x, double _y)
        {
            x = _x;
            y = _y;
        }
    }
}
