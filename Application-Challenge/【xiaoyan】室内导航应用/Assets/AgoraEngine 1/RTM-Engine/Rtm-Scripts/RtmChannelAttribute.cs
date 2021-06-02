using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;

namespace agora_rtm {
	public sealed class RtmChannelAttribute : IRtmApiNative, IDisposable {

		private IntPtr _channelAttributePtr = IntPtr.Zero;
		private MESSAGE_FLAG _flag = MESSAGE_FLAG.SEND;
		private bool _disposed = false;
		private string _key {
			get;
			set;
		}

		private string _value {
			get;
			set;
		}

		private Int64 _lastUpdateTs {
			get;
			set;
		}

		private string _lastUpdateUserId {
			get;
			set;
		}

		public RtmChannelAttribute(MESSAGE_FLAG flag) {
			_flag = flag;
		}

		public RtmChannelAttribute(IntPtr channelAttributePtr) {
			_channelAttributePtr = channelAttributePtr;
		}

		public RtmChannelAttribute(RtmChannelAttribute channelAttribute, MESSAGE_FLAG flag) {
			_flag = flag;
			_key = channelAttribute.GetKey();
			_value = channelAttribute.GetValue();
			_lastUpdateTs = channelAttribute.GetLastUpdateTs();
			_lastUpdateUserId = channelAttribute.GetLastUpdateUserId();
		}

		~RtmChannelAttribute() {
			Debug.Log("~RtmChannelAttribute");
			Dispose(false);
		}

		/// <summary>
		/// Sets the key of the channel attribute.
		/// </summary>
		/// <param name="key">Key of channel attribute. Must be visible characters and not exceed 32 Bytes.</param>
		public void SetKey(string key) {
			if (_flag == MESSAGE_FLAG.RECEIVE) {
				_key = key;
				return;
			}

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return;
			}
			channelAttribute_setKey(_channelAttributePtr, key);
		}

		/// <summary>
		/// Gets the key of the channel attribute.
		/// </summary>
		/// <returns>Key of the channel attribute.</returns>
		public string GetKey() {
			if (_flag == MESSAGE_FLAG.RECEIVE) {
				return _key;
			}

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr keyPtr = channelAttribute_getKey(_channelAttributePtr);
            if (!ReferenceEquals(keyPtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(keyPtr);
			} else {
				return "";
			}
		}

		/// <summary>
		/// Sets the value of the channel attribute.
		/// </summary>
		/// <param name="value">Value of the channel attribute. Must not exceed 8 KB in length.</param>
		public void SetValue(string value) {
			if (_flag == MESSAGE_FLAG.RECEIVE) {
				_value = value;
				return;
			}

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return;
			}
			channelAttribute_setValue(_channelAttributePtr, value);
		}

		/// <summary>
		/// Gets the value of the channel attribute.
		/// </summary>
		/// <returns>Value of the channel attribute.</returns>
		public string GetValue() {
			if (_flag == MESSAGE_FLAG.RECEIVE) {
				return _value;
			}

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = channelAttribute_getValue(_channelAttributePtr);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}
		}

		public void SetLastUpdateUserId(string lastUpdateUserId) {
			_lastUpdateUserId = lastUpdateUserId;
		}

		/// <summary>
		/// Gets the User ID of the user who makes the latest update to the channel attribute.
		/// </summary>
		/// <returns>User ID of the user who makes the latest update to the channel attribute.</returns>
		public string GetLastUpdateUserId() {
			if (_flag == MESSAGE_FLAG.RECEIVE) {
				return _lastUpdateUserId;
			}

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr userIdPtr = channelAttribute_getLastUpdateUserId(_channelAttributePtr);
            if (!ReferenceEquals(userIdPtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(userIdPtr);
			} else {
				return "";
			}
		}

		public void SetLastUpdateTs(Int64 ts) {
			_lastUpdateTs = ts;
		}

		/// <summary>
		/// Gets the timestamp of when the channel attribute was last updated.
		/// </summary>
		/// <returns>Timestamp of when the channel attribute was last updated in milliseconds.</returns>
		public Int64 GetLastUpdateTs() {
			if (_flag == MESSAGE_FLAG.RECEIVE) {
				return _lastUpdateTs;
			}

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			Int64 ts = channelAttribute_getLastUpdateTs(_channelAttributePtr);
			return ts;
		}

		public IntPtr GetPtr() {
			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return IntPtr.Zero;
			}
			return _channelAttributePtr;
		}

		/// <summary>
		/// Release all resources used by the IRtmChannelAttribute instance.
		/// </summary>
		private void Release() {
			if (_flag == MESSAGE_FLAG.RECEIVE)
				return;

			if (_channelAttributePtr == IntPtr.Zero)
			{
				Debug.LogError("_channelAttributePtr is null");
				return;
			}
			channelAttribute_release(_channelAttributePtr);
			_channelAttributePtr = IntPtr.Zero;
		}

		public void Dispose() {
			Dispose(true);
			GC.SuppressFinalize(this);
		}

		public void Dispose(bool disposing) {
			if (_disposed) return;
			if (disposing) {}
			Release();
			_disposed = true;
		}
	}
}
