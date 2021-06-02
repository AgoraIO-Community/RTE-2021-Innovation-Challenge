using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;


namespace agora_rtm {
	public sealed class LocalInvitation : IRtmApiNative, IDisposable {
		private IntPtr _localInvitationPtr = IntPtr.Zero;
		private bool _disposed = false;
		private bool _needDispose = true;
		public LocalInvitation(IntPtr localInvitationPtr, bool needRelease = true) {
			_localInvitationPtr = localInvitationPtr;
			_needDispose = needRelease;
		}

		~LocalInvitation() {
			if (_needDispose) {
				Dispose(false);
			}
		}
        
		/// <summary>
		/// Allows the caller to get the User ID of the callee.
		/// </summary>
		/// <returns></returns>
		public string GetCalleeId() {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_remote_call_manager_getCallerId(_localInvitationPtr);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}
		}

		/// <summary>
		/// Allows the caller to set the call invitation content.
		/// </summary>
		/// <param name="content">The content of the call invitation. The content must not exceed 8 KB in length if encoded in UTF-8.</param>
		public void SetContent(string content) {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return;
			}
			i_local_call_invitation_setContent(_localInvitationPtr, content);
		}

		/// <summary>
		/// Allows the caller to get the call invitation content.
		/// </summary>
		/// <returns></returns>
		public string GetContent() {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_local_call_invitation_getContent(_localInvitationPtr);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}	
		}

		/// <summary>
		/// Sets the channel ID.
		/// </summary>
		/// <param name="channelId">The channel ID to be set.</param>
		public void SetChannelId(string channelId) {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return;
			}
			i_local_call_invitation_setChannelId(_localInvitationPtr, channelId);
		}

		/// <summary>
		/// Gets the channel ID.
		/// </summary>
		/// <returns></returns>
		public string GetChannelId() {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_local_call_invitation_getChannelId(_localInvitationPtr);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}
		}

		/// <summary>
		/// Allows the caller to get the callee's response to the call invitation.
		/// </summary>
		/// <returns></returns>
		public string GetResponse() {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_local_call_invitation_getResponse(_localInvitationPtr);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}	
		}

		public IntPtr GetPtr() {
			return _localInvitationPtr;
		}

		/// <summary>
		/// Allows the caller to get the state of the outgoing call invitation.
		/// </summary>
		/// <returns>State of the outgoing call invitation. </returns>
		public LOCAL_INVITATION_STATE GetState() {
			if (_localInvitationPtr == IntPtr.Zero)
			{
				Debug.LogError("_localInvitationPtr is null");
				return LOCAL_INVITATION_STATE.LOCAL_INVITATION_STATE_FAILURE;
			}
			return (LOCAL_INVITATION_STATE)i_local_call_invitation_getState(_localInvitationPtr);
		}

		private void Release() {
			if (_localInvitationPtr == IntPtr.Zero) {
				Debug.LogError("_localInvitationPtr is null");
				return;
			}
			i_local_call_invitation_release(_localInvitationPtr);
			_localInvitationPtr = IntPtr.Zero;
		}

		public void Dispose() {
			Dispose(true);
			GC.SuppressFinalize(this);
		}

		private void Dispose(bool disposing) {
			if (_needDispose) {
				if (_disposed) return;
				if (disposing) {}
				Release();
			} else {
				_localInvitationPtr = IntPtr.Zero;
			}
			_disposed = true;
			Debug.Log("~LocalInvitation Dispose");
		}
	}
}