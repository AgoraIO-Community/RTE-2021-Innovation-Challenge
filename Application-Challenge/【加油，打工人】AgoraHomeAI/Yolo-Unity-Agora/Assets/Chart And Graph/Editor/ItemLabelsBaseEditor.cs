#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;

namespace ChartAndGraph
{
    abstract class ItemLabelsBaseEditor : Editor
    {
        protected abstract string Name { get; }
        protected abstract bool isSupported(AnyChart chart);
        public override void OnInspectorGUI()
        {
            

            ItemLabelsBase labels = (ItemLabelsBase)target;

            if (labels.gameObject == null)
                return;

            AnyChart chart = labels.gameObject.GetComponent<AnyChart>();
            if (chart == null)
                return;
            if (isSupported(chart) == false)
            {
                EditorGUILayout.HelpBox(string.Format("Chart of type {0} does not support {1}", chart.GetType().Name,Name),MessageType.Warning);
                return;
            }
            base.OnInspectorGUI();
        }

    }
}
