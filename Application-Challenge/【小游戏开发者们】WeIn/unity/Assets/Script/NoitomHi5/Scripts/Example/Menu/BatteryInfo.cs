using UnityEngine;

namespace HI5.VRCalibration
{
    public class BatteryInfo : MonoBehaviour
    {
        [SerializeField] private Hand m_Type;
        [SerializeField] private SpriteRenderer m_SpritRenderer;
        [SerializeField] private Sprite m_Low;
        [SerializeField] private Sprite m_Normal;
        [SerializeField] private Sprite m_Full;

        private PowerLevel m_CurrentLevel = PowerLevel.Full;
        private HI5_GloveStatus m_HI5Status;

        private void OnEnable()
        {
            m_HI5Status = HI5_Manager.GetGloveStatus();
        }

        private void Start()
        {
            SetPowerLevelSpriteRenderer(m_CurrentLevel);
        }

        private void Update()
        {
            m_CurrentLevel = m_HI5Status.GetPowerLevel(m_Type);

            if (m_HI5Status.IsGloveAvailable(m_Type))
                SetPowerLevelSpriteRenderer(m_CurrentLevel);
            else
                SetPowerLevelSpriteRenderer(PowerLevel.Unknown);
        }

        private void SetPowerLevelSpriteRenderer(PowerLevel level)
        {
            switch (level)
            {
                case PowerLevel.Unknown:
                    m_SpritRenderer.sprite = null;
                    break;
                case PowerLevel.Full:
                    m_SpritRenderer.sprite = m_Full;
                    break;
                case PowerLevel.Normal:
                    m_SpritRenderer.sprite = m_Normal;
                    break;
                case PowerLevel.Low:
                    m_SpritRenderer.sprite = m_Low;
                    break;
            }
        }
    }
}

