#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{
    [CustomPropertyDrawer(typeof(GraphDataFiller.CategoryData))]
    class CategoryDataEditor  : PropertyDrawer
    {
        public override float GetPropertyHeight(SerializedProperty property, GUIContent label)
        {
            
            return -2f;
        }

        public override void OnGUI(Rect position, SerializedProperty property, GUIContent label)
        {
            EditorGUI.BeginProperty(position, label, property);
            var enabled = property.FindPropertyRelative("Enabled");
            
            property.isExpanded = EditorGUILayout.Foldout(property.isExpanded, property.displayName);
            if(property.isExpanded)
            {
                EditorGUI.indentLevel++;
                EditorGUILayout.PropertyField(enabled);
                if (enabled.boolValue == true)
                {
                    var dataType = property.FindPropertyRelative("DataType");
                    EditorGUILayout.PropertyField(dataType);
                    int item = dataType.enumValueIndex;
                    var iterator = property.Copy();
                    var end = iterator.GetEndProperty();
                    bool hasNext = iterator.NextVisible(true);
                    Type t = typeof(GraphDataFiller.CategoryData);
                    while (hasNext)
                    {
                        if ((SerializedProperty.EqualContents(iterator, end)))
                            break;
                        bool show = false;

                        foreach (object attrb in t.GetField(iterator.name).GetCustomAttributes(false))
                        {
                            var cast = attrb as ChartFillerEditorAttribute;
                            if (cast != null)
                            {
                                if ((int)cast.ShowForType == item)
                                {
                                    show = true;
                                    break;
                                }
                            }
                        }
                        if (show)
                            EditorGUILayout.PropertyField(iterator);
                        //    Debug.Log(iterator.displayName);
                        hasNext = iterator.NextVisible(false);
                    }
                }
            }
            EditorGUI.indentLevel--;
            EditorGUI.EndProperty();
        }
    }
}
