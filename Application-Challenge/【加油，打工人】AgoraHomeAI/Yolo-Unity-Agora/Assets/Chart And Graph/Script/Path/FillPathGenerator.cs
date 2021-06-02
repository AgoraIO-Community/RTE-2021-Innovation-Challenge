#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    public partial class FillPathGenerator : SmoothPathGenerator
    {
        public bool WithTop = true;
        public bool MatchLine = true;
        private float mGraphBottom = 0f;
        private float mGraphTop = 10f;
        private bool StretchFill = false;
        List<int> mTmpTringle = new List<int>();
        List<Vector2> mTmpUv = new List<Vector2>();
        List<Vector3> mVertices = new List<Vector3>();

        private bool mHasParent = false;
        private int mParentJointSmoothing;
        private float mParentJointSize;

        protected override float JointSizeLink
        {
            get
            {
                if (MatchLine == true && mHasParent)
                    return mParentJointSize;
                return base.JointSizeLink;
            }
        }
        protected override int JointSmoothingLink
        {
            get
            {
                if (MatchLine == true && mHasParent)
                    return mParentJointSmoothing;
                return base.JointSmoothingLink;
            }
        }
        public void SetLineSmoothing(bool hasParent,int jointSmoothing , float jointSize)
        {
            mHasParent = hasParent;
            mParentJointSmoothing = jointSmoothing;
            mParentJointSize = jointSize;
        }

        
        public void SetStrechFill(bool strech)
        {
            StretchFill = strech;
        }

        public void SetGraphBounds(float bottom,float top)
        {
            mGraphBottom = bottom;
            mGraphTop = top;
        }

        int WriteVector(Vector3 position,float thickness,float u)
        {
            Vector3 v = new Vector3(0, 0, thickness);
            Vector3 bottomPosition = new Vector3(position.x, mGraphBottom, position.z);
            int index = mVertices.Count;

            float vTex = 1f;
            if(StretchFill == false)
                vTex = Mathf.Abs((position.y - mGraphBottom) / (mGraphBottom - mGraphTop));

            mVertices.Add(position - v);
            mVertices.Add(position + v);

            mVertices.Add(bottomPosition - v);
            mVertices.Add(bottomPosition + v);

            mVertices.Add(mVertices[index + 2]);
            mVertices.Add(mVertices[index + 3]);

            mTmpUv.Add(new Vector2(u, vTex));
            mTmpUv.Add(new Vector2(u, vTex));

            mTmpUv.Add(new Vector2(u, 0f));
            mTmpUv.Add(new Vector2(u, 0f));

            mTmpUv.Add(new Vector2(u, 0f));
            mTmpUv.Add(new Vector2(u, 0f));

            if (WithTop)
            {
                mVertices.Add(mVertices[index]);
                mVertices.Add(mVertices[index + 1]);

                mTmpUv.Add(new Vector2(u, vTex));
                mTmpUv.Add(new Vector2(u, vTex));
            }

            return index;
        }

        void AddTringles(List<int> tringles, int from, int to)
        {
            tringles.Add(from);
            tringles.Add(to);
            tringles.Add(from +2);

            tringles.Add(to);
            tringles.Add(to + 2);
            tringles.Add(from + 2);

            tringles.Add(from + 3);
            tringles.Add(to + 1);
            tringles.Add(from +1 );

            tringles.Add(from + 3);
            tringles.Add(to + 3);
            tringles.Add(to + 1);         

            tringles.Add(from + 4);
            tringles.Add(to + 4);
            tringles.Add(from + 5);

            tringles.Add(to + 4);
            tringles.Add(to + 5);
            tringles.Add(from + 5);

            if(WithTop)
            {
                tringles.Add(from + 7);
                tringles.Add(to + 6);
                tringles.Add(from + 6);

                tringles.Add(from + 7);
                tringles.Add(to + 7);
                tringles.Add(to + 6);
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

            int first = WriteVector(mTmpCenters[0], thickness, currentU);

            mTmpTringle.Add(first + 2);
            mTmpTringle.Add(first + 1);
            mTmpTringle.Add(first);
            
            mTmpTringle.Add(first + 1);
            mTmpTringle.Add(first + 2);
            mTmpTringle.Add(first + 3);

            int prevIndex = WriteVector(mTmpCenters[0], thickness, currentU);

            for (int i = 1; i < mTmpCenters.Count; i++)
            {
                Vector3 prev = mTmpCenters[i - 1];
                Vector3 curr = mTmpCenters[i];

                Vector3 diff = curr - prev;

                float size = diff.magnitude;
                currentU += size;

                int index = WriteVector(curr, thickness, currentU);

                AddTringles(mTmpTringle, prevIndex, index);
                prevIndex = index;
            }
            int last = WriteVector(mTmpCenters[mTmpCenters.Count-1], thickness, currentU);

            mTmpTringle.Add(last);
            mTmpTringle.Add(last + 1);
            mTmpTringle.Add(last + 2);

            mTmpTringle.Add(last + 3);
            mTmpTringle.Add(last + 2);
            mTmpTringle.Add(last + 1);
            
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
