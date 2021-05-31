using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace HI5.VRCalibration
{
    public class SwitchHandButtonState : MonoBehaviour
    {
        [SerializeField]
        private MenuStateMachine m_MenuSM;
        [SerializeField]
        private HI5_VIVEInstance m_HI5_Instance;

        private GameObject posRef;
        private bool isEnabled = false;

        private void OnEnable()
        {
            if(m_MenuSM != null)
                 m_MenuSM.OnStateEnter += HandleStateEnter;
        }

        private void OnDisable()
        {
            if (m_MenuSM != null)
                m_MenuSM.OnStateEnter -= HandleStateEnter;
        }

        private void Start()
        {
            EnableButton(false);
        }

        private void Update()
        {
            if (this == null)
                return;
            if (!isEnabled)
                return;
            if(m_HI5_Instance != null)
                EnableButton(m_HI5_Instance.IsValid);
        }

        private void HandleStateEnter(MenuState state)
        {
            if (isEnabled)
                return;

            if (state == MenuState.Exit)
            {
                EnableButton(true);
                isEnabled = true;
            }
        }

        private void EnableButton(bool value)
        {
            if (GetComponent<Renderer>())
                GetComponent<Renderer>().enabled = value;

            if (GetComponent<Collider>())
                GetComponent<Collider>().enabled = value;
        }
    }
}
