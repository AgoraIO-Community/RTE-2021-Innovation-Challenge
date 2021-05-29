//======= Copyright (c) Beijing Noitom Technology Ltd., All rights reserved. ===============
//
// Purpose: Provide the interface between Steam tracked objects to Hi5.
//
//==========================================================================================

using UnityEngine;
using Valve.VR;
using HI5;

[RequireComponent(typeof(SteamVR_TrackedObject))]
public class HI5_TrackedDeviceInterface : MonoBehaviour
{
    [SerializeField]
    private SteamVR_TrackedObject m_SteamVR_TrackedObject;

    private string m_DeviceSerialNumber = null;
    private int m_Index = -1;

    SteamVR_Events.Action newPosesAction;

    private void Awake()
    {
        newPosesAction = SteamVR_Events.NewPosesAction(OnNewPoses);

        m_SteamVR_TrackedObject = GetComponent<SteamVR_TrackedObject>();
    }

    private void OnEnable()
    {
        if(newPosesAction != null)
            newPosesAction.enabled = true;
        HI5_Calibration.OnCalibrationComplete += HandleCalibrationComplete;
    }

    private void OnDisable()
    {
        newPosesAction.enabled = false;
        HI5_Calibration.OnCalibrationComplete -= HandleCalibrationComplete;
    }


    private void OnNewPoses(TrackedDevicePose_t[] poses)
    {
 
        if (!m_SteamVR_TrackedObject.isValid)
            return;

        int index = (int)m_SteamVR_TrackedObject.index;

        if (index == -1)
            return;

        if (poses.Length <= index)
            return;

        if (!poses[index].bDeviceIsConnected)
            return;

        if (!poses[index].bPoseIsValid)
            return;

        if (poses[index].eTrackingResult != ETrackingResult.Running_OK)
            return;

        // set index on this device
        m_Index = index;

        // get and check device type
        OPTDeviceType type = GetDeviceClass(index);
        if (type == OPTDeviceType.Unknown)
            return;

        // get and check device serial number
        string deviceSN = GetStringProperty((uint)index, ETrackedDeviceProperty.Prop_SerialNumber_String);
        if (deviceSN == null)
            return;

        m_DeviceSerialNumber = deviceSN;

        // get device position and rotation
        var pose = new SteamVR_Utils.RigidTransform(poses[index].mDeviceToAbsoluteTracking);
        Vector3 pos = pose.pos;
        Quaternion rot = pose.rot;

        HI5_DataTransform.PushOpticalData(deviceSN, type, pos, rot);

        if (HI5_BindInfoManager.IsGloveBinded(Hand.LEFT) && HI5_BindInfoManager.IsGloveBinded(Hand.RIGHT))
            return;           

        CheckDeviceBinded(index);
    }

    private void HandleCalibrationComplete(HI5_Pose pose)
    {
        if (pose == HI5_Pose.BPose)
            CheckDeviceBinded(m_Index);
    }

    private OPTDeviceType GetDeviceClass(int index)
    {
        var system = OpenVR.System;
        if (system != null)
        {
            var deviceClass = system.GetTrackedDeviceClass((uint)index);
            if (deviceClass == ETrackedDeviceClass.Controller)
                return OPTDeviceType.HTC_VIVE_Controller;
            if (deviceClass == ETrackedDeviceClass.GenericTracker)
                return OPTDeviceType.HTC_VIVE_Tracker;

            return OPTDeviceType.Unknown;
        }
        else
        {
            return OPTDeviceType.Unknown;
        }
    }

    private string GetStringProperty(uint deviceId, ETrackedDeviceProperty prop)
    {
        var hmd = OpenVR.System;
        if (hmd == null)
            return null;

        var error = ETrackedPropertyError.TrackedProp_Success;
        var capactiy = hmd.GetStringTrackedDeviceProperty(deviceId, prop, null, 0, ref error);
        if (capactiy > 1)
        {
            var result = new System.Text.StringBuilder((int)capactiy);
            hmd.GetStringTrackedDeviceProperty(deviceId, prop, result, capactiy, ref error);
            return result.ToString();
        }
        return (error != ETrackedPropertyError.TrackedProp_Success) ? error.ToString() : "<unknown>";
    }

    private void CheckDeviceBinded(int index)
    {
        if (!m_SteamVR_TrackedObject.isValid)
            return;

        HI5_BindInfoManager.LoadItems();

        if (HI5_BindInfoManager.CheckDeviceBinded(Hand.LEFT, m_DeviceSerialNumber))
        {
            HI5_BindInfoManager.LeftID = index;
            return;
        }
        if (HI5_BindInfoManager.CheckDeviceBinded(Hand.RIGHT, m_DeviceSerialNumber))
        {
            HI5_BindInfoManager.RightID = index;
            return;
        }

        EnableRenderer(true);
    }

    private void EnableRenderer(bool value)
    {
        if (GetComponent<Renderer>())
            GetComponent<Renderer>().enabled = value;
    }

}