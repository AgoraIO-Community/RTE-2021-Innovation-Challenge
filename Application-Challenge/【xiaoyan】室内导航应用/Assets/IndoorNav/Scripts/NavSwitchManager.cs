using UnityEngine;
using UnityEngine.Events;

namespace indoorNav
{
    public class NavSwitchManager: MonoBehaviour
    {
        [Header("SWITCH")]
        public bool isOn;
        public Animator switchAnimator;

        [Header("EVENT")]
        [SerializeField]
        public UnityEvent onEvent;
        public UnityEvent offEvent;

        private string onTransition = "Switch On";
        private string offTransition = "Switch Off";

        void Start()
        {
            if (isOn == true)
            {
                switchAnimator.Play(onTransition);
                onEvent.Invoke();
            }

            else
            {
                switchAnimator.Play(offTransition);
                offEvent.Invoke();
            }
        }

        public void AnimateSwitch()
        {
            if (isOn == true)
            {
                switchAnimator.Play(offTransition);
                offEvent.Invoke();
                isOn = false;
            }

            else
            {
                switchAnimator.Play(onTransition);
                onEvent.Invoke();
                isOn = true;
            }
        }
    }
}