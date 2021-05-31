using System;
using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Valve.VR;
using Hi5_Interaction_Core;

/// <summary>
/// 管理交互的内容与形式
/// </summary>
public class ToolManager : Singleton<ToolManager>
{
    public VoxelPlacer voxelPlacer;
    public FaceStretcher faceStretcher;
    public ObjectManipulator objectManipulator;
    //public ObjectSelector objectSelector;
    private Hi5InputController vrcon;
    public UnityEngine.TextMesh Switch_Mode_Button_Text;
    public Transform steamVRObjects;
    public Transform plane;
    public WorldData worldData;
    public WorldObject wb;


    public enum ToolMode
    {
        PlaceVoxel,
        FaceStretch,
        ObjectManipulation
    }
    public ToolMode Tmode;
    public enum InteractionMode
    {
        Desktop,
        VR
    }
    public InteractionMode Imode;

    private void Start()
    {
        this.Imode = InteractionMode.VR;
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
        Switch_Mode_Button_Text = GameObject.Find("Switch_Mode_Button/Switch_Text").GetComponent<TextMesh>();
        objectManipulator = GameObject.Find("ObjectManipulator").GetComponent<ObjectManipulator>();
        steamVRObjects = GameObject.Find("SteamVRObjects").transform;
        plane = GameObject.Find("Plane").transform;
        ToolModeSwitching();
        wb = GameObject.Find("WorldObject").GetComponent<WorldObject>();
        //worldData = new WorldData();
    }

    // Update is called once per frame
    void Update()
    {
        ToolModeUpdate();
        InteractionModeUpdate();
        UpPosition();
        DownPosition();
        ChangeWorldSize();
    }

    private void ToolModeUpdate()
    {
        if (Imode == InteractionMode.Desktop)
        {
            if (Input.GetKeyDown(KeyCode.F1))
            {
                Tmode = ToolMode.ObjectManipulation;
                ToolModeSwitching();
            }
            else if (Input.GetKeyDown(KeyCode.F2))
            {
                if (objectManipulator.objectSelector.selectedObjects.Count != 0)
                {
                    Tmode = ToolMode.PlaceVoxel;
                    ToolModeSwitching();
                }
            }
            else if (Input.GetKeyDown(KeyCode.F3))
            {
                if (objectManipulator.objectSelector.selectedObjects.Count != 0)
                {
                    Tmode = ToolMode.FaceStretch;
                    ToolModeSwitching();
                }
            }
        }
        else if (Imode == InteractionMode.VR)
        {
            if (vrcon.switchModeInput() == 1)
            {
                if (Tmode == ToolMode.ObjectManipulation)
                {
                    Tmode = ToolMode.PlaceVoxel;
                    if (objectManipulator.objectSelector.selectedObjects.Count > 0)
                    {
                        for(int i = objectManipulator.objectSelector.selectedObjects.Count-1; i>= 0; i--)
                        {
                            ObjectComponent deleobject = objectManipulator.objectSelector.selectedObjects[i];
                            ToolManager.unHighlightObject(deleobject.gameObject);
                            objectManipulator.objectSelector.selectedObjects.Remove(deleobject);
                        }
                    }
                    ToolModeSwitching();
                }
                else if (Tmode == ToolMode.PlaceVoxel)
                {
                    Tmode = ToolMode.FaceStretch;
                    if (!(voxelPlacer.voxelTargetObj == null))
                    {
                        ToolManager.unHighlightObject(voxelPlacer.voxelTargetObj.gameObject);
                        voxelPlacer.voxelTargetObj = null;
                    }
                    ToolModeSwitching();
                }
                else if (Tmode == ToolMode.FaceStretch)
                {
                    Tmode = ToolMode.ObjectManipulation;
                    if (faceStretcher.faceSelector.selectionPoints.Count > 0)
                    {
                        for (int i = faceStretcher.faceSelector.selectionPoints.Count - 1; i >= 0; i--)
                        {
                            Vector3Int deleVec = faceStretcher.faceSelector.selectionPoints[i];
                            Voxel v = faceStretcher.faceTargetObj.voxelObjectData.GetVoxelAt(deleVec);
                            v.color = Color.white;
                        }
                        faceStretcher.faceSelector.selectionPoints.Clear();
                        faceStretcher.stretchedPoints.Clear();
                    }
                    if (!(faceStretcher.faceTargetObj == null))
                    {
                        ToolManager.unHighlightObject(faceStretcher.faceTargetObj.gameObject);
                        faceStretcher.faceTargetObj = null;
                    }
                    ToolModeSwitching();
                }
            }
        }
    }

    private void ToolModeSwitching()
    {
        switch (Tmode)
        {
            case ToolMode.ObjectManipulation:
                objectManipulator.gameObject.SetActive(true);
                Switch_Mode_Button_Text.text = "Mode:Object";
                voxelPlacer.gameObject.SetActive(false);
                faceStretcher.faceSelector.hitPointReader.ToggleVRPointer(false);
                faceStretcher.gameObject.SetActive(false);
                break;
            case ToolMode.PlaceVoxel:
                voxelPlacer.gameObject.SetActive(true);
                //voxelPlacer.SetTargetObj();
                Switch_Mode_Button_Text.text = "Mode:Voxel";
                objectManipulator.gameObject.SetActive(false);
                faceStretcher.gameObject.SetActive(false);
                break;
            case ToolMode.FaceStretch:
                faceStretcher.gameObject.SetActive(true);
                Switch_Mode_Button_Text.text = "Mode:Face";
                faceStretcher.faceSelector.hitPointReader.ToggleVRPointer(true);
                //faceStretcher.targetObj = voxelPlacer.targetObj;
                voxelPlacer.gameObject.SetActive(false);
                objectManipulator.gameObject.SetActive(false);
                break;
            default:
                break;
        }
    }

    public void InteractionModeUpdate()
    {
        // Switch to VR mode
        if (Input.GetKeyDown(KeyCode.F4) && Imode == InteractionMode.Desktop)
        {
            Imode = InteractionMode.VR;
        }
        // VR to desktop
        if (Input.GetKeyDown(KeyCode.F5) && Imode == InteractionMode.VR)
        {
            Imode = InteractionMode.Desktop;
        }
    }

    static public void highlightObject(GameObject obj, Color c, float width)
    {
            var outline = obj.AddComponent<Outline>();

            outline.OutlineMode = Outline.Mode.OutlineAll;
            outline.OutlineColor = c;
            outline.OutlineWidth = width;
    }

    static public void unHighlightObject(GameObject obj)
    {
        Destroy(obj.GetComponent<Outline>());
    }

    private void UpPosition()
    {

        if (vrcon.upUp() == 1 || vrcon.upUp() == 2)
        {
            steamVRObjects.Translate(new Vector3(0, (float)0.1, 0));
            plane.Translate(new Vector3(0, (float)0.1, 0));

        }
    }

    private void DownPosition()
    {

        if (vrcon.downDown() == 1 || vrcon.downDown() == 2)
        {
            steamVRObjects.Translate(new Vector3(0, -(float)0.1, 0));
            plane.Translate(new Vector3(0, -(float)0.1, 0));

        }

    }

    private void ChangeWorldSize()
    {
        if(vrcon.changeSize() == 1)
        {
            wb = GameObject.Find("WorldObject").GetComponent<WorldObject>();
            if(wb.transform.localScale == new Vector3Int(1,1,1))
            {
                wb.transform.localScale = new Vector3Int(50,50, 50);
            }
            else if (wb.transform.localScale == new Vector3Int(50, 50, 50))
            {
                wb.transform.localScale = new Vector3Int(1, 1, 1);
            }
        }
    }

}
