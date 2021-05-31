using System;
using System.Collections.Generic;
using UnityEngine;
using Valve.VR;
using Hi5_Interaction_Core;

/// <summary>
/// 包含所有对Object的操作
/// </summary>
public class ObjectManipulator : MonoBehaviour
{
    public ObjectSelector objectSelector;

    // VR
    private Hi5InputController vrcon;
    public List<Vector3Int> creatingObjectBuffer;
    public int copyObjectInputState;
    public HelpOptions helpOptions;
    public GameObject userGuide;
    public MergeOptions mOptions;
    public WorldOptions wOptions;

    // 移动物体
    private Vector3 moveStartLocHand;
    private Vector3Int moveStartLocObj;

    void Awake()
    {
        Debug.Log("Awake:manipulator");
        helpOptions = GameObject.Find("HelpMenu").GetComponent<HelpOptions>();
        userGuide = GameObject.Find("UserGuide");
    }
    private void Start()
    {
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
    }

    private void Update()
    {
        copyObjectInputState = vrcon.copyObjectInput();
        ProcessInput(ToolManager.Instance.Imode);
        vrcon.WorldChange();
    }

    private void ProcessInput(ToolManager.InteractionMode mode)
    {
        if (mode == ToolManager.InteractionMode.Desktop)
        {
            //Copy
            if (Input.GetKeyDown(KeyCode.C))
            {
                CopyObject();
            }

            //Merge
            if (objectSelector.selectedObjects.Count > 1)
            {
                if (Input.GetKey(KeyCode.M))
                {
                    if (Input.GetKeyDown(KeyCode.Alpha1))
                    {
                        MergeObject(WorldData.MergeType.Or);
                    }
                    if (Input.GetKeyDown(KeyCode.Alpha2))
                    {
                        MergeObject(WorldData.MergeType.And);
                    }
                    if (Input.GetKeyDown(KeyCode.Alpha3))
                    {
                        MergeObject(WorldData.MergeType.Not);
                    }
                }
            }

            //Move
            MoveObjectByKeyboard();
        }
        else if (mode == ToolManager.InteractionMode.VR)
        {
            if (this.objectSelector.selectedObjects.Count > 0)
            {
                // 按下正面按钮，启动物体移动
                if (vrcon.moveObjectInput() == 1)
                {
         
                    moveStartLocHand = vrcon.HI5_Right_Human_Collider.GetThumbAndMiddlePoint();
                    moveStartLocObj = this.objectSelector.GetSelectedObject().gridBasePoint;
                }
                
                if ((vrcon.moveObjectInput() == 2 ) || (copyObjectInputState == 2)) // 保持按住正面按钮，移动物体
                {
                    MoveObjectByController();
                }
                
                if (copyObjectInputState == 1) // 按下扳机键，启动复制
                {
                    moveStartLocHand = vrcon.HI5_Right_Human_Collider.GetThumbAndMiddlePoint();
                    CopyObject();
                }
                
                if (vrcon.combineObjectInput() == 1) // 启动合并Object
                {
                    // 根据菜单选择合并模式
                    mOptions.gameObject.SetActive(true);
                    Debug.Log("merge_start");
                }

                if (vrcon.deleteObjectInput() == 1) // 启动删除Object
                {
                    for (int i = objectSelector.selectedObjects.Count - 1; i >= 0;i--)
                    {
                        Debug.Log("Delete" + objectSelector.selectedObjects[i].name);
                        WorldDataManager.Instance.ActiveWorld.DeleteObject(objectSelector.selectedObjects[i]);
                    }
                    
                }
            }

            if (vrcon.createObjectInput() == 3)
            {
                    CreateNewObject();
            }
                // 启动创建新Object
            if (vrcon.createObjectInput() == 2)
            {

                    CreatingNewObject();
            }

            if (vrcon.worldMenuInput() == 1) // 启动world切换
            {
                // 根据菜单选择操作
                wOptions.gameObject.SetActive(true);

            }
            
            if(vrcon.helpImageInput() == 1)
            {
                Debug.Log("vrcon.helpImageInput");
                helpOptions.gameObject.SetActive(true);
                userGuide.SetActive(true);
            }

        }
    }

