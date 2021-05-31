#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    public class RadarFill : MaskableGraphic
    {
        Material mCachedMaterial;
        Vector3[] mPath;
        Rect mPathRect;

        public RadarFill()
        {

        }

        /// <summary>
        /// sets the lines for this renderer
        /// </summary>
        /// <param name="lines"></param>
        internal void SetPath(Vector3[] path,float radius)
        {
            mPath = path;
            if (mPath.Length == 0)
                mPath = null;
            mPathRect = new Rect(-radius, -radius, radius * 2f, radius * 2f);
            SetAllDirty();
            Rebuild(CanvasUpdate.PreRender);
        }

        protected override void UpdateMaterial()
        {
            base.UpdateMaterial();
            canvasRenderer.SetTexture(material.mainTexture);
        }

        protected override void OnDisable()
        {
            base.OnDisable();
            ChartCommon.SafeDestroy(mCachedMaterial);
        }

        public override Material material
        {
            get
            {
                return base.material;
            }

            set
            {
                ChartCommon.SafeDestroy(mCachedMaterial);
                if (value == null)
                {
                    mCachedMaterial = null;
                    base.material = null;
                    return;
                }
                mCachedMaterial = new Material(value);
                mCachedMaterial.hideFlags = HideFlags.DontSave;
                base.material = mCachedMaterial;
            }
        }

        Vector2 InterpolateInViewRect(Vector3 position)
        {
            float x = position.x - mPathRect.xMin;
            float y = position.y - mPathRect.yMin;
            return new Vector2(x / mPathRect.width, y / mPathRect.height);
        }

        IEnumerable<UIVertex> getVerices()
        {
            if (mPath == null)
                yield break;
            UIVertex origin = new UIVertex();
            origin.position = new Vector3();
            origin.uv0 = InterpolateInViewRect(origin.position);
            for (int i=0; i<mPath.Length; i++)
            {
                UIVertex current = new UIVertex();
                current.position = mPath[i];
                current.uv0 = InterpolateInViewRect(current.position);
                yield return current;
            }
            yield return origin;
        }

#if (!UNITY_5_2_0) && (!UNITY_5_2_1)
        protected override void OnPopulateMesh(VertexHelper vh)
        {
            base.OnPopulateMesh(vh);
            if (mPath == null)
            {
                vh.Clear();
                return;
            }
            vh.Clear();
            foreach (UIVertex v in getVerices())
                vh.AddVert(v);
            for (int i = 0; i < mPath.Length; i++)
            {
                int prev = i - 1;
                if (prev < 0)
                    prev = mPath.Length - 1;
                vh.AddTriangle(prev, i, mPath.Length);
            }
        }
#endif
#pragma warning disable 0672
#if !UNITY_2017_1_OR_NEWER
        protected override void OnPopulateMesh(Mesh m)
        {
            if (mPath == null)
            {
                m.Clear();
                return;
            }
            WorldSpaceChartMesh mesh = new WorldSpaceChartMesh(1);
            foreach (UIVertex v in getVerices())
                mesh.AddVertex(v);
            for (int i = 0; i < mPath.Length; i++)
            {
                int prev = i - 1;
                if (prev < 0)
                    prev = mPath.Length - 1;
                mesh.AddTringle(prev, i, mPath.Length);
            }
            mesh.ApplyToMesh(m);
        }
#endif
#pragma warning restore 0672

    }
}
