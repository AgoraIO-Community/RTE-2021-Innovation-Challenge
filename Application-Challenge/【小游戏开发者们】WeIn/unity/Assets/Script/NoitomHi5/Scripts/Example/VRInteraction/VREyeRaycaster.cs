using System;
using UnityEngine;

namespace HI5.VRInteraction
{
    public class VREyeRaycaster : MonoBehaviour
    {
        public event Action<RaycastHit> OnRaycasthit;                   // This event is called every frame that the user's gaze is over a collider.


        [SerializeField] private Transform m_Camera;
        [SerializeField] private LayerMask m_ExclusionLayers;           // Layers to exclude from the raycast.
        [SerializeField] private Reticle m_Reticle;                     // The reticle, if applicable.

        [SerializeField] private SelectionRadial m_SelectionRadial;

        [SerializeField] private VRInput m_VrInput;                     // Used to call input based events on the current VRInteractiveItem.
        [SerializeField] private bool m_ShowDebugRay;                   // Optionally show the debug ray.
        [SerializeField] private float m_DebugRayLength = 5f;           // Debug ray length.
        [SerializeField] private float m_DebugRayDuration = 1f;         // How long the Debug ray will remain visible.
        [SerializeField] private float m_RayLength = 500f;              // How far into the scene the ray is cast.

        private VRInteractiveItem m_CurrentInteractible;                //The current interactive item
        private VRInteractiveItem m_LastInteractible;                   //The last interactive item

        // Utility for other classes to get the current interactive item
        public VRInteractiveItem CurrentInteractible
        {
            get { return m_CurrentInteractible; }
        }

        private void OnEnable()
        {
            m_VrInput.OnClick += HandleClick;
            m_VrInput.OnDoubleClick += HandleDoubleClick;
            m_VrInput.OnUp += HandleUp;
            m_VrInput.OnDown += HandleDown;
        }


        private void OnDisable ()
        {
            m_VrInput.OnClick -= HandleClick;
            m_VrInput.OnDoubleClick -= HandleDoubleClick;
            m_VrInput.OnUp -= HandleUp;
            m_VrInput.OnDown -= HandleDown;
        }

        private void Update()
        {
            EyeRaycast();
        }
      
        private void EyeRaycast()
        {
            // Show the debug ray if required
            if (m_ShowDebugRay)
            {
                //Debug.DrawRay(m_Camera.position, m_Camera.forward * m_DebugRayLength, Color.blue, m_DebugRayDuration);
                Debug.DrawRay(m_Camera.position, Quaternion.AngleAxis(5f, m_Camera.right) * m_Camera.forward * m_DebugRayLength, Color.blue, m_DebugRayDuration);
            }

            // Create a ray that points forwards from the camera.
            //Ray ray = new Ray(m_Camera.position, m_Camera.forward);
            Ray ray = new Ray(m_Camera.position, Quaternion.AngleAxis(5f, m_Camera.right) * m_Camera.forward);
            RaycastHit hit;
            
            // Do the raycast forweards to see if we hit an interactive item
            if (Physics.Raycast(ray, out hit, m_RayLength, ~m_ExclusionLayers))
            {
                VRInteractiveItem interactible = hit.collider.GetComponent<VRInteractiveItem>(); //attempt to get the VRInteractiveItem on the hit object
                m_CurrentInteractible = interactible;
              
                // If we hit an interactive item and it's not the same as the last interactive item, then call Over
                if (interactible && interactible != m_LastInteractible)
                {
                    //Debug.Log("Physics.Raycast");
                    interactible.Over();

                    if (m_SelectionRadial)
                        m_SelectionRadial.HandleOver();
                }

                // Deactive the last interactive item 
                if (interactible != m_LastInteractible)
                    DeactiveLastInteractible();

                m_LastInteractible = interactible;

                // Something was hit, set at the hit position.
                if (m_Reticle)
                    m_Reticle.SetPosition(hit);

                if (OnRaycasthit != null)
                    OnRaycasthit(hit);
            }
            else
            {
                // Nothing was hit, deactive the last interactive item.
                DeactiveLastInteractible();
                m_CurrentInteractible = null;

                // Position the reticle at default distance.
                if (m_Reticle)
                    m_Reticle.SetPosition();
            }
        }


        private void DeactiveLastInteractible()
        {
            if (m_LastInteractible == null)
                return;

            m_LastInteractible.Out();
            m_LastInteractible = null;

            if (m_SelectionRadial)
            {
                m_SelectionRadial.HandleOut();
            }
        }

        //ruige 2d click button
        private void HandleUp()
        {
            if (m_CurrentInteractible != null)
                m_CurrentInteractible.Up();
            //Debug.Log("HandleUp");
        }

        //ruige 2d click button
        private void HandleDown()
        {
            if (m_CurrentInteractible != null)
                m_CurrentInteractible.Down();
           // Debug.Log("HandleDown");
        }

        //ruige 2d click button
        private void HandleClick()
        {
            if (m_CurrentInteractible != null)
                m_CurrentInteractible.Click();
            //Debug.Log("HandleClick");
        }

        //ruige 2d click button
        private void HandleDoubleClick()
        {
            if (m_CurrentInteractible != null)
                m_CurrentInteractible.DoubleClick();
            //Debug.Log("HandleDoubleClick");
        }
    }
}