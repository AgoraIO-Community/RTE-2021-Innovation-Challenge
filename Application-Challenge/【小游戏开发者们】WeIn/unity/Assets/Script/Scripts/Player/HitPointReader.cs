using UnityEngine;
using Valve.VR.Extras;
using Hi5_Interaction_Core;

[System.Serializable]
public struct HitPoint
{
    public Vector3 position;
    public Vector3 normal;
}
public class HitPointReader : MonoBehaviour
{
    public HitPoint hitPoint;
    public bool hitting { get; private set; }

    private Hi5InputController vrcon;
    private Hi5Laser hi5Laser;
    public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
    public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;
    public Transform finger;


    private void Awake()
    {
        vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();
        this.hi5Laser = vrcon.HI5_Right_Human_Collider.GetComponent<Hi5Laser>();
        HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
        HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
    }

    private void Update()
    {
        SelectVoxel();

    }

    private void SelectVoxel()
    {
        hitting = false;
        hitPoint.position = Vector3.zero;
        hitPoint.normal = Vector3.zero;

        RaycastHit hit;
        if (ToolManager.Instance.Imode == ToolManager.InteractionMode.Desktop)
        {
            Physics.Raycast(Camera.main.ScreenPointToRay(Input.mousePosition), out hit);
        }
        else // VR mode
        {
            if (ToolManager.Instance.Tmode == ToolManager.ToolMode.FaceStretch)
            {
                Debug.Log("reader:FaceStretch");

                finger = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform;
                finger.localRotation = Quaternion.Euler(0, 90, 0);
                //Physics.Raycast(new Ray(HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.position, HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform.right), out hit);
                Physics.Raycast(new Ray(finger.transform.position, finger.transform.forward), out hit);

                if (hit.collider)
                {
                    hitting = true;
                    hitPoint.position = hit.point; // / WorldDataManager.Instance.ActiveWorld.worldSize;
                    hitPoint.normal = hit.normal;
                    Debug.Log("HitPointReader:" + hitPoint.position);

                }
            }
            else
            {
                //hitPoint.position = vrcon.HI5_Right_Human_Collider.GetThumbAndMiddlePoint();
                hitPoint.position = hi5Laser.transform.position;

            }

        }
        
        

    }

    public void ToggleVRPointer(bool active)
    {
        if (ToolManager.Instance.Imode == ToolManager.InteractionMode.Desktop)
        {
            Destroy(this.hi5Laser.holder);
            Destroy(this.hi5Laser.pointer);
            this.hi5Laser.enabled = false;
        }
        else
        {
            if(active == false)
            {
                Destroy(this.hi5Laser.holder);
                Destroy(this.hi5Laser.pointer);
                this.hi5Laser.enabled = false;

            }
            else if(active == true)
            {
                this.hi5Laser.enabled = true;

                this.hi5Laser.enAbled();
            }
        }
    }
}