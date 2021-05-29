using UnityEngine;
using System.Collections.Generic;
using Hi5_Interaction_Core;
using Hi5_Interaction_Interface;

public class ObjectSelector : MonoBehaviour
{
    public HitPointReader hitPointReader;
    public List<ObjectComponent> selectedObjects;

    private Hi5InputController vrcon;
    public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
    public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;

    private void Awake()
    {
        selectedObjects = new List<ObjectComponent>();
    }

    private void Start()
    {
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
        HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
        HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
        //selectedObjects.Add(WorldDataManager.Instance.ActiveWorld.GetVoxelObject(0));
    }

    private void Update()
    {
        ProcessInput(ToolManager.Instance.Imode);           
    }

    private void ProcessInput(ToolManager.InteractionMode mode)
    {
        if (mode == ToolManager.InteractionMode.Desktop)
        {
            // click to select pointing object 
            if (Input.GetKeyDown(KeyCode.Mouse0) && hitPointReader.hitting)
            {
                // 按住 contorl 键增加被选中的物体
                if (!Input.GetKey(KeyCode.LeftControl))
                {
                    selectedObjects.Clear();
                }

                ObjectComponent[] os =
                    WorldDataManager.Instance.ActiveWorld.GetVoxelObjectsAt(
                        hitPointReader.hitPoint.position - hitPointReader.hitPoint.normal / 2);


                //holding shift to only get first one
                if (Input.GetKey(KeyCode.LeftShift))
                {
                    if (!selectedObjects.Contains(os[0]))
                    {
                        selectedObjects.Add(os[0]);
                    }

                }
                else
                {
                    foreach (var o in os)
                    {
                        if (!selectedObjects.Contains(os[0]))
                        {
                            selectedObjects.Add(o);
                        }
                    }
                }


                Debug.Log("Selected Object " + selectedObjects);
            }
            if (Input.GetKeyDown(KeyCode.Tab))
            {
                var last = selectedObjects[0];
                selectedObjects.Clear();
                selectedObjects.Add(WorldDataManager.Instance.ActiveWorld.GetNextObject(last));
                Debug.Log("Selected Object " + selectedObjects);
            }

        }
        else // VR mode
        {
            if (vrcon.selectObjectInput() == 1)
            {
                // 选中Object
                ObjectComponent[] os = WorldDataManager.Instance.ActiveWorld.GetVoxelObjectsAt(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position);
                if (os.Length > 0)
                {
                    foreach (var o in os)
                    {
                        // 如果没被选中过
                        if (!this.selectedObjects.Contains(os[0]))
                        {
                            this.selectedObjects.Add(o);
                            ToolManager.highlightObject(o.gameObject, Color.yellow, 5f);
                            Debug.Log("Object picked " + this.selectedObjects[0].name);
                        }
                        else // 取消选中
                        {
                            ToolManager.unHighlightObject(o.gameObject); 
                            this.selectedObjects.Remove(o);
                            Debug.Log("Object released " + o);
                        }
                    }
                }
            }
        }
    }

    /// <summary>
    /// 返回第一个被选中的目标（默认一次只能选中一个Object）
    /// </summary>
    /// <returns></returns>
    public ObjectComponent GetSelectedObject() 
    {
        return this.selectedObjects[0];
    }
}
