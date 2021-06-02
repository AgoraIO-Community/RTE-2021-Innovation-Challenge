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
    /// base class for all chart mesh generators
    /// </summary>
    abstract class ChartMeshBase : IChartMesh
    {
        List<BillboardText> mText = new List<BillboardText>();
        List<BillboardText> mCurrentTexts = new List<BillboardText>();
        Dictionary<string, BillboardText> mRecycled = new Dictionary<string, BillboardText>();
        List<BillboardText> mCached = new List<BillboardText>();
        float innerTile = 1f;
        public ChartOrientation Orientation { get; set; }
        public float Tile
        {
            get { return innerTile; }
            set
            {
                innerTile = value;

            }
        }
        public float Offset { get; set; }
        public float Length { get; set; }
        public bool RecycleText { get; set; }

        public ChartMeshBase()
        {
            Orientation = ChartOrientation.Vertical;
        }

        public virtual List<BillboardText> CurrentTextObjects
        {
            get
            {
                return mCurrentTexts;
            }
        }

        public virtual List<BillboardText> TextObjects
        {
            get
            {
                return mText;
            }
        }

        Vector2[][] mUvs = new Vector2[2][]
        {
            new Vector2[]{ new Vector2(0f,0f),new Vector2(1f,0f),new Vector2(0f,1f), new Vector2(1f,1f)},
            new Vector2[]{ new Vector2(0f,1f),new Vector2(0f,0f),new Vector2(1f,1f), new Vector2(1f,0f)}
        };

        Vector2[] mTmpUv = new Vector2[4];

        protected Vector2[] GetUvs(Rect rect)
        {
            return GetUvs(rect, Orientation);
        }

        public virtual void Clear()
        {
            if (RecycleText)
            {
                for (int i = 0; i < mCurrentTexts.Count; i++)
                {
                    string key = ChartCommon.GetText(mCurrentTexts[i].UIText);
                    mCurrentTexts[i].SetVisible(false);
                    mCurrentTexts[i].Recycled = true;
                    if (key == null || mRecycled.ContainsKey(key))
                    {
                        mCached.Add(mCurrentTexts[i]);
                        continue;
                    }
                    mRecycled[key] = mCurrentTexts[i];
                }
            }
            mText.Clear();
            mCurrentTexts.Clear();
        }

        protected Vector2[] GetUvs(Rect rect, ChartOrientation orientaion)
        {
            Vector2[] arr;
            if (Orientation == ChartOrientation.Vertical)
                arr = mUvs[0];
            else
                arr = mUvs[1];
            if (Tile <= 0f)
            {
                for (int i = 0; i < 4; i++)
                {
                    Vector2 uv = arr[i];
                    mTmpUv[i] = new Vector2(Offset + uv.x * Length, uv.y);
                }
                return mTmpUv;
            }
            float length = rect.width;
            if (orientaion == ChartOrientation.Horizontal)
                length = rect.height;
            length /= Math.Abs(Length);
            for (int i = 0; i < 4; i++)
            {
                Vector2 uv = arr[i];
                mTmpUv[i] = new Vector2((Offset + uv.x * Length) * length / Tile, uv.y);
            }
            return mTmpUv;
        }

        void DestoryBillboard(BillboardText t)
        {
            t.Recycled = false;
            GameObject uiText = t.UIText;
            TextDirection d = t.Direction;
            if (uiText != null && uiText.gameObject != null)
                ChartCommon.SafeDestroy(uiText.gameObject);
            if (d != null && d.gameObject != null)
                ChartCommon.SafeDestroy(d.gameObject);
            if (t != null)
                ChartCommon.SafeDestroy(t.gameObject);
        }

        public void DestoryRecycled()
        {
            foreach (BillboardText t in mRecycled.Values)
            {
                mCached.Add(t);
                //                DestoryBillboard(t);
            }
            mRecycled.Clear();
        }

        public virtual BillboardText AddText(AnyChart chart, MonoBehaviour prefab, Transform parentTransform, int fontSize, float fontSharpness, string text, float x, float y, float z, float angle, object userData)
        {
            BillboardText recycled = null;
            if (RecycleText == true)
            {
                if (mRecycled.TryGetValue(text, out recycled))
                {
                    recycled.SetVisible(true);
                    recycled.Recycled = false;
                    mRecycled.Remove(text);
                }
                else
                {
                    if (mCached.Count > 0)
                    {
                        recycled = mCached[mCached.Count - 1];
                        recycled.SetVisible(true);
                        recycled.Recycled = false;
                        mCached.RemoveAt(mCached.Count - 1);
                    }
                    else
                    {
                        recycled = null;
                    }
                }
            }

            BillboardText billboardText = ChartCommon.CreateBillboardText(recycled, prefab, parentTransform, text, x, y, z, angle, null, ((IInternalUse)chart).HideHierarchy, fontSize, fontSharpness);
            if (billboardText == null)
                return null;
            billboardText.Recycled = false;
            billboardText.UserData = userData;
            if (recycled == null)
                mText.Add(billboardText);
            mCurrentTexts.Add(billboardText);
            return billboardText;
        }

        public abstract void AddQuad(UIVertex vLeftTop, UIVertex vRightTop, UIVertex vLeftBottom, UIVertex vRightBottom);
        public abstract void AddXYRect(Rect rect, int subMeshGroup, float depth);
        public abstract void AddXZRect(Rect rect, int subMeshGroup, float yPosition);
        public abstract void AddYZRect(Rect rect, int subMeshGroup, float xPosition);

    }
}
