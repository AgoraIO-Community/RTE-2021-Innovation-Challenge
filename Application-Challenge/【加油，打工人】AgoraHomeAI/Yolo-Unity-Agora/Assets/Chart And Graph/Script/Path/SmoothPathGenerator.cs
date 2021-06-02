#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public abstract partial class SmoothPathGenerator : PathGenerator
    {
        public int JointSmoothing = 2;
        public float JointSize = 0.1f;
        protected List<int> mSkipJoints = new List<int>();
        protected virtual int JointSmoothingLink
        {
            get { return JointSmoothing; }
        }

        protected virtual float JointSizeLink
        {
            get { return JointSize;  }
        }

        private List<Vector3> mInnerTmpCenters = new List<Vector3>();
        protected List<Vector3> mTmpCenters = new List<Vector3>();

        /// <summary>
        /// the mesh filter for this object
        /// </summary>
        MeshFilter mFilter;
        /// <summary>
        /// Contains a mesh that was generate for this object only and should be destoryed once the object is cleaned
        /// </summary>
        Mesh mCleanMesh = null;

        public override void Clear()
        {
            ChartCommon.CleanMesh(null, ref mCleanMesh);
        }

        public void OnDestroy()
        {
            Clear();
        }

       /* public void Update()
        {
            foreach(Vector3 point in mTmpCenters)
            {
                Debug.DrawLine(point, point + Vector3.up);
            }
        }*/

        protected bool EnsureMeshFilter()
        {
            if (mFilter == null)
                mFilter = GetComponent<MeshFilter>();
            if (mFilter == null)
                return false;
            return true;
        }

        public void SetMesh(Mesh mesh)
        {
            mesh.hideFlags = HideFlags.DontSave;
            mFilter.sharedMesh = mesh;
            ChartCommon.CleanMesh(mesh, ref mCleanMesh);
            MeshCollider collider = GetComponent<MeshCollider>();
            if (collider != null)
                collider.sharedMesh = mesh;
        }

        protected Quaternion LookRotation(Vector3 diff)
        {
            if (diff.sqrMagnitude <= float.Epsilon)
                return Quaternion.identity;
            Vector3 up = new Vector3(diff.y, -diff.x, 0f).normalized;
            return Quaternion.LookRotation(diff, up);
        }

        void AddJointSegments(Vector3 from, Vector3 curr, Vector3 to)
        {
            if (JointSmoothingLink <= 0)
            {
                mInnerTmpCenters.Add(curr);
                return;
            }
            for (int i = 0; i < JointSmoothingLink; i++)
            {
                float t = ((float)(i + 1)) / (float)(JointSmoothingLink + 1);
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

        protected void ModifyPath(Vector3[] path, bool closed,List<Vector3> res)
        {
            if (path.Length <= 1)
                return;

            mInnerTmpCenters.Clear();
            for (int i = 0; i <= path.Length; i++)
            {
                //                bool closing = false;
                if ((i == 0 && closed == false) || mSkipJoints.Contains(i))
                {
                    if(i < path.Length)
                        mInnerTmpCenters.Add(path[i]);
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
                float fromSize = Math.Min(JointSizeLink, dirMag1 * 0.5f);
                float toSize = Math.Min(JointSizeLink, dirMag2 * 0.5f);
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
            res.Clear();
            res.Add(mInnerTmpCenters[0]);
            for (int i = 1; i < mInnerTmpCenters.Count; i++)
            {
                Vector3 prev = mInnerTmpCenters[i - 1];
                Vector3 curr = mInnerTmpCenters[i];
                if ((curr - prev).sqrMagnitude < 0.000001)
                    continue;
                res.Add(curr);
            }
        }
        protected void ModifyPath(Vector3[] path, bool closed)
        {
            ModifyPath(path, closed, mTmpCenters);
        }
    }
}
