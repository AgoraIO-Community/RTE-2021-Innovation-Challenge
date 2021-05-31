using System;
using UnityEngine;

namespace HI5.VRInteraction
{
    // This script is for switching the calibration states on Calibration UI
    // Each 'button' will be a rendering showing the scene
    // that will be loaded and use the SelectionRadial.

    public class VRButton : MonoBehaviour
    {
        //public event Action<VRButton> OnButtonSelected;                   // This event is triggered when the selection of the button has finished.

        [SerializeField] protected SelectionRadial m_SelectionRadial;         // This controls when the selection is complete.
        [SerializeField] protected VRInteractiveItem m_InteractiveItem;       // The interactive item for where the user should click to load the level.

        public SelectionRadial SelectionRadial
        {
            get { return m_SelectionRadial; }
        }

        protected bool m_GazeOver;

        protected void OnEnable ()
        {
            m_InteractiveItem.OnOver += HandleOver;
            m_InteractiveItem.OnOut += HandleOut;

            if (GetComponent<Collider>())
                GetComponent<Collider>().enabled = true;

            m_GazeOver = false;
        }

        protected void OnDisable ()
        {
            m_InteractiveItem.OnOver -= HandleOver;
            m_InteractiveItem.OnOut -= HandleOut;
            
        }

        protected void HandleOver()
        {
            // When the user looks at the rendering of the scene, show the radial.
            m_SelectionRadial.Show();

            m_GazeOver = true;
        }

        protected void HandleOut()
        {
            // When the user looks away from the rendering of the scene, hide the radial.
            m_SelectionRadial.Hide();

            m_GazeOver = false;
        }
    }
}