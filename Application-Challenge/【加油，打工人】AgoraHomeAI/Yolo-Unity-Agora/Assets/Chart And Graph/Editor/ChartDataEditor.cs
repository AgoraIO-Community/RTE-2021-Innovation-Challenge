#define Graph_And_Chart_PRO
using System.Collections.Generic;
using UnityEditor;
using UnityEngine;

public class ChartDataEditor : UnityEditor.EditorWindow
{
    SerializedObject mEditedObject;
    SerializedProperty mBarData;
    SerializedProperty mCategories;
    SerializedProperty mGroups;
    SerializedProperty mData;
    Dictionary<string, SerializedProperty> mValues;

    public static ChartDataEditor ShowForObject(SerializedObject obj)
    {
        ChartDataEditor window = (ChartDataEditor)EditorWindow.GetWindow(typeof(ChartDataEditor));
        window.SetEditedObject(obj);
        return window;
    }

    public void SetEditedObject(SerializedObject obj)
    {
        mEditedObject = obj;
        mBarData = mEditedObject.FindProperty("Data");
        mCategories = mBarData.FindPropertyRelative("mCategories");
        mGroups = mBarData.FindPropertyRelative("mGroups");
        mData = mBarData.FindPropertyRelative("mData");
        LoadData();
        

    }

    void LoadData()
    {
        if(mValues == null)
            mValues = new Dictionary<string, SerializedProperty>();
        mValues.Clear();
        int size = mData.arraySize;
        for (int i = 0; i < size; i++)
        {
            SerializedProperty prop = mData.GetArrayElementAtIndex(i);
            string columnName = prop.FindPropertyRelative("ColumnName").stringValue;
            string rowName = prop.FindPropertyRelative("GroupName").stringValue;
            SerializedProperty amount = prop.FindPropertyRelative("Amount");
            string keyName = getKey(columnName, rowName);
            mValues[keyName] = amount;
        }
    }

    string getKey(string column,string row)
    {
        return string.Format("{0}|{1}", column, row);
    }
    
    void OnGUI()
    {
        GUILayout.Label("Edit Values", EditorStyles.boldLabel);
        GUILayout.BeginVertical();
        int categoryCount = mCategories.arraySize;
        int groupCount = mGroups.arraySize;
        GUILayout.BeginHorizontal();
        
        GUILayout.Label(" ",GUILayout.Width(EditorGUIUtility.fieldWidth));
        for (int i = 0; i < groupCount; i++)
        {
            string group = mGroups.GetArrayElementAtIndex(i).stringValue;
            GUILayout.Label(group, GUILayout.Width(EditorGUIUtility.fieldWidth));
        }
        GUILayout.EndHorizontal();
        for (int i=0; i<categoryCount; i++)
        {
            SerializedProperty catProp = mCategories.GetArrayElementAtIndex(i);
            string category = catProp.FindPropertyRelative("Name").stringValue;
            GUILayout.BeginHorizontal();
            GUILayout.Label(category, GUILayout.Width(EditorGUIUtility.fieldWidth));
            for (int j=0; j<groupCount; j++)
            {
                string group = mGroups.GetArrayElementAtIndex(j).stringValue;
                string keyName = getKey(category, group);
                double value =0.0;
                SerializedProperty prop;
                if (mValues.TryGetValue(keyName, out prop))
                    value = prop.doubleValue;
                else
                    prop = null;
                double newVal = EditorGUILayout.DoubleField(value, GUILayout.Width(EditorGUIUtility.fieldWidth));
                if(newVal != value)
                    prop.doubleValue = newVal;
            }
            GUILayout.EndHorizontal();
        }
        GUILayout.EndVertical();
        mEditedObject.ApplyModifiedProperties();
    }
}
