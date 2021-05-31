#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{
    [CustomEditor(typeof(GraphChartBase), true)]
    class GraphChartInspector : Editor
    {
        string mEditedCategory = "";
        bool mCategories = false;
        string mCategoryError = null;
        string mNewCategoryName = "";
        GUIStyle mRedStyle;
        GUIStyle mBold;
        HashSet<string> mAllNames = new HashSet<string>();
        GUIStyle mSplitter;
        List<int> mToRemove = new List<int>();
        List<int> mToUp = new List<int>();
        Dictionary<string, string> mOperations = new Dictionary<string, string>();
        Texture mSettings;
        GraphDataEditor mWindow;
        RenameWindow mRenameWindow = null;

        public void OnEnable()
        {
            mRedStyle = new GUIStyle();
            mRedStyle.normal.textColor = Color.red;

            mSplitter = new GUIStyle();
            mSplitter.normal.background = EditorGUIUtility.whiteTexture;
            mSplitter.stretchWidth = true;
            mSplitter.margin = new RectOffset(0, 0, 7, 7);
        }

        public void Splitter()
        {
            Rect position = GUILayoutUtility.GetRect(GUIContent.none, mSplitter, GUILayout.Height(1f));
            if (Event.current.type == EventType.Repaint)
            {
                Color restoreColor = GUI.color;
                GUI.color = new Color(0.5f, 0.5f, 0.5f);
                mSplitter.Draw(position, false, false, false, false);
                GUI.color = restoreColor;
            }
        }

        private static bool IsAlphaNum(string str)
        {
            if (string.IsNullOrEmpty(str))
                return false;

            for (int i = 0; i < str.Length; i++)
            {
                if (!(char.IsLetter(str[i])) && (!(char.IsNumber(str[i]))) && str[i] != ' ')
                    return false;
            }

            return true;
        }

        private void DoOperations(SerializedProperty items, int size, string type)
        {
            mToRemove.Clear();
            mToUp.Clear();
            bool up = false;
            for (int i = 0; i < size; i++)
            {
                SerializedProperty entry = items.GetArrayElementAtIndex(i);
                if (entry == null)
                    continue;
                SerializedProperty nameProp = entry.FindPropertyRelative("Name");
                string name = null;
                if (nameProp == null)
                    name = entry.stringValue;
                else
                    name = nameProp.stringValue;

                string arg = type + "|" + name;
                string res = null;

                if (up == true)
                {
                    mToUp.Add(i);
                    up = false;
                }

                if (mOperations.TryGetValue(arg, out res))
                {
                    if (res == "remove")
                        mToRemove.Add(i);
                    if (res == "up" && i > 0)
                        mToUp.Add(i);
                    if (res == "down")
                        up = true;
                    mOperations.Remove(arg);
                }
            }
            for (int i = 0; i < mToRemove.Count; i++)
            {
                items.DeleteArrayElementAtIndex(mToRemove[i]);
            }
            for (int i = 0; i < mToUp.Count; i++)
            {
                int cur = mToUp[i];
                items.MoveArrayElement(cur, cur - 1);
            }
        }
        private void DataEditor(SerializedProperty data, string type, string property, string caption)
        {

        }
        private void NamedItemEditor(SerializedProperty data, string type, string property, string caption, ref string errorMessage, ref bool foldout, ref string newName)
        {
            SerializedProperty items = data.FindPropertyRelative(property);
            items.isExpanded = EditorGUILayout.Foldout(items.isExpanded, caption);
            //bool up, down;
            mAllNames.Clear();
            int size = items.arraySize;
            if (Event.current.type == EventType.Layout)
                DoOperations(items, size, type);
            size = items.arraySize;
            if (items.isExpanded)
            {
                EditorGUI.indentLevel++;
                for (int i = 0; i < size; i++)
                {
                    SerializedProperty entry = items.GetArrayElementAtIndex(i);
                    if (entry == null)
                        continue;

                    SerializedProperty nameProp = entry.FindPropertyRelative("Name");
                    string name = null;
                    if (nameProp == null)
                        name = entry.stringValue;
                    else
                        name = nameProp.stringValue;
                    mAllNames.Add(name);
                    bool toogle = false;
                    EditorGUILayout.BeginHorizontal();
                    if (nameProp != null)
                        toogle = entry.isExpanded = EditorGUILayout.Foldout(entry.isExpanded, name);
                    else
                    {
                        toogle = false;
                        EditorGUILayout.LabelField(name);
                    }
                    GUILayout.FlexibleSpace();
                    if (GUILayout.Button("..."))
                        DoContext(type, name);
                    EditorGUILayout.EndHorizontal();
                    if (toogle)
                    {
                        EditorGUI.indentLevel++;
                        if (nameProp != null)
                        {
                            SerializedProperty end = entry.GetEndProperty(true);
                            entry.Next(true);
                            if (SerializedProperty.EqualContents(entry, end) == false)
                            {
                                do
                                {
                                    if (entry.name != "Name" && entry.name != "data")
                                    {
                                        if (entry.name == "ViewOrder")
                                            continue;
                                        if (target is GraphChart) // canvas graph chart
                                        {
                                            if ((entry.name == "LinePrefab") || (entry.name == "FillPrefab") || (entry.name == "DotPrefab") || (entry.name == "Depth"))
                                            {
                                                continue;
                                            }
                                        }
                                        else
                                        {
                                            if ((entry.name == "LineHoverPrefab") || (entry.name == "PointHoverPrefab"))
                                                continue;
                                        }
                                        EditorGUILayout.PropertyField(entry, true);
                                    }
                                }
                                while (entry.Next(false) && SerializedProperty.EqualContents(entry, end) == false);
                                GUILayout.BeginHorizontal();
                                GUILayout.Space(EditorGUI.IndentedRect(EditorGUILayout.GetControlRect()).xMin);
                                if (GUILayout.Button("Edit Values..."))
                                {
                                    mEditedCategory = name;
                                    if (mWindow == null)
                                        mWindow = GraphDataEditor.ShowForObject(serializedObject, mEditedCategory);
                                }
                                GUILayout.EndHorizontal();
                            }
                        }

                        EditorGUI.indentLevel--;
                    }
                }

                if (errorMessage != null)
                    EditorGUILayout.LabelField(errorMessage, mRedStyle);
                EditorGUILayout.LabelField(string.Format("Add new {0} :", type));
                //Rect indentAdd = EditorGUI.IndentedRect(new Rect(0f, 0f, 1000f, 1000f));
                EditorGUILayout.BeginHorizontal();
                newName = EditorGUILayout.TextField(newName);
                //GUILayout.Space(indentAdd.xMin);
                if (GUILayout.Button("Add"))
                {
                    bool error = false;
                    if (newName.Trim().Length == 0)
                    {
                        errorMessage = "Name can't be empty";
                        error = true;
                    }
                    else if (IsAlphaNum(newName) == false)
                    {
                        errorMessage = "Name conatins invalid characters";
                        error = true;
                    }
                    else if (mAllNames.Contains(newName))
                    {
                        errorMessage = string.Format("A {0} named {1} already exists in this chart", type, newName);
                        error = true;
                    }
                    if (error == false)
                    {
                        errorMessage = null;
                        items.InsertArrayElementAtIndex(size);
                        SerializedProperty newItem = items.GetArrayElementAtIndex(size);
                        SerializedProperty newItemName = newItem.FindPropertyRelative("Name");
                        if (newItemName == null)
                            newItem.stringValue = newName;
                        else
                            newItemName.stringValue = newName;
                        newName = "";
                        UpdateWindow();
                    }
                }
                EditorGUILayout.EndHorizontal();
                EditorGUI.indentLevel--;
            }
            else
            {
                errorMessage = null;
            }
            UpdateWindow();
        }
        void callback(object val)
        {
            KeyValuePair<string, string> pair = (KeyValuePair<string, string>)val;
            mOperations[pair.Key] = pair.Value;
        }
        bool RenameCategory(string oldName, string newName)
        {
            GraphChartBase graph = (GraphChartBase)serializedObject.targetObject;
            try
            {
                graph.DataSource.RenameCategory(oldName, newName);
            }
            catch (Exception)
            {
                return false;
            }
            serializedObject.Update();
            if (graph.gameObject.activeInHierarchy)
                graph.GenerateChart();
            else
                EditorUtility.SetDirty(graph);
            return true;
        }
        void RenameCalled(object val)
        {
            var data = (KeyValuePair<string, string>)val;
            RenameWindow window = EditorWindow.GetWindow<RenameWindow>();
            mRenameWindow = window;
            window.ShowDialog(data.Value, data.Key, RenameCategory);
        }
        void DoContext(string type, string name)
        {
            string arg = type + "|" + name;
            GenericMenu menu = new GenericMenu();
            menu.AddItem(new GUIContent("Move Up"), false, callback, new KeyValuePair<string, string>(arg, "up"));
            menu.AddItem(new GUIContent("Move Down"), false, callback, new KeyValuePair<string, string>(arg, "down"));
            menu.AddItem(new GUIContent("Remove"), false, callback, new KeyValuePair<string, string>(arg, "remove"));
            menu.AddItem(new GUIContent("Rename.."), false, RenameCalled, new KeyValuePair<string, string>(type, name));
            menu.ShowAsContext();
        }
        void UpdateWindow()
        {
        }
        void OnDisable()
        {
            if (mRenameWindow != null)
            {
                mRenameWindow.Close();
                mRenameWindow = null;
            }
            if (mWindow != null)
            {
                mWindow.Close();
                mWindow = null;
            }
        }

        public override void OnInspectorGUI()
        {
            //serializedObject.Update();
            DrawDefaultInspector();

            SerializedProperty autoScrollHorizontally = serializedObject.FindProperty("autoScrollHorizontally");
            SerializedProperty horizontalScrolling = serializedObject.FindProperty("horizontalScrolling");
            SerializedProperty autoScrollVertically = serializedObject.FindProperty("autoScrollVertically");
            SerializedProperty verticalScrolling = serializedObject.FindProperty("verticalScrolling");
            // SerializedProperty scrollable = serializedObject.FindProperty("scrollable");
            // EditorGUILayout.PropertyField(scrollable);
            //  if (scrollable.boolValue == true)
            {
                EditorGUI.indentLevel++;
                EditorGUILayout.PropertyField(autoScrollHorizontally);
                if (autoScrollHorizontally.boolValue == false)
                    EditorGUILayout.PropertyField(horizontalScrolling);
                EditorGUILayout.PropertyField(autoScrollVertically);
                if (autoScrollVertically.boolValue == false)
                    EditorGUILayout.PropertyField(verticalScrolling);
                EditorGUI.indentLevel--;
            }

            SerializedProperty graphData = serializedObject.FindProperty("Data");
            EditorGUILayout.BeginVertical();
            Splitter();
            if (mBold == null)
                mBold = new GUIStyle(EditorStyles.foldout);
            EditorGUILayout.LabelField("Data", EditorStyles.boldLabel);



            EditorGUI.indentLevel++;
            NamedItemEditor(graphData, "category", "mSerializedData", "Categories", ref mCategoryError, ref mCategories, ref mNewCategoryName);
            EditorGUI.indentLevel--;

            SerializedProperty horizontalOrigin = graphData.FindPropertyRelative("horizontalViewOrigin");
            SerializedProperty horizontalSize = graphData.FindPropertyRelative("horizontalViewSize");
            SerializedProperty automaticcHorizontaViewGap = graphData.FindPropertyRelative("automaticcHorizontaViewGap");

            SerializedProperty verticalOrigin = graphData.FindPropertyRelative("verticalViewOrigin");
            SerializedProperty verticalSize = graphData.FindPropertyRelative("verticalViewSize");
            SerializedProperty automaticVerticalViewGap = graphData.FindPropertyRelative("automaticVerticalViewGap");

            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.LabelField("Horizontal View", EditorStyles.boldLabel);
            SerializedProperty automaticProp = graphData.FindPropertyRelative("automaticHorizontalView");
            EditorGUILayout.PropertyField(automaticProp, new GUIContent("Auto"));
            bool automatic = automaticProp.boolValue;
            GUILayout.FlexibleSpace();
            EditorGUILayout.EndHorizontal();
            if (automatic == false)
            {
                EditorGUILayout.PropertyField(horizontalOrigin);
                EditorGUILayout.PropertyField(horizontalSize);
                //  if (horizontalSize.doubleValue < 0.0)
                //      horizontalSize.doubleValue = 0.0001;
            }
            else
            {
                EditorGUILayout.PropertyField(automaticcHorizontaViewGap, new GUIContent("Horizontal Gap"));
            }

            EditorGUILayout.BeginHorizontal();
            EditorGUILayout.LabelField("Vertical View", EditorStyles.boldLabel);
            automaticProp = graphData.FindPropertyRelative("automaticVerticallView");
            EditorGUILayout.PropertyField(automaticProp, new GUIContent("Auto"));
            automatic = automaticProp.boolValue;
            GUILayout.FlexibleSpace();
            EditorGUILayout.EndHorizontal();
            if (automatic == false)
            {
                EditorGUILayout.PropertyField(verticalOrigin);
                EditorGUILayout.PropertyField(verticalSize);
                //       if (verticalSize.doubleValue < 0.0)
                //           verticalSize.doubleValue = 0.0001;
            }
            else
            {
                EditorGUILayout.PropertyField(automaticVerticalViewGap, new GUIContent("Vertical Gap"));
            }
            EditorGUILayout.EndVertical();
            serializedObject.ApplyModifiedProperties();

            if (mWindow != null)
            {
                mWindow.SetEditedObject(serializedObject, mEditedCategory);
                mWindow.Repaint();
            }
        }
    }
}
