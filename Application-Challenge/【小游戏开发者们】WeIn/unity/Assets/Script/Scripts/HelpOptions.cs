using UnityEngine;
using Hi5_Interaction_Core;
using System;

public class HelpOptions : MonoBehaviour
{
    public GameObject userGuide;
    public GameObject objectMode;
    public GameObject voxelMode;
    public GameObject faceMode;
    public GameObject systemHandle;

    private void Awake()
    {
        userGuide = GameObject.Find("UserGuide");
        objectMode = GameObject.Find("ObjectMode");
        voxelMode = GameObject.Find("VoxelMode");
        faceMode = GameObject.Find("FaceMode");
        systemHandle = GameObject.Find("SystemHandle");
    }

    public void closeHelpMenu()
    {
        userGuide.SetActive(false);
        objectMode.SetActive(false);
        voxelMode.SetActive(false);
        faceMode.SetActive(false);
        systemHandle.SetActive(false);
        this.gameObject.SetActive(false);
    }

    public void turnToObject()
    {
        userGuide.SetActive(false);
        objectMode.SetActive(true);
        voxelMode.SetActive(false);
        faceMode.SetActive(false);
        systemHandle.SetActive(false);
    }

    public void turnToVoxel()
    {
        userGuide.SetActive(false);
        objectMode.SetActive(false);
        voxelMode.SetActive(true);
        faceMode.SetActive(false);
        systemHandle.SetActive(false);
    }

    public void turnToFace()
    {
        userGuide.SetActive(false);
        objectMode.SetActive(false);
        voxelMode.SetActive(false);
        faceMode.SetActive(true);
        systemHandle.SetActive(false);
    }

    public void turnToSystem()
    {
        userGuide.SetActive(false);
        objectMode.SetActive(false);
        voxelMode.SetActive(false);
        faceMode.SetActive(false);
        systemHandle.SetActive(true);
    }
}
