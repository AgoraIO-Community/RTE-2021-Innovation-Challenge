#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    [RequireComponent(typeof(LineRenderer))]
    partial class LineRendererPathGenerator : PathGenerator
    {
        LineRenderer mRenderer;
        private void Start()
        {
            
        }
        public void EnsureRenderer()
        {
            mRenderer = GetComponent<LineRenderer>();
        }
        public override void Clear()
        {
            EnsureRenderer();
            if (mRenderer!= null)
            {
#if (!UNITY_5_2) && (!UNITY_5_3) && (!UNITY_5_4)
#if !UNITY_5_5
                mRenderer.positionCount = 0;    
#else
                    mRenderer.numPositions = 0;
#endif
#else
                mRenderer.SetVertexCount(0);
#endif
            }
        }

        public override void Generator(Vector3[] path, float thickness, bool closed)
        {
            EnsureRenderer();
            if (mRenderer != null)
            {
#if !UNITY_5_2 && !UNITY_5_3 && !UNITY_5_4
#if !UNITY_5_5
                mRenderer.startWidth = thickness;
                mRenderer.endWidth = thickness;
                mRenderer.positionCount = path.Length;    
#else
                mRenderer.startWidth = thickness;
                mRenderer.endWidth = thickness;
                mRenderer.numPositions = path.Length;
#endif
#else
                mRenderer.SetWidth(thickness, thickness);
                mRenderer.SetVertexCount(path.Length);

#endif

                for (int i=0; i< path.Length; i++)
                {
                    mRenderer.SetPosition(i, path[i]);
                }
            }
        }
    }
}
