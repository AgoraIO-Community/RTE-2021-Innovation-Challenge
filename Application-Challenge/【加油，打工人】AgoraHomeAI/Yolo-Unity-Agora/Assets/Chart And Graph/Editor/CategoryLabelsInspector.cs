#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;

namespace ChartAndGraph
{
    [CustomEditor(typeof(CategoryLabels))]
    class CategoryLabelsLabelsInspector : ItemLabelsBaseEditor
    {
        protected override string Name
        {
            get
            {
                return "category labels";
            }
        }

        protected override bool isSupported(AnyChart chart)
        {
            return ((IInternalUse)chart).InternalSupportsCategoryLables;
        }
    }
}
