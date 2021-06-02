using System;
using System.Collections;
using UnityEngine;
using UnityEngine.Networking;


namespace agora_utilities
{
    [Serializable]
    public class RTCTokenObject
    {
        public string rtcToken;
    }
    [Serializable]
    public class RTMTokenObject
    {
        public string rtmToken;
    }

    public static class TokenRequestHelper
    {
        public static IEnumerator FetchToken(
            string url, string channel, uint userId = 0, Action<string> callback = null, bool isrtc=true
        )
        {
            UnityWebRequest request;
            if(!isrtc){

                 request = UnityWebRequest.Get(string.Format(
                "{0}/rtm/{1}/", url, channel
                ));
            }
            else{
                Debug.Log(string.Format("{0}/rtc/{1}/publisher/uid/{2}/", 
                url, channel, userId));
                request = UnityWebRequest.Get(string.Format(
                "{0}/rtc/{1}/publisher/uid/{2}/", url, channel, userId
                ));
            }
            yield return request.SendWebRequest();

            if (request.isNetworkError || request.isHttpError)
            {
                Debug.Log(request.error);
                callback(null);
                yield break;
            }
            if(!isrtc){
                RTMTokenObject tokenInfo = JsonUtility.FromJson<RTMTokenObject>(
                request.downloadHandler.text);
                callback(tokenInfo.rtmToken);
            }
            else{
                RTCTokenObject tokenInfo = JsonUtility.FromJson<RTCTokenObject>(
                request.downloadHandler.text);
                callback(tokenInfo.rtcToken);
            }
        }
    }
}
