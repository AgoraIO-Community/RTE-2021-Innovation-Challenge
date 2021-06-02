using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;

namespace io.agora.rtm.demo
{
    public class MessageDisplay : MonoBehaviour
    {
#pragma warning disable 0649
        [SerializeField] int maxMessages = 25;
        [SerializeField] GameObject chatPanel, textPrefab;
        [SerializeField] MessageColorStruct MessageColors;
        [SerializeField] List<Message> messageList = new List<Message>();
        [SerializeField] RawImage ImageDisplay;
#pragma warning restore 0649

        private void Awake()
        {
            ImageDisplay.gameObject.SetActive(false);
        }

        public void AddTextToDisplay(string text, Message.MessageType messageType)
        {
            if (messageList.Count >= maxMessages)
            {
                Destroy(messageList[0].textObj.gameObject);
                messageList.Remove(messageList[0]);
            }

            Message newMessage = new Message();
            newMessage.text = text;

            GameObject newText = Instantiate(textPrefab, chatPanel.transform);
            newMessage.textObj = newText.GetComponent<Text>();
            newMessage.textObj.text = newMessage.text;
            newMessage.textObj.color = MessageTypeColor(messageType);
            messageList.Add(newMessage);
        }

        public void AddImageToDisplay(byte [] bytes)
        {
            // Create a texture. Texture size does not matter, since
            // LoadImage will replace with with incoming image size.
            Texture2D texture = new Texture2D(2, 2);
            texture.LoadImage(bytes);
            ImageDisplay.texture = texture;
            ImageDisplay.gameObject.SetActive(true);
	    }

        public void Clear()
        {
            ImageDisplay.texture = null;
            ImageDisplay.gameObject.SetActive(false);
            foreach (Message msg in messageList) {
                Destroy(msg.textObj.gameObject);
	        }
            messageList.Clear();
	    }

        Color MessageTypeColor(Message.MessageType messageType)
        {
            Color color = MessageColors.infoColor;

            switch (messageType)
            {
                case Message.MessageType.PlayerMessage:
                    color = MessageColors.playerColor;
                    break;
                case Message.MessageType.ChannelMessage:
                    color = MessageColors.channelColor;
                    break;
                case Message.MessageType.PeerMessage:
                    color = MessageColors.peerColor;
                    break;
                case Message.MessageType.Error:
                    color = MessageColors.errorColor;
                    break;
            }

            return color;
        }
    }


    [System.Serializable]
    public class Message
    {
        public string text;
        public Text textObj;
        public MessageType messageType;

        public enum MessageType
        {
            Info,
            Error,
            PlayerMessage,
            ChannelMessage,
            PeerMessage
        }
    }

    [System.Serializable]
    public struct MessageColorStruct
    {
        public Color infoColor, errorColor, playerColor, peerColor, channelColor;
    }
}
