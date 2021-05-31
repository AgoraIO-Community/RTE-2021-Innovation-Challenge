#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{
    [CustomPropertyDrawer(typeof(AutoFloat))]
    class AutoFloatInspector : PropertyDrawer
    {
        public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
        {
            label = EditorGUI.BeginProperty(position, label, property);
            position = EditorGUI.PrefixLabel(position, GUIUtility.GetControlID(FocusType.Passive), label);
            SerializedProperty auto = property.FindPropertyRelative("Automatic");
            SerializedProperty val = property.FindPropertyRelative("Value");
            int indent = EditorGUI.indentLevel;
            EditorGUI.indentLevel = 0;
            bool res = EditorGUI.ToggleLeft(position,"Auto",auto.boolValue);
            EditorGUI.indentLevel = indent;
            EditorGUI.indentLevel++;
            if (auto.boolValue == false && EditorGUI.showMixedValue == false)
                val.floatValue = EditorGUILayout.FloatField("Value",val.floatValue);
            auto.boolValue = res;
            EditorGUI.indentLevel--;
            EditorGUI.EndProperty();
        }
    }
}
