using System.Collections.Generic;
using UnityEngine;
using UnityEngine.UI;
using TMPro;
// using SWS;

namespace indoorNav
{
    public class NavMessageDisplay : MonoBehaviour
    {
#pragma warning disable 0649
        [SerializeField] int maxMessages = 25;
        [SerializeField] GameObject chatPanel, textPrefab;
        [SerializeField] GameObject entryLPrefab, entryRPrefab;
        [SerializeField] GameObject rtcManager;
        // [SerializeField] NavRtcManager wpManager;
        [SerializeField] MessageColorStruct MessageColors;
        [SerializeField] List<Message> messageList = new List<Message>();
        [SerializeField] RawImage ImageDisplay;

        NavPathBehavior pathBehavior;

#pragma warning restore 0649
        private void Start()
        {
            pathBehavior = rtcManager.GetComponent<NavPathBehavior>();
            // ImageDisplay.gameObject.SetActive(false);
        }
        public void AppendList(string nickname, float powerlevel, Vector3 location, string msg, bool isme=false){
            Transform parent = chatPanel.GetComponent<Transform>();
            GameObject element;
            uint uid;
            uint.TryParse(nickname, out uid);
            if(isme){
                element = (GameObject)Instantiate(entryRPrefab);
            }
            else{
                element = (GameObject)Instantiate(entryLPrefab);
            }
            element.GetComponentInChildren<TextMeshProUGUI>().SetText(nickname);
            Slider powerContainer = element.GetComponentInChildren<Slider>();
            TextMeshProUGUI msgContainer = element.transform.Find("Content").GetComponentInChildren<TextMeshProUGUI>();
            Button navButton = element.GetComponentInChildren<Button>();

            if(powerContainer != null){
                powerContainer.value = powerlevel;
            }
            else{
                Debug.LogError("powerContainer is null obj");
            }

            if(msgContainer != null){
                msgContainer.SetText(msg);
            }
            else{
                Debug.LogError("msgContainer is null obj");
            }

            if(navButton !=null){

                navButton.onClick.AddListener(()=>{
                    // uint uid = 0;
                    pathBehavior.Letsgo(uid);
                });
            }
            else{
                Debug.LogError("sendButton is null obj");
            }

            // there will be toggle to turn on the video;
            element.transform.SetParent(parent);
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
