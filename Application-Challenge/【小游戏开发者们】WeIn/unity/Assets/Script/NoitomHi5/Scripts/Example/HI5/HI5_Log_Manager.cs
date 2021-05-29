using System.Collections;
using System.Collections.Generic;
using UnityEngine;
namespace HI5
{
    public class HI5_Log_Manager : MonoBehaviour
    {
        public bool isVisibleLog = false; 
        public void SetLogVisible(bool isVisible)
        {
            Hi5_Log.IsVisibleLog = isVisible;
        }

        public void Update()
        {
            SetLogVisible(isVisibleLog);
        }
    }
}
