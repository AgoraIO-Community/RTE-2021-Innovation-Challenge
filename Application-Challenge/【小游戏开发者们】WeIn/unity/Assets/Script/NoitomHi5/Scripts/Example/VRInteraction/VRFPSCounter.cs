using UnityEngine;
using UnityEngine.UI;

namespace HI5.VRInteraction
{
    public class VRFPSCounter : MonoBehaviour
    {
        private float m_DeltaTime;                      // This is the smoothed out time between frames.
        private Text m_Text;                            // Reference to the component that displays the fps.
        private const float k_SmoothingCoef = 0.1f;     // This is used to smooth out the displayed fps.

        private void Start ()
        {
            m_Text = GetComponent<Text> ();
        }

        private void Update ()
        {
            // This line has the effect of smoothing out delta time.
            m_DeltaTime += (Time.deltaTime - m_DeltaTime) * k_SmoothingCoef;
            
            // The frames per second is the number of frames this frame (one) divided by the time for this frame (delta time).
            float fps = 1.0f / m_DeltaTime;

            // Set the displayed value of the fps to be an integer.
            m_Text.text = Mathf.FloorToInt (fps) + " fps";

            // Turn the fps display on and off using the F key.
            if (Input.GetKeyDown (KeyCode.F))
            {
                m_Text.enabled = !m_Text.enabled;
            }
        }
    }
}