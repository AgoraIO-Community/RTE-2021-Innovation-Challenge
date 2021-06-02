using UnityEngine;
using System.Runtime.InteropServices;
using System;
using System.Collections.Generic;
using AOT;

namespace agora_rtm {
	public sealed class RtmClientEventHandler : IRtmApiNative {
		private int currentIdIndex = 0;
		private static int _id = 0;
		private IntPtr _rtmClientEventHandlerPtr = IntPtr.Zero;
		private static Dictionary<int, RtmClientEventHandler> clientEventHandlerHandlerDic = new Dictionary<int, RtmClientEventHandler>();
		/// <summary>
		/// Occurs when a user logs in the Agora RTM system.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		public delegate void OnLoginSuccessHandler(int id);

		/// <summary>
		/// Occurs when a user fails to log in the Agora RTM system.
		/// </summary>
		/// <param name="id">
		/// the id of your engine
		/// </param>
		/// <param name="errorCode">Error codes related to login.</param>
		public delegate void OnLoginFailureHandler(int id, LOGIN_ERR_CODE errorCode);

		/// <summary>
		/// Reports the result of the renewToken method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="token">Your new token.</param>
		/// <param name="errorCode">The error code. </param>
		public delegate void OnRenewTokenResultHandler(int id, string token, RENEW_TOKEN_ERR_CODE errorCode);
		
		/// <summary>
		/// Occurs when the RTM server detects that the RTM token has exceeded the 24-hour validity period and when the SDK is in the CONNECTION_STATE_RECONNECTING state.
		/// This callback occurs only when the SDK is reconnecting to the server. You will not receive this callback when the SDK is in the CONNECTION_STATE_CONNECTED state.
		/// When receiving this callback, generate a new RTM Token on the server and call the renewToken method to pass the new Token on to the server.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		public delegate void OnTokenExpiredHandler(int id);

		/// <summary>
		/// Occurs when a user logs out of the Agora RTM system.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="errorCode">The error code. </param>
		public delegate void OnLogoutHandler(int id, LOGOUT_ERR_CODE errorCode);

		/// <summary>
		/// Occurs when the connection state changes between the SDK and the Agora RTM system.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="state">The new connection state.</param>
		/// <param name="reason">The reason for the connection state change.</param>
		public delegate void OnConnectionStateChangedHandler(int id, CONNECTION_STATE state, CONNECTION_CHANGE_REASON reason);
		
		/// <summary>
		/// Reports the result of the sendMessageToPeer method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="messageId">The ID of the sent message.</param>
		/// <param name="errorCode">The peer-to-peer message state. </param>
		public delegate void OnSendMessageResultHandler(int id, Int64 messageId, PEER_MESSAGE_ERR_CODE errorCode);
		
		/// <summary>
		/// Occurs when receiving a peer-to-peer message.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="peerId">The ID of the message sender.</param>
		/// <param name="message">The received peer-to-peer message.</param>
		public delegate void OnMessageReceivedFromPeerHandler(int id, string peerId, TextMessage message);
		
		/// <summary>
		/// Occurs when receiving a peer-to-peer image message.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="peerId">The ID of the message sender.</param>
		/// <param name="message">The received peer-to-peer image message.</param>
		public delegate void OnImageMessageReceivedFromPeerHandler(int id, string peerId, ImageMessage message);
		
		/// <summary>
		/// Occurs when receiving a peer-to-peer file message.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="peerId">The ID of the message sender.</param>
		/// <param name="message">The received peer-to-peer file message.</param>
		public delegate void OnFileMessageReceivedFromPeerHandler(int id, string peerId, FileMessage message);
		
		/// <summary>
		/// Reports the progress of an ongoing upload task.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the upload request.</param>
		/// <param name="progress">The progress of the ongoing upload task.</param>
		public delegate void OnMediaUploadingProgressHandler(int id, Int64 requestId, MediaOperationProgress progress);
		
		/// <summary>
		/// Reports the progress of an ongoing download task.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the download request.</param>
		/// <param name="progress">The progress of the ongoing download task. </param>
		public delegate void OnMediaDownloadingProgressHandler(int id, Int64 requestId, MediaOperationProgress progress);
		
