#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [RequireComponent(typeof(MeshRenderer))]
    [RequireComponent(typeof(MeshFilter))]
    public partial class BoxPathGenerator : SmoothPathGenerator
    {
        [Range(0f, 10f)]
        public float HeightRatio = 1f;

        List<int> mTmpTringle = new List<int>();
        List<Vector2> mTmpUv = new List<Vector2>();
        List<Vector3> mVertices = new List<Vector3>();

        int WriteBox(float thickness, Quaternion rotation, Vector3 center, float u)
        {

            Vector3 vz = new Vector3(thickness, 0f, 0f);
            Vector3 vy = new Vector3(0f, thickness * HeightRatio, 0f);

            int index = mVertices.Count;

            mVertices.Add(center + (rotation * (vz + vy)));
            mVertices.Add(center + (rotation * (-vz + vy)));
            mVertices.Add(center + (rotation * (-vz - vy)));
            mVertices.Add(center + (rotation * (vz - vy)));

            mVertices.Add(mVertices[index]);
            mVertices.Add(mVertices[index + 1]);
            mVertices.Add(mVertices[index + 2]);
            mVertices.Add(mVertices[index + 3]);

            mTmpUv.Add(new Vector2(u, 0f));
            mTmpUv.Add(new Vector2(u, 1f));
            mTmpUv.Add(new Vector2(u, 0f));
            mTmpUv.Add(new Vector2(u, 1f));

            mTmpUv.Add(new Vector2(u, 0f));
            mTmpUv.Add(new Vector2(u, 1f));
            mTmpUv.Add(new Vector2(u, 0f));
            mTmpUv.Add(new Vector2(u, 1f));

            return index;
        }

        void AddTringles(List<int> tringles, int from, int to)
        {
            tringles.Add(from + 1);
            tringles.Add(to);
            tringles.Add(from);


            tringles.Add(to + 1);
            tringles.Add(to);
            tringles.Add(from + 1);


            tringles.Add(from + 2);
            tringles.Add(to + 5);
            tringles.Add(from + 5);


            tringles.Add(to + 2);
            tringles.Add(to + 5);
            tringles.Add(from + 2);

            tringles.Add(from + 3);
            tringles.Add(to + 6);
            tringles.Add(from + 6);

            tringles.Add(to + 3);
            tringles.Add(to + 6);
            tringles.Add(from + 3);

            tringles.Add(from + 4);
            tringles.Add(to + 7);
            tringles.Add(from + 7);

            tringles.Add(to + 4);
            tringles.Add(to + 7);
            tringles.Add(from + 4);
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
            if(closed == false)
            {
                int box = WriteBox(thickness, LookRotation(mTmpCenters[1] - mTmpCenters[0]), mTmpCenters[0], currentU);
                mTmpTringle.Add(box + 3);
                mTmpTringle.Add(box + 1);
                mTmpTringle.Add(box);

                mTmpTringle.Add(box + 3);
                mTmpTringle.Add(box + 2);
                mTmpTringle.Add(box + 1);

            }
            int prevBoxIndex = WriteBox(thickness, LookRotation(mTmpCenters[1] - mTmpCenters[0]), mTmpCenters[0], currentU);

            Quaternion rotation = Quaternion.identity;
            Vector3 curr = Vector3.zero;
            for (int i = 1; i < mTmpCenters.Count; i++)
            {
                Vector3 prev = mTmpCenters[i - 1];
                curr = mTmpCenters[i];
                Vector3 diff = curr - prev;
                float size = diff.magnitude;

                if (size <= 0.0001f)
                {
                    continue;
                }
                currentU += size;

                rotation = LookRotation(diff);

                if (i + 1 < mTmpCenters.Count)
                {
                    Vector3 next = mTmpCenters[i + 1];
                    // Vector3 nextDiff = next - curr;
                    Quaternion rotationB = LookRotation(next - curr);
                    rotation = Quaternion.Lerp(rotation, rotationB, 0.5f);
                }

                int index = WriteBox(thickness, rotation, curr, currentU);
                AddTringles(mTmpTringle, prevBoxIndex, index);
                prevBoxIndex = index;
            }
            if (closed == false)
            {
                int box = WriteBox(thickness, rotation, curr, currentU);
                mTmpTringle.Add(box);
                mTmpTringle.Add(box + 1);
                mTmpTringle.Add(box + 3);
                
                mTmpTringle.Add(box + 1);
                mTmpTringle.Add(box + 2);
                mTmpTringle.Add(box + 3);

            }

            for (int i = 0; i < mTmpUv.Count; i++)
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
