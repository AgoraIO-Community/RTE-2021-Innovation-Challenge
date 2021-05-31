#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [RequireComponent(typeof(MeshRenderer))]
    [RequireComponent(typeof(MeshFilter))]
    public partial class CylinderPathGenerator : SmoothPathGenerator
    {
        public int CircleVertices = 10;
        [Range(0.01f,10.0f)]
        public float HeightRatio = 1f;

        Vector3[] mCircleTemp;
        Vector3[] mCurrentCircle;
        List<int> mTmpTringle = new List<int>();
        List<Vector2> mTmpUv = new List<Vector2>();
        List<Vector3> mVertices = new List<Vector3>();

        void EnsureCirlce()
        {
            if(mCircleTemp == null || mCircleTemp.Length !=CircleVertices)
            {
                mCircleTemp = new Vector3[CircleVertices];
                for(int i=0; i<CircleVertices; i++)
                {
                    float intep = ((float)i) / ((float)CircleVertices);
                    float angle = intep * Mathf.PI * 2f;
                    mCircleTemp[i] = new Vector3(Mathf.Cos(angle), Mathf.Sin(angle), 0.0f);
                }
                mCurrentCircle = new Vector3[CircleVertices];
            }
        }

        int WriteCircle(float thickness,Quaternion angle,Vector3 center,float u)
        {
            EnsureCirlce();
            mCircleTemp.CopyTo(mCurrentCircle, 0);
            for(int i=0; i< mCurrentCircle.Length; i++)
            {
                mCurrentCircle[i] *= thickness;
                mCurrentCircle[i].y *= HeightRatio;
                mCurrentCircle[i] = angle * mCurrentCircle[i];
                mCurrentCircle[i] += center;
                float v = ((float)i) / (float)(mCurrentCircle.Length - 1);
                mTmpUv.Add(new Vector2(u, v));
            }
            int index = mVertices.Count;
            mVertices.AddRange(mCurrentCircle);
            return index;
        }

        void AddTringles(List<int> tringles, int from, int to)
        {
            if (CircleVertices <= 1)
                return;
            for (int i = 0; i < CircleVertices; i++)
            {
                int prev = i - 1;
                if (prev < 0)
                    prev = CircleVertices - 1;
                int fromPrev = from + prev;
                int toPrev = to + prev;
                int fromCurr = from + i;
                int toCurr = to + i;

                tringles.Add(fromPrev);
                tringles.Add(fromCurr);
                tringles.Add(toCurr);

                tringles.Add(toCurr);
                tringles.Add(toPrev);
                tringles.Add(fromPrev);  
            }
        }

        public override void Generator(Vector3[] path, float thickness, bool closed)
        {
            if (EnsureMeshFilter() == false)    // No mesh filter attached
                return;

            Clear();
            if (path.Length <= 1)
                return;

            mTmpTringle.Clear();
            mTmpUv.Clear();
            mVertices.Clear();

            ModifyPath(path, closed);
            if (mTmpCenters.Count <= 1)
                return;

            float currentU = 0f;
            int prevCircleIndex = WriteCircle(thickness, LookRotation(mTmpCenters[1] - mTmpCenters[0]), mTmpCenters[0], currentU);

            if (closed == false)
            {
                int circle = WriteCircle(thickness, LookRotation(mTmpCenters[1] - mTmpCenters[0]), mTmpCenters[0], currentU);
                mVertices.Add(mTmpCenters[0]);
                mTmpUv.Add(new Vector2(0f, 0.5f));
                for (int i = 0; i < CircleVertices; i++)
                {
                    int next = (i + 1) % CircleVertices;
                    mTmpTringle.Add(circle + CircleVertices);
                    mTmpTringle.Add(circle + next);
                    mTmpTringle.Add(circle + i);

                }
            }

            Vector3 curr = Vector3.zero;
            Quaternion rotation = Quaternion.identity;

            for (int i=1; i<mTmpCenters.Count; i++)
            {
                Vector3 prev = mTmpCenters[i - 1];
                curr = mTmpCenters[i];
               
                Vector3 diff = curr - prev;

                float size = diff.magnitude;
                currentU += size;
                rotation = LookRotation(diff);

                /*if (i + 1 < mTmpCenters.Count)
                {
                    Vector3 next = mTmpCenters[i + 1];
                    Quaternion rotationB = Quaternion.LookRotation(next - curr, Vector3.up);
                    rotation = Quaternion.Lerp(rotation, rotationB, 0.5f);
                }*/

                int index = WriteCircle(thickness, rotation, curr, 0f);

                AddTringles(mTmpTringle, prevCircleIndex, index);
                prevCircleIndex = index;
            }

            if (closed == false)
            {
                int circle = WriteCircle(thickness, rotation, curr, 1f);
                mVertices.Add(curr);
                mTmpUv.Add(new Vector2(1f, 0.5f));
                for (int i = 0; i < CircleVertices; i++)
                {
                    int next = (i + 1) % CircleVertices;     
                    mTmpTringle.Add(circle + next);
                    mTmpTringle.Add(circle + CircleVertices);
                    mTmpTringle.Add(circle + i);
                }
            }

            for (int i=0; i<mTmpUv.Count; i++)
            {
                Vector2 uv = mTmpUv[i];
                uv.x /= currentU;
                mTmpUv[i] = uv;
            }

            Mesh mesh = new Mesh();
            mesh.vertices = mVertices.ToArray();
            mesh.uv = mTmpUv.ToArray();
            mesh.triangles = mTmpTringle.ToArray();
            mesh.RecalculateNormals();
            SetMesh(mesh);

            mTmpTringle.Clear();
            mTmpUv.Clear();
            mVertices.Clear();
        }
    }
}