		/// <summary>
		/// Reports the result of the createFileMessageByUploading method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the upload request.</param>
		/// <param name="fileMessage">An IFileMessage instance.</param>
		/// <param name="code">Error codes.</param>
		public delegate void OnFileMediaUploadResultHandler(int id, Int64 requestId, FileMessage fileMessage, UPLOAD_MEDIA_ERR_CODE code);
		
		/// <summary>
		/// Reports the result of the createImageMessageByUploading method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the upload request.</param>
		/// <param name="imageMessage">An ImageMessage instance.</param>
		/// <param name="code">Error codes.</param>
		public delegate void OnImageMediaUploadResultHandler(int id, Int64 requestId, ImageMessage imageMessage, UPLOAD_MEDIA_ERR_CODE code);
		
		/// <summary>
		/// Reports the result of the downloadMediaToFile method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the download request.</param>
		/// <param name="code">Error codes.</param>
		public delegate void OnMediaDownloadToFileResultHandler(int id, Int64 requestId, DOWNLOAD_MEDIA_ERR_CODE code);
		
		/// <summary>
		/// Reports the result of the downloadMediaToMemory method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the download request.</param>
		/// <param name="memory">The memory address where the downloaded file or image is stored.</param>
		/// <param name="length">The size of the downloaded file or image.</param>
		/// <param name="code">Error codes.</param>
		public delegate void OnMediaDownloadToMemoryResultHandler(int id, Int64 requestId, byte[] memory, Int64 length, DOWNLOAD_MEDIA_ERR_CODE code);
		
		/// <summary>
		/// Reports the result of the cancelMediaDownload or cancelMediaUpload method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of the cancel request.</param>
		/// <param name="code">Error codes.</param>
		public delegate void OnMediaCancelResultHandler(int id, Int64 requestId, CANCEL_MEDIA_ERR_CODE code);
		
		/// <summary>
		/// Reports the result of the queryPeersOnlineStatus method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="peersStatus">The online status of the peer. </param>
		/// <param name="peerCount">The number of the queried peers.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnQueryPeersOnlineStatusResultHandler(int id, Int64 requestId, PeerOnlineStatus[] peersStatus, int peerCount, QUERY_PEERS_ONLINE_STATUS_ERR errorCode);
		
		/// <summary>
		/// Returns the result of the subscribePeersOnlineStatus or unsubscribePeersOnlineStatus method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnSubscriptionRequestResultHandler(int id, Int64 requestId, PEER_SUBSCRIPTION_STATUS_ERR errorCode);
		
		/// <summary>
		/// Returns the result of the queryPeersBySubscriptionOption method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="peerIds">A user ID array of the specified users, to whom you subscribe.</param>
		/// <param name="peerCount">Count of the peers.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnQueryPeersBySubscriptionOptionResultHandler(int id, Int64 requestId, string[] peerIds, int peerCount, QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the setLocalUserAttributes method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnSetLocalUserAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the addOrUpdateLocalUserAttributes method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnAddOrUpdateLocalUserAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the deleteLocalUserAttributesByKeys method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnDeleteLocalUserAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the clearLocalUserAttributes method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnClearLocalUserAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the getUserAttributes or getUserAttributesByKeys method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="userId">The user ID of the specified user.</param>
		/// <param name="attributes">An array of the returned attributes. See RtmAttribute.</param>
		/// <param name="numberOfAttributes">The total number of the user's attributes</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnGetUserAttributesResultHandler(int id, Int64 requestId, string userId, RtmAttribute[] attributes, int numberOfAttributes, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the setChannelAttributes method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnSetChannelAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the addOrUpdateChannelAttributes method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnAddOrUpdateChannelAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the deleteChannelAttributesByKeys method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnDeleteChannelAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the clearChannelAttributes method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnClearChannelAttributesResultHandler(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the getChannelAttributes or getChannelAttributesByKeys method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="attributes"></param>
		/// <param name="numberOfAttributes">The total number of the attributes.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnGetChannelAttributesResultHandler(int id, Int64 requestId, RtmChannelAttribute[] attributes, int numberOfAttributes, ATTRIBUTE_OPERATION_ERR errorCode);
		
		/// <summary>
		/// Reports the result of the getChannelMemberCount method call.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="requestId">The unique ID of this request.</param>
		/// <param name="channelMemberCounts">An array of the channel member counts.</param>
		/// <param name="channelCount">The total number of the channels.</param>
		/// <param name="errorCode">Error Codes.</param>
		public delegate void OnGetChannelMemberCountResultHandler(int id, Int64 requestId, ChannelMemberCount[] channelMemberCounts , int channelCount, GET_CHANNEL_MEMBER_COUNT_ERR_CODE errorCode);
		
		/// <summary>
		/// Occurs when the online status of the peers, to whom you subscribe, changes.
		/// When the subscription to the online status of specified peers succeeds, the SDK returns this callback to report the online status of peers, to whom you subscribe.
		/// When the online status of the peers, to whom you subscribe, changes, the SDK returns this callback to report whose online status has changed.
		/// If the online status of the peers, to whom you subscribe, changes when the SDK is reconnecting to the server, the SDK returns this callback to report whose online status has changed when successfully reconnecting to the server.
		/// </summary>
		/// <param name="id">the id of your engine</param>
		/// <param name="peersStatus">An array of peers' online states. See PeerOnlineStatus.</param>
		/// <param name="peerCount">Count of the peers, whose online status changes.</param>
		public delegate void OnPeersOnlineStatusChangedHandler(int id, PeerOnlineStatus[] peersStatus, int peerCount);

		public OnLoginSuccessHandler OnLoginSuccess;
		public OnLoginFailureHandler OnLoginFailure;
		public OnRenewTokenResultHandler OnRenewTokenResult;
		public OnTokenExpiredHandler OnTokenExpired;
		public OnLogoutHandler OnLogout;
		public OnConnectionStateChangedHandler OnConnectionStateChanged;
		public OnSendMessageResultHandler OnSendMessageResult;
		public OnMessageReceivedFromPeerHandler OnMessageReceivedFromPeer;
		public OnImageMessageReceivedFromPeerHandler OnImageMessageReceivedFromPeer;
		public OnFileMessageReceivedFromPeerHandler OnFileMessageReceivedFromPeer;
		public OnMediaUploadingProgressHandler OnMediaUploadingProgress;
		public OnMediaDownloadingProgressHandler OnMediaDownloadingProgress;
		public OnFileMediaUploadResultHandler OnFileMediaUploadResult;
		public OnImageMediaUploadResultHandler OnImageMediaUploadResult;
		public OnMediaDownloadToFileResultHandler OnMediaDownloadToFileResult;
		public OnMediaDownloadToMemoryResultHandler OnMediaDownloadToMemoryResult;
		public OnMediaCancelResultHandler OnMediaCancelResult;
		public OnQueryPeersOnlineStatusResultHandler OnQueryPeersOnlineStatusResult;
		public OnSubscriptionRequestResultHandler OnSubscriptionRequestResult;
		public OnQueryPeersBySubscriptionOptionResultHandler OnQueryPeersBySubscriptionOptionResult;
		public OnSetLocalUserAttributesResultHandler OnSetLocalUserAttributesResult;
		public OnAddOrUpdateLocalUserAttributesResultHandler OnAddOrUpdateLocalUserAttributesResult;
		public OnDeleteLocalUserAttributesResultHandler OnDeleteLocalUserAttributesResult;
		public OnClearLocalUserAttributesResultHandler OnClearLocalUserAttributesResult;
		public OnGetUserAttributesResultHandler OnGetUserAttributesResult;
		public OnSetChannelAttributesResultHandler OnSetChannelAttributesResult;
		public OnAddOrUpdateChannelAttributesResultHandler OnAddOrUpdateChannelAttributesResult;
		public OnDeleteChannelAttributesResultHandler OnDeleteChannelAttributesResult;
		public OnClearChannelAttributesResultHandler OnClearChannelAttributesResult;
		public OnGetChannelAttributesResultHandler OnGetChannelAttributesResult;
		public OnGetChannelMemberCountResultHandler OnGetChannelMemberCountResult;
		public OnPeersOnlineStatusChangedHandler OnPeersOnlineStatusChanged;


		public RtmClientEventHandler() {
			currentIdIndex = _id;
			clientEventHandlerHandlerDic.Add(currentIdIndex, this);
			_rtmClientEventHandlerPtr = service_event_handler_createEventHandle(currentIdIndex, OnLoginSuccessCallback,
																				OnLoginFailureCallback,
																				OnRenewTokenResultCallback,
																				OnTokenExpiredCallback,
																				OnLogoutCallback,
																				OnConnectionStateChangedCallback,
																				OnSendMessageResultCallback,
																				OnMessageReceivedFromPeerCallback,
																				OnImageMessageReceivedFromPeerCallback,
																				OnFileMessageReceivedFromPeerCallback,
																				OnMediaUploadingProgressCallback,
																				OnMediaDownloadingProgressCallback,
																				OnFileMediaUploadResultCallback,
																				OnImageMediaUploadResultCallback,
																				OnMediaDownloadToFileResultCallback,
																				OnMediaDownloadToMemoryResultCallback,
																				OnMediaCancelResultCallback,
																				OnQueryPeersOnlineStatusResultCallback,
																				OnSubscriptionRequestResultCallback,
																				OnQueryPeersBySubscriptionOptionResultCallback,
																				OnPeersOnlineStatusChangedCallback,
																				OnSetLocalUserAttributesResultCallback,
																				OnDeleteLocalUserAttributesResultCallback,
																				OnClearLocalUserAttributesResultCallback,
																				OnGetUserAttributesResultCallback,
																				OnSetChannelAttributesResultCallback,
																				OnAddOrUpdateLocalUserAttributesResultCallback,
																				OnDeleteChannelAttributesResultCallback,
																				OnClearChannelAttributesResultCallback,
																				OnGetChannelAttributesResultCallback,
																				OnGetChannelMemberCountResultCallback);
			_id ++;
		}

		public void Release() {
			Debug.Log("RtmClientEventHandler Released");
			if (_rtmClientEventHandlerPtr == IntPtr.Zero) {
				return;
			}
			clientEventHandlerHandlerDic.Remove(currentIdIndex);
			service_event_handler_releaseEventHandler(_rtmClientEventHandlerPtr);
			_rtmClientEventHandlerPtr = IntPtr.Zero;
		}

		public IntPtr GetRtmClientEventHandlerPtr() {
			return _rtmClientEventHandlerPtr;
		}
		
