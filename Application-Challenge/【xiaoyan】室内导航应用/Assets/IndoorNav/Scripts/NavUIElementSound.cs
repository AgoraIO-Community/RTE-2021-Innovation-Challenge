using UnityEngine;
using UnityEngine.UI;
using UnityEngine.EventSystems;

namespace indoorNav
{
    public class NavUIElementSound : MonoBehaviour, IPointerClickHandler, IPointerEnterHandler
    {
        [Header("RESOURCES")]
        public AudioClip hoverSound;
        public AudioClip clickSound;
        public AudioClip notificationSound;

        [Header("SETTINGS")]
        public bool enableHoverSound = true;
        public bool enableClickSound = true;
        public bool isNotification = false;

        private AudioSource HoverSource { get { return GetComponent<AudioSource>(); } }
        private AudioSource ClickSource { get { return GetComponent<AudioSource>(); } }
        private AudioSource NotificationSource { get { return GetComponent<AudioSource>(); } }

        void Start()
        {
            gameObject.AddComponent<AudioSource>();
            HoverSource.clip = hoverSound;
            ClickSource.clip = clickSound;

            HoverSource.playOnAwake = false;
            ClickSource.playOnAwake = false;
        }

        public void OnPointerEnter(PointerEventData eventData)
        {
            if (enableHoverSound == true)
            {
                HoverSource.PlayOneShot(hoverSound);
            }
        }

        public void OnPointerClick(PointerEventData eventData)
        {
            if (enableClickSound == true)
            {
                ClickSource.PlayOneShot(clickSound);
            }
        }

        public void Notification()
        {
            if (isNotification == true)
            {
                NotificationSource.PlayOneShot(notificationSound);
            }
        }
    }
}