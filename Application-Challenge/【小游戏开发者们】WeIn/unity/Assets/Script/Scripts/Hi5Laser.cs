//======= Copyright (c) Valve Corporation, All rights reserved. ===============
using UnityEngine;
using System.Collections;
using Valve.VR.Extras;

namespace Hi5_Interaction_Core
{
    public class Hi5Laser : MonoBehaviour
    {
        //public SteamVR_Behaviour_Pose pose;

        //public SteamVR_Action_Boolean interactWithUI = SteamVR_Input.__actions_default_in_InteractUI;
        //public SteamVR_Action_Boolean interactWithUI = SteamVR_Input.GetBooleanAction("InteractUI");

        public bool active = true;
        public Color color;
        public float thickness = 0.002f;
        public Color clickColor = Color.green;
        public GameObject holder;
        public GameObject pointer;
        public GameObject Fire;
        bool isActive = false;
        public bool addRigidBody = false;
        public Transform reference;
        public event PointerEventHandler PointerIn;
        public event PointerEventHandler PointerOut;
        public event PointerEventHandler PointerClick;
        public Hi5_Glove_Interaction_Hand HI5_Left_Human_Collider;
        public Hi5_Glove_Interaction_Hand HI5_Right_Human_Collider;
        private Hi5InputController vrcon;
        private int inputState;
        public Transform finger;


        Transform previousContact = null;


        private void Start()
        {
            /*            if (pose == null)
                            pose = this.GetComponent<SteamVR_Behaviour_Pose>();
                        if (pose == null)
                            Debug.LogError("No SteamVR_Behaviour_Pose component found on this object", this);

                        if (interactWithUI == null)
                            Debug.LogError("No ui interaction action has been set on this component.", this);*/
            HI5_Left_Human_Collider = GameObject.Find("HI5_Left_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
            HI5_Right_Human_Collider = GameObject.Find("HI5_Right_Human_Collider").GetComponent<Hi5_Glove_Interaction_Hand>();
            vrcon = GameObject.Find("Hi5InputController").GetComponent<Hi5InputController>();


            /*            holder = new GameObject();
                        holder.transform.parent = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform;
                        holder.transform.localPosition = Vector3.zero;
                        holder.transform.localRotation = Quaternion.identity;

                        pointer = GameObject.CreatePrimitive(PrimitiveType.Cube);
                        pointer.transform.parent = holder.transform;
                        pointer.transform.localScale = new Vector3(thickness, thickness, 100f);
                        pointer.transform.localPosition = new Vector3(0f, 0f, 50f);
                        pointer.transform.localRotation = Quaternion.identity;
                        BoxCollider collider = pointer.GetComponent<BoxCollider>();
                        if (addRigidBody)
                        {
                            if (collider)
                            {
                                collider.isTrigger = true;
                            }
                            Rigidbody rigidBody = pointer.AddComponent<Rigidbody>();
                            rigidBody.isKinematic = true;
                        }
                        else
                        {
                            if (collider)
                            {
                                Object.Destroy(collider);
                            }
                        }
                        Material newMaterial = new Material(Shader.Find("Unlit/Color"));
                        newMaterial.SetColor("_Color", color);
                        pointer.GetComponent<MeshRenderer>().material = newMaterial;*/
        }

        internal void enAbled()
        {
            holder = new GameObject();
            finger = HI5_Right_Human_Collider.mFingers[Hi5_Glove_Interaction_Finger_Type.EIndex].mChildNodes[4].transform;
            finger.localRotation = Quaternion.Euler(0,90,0);
/*            Fire = GameObject.CreatePrimitive(PrimitiveType.Cube);
            Fire.transform.localScale = new Vector3(0.1f, 0.1f, 0.1f);
            Fire.transform.parent = finger.transform;*/
            holder.transform.parent = finger.transform;
            holder.transform.localPosition = Vector3.zero;
            holder.transform.localRotation = Quaternion.identity;

            pointer = GameObject.CreatePrimitive(PrimitiveType.Cube);
            pointer.transform.parent = holder.transform;
            pointer.transform.localScale = new Vector3(thickness, thickness, 100f);
            pointer.transform.localPosition = new Vector3(0f, 0f, 50f);
            pointer.transform.localRotation = Quaternion.identity;
            BoxCollider collider = pointer.GetComponent<BoxCollider>();
            if (addRigidBody)
            {
                if (collider)
                {
                    collider.isTrigger = true;
                }
                Rigidbody rigidBody = pointer.AddComponent<Rigidbody>();
                rigidBody.isKinematic = true;
            }
            else
            {
                if (collider)
                {
                    Object.Destroy(collider);
                }
            }
            Material newMaterial = new Material(Shader.Find("Unlit/Color"));
            newMaterial.SetColor("_Color", color);
            pointer.GetComponent<MeshRenderer>().material = newMaterial;
        }

 /*       public virtual void OnPointerIn(PointerEventArgs e)
        {
            if (PointerIn != null)
                PointerIn(this, e);
        }

        public virtual void OnPointerClick(PointerEventArgs e)
        {
            if (PointerClick != null)
                PointerClick(this, e);
        }

        public virtual void OnPointerOut(PointerEventArgs e)
        {
            if (PointerOut != null)
                PointerOut(this, e);
        }*/


        private void Update()
        {
            inputState = vrcon.laserObjectInput();
            if (!isActive)
            {
                isActive = true;
                this.transform.GetChild(0).gameObject.SetActive(true);
            }

            float dist = 100f;

            Ray raycast = new Ray(holder.transform.position, holder.transform.forward);
            RaycastHit hit;
            bool bHit = Physics.Raycast(raycast, out hit);

            if (previousContact && previousContact != hit.transform)
            {

                previousContact = null;
            }
            if (bHit && previousContact != hit.transform)
            {

                previousContact = hit.transform;
            }
            if (!bHit)
            {
                previousContact = null;
            }
            if (bHit && hit.distance < 100f)
            {
                dist = hit.distance;
            }

            if (bHit && inputState == 1)
            {

            }

            if (inputState == 2)
            {
                pointer.transform.localScale = new Vector3(thickness * 5f, thickness * 5f, dist);
                pointer.GetComponent<MeshRenderer>().material.color = clickColor;
            }
            else if(inputState == 3)
            {
                pointer.transform.localScale = new Vector3(thickness, thickness, dist);
                pointer.GetComponent<MeshRenderer>().material.color = color;
            }
            pointer.transform.localPosition = new Vector3(0f, 0f, dist / 2f);
        }
    }

/*    public struct PointerEventArgs
    {
        //public SteamVR_Input_Sources fromInputSource;
        public uint flags;
        public float distance;
        public Transform target;
    }

    public delegate void PointerEventHandler(object sender, PointerEventArgs e);*/
}