//======= Copyright (c) Beijing Noitom Technology Ltd., All rights reserved. ===============
//
// Purpose: Help bind the transforms of hand bones on HI5_VIVEInstance component.
//
//==========================================================================================

using UnityEngine;
using UnityEditor;
using HI5;

[CustomEditor(typeof(HI5_Instance), true)]
public class HI5_InstanceEditor : Editor
{
    public override void OnInspectorGUI()
    {
        DrawDefaultInspector();

        HI5_Instance targetScript = (HI5_Instance)target;

        if (targetScript.HandType == Hand.LEFT)
        {
            if (GUILayout.Button("Load LEFT Hand Transforms"))
            {
                Debug.Log("[HI5_Instance] - LOAD Left hand Transform references into the bones list!");
                targetScript.AutoBindBones(Hand.LEFT);
                EditorUtility.SetDirty(targetScript);
            }
        }

        if (targetScript.HandType == Hand.RIGHT)
        {
            if (GUILayout.Button("Load RIGHT Hand Transforms"))
            {
                Debug.Log("[HI5_Instance] - LOAD Right hand Transform references into the bones list!");
                targetScript.AutoBindBones(Hand.RIGHT);
                EditorUtility.SetDirty(targetScript);
            }
        }
    }
}
