using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HI5
{
    public enum ECalibrationInterfaceResult
    {
        ENone = 0,
        EBposSuccess = 1,
        EBposFailed = 2,
        EPposComplete = 3
    }

    public class CalibrationInterfaceResult
    {
        public ECalibrationInterfaceResult result = ECalibrationInterfaceResult.ENone;
    }
    public class HI5_Glove_Calibration_Process_Interface : MonoBehaviour
    {
        private HI5_Pose mPos;
        private float mPercent;
        public void StartCalibration(HI5_Pose posType)
        {
            mPercent = 0.0f;
            if (posType == HI5_Pose.BPose)
            {
                HI5_Manager.GetGloveStatus().BposErr = BPoseCalibrationErrors.BE_NotCalibrated;
                HI5_Calibration.ResetCalibration();
                HI5_Manager.GetGloveStatus().StartCalibrationBpos();
            }
            HI5_Calibration.StartCalibration(posType);
            mPos = posType;
            StartCoroutine(UpdateCalibrationProgress());
        }

        public float GetCalibrationProgress()
        {
            return mPercent;
        }

        IEnumerator UpdateCalibrationProgress()
        {
            int calibrationProgress = 0;
            while (calibrationProgress < 100)
            {
                calibrationProgress = HI5_Calibration.GetCalibrationProgress(mPos);
                mPercent = (float)calibrationProgress;
                yield return null;
            }
            if (mPos == HI5_Pose.BPose)
            {
                if(HI5_Manager.GetGloveStatus().BposReceiveResult)
                {
                    if (HI5_Manager.GetGloveStatus().IsCalibrationBposSuccess)
                    {
                        CalibrationInterfaceResult result = new CalibrationInterfaceResult();
                        result.result = ECalibrationInterfaceResult.EBposSuccess;
                        Hi5_Message.GetInstance().DispenseMessage(Hi5_Message.Hi5_MessageMessageKey.messageCalibrationResult,(object)result);
                    }
                    else
                    {
                        CalibrationInterfaceResult result = new CalibrationInterfaceResult();
                        result.result = ECalibrationInterfaceResult.EBposFailed;
                        Hi5_Message.GetInstance().DispenseMessage(Hi5_Message.Hi5_MessageMessageKey.messageCalibrationResult, (object)result);
                    }
                }
                else
                    yield return null;
            }
            else
            {
                CalibrationInterfaceResult result = new CalibrationInterfaceResult();
                result.result = ECalibrationInterfaceResult.EPposComplete;
                Hi5_Message.GetInstance().DispenseMessage(Hi5_Message.Hi5_MessageMessageKey.messageCalibrationResult, (object)result);
            }
        }
    }
}
