using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using Valve.VR;
using Valve.VR.InteractionSystem;

public class VRInputController : MonoBehaviour
{
    // Object级输入
    public SteamVR_Action_Boolean selectObjectInput; 
    public SteamVR_Action_Boolean createObjectInput;
    public SteamVR_Action_Boolean deleteObjectInput;
    public SteamVR_Action_Boolean copyObjectInput;
    public SteamVR_Action_Boolean moveObjectInput;
    public SteamVR_Action_Boolean rotateObjectInput;
    public SteamVR_Action_Boolean combineObjectInput;

    // Voxel级输入
    public SteamVR_Action_Boolean createVoxelInput;
    public SteamVR_Action_Boolean deleteVoxelInput;
    public SteamVR_Action_Boolean selectVoxelInput;

    // Face级输入
    public SteamVR_Action_Boolean selectFaceInput;
    public SteamVR_Action_Boolean pullFaceInput;

    // 模式控制
    public SteamVR_Action_Boolean switchModeInput;
    public SteamVR_Action_Boolean worldMenuInput;

    public Hand leftHand;
    public Hand rightHand;

    // Start is called before the first frame update
    void Start()
    {
        
    }

    // Update is called once per frame
    void Update()
    {
        
    }

    public Vector3Int GetScaledHandLocation(Hand h)
    {
        Vector3 pos = new Vector3Int();
        pos = h.transform.position / WorldDataManager.Instance.ActiveWorld.worldSize;
        return MathHelper.WorldPosToWorldIntPos(pos);
    }
}
