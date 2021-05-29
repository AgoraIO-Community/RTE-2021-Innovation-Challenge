using UnityEngine;
using Hi5_Interaction_Core;
using System;

public class MergeOptions : MonoBehaviour
{
    public ObjectManipulator om;

    public void OnPressForAdd()
    {
        om.MergeObject(WorldData.MergeType.And);
        Debug.Log("OnPressForAdd!");
        this.gameObject.SetActive(false);
    }

    public void OnPressForOr()
    {
        om.MergeObject(WorldData.MergeType.Or);
        Debug.Log("OnPressForOr!");
        this.gameObject.SetActive(false);
    }

    public void OnPressForNot()
    {
        om.MergeObject(WorldData.MergeType.Not);
        Debug.Log("OnPressForNot!");
        this.gameObject.SetActive(false);
    }
}
