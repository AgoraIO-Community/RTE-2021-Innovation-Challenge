#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEditor;
using UnityEngine;

namespace ChartAndGraph
{
    class RenameWindow :EditorWindow
    {
        string mStartName;
        string mValue = "";
        string mType;
        Func<string, string, bool> mRenameMethod;
        string mMessage = null;

        public void ShowDialog(string currentName,string type,Func<string,string, bool> renameMethod)
        {
            mStartName = currentName;
            mValue = currentName;
            mType = type;
            mRenameMethod = renameMethod;
            float height = (float)(EditorGUIUtility.singleLineHeight * 6f);
            minSize = maxSize = new Vector2(300, height);
            Show();
            
        }
        void OnGUI()
        {
            EditorGUILayout.LabelField(string.Format("Select a new {0} name",mType));
            mValue = EditorGUILayout.TextField(mValue);
            bool disabled = false;
            if (mValue.Trim().Length == 0)
            {
                mMessage = null;
                EditorGUILayout.LabelField("Name can't be empty");
                disabled = true;
            }
            else
                if (ChartEditorCommon.IsAlphaNum(mValue) == false)
                {
                    mMessage = null;
                    EditorGUILayout.LabelField("Name contains invalid charecters");
                    disabled = true;
                }
            if (mMessage != null)
                EditorGUILayout.LabelField(mMessage);
            EditorGUILayout.BeginHorizontal();
            GUI.enabled = !disabled;
            if (GUILayout.Button("Rename"))
            {
                if (mStartName == mValue)
                    Close();
                else
                {
                    if (mRenameMethod(mStartName,mValue))
                        Close();
                    else
                        mMessage = string.Format("A {0} by the name {1} already exists", mType, mValue);
                }
            }
            GUI.enabled = true;
            GUILayout.FlexibleSpace();
            if (GUILayout.Button("Cancel"))
                Close();
            EditorGUILayout.EndHorizontal();
        }
    }
}
