#define Graph_And_Chart_PRO
//using UnityEngine;
//using System.Collections;
//using ChartAndGraph;

//public partial class HoverText
//{

//    partial void HoverStart()
//    {
//        var candle = GetComponent<CandleChart>();
//        if (candle != null)
//        {
//            mChart = candle;
//            candle.CandleHovered.AddListener(CandleHover);
//            candle.NonHovered.AddListener(NonHover);
//        }
//    }

//    void CandleHover(CandleChart.CandleEventArgs args)
//    {
//        string data = "";
//        string open, high, low, close, start, duration;
//        ((CandleChart)mChart).FormatCandleValue(args.CandleValue, fractionDigits, out open, out high, out low, out close, out start, out duration);
//        if (args.IsBodyEvent)
//            data = ((CandleChart)mChart).FormatBody(open, close, high, low, start, duration);
//        else if (args.IsHighEvent)
//            data = ((CandleChart)mChart).FormatHigh(open, close, high, low, start, duration);
//        else if (args.IsLowEvent)
//            data = ((CandleChart)mChart).FormatLow(open, close, high, low, start, duration);
//        PopText(data, args.Position, true);
//    }


//}
