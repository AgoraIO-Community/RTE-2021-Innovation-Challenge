#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;

namespace ChartAndGraph
{
    [CustomEditor(typeof(GroupLabels))]
    class GroupLabelsInspector : ItemLabelsBaseEditor
    {
        protected override string Name
        {
            get
            {
                return "group labels";
            }
        }

        protected override bool isSupported(AnyChart chart)
        {
            return ((IInternalUse)chart).InternalSupportsGroupLabels;
        }
    }
}
