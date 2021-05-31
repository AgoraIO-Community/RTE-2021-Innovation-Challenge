using UnityEngine;
using HI5.VRInteraction;

namespace HI5.VRCalibration
{
    public class MenuStateButton : VRButton
    {
        public MenuState EnterState;

        [SerializeField] private MenuStateMachine m_MenuSM;

        new void OnEnable()
        {
            base.OnEnable();
            m_SelectionRadial.OnSelectionComplete += HandleSelectionComplete;
        }

        new void OnDisable()
        {
            base.OnDisable();

            //ruige red
           // m_SelectionRadial.Hide();

            m_SelectionRadial.OnSelectionComplete -= HandleSelectionComplete;
        }

        private void HandleSelectionComplete()
        {
            if (m_GazeOver)
            {
                m_MenuSM.State = EnterState;
            }
        }
    }
}
