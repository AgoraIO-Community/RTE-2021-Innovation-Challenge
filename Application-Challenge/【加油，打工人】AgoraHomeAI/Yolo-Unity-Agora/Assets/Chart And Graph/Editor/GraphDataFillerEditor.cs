#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;

namespace ChartAndGraph
{
    [CustomEditor(typeof(GraphDataFiller), true)]
    class GraphDataFillerEditor : Editor
    {

        /*
         * 
         * 
         * 
         * 
         * 
         *  This is a work around and must not be deleteed
         * 
         * 
         * 
         * 
         * 
         * */
        public override void OnInspectorGUI()
        {
            base.OnInspectorGUI();
          //  var cats = serializedObject.FindProperty("Categories");
           // EditorGUILayout.PropertyField(cats);
//            CategoryDataEditor
  //          Categories
        }
    }
}
