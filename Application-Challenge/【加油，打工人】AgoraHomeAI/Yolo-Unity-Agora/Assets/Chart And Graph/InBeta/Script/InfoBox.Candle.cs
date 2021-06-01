#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    partial class InfoBox
    {
        public CandleChart[] CandleChart;
        void CandleClicked(CandleChart.CandleEventArgs args)
        {
            if (args.IsBodyEvent)
                infoText.text = string.Format("{0} : Candle Body Clicked , O:{1},C:{2}", args.Category, args.CandleValue.Open, args.CandleValue.Close);
            if (args.IsHighEvent)
                infoText.text = string.Format("{0} : Candle High Clicked , H:{1}", args.Category, args.CandleValue.High);
            if (args.IsLowEvent)
                infoText.text = string.Format("{0} : Candle Low Clicked , L:{1}", args.Category, args.CandleValue.Low);
        }

        void CandleHovered(CandleChart.CandleEventArgs args)
        {
            if (args.IsBodyEvent)
                infoText.text = string.Format("{0} : Candle Body  , O:{1},C:{2}", args.Category, args.CandleValue.Open, args.CandleValue.Close);
            if (args.IsHighEvent)
                infoText.text = string.Format("{0} : Candle High  , H:{1}", args.Category, args.CandleValue.High);
            if (args.IsLowEvent)
                infoText.text = string.Format("{0} : Candle Low , L:{1}", args.Category, args.CandleValue.Low);
        }

        partial void HookCandle()
        {

            if (CandleChart != null)
            {
                foreach (CandleChart candle in CandleChart)
                {
                    if (candle == null)
                        return;
                    candle.CandleHovered.AddListener(CandleHovered);
                    candle.CandleClicked.AddListener(CandleClicked);
                    candle.NonHovered.AddListener(NonHovered);
                }
            }
        }
    }
}
