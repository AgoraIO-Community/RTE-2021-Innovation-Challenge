using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Hi5_Interaction_Core;

public class VoxelSelector : MonoBehaviour
{
    public List<Voxel> selectedVoxels;
    public VoxelPlacer vp;
    private Hi5InputController vrcon;
    public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
    public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;

    // Start is called before the first frame update
    void Start()
    {
        selectedVoxels = new List<Voxel>();
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
        vp = GameObject.Find("VoxelPlacer").GetComponent<VoxelPlacer>();
        HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
        HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
    }

    // Update is called once per frame
    void Update()
    {
        ProcessInput();
    }

    private void ProcessInput()
    {
        // 如果有选择事件被触发
        /*if (vrcon.selectObjectInput() == 1)
        {
            ObjectComponent os = WorldDataManager.Instance.ActiveWorld.GetOneObjectAt(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position);
            if (!(os is null))
            {
                if (vp.voxelTargetObj != os)
                {
                    if (!(vp.voxelTargetObj is null))
                    {
                        ToolManager.unHighlightObject(vp.voxelTargetObj.gameObject);
                    }
                    vp.voxelTargetObj = os;
                    ToolManager.highlightObject(vp.voxelTargetObj.gameObject, Color.yellow, 5f);
                }
                else
                {
                    if (!(vp.voxelTargetObj is null))
                    {
                        ToolManager.unHighlightObject(vp.voxelTargetObj.gameObject);
                    }
                    vp.voxelTargetObj = null;
                }
            }
            else
            {
                if (!(vp.voxelTargetObj is null))
                {
                    ToolManager.unHighlightObject(vp.voxelTargetObj.gameObject);
                }
                vp.voxelTargetObj = null;
            }
        }*/
        if (!(vp.voxelTargetObj == null))
        {
            if (vrcon.selectVoxelInput() == 1)
            {
                // 选中位置的信息存入一个Voxel对象
                Vector3Int pos = MathHelper.WorldPosToWorldIntPos(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position / WorldDataManager.Instance.ActiveWorld.worldSize);
                //Debug.Log(pos);

                Voxel v = vp.voxelTargetObj.voxelObjectData.GetVoxelAt(pos - vp.voxelTargetObj.gridBasePoint);
                /*            Debug.Log(pos - vp.targetObj.gridBasePoint);*/
                // 如果此处没有voxel
                if (v.voxel == null)
                {
                    Debug.Log("null voxel");
                    // 该位置不靠近已有的体素，取消所有选中的voxel
                    if (!vp.voxelTargetObj.IsNearVoxel(pos))
                    {
                        for (int i = 0; i < this.selectedVoxels.Count; i++)
                        {
                            Voxel vo = this.selectedVoxels[i];
                            vo.color = Color.white;
                            this.selectedVoxels[i] = vo;
                        }
                        this.selectedVoxels.Clear();
                    }
                }
                // 如果有voxel，则选中该voxel并高亮显示
                else if (!this.selectedVoxels.Contains(v))
                {
                    Debug.Log("add voxel");
                    this.selectedVoxels.Add(v);
                    v.color = Color.yellow;

                }
                Debug.Log("has voxel");

            }
        }
    }
}
