using System.Collections;
using System.Collections.Generic;
using UnityEngine;

namespace HI5.VRCalibration
{
    public class MagneticInfo : MonoBehaviour
    {

        [SerializeField]
        private Hand m_Type;
        [SerializeField]
        private SpriteRenderer m_SpritRenderer;
        [SerializeField]
        private Sprite m_Bad;
        [SerializeField]
        private Sprite m_Fair;
        [SerializeField]
        private Sprite m_Good;

        private MagneticStatus currentState = MagneticStatus.Good;
        private HI5_GloveStatus m_HI5Status;

        private void OnEnable()
        {
            m_HI5Status = HI5_Manager.GetGloveStatus();
        }

        private void OnDisable()
        {
        }

        private void Start()
        {
            SetMagneticStateSpriteRenderer(currentState);
        }

        private void Update()
        {
            //ruige 2019 6 25
            //currentState = m_HI5Status.GetMagneticState(m_Type);
            if (m_Type == Hand.LEFT)
                currentState = HI5_Manager.magneticStatus.statusLeft;
            else if (m_Type == Hand.RIGHT)
                currentState = HI5_Manager.magneticStatus.statusRight;
            if (m_HI5Status.IsGloveAvailable(m_Type))
                SetMagneticStateSpriteRenderer(currentState);
            else
                SetMagneticStateSpriteRenderer(MagneticStatus.Unknown);
        }

        private void SetMagneticStateSpriteRenderer(MagneticStatus state)
        {
            switch (state)
            {
                case MagneticStatus.Unknown:
                    m_SpritRenderer.sprite = null;
                    break;
                case MagneticStatus.Good:
                    m_SpritRenderer.sprite = m_Good;
                    break;
                case MagneticStatus.Fair:
                    m_SpritRenderer.sprite = m_Fair;
                    break;
                case MagneticStatus.Bad:
                    m_SpritRenderer.sprite = m_Bad;
                    break;
            }
        }
    }
}

