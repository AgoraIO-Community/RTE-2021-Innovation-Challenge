using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace HI5.VRCalibration
{
    public class PhysicalHandButton : MonoBehaviour
    {
        [SerializeField]
        private MenuStateMachine m_MenuSM;
        [SerializeField]
        private Collider m_InteractiveItemCollider;

        private float m_CoolDownf = 1f;
        private bool m_IsCollDown = false;
        private bool m_IsEnter = false;

        private void OnTriggerEnter(Collider col)
        {
            if (col.gameObject.GetComponent<HandInteractiveItem>())
            {
                if (!m_IsCollDown && !m_IsEnter)
                {
                    HandleTriggerEnter();
                    m_IsEnter = true;

                    StartCoroutine(CoolDown());
                    m_IsCollDown = true;
                }

                EnableEyeInteraction(false);
            }
        }

        private void OnTriggerExit(Collider col)
        {
            if (col.gameObject.GetComponent<HandInteractiveItem>())
            {
                m_IsEnter = false;
                EnableEyeInteraction(true);
            }
        }

        IEnumerator CoolDown()
        {
            float timer = m_CoolDownf;
            while (timer > 0)
            {
                timer -= Time.deltaTime;
                yield return null;
            }

            m_IsCollDown = false;
        }

        private void HandleTriggerEnter()
        {
			if (m_MenuSM == null)
				return;
            if (m_MenuSM.State == MenuState.Exit)
                if (HI5_Manager.GetGloveStatus() == null)
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
            else
                m_MenuSM.State = MenuState.Exit;
        }

        private void EnableEyeInteraction(bool value)
        {
            m_InteractiveItemCollider.enabled = value;
        }
    }
}

