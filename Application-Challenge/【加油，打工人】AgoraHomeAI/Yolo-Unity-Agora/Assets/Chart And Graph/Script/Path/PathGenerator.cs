#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public abstract partial class PathGenerator : MonoBehaviour
    {
        public abstract void Generator(Vector3[] path, float thickness, bool closed);
        public abstract void Clear();
    }
}
