using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;
using System.Collections.Generic;
using System;
using TMPro;

namespace indoorNav
{
    public class NavInTextDropdown : MonoBehaviour, IPointerClickHandler, IPointerExitHandler
    {
        private Animator panelAnimator;
        private TMP_Dropdown embededDropdown;
        private bool isOpen = false;
        private TMP_InputField textArea;
        public static readonly List<string> list = new List<string>();
        private List<string> list_default = new List<string>();


        void Start()
        {
            list_default.Add("Default");
            panelAnimator = this.GetComponent<Animator>();
            embededDropdown = this.GetComponentInChildren<TMP_Dropdown>();
            textArea = this.GetComponent<TMP_InputField>();
            this.GetComponent<TMP_InputField>().onValueChanged.AddListener((str) =>{
                embededDropdown.options.Clear();
                embededDropdown.AddOptions(list_default);
                if(list.Count > 0){
                    embededDropdown.AddOptions(list);
                }
               if(str.EndsWith("#") || str.EndsWith("@")){
                   embededDropdown.Show();
               } 
            });
            embededDropdown.onValueChanged.AddListener(delegate {
            DropdownValueChanged(embededDropdown);});
        }
        public static void AddOption(string uid){
            list.Add(uid);
        }
        void DropdownValueChanged(TMP_Dropdown change)
        {
            // Debug.Log("value changed" + change.value);
            if(textArea == null){
                Debug.Log(" the textArea is null");
            }
            Debug.Log(change.options[change.value].text);
            if( change.value != 0){
                textArea.text = string.Format(textArea.text + change.options[change.value].text);
            }
            change.value = 0;
            textArea.Select();
            textArea.ActivateInputField();
            
        }

        public void OnPointerClick(PointerEventData eventData)
        {
            // if (isOpen == false)
            // {
            //     panelAnimator.Play("Minimize");
            //     // embededDropdown.Show();
            //     isOpen = true;
            // }
            // else if (isOpen == true)
            // {
            //     panelAnimator.Play("Minimize");
            //     isOpen = false;
            // }
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            // if (panelAnimator.GetCurrentAnimatorStateInfo(0).IsName("Loop"))
            // {
            //     panelAnimator.Play("Minimize");
            //     isOpen = false;
            // }
        }

        public void TriggerExit()
        {
            // if (panelAnimator.GetCurrentAnimatorStateInfo(0).IsName("Loop"))
            // {
            //     panelAnimator.Play("Minimize");
            //     isOpen = false;
            // }
        }

        public void ButtonClick()
        {
            // if (isOpen == false)
            // {
            //     panelAnimator.Play("Expand");
            //     isOpen = true;
            // }
            // else if (isOpen == true)
            // {
            //     panelAnimator.Play("Minimize");
            //     isOpen = false;
            // }
        }
    }
}