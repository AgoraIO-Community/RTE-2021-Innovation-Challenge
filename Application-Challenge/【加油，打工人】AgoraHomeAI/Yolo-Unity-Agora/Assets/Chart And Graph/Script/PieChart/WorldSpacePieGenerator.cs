#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// pie generator for world space charts.
    /// </summary>
    class WorldSpacePieGenerator : MonoBehaviour,IPieGenerator
    {
        MeshRenderer mRenderer;
        MeshFilter mFilter;
        Mesh mCleanMesh;

        public void Start()
        {
        }

        public void Generate(float startAngle, float angleSpan, float radius, float innerRadius, int segments, float outerDepth,float innerDepth)
        {
            WorldSpaceChartMesh mesh = new WorldSpaceChartMesh(1);
            float maxDepth = Mathf.Max(outerDepth, innerDepth);
            if (maxDepth <= 0f)
                PieMesh.Generate2dMesh(mesh, startAngle, angleSpan, radius, innerRadius, segments);
            else
                PieMesh.Generate3dMesh(mesh, startAngle, angleSpan, radius, innerRadius, segments, outerDepth,innerDepth);

            if (mCleanMesh != null)
            {
                mCleanMesh.Clear();
                mesh.ApplyToMesh(mCleanMesh);
                MeshCollider collider = ChartCommon.EnsureComponent<MeshCollider>(gameObject);
                if (collider != null)
                {
                    collider.sharedMesh = null;
                    collider.sharedMesh = mCleanMesh;
                }
            }
            else
            {
                Mesh newMesh = mesh.Generate();
                newMesh.hideFlags = HideFlags.DontSave;
                if (mFilter == null)
                    mFilter = GetComponent<MeshFilter>();
                mFilter.sharedMesh = newMesh;
                MeshCollider collider =  ChartCommon.EnsureComponent<MeshCollider>(gameObject);
                if (collider != null)
                    collider.sharedMesh = newMesh;
                ChartCommon.CleanMesh(newMesh, ref mCleanMesh);
            }
        }

        void OnDestroy()
        {
            ChartCommon.CleanMesh(null, ref mCleanMesh);
        }

    }
}
