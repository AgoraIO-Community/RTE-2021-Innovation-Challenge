using System;
using UnityEngine;

namespace agora_rtm
{
    public sealed class AgoraCallbackObject {
        private static string _GameObjectName = "agoraCallbackObject";
        private GameObject _CallbackGameObject {
            get; 
            set;
        }
        
        private static AgoraCallbackObject _AgoraCallbackObject {
            get;
            set;
        }

        public AgoraCallbackQueue _CallbackQueue {
            set;
            get;
        }

        public static AgoraCallbackObject GetInstance() {
            if (_AgoraCallbackObject == null) {
                _AgoraCallbackObject = new AgoraCallbackObject(_GameObjectName);
            }
            return _AgoraCallbackObject;
        }

        public AgoraCallbackObject(string gameObjectName)
        {
            InitGameObject(gameObjectName);
        }

        public void Release()
        {
            if (!ReferenceEquals(_CallbackGameObject, null))
            {
                if (!ReferenceEquals(_CallbackQueue, null))
                {
                    _CallbackQueue.ClearQueue();
                }
                GameObject.Destroy(_CallbackGameObject);
                _CallbackGameObject = null;
                _CallbackQueue = null;
            }
        }

        private void InitGameObject(string gameObjectName)
        {
            DeInitGameObject(gameObjectName);
            _CallbackGameObject = new GameObject(gameObjectName);
            _CallbackQueue = _CallbackGameObject.AddComponent<AgoraCallbackQueue>();
            GameObject.DontDestroyOnLoad(_CallbackGameObject);
            _CallbackGameObject.hideFlags = HideFlags.HideInHierarchy;
        }

        private void DeInitGameObject(string gameObjectName)
        {
            GameObject gameObject = GameObject.Find(gameObjectName);
            if (!ReferenceEquals(gameObject, null))
            {
                AgoraCallbackQueue callbackQueue = gameObject.GetComponent<AgoraCallbackQueue>();
                if (!ReferenceEquals(callbackQueue, null))
                {
                    callbackQueue.ClearQueue();
                }
                GameObject.Destroy(gameObject);
                gameObject = null;
                callbackQueue = null;
            }
        }
    } 
}