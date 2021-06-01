#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    class PyramidMesh
    {
        public static void Generate2dMesh(UIVertex[] mesh, Vector2[] normals,float baseX1, float baseX2,float baseSize, float slopLeft, float slopeRight, float height,float startV,float endV)
        {
            float halfHeight = height * 0.5f;
            float halfWidth = baseSize * 0.5f;
            slopLeft = -Mathf.Clamp(slopLeft, -90, 90) +90;
            slopeRight = Mathf.Clamp(slopeRight ,- 90, 90) +90;

            float tanLeft = (1f/Mathf.Tan(slopLeft * Mathf.Deg2Rad)) * height;
            float tanRight =(1f/Mathf.Tan(slopeRight * Mathf.Deg2Rad)) * height;

            float leftPos = baseX1 + tanLeft;
            float rightPos = baseX2 + tanRight;
            leftPos = Mathf.Clamp(leftPos, 0,baseSize);
            rightPos = Mathf.Clamp(rightPos, 0, baseSize);


            if (leftPos > rightPos)
                leftPos = rightPos = Mathf.Clamp(Mathf.Lerp(leftPos, rightPos, 0.5f), 0, baseSize);

            UIVertex v1 = ChartCommon.CreateVertex(new Vector3(baseX1 - halfWidth, -halfHeight), new Vector2(0f, startV));
            UIVertex v2 = ChartCommon.CreateVertex(new Vector3(baseX2 - halfWidth, -halfHeight), new Vector2(1f, startV));
            UIVertex v3 = ChartCommon.CreateVertex(new Vector3(leftPos - halfWidth, halfHeight), new Vector2(0f, endV));
            UIVertex v4 = ChartCommon.CreateVertex(new Vector3(rightPos - halfWidth, halfHeight), new Vector2(1f, endV));

            normals[0] = ((Vector2.up + ChartCommon.Perpendicular(v3.position - v1.position).normalized) * 0.5f).normalized;
            normals[1] = ((Vector2.up + ChartCommon.Perpendicular(v2.position - v4.position).normalized) * 0.5f).normalized;
            normals[2] = ((Vector2.down + ChartCommon.Perpendicular(v3.position - v1.position).normalized) * 0.5f).normalized;
            normals[3] = ((Vector2.down + ChartCommon.Perpendicular(v2.position - v4.position).normalized) * 0.5f).normalized;

            mesh[0] = v1;
            mesh[1] = v2;
            mesh[2] = v3;
            mesh[3] = v4;
        }
    }
}
