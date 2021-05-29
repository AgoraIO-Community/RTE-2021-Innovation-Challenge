using UnityEngine;
using HI5.VRInteraction;

namespace HI5.VRCalibration
{
    public class StartButton : VRButton
    {
        [SerializeField] private CountDownUI m_CountDownUI;
        [SerializeField] private CalibrationStateMachine m_CalibrationSM;

        new void OnEnable()
        {
            base.OnEnable();
            m_SelectionRadial.OnSelectionComplete += HandleSelectionComplete;

            EnableButton(true);
        }

        new void OnDisable()
        {
            base.OnDisable();
            m_SelectionRadial.OnSelectionComplete -= HandleSelectionComplete;
        }

        private void HandleSelectionComplete()
        {
            if (m_GazeOver)
            {
                if (m_CountDownUI != null)
                    m_CountDownUI.StartCD();

                HI5_Calibration.ResetCalibration();
                EnableButton(false);
            }
        }

        private void EnableButton(bool value)
        {
            GetComponent<Collider>().enabled = value;
        }
    }
}