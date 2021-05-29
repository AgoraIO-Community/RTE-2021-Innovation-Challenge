using UnityEngine;
using HI5.VRInteraction;

namespace HI5.VRCalibration
{
    public class HelpStateButton : VRButton
    {
        public HelpState EnterState;

        [SerializeField] private HelpStateMachine m_HelpSM;

        new void OnEnable()
        {
            base.OnEnable();
            m_SelectionRadial.OnSelectionComplete += HandleSelectionComplete;
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
                m_HelpSM.State = EnterState;
            }
        }
    }

}