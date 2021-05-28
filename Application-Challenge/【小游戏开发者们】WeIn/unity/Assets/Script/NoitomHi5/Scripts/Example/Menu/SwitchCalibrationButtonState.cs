using UnityEngine;
using HI5.VRInteraction;

namespace HI5.VRCalibration
{
    public class SwitchCalibrationButtonState : MonoBehaviour
    {
        [SerializeField] private VRButton m_Button;
        [SerializeField] private CountDownUI m_CountDown;
        [SerializeField] private Sprite[] m_Styles;

        private void OnEnable()
        {
            m_CountDown.OnCountDwonStart += HandleCountDownStart;

            EnableButton(true);
        }

        private void OnDisable()
        {
            m_CountDown.OnCountDwonStart -= HandleCountDownStart;
        }

        private void HandleCountDownStart()
        {
            EnableButton(false);
        }

        private void EnableButton(bool value)
        {
            //ruige red
            //ActiveSelectionRadial(false);
            ActiveSelectionRadial(value);

            GetComponent<Collider>().enabled = value;

            int index = value == false ? 1 : 0;
            SwitchStyle(index);
        }

        private void SwitchStyle(int index)
        {
            if (index > m_Styles.Length - 1)
                return;

            if (m_Styles[index] != null)
                GetComponent<SpriteRenderer>().sprite = m_Styles[index];
        }

        private void ActiveSelectionRadial(bool value)
        {
            if (value)
                m_Button.SelectionRadial.Show();
            else
                m_Button.SelectionRadial.Hide();
        }
    }

}
