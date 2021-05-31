//======= Copyright (c) Beijing Noitom Technology Ltd., All rights reserved. ===============
//
// Purpose: Apply the Hi5 data on rigged model.
//
//==========================================================================================

using UnityEngine;
using System.Collections;
using System.Collections.Generic;
using Valve.VR;
using HI5.VRCalibration;

    namespace HI5
{
    public class HI5_VIVEInstance : HI5_Instance
    {
        [SerializeField] private Renderer m_Renderer;
        internal  bool isVisible = true;
        public bool IsValid { get { return isValid; } }
        private bool isValid = false;

        private HI5_GloveStatus m_Status;
        //private List<Bones> m_ValidBones = new List<Bones>();
        private static int m_INDEX_Hand = (int)Bones.Hand;

        private HumanButtons mHumanButtons;

        SteamVR_Events.Action newPosesAction;
        
        private void Awake()
        {
            newPosesAction = SteamVR_Events.NewPosesAction(OnNewPoses);
            mHumanButtons = GetComponentInChildren<HumanButtons>();
      
        }

        new void OnEnable()
        {
            base.OnEnable();
            //LoadValidBones();
            if(newPosesAction != null)
                newPosesAction.enabled = true;
        }

        new void OnDisable()
        {
            base.OnDisable();
            if(newPosesAction != null)
                newPosesAction.enabled = false;
        }

        private void Start()
        {
            m_Status = HI5_Manager.GetGloveStatus();
        }

        new void Update()
        {
            base.Update();
            //ruige 2018 3 27
            if (m_Status != null)
            {
                if (HI5_Manager.modifyThreadSave)
                {
                    m_Status.MainThreadUpdate();
                    HI5_Manager.Update();
                }

            }
            CheckIsValid();
            UpdateModelRenderer(isValid);

            if (!isValid)
                return;
            //if (m_Status.Status == GloveStatus.NoDongle)
            //    return;
            if (m_BindSource != null)
            {
                ApplyHandMotion_Rotation(m_BindSource);
            }

            if (m_BindSource != null)
            {
                ApplyFingerMotion(m_BindSource);
            }
        }

        private void CheckIsValid()
        {
            bool isAvailable = false;
            if (m_Status != null)
                isAvailable  = m_Status.IsGloveAvailable(HandType);
            bool isBinded = HI5_BindInfoManager.IsGloveBinded(HandType);
            bool isBposSuccess = HI5_Manager.GetGloveStatus().isGloveBPosSuccess();
           
            if (isAvailable && isBinded && isBposSuccess)
            {
                if (HI5_Calibration.IsCalibratingBPose)
                    isValid = false;
                else
                    isValid = true;
            }
            else
                isValid = false;
           
            //if (HI5_Calibration.IsCalibratingBPose)
            //    isValid = false;
        }

        private void UpdateModelRenderer(bool value)
        {
            bool isAvailable = m_Status.IsGloveAvailable(HandType);
            /*if(!isAvailable)
            {
                Hi5_Log.Log("isAvailable dnot");
            }*/
             if (isVisible)
            {
                m_Renderer.gameObject.SetActive(value);
              
            }
            else
            {
                m_Renderer.gameObject.SetActive(false);
                
            }

            if (mHumanButtons != null)
            {
                mHumanButtons.setVisible(value);

            }
        }

        private void ApplyFingerMotion(HI5_Source source)
        {
            for (int i = (m_INDEX_Hand +1 ); i < (int)Bones.NumOfHI5Bones && i < HandBones.Length; i++)
            {
                if (HandBones[i] != null)
                {
                    SetRotation(HandBones, i, source.GetReceivedRotation(i, HandType));
                }
            }
        }

        private void ApplyHandMotion_Rotation(HI5_Source source)
        {
            if (HandBones[m_INDEX_Hand] != null)
            {
                HandBones[m_INDEX_Hand].localEulerAngles = HI5_DataTransform.ToUnityEulerAngles(source.GetReceivedRotation(m_INDEX_Hand, HandType));
            }
        }

        private void ApplyHandMotion_Position(Vector3 pos, Quaternion rot)
        {
            Vector3 offset = HandType == Hand.LEFT ? HI5_Manager.LeftOffset : HI5_Manager.RightOffset;
            Vector3 handPos = pos + rot * offset;

            if (HandBones[m_INDEX_Hand] != null)
            {
                
                if (!float.IsNaN(handPos.x) && !float.IsNaN(handPos.y) && !float.IsNaN(handPos.z))
                    HandBones[m_INDEX_Hand].localPosition = handPos;
            }
        }

        
        private void SetPosition(Transform[] bones, int bone, Vector3 position)
        {
            Transform t = bones[(int)bone];
            if (t != null)
            {
                Vector3 pos = position;
                if (!float.IsNaN(pos.x) && !float.IsNaN(pos.y) && !float.IsNaN(pos.z))
                {
                    t.localPosition = pos;
                }
            }
        }

        private void SetRotation(Transform[] bones, int bone, Vector3 rotation)
        {
            Transform t = bones[(int)bone];
            if (t != null)
            {
                Quaternion rot = Quaternion.Euler(rotation);
                if (!float.IsNaN(rot.x) && !float.IsNaN(rot.y) && !float.IsNaN(rot.z) && !float.IsNaN(rot.w))
                {
                    t.localRotation = rot;
                }
            }
        }

        private void OnNewPoses(TrackedDevicePose_t[] poses)
        {

            int index = HandType == Hand.LEFT ? HI5_BindInfoManager.LeftID : HI5_BindInfoManager.RightID;

            if (index == -1)
                return;

            if (poses.Length <= index)
                return;

            if (!poses[index].bDeviceIsConnected)
                return;

            if (!poses[index].bPoseIsValid)
                return;

            var pose = new SteamVR_Utils.RigidTransform(poses[index].mDeviceToAbsoluteTracking);

            Vector3 pos = pose.pos;
            Quaternion rot = pose.rot;
            if (m_Status.Status != GloveStatus.NoGlove 
                && m_Status.Status != GloveStatus.NoDongle
                && HI5_Manager.IsDongleAvailable()
                && HI5_Manager.IsHasDongle)
                ApplyHandMotion_Position(pos, rot);
           
        }
        
        /*
        private void LoadValidBones()
        {
            // load valid bones 
            m_ValidBones.Add(Bones.ForeArm);
            m_ValidBones.Add(Bones.Hand);
            m_ValidBones.Add(Bones.HandThumb1);
            m_ValidBones.Add(Bones.HandThumb2);
            m_ValidBones.Add(Bones.HandThumb3);
            m_ValidBones.Add(Bones.InHandIndex);
            m_ValidBones.Add(Bones.HandIndex1);
            m_ValidBones.Add(Bones.HandIndex2);
            m_ValidBones.Add(Bones.HandIndex3);
            m_ValidBones.Add(Bones.InHandMiddle);
            m_ValidBones.Add(Bones.HandMiddle1);
            m_ValidBones.Add(Bones.HandMiddle2);
            m_ValidBones.Add(Bones.HandMiddle3);
            m_ValidBones.Add(Bones.InHandRing);
            m_ValidBones.Add(Bones.HandRing1);
            m_ValidBones.Add(Bones.HandRing2);
            m_ValidBones.Add(Bones.HandRing3);
            m_ValidBones.Add(Bones.InHandPinky);
            m_ValidBones.Add(Bones.HandPinky1);
            m_ValidBones.Add(Bones.HandPinky2);
            m_ValidBones.Add(Bones.HandPinky3);
        }
        */
    }
}
