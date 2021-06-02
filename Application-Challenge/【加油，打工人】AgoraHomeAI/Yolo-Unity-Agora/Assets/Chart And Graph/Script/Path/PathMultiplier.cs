#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    partial class PathMultiplier
    {
        public int JointSmoothing = 2;
        public float JointSize = 0.1f;

        private List<Vector3> mInnerTmpCenters = new List<Vector3>();
        protected List<Vector3> mTmpCenters = new List<Vector3>();


        protected List<Vector3> mMultipliedPath = new List<Vector3>();

        protected Quaternion LookRotation(Vector3 diff)
        {
            Vector3 up = new Vector3(diff.y, -diff.x, 0f).normalized;
            return Quaternion.LookRotation(diff, up);
        }

        void AddJointSegments(Vector3 from, Vector3 curr, Vector3 to)
        {
            if (JointSmoothing <= 0)
            {
                mInnerTmpCenters.Add(curr);
                return;
            }
            for (int i = 0; i < JointSmoothing; i++)
            {
                float t = ((float)(i + 1)) / (float)(JointSmoothing + 1);
                float invT = 1f - t;
                Vector3 point = Vector3.zero;
                /*                if (t < 0.5f)
                                    point = Vector3.Lerp(from, curr, t / 0.5f);
                                else
                                    point = Vector3.Lerp(curr, to, (t - 0.5f) / 0.5f);*/
                point = (invT * invT * from) + (2 * invT * t * curr) + (t * t * to);
                mInnerTmpCenters.Add(point);
            }
        }

        void AddCenters(Vector3 translation,float scale)
        {
            for(int i=0; i< mTmpCenters.Count; i++)
            {
                mMultipliedPath.Add((mTmpCenters[i] * scale) + translation);
            }
        }

        public void ApplyToMesh(WorldSpaceChartMesh mesh)
        {
            
        }

        public void MultiplyPath(Vector3[] path)
        {
            mMultipliedPath.Clear();
            for(int i=0; i<path.Length; i++)
            {
                AddCenters(path[i],1f);
            }
        }

        public void ModifyPath(Vector3[] path, bool closed)
        {
            if (path.Length <= 1)
                return;

            mInnerTmpCenters.Clear();
            for (int i = 0; i <= path.Length; i++)
            {
                //                bool closing = false;
                if (i == 0 && closed == false)
                {
                    mInnerTmpCenters.Add(path[0]);
                    continue;
                }
                if (i >= path.Length - 1)
                {
                    if (closed == false)
                        continue;
                }
                int prevIndex = i - 1;
                if (prevIndex < 0)
                    prevIndex = path.Length - 1;
                Vector3 prev = path[prevIndex];
                // int currIndex = i;
                Vector3 curr = path[i % path.Length];
                int nextIndex = i + 1;
                Vector3 next = path[nextIndex % path.Length];
                Vector3 dir1 = (curr - prev);
                Vector3 dir2 = (next - curr);
                float dirMag1 = dir1.magnitude;
                float dirMag2 = dir2.magnitude;
                dir1.Normalize();
                dir2.Normalize();
                float fromSize = Math.Min(JointSize, dirMag1 * 0.5f);
                float toSize = Math.Min(JointSize, dirMag2 * 0.5f);
                Vector3 from = curr - dir1 * fromSize;
                Vector3 to = curr + dir2 * toSize;
                Vector3 innerFrom = curr - dir1 * fromSize * 0.7f;
                Vector3 innerTo = curr + dir2 * toSize * 0.7f;
                if (i > 0)
                {
                    mInnerTmpCenters.Add(from);
                    mInnerTmpCenters.Add(innerFrom);
                    AddJointSegments(innerFrom, curr, innerTo);
                    mInnerTmpCenters.Add(innerTo);
                }
                mInnerTmpCenters.Add(to);
            }

            if (closed == false)
                mInnerTmpCenters.Add(path[path.Length - 1]);
            //  else
            //    mInnerTmpCenters.Add(path[0]);
            mTmpCenters.Clear();
            mTmpCenters.Add(mInnerTmpCenters[0]);
            for (int i = 1; i < mInnerTmpCenters.Count; i++)
            {
                Vector3 prev = mInnerTmpCenters[i - 1];
                Vector3 curr = mInnerTmpCenters[i];
                if ((curr - prev).sqrMagnitude < 0.000001)
                    continue;
                mTmpCenters.Add(curr);
            }
        }
    }
}

