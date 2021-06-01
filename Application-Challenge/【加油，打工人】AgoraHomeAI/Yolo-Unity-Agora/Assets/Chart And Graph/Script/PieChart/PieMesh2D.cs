#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// pie mesh creation class
    /// </summary>
    partial class PieMesh
    {

        public static void Generate2dPath(List<Vector2> path, List<int> tringles,float startAngle, float angleSpan, float radius, float innerRadius, int segments)
        {
            float segmentAngle = angleSpan / segments;
            float currentAngle = startAngle;

            path.Clear();
            for (int i = 0; i <= segments; i++)
            {
                path.Add(Vector2.zero);
                path.Add(Vector2.zero);
            }

            for (int i =0; i <= segments; i++)
            {
                currentAngle += segmentAngle;
                float cos = Mathf.Cos(currentAngle);
                float sin = Mathf.Sin(currentAngle);

                Vector3 innerVertex = new Vector3(cos * innerRadius, sin * innerRadius, 0f);
                Vector3 outerVertex = new Vector3(cos * radius, sin * radius, 0f);
                path[i] = innerVertex;
                path[path.Count - 1 - i] = outerVertex;
            }

            for (int i = 1; i <= segments; i++)
            {
                tringles.Add(i-1);
                tringles.Add(i);
                tringles.Add(path.Count - i);

                tringles.Add(i);
                tringles.Add(path.Count - i);
                tringles.Add(path.Count - 1 - i);
            }

        }

        public static void Generate2dMesh(IChartMesh mesh, float startAngle,float angleSpan,float radius,float innerRadius,int segments)
        {
            float segmentAngle = angleSpan / segments;
            float currentAngle = startAngle;
            float segmenUv = 1f / segments;
            float currentUv = 0f; 
            float cos = Mathf.Cos(currentAngle);
            float sin = Mathf.Sin(currentAngle);

            UIVertex prevInnerVertex = ChartCommon.CreateVertex(new Vector3(cos * innerRadius, sin * innerRadius, 0f), new Vector2(currentUv, 0f));
            UIVertex prevOuterVertex = ChartCommon.CreateVertex(new Vector3(cos * radius, sin * radius, 0f), new Vector2(currentUv, 1f));
            for (int i=1; i<segments+1; i++)
            {
                currentUv += segmenUv;
                currentAngle += segmentAngle;
                cos = Mathf.Cos(currentAngle);
                sin = Mathf.Sin(currentAngle);

                UIVertex innerVertex = ChartCommon.CreateVertex(new Vector3(cos * innerRadius, sin * innerRadius, 0f), new Vector2(currentUv, 0f));
                UIVertex outerVertex = ChartCommon.CreateVertex(new Vector3(cos * radius, sin * radius, 0f), new Vector2(currentUv, 1f));
                mesh.AddQuad(prevInnerVertex, innerVertex, prevOuterVertex, outerVertex);
                prevInnerVertex = innerVertex;
                prevOuterVertex = outerVertex;
            }
        }
    }
}
