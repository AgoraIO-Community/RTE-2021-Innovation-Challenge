#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{

    public interface IPyramidGenerator
    {
        GameObject ContainerObject { get; }
        void SetParams(float baseX1, float baseX2, float baseSize, float slopLeft, float slopeRight, float height,float inset,float startV,float endV);
        Vector3 GetTextPosition(PyramidChart.JustificationType justification,bool isBase);
        void GetUpperBase(out float baseX1, out float baseX2);
        void Generate();
        void ApplyInfo(string title, string text, Sprite image, float scale);
        void SetAlpha(float alpha);
    }
}
