using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
namespace Hi5_Interaction_Core
{
    public class Hi5ButtonRect : MonoBehaviour
    {
        internal bool isButtonTrigger = false;
        float cd = 0.8f;
        public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
        public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;
        public Transform HMDTransform;
        public Hi5Laser hi5Laser;

        internal Vector3 buttonPosition;

        internal void setButtonPosition(float addY)
        {
            buttonPosition.x = HMDTransform.position.x;
            buttonPosition.y = HMDTransform.position.y+1.8f+addY ;
            buttonPosition.z = HMDTransform.position.z + 0.5f;
        }

        // Start is called before the first frame update
        void Start()
        {
            HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
            HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
            hi5Laser = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5Laser>();
        }

        void Awake()
        {
            HMDTransform = GameObject.Find("HMDPosition").GetComponent<Transform>();

        }

        // Update is called once per frame
        void Update()
        {
        }

        internal bool IsTouch()
        {
            Transform tailIndex_right = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4];
            Transform tailIndex_left = HI5_Left_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4];
            float distance_1 = Vector3.Distance(buttonPosition, tailIndex_right.position);
            float distance_2 = Vector3.Distance(buttonPosition, tailIndex_left.position);


            if (distance_1 < 0.1f || distance_1 < 0.1f)
            {
                ChangeColor();
                return true;
            }
            else
            {
                unChangeColor();
                return false;
            }
        }

        internal void ChangeColor()
        {
            if (this.gameObject.GetComponent<Image>().color != Color.yellow)
            {
                var image = this.gameObject.GetComponent<Image>();
                image.color = Color.yellow;
            }
        }

        internal void unChangeColor()
        {
            if (this.gameObject.GetComponent<Image>().color != Color.white)
            {
                var image = this.gameObject.GetComponent<Image>();
                image.color = Color.white;
            }
        }
    }
}