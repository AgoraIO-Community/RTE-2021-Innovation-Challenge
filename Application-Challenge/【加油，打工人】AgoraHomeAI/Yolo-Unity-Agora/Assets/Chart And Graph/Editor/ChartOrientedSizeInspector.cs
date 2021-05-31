#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using ChartAndGraph;
using UnityEngine;

[CustomPropertyDrawer(typeof(ChartOrientedSize))]
class ChartOrientedSizeInspector : PropertyDrawer
{
    public override float GetPropertyHeight(SerializedProperty property, GUIContent label)
    {
        return EditorGUIUtility.singleLineHeight * 2;
    }

    void DoField(SerializedProperty prop, string label, Rect position)
    {
        float size = GUI.skin.label.CalcSize(new GUIContent(label)).x;
        Rect labelRect = new Rect(position.x, position.y, size, position.height);
        Rect FieldRect = new Rect(labelRect.xMax, position.y, position.width - size, position.height);
        EditorGUI.LabelField(labelRect, label);
        float val = prop.floatValue;
        EditorGUI.LabelField(labelRect, label);
        float labelWidth = EditorGUIUtility.labelWidth;
        EditorGUIUtility.labelWidth = 5;
        val = EditorGUI.FloatField(FieldRect, " ", val);
        EditorGUIUtility.labelWidth = labelWidth;
        prop.floatValue = val;
    }

    public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
    {
        
        label = EditorGUI.BeginProperty(position, label, property);
        EditorGUI.PrefixLabel(position, GUIUtility.GetControlID(FocusType.Passive), label);

        position = EditorGUI.IndentedRect(position);

        float halfWidth = position.width *0.5f;
        float y = position.y + EditorGUIUtility.singleLineHeight;
        float height = position.height - EditorGUIUtility.singleLineHeight;
        Rect breadthRect = new Rect(position.x, y, halfWidth, height);
        Rect depthRect = new Rect(breadthRect.xMax, y, halfWidth, height);
        
        int indent = EditorGUI.indentLevel;
        EditorGUI.indentLevel=0;
        SerializedProperty breadth = property.FindPropertyRelative("Breadth");
        SerializedProperty depth = property.FindPropertyRelative("Depth");
        DoField(breadth, "Breadth:", breadthRect);
        DoField(depth, "Depth:", depthRect);
        EditorGUI.indentLevel = indent;
       // EditorGUILayout.EndVertical();
        EditorGUI.EndProperty();
    }
}

