#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    /// <summary>
    /// a chart mesh that is used for canvas rendering
    /// </summary>
    class CanvasChartMesh : ChartMeshBase
    {
        VertexHelper mVHWrapAround;
        List<UIVertex> mListWrapAround;
        UIVertex[] mTmpQuad = new UIVertex[4];
        bool mTextOnly = false;

        public CanvasChartMesh(bool forText)
        {
            mTextOnly = forText;
        }

        public CanvasChartMesh(VertexHelper wrapAround)
        {
            mVHWrapAround = wrapAround;
        }

        public CanvasChartMesh(List<UIVertex> wrapAround)
        {
            mListWrapAround = wrapAround;
        }
        public void WrapAround(VertexHelper wrapAround)
        {
            Clear();
            mVHWrapAround = wrapAround;
            mListWrapAround = null;
        }
        public void WrapAround(List<UIVertex> wrapAround)
        {
            Clear();
            mVHWrapAround = null;
            mListWrapAround = wrapAround;
        }
        public override BillboardText AddText(AnyChart chart, MonoBehaviour prefab, Transform parentTransform, int fontSize,float fontScale, string text, float x, float y, float z,float angle, object userData)
        {
            if(mTextOnly)
               return base.AddText(chart, prefab, parentTransform, fontSize,fontScale, text, x, y, z,angle, userData);
            return null;
        }

        private UIVertex FloorVertex(UIVertex vertex)
        {
            Vector3 newPosition = new Vector3(Mathf.Floor(vertex.position.x), Mathf.Floor(vertex.position.y), Mathf.Floor(vertex.position.z));
            vertex.position = newPosition;
            return vertex;
        }
        public override void AddQuad(UIVertex vLeftTop, UIVertex vRightTop, UIVertex vLeftBottom, UIVertex vRightBottom)
        {
            if (mListWrapAround != null)
            {
                mListWrapAround.Add(vLeftTop);
                mListWrapAround.Add(vRightTop);
                mListWrapAround.Add(vRightBottom);
                mListWrapAround.Add(vLeftBottom);
                return;
            }

            if (mVHWrapAround != null)
            {
                mTmpQuad[0] = vLeftTop;
                mTmpQuad[1] = vRightTop;
                mTmpQuad[2] = vRightBottom;
                mTmpQuad[3] = vLeftBottom;
                mVHWrapAround.AddUIVertexQuad(mTmpQuad);
            }
        }
        
        public override void AddXYRect(Rect rect, int subMeshGroup, float depth)
        {
            Vector2[] uvs = GetUvs(rect);
            UIVertex leftTop = ChartCommon.CreateVertex(new Vector3(rect.xMin, rect.yMin, depth), uvs[0]);
            UIVertex rightTop = ChartCommon.CreateVertex(new Vector3(rect.xMax, rect.yMin, depth), uvs[1]);
            UIVertex leftBottom = ChartCommon.CreateVertex(new Vector3(rect.xMin, rect.yMax, depth), uvs[2]);
            UIVertex rightBottom = ChartCommon.CreateVertex(new Vector3(rect.xMax, rect.yMax, depth), uvs[3]);
            AddQuad(leftTop, rightTop, leftBottom, rightBottom);
        }

        public override void AddXZRect(Rect rect, int subMeshGroup, float yPosition)
        {
            // this does nothing , canvas are 2d only
        }

        public override void AddYZRect(Rect rect, int subMeshGroup, float xPosition)
        {
            // this does nothing , canvas are 2d only
        }
    }
}
