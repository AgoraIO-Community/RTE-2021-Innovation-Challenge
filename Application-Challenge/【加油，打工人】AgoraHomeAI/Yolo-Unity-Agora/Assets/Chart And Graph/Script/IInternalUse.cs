#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;

namespace ChartAndGraph
{
    /// <summary>
    /// this interface is used internally by the charting library and should not be accessed
    /// </summary>
    public interface IInternalUse
    {
        CategoryLabels CategoryLabels { get; set; }
        ItemLabels ItemLabels { get; set; }
        GroupLabels GroupLabels { get; set; }
        HorizontalAxis HorizontalAxis { get; set; }
        VerticalAxis VerticalAxis { get; set; }
        Camera InternalTextCamera { get; }
        float InternalTextIdleDistance { get; }
        TextController InternalTextController { get; }
        LegenedData InternalLegendInfo { get; }
        bool InternalHasValues(AxisBase axis);
        double InternalMaxValue(AxisBase axis);
        double InternalMinValue(AxisBase axis);
        void InternalItemSelected(object userData);
        void InternalItemLeave(object userData);
        void InternalItemHovered(object userData);
        void CallOnValidate();
        float InternalTotalWidth { get; }
        float InternalTotalDepth { get; }
        float InternalTotalHeight { get; }
        bool InternalSupportsItemLabels { get; }
        bool InternalSupportsCategoryLables { get; }
        bool InternalSupportsGroupLabels { get; }
        event Action Generated;
        bool HideHierarchy { get; }
    }
}
