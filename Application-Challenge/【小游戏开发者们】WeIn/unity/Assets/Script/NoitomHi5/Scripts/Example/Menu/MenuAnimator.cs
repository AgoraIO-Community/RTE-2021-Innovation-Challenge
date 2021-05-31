using System.Collections;
using UnityEngine;

namespace HI5.VRCalibration
{
    public class MenuAnimator : MonoBehaviour
    {
        [SerializeField] private int m_FrameRate = 30;                  // The number of times per second the image should change.
        [SerializeField] private SpriteRenderer m_SpriteRenderer;            
        [SerializeField] private Sprite[] m_AnimSprites;

        private WaitForSeconds m_FrameRateWait;                         // The delay between frames.
        private int m_CurrentTextureIndex;                              // The index of the textures array.
        private bool m_Playing = true;                                         // Whether the textures are currently being looped through.
        private float m_Pause = 0f;

        private void Awake ()
        {
            // The delay between frames is the number of seconds (one) divided by the number of frames that should play during those seconds (frame rate).
            m_FrameRateWait = new WaitForSeconds (1f / m_FrameRate);
        }

        private void OnEnable ()
        {
            StartCoroutine(PlaySprites());
        }

        private void OnDisable ()
        {
            StopCoroutine(PlaySprites());
        }

        private IEnumerator PlaySprites()
        {
            // So long as the textures should be playing...
            while (m_Playing)
            {
                if (m_SpriteRenderer != null)
                {
                    // Set the texture of the mesh renderer to the texture indicated by the index of the textures array.
                    m_SpriteRenderer.sprite = m_AnimSprites[m_CurrentTextureIndex];

                    // Then increment the texture index (looping once it reaches the length of the textures array.
                    m_CurrentTextureIndex = (m_CurrentTextureIndex + 1) % m_AnimSprites.Length;
                }

                if (m_CurrentTextureIndex == 0)
                    yield return new WaitForSeconds(m_Pause);

                // Wait for the next frame.
                yield return m_FrameRateWait;
            }
        }
    }
}