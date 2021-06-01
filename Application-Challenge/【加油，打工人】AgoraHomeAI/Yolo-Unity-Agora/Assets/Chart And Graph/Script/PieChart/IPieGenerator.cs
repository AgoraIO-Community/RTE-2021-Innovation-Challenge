#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    /// <summary>
    /// base interface for pie mesh generators
    /// </summary>
    public interface IPieGenerator
    {
        void Generate(float startAngle, float angleSpan, float radius, float innerRadius, int segments, float outerDepth,float innerDepth);
    }
}
