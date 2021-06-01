#define Graph_And_Chart_PRO

using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    class CanvasCandle : MonoBehaviour, ICandleCreator
    {
        List<CandleChartData.CandleValue> mEmptyCandle = new List<CandleChartData.CandleValue>();
        HashSet<CanvasCandleGraphic> mOccupied = new HashSet<CanvasCandleGraphic>();
        CanvasCandleGraphic mCandle, mLine;
        CanvasCandleGraphic mOutline;
        /// <summary>
        /// The selected index is hovered about the specified point
        /// </summary>
        public event EventHandlingGraphic.GraphicEvent Hover;
        /// <summary>
        /// The selected index is clicked about the specified point
        /// </summary>
        public event EventHandlingGraphic.GraphicEvent Click;
        /// <summary>
        /// The currently hovered and selected objects are no longer selected or hovered. 
        /// </summary>
        public event Action Leave;

        private CanvasCandleGraphic CreateCandleGraphic()
        {
            GameObject obj = ChartCommon.CreateCanvasChartItem();
            CanvasCandleGraphic graphic = obj.AddComponent<CanvasCandleGraphic>();
            graphic.maskable = true;
            RectTransform rect = obj.GetComponent<RectTransform>();
            rect.SetParent(transform, false);
            rect.anchoredPosition = Vector3.zero;
            rect.rotation = Quaternion.identity;
            rect.localScale = new Vector3(1f, 1f, 1f);
            HookEventsForGraphic(graphic);
            return graphic;
        }

        void HookEventsForGraphic(CanvasCandleGraphic graphic)
        {
            graphic.Hover += (index, type, data, position) => { Candle_Hover(graphic, index, type, data, position); };
            graphic.Click += (index, type, data, position) => { Candle_Click(graphic, index, type, data, position); }; ;
            graphic.Leave += () => { Candle_Leave(graphic); };
        }

        public void Clear()
        {
            ChartCommon.SafeDestroy(mCandle);
            ChartCommon.SafeDestroy(mLine);
            ChartCommon.SafeDestroy(mOutline);
        }
        public void SetRefrenceIndex(int index)
        {
            if (mCandle != null)
                mCandle.SetRefrenceIndex(index);
            if (mLine != null)
                mCandle.SetRefrenceIndex(index);
            if (mOutline != null)
                mOutline.SetRefrenceIndex(index);

        }
        public void Generate(CandleChart parent, Rect viewRect, IList<CandleChartData.CandleValue> value, CandleChartData.CandleSettings settings)
        {
       
            if (parent.IsCanvas == false)
            {
                Debug.LogWarning("prefab is meant to be used with canvas candle chart only");
                return;
            }
            if (mCandle == null)
                mCandle = CreateCandleGraphic();
            if (settings.Fill == null || settings.CandleThicknessMultiplier < 0.0001f)
                mCandle.SetCandle(0, mEmptyCandle, settings);
            else
                mCandle.SetCandle(0, value, settings);
            mCandle.HoverTransform(transform);
            mCandle.SetViewRect(viewRect, new Rect());
            mCandle.SetHoverPrefab(settings.CandleHoverPrefab);
            mCandle.material = settings.Fill;


            if (mLine == null)
                mLine = CreateCandleGraphic();
            if (settings.Line == null || settings.LineThickness < 0.0001f)
                mLine.SetCandle(1, mEmptyCandle, settings);
            else
                mLine.SetCandle(1, value, settings);
            mLine.HoverTransform(transform);
            mLine.SetHoverPrefab(settings.CandleHoverPrefab);
            mLine.material = settings.Line;
            mLine.SetViewRect(viewRect, new Rect());

            if (mOutline == null)
                mOutline = CreateCandleGraphic();
            mOutline.material = settings.Outline;
            mOutline.SetViewRect(viewRect, new Rect());
            if (settings.Outline == null || settings.OutlineThickness < 0.0001f)
                mOutline.SetCandle(2, mEmptyCandle, settings);
            else
                mOutline.SetCandle(2, value, settings);
        }

        private void Candle_Leave(CanvasCandleGraphic graphic)
        {
            if (mOccupied.Remove(graphic))
            {
                if (mOccupied.Count == 0)
                {
                    if (Leave != null)
                        Leave();
                }
            }
        }

        private void Candle_Click(CanvasCandleGraphic graphic, int index, int type, object data, Vector2 position)
        {
            mOccupied.Add(graphic);
            position = graphic.transform.TransformPoint(position);
            if (Click != null)
                Click(index, type, data, position);
        }

        private void Candle_Hover(CanvasCandleGraphic graphic, int index, int type, object data, Vector2 position)
        {
            mOccupied.Add(graphic);
            position = graphic.transform.TransformPoint(position);
            if (Hover != null)
                Hover(index, type, data, position);
        }
    }
}
