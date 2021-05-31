using System.Collections;
using UnityEngine;

namespace HI5.VRCalibration
{
    public class CloseState : MonoBehaviour
    {
        [SerializeField] private MenuStateMachine m_MenuSM;
        [SerializeField] private SpriteRenderer m_SpriteRenderer;

        private bool isShowed = false;
        private bool isClosed = false;

        private void OnEnable()
        {
            m_MenuSM.OnStateEnter += HandleStateEnter;
            m_SpriteRenderer.enabled = false;
        }

        private void OnDisable()
        {
            m_MenuSM.OnStateEnter -= HandleStateEnter;
        }

        private void HandleStateEnter(MenuState state)
        {
            if (isShowed && isClosed)
                return;

            if (state == MenuState.Main)
            {
                if (isShowed)
                {
                    m_SpriteRenderer.enabled = false;
                    isClosed = true;
                }
            }

            if (state == MenuState.Exit)
            {
                isShowed = true;
                m_SpriteRenderer.enabled = true;
                StartCoroutine(AutoClose());
            }
        }

        IEnumerator AutoClose()
        {
            yield return new WaitForSeconds(5f);
            m_SpriteRenderer.enabled = false;
            isClosed = true;
        }
    }
}
