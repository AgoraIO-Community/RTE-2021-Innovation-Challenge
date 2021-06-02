using UnityEngine;

namespace indoorNav
{
    public class NavSplashScreenManager : MonoBehaviour
    {
        [Header("RESOURCES")]
        // public GameObject splashScreen;
        public GameObject mainPanels;
        public Animator startFadeIn;
        private Animator mainPanelsAnimator;

        // [Header("SETTINGS")]
        private bool disableSplashScreen = true;
        public bool enableLoginScreen = false;

        void Start()
        {
            mainPanelsAnimator = mainPanels.GetComponent<Animator>();

            if (disableSplashScreen == true)
            {
                mainPanels.SetActive(true);
                mainPanelsAnimator.Play("Panel Start");
            }

            else
            {
                mainPanelsAnimator.Play("Wait");
                startFadeIn.Play("Start with Splash");
            }
        }
    }
}