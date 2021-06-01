using UnityEngine;
using UnityEngine.EventSystems;

namespace indoorNav
{
    public class NavFriendsPanelAnim: MonoBehaviour, IPointerEnterHandler, IPointerExitHandler
    {
        private Animator panelAnimator;
        [Header("SETTINGS")]
        public bool isConsole;

        bool isOn;

        void Start()
        {
            panelAnimator = this.GetComponent<Animator>();
        }

        public void OnPointerEnter(PointerEventData eventData)
        {
            panelAnimator.Play("Friends In");
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            panelAnimator.Play("Friends Out");
        }
    }
}