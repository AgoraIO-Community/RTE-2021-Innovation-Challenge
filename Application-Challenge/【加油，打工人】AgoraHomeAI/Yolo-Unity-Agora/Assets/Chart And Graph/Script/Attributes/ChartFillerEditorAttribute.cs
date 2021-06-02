#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;

namespace ChartAndGraph
{
    [AttributeUsage(AttributeTargets.Field, AllowMultiple = true)]
    public class ChartFillerEditorAttribute : Attribute
    {
        public GraphDataFiller.DataType ShowForType;
        public ChartFillerEditorAttribute(GraphDataFiller.DataType type)
        {
            ShowForType = type;
        }
        public BarDataFiller.DataType ShowForBarType;
        public ChartFillerEditorAttribute(BarDataFiller.DataType type)
        {
            ShowForBarType = type;
        }

        public RadarDataFiller.DataType ShowForRadarType;
        public ChartFillerEditorAttribute(RadarDataFiller.DataType type)
        {
            ShowForRadarType = type;
        }
    }
}
