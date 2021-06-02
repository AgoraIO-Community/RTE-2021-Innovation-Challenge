#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    public class CanvasLinesHover : MaskableGraphic
    {
        float mHalfThickness;
        UIVertex[] mTmpVerts = new UIVertex[4];

        public void Init(float thickness)
        {
            mHalfThickness = thickness * 0.5f;
            SetAllDirty();
            Rebuild(CanvasUpdate.PreRender);
        }

        IEnumerable<UIVertex> getVerices()
        {
            UIVertex v = new UIVertex();
            v.position = new Vector3(-mHalfThickness, -mHalfThickness);
            v.uv0 = new Vector2(0f, 0f);
            yield return v;
            v.position = new Vector3(-mHalfThickness, mHalfThickness);
            v.uv0 = new Vector2(0f, 1f);
            yield return v;
            v.position = new Vector3(mHalfThickness, mHalfThickness);
            v.uv0 = new Vector2(1f, 1f);
            yield return v;
            v.position = new Vector3(mHalfThickness, -mHalfThickness);
            v.uv0 = new Vector2(1f, 0f);
            yield return v;
        }

#if (!UNITY_5_2_0) && (!UNITY_5_2_1)
        protected override void OnPopulateMesh(VertexHelper vh)
        {
            base.OnPopulateMesh(vh);
            vh.Clear();
            int vPos = 0;
            foreach (UIVertex v in getVerices())
            {
                mTmpVerts[vPos++] = v;
                if (vPos == 4)
                {
                    UIVertex tmp = mTmpVerts[2];
                    mTmpVerts[2] = mTmpVerts[3];
                    mTmpVerts[3] = tmp;
                    vPos = 0;
                    vh.AddUIVertexQuad(mTmpVerts);
                }
            }
        }
#endif
#pragma warning disable 0672
#if !UNITY_2017_1_OR_NEWER
        protected override void OnPopulateMesh(Mesh m)
        {
            WorldSpaceChartMesh mesh = new WorldSpaceChartMesh(1);
            int vPos = 0;
            foreach (UIVertex v in getVerices())
            {
                mTmpVerts[vPos++] = v;
                if (vPos == 4)
                {
                    vPos = 0;

                    mesh.AddQuad(mTmpVerts[0], mTmpVerts[1], mTmpVerts[2], mTmpVerts[3]);
                }
            }
            mesh.ApplyToMesh(m);
        }
#endif
#pragma warning restore 0672
    }
}
