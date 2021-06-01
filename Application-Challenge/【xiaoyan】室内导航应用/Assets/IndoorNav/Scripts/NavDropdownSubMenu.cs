using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

namespace indoorNav
{
    public class NavDropdownSubMenu: MonoBehaviour, IPointerClickHandler, IPointerExitHandler
    {
        private Animator panelAnimator;
        private bool isOpen = false;

        void Start()
        {
            panelAnimator = this.GetComponent<Animator>();
        }

        public void OnPointerClick(PointerEventData eventData)
        {
            if (isOpen == false)
            {
                panelAnimator.Play("Expand");
                isOpen = true;
            }
            else if (isOpen == true)
            {
                panelAnimator.Play("Minimize");
                isOpen = false;
            }
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            if (panelAnimator.GetCurrentAnimatorStateInfo(0).IsName("Loop"))
            {
                panelAnimator.Play("Minimize");
                isOpen = false;
            }
        }

        public void TriggerExit()
        {
            if (panelAnimator.GetCurrentAnimatorStateInfo(0).IsName("Loop"))
            {
                panelAnimator.Play("Minimize");
                isOpen = false;
            }
        }

        public void ButtonClick()
        {
            if (isOpen == false)
            {
                panelAnimator.Play("Expand");
                isOpen = true;
            }
            else if (isOpen == true)
            {
                panelAnimator.Play("Minimize");
                isOpen = false;
            }
        }
    }
}