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
		/// @note You can join a maximum of 20 RTM channels at the same time. When the number of the channels you join exceeds the limit, you receive the #JOIN_CHANNEL_ERR error code.
		/// - If this method call succeeds:
	    ///   - The local user receives the \ref agora_rtm.RtmChannelEventHandler.OnJoinSuccessHandler "OnJoinSuccessHandler" callback.
		///   - All remote users receive the \ref agora_rtm.RtmChannelEventHandler.OnMemberJoinedHandler "OnMemberJoinedHandler" callback.
		/// - If this method call fails, the local user receives the \ref agora_rtm.RtmChannelEventHandler.OnJoinFailureHandler "OnJoinFailureHandler" callback. See #JOIN_CHANNEL_ERR for the error codes.
		/// </summary>
		/// <returns>
		///  - 0: Success.
		///  - ≠0: Failure. See #JOIN_CHANNEL_ERR for the error codes.
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
		/// - If this method call succeeds:
		///   - The local user receives the \ref agora_rtm.RtmChannelEventHandler.OnLeaveHandler "OnLeaveHandler" callback with the LEAVE_CHANNEL_ERR_OK state.
		///   - All remote users receive the \ref agora_rtm.RtmChannelEventHandler.OnMemberLeftHandler "OnMemberLeftHandler" callback.
		/// - If this method call fails, the local user receives the \ref agora_rtm.RtmChannelEventHandler.OnLeaveHandler "OnLeaveHandler" callback with an error code. See #LEAVE_CHANNEL_ERR for the error codes.
		/// </summary>
		/// <returns>
		///  - 0: Success.
		///  - ≠0: Failure. See #LEAVE_CHANNEL_ERR for the error codes.
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
		/// Agora does not recommend using this method to send a channel message. Use #SendMessage instead.
		/// If this method call succeeds:
		/// - The \ref agora_rtm.RtmClientEventHandler.OnSendMessageResultHandler(int id, Int64 messageId, PEER_MESSAGE_ERR_CODE errorCode ) "OnSendMessageResultHandler" callback returns the result.
		/// - All remote users in the channel receive the \ref agora_rtm.RtmChannelEventHandler.OnMessageReceivedHandler "OnMessageReceivedHandler" callback.
		/// </summary>
		/// <param name="message">The message to be sent. See \ref agora_rtm.IMessage "IMessage".</param>
		/// <returns>
		///  - 0: Success.
		///  - ≠0: Failure. See #CHANNEL_MESSAGE_ERR_CODE for the error codes.
		/// </returns>
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
		/// - The \ref agora_rtm.RtmClientEventHandler.OnSendMessageResultHandler(int id, Int64 messageId, PEER_MESSAGE_ERR_CODE errorCode ) "OnSendMessageResultHandler" callback returns the result.
		/// - All remote users in the channel receive the \ref agora_rtm.RtmChannelEventHandler.OnMessageReceivedHandler "OnMessageReceivedHandler" callback.
		/// @note You can send messages, including peer-to-peer and channel messages, at a maximum frequency of 180 calls every three seconds.
		/// </summary>
		/// <param name="message">The message to be sent. See \ref agora_rtm.IMessage "IMessage".</param>
		/// <param name="options">Options when sending the channel message. See \ref agora_rtm.SendMessageOptions "SendMessageOptions".</param>
		/// <returns>
		///  - 0: Success.
		///  - ≠0: Failure. See #CHANNEL_MESSAGE_ERR_CODE for the error codes.
		/// </returns>
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
		/// The \ref agora_rtm.RtmChannelEventHandler.OnGetMembersHandler "OnGetMembersHandler" callback returns the result of this method call.
		/// @note You can call this method at a maximum frequency of five calls every two seconds. This method returns a maximum of 512 members. If the number of channel members exceed 512, the method returns 512 members randomly.
		/// </summary>
		/// <returns>
		///  - 0: Success.
		///  - ≠0: Failure. See #GET_MEMBERS_ERR for the error codes.
		/// </returns>
		public int GetMembers() {
			if (_rtmChannelPtr == IntPtr.Zero)
			{
				Debug.LogError("_rtmChannelPtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
			return channel_getMembers(_rtmChannelPtr);
		}

 		/// <summary>
		/// Releases all resources used by the #RtmChannel instance.
		/// </summary>
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
		/// Releases all resources used by the #RtmChannel instance.
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
