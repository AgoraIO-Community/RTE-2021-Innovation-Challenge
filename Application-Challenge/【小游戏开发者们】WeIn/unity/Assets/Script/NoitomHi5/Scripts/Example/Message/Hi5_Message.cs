using System.Collections;
using System.Collections.Generic;
using UnityEngine;



public class Hi5_Message
{
    public class Hi5_MessageMessageKey
    {
        public static readonly string messageStateChange = "messageStateChange";
        public static readonly string messageCalibrationResult = "messageCalibrationResult";
       
    }
    private static object _lock = new object();
    private static Hi5_Message instance;

    private Hi5_Message()
    {

    }

    public static Hi5_Message GetInstance()
    {
        if (instance == null)
        {
            lock (_lock)
            {
                if (instance == null)
                {
                    instance = new Hi5_Message();
                }
            }
        }
        return instance;
    }

    public delegate void MessageFun(string messageKey, object param1, object param2);

    public Dictionary<string, MessageFun> dicMessage = new Dictionary<string, MessageFun>();

    //分发事件
    public void DispenseMessage(string messageKey, object param1, object param2 = null)
    {
        if (dicMessage.ContainsKey(messageKey))
        {
            dicMessage[messageKey](messageKey, param1, param2);
        }
    }

    //注册监听
    public void RegisterMessage(MessageFun fun, string messageKey)
    {
        if (!dicMessage.ContainsKey(messageKey))
        {
            dicMessage.Add(messageKey, null);
            dicMessage[messageKey] += fun;
        }
        else
        {
            dicMessage[messageKey] += fun;
        }
    }

    //取消监听
    public void UnRegisterMessage(MessageFun fun, string messageKey)
    {
        if (dicMessage.ContainsKey(messageKey))
        {
            dicMessage[messageKey] -= fun;
        }
    }
}