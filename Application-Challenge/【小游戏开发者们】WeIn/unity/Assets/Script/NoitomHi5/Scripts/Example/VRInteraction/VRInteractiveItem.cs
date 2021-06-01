using System;
using UnityEngine;

namespace HI5.VRInteraction
{
    public class VRInteractiveItem : MonoBehaviour
    {
        public event Action OnOver;             // Called when the gaze moves over this object
        public event Action OnOut;              // Called when the gaze leaves this object

        //ruige 2d used 
        public event Action OnClick;            // Called when click input is detected whilst the gaze is over this object.
        public event Action OnDoubleClick;      // Called when double click input is detected whilst the gaze is over this object.
        public event Action OnUp;               // Called when Fire1 is released whilst the gaze is over this object.
        public event Action OnDown;             // Called when Fire1 is pressed whilst the gaze is over this object.

        protected bool m_IsOver;

        public bool IsOver
        {
            get { return m_IsOver; }              // Is the gaze currently over this object?
        }

        // The below functions are called by the VREyeRaycaster when the appropriate input is detected.
        // They in turn call the appropriate events should they have subscribers.

        //ruige use EyeRaycast occurrent ray hit
        public void Over()
        {
            m_IsOver = true;

            if (OnOver != null)
                OnOver();
        }

        //ruige use EyeRaycast occurrent ray outhit DeactiveLastInteractible
        public void Out()
        {
            m_IsOver = false;

            if (OnOut != null)
                OnOut();
        }

        //ruige 2d click button
        public void Click()
        {
            if (OnClick != null)
                OnClick();
        }

        //ruige 2d click button
        public void DoubleClick()
        {
            if (OnDoubleClick != null)
                OnDoubleClick();
        }

        //ruige 2d click button
        public void Up()
        {
            if (OnUp != null)
                OnUp();
        }

        //ruige 2d click button
        public void Down()
        {
            if (OnDown != null)
                OnDown();
        }
    }
}