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
    partial class CustomPathGenerator : SmoothPathGenerator, IPieGenerator , IBarGenerator
    {
       // List<Vector2> mTmpPath = new List<Vector2>();
      //  List<Vector2> mOfPathTringles = new List<Vector2>();
        List<Vector3> mOfPAth = new List<Vector3>();
        List<float> mTmpScales = new List<float>();
        List<Quaternion> mTmpAngles = new List<Quaternion>();
        List<int> mTmpTringle = new List<int>();
        List<Vector2> mTmpUv = new List<Vector2>();
        List<Vector3> mVertices = new List<Vector3>();
        bool mClosedSmooth = false;
       // List<Vector3> mNormals = new List<Vector3>();

        int writeItem(Quaternion angle, Vector3 center, float scale, float u)
        {
            int index = mVertices.Count;
            for (int i = 0; i < mOfPAth.Count; i++)
            {
                Vector3 current = mOfPAth[i] * scale;
                current = angle * current;
                current += center;
                float v = ((float)i) / (float)(mOfPAth.Count - 1);
                mTmpUv.Add(new Vector2(u, v));
                mVertices.Add(current);
            }
            return index;
        }

        void AddTringles(List<int> tringles, int from, int to)
        {
            if (mOfPAth.Count <= 1)
                return;

            for (int i = 0; i < mOfPAth.Count; i++)
            {
                int prev = i - 1;
                if (prev < 0)
                    prev = mOfPAth.Count - 1;
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

        protected void OfPath(Vector3[] path)
        {
            ModifyPath(path, true, mOfPAth);
        }

        Quaternion getAngle(int index, Quaternion def)
        {
            if (mTmpAngles.Count <= index)
            {
                return def;
            }
            return mTmpAngles[index];
        }

        float getScale(int index)
        {
            if (mTmpScales.Count <= index)
                return 1f;
            return mTmpScales[index];
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

            //  ModifyPath(path, closed);
            mTmpCenters.Clear();
            mTmpCenters.AddRange(path);
            if (mTmpCenters.Count <= 1)
                return;

            float currentU = 0f;
            int prevItemIndex = writeItem(getAngle(0, LookRotation(mTmpCenters[1] - mTmpCenters[0])), mTmpCenters[0], getScale(0), currentU);

            if (closed == false)
            {
                int item = prevItemIndex;
                if (mClosedSmooth == false)
                    item = writeItem(getAngle(0, LookRotation(mTmpCenters[1] - mTmpCenters[0])), mTmpCenters[0], getScale(0), currentU);
                mVertices.Add(mTmpCenters[0]);
                mTmpUv.Add(new Vector2(0f, 0.5f));
                for (int i = 0; i < mOfPAth.Count; i++)
                {
                    int next = (i + 1) % mOfPAth.Count;
                    mTmpTringle.Add(item + mOfPAth.Count);
                    mTmpTringle.Add(item + next);
                    mTmpTringle.Add(item + i);

                }
            }

            Vector3 curr = Vector3.zero;
            Quaternion rotation = Quaternion.identity;

            for (int i = 1; i < mTmpCenters.Count; i++)
            {
                Vector3 prev = mTmpCenters[i - 1];
                curr = mTmpCenters[i];

                Vector3 diff = curr - prev;

                float size = diff.magnitude;
                currentU += size;
                rotation = LookRotation(diff);

                int index = writeItem(getAngle(i, rotation), curr, getScale(i), currentU);

                AddTringles(mTmpTringle, prevItemIndex, index);
                prevItemIndex = index;
            }

            if (closed == false)
            {
                int item = prevItemIndex;
                if (mClosedSmooth == false)
                    item = writeItem(getAngle(mTmpCenters.Count - 1, rotation), curr, getScale(mTmpCenters.Count - 1), 1f);
                mVertices.Add(curr);
                mTmpUv.Add(new Vector2(1f, 0.5f));
                for (int i = 0; i < mOfPAth.Count; i++)
                {
                    int next = (i + 1) % mOfPAth.Count;
                    mTmpTringle.Add(item + next);
                    mTmpTringle.Add(item + mOfPAth.Count);
                    mTmpTringle.Add(item + i);
                }
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

        private float quickBlend(float blend)
        {
            return Mathf.Sqrt(1- blend* blend);
        }


        public void Generate(float startAngle, float angleSpan, float radius, float innerRadius, int segments, float outerdepth,float innerdepth)
        {
            mClosedSmooth = false;
            float maxDepth = Mathf.Max(innerdepth,outerdepth);
            Vector3[] path = new Vector3[] { new Vector3(0f, radius , 0f), new Vector3(0f, innerRadius, 0f), new Vector3(outerdepth, innerRadius, 0f), new Vector3(innerdepth, radius, 0f) };
            float midRadius = (innerRadius + radius) * 0.5f;
            Vector3 center = new Vector3(maxDepth * 0.5f, midRadius);
            for (int j = 0; j < path.Length; j++)
                path[j] -= center;

            mSkipJoints.Clear();
            if (innerRadius <= 0.0001f)
            {
                mSkipJoints.Add(0);
                mSkipJoints.Add(3);
                mSkipJoints.Add(4);
            }

            OfPath(path);

            Vector3[] alongPath = new Vector3[segments + 1  ];
            float toAngle = startAngle + angleSpan;

            if(Mathf.Abs(startAngle - toAngle) < 0.0001f)
            {
                Generator(new Vector3[0], 0f, false);
                return;
            }

            int i = 0;
            mTmpScales.Clear();
            mTmpAngles.Clear();
          //  float dir = Mathf.Sign(angleSpan);
        //    float minSize = Mathf.Max(0f, 1f - JointSize);
            //startAngle = startAngle + dir * JointSize / midRadius;

/*            for (; i<JointSmoothing; i++)
            {
                float blend = 1f - (i / ((float)JointSmoothing));
                float angle = startAngle - ((JointSize / midRadius) * blend)* dir;
                alongPath[i] = ChartCommon.FromPolarRadians(angle, midRadius);
                mTmpAngles.Add(Quaternion.Euler(-90f, 0f, 0f) * Quaternion.Euler(0f, -angle * Mathf.Rad2Deg, 0f) * Quaternion.Euler(0f, 0f, 90f));
                mTmpScales.Add(1f);// Mathf.Lerp(0f,1f , quickBlend(blend )));
            }*/
            
          //  float toJoint = toAngle + dir * (JointSize / midRadius);
          //  int next = JointSmoothing + segments;

            for(i=0 ; i<= segments; i++)
            {
                float blend = (i) / ((float)segments);
                float angle = Mathf.Lerp(startAngle, toAngle, blend);
                alongPath[i] = ChartCommon.FromPolarRadians(angle, midRadius);
                mTmpAngles.Add(Quaternion.Euler(-90f, 0f, 0f) * Quaternion.Euler(0f, -angle * Mathf.Rad2Deg,0f) * Quaternion.Euler(0f,0f, 90f));
                mTmpScales.Add(1f);
            }

         /*   for (i =0 ; i < JointSmoothing; i++)
            {
                float blend = (i+1) / ((float)JointSmoothing);
                float angle = toAngle + ((JointSize / midRadius) * blend) * dir;
                alongPath[next+ 1 + i] = ChartCommon.FromPolarRadians(angle, midRadius);
                mTmpAngles.Add(Quaternion.Euler(-90f, 0f, 0f) * Quaternion.Euler(0f, -angle * Mathf.Rad2Deg, 0f) * Quaternion.Euler(0f, 0f, 90f));
                mTmpScales.Add(Mathf.Lerp(0f,1f, quickBlend(blend) ));
            }*/

            Generator(alongPath, 0f, false);
        }

        void IBarGenerator.Generate(float normalizedSize,float scale)
        {
            mClosedSmooth = false;
            Vector3[] path = new Vector3[] { new Vector3(0f, 0f, 0f), new Vector3(0f, 0f, 1f), new Vector3(1f, 0f, 1f), new Vector3(1f, 0f, 0f)};
            Vector3 center = new Vector3(0.5f, 0f,0.5f);
            for (int j = 0; j < path.Length; j++)
                path[j] -= center;

            mSkipJoints.Clear();
            OfPath(path);

            Vector3[] alongPath = new Vector3[(JointSmoothing+3)*2];

            if (scale < 0.0001f)
            {
                Generator(new Vector3[0], 0f, false);
                return;
            }

            int i = 0;
            mTmpScales.Clear();
            mTmpAngles.Clear();
            float joint = JointSize / scale;
            joint = Math.Min(joint, 0.5f);
            float minSize = 1f - JointSize;

            for (i=0 ; i<= JointSmoothing+2; i++)
            {
                float blend = (i / ((float)JointSmoothing));
                float pos = joint * blend;
                alongPath[i] = new Vector3(0f, pos);
                mTmpAngles.Add(Quaternion.identity);
                mTmpScales.Add(Mathf.Lerp(minSize,1f,quickBlend(1f-blend)));
            }
            for (i=0; i <= JointSmoothing+2; i++)
            {
                float blend = 1f - ((i-2) / ((float)JointSmoothing));
                float pos = 1f - joint * blend;
                alongPath[i + JointSmoothing +3] = new Vector3(0f, pos);
                mTmpAngles.Add(Quaternion.identity);
                mTmpScales.Add(Mathf.Lerp(minSize, 1f, quickBlend(1f - blend)));
            }

            Generator(alongPath, 0f, false);
        }

        void IBarGenerator.Clear()
        {
            Clear();
        }
    }
}
