using UnityEngine;
using HI5.VRInteraction;

namespace HI5.VRCalibration
{
    public class CalibrationButton : VRButton
    {
        [SerializeField] private CalibrationState m_EnterState;
        [SerializeField] private CalibrationStateMachine m_CalibrationSM;

        new void OnEnable()
        {
            base.OnEnable();
            m_SelectionRadial.OnSelectionComplete += HandleSelectionComplete;

            EnableButton(true);
        }

        new void OnDisable()
        {
            //if (GetComponent<VRInteractiveItem>() != null)
            //    GetComponent<VRInteractiveItem>().Out();
            base.OnDisable();
        
            m_SelectionRadial.OnSelectionComplete -= HandleSelectionComplete;
        }

        private void HandleSelectionComplete()
        {
            if (m_GazeOver)
            {
                m_CalibrationSM.State = m_EnterState;
            }
        }

        private void EnableButton(bool value)
        {
            GetComponent<Renderer>().enabled = value;
            GetComponent<Collider>().enabled = value;
        }
    }

}
