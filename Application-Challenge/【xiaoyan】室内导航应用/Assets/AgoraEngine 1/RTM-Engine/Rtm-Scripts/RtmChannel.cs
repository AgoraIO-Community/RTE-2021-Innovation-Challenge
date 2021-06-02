using UnityEngine;
using System.Runtime.InteropServices;
using System;
using AOT;

namespace agora_rtm {
	public sealed class RtmChannel :  IRtmApiNative, IDisposable{
		private IntPtr _rtmChannelPtr = IntPtr.Zero;
		private bool _disposed = false;
		private RtmChannelEventHandler _channelEventHandler;
		
		public RtmChannel(IntPtr rtmChannelPtr, RtmChannelEventHandler rtmChannelEventHandler) {
			_rtmChannelPtr = rtmChannelPtr;
			_channelEventHandler = rtmChannelEventHandler;
		}

		~RtmChannel() {
			Dispose(false);
		}

		/// <summary>
		/// Joins a channel.
		/// You can join a maximum of 20 RTM channels at the same time. When the number of the channels you join exceeds the limit, you receive the JOIN_CHANNEL_ERR_FAILURE error code.
		/// If this method call succeeds:
		/// The local user receives the onJoinSuccess callback.
		/// All remote users receive the onMemberJoined callback.
		/// If this method call fails, the local user receives the onJoinFailure callback. See JOIN_CHANNEL_ERR for the error codes.
		/// </summary>
		/// <returns>
		/// 0: Success.
		/// ≠0: Failure. 
		/// </returns>
		public int Join() {
			if (_rtmChannelPtr == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_join(_rtmChannelPtr);
		}

		/// <summary>
		/// Leaves a channel.
		/// If this method call succeeds:
		/// The local user receives the onLeave callback with the LEAVE_CHANNEL_ERR_OK state.
		/// All remote users receive the onMemberLeft callback.
		/// If this method call fails, the local user receives the onLeave callback with an error code. See LEAVE_CHANNEL_ERR for the error codes.
		/// </summary>
		/// <returns>
		/// 0: Success.
		/// ≠0: Failure.
		/// </returns>
		public int Leave() {
			if (_rtmChannelPtr == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_leave(_rtmChannelPtr);
		}

		/// <summary>
		/// Agora does not recommend using this method to send a channel message. Use sendMessage instead.
		/// If this method call succeeds:
		/// The onSendMessageResult callback returns the result.
		/// All remote users in the channel receive the onMessageReceived callback.
		/// </summary>
		/// <param name="message">The message to be sent.</param>
		/// <returns></returns>
		public int SendMessage(IMessage message) {
			if (_rtmChannelPtr == IntPtr.Zero || message.GetPtr() == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_sendMessage(_rtmChannelPtr, message.GetPtr());
		}

		/// <summary>
		/// Allows a channel member to send a message to all members in the channel.
		/// If this method call succeeds:
		/// The onSendMessageResult callback returns the result.
		/// All remote users in the channel receive the onMessageReceived callback.
		/// </summary>
		/// <param name="message">The message to be sent.</param>
		/// <param name="options">Options when sending the channel message.</param>
		/// <returns></returns>
		public int SendMessage(IMessage message, SendMessageOptions options)
		{
			if (_rtmChannelPtr == IntPtr.Zero || message.GetPtr() == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_sendMessage2(_rtmChannelPtr, message.GetPtr(), options.enableOfflineMessaging, options.enableHistoricalMessaging);
		}

		/// <summary>
		/// Retrieves the channel ID.
		/// </summary>
		/// <returns>The channel ID of the channel.</returns>
		public int GetId() {
			if (_rtmChannelPtr == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_getId(_rtmChannelPtr);
		}

		/// <summary>
		/// Retrieves a member list of the channel.
		/// The onGetMembers callback returns the result of this method call.
		/// </summary>
		/// <returns>
		/// 0: Success.
		/// ≠0: Failure. 
		/// </returns>
		public int GetMembers() {
			if (_rtmChannelPtr == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_getMembers(_rtmChannelPtr);
		}

 		public void Dispose() {
			 Dispose(true);
			 GC.SuppressFinalize(this);
		}

		private void Dispose(bool disposing) {
			if (_disposed) return;
			if (disposing){}
			Release();
			_disposed = true;
			Debug.Log("RtmChannel Dispose");
		}

		/// <summary>
		/// Releases all resources used by the RtmChannel instance.
		/// </summary>
		private void Release() {
			if (_rtmChannelPtr == IntPtr.Zero)
			{
				return;
			}
			channel_release(_rtmChannelPtr);
			_rtmChannelPtr = IntPtr.Zero;
			if (_channelEventHandler != null) {
				_channelEventHandler.Release();
			}
		}
	}
}
