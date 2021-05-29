using System.Collections;
using System.Collections.Generic;
using UnityEngine;
using System;

namespace HI5.VRCalibration
{
    public enum MenuState
    {
        Exit = -1,
        Main = 0,
        Help,
        Calibration,
    }

    public class MenuStateMachine : MonoBehaviour
    {
        static MenuStateMachine clone = null;
        public static MenuStateMachine GetInstanceMenuStateMachine()
        {
            return clone;
        }

        public Action<MenuState> OnStateEnter;

        [SerializeField] private GameObject[] m_StateItems;
        [SerializeField] private GameObject[] m_BasicElements;
        public bool isFirstEnterCLose  = false;
        public MenuState State
        {
            get { return currentState; }
            set
            {
                StateExit(currentState);
                currentState = value;
                if (currentState == MenuState.Exit)
                    isFirstEnterCLose = true;
               // Hi5_Message.GetInstance().DispenseMessage(Hi5_Message.Hi5_MessageMessageKey.messageStateChange, (object)currentState,null);
                StateEnter(currentState);
            }
        }

        private MenuState currentState;

        private void Awake()
        {
            MenuStateMachine.clone = this;
            
        }

        private void OnEnable()
        {
            
            OnStateEnter += HandleStateEnter;

            currentState = MenuState.Main;
            //Hi5_Message.GetInstance().DispenseMessage(Hi5_Message.Hi5_MessageMessageKey.messageStateChange, (object)currentState, null);
            this.State = currentState;

            EnableBasicElements(true);
        }

        private void OnDisable()
        {
            OnStateEnter -= HandleStateEnter;
        }

        private void StateExit(MenuState exitState)
        {
        }

        private void StateEnter(MenuState enterState)
        {
            if (OnStateEnter != null)
                OnStateEnter(enterState);

            ActiveItem((int)enterState);
        }

        private void ActiveItem(int index)
        {
            for (int i = 0; i < m_StateItems.Length; i++)
            {
                if (m_StateItems[i] != null)
                    m_StateItems[i].SetActive(false);
            }

            if (index > m_StateItems.Length || index < 0)
                return;

            m_StateItems[index].SetActive(true);
        }

        private void HandleStateEnter(MenuState state)
        {
            if (state != MenuState.Exit)
                EnableBasicElements(true);

            if (state == MenuState.Exit)
                EnableBasicElements(false);
        }

        private void EnableBasicElements(bool value)
        {
            for (int i = 0; i < m_BasicElements.Length; i++)
            {
                if (m_BasicElements[i] != null)
                    m_BasicElements[i].SetActive(value);
            }
        }
    }

}
