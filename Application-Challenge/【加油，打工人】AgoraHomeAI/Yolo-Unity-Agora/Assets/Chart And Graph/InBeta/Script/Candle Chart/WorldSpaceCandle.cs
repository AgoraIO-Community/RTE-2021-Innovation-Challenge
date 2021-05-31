#define Graph_And_Chart_PRO


//using System;
//using System.Collections.Generic;
//using System.Linq;
//using System.Text;
//using UnityEngine;

//namespace ChartAndGraph
//{
//    class WorldSpaceCandle : MonoBehaviour, ICandleCreator
//    {
//        public GameObject Prefab = null;
//        GameObjectPool<ChartItem> mPooledCandles = new GameObjectPool<ChartItem>();
//        List<ChartItem> mCandles = new List<ChartItem>();
       
//        class CandleObject
//        { 
//            public GameObject upper;
//            public GameObject lower;
//            public GameObject body;
//        }

//        GameObject CreatePrefab(Transform parent,float centerX, float fromY, float width, float toY)
//        {
//            GameObject obj = GameObject.Instantiate(Prefab);
//            ChartCommon.EnsureComponent<ChartItem>(obj);
//            obj.transform.SetParent((parent == null) ? transform : parent, true);
//            float centerY = (fromY + toY) * 0.5f;
//            float height = Mathf.Abs(fromY - toY);
//            obj.transform.position = new Vector3(centerX, centerY, 0f);
//            obj.transform.rotation = Quaternion.identity;
//            obj.transform.localScale = new Vector3(width * 2f, height, 1f);
//            return obj;
//        }

//        ChartItem CreateCandle(CandleChartData.CandleValue candle, CandleChartData.CandleSettings settings)
//        {
//            float max = (float)candle.Max;
//            float min = (float)candle.Min;
//            float midX = (float)(candle.Start + (candle.Duration * 0.5));
//           // float midY = (max + min) * 0.5f;

//            GameObject candleGameobj = ChartCommon.CreateChartItem();
//            candleGameobj.transform.SetParent(transform);
//            candleGameobj.transform.position = new Vector3(midX, 0f, 0f);
//            CandleObject candleObj = new CandleObject();
           
//            candleObj.upper = CreatePrefab(candleGameobj.transform,0f, (float)candle.High, (float)settings.LineThickness, max);
//            candleObj.lower = CreatePrefab(candleGameobj.transform,0f, (float)candle.Low, (float)settings.LineThickness, min);
//            candleObj.body = CreatePrefab(candleGameobj.transform,0f, min, (float)settings.CandleThicknessMultiplier, max);

//            SetMaterial(candleObj.upper, settings.Line);
//            SetMaterial(candleObj.lower, settings.Line);
//            SetMaterial(candleObj.body, settings.Fill);

//            ChartItem t = candleGameobj.GetComponent<ChartItem>();
//            t.TagData = candleObj;

//            return t;
//        }

//        void ClearCandles()
//        {
//            for(int i=0; i<mCandles.Count; i++)
//            {
//                if (mCandles[i] != null)
//                {
//                    ChartCommon.SafeDestroy(mCandles[i].gameObject);
//                }
//            }
//            mCandles.Clear();
//            mPooledCandles.DestoryAll();
//        }

//        void SetMaterial(GameObject obj, Material mat)
//        {
//            Renderer rend = obj.GetComponent<Renderer>();
//            if (rend != null)
//                rend.material = mat;
//        }
        
//        public void Generate(CandleChart parent,Rect viewRect, IList<CandleChartData.CandleValue> value, CandleChartData.CandleSettings settings)
//        {
//            if ((parent is ICanvas))
//            {
//                Debug.LogWarning("prefab is meant not meant to be used with canvas candle chart");
//                return;
//            }

//            ClearCandles();
            
//            for (int i=0; i<value.Count; i++)
//            {
//                ChartItem candle = CreateCandle(value[i], settings);
//                mCandles.Add(candle);
//            }
//        }
//    }
//}
