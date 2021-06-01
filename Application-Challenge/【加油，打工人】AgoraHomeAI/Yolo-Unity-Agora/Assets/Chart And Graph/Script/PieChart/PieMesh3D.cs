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
        public static void Generate3dMesh(WorldSpaceChartMesh mesh, float startAngle, float angleSpan, float radius, float innerRadius, int segments,float outerDepth,float innerDepth)
        {
            float maxDepth = Mathf.Max(outerDepth, innerDepth);
            float bottom = maxDepth * 0.5f;
            float innerUp = bottom - innerDepth;
            float outerUp = bottom - outerDepth;
            //float halfDepth = maxDepth * 0.5f;
            float segmentAngle = angleSpan / segments;
            float currentAngle = startAngle;
            float segmenUv = 1f / segments;
            float currentUv = 0f;
            float cos = Mathf.Cos(currentAngle);
            float sin = Mathf.Sin(currentAngle);

            UIVertex innerV = ChartCommon.CreateVertex(new Vector3(cos * innerRadius, sin * innerRadius, innerUp), new Vector2(currentUv, 0f));
            UIVertex outerV = ChartCommon.CreateVertex(new Vector3(cos * radius, sin * radius, outerUp), new Vector2(currentUv, 1f));

            int currentInner = mesh.AddVertex(innerV);
            int currentOuter = mesh.AddVertex(outerV);
            int prevInnerVertex = mesh.AddVertex(innerV);
            int prevOuterVertex = mesh.AddVertex(outerV);
            int prevOpeningVertex = mesh.AddVertex(innerV);
            int prevClosingVertex = mesh.AddVertex(outerV);
            innerV.position.z = bottom;
            outerV.position.z = bottom;

            int currentInnerDeep = mesh.AddVertex(innerV);
            int currentOuterDeep = mesh.AddVertex(outerV);
            int prevInnerVertexDeep = mesh.AddVertex(innerV);
            int prevOuterVertexDeep = mesh.AddVertex(outerV);

            mesh.AddTringle(currentInner, currentOuter,currentOuterDeep);
            mesh.AddTringle(currentOuterDeep, currentInnerDeep, currentInner);

            int prevOpeningVertexDeep = mesh.AddVertex(innerV);
            int prevClosingVertexDeep = mesh.AddVertex(outerV);

            for (int i = 1; i <= segments; i++)
            {
                currentUv += segmenUv;
                currentAngle += segmentAngle;
                cos = Mathf.Cos(currentAngle);
                sin = Mathf.Sin(currentAngle);

                UIVertex innerVertex = ChartCommon.CreateVertex(new Vector3(cos * innerRadius, sin * innerRadius, innerUp), new Vector2(currentUv, 0f));
                UIVertex outerVertex = ChartCommon.CreateVertex(new Vector3(cos * radius, sin * radius, outerUp), new Vector2(currentUv, 1f));

                int leftBottom = -1;
                int rightBottomAdded = -1;
                if (innerRadius > 0f)
                {
                    rightBottomAdded = mesh.AddVertex(innerVertex);
                    leftBottom = prevInnerVertex;
                }

                int leftTop = prevOuterVertex;
                int rightTop = mesh.AddVertex(outerVertex);
                int rightBottom = mesh.AddVertex(innerVertex);
                int rightTopAdded = mesh.AddVertex(outerVertex);
                
                innerVertex.position.z = bottom;
                outerVertex.position.z = bottom;

                int leftBottomDeep = -1;
                if (innerRadius > 0f)
                    leftBottomDeep = prevInnerVertexDeep;

                int leftTopDeep = prevOuterVertexDeep;
                int rightTopDeep = mesh.AddVertex(outerVertex);
                int rightBottomDeep = mesh.AddVertex(innerVertex);
                int rightTopAddedDeep = mesh.AddVertex(outerVertex);
                
                mesh.AddTringle(rightBottom, rightTop, leftTop);
                mesh.AddTringle(leftTopDeep, rightTopDeep, rightBottomDeep);

                mesh.AddTringle(prevClosingVertexDeep, prevClosingVertex, rightTopAdded);
                mesh.AddTringle(rightTopAdded, rightTopAddedDeep, prevClosingVertexDeep);

                prevClosingVertex = rightTopAdded;
                prevClosingVertexDeep = rightTopAddedDeep;

                if (innerRadius > 0f)
                {
                    int rightBottomAddedDeep = mesh.AddVertex(innerVertex);
                    mesh.AddTringle(leftTop, leftBottom, rightBottom);
                    mesh.AddTringle(rightBottomDeep, leftBottomDeep, leftTopDeep);

                    mesh.AddTringle(rightBottomAdded, prevOpeningVertex, prevOpeningVertexDeep);
                    mesh.AddTringle(prevOpeningVertexDeep, rightBottomAddedDeep, rightBottomAdded);
                    prevOpeningVertexDeep = rightBottomAddedDeep;
                    prevOpeningVertex = rightBottomAdded;
                }
                prevInnerVertex = rightBottom;
                prevOuterVertex = rightTop;
                prevInnerVertexDeep = rightBottomDeep;
                prevOuterVertexDeep = rightTopDeep;

                if(i==segments)
                {
                    rightTopDeep = mesh.AddVertex(outerVertex);
                    rightBottomDeep = mesh.AddVertex(innerVertex);
                    innerVertex.position.z = innerUp;
                    outerVertex.position.z = outerUp;
                    rightTop = mesh.AddVertex(outerVertex);
                    rightBottom = mesh.AddVertex(innerVertex);
                    mesh.AddTringle(rightTopDeep, rightTop, rightBottom);
                    mesh.AddTringle(rightBottom, rightBottomDeep, rightTopDeep);
                }
            }

        }
        
    }
}
