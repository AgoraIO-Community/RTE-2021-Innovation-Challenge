using UnityEngine;
using System.Collections.Generic;
using System.Drawing;
using Hi5_Interaction_Core;

public class FaceStretcher : MonoBehaviour
{
    // 当前正在被修改的Object
    public ObjectComponent faceTargetObj;

    public FaceSelector faceSelector;
    public FaceIndicator selectionIndicator;
    public int stretchResult;
    private Vector3? m_downCursorPoint;
    private Vector3? m_upCursorPoint;
    private int pullInputState;
    public HelpOptions helpOptions;
    public GameObject userGuide;
    public MergeOptions mOptions;
    public WorldOptions wOptions;

    public List<Vector3Int> stretchedPoints;

    // VR
    private Hi5InputController vrcon;

    private void Awake()
    {
        stretchedPoints = new List<Vector3Int>();
        stretchResult = 0;
        m_downCursorPoint = null;
        m_upCursorPoint = null;
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
        helpOptions = GameObject.Find("HelpMenu").GetComponent<HelpOptions>();
        userGuide = GameObject.Find("UserGuide");
        wOptions = GameObject.Find("WorldMenu").GetComponent<WorldOptions>();
    }

    private void Update()
    {
        pullInputState = vrcon.pullFaceInput();
        ComputeStretching(ToolManager.Instance.Imode);

        //Apply
        if (ToolManager.Instance.Imode == ToolManager.InteractionMode.Desktop)
        {
            if (Input.GetKeyUp(KeyCode.Mouse1) && faceSelector.normal != null)
            {
                ApplyStretching();
            }
        }
        else
        {
            if (!(faceTargetObj == null))
            {
                if (pullInputState == 3 && faceSelector.normal != null)
                {
                    ApplyStretching();
                }
                else if (vrcon.deleteFaceInput() == 1 && faceSelector.normal != null)
                {
                    DeleteFace();
                }
            }
        }
        if (vrcon.worldMenuInput() == 1) // 启动world切换
        {
            // 根据菜单选择操作
            wOptions.gameObject.SetActive(true);

        }

        if (vrcon.helpImageInput() == 1)
        {
            helpOptions.gameObject.SetActive(true);
            userGuide.SetActive(true);
        }
        vrcon.WorldChange();
    }

    private void ComputeStretching(ToolManager.InteractionMode mode)
    {
        if (mode == ToolManager.InteractionMode.Desktop)
        {
            if (Input.GetKeyDown(KeyCode.Mouse1))
            {
                m_upCursorPoint = null;
                m_downCursorPoint = Input.mousePosition;
            }
            if (Input.GetKey(KeyCode.Mouse1))
            {
                if (m_downCursorPoint != null && selectionIndicator.data.Count > 0 && faceSelector.normal != null)
                {
                    var normal = new Vector3Int(
                        (int)faceSelector.normal.Value.x,
                        (int)faceSelector.normal.Value.y,
                        (int)faceSelector.normal.Value.z);

                    m_upCursorPoint = Input.mousePosition;
                    Camera camera = Camera.main;
                    Vector3 p1 = camera.ScreenToWorldPoint(new Vector3(m_upCursorPoint.Value.x, m_upCursorPoint.Value.y, camera.nearClipPlane));
                    Vector3 p2 = camera.ScreenToWorldPoint(new Vector3(m_downCursorPoint.Value.x, m_downCursorPoint.Value.y, camera.nearClipPlane));

                    float result = Vector3.Dot(p1 - p2, normal);
                    stretchResult = (int)(result * 50);
                }

                //Make stretched data
                UpdateStretchedPointDict();

            }
        }
        else // VR mode
        {
            if (pullInputState == 1)
            {
                m_upCursorPoint = null;
                m_downCursorPoint = vrcon.HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position;
            }

            if (pullInputState == 2)
            {
                if (m_downCursorPoint != null && selectionIndicator.data.Count > 0 && faceSelector.normal != null)
                {
                    var normal = new Vector3Int(
                        (int)faceSelector.normal.Value.x,
                        (int)faceSelector.normal.Value.y,
                        (int)faceSelector.normal.Value.z);

                    m_upCursorPoint = vrcon.HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position;

                    float result = Vector3.Dot(m_upCursorPoint.Value - m_downCursorPoint.Value, normal);
                    stretchResult = (int)(result * 50);
                }
                //Make stretched data
                UpdateStretchedPointDict();
            }
            
        }
    }

    private void UpdateStretchedPointDict()
    {
        if (faceSelector.normal != null)
        {
            var normal = new Vector3Int(
                (int)faceSelector.normal.Value.x,
                (int)faceSelector.normal.Value.y,
                (int)faceSelector.normal.Value.z);

            stretchedPoints.Clear();

            // 根据选择的面和推拉的距离，生成推拉后的点集
            foreach (var p in faceSelector.selectionPoints)
            {
                //Add 
                if (stretchResult > 0)
                {
                    for (int i = 0; i <= stretchResult; i++)
                    {
                        //Stretch out
                        stretchedPoints.Add(p + i * normal);
                    }
                }
                //Substract
                else
                {
                    for (int i = stretchResult; i <= 0; i++)
                    {
                        //Stretch out
                        stretchedPoints.Add(p + i * normal);
                        
                    }
                }
            }
        }
    }

    private void ApplyStretching()
    {
        
        var normal = new Vector3Int(
            (int)faceSelector.normal.Value.x,
            (int)faceSelector.normal.Value.y,
            (int)faceSelector.normal.Value.z);
        //Add
        if (stretchResult > 0)
        {
            foreach (var p in faceSelector.selectionPoints)
            {
                Voxel v = this.faceTargetObj.voxelObjectData.GetVoxelAt(p);
                for (int i = 1; i <= stretchResult; i++)
                {
                    this.faceTargetObj.voxelObjectData.SetVoxelAt(p + normal * i, v);
                }
            }
            this.faceTargetObj.UpdateObjectMesh();
        }

        faceSelector.selectionPoints.Clear();
        stretchedPoints.Clear();
        stretchResult = 0;
    }

    private void DeleteFace()
    {
            var normal = new Vector3Int(
        (int)faceSelector.normal.Value.x,
        (int)faceSelector.normal.Value.y,
        (int)faceSelector.normal.Value.z);
        foreach (var p in faceSelector.selectionPoints)
        {
            for (int i = stretchResult; i <= 0; i++)
            {
                //Delete voxel
                this.faceTargetObj.voxelObjectData.DeleteVoxelAt(p + normal * i);
            }
        }
        this.faceTargetObj.UpdateObjectMesh();
        faceSelector.selectionPoints.Clear();
        stretchedPoints.Clear();
        stretchResult = 0;
    }
}