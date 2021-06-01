#define Graph_And_Chart_PRO
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using UnityEngine;
using UnityEngine.Events;
using UnityEngine.EventSystems;

namespace ChartAndGraph
{
    /// <summary>
    /// provides functionallity for recieving events for chart items (such as bars and pie slices)
    /// </summary>
    class ChartItemEvents : MonoBehaviour , IPointerEnterHandler, IPointerExitHandler, IPointerDownHandler,IPointerUpHandler, InternalItemEvents
    {
        [Serializable]
        public class Event : UnityEvent<GameObject>
        {

        }

        /// <summary>
        /// occures when the mouse is over the item
        /// </summary>
        [Tooltip("Occures when the mouse is over the item")]
        public Event OnMouseHover = new Event();
        /// <summary>
        /// occurs when the mouse is no longer over the item
        /// </summary>
        [Tooltip("Occurs when the mouse is no longer over the item")]
        public Event OnMouseLeave = new Event();
        /// <summary>
        /// occurs when the user clicks the chart item
        /// </summary>
        [Tooltip("Occurs when the user clicks the chart item")]
        public Event OnSelected = new Event();

        private bool mMouseOver = false;
        private bool mMouseDown = false;
        
        private IInternalUse mParent;
        private object mUserData;

        IInternalUse InternalItemEvents.Parent
        {
            get
            {
                return mParent;
            }

            set
            {
                mParent = value;
            }
        }

        object InternalItemEvents.UserData
        {
            get
            {
                return mUserData;
            }

            set
            {
                mUserData = value;
            }
        }

        void Start()
        {
                      
        }

        void OnMouseEnter()
        {
            
            if (mMouseOver == false)
                OnMouseHover.Invoke(gameObject);
            if (mParent != null)
                mParent.InternalItemHovered(mUserData);
            mMouseOver = true;   
        }

        void OnMouseExit()
        {
            if(mMouseOver == true)
                OnMouseLeave.Invoke(gameObject);
            if (mParent != null)
                mParent.InternalItemLeave(mUserData);
            mMouseOver = false;
           
        }

        void OnMouseDown()
        {
            if (mMouseDown == false)
                OnSelected.Invoke(gameObject);
            if (mParent != null)
                mParent.InternalItemSelected(mUserData);
            mMouseDown = true;            
        }

        void OnMouseUp()
        {
            mMouseDown = false;
        }

        public void OnPointerEnter(PointerEventData eventData)
        {
            OnMouseEnter();
        }

        public void OnPointerExit(PointerEventData eventData)
        {
            OnMouseExit();
        }

        public void OnPointerDown(PointerEventData eventData)
        {
            OnMouseDown();
        }

        public void OnPointerUp(PointerEventData eventData)
        {
            OnMouseUp();
        }
    }
}
