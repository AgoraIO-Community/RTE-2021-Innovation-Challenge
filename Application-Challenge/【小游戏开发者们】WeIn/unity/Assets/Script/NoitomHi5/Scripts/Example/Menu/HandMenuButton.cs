using UnityEngine;
using HI5.VRInteraction;

namespace HI5.VRCalibration
{
    public class HandMenuButton : VRButton
    {
        [SerializeField]
        private MenuStateMachine m_MenuSM;

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
                if (m_MenuSM.State == MenuState.Exit)
                {
                    if (HI5_Manager.GetGloveStatus() == null )
                        m_MenuSM.State = MenuState.Main;
                    else
                    {

                        if (HI5_Manager.GetGloveStatus().IsLeftGloveAvailable 
                            && HI5_Manager.GetGloveStatus().IsRightGloveAvailable
                            && HI5_BindInfoManager.IsLeftGloveBinded && HI5_BindInfoManager.IsRightGloveBinded)
                        {
                            m_MenuSM.State = MenuState.Main;
                        }
                    }
                }
                else
                    m_MenuSM.State = MenuState.Exit;                
            }
        }
    }

}