		[MonoPInvokeCallback(typeof(OnLoginSuccessHandler))]
		private static void OnLoginSuccessCallback(int id) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnLoginSuccess != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnLoginSuccess != null) {
							clientEventHandlerHandlerDic[id].OnLoginSuccess(id);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnLoginFailureHandler))]
		private static void OnLoginFailureCallback(int id, LOGIN_ERR_CODE errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnLoginFailure != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnLoginFailure != null) {
							clientEventHandlerHandlerDic[id].OnLoginFailure(id, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnRenewTokenResultHandler))]
		private static void OnRenewTokenResultCallback(int id, string token, RENEW_TOKEN_ERR_CODE errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnRenewTokenResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnRenewTokenResult != null) {
							clientEventHandlerHandlerDic[id].OnRenewTokenResult(id, token, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnTokenExpiredHandler))]
		private static void OnTokenExpiredCallback(int id) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnTokenExpired != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnTokenExpired != null) {
							clientEventHandlerHandlerDic[id].OnTokenExpired(id);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnLogoutHandler))]
		private static void OnLogoutCallback(int id, LOGOUT_ERR_CODE errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnLogout != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnLogout != null) {
							clientEventHandlerHandlerDic[id].OnLogout(id, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnConnectionStateChangedHandler))]
		private static void OnConnectionStateChangedCallback(int id, CONNECTION_STATE state, CONNECTION_CHANGE_REASON reason)
		{
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnConnectionStateChanged != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnConnectionStateChanged != null) {
							clientEventHandlerHandlerDic[id].OnConnectionStateChanged(id, state, reason);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnSendMessageResultHandler))]
		private static void OnSendMessageResultCallback(int id, Int64 messageId, PEER_MESSAGE_ERR_CODE errorCode)
		{
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSendMessageResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSendMessageResult != null) {
							clientEventHandlerHandlerDic[id].OnSendMessageResult(id, messageId, errorCode);
						}
					});
				}
			}
		}
		
		[MonoPInvokeCallback(typeof(EngineEventOnMessageReceived))]
		private static void OnMessageReceivedFromPeerCallback(int id, string peerId, IntPtr message) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMessageReceivedFromPeer != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					TextMessage textMessage = new TextMessage(message, TextMessage.MESSAGE_FLAG.SEND);
					TextMessage _textMessage = new TextMessage(textMessage, TextMessage.MESSAGE_FLAG.RECEIVE);
					textMessage.SetMessagePtr(IntPtr.Zero);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMessageReceivedFromPeer != null) {
							clientEventHandlerHandlerDic[id].OnMessageReceivedFromPeer(id, peerId, _textMessage);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnImageMessageReceived))]
		private static void OnImageMessageReceivedFromPeerCallback(int id, string peerId, IntPtr message) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnImageMessageReceivedFromPeer != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					ImageMessage imageMessage = new ImageMessage(message, ImageMessage.MESSAGE_FLAG.SEND);
					ImageMessage _imageMessage = new ImageMessage(imageMessage, ImageMessage.MESSAGE_FLAG.RECEIVE);
					imageMessage.SetMessagePtr(IntPtr.Zero);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnImageMessageReceivedFromPeer != null) {
							clientEventHandlerHandlerDic[id].OnImageMessageReceivedFromPeer(id, peerId, _imageMessage);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnFileMessageReceived))]
		private static void OnFileMessageReceivedFromPeerCallback(int id, string peerId, IntPtr message) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnFileMessageReceivedFromPeer != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					FileMessage fileMessage = new FileMessage(message, FileMessage.MESSAGE_FLAG.SEND);
					FileMessage _fileMessage = new FileMessage(fileMessage, FileMessage.MESSAGE_FLAG.RECEIVE);
					fileMessage.SetMessagePtr(IntPtr.Zero);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnFileMessageReceivedFromPeer != null) {
							clientEventHandlerHandlerDic[id].OnFileMessageReceivedFromPeer(id, peerId, _fileMessage);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnMediaDownloadToFileResultHandler))]
		private static void OnMediaDownloadToFileResultCallback(int id, Int64 requestId, DOWNLOAD_MEDIA_ERR_CODE code) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaDownloadToFileResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaDownloadToFileResult != null) {
							clientEventHandlerHandlerDic[id].OnMediaDownloadToFileResult(id, requestId, code);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnMediaDownloadToMemoryResult))]
		private static void OnMediaDownloadToMemoryResultCallback(int id, Int64 requestId, IntPtr memory, Int64 length, DOWNLOAD_MEDIA_ERR_CODE code) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaDownloadToMemoryResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					byte[] memoryData = new byte[length];
					Marshal.Copy(memory, memoryData, 0, (int)length);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaDownloadToMemoryResult != null) {
							clientEventHandlerHandlerDic[id].OnMediaDownloadToMemoryResult(id, requestId, memoryData, length, code);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnMediaCancelResultHandler))]
		private static void OnMediaCancelResultCallback(int id, Int64 requestId, CANCEL_MEDIA_ERR_CODE code)
		{
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaCancelResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaCancelResult != null) {
							clientEventHandlerHandlerDic[id].OnMediaCancelResult(id, requestId, code);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnQueryPeersOnlineStatusResult))]
		private static void OnQueryPeersOnlineStatusResultCallback(int id, Int64 requestId, string peersStatus, int peerCount, QUERY_PEERS_ONLINE_STATUS_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnQueryPeersOnlineStatusResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnQueryPeersOnlineStatusResult != null) {
							int j = 1;
							string[] sArray = peersStatus.Split('\t');
							PeerOnlineStatus [] channelAttributes = new PeerOnlineStatus[peerCount];
							for (int i = 0; i < peerCount; i++) {
								PeerOnlineStatus peerOnlineStatus = new PeerOnlineStatus();
								// word 1 = user id string
								peerOnlineStatus.peerId = sArray[j++];
								// word 2 = online/offline (1 or 0)
								peerOnlineStatus.isOnline = 1 == int.Parse(sArray[j++]);
								// word 3 = PEER_ONLINE_STATE mapped from int to enum
								peerOnlineStatus.onlineState = (PEER_ONLINE_STATE)int.Parse(sArray[j++]);
								channelAttributes[i] = peerOnlineStatus;
							}
							clientEventHandlerHandlerDic[id].OnQueryPeersOnlineStatusResult(id, requestId, channelAttributes, peerCount, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnSubscriptionRequestResultHandler))]
		private static void OnSubscriptionRequestResultCallback(int id, Int64 requestId, PEER_SUBSCRIPTION_STATUS_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSubscriptionRequestResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSubscriptionRequestResult != null) {
							clientEventHandlerHandlerDic[id].OnSubscriptionRequestResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnSetLocalUserAttributesResultHandler))]
		private static void OnSetLocalUserAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSetLocalUserAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSetLocalUserAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnSetLocalUserAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnAddOrUpdateLocalUserAttributesResultHandler))]
		private static void OnAddOrUpdateLocalUserAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnAddOrUpdateLocalUserAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnAddOrUpdateLocalUserAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnAddOrUpdateLocalUserAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnDeleteLocalUserAttributesResultHandler))]
		private static void OnDeleteLocalUserAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnDeleteLocalUserAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnDeleteLocalUserAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnDeleteLocalUserAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnClearLocalUserAttributesResultHandler))]
		private static void OnClearLocalUserAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnClearLocalUserAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnClearLocalUserAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnClearLocalUserAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnSetChannelAttributesResultHandler))]
		private static void OnSetChannelAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSetChannelAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnSetChannelAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnSetChannelAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnAddOrUpdateChannelAttributesResultHandler))]
		private static void OnAddOrUpdateChannelAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnAddOrUpdateChannelAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnAddOrUpdateChannelAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnAddOrUpdateChannelAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnDeleteChannelAttributesResultHandler))]
		private static void OnDeleteChannelAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnDeleteChannelAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnDeleteChannelAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnDeleteChannelAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnClearChannelAttributesResultHandler))]
		private static void OnClearChannelAttributesResultCallback(int id, Int64 requestId, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnClearChannelAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnClearChannelAttributesResult != null) {
							clientEventHandlerHandlerDic[id].OnClearChannelAttributesResult(id, requestId, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnFileMediaUploadResult))]
		private static void OnFileMediaUploadResultCallback(int id, Int64 requestId, IntPtr fileMessagePtr, UPLOAD_MEDIA_ERR_CODE code) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnFileMediaUploadResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
				 	FileMessage fileMessage = new FileMessage(fileMessagePtr, FileMessage.MESSAGE_FLAG.SEND);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnFileMediaUploadResult != null) {
							clientEventHandlerHandlerDic[id].OnFileMediaUploadResult(id, requestId, fileMessage, code);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnImageMediaUploadResult))]
		private static void OnImageMediaUploadResultCallback(int id, Int64 requestId, IntPtr imageMessagePtr, UPLOAD_MEDIA_ERR_CODE code) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnImageMediaUploadResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					Debug.Log("OnImageUploadResutl  result = " + code);
				 	ImageMessage imageMessage = new ImageMessage(imageMessagePtr, ImageMessage.MESSAGE_FLAG.SEND);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnImageMediaUploadResult != null) {
							clientEventHandlerHandlerDic[id].OnImageMediaUploadResult(id, requestId, imageMessage, code);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnMediaUploadingProgress))]
		private static void OnMediaUploadingProgressCallback(int id, Int64 requestId, Int64 totalSize, Int64 currentSize) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaUploadingProgress != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaUploadingProgress != null) {
							MediaOperationProgress mediaOperationProgress = new MediaOperationProgress();
							mediaOperationProgress.totalSize = totalSize;
							mediaOperationProgress.currentSize = currentSize;
							clientEventHandlerHandlerDic[id].OnMediaUploadingProgress(id, requestId, mediaOperationProgress);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(OnMediaDownloadingProgressHandler))]
		private static void OnMediaDownloadingProgressCallback(int id, Int64 requestId, Int64 totalSize, Int64 currentSize) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaDownloadingProgress != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						MediaOperationProgress mediaOperationProgress = new MediaOperationProgress();
						mediaOperationProgress.totalSize = totalSize;
						mediaOperationProgress.currentSize = currentSize;
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnMediaDownloadingProgress != null) {
							clientEventHandlerHandlerDic[id].OnMediaDownloadingProgress(id, requestId, mediaOperationProgress);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnGetUserAttributesResultHandler))]
		private static void OnGetUserAttributesResultCallback(int id, Int64 requestId, string userId, string attributes, int numberOfAttributes, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnGetUserAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnGetUserAttributesResult != null) {
							int j = 1;
							string[] sArray = attributes.Split('\t');
							RtmAttribute [] attribute = new RtmAttribute[numberOfAttributes];
							for (int i = 0; i < numberOfAttributes; i++) {
								attribute[i].key = sArray[j++];
								attribute[i].value = sArray[j++];
							}
							clientEventHandlerHandlerDic[id].OnGetUserAttributesResult(id, requestId, userId, attribute, numberOfAttributes, errorCode);										
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnGetChannelAttributesResult))]
		private static void OnGetChannelAttributesResultCallback(int id, Int64 requestId, string attributes, int numberOfAttributes, ATTRIBUTE_OPERATION_ERR errorCode) {
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnGetChannelAttributesResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnGetChannelAttributesResult != null) {
							int j = 1;
							string[] sArray = attributes.Split('\t');
							RtmChannelAttribute [] channelAttributes = new RtmChannelAttribute[numberOfAttributes];
							for (int i = 0; i < numberOfAttributes; i++) {
								RtmChannelAttribute _attribute = new RtmChannelAttribute(MESSAGE_FLAG.RECEIVE);
								_attribute.SetKey(sArray[j++]);
								_attribute.SetValue(sArray[j++]);
								_attribute.SetLastUpdateTs(Int64.Parse(sArray[j++]));
								_attribute.SetLastUpdateUserId(sArray[j++]);	
								channelAttributes[i] = _attribute;
							}
							clientEventHandlerHandlerDic[id].OnGetChannelAttributesResult(id, requestId, channelAttributes, numberOfAttributes, errorCode);
						}
					});
				}
			}
		}

		[MonoPInvokeCallback(typeof(EngineEventOnGetChannelMemberCountResult))]
		private static void OnGetChannelMemberCountResultCallback(int id, Int64 requestId, string channelMemberCounts , int channelCount, GET_CHANNEL_MEMBER_COUNT_ERR_CODE errorCode)
		{
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnGetChannelMemberCountResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnGetChannelMemberCountResult != null) {
							int j = 1;
							string[] sArray = channelMemberCounts.Split('\t');
							ChannelMemberCount [] channelAttributes = new ChannelMemberCount[channelCount];
							for (int i = 0; i < channelCount; i++) {
								ChannelMemberCount channelMemeber = new ChannelMemberCount();
								channelMemeber.channelId = sArray[j++];
								channelMemeber.count = int.Parse(sArray[j++]);	
								channelAttributes[i] = channelMemeber;
							}
							clientEventHandlerHandlerDic[id].OnGetChannelMemberCountResult(id, requestId, channelAttributes, channelCount, errorCode);
						}
					});
				}
			}	
		}

		[MonoPInvokeCallback(typeof(EngineEventOnPeersOnlineStatusChanged))]
		private static void OnPeersOnlineStatusChangedCallback(int id, string peersStatusStr, int peerCount)
		{
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnPeersOnlineStatusChanged != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnPeersOnlineStatusChanged != null) {
							int j = 1;
							string[] sArray = peersStatusStr.Split('\t');
							PeerOnlineStatus [] channelAttributes = new PeerOnlineStatus[peerCount];
							for (int i = 0; i < peerCount; i++) {
								PeerOnlineStatus peerOnlineStatus = new PeerOnlineStatus();
								peerOnlineStatus.peerId = sArray[j++];
								peerOnlineStatus.isOnline = bool.Parse(sArray[j++]);	
								peerOnlineStatus.onlineState = (PEER_ONLINE_STATE)int.Parse(sArray[j++]);
								channelAttributes[i] = peerOnlineStatus;
							}
							clientEventHandlerHandlerDic[id].OnPeersOnlineStatusChanged(id, channelAttributes, peerCount);
						}
					});
				}
			}	
		}

		[MonoPInvokeCallback(typeof(OnQueryPeersBySubscriptionOptionResultHandler))]
		private static void OnQueryPeersBySubscriptionOptionResultCallback(int id, Int64 requestId, string[] peerIds, int peerCount, QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR errorCode)
		{
			if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnQueryPeersBySubscriptionOptionResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (clientEventHandlerHandlerDic.ContainsKey(id) && clientEventHandlerHandlerDic[id].OnQueryPeersBySubscriptionOptionResult != null) {
							clientEventHandlerHandlerDic[id].OnQueryPeersBySubscriptionOptionResult(id, requestId, peerIds, peerCount, errorCode);
						}
					});
				}
			}	
		}
	}
}
