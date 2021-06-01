#define Graph_And_Chart_PRO
using ChartAndGraph;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Reflection;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{
    [CustomEditor(typeof(AxisBase), true)]
    class AxisInspector : Editor
    {
        bool mMain = false;
        bool mSub = false;

        public override void OnInspectorGUI()
        {
            AxisBase axis = (AxisBase)target;

            if (axis.gameObject == null)
                return;

            AnyChart chart = axis.gameObject.GetComponent<AnyChart>();
            if (chart == null)
                return;
            if((chart is AxisChart) == false)
            {
                EditorGUILayout.LabelField(string.Format("Chart of type {0} does not support axis",chart.GetType().Name));
                return;
            }
            SerializedProperty simpleViewProp = serializedObject.FindProperty("SimpleView");
            if (simpleViewProp == null)
                return;

            Type canvasType = (chart is ICanvas) ? typeof(CanvasAttribute) : typeof(NonCanvasAttribute);

            EditorGUILayout.BeginVertical();
            bool negate = false;
            if (simpleViewProp.boolValue == true)
                negate = GUILayout.Button("Advanced View");
            else
                negate = GUILayout.Button("Simple View");
            if (negate)
                simpleViewProp.boolValue = !simpleViewProp.boolValue;
            bool simple = simpleViewProp.boolValue;


            SerializedProperty depth = serializedObject.FindProperty("depth");
            if (depth != null)
                EditorGUILayout.PropertyField(depth);
            SerializedProperty format= serializedObject.FindProperty("format");
            EditorGUILayout.PropertyField(format);
            if (simple)
                DoSimpleView(canvasType);
            else
                DoAdvanvedView(canvasType,true);

            EditorGUILayout.EndVertical();
            serializedObject.ApplyModifiedProperties();
            serializedObject.Update();
        }

        void SetValue(SerializedProperty assignTo,SerializedProperty from)
        {

            if(assignTo.propertyType != from.propertyType)
            {
                Debug.LogWarning("type does not match");
                return;
            }

            if (assignTo.type == "AutoFloat")
            {
                SerializedProperty auto = assignTo.FindPropertyRelative("Automatic");
                SerializedProperty val = assignTo.FindPropertyRelative("Value");
                SerializedProperty autofrom = from.FindPropertyRelative("Automatic");
                SerializedProperty valfrom = from.FindPropertyRelative("Value");
                auto.boolValue = autofrom.boolValue;
                val.floatValue = valfrom.floatValue;
                return;
            }

            switch (assignTo.propertyType)
            {
                case SerializedPropertyType.Float:
                    assignTo.floatValue = from.floatValue;
                    break;
                case SerializedPropertyType.Integer:
                    assignTo.intValue = from.intValue;
                    break;
                case SerializedPropertyType.Enum:
                    assignTo.enumValueIndex = from.enumValueIndex;
                    break;
                case SerializedPropertyType.Boolean:
                    assignTo.boolValue = from.boolValue;
                    break;
                case SerializedPropertyType.String:
                    assignTo.stringValue = from.stringValue;
                    break;
                case SerializedPropertyType.ObjectReference:
                    assignTo.objectReferenceValue = from.objectReferenceValue;
                    break;
                default:
                    Debug.LogWarning("type cannot be set");
                    break;
            }
        }

        Type getTypeFromField(Type type,string fieldName)
        {
            FieldInfo inf = type.GetField(fieldName, BindingFlags.NonPublic | BindingFlags.Instance);
            if (inf == null)
                return null;
            return inf.FieldType;
        }

        bool CompareValues(Type type,string fieldName,object a,object b)
        {
            FieldInfo inf= type.GetField(fieldName, BindingFlags.NonPublic | BindingFlags.Instance);
            object valA = inf.GetValue(a);
            object valB = inf.GetValue(b);
            if(valA is UnityEngine.Object && valB is UnityEngine.Object)
            {
                if(((UnityEngine.Object)valA) == ((UnityEngine.Object)valB))
                    return true;
                return false;
            }
            if (valA == null && valB == null)
                return true;
            return valA.Equals(valB);
        }

        void DoSimpleView(Type canvasType)
        {
            SerializedProperty it = serializedObject.FindProperty("mainDivisions");
            SerializedProperty SubDivisions = serializedObject.FindProperty("subDivisions");
            object mainDivision = ((AxisBase)target).MainDivisions;
            object subDivision = ((AxisBase)target).SubDivisions;
            if (it == null || SubDivisions == null)
               return;
            SerializedProperty end = it.GetEndProperty();
            while(it.NextVisible(true) && SerializedProperty.EqualContents(end,it) == false)
            {
                if (it.name == "SimpleView")
                    continue;
                if (ChartEditorCommon.HasAttributeOfType(typeof(ChartDivisionInfo), it.name, canvasType) == false)
                    if (ChartEditorCommon.HasAttributeOfType(typeof(AxisBase), it.name, canvasType) == false)
                        continue;
                if (ChartEditorCommon.HasAttributeOfType(typeof(AxisBase), it.name, typeof(SimpleAttribute)) == false)
                    if (ChartEditorCommon.HasAttributeOfType(typeof(ChartDivisionInfo), it.name, typeof(SimpleAttribute)) == false)
                        continue;
                SerializedProperty clone = SubDivisions.FindPropertyRelative(it.name);
                if (clone == null)
                    Debug.LogWarning("can't find property " + it.name);
                bool equal = CompareValues(typeof(ChartDivisionInfo), clone.name, mainDivision, subDivision);
                Type t = getTypeFromField(typeof(ChartDivisionInfo), clone.name);
                EditorGUI.BeginChangeCheck();
                EditorGUI.showMixedValue = !equal;
                DoMixedFiled(it, t);
                EditorGUI.showMixedValue = false;
                if (EditorGUI.EndChangeCheck())
                    SetValue(clone, it);
            }
            DoAdvanvedView(canvasType,false);
        }
        public void DoMixedFiled(SerializedProperty prop,Type type)
        {
            if(prop.type == "AutoFloat")
            {
                EditorGUILayout.BeginHorizontal();
                EditorGUILayout.PrefixLabel(prop.displayName);
                SerializedProperty auto = prop.FindPropertyRelative("Automatic");
                SerializedProperty val = prop.FindPropertyRelative("Value");
                auto.boolValue = EditorGUILayout.ToggleLeft("Auto", auto.boolValue);
                EditorGUILayout.EndHorizontal();
                EditorGUI.indentLevel++;
                if (auto.boolValue == false && EditorGUI.showMixedValue == false)
                    val.floatValue = EditorGUILayout.FloatField("Value",val.floatValue);
                EditorGUI.indentLevel--;

                return;
            }
            switch(prop.propertyType)
            {
                case SerializedPropertyType.Float:
                    if(prop.name == "FontSharpness")
                        prop.floatValue = EditorGUILayout.Slider(prop.displayName, prop.floatValue,1f,3f);
                    else
                        prop.floatValue = EditorGUILayout.FloatField(prop.displayName,prop.floatValue);
                    break;
                case SerializedPropertyType.Integer:
                    if (prop.name == "FractionDigits")
                        prop.intValue = EditorGUILayout.IntSlider(prop.displayName, prop.intValue,0,7);
                    else
                        prop.intValue = EditorGUILayout.IntField(prop.displayName, prop.intValue);
                    break;
                case SerializedPropertyType.Enum:
                    ChartDivisionAligment selected = (ChartDivisionAligment)Enum.Parse( typeof(ChartDivisionAligment), prop.enumNames[prop.enumValueIndex]);
                    Enum res = EditorGUILayout.EnumPopup(prop.displayName, selected);
                    string newName= Enum.GetName(typeof(ChartDivisionAligment), res);
                    for (int i = 0; i < prop.enumNames.Length; ++i)
                    {
                        if (prop.enumNames[i] == newName)
                        {
                            prop.enumValueIndex = i;
                            break;
                        }
                    }                    
                    break;
                case SerializedPropertyType.String:
                    prop.stringValue = EditorGUILayout.TextField(prop.displayName, prop.stringValue);
                    break;
                case SerializedPropertyType.Boolean:
                    prop.boolValue = EditorGUILayout.Toggle(prop.displayName, prop.boolValue);
                    break;
                case SerializedPropertyType.ObjectReference:
                    if(type != null)
                        prop.objectReferenceValue = EditorGUILayout.ObjectField(prop.displayName, prop.objectReferenceValue, type, true);
                    break;
                default:
                    Debug.LogWarning("type cannot be set");
                    break;
            }
        }

        void DoAdvanvedView(Type canvasType,bool includeSimple)
        {
            SerializedProperty mainDivisions = serializedObject.FindProperty("mainDivisions");
            SerializedProperty subDivisions = serializedObject.FindProperty("subDivisions");
            mMain =mainDivisions.isExpanded = EditorGUILayout.Foldout(mainDivisions.isExpanded, "Main Divisions");
            if (mMain)
            {
                EditorGUI.indentLevel++;
                
                SerializedProperty end = mainDivisions.GetEndProperty();
                while (mainDivisions.NextVisible(true) && SerializedProperty.EqualContents(mainDivisions, end) == false)
                {
                    if (ChartEditorCommon.HasAttributeOfType(typeof(ChartDivisionInfo), mainDivisions.name, canvasType) == false)
                        if (ChartEditorCommon.HasAttributeOfType(typeof(AxisBase), mainDivisions.name, canvasType) == false)
                            continue;
                    if (includeSimple == false)
                    {
                        if (ChartEditorCommon.HasAttributeOfType(typeof(ChartDivisionInfo), mainDivisions.name, typeof(SimpleAttribute)))
                            continue;
                    }
                    EditorGUILayout.PropertyField(mainDivisions);
                }
                EditorGUI.indentLevel--;
            }
            mSub = subDivisions.isExpanded = EditorGUILayout.Foldout(subDivisions.isExpanded, "Sub Divisions");
            if (mSub)
            {
                EditorGUI.indentLevel++;
                SerializedProperty end = subDivisions.GetEndProperty();
                while (subDivisions.NextVisible(true) && SerializedProperty.EqualContents(subDivisions, end) == false)
                {
                    if (ChartEditorCommon.HasAttributeOfType(typeof(ChartDivisionInfo), subDivisions.name, canvasType) == false)
                        if (ChartEditorCommon.HasAttributeOfType(typeof(AxisBase), subDivisions.name, canvasType) == false)
                            continue;
                    if (includeSimple == false)
                    {
                        if (ChartEditorCommon.HasAttributeOfType(typeof(ChartDivisionInfo), subDivisions.name, typeof(SimpleAttribute)))
                            continue;
                    }
                    EditorGUILayout.PropertyField(subDivisions);
                }
                EditorGUI.indentLevel--;
            }
        }
    }
}
