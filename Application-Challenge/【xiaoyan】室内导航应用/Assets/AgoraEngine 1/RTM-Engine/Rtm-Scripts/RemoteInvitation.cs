using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;

namespace agora_rtm {
	public sealed class RemoteInvitation : IRtmApiNative, IDisposable {
		private IntPtr _remoteInvitationPrt = IntPtr.Zero;
		private bool _needDispose = true;
		private bool _disposed = false;
		public RemoteInvitation(IntPtr remoteInvitationPtr, bool needDispose = true) {
			_remoteInvitationPrt = remoteInvitationPtr;
			_needDispose = needDispose;
		}

		~RemoteInvitation() {
			if (_needDispose) {
				Dispose(false);
			}
		}

		/// <summary>
		/// Allows the callee to get the User ID of the caller.
		/// </summary>
		/// <returns>
		/// CallId
		/// </returns>
		public string GetCallerId() {
			if (_remoteInvitationPrt == IntPtr.Zero)
			{
				Debug.LogError("_remoteInvitationPrt is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_remote_call_manager_getCallerId(_remoteInvitationPrt);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}	
		}

		/// <summary>
		/// Allows the callee to get the call invitation content set by the caller.
		/// </summary>
		/// <returns>Return the content.</returns>
		public string GetContent() {
			if (_remoteInvitationPrt == IntPtr.Zero)
			{
				Debug.LogError("_remoteInvitationPrt is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_remote_call_manager_getContent(_remoteInvitationPrt);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}	
		}

		/// <summary>
		/// Allows the callee to set a response to the call invitation.
		/// </summary>
		/// <param name="response">The callee's response to the call invitation. The response must not exceed 8 KB in length if encoded in UTF-8.</param>
		public void SetResponse(string response) {
			if (_remoteInvitationPrt == IntPtr.Zero)
			{
				Debug.LogError("_remoteInvitationPrt is null");
				return;
			}
			i_remote_call_manager_setResponse(_remoteInvitationPrt, response);	
		}

		/// <summary>
		/// Allows the callee to get his/her response to the incoming call invitation.
		/// </summary>
		/// <returns></returns>
		public string GetResponse() {
			if (_remoteInvitationPrt == IntPtr.Zero)
			{
				Debug.LogError("_remoteInvitationPrt is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_remote_call_manager_getResponse(_remoteInvitationPrt);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}		
		}

		/// <summary>
		/// Gets the channel ID.
		/// </summary>
		/// <returns></returns>
		public string GetChannelId() {
			if (_remoteInvitationPrt == IntPtr.Zero)
			{
				Debug.LogError("_remoteInvitationPrt is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR + "";
			}
			IntPtr valuePtr = i_remote_call_manager_getChannelId(_remoteInvitationPrt);
            if (!ReferenceEquals(valuePtr, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(valuePtr);
			} else {
				return "";
			}		
		}

		/// <summary>
		/// Allows the callee to get the state of the incoming call invitation.
		/// </summary>
		/// <returns>The state of the incoming call invitation See: REMOTE_INVITATION_STATE.</returns>
		public REMOTE_INVITATION_STATE GetState() {
			if (_remoteInvitationPrt == IntPtr.Zero)
			{
				Debug.LogError("_remoteInvitationPrt is null");
				return REMOTE_INVITATION_STATE.REMOTE_INVITATION_STATE_FAILURE;
			}
			return (REMOTE_INVITATION_STATE)i_remote_call_manager_getState(_remoteInvitationPrt);		
		}

		public IntPtr GetPtr() {
			return _remoteInvitationPrt;
		}

		/// <summary>
		/// Releases all resources used by the IRemoteCallInvitation instance.
		/// </summary>
		private void Release() {
			if (_remoteInvitationPrt == IntPtr.Zero)
				return;

			i_remote_call_manager_release(_remoteInvitationPrt);
			_remoteInvitationPrt = IntPtr.Zero;
		}

		/// <summary>
		/// Release unmanaged resources.
		/// </summary>
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
				_remoteInvitationPrt = IntPtr.Zero;
			}
			_disposed = true;
		}
	}
}
