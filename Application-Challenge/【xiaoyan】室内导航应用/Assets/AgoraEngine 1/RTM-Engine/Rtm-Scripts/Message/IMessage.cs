using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;

namespace agora_rtm {
    public abstract class IMessage : IRtmApiNative {

		protected MESSAGE_FLAG _MessageFlag = MESSAGE_FLAG.RECEIVE;

		protected IntPtr _MessagePtr = IntPtr.Zero;

		protected Int64 _MessageId = 0;

		protected MESSAGE_TYPE _MessageType = MESSAGE_TYPE.MESSAGE_TYPE_UNDEFINED;

		protected string _MessageText = "";

		protected bool _IsOfflineMessage = false;

		protected Int64 _Ts = 0;

		protected byte[] _RawMessageData = null;

		public int _Length = 0;

		public Int64 GetMessageId() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE) 
				return _MessageId;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null ptr");
				return _MessageId;
			}
			return imessage_getMessageId(_MessagePtr);
		}

		/// <summary>
		/// Retrieves the message type.
		/// </summary>
		/// <returns>The message type.</returns>
		public MESSAGE_TYPE GetMessageType() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _MessageType;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null ptr");
				return _MessageType;
			}
			return (MESSAGE_TYPE)imessage_getMessageType(_MessagePtr);
		}

		/// <summary>
		/// Sets the content of the text message or the text description of the raw message.
		/// </summary>
		/// <param name="text">The content of the text message or the text description of the raw message.</param>
		public void SetText(string text) {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
			{
				_MessageText = text;
				return;
			}

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return;
			}
			imessage_setText(_MessagePtr, text);
		}

		/// <summary>
		/// Retrieves the content of the text message or the text description of the raw message.
		/// </summary>
		/// <returns>The content of the text message or the text description of the raw message.</returns>
		public string GetText() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _MessageText;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return _MessageText;
			}
			IntPtr textPtr = imessage_getText(_MessagePtr);
			if (!ReferenceEquals(textPtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(imessage_getText(_MessagePtr));
			} else {
				return "";
			}
		}

		/// <summary>
		/// Retrieves the payload of the raw message.
		/// </summary>
		/// <returns>The payload of the raw message.</returns>
		public byte[] GetRawMessageData() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _RawMessageData;

			if (_MessagePtr == IntPtr.Zero)
			{
				return _RawMessageData;
			}
			_RawMessageData = new byte[GetRawMessageLength()];
			IntPtr _RawMessagePtr = imessage_getRawMessageData(_MessagePtr);
            Marshal.Copy(_RawMessagePtr, _RawMessageData, 0, GetRawMessageLength());
			return _RawMessageData;
		}

		/// <summary>
		///  Get the length of the raw message.
		/// </summary>
		/// <returns>The length of the raw message</returns>
		public int GetRawMessageLength() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _Length;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return _Length;
			}
			return imessage_getRawMessageLength(_MessagePtr);
		}

		/// <summary>
		/// Allows the receiver to retrieve the timestamp of when the messaging server receives this message.
		/// </summary>
		/// <returns>The timestamp (ms) of when the messaging server receives this message.</returns>
		public Int64 GetServerReceiveTs() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _Ts;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return imessage_getServerReceivedTs(_MessagePtr);
		}

		/// <summary>
		/// Allows the receiver to check whether this message has been cached on the server (Applies to peer-to-peer message only).
		/// This method returns false if a message is not cached by the server. Only if the sender sends the message as an offline message (sets enableOfflineMessaging as true) when the specified user is offline, does the method return true when the user is back online.
		/// For now we only cache 200 offline messages for up to seven days for each receiver. When the number of the cached messages reaches this limit, the newest message overrides the oldest one.
		/// </summary>
		/// <returns>
		/// true: This message has been cached on the server (the server caches this message and resends it to the receiver when he/she is back online).
		/// false: This message has not been cached on the server.
		/// </returns>
		public bool IsOfflineMessage() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return _IsOfflineMessage;

			if (_MessagePtr == IntPtr.Zero)
			{
				Debug.LogError("_MessagePtr is null");
				return false;
			}
			return imessage_isOfflineMessage(_MessagePtr);
		}

		protected void Release() {
			if (_MessageFlag == MESSAGE_FLAG.RECEIVE)
				return;

			if (_MessagePtr == IntPtr.Zero)
				return;

			imessage_release(_MessagePtr);
			_MessagePtr = IntPtr.Zero;
		}

		public IntPtr GetPtr() {
			return _MessagePtr;
		}

		public void SetMessagePtr (IntPtr messagePtr) {
			_MessagePtr = messagePtr;
		}
    }
}