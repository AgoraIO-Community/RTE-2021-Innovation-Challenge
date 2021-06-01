#define Graph_And_Chart_PRO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.UI;

namespace ChartAndGraph
{
    class CanvasCandleGraphic : EventHandlingGraphic
    {
        CandleChartData.CandleSettings mCandleSettings;
        List<CandleChartData.CandleValue> mCandles = new List<CandleChartData.CandleValue>();

        int mPart;
        UIVertex[] mTmpVerts = new UIVertex[4];
        Vector2 mMin, mMax;

        protected override Vector2 Min
        {
            get
            {
                return mMin;
            }
        }

        protected override Vector2 Max
        {
            get
            {
                return mMax;
            }
        }

        protected Rect RectFromIndex(int index, int type, Rect Default)
        {
            if (index >= mCandles.Count)
                return Default;
            CandleChartData.CandleValue candle = mCandles[index];
            float max = (float)candle.Max;
            float min = (float)candle.Min;
            float start = (float)candle.Start;
            float end = (float)(candle.Start + candle.Duration);
            float mid = (start + end) * 0.5f;

            if (type == 0)
                return ChartCommon.RectFromCenter(mid, (float)mCandleSettings.LineThickness, max, (float)candle.High);
            if (type == 2)
                return ChartCommon.RectFromCenter(mid, (float)mCandleSettings.LineThickness, (float)candle.Low, min);
            return ChartCommon.RectFromCenter(mid, (float)(mCandleSettings.CandleThicknessMultiplier * candle.Duration), min, max);
        }

        protected override void SetUpHoverObject(ChartItemEffect hover, int index, int type, object selectionData)
        {
            Rect selectionRect = (Rect)selectionData;
            selectionRect = RectFromIndex(index, type, selectionRect);
            hover.ItemData = selectionRect;
            SetupHoverObjectToRect(hover, index, type, selectionRect);
        }

        protected void PickLine(Vector3 mouse, out int pickedIndex, out int pickedType, out object selectionData)
        {
            pickedIndex = -1;
            pickedType = -1;
            selectionData = null;

            for (int i = 0; i < mCandles.Count; i++)
            {
                CandleChartData.CandleValue candle = mCandles[i];

                float max = (float)candle.Max;
                float min = (float)candle.Min;
                float start = (float)candle.Start;
                float end = (float)(candle.Start + candle.Duration);
                float mid = (start + end) * 0.5f;

                Rect high = ChartCommon.RectFromCenter(mid, (float)mCandleSettings.LineThickness, max, (float)candle.High);
                Rect low = ChartCommon.RectFromCenter(mid, (float)mCandleSettings.LineThickness, (float)candle.Low, min);
                if (high.Contains(mouse))
                {
                    selectionData = high;
                    pickedType = 0;
                    pickedIndex = i;
                    return;
                }

                if (low.Contains(mouse))
                {
                    selectionData = low;
                    pickedType = 2;
                    pickedIndex = i;
                    return;
                }
            }
        }

        protected void PickBody(Vector3 mouse, out int pickedIndex, out int pickedType, out object selectionData)
        {
            pickedIndex = -1;
            pickedType = -1;
            selectionData = null;
            for (int i = 0; i < mCandles.Count; i++)
            {
                CandleChartData.CandleValue candle = mCandles[i];
                float max = (float)candle.Max;
                float min = (float)candle.Min;
                float start = (float)candle.Start;
                float end = (float)(candle.Start + candle.Duration);
                float mid = (start + end) * 0.5f;
                Rect Body = ChartCommon.RectFromCenter(mid, (float)(mCandleSettings.CandleThicknessMultiplier * candle.Duration), min, max);

                if (Body.Contains(mouse))
                {
                    selectionData = Body;
                    pickedType = 1;
                    pickedIndex = i;
                    return;
                }
            }
        }

        protected override void Pick(Vector3 mouse, out int pickedIndex, out int pickedType, out object selectionData)
        {
            if (mPart == 0)
                PickBody(mouse, out pickedIndex, out pickedType, out selectionData);
            else
                PickLine(mouse, out pickedIndex, out pickedType, out selectionData);
            if (pickedIndex >= 0)
                pickedIndex += refrenceIndex;
        }

        protected override float MouseInThreshold
        {
            get
            {
                return Sensitivity;
            }
        }

        public void ClearCandles()
        {
            mCandles = null;
            SetAllDirty();
            Rebuild(CanvasUpdate.PreRender);
        }

        public void SetCandle(int part, IList<CandleChartData.CandleValue> candles, CandleChartData.CandleSettings settings)
        {
            mPart = part;
            mCandles.Clear();
            mCandles.AddRange(candles);
            mCandleSettings = settings;
            double minX = double.PositiveInfinity;
            double minY = double.PositiveInfinity;
            double maxX = double.NegativeInfinity;
            double maxY = double.NegativeInfinity;

            for (int i = 0; i < mCandles.Count; i++)
            {
                var candle = mCandles[i];
                minY = Math.Min(candle.LowBound, minY);
                maxY = Math.Max(candle.HighBound, maxY);
                minX = Math.Min(minX, candle.Start);
                maxX = Math.Max(maxX, candle.Start + candle.Duration);
            }
            mMin = new Vector2((float)minX, (float)minY);
            mMax = new Vector2((float)maxX, (float)maxY);

            SetAllDirty();
            Rebuild(CanvasUpdate.PreRender);
            SetUpAllHoverObjects();


        }

        IEnumerable<UIVertex> getOutline()
        {
            UIVertex v = new UIVertex();

            if (mCandles == null)
                yield break;

            float outlineThickness = (float)mCandleSettings.OutlineThickness * 0.5f;
            for (int i = 0; i < mCandles.Count; i++)
            {
                CandleChartData.CandleValue candle = mCandles[i];
                float max = (float)Math.Max(candle.Open, candle.Close);
                float min = (float)Math.Min(candle.Open, candle.Close);
                float start = (float)candle.Start;
                float end = (float)(candle.Start + candle.Duration);
                float mid = (start + end) * 0.5f;
                float thickness = (float)(mCandleSettings.CandleThicknessMultiplier * candle.Duration * 0.5);

                //long and dirty part the defines all the verices of the candle outline

                //outline of the body
                v.position = new Vector3(mid - thickness - outlineThickness, max, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;
                v.position = new Vector3(mid - thickness + outlineThickness, max, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;
                v.position = new Vector3(mid - thickness - outlineThickness, min, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;
                v.position = new Vector3(mid - thickness + outlineThickness, min, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

                //outline of the body
                v.position = new Vector3(mid + thickness - outlineThickness, max, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;
                v.position = new Vector3(mid + thickness + outlineThickness, max, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;
                v.position = new Vector3(mid + thickness - outlineThickness, min, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;
                v.position = new Vector3(mid + thickness + outlineThickness, min, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;


                //outline of the high line
                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness - outlineThickness, (float)candle.High, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness + outlineThickness, (float)candle.High, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness - outlineThickness, max, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness + outlineThickness, max, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

                //outline of the high line
                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness - outlineThickness, (float)candle.High, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness + outlineThickness, (float)candle.High, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness - outlineThickness, max, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness + outlineThickness, max, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;


                //outline of the low line
                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness - outlineThickness, min, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness + outlineThickness, min, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness - outlineThickness, (float)candle.Low, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness + outlineThickness, (float)candle.Low, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;


                //outline of the low line
                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness - outlineThickness, min, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness + outlineThickness, min, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness - outlineThickness, (float)candle.Low, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness + outlineThickness, (float)candle.Low, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;


                //outline of the low line connection with body
                v.position = new Vector3(mid - thickness - outlineThickness, min + outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, min + outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid - thickness - outlineThickness, min - outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, min - outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

                //outline of the low line connection with body
                v.position = new Vector3(mid + thickness + outlineThickness, min + outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, min + outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid + thickness + outlineThickness, min - outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, min - outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

                //outline of the high line connection with body
                v.position = new Vector3(mid - thickness - outlineThickness, max - outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, max - outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid - thickness - outlineThickness, max + outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, max + outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

                //outline of the high line connection with body
                v.position = new Vector3(mid + thickness + outlineThickness, max - outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, max - outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid + thickness + outlineThickness, max + outlineThickness, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, max + outlineThickness, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

            }
        }
        IEnumerable<UIVertex> getCandle()
        {
            UIVertex v = new UIVertex();

            if (mCandles == null)
                yield break;

            for (int i = 0; i < mCandles.Count; i++)
            {
                CandleChartData.CandleValue candle = mCandles[i];
                float max = (float)Math.Max(candle.Open, candle.Close);
                float min = (float)Math.Min(candle.Open, candle.Close);
                float start = (float)candle.Start;
                float end = (float)(candle.Start + candle.Duration);
                float mid = (start + end) * 0.5f;
                float thickness = (float)(mCandleSettings.CandleThicknessMultiplier * candle.Duration * 0.5);

                v.position = new Vector3(mid - thickness, max, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;
                v.position = new Vector3(mid + thickness, max, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;
                v.position = new Vector3(mid - thickness, min, 0f);
                v.uv0 = new Vector2(-0f, 1f);
                yield return v;
                v.position = new Vector3(mid + thickness, min, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;

            }
        }

        IEnumerable<UIVertex> getLine()
        {
            UIVertex v = new UIVertex();
            if (mCandles == null)
                yield break;
            for (int i = 0; i < mCandles.Count; i++)
            {
                CandleChartData.CandleValue candle = mCandles[i];
                float max = (float)Math.Max(candle.Open, candle.Close);
                float min = (float)Math.Min(candle.Open, candle.Close);

                float start = (float)candle.Start;
                float end = (float)(candle.Start + candle.Duration);
                float mid = (start + end) * 0.5f;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, (float)candle.High, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, (float)candle.High, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, max, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, max, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;



                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, min, 0f);
                v.uv0 = new Vector2(0f, 0f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, min, 0f);
                v.uv0 = new Vector2(1f, 0f);
                yield return v;

                v.position = new Vector3(mid - (float)mCandleSettings.LineThickness, (float)candle.Low, 0f);
                v.uv0 = new Vector2(0f, 1f);
                yield return v;

                v.position = new Vector3(mid + (float)mCandleSettings.LineThickness, (float)candle.Low, 0f);
                v.uv0 = new Vector2(1f, 1f);
                yield return v;


            }
        }

        IEnumerable<UIVertex> getVerices()
        {
            if (mCandles == null)
                return new UIVertex[0];
            if (mPart == 0)
                return getCandle();
            else if (mPart == 1)
                return getLine();
            return getOutline();
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
