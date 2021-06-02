#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{
    [CustomPropertyDrawer(typeof(MaterialTiling))]
    class MaterialTilingInspector : PropertyDrawer
    {

        public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
        {
            label = EditorGUI.BeginProperty(position, label, property);
            position = EditorGUI.PrefixLabel(position, GUIUtility.GetControlID(FocusType.Passive), label);
            SerializedProperty auto = property.FindPropertyRelative("EnableTiling");
            SerializedProperty val = property.FindPropertyRelative("TileFactor");
            int indent = EditorGUI.indentLevel;
            EditorGUI.indentLevel = 0;
            bool res = !EditorGUI.ToggleLeft(position,"Stretch", !auto.boolValue);
            EditorGUI.indentLevel = indent;
            //EditorGUILayout.EndHorizontal();
            EditorGUI.indentLevel++;
            if (auto.boolValue == true && EditorGUI.showMixedValue == false)
            {
                EditorGUILayout.HelpBox("Remember to enable texture repeat mode in order to make tiling work !",MessageType.Warning,true);
                EditorGUILayout.HelpBox("For the best results on canvas , set the tiling factor to the pixel size of the textre", MessageType.Info, true);
                val.floatValue = EditorGUILayout.FloatField("Tiling Factor",val.floatValue);
                if (val.floatValue <= 0f)
                    val.floatValue = 0.01f;
            }
            auto.boolValue = res;
            EditorGUI.indentLevel--; 
            EditorGUI.EndProperty();
        }
    }
}
