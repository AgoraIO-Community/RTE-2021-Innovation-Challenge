//======= Copyright (c) Beijing Noitom Technology Ltd., All rights reserved. ===============
//
// Purpose: Connect and disconnect the Hi5 devices.
//
//==========================================================================================

using UnityEngine;

namespace HI5
{
    public class HI5_Instance : MonoBehaviour
    {
        public Hand HandType;

        [Header("Bone Transform Settings")]

        [SerializeField]
        protected Transform m_Root = null;

        [SerializeField]
        protected string m_Prefix = "Human_";

        public Transform[] HandBones = new Transform[(int)Bones.NumOfHI5Bones];

        protected HI5_Source m_BindSource = null;

        protected void OnEnable()
        {
            Connect();
        }

        protected void OnDisable()
        {
        }

        protected void OnApplicationQuit()
        {
            Disconnect();
        }

        protected void Update()
        {
        }

        protected void Connect()
        {
            if (!HI5_Manager.IsConnected) { HI5_Manager.Connect(); }
            m_BindSource = HI5_Manager.GetHI5Source();
        }

        protected void Disconnect()
        {
            if (HI5_Manager.IsConnected) { HI5_Manager.DisConnect(); }
            else { return; }
        }

        // Used by HI5_InstanceEditor.
        public void AutoBindBones(Hand type)
        {
            HI5_Manager.BindBones(m_Root, HandBones, m_Prefix, type);
        }

        // Returns the saved transform references in the HandBones list.
        public Transform GetBoneTransform(Bones bone)
        {
            return HandBones[(int)bone];
        }
    }
}