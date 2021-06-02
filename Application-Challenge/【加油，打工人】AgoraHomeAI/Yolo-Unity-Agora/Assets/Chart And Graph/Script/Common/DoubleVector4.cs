#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public class DoubleVector4
    {
        public double x, y, z, w;
        public DoubleVector4(double _x,double _y,double _z,double _w)
        {
            x = _x;
            y = _y;
            z = _z;
            w = _w;
        }

        public Vector4 ToVector4()
        {
            return new Vector4((float)x, (float)y, (float)z,(float)w);
        }

        public Vector3 ToVector3()
        {
            return new Vector3((float)x, (float)y, (float)z);
        }

        public DoubleVector3 ToDoubleVector3()
        {
            return new DoubleVector3(x, y, z);
        }
    }
}
