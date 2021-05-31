using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Hi5_Interaction_Core;

public class VoxelPlacer : MonoBehaviour
{
    // ---- 桌面模式变量 ----
    public HitPointReader hitPointReader;
    public ObjectSelector objectSelector;

    // ---- VR模式变量 ----
    public VoxelSelector vselector;
    private Hi5InputController vrcon;
    public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
    public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;
    Vector3 moveStartLocHand;
    public HelpOptions helpOptions;
    public GameObject userGuide;
    public MergeOptions mOptions;
    public WorldOptions wOptions;

    // 当前正在被修改的Object
    public ObjectComponent voxelTargetObj;


    public Voxel voxelArg;

    // Start is called before the first frame update

    private void Awake()
    {
        helpOptions = GameObject.Find("HelpMenu").GetComponent<HelpOptions>();
        userGuide = GameObject.Find("UserGuide");
        wOptions = GameObject.Find("WorldMenu").GetComponent<WorldOptions>();

    }
    private void Start()
    {
        //Setup voxel arg
        voxelArg = new Voxel()
        {
            voxel = VoxelInfoLibrary.GetVoxel("Stone"),
            color = Color.white,
        };
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
        HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
        HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();    }

    // Update is called once per frame
    void Update()
    {
        ProcessInput(ToolManager.Instance.Imode);
        vrcon.WorldChange();
    }

    private void ProcessInput(ToolManager.InteractionMode mode)
    {
        if (mode == ToolManager.InteractionMode.Desktop)
        {
            if (hitPointReader.hitting)
            {
                // 按下鼠标左键
                if (Input.GetKeyDown(KeyCode.Mouse0))
                {
                    // 保存当前世界中与选中Voxel位置相邻的Object
                    List<ObjectComponent> Objects = new List<ObjectComponent>();

                    foreach (var o in WorldDataManager.Instance.ActiveWorld.ObjectList)
                    {
                        if (o.IsNearVoxel(hitPointReader.hitPoint.position + hitPointReader.hitPoint.normal / 2) && !o.voxelObjectData.isStatic)
                        {
                            WorldDataManager.Instance.ActiveWorld.SetVoxelAt(
                            o,
                            hitPointReader.hitPoint.position + hitPointReader.hitPoint.normal / 2,
                            voxelArg);
                            o.UpdateObjectMesh();
                            Objects.Add(o);
                        }
                    }
                    // 如果没有Object与该Voxel相邻，则认为创建了一个新的Object
                    if (Objects.Count == 0)
                    {
                        var o = WorldDataManager.Instance.ActiveWorld.
                            CreateNewObject(MathHelper.WorldPosToWorldIntPos(hitPointReader.hitPoint.position + hitPointReader.hitPoint.normal / 2));
                        WorldDataManager.Instance.ActiveWorld.SetVoxelAt(
                           o,
                           hitPointReader.hitPoint.position + hitPointReader.hitPoint.normal / 2,
                           voxelArg);
                        o.UpdateObjectMesh();
                    }
                    else // 将所有相邻的Object融合成一个新的Object
                    {
                        var firstObj = Objects[0];
                        for (int i = 1; i < Objects.Count; i++)
                        {
                            WorldDataManager.Instance.ActiveWorld.MergeTwoObjects(firstObj, Objects[i], WorldData.MergeType.Or);
                        }
                    }
                }

                // 点击鼠标右键
                else if (Input.GetKeyDown(KeyCode.Mouse1))
                {
                    // 遍历被选中的Object，删除选中位置的Voxel
                    List<ObjectComponent> deleteObjects = new List<ObjectComponent>();
                    foreach (var o in objectSelector.selectedObjects)
                    {
                        WorldDataManager.Instance.ActiveWorld.DeleteVoxelAt(o,
                            hitPointReader.hitPoint.position - hitPointReader.hitPoint.normal / 2);
                        if (o.voxelObjectData.VoxelDataDict.Count == 0)
                            deleteObjects.Add(o);
                        o.UpdateObjectMesh();
                    }
                    for (int i = 0; i < deleteObjects.Count; i++)
                    {
                        WorldDataManager.Instance.ActiveWorld.DeleteObject(deleteObjects[i]);
                    }
                }
            }
        }
        else // VR mode
        {
            if(vrcon.selectObjectInput() == 1)
            {
                ObjectComponent os = WorldDataManager.Instance.ActiveWorld.GetOneObjectAt(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position);
                if (!(os == null))
                {
                    if (voxelTargetObj != os)
                    {
                        if (!(voxelTargetObj == null))
                        {
                            ToolManager.unHighlightObject(voxelTargetObj.gameObject);
                        }
                        voxelTargetObj = os;
                        ToolManager.highlightObject(voxelTargetObj.gameObject, Color.yellow, 5f);
                    }
                    else
                    {
                        if (!(voxelTargetObj == null))
                        {
                            ToolManager.unHighlightObject(voxelTargetObj.gameObject);
                        }
                        voxelTargetObj = null;
                    }
                }
                else
                {
                    if (!(voxelTargetObj == null))
                    {
                        ToolManager.unHighlightObject(voxelTargetObj.gameObject);
                    }
                    voxelTargetObj = null;
                }
            }
            // 创建新的voxel
            if (!(voxelTargetObj == null))
            {
                if (vrcon.createVoxelInput() == 1 || vrcon.createVoxelInput() == 2)
                {
                    // 选中位置的信息存入一个Voxel对象
                    Vector3Int pos = MathHelper.WorldPosToWorldIntPos(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position / WorldDataManager.Instance.ActiveWorld.worldSize);
                    Debug.Log("voxelcreate_finger:" + pos);
                    Debug.Log("gridBasePoint:" + this.voxelTargetObj.gridBasePoint);
                    Voxel v = this.voxelTargetObj.voxelObjectData.GetVoxelAt(pos - this.voxelTargetObj.gridBasePoint);
                    // 如果此处没有voxel，进一步判断是否与已有voxel相连，如果相连，则处理按键的事件
                    if (v.voxel == null && this.voxelTargetObj.IsNearVoxel(pos))
                    {
                        WorldDataManager.Instance.ActiveWorld.SetVoxelAt(this.voxelTargetObj, pos, voxelArg);
                        this.voxelTargetObj.UpdateObjectMesh();
                    }
                }
                // 如果有voxel，则根据按键删除voxel
                if (vrcon.deleteVoxelInput() == 1 || vrcon.deleteVoxelInput() == 2)
                {
                    Debug.Log("Delete");
                    // 选中位置的信息存入一个Voxel对象
                    Vector3Int pos = MathHelper.WorldPosToWorldIntPos(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position / WorldDataManager.Instance.ActiveWorld.worldSize);
                    Voxel v = this.voxelTargetObj.voxelObjectData.GetVoxelAt(pos - this.voxelTargetObj.gridBasePoint);
                    if (v.voxel != null)
                    {
                        Debug.Log("Delete" + v.voxel.name);
                        WorldDataManager.Instance.ActiveWorld.DeleteVoxelAt(this.voxelTargetObj, pos);
                        if (this.voxelTargetObj.voxelObjectData.VoxelDataDict.Count == 0)
                            WorldDataManager.Instance.ActiveWorld.DeleteObject(this.voxelTargetObj);
                        this.voxelTargetObj.UpdateObjectMesh();
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
                Debug.Log("vrcon.helpImageInput");
                helpOptions.gameObject.SetActive(true);
                userGuide.SetActive(true);
            }
        }
    }

    public void SetTargetObj()
    {
        this.voxelTargetObj = this.objectSelector.GetSelectedObject();
    }
}