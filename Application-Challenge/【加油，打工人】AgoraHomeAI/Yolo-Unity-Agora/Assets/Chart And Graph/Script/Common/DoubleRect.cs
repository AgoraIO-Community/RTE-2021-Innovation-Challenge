#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    public struct DoubleRect
    {
        public double X,Y,Width,Height;

        public DoubleRect(double x,double y,double width,double height)
        {
            X = x;
            Y = y;
            Width = width;
            Height = height;
        }

        public override string ToString()
        {
            return string.Format("x:{0},y:{1},w:{2},h:{3}", X, Y, Width, Height);
        }

        public DoubleVector3 min { get { return new DoubleVector3(X, Y); } }
        public DoubleVector3 max { get { return new DoubleVector3(X + Width, Y + Height); } }
    }
}
