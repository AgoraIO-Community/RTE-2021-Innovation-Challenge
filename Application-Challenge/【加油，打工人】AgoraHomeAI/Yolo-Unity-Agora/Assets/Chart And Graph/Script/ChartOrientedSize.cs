#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// A size vector this depends on the chart orientation.
    /// </summary>
    [Serializable]
    public class ChartOrientedSize
    {

        public ChartOrientedSize()
        {

        }
        public ChartOrientedSize(float breadth)
        {
            Breadth = breadth;
            Depth = 0f;
        }
        public ChartOrientedSize(float breadth,float depth)
        {
            Breadth = breadth;
            Depth = depth;
        }

        /// <summary>
        /// If the orientation is horizontal , this is the size along the X axis. if the orientation is vertical this is the size along the Y axis
        /// </summary>
        public float Breadth;
        /// <summary>
        /// the size along the Z axis
        /// </summary>
        public float Depth;

        public override bool Equals(object obj)
        {
            if(obj is ChartOrientedSize)
            {
                ChartOrientedSize cast = (ChartOrientedSize)obj;
                if (cast.Depth == Depth && cast.Breadth == Breadth)
                    return true;
                return false;
            }
            return false;
        }
        public override int GetHashCode()
        {
            return Breadth.GetHashCode() ^ Depth.GetHashCode();
        }
    }
}
