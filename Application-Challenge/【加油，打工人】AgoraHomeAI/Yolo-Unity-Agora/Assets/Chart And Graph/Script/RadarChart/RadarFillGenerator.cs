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
    class RadarFillGenerator : MonoBehaviour
    {
        public int Smoothing = 3;
        /// <summary>
        /// Contains a mesh that was generate for this object only and should be destoryed once the object is cleaned
        /// </summary>
        Mesh mCleanMesh = null;
        /// <summary>
        /// the mesh filter for this object
        /// </summary>
        MeshFilter mFilter;

        Vector3[] mPath;
        Rect mPathRect;
        float mCurve;

        private bool EnsureMeshFilter()
        {
            if (mFilter == null)
                mFilter = GetComponent<MeshFilter>();
            if (mFilter == null)
                return false;
            return true;
        }

        Vector2 InterpolateInViewRect(Vector3 position)
        {
            float x = position.x - mPathRect.xMin;
            float y = position.y - mPathRect.yMin;
            return new Vector2(x / mPathRect.width, y / mPathRect.height);
        }

        Vector3 curve(Vector3 origin,Vector3 end,float t)
        {
            Vector3 cont = Vector3.Lerp(origin, end, 0.5f);
            cont.z *= 1.5f;
            float invT = 1f - t;
            return (invT * invT * origin) + (2 * invT * t * cont) + (t * t * end);
        }

        IEnumerable<UIVertex> getVerices()
        {
            if (mPath == null)
                yield break;
            UIVertex origin = new UIVertex();
            origin.position = new Vector3();
            origin.position.z = mCurve;
            origin.uv0 = InterpolateInViewRect(origin.position);
            for (int i = 0; i < mPath.Length; i++)
            {
                UIVertex current = new UIVertex();
                current.position = mPath[i];
                current.uv0 = InterpolateInViewRect(current.position);
                
                for(int j=0; j<Smoothing; j++)
                {
                    float t = ((float)(j + 1)) / (float)(Smoothing);
                    UIVertex smooth = new UIVertex();
                    smooth.position = curve(origin.position, current.position, t);
                    smooth.uv0 = InterpolateInViewRect(smooth.position);
                    yield return smooth;
                }
            }
            yield return origin;
        }
        public void Generate(Vector3[] path, float radius, float curve)
        {
            if (EnsureMeshFilter() == false)    // No mesh filter attached
                return;

            mPath = path;
            mPathRect = new Rect(-radius, -radius, radius * 2f, radius * 2f);
            mCurve = curve;
            WorldSpaceChartMesh mesh = new WorldSpaceChartMesh(1);
            int origin = 0;

            foreach (UIVertex v in getVerices())
                origin = mesh.AddVertex(v);

            for (int i = 0; i < mPath.Length; i++)
            {
                int prev = i - 1;
                if (prev < 0)
                    prev = mPath.Length - 1;
                mesh.AddTringle(origin, i * Smoothing, prev * Smoothing);
            }

            for (int j = 1; j < Smoothing; j++)
            {
                int current = j;
                int prev = j - 1;
                for (int i = 0; i < mPath.Length; i++)
                {
                    int prevI = i - 1;
                    if (prevI < 0)
                        prevI = mPath.Length - 1;
                    int a = (i * Smoothing) + current;
                    int b = (i * Smoothing) + prev;
                    int c = (prevI * Smoothing) + current;
                    int d = (prevI * Smoothing) + prev;
                    mesh.AddTringle(c, b, a);
                    mesh.AddTringle(b, c, d);
                }
            }

            Mesh newMesh = mesh.Generate();
            newMesh.hideFlags = HideFlags.DontSave;
            newMesh.RecalculateNormals();
            
            mFilter.sharedMesh = newMesh;
            ChartCommon.CleanMesh(newMesh, ref mCleanMesh);
        }

        public void Clear()
        {
            ChartCommon.CleanMesh(null, ref mCleanMesh);
        }

        void OnDestroy()
        {
            Clear();
        }
    }
}
