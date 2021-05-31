#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [Serializable]
    public struct ChartMagin
    {
        [SerializeField]
        private float left, top, right, bottom;

        public float Left { get { return left; } }
        public float Right { get { return right; } }
        public float Top { get { return top; } }
        public float Bottom { get { return bottom; } }

        public ChartMagin(float leftMargin, float topMargin, float rightMargin, float bottomMargin)
        {
            left = leftMargin;
            right = rightMargin;
            top = topMargin;
            bottom = bottomMargin;
        }


    }
}
