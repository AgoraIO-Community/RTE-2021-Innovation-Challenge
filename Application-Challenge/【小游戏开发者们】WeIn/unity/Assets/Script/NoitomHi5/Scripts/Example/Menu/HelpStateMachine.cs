using UnityEngine;
using System;

namespace HI5.VRCalibration
{
    public enum HelpState
    {
        Exit = -1,
        Page_0 = 0,
        Page_1,
    }

    public class HelpStateMachine : MonoBehaviour
    {
        [SerializeField] private MenuStateMachine m_MenuSM;
        [SerializeField] private GameObject[] m_StateItems;

        // Use this action on your own project to call the specific function after enter a state.
        public Action<HelpState> OnStateEnter;

        public HelpState State
        {
            get { return currentState; }
            set
            {
                StateExit(currentState);
                currentState = value;
                StateEnter(currentState);
            }
        }

        private HelpState currentState;

        private void OnEnable()
        {
            m_MenuSM.OnStateEnter += HandleMenuStateEnter;

            currentState = HelpState.Page_0;
            this.State = currentState;

            HI5_Calibration.ResetCalibration();
        }

        private void OnDisable()
        {
            m_MenuSM.OnStateEnter -= HandleMenuStateEnter;
        }

        private void StateExit(HelpState stateExit)
        {
        }

        private void StateEnter(HelpState stateEnter)
        {
            if (OnStateEnter != null)
                OnStateEnter(stateEnter);

            ActiveItem((int)stateEnter);
        }

        private void ActiveItem(int index)
        {
            for (int i = 0; i < m_StateItems.Length; i++)
            {
                if (m_StateItems[i] != null)
                    m_StateItems[i].SetActive(false);
            }

            if (index > m_StateItems.Length && index < 0)
                return;

            if (index > -1 && index < m_StateItems.Length)
                m_StateItems[index].SetActive(true);
        }

        private void HandleMenuStateEnter(MenuState state)
        {
            if (state == MenuState.Help)
                currentState = HelpState.Page_0;

            if (state != MenuState.Help)
                currentState = HelpState.Exit;
        }
    }
}

