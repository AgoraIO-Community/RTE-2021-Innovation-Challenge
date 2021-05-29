using System.Collections;
using UnityEngine;

namespace HI5.VRCalibration
{
    public class CalibrationInstance : MonoBehaviour
    {
        [SerializeField] private HI5_Pose m_Pose;
        [SerializeField] private CountDownUI m_CountDownUI;
        [SerializeField] private GameObject m_Progress;
        [SerializeField] private CalibrationStateMachine m_CalibrationSM;

        private void OnEnable()
        {
            m_CountDownUI.OnCountDownComplete += HandleCountDownComplete;
            HI5_Calibration.OnCalibrationComplete += HandleCalibrationComplete;
            ResetProgress();
        }

        private void OnDisable()
        {
            m_CountDownUI.OnCountDownComplete -= HandleCountDownComplete;
            HI5_Calibration.OnCalibrationComplete -= HandleCalibrationComplete;
            ResetProgress();
        }

        private void HandleCountDownComplete()
        {
            if (m_Pose == HI5_Pose.BPose)
            {
                HI5_Calibration.ResetCalibration();
                HI5_Manager.GetGloveStatus().StartCalibrationBpos();
                
                //删除文件
                // System.IO.File.Delete(HI5_Calibration.DefaultPathAndName);
            }
            HI5_Calibration.StartCalibration(m_Pose);
            StartCoroutine(UpdateCalibrationProgress());
        }

        IEnumerator UpdateCalibrationProgress()
        {
            int calibrationProgress = 0;
            while (calibrationProgress < 100)
            {
                calibrationProgress = HI5_Calibration.GetCalibrationProgress(m_Pose);

                float percent = (float)calibrationProgress / 100;

                Vector3 scale = m_Progress.transform.localScale;
                m_Progress.transform.localScale = new Vector3(percent, scale.y, scale.z);

                yield return null;
            }

            yield return new WaitForSeconds(1f);
           
            if (HI5_Calibration.OnCalibrationComplete != null)
                HI5_Calibration.OnCalibrationComplete(m_Pose);

        }

        private void ResetProgress()
        {
            Vector3 scale = m_Progress.transform.localScale;
            m_Progress.transform.localScale = new Vector3(0f, scale.y, scale.z);
        }


        private void HandleCalibrationComplete(HI5_Pose pose)
        {
            if (pose == HI5_Pose.BPose)
            {
                //HI5_GloveStatus.
                m_CalibrationSM.State = CalibrationState.PPose;
                return;
            }
            if (pose == HI5_Pose.PPose)
            {
                m_CalibrationSM.State = CalibrationState.Finish;
                return;
            }

        }
    }
}