    /// <summary>
    /// 准备创建新Object，根据双手距离，决定其边长，创建一个长方体
    /// </summary>
    private void CreatingNewObject()
    {
        Vector3 leftPoint = vrcon.HI5_Left_Human_Collider.GetThumbAndMiddlePoint();
        Vector3 rightPoint = vrcon.HI5_Right_Human_Collider.GetThumbAndMiddlePoint();
        Vector3Int min, max;
        MathHelper.GetMinMaxPoint(leftPoint, rightPoint, out min, out max);
        creatingObjectBuffer.Clear();
        creatingObjectBuffer = MathHelper.GenerateGridFromDiagnal(min, max);
       
    }

    private void CreateNewObject()
    {
        WorldDataManager.Instance.ActiveWorld.CreateNewObjectFromGridData(
            creatingObjectBuffer, new Voxel());
        
        WorldDataManager.Instance.ActiveWorld.ObjectList[WorldDataManager.Instance.ActiveWorld.ObjectList .Count-1].UpdateObjectMesh();
        creatingObjectBuffer.Clear();
    }

    private void CopyObject()
    {
        moveStartLocObj = this.objectSelector.GetSelectedObject().gridBasePoint;
        foreach (var o in objectSelector.selectedObjects)
        {
            WorldDataManager.Instance.ActiveWorld.CopyObject(o);
            Debug.Log(o.name+" has been copied");
        }
        
    }

    public void MergeObject(WorldData.MergeType t)
    {
        for (int i = 1; i < objectSelector.selectedObjects.Count; i++)
        {
            WorldDataManager.Instance.ActiveWorld.MergeTwoObjects(
                objectSelector.selectedObjects[0],
                objectSelector.selectedObjects[i],
                t);
        }
    }

    private void MoveObjectByKeyboard()
    {
        Vector3Int delta = new Vector3Int();
        if (Input.GetKeyDown(KeyCode.LeftArrow))
        {
            delta.x = -1;
        }
        else if (Input.GetKeyDown(KeyCode.RightArrow))
        {
            delta.x = 1;
        }
        else if (Input.GetKeyDown(KeyCode.DownArrow))
        {
            delta.z = -1;
        }
        else if (Input.GetKeyDown(KeyCode.UpArrow))
        {
            delta.z = 1;
        }
        else if (Input.GetKeyDown(KeyCode.PageDown))
        {
            delta.y = -1;
        }
        else if (Input.GetKeyDown(KeyCode.PageUp))
        {
            delta.y = 1;
        }
        foreach (var o in objectSelector.selectedObjects)
        {
            o.gridBasePoint += delta;
        }
    }
    
    // 根据手相对于抓取时刻的位置，判定Object移动的方向和距离
    private void MoveObjectByController()
    {
        if (objectSelector.selectedObjects.Count > 0)
        {
            foreach (var o in objectSelector.selectedObjects)
            {
                Vector3 direction = vrcon.HI5_Right_Human_Collider.GetThumbAndMiddlePoint() - this.moveStartLocHand;
                //Debug.Log("this.moveStartLoc: " + this.moveStartLocHand);
                //Debug.Log("vrcon.rightHand.transform.position: " + vrcon.rightHand.transform.position);
                //Debug.Log("direction: " + direction.ToString("f4"));

                Vector3Int delta_axis = new Vector3Int();
                delta_axis = MathHelper.WorldOriToMainAxis(direction);
                //Debug.Log("delta: " + delta_axis);
                int delta_mag = Mathf.CeilToInt(direction.magnitude * 100) / 10;
                delta_axis.Scale(new Vector3Int(delta_mag, delta_mag, delta_mag));
                o.gridBasePoint = this.moveStartLocObj + delta_axis;
                //Debug.Log("o.basePoint: " + o.gridBasePoint);
            }
        }
        else
        {
            Debug.Log("No objects selected");
        }
    }

    /// <summary>
    /// 每次调用，将被选中物体旋转90度
    /// </summary>
    private void RotateObject()
    {
        // TODO
    }

}