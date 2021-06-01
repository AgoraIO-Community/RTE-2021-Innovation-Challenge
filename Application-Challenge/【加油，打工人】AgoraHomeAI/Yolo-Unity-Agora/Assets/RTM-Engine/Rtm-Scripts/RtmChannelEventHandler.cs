using UnityEngine;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System;
using AOT;



namespace agora_rtm {
    public sealed class RtmChannelEventHandler : IRtmApiNative { 
        private static int _id = 0;
        private static Dictionary<int, RtmChannelEventHandler> channelEventHandlerDic = new Dictionary<int, RtmChannelEventHandler>();
        private IntPtr channelEventHandlerPtr = IntPtr.Zero;
        private int currentIdIndex = 0;

		/// <summary>
		/// Occurs when successfully joining a channel.
		/// When the local user calls the \ref agora_rtm.RtmChannel.Join "Join" method and successfully joins the channel:
		/// - The SDK triggers this callback;
		/// - All remote users in the channel receive the \ref agora_rtm.RtmChannelEventHandler.OnMemberJoinedHandler "OnMemberJoinedHandler" callback.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
        public delegate void OnJoinSuccessHandler(int id);

		/// <summary>
		/// Occurs when failing to join a channel.
		/// The local user receives this callback when the \ref agora_rtm.RtmChannel.Join "Join" method call fails.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="errorCode">The error code. See #JOIN_CHANNEL_ERR.</param>
        public delegate void OnJoinFailureHandler(int id, JOIN_CHANNEL_ERR errorCode);
        
		/// <summary>
		/// Returns the result of the leave method call.
		/// </summary>
		/// <param name="id">the ID of the RtmChannelEventHandler</param>
		/// <param name="errorCode">The error code. See #LEAVE_CHANNEL_ERR. </param>
		public delegate void OnLeaveHandler(int id, LEAVE_CHANNEL_ERR errorCode);
        
		/// <summary>
		/// Occurs when receiving a channel message.
		/// </summary>
		/// <param name="id">the ID of the RtmChannelEventHandler</param>
		/// <param name="userId">the ID of the message sender.</param>
		/// <param name="message">The received channel message. See \ref agora_rtm.IMessage "IMessage".</param>
		public delegate void OnMessageReceivedHandler(int id, string userId, TextMessage message);
        
		/// <summary>
		/// Occurs when receiving a channel image message.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="userId">the ID of the message sender.</param>
		/// <param name="message">The received channel image message. See \ref agora_rtm.ImageMessage "ImageMessage".</param>
		public delegate void OnImageMessageReceivedHandler(int id, string userId, ImageMessage message);
        
		/// <summary>
		/// Occurs when receiving a channel file message.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="userId">The the ID of the message sender.</param>
		/// <param name="message">The received channel file message. See \ref agora_rtm.FileMessage "FileMessage".</param>
		public delegate void OnFileMessageReceivedHandler(int id, string userId, FileMessage message);
        
		/// <summary>
		/// Returns the result of the \ref agora_rtm.RtmChannel.SendMessage "SendMessage" method call.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="messageId">the ID of the sent channel message.</param>
		/// <param name="errorCode">The error codes. See #CHANNEL_MESSAGE_ERR_CODE.</param>
		public delegate void OnSendMessageResultHandler(int id, Int64 messageId, CHANNEL_MESSAGE_ERR_CODE errorCode);
        
		/// <summary>
		/// Occurs when a remote user joins the channel.
		/// When a remote user calls the \ref agora_rtm.RtmChannel.Join	"Join" method and receives the #OnJoinSuccessHandler callback (successfully joins the channel), the local user receives this callback.
		/// @note This callback is disabled when the number of the channel members exceeds 512.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="member">The user joining the channel. See ChannelMemberCount.</param>
		public delegate void OnMemberJoinedHandler(int id, RtmChannelMember member);
        
		/// <summary>
		/// Occurs when a remote member leaves the channel.
		/// When a remote member in the channel calls the \ref agora_rtm.RtmChannel.Leave "Leave" method and receives the the #OnLeaveHandler (LEAVE_CHANNEL_ERR_OK) callback, the local user receives this callback.
		/// @note This callback is disabled when the number of the channel members exceeds 512.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="member">The channel member that leaves the channel. See \ref agora_rtm.RtmChannelMember "ChannelMember".</param>
		public delegate void OnMemberLeftHandler(int id, RtmChannelMember member);
        
		/// <summary>
		/// Returns the result of the \ref agora_rtm.RtmChannel.GetMembers "GetMembers" method call.
		/// When the method call succeeds, the SDK returns the member list of the channel.
		/// @note 
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="members">The member list. See \ref agora_rtm.RtmChannel "RtmChannel".</param>
		/// <param name="userCount">The number of members.</param>
		/// <param name="errorCode">Error code. See #GET_MEMBERS_ERR.</param>
		public delegate void OnGetMembersHandler(int id, RtmChannelMember[] members, int userCount, GET_MEMBERS_ERR errorCode);
        
		/// <summary>
		/// Occurs when channel attributes are updated, and returns all attributes of the channel.
		/// @note This callback is enabled only when the user, who updates the attributes of the channel, sets \ref agora_rtm.ChannelAttributeOptions.enableNotificationToChannelMembers "enableNotificationToChannelMembers" as `true`. Also note that this flag is valid only within the current channel attribute method call.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="attributesList">All attribute of this channel.</param>
		/// <param name="numberOfAttributes">The total number of the channel attributes.</param>
		public delegate void OnAttributesUpdatedHandler(int id, RtmChannelAttribute[] attributesList, int numberOfAttributes);
        
		/// <summary>
		/// Occurs when the number of the channel members changes, and returns the new number.
		/// @note
		/// - When the number of channel members ≤ 512, the SDK returns this callback when the number changes at the frequency of once per second.
		/// - When the number of channel members exceeds 512, the SDK returns this callback when the number changes at the frequency of once every three seconds.
		/// - You will receive this callback when successfully joining an RTM channel, so Agora recommends implementing this callback to receive timely updates on the number of the channel members.
		/// </summary>
		/// <param name="id">the ID of the #RtmChannelEventHandler</param>
		/// <param name="memberCount">Member count of this channel.</param>
		public delegate void OnMemberCountUpdatedHandler(int id, int memberCount);

        public OnJoinSuccessHandler OnJoinSuccess;
        public OnJoinFailureHandler OnJoinFailure;
        public OnLeaveHandler OnLeave;
        public OnMessageReceivedHandler OnMessageReceived;
        public OnImageMessageReceivedHandler OnImageMessageReceived;
        public OnFileMessageReceivedHandler OnFileMessageReceived;
        public OnSendMessageResultHandler OnSendMessageResult;
        public OnMemberJoinedHandler OnMemberJoined;
        public OnMemberLeftHandler OnMemberLeft;
        public OnAttributesUpdatedHandler OnAttributesUpdated;
        public OnMemberCountUpdatedHandler OnMemberCountUpdated;
        public OnGetMembersHandler OnGetMembers;

        public RtmChannelEventHandler() {
            currentIdIndex = _id;
			channelEventHandlerDic.Add(currentIdIndex, this);
            channelEventHandlerPtr = channel_event_handler_createEventHandler(currentIdIndex, OnJoinSuccessCallback,
                                                                                              OnJoinFailureCallback,
                                                                                              OnLeaveCallback,
                                                                                              OnMessageReceivedCallback,
                                                                                              OnImageMessageReceivedCallback,
                                                                                              OnFileMessageReceivedCallback,
                                                                                              OnSendMessageResultCallback,
                                                                                              OnMemberJoinedCallback,
                                                                                              OnMemberLeftCallback,
                                                                                              OnGetMemberCallback,
                                                                                              OnMemberCountUpdatedCallback,
                                                                                              OnAttributesUpdatedCallback);
            _id ++;
        }

        public IntPtr GetChannelEventHandlerPtr() {
            return channelEventHandlerPtr;
        }

        [MonoPInvokeCallback(typeof(OnLeaveHandler))]
        private static void OnLeaveCallback(int id, LEAVE_CHANNEL_ERR errorCode) {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnLeave != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnLeave != null) {
							channelEventHandlerDic[id].OnLeave(id, errorCode);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(OnJoinFailureHandler))]
        private static void OnJoinFailureCallback(int id, JOIN_CHANNEL_ERR errorCode) {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnJoinFailure != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnJoinFailure != null) {
							channelEventHandlerDic[id].OnJoinFailure(id, errorCode);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(OnJoinSuccessHandler))]
        private static void OnJoinSuccessCallback(int id) {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnJoinSuccess != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnJoinSuccess != null) {
							channelEventHandlerDic[id].OnJoinSuccess(id);
						}
					});
				}
			}
        }
        
        [MonoPInvokeCallback(typeof(EngineEventOnMessageReceived))]
        private static void OnMessageReceivedCallback(int id, string userId, IntPtr messagePtr)
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMessageReceived != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
                    TextMessage textMessage = new TextMessage(messagePtr, TextMessage.MESSAGE_FLAG.SEND);
					TextMessage _textMessage = new TextMessage(textMessage, TextMessage.MESSAGE_FLAG.RECEIVE);
					textMessage.SetMessagePtr(IntPtr.Zero);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMessageReceived != null) {
							channelEventHandlerDic[id].OnMessageReceived(id, userId, _textMessage);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(EngineEventOnImageMessageReceived))]
        private static void OnImageMessageReceivedCallback(int id, string userId, IntPtr messagePtr) 
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnImageMessageReceived != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
                    ImageMessage imageMessage = new ImageMessage(messagePtr, ImageMessage.MESSAGE_FLAG.SEND);
					ImageMessage _imageMessage = new ImageMessage(imageMessage, ImageMessage.MESSAGE_FLAG.RECEIVE);
					imageMessage.SetMessagePtr(IntPtr.Zero);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnImageMessageReceived != null) {
							channelEventHandlerDic[id].OnImageMessageReceived(id, userId, _imageMessage);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(EngineEventOnFileMessageReceived))]
        private static void OnFileMessageReceivedCallback(int id, string userId, IntPtr messagePtr)
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnFileMessageReceived != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
                    FileMessage fileMessage = new FileMessage(messagePtr, FileMessage.MESSAGE_FLAG.SEND);
					FileMessage _fileMessage = new FileMessage(fileMessage, FileMessage.MESSAGE_FLAG.RECEIVE);
					fileMessage.SetMessagePtr(IntPtr.Zero);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnFileMessageReceived != null) {
							channelEventHandlerDic[id].OnFileMessageReceived(id, userId, _fileMessage);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(OnSendMessageResultHandler))]
        private static void OnSendMessageResultCallback(int id, Int64 messageId, CHANNEL_MESSAGE_ERR_CODE errorCode)
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnSendMessageResult != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnSendMessageResult != null) {
							channelEventHandlerDic[id].OnSendMessageResult(id, messageId, errorCode);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(EngineEventOnMemberJoined))]
        private static void OnMemberJoinedCallback(int id, IntPtr channelMemberPtr)
        {
            Debug.Log("OnMemberJoinedCallback");
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMemberJoined != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
                    string userId = Marshal.PtrToStringAnsi(channel_member_getUserId(channelMemberPtr));
                    string channelId = Marshal.PtrToStringAnsi(channel_member_getChannelId(channelMemberPtr));
                    RtmChannelMember rtmChannelMember = new RtmChannelMember(userId, channelId);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMemberJoined != null) {
							channelEventHandlerDic[id].OnMemberJoined(id, rtmChannelMember);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(EngineEventOnMemberLeft))]
        private static void OnMemberLeftCallback(int id, IntPtr channelMemberPtr)
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMemberLeft != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
                    string userId = Marshal.PtrToStringAnsi(channel_member_getUserId(channelMemberPtr));
                    string channelId = Marshal.PtrToStringAnsi(channel_member_getChannelId(channelMemberPtr));
                    RtmChannelMember rtmChannelMember = new RtmChannelMember(userId, channelId);
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMemberLeft != null) {
							channelEventHandlerDic[id].OnMemberLeft(id, rtmChannelMember);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(EngineEventOnAttributesUpdated))]
        private static void OnAttributesUpdatedCallback(int id, string attributesListPtr, int numberOfAttributes)
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnAttributesUpdated != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnAttributesUpdated != null) {
							int j = 1;
							string[] sArray = attributesListPtr.Split('\t');
							RtmChannelAttribute [] channelAttributes = new RtmChannelAttribute[numberOfAttributes];
							for (int i = 0; i < numberOfAttributes; i++) {
								RtmChannelAttribute _attribute = new RtmChannelAttribute(MESSAGE_FLAG.RECEIVE);
								_attribute.SetKey(sArray[j++]);
								_attribute.SetValue(sArray[j++]);
								_attribute.SetLastUpdateTs(Int64.Parse(sArray[j++]));
								_attribute.SetLastUpdateUserId(sArray[j++]);	
								channelAttributes[i] = _attribute;
							}
							channelEventHandlerDic[id].OnAttributesUpdated(id, channelAttributes, numberOfAttributes);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(OnMemberCountUpdatedHandler))]
        private static void OnMemberCountUpdatedCallback(int id, int memberCount)
        {
			if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMemberCountUpdated != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnMemberCountUpdated != null) {
							channelEventHandlerDic[id].OnMemberCountUpdated(id, memberCount);
						}
					});
				}
			}
        }

        [MonoPInvokeCallback(typeof(EngineEventOnGetMember))]
        private static void OnGetMemberCallback(int id, string membersStr, int userCount, GET_MEMBERS_ERR errorCode)
        {
		    if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnGetMembers != null) {
				if (AgoraCallbackObject.GetInstance()._CallbackQueue != null) {
					AgoraCallbackObject.GetInstance()._CallbackQueue.EnQueue(()=>{
						if (channelEventHandlerDic.ContainsKey(id) && channelEventHandlerDic[id].OnGetMembers != null) {
							int j = 1;
							string[] sArray = membersStr.Split('\t');
							RtmChannelMember [] membersList = new RtmChannelMember[userCount];
							for (int i = 0; i < userCount; i++) {
								RtmChannelMember member = new RtmChannelMember(sArray[j++], sArray[j++]);
								membersList[i] = member;
							}
							channelEventHandlerDic[id].OnGetMembers(id, membersList, userCount, errorCode);
						}
					});
				}
			}
        }

        public void Release() {
			Debug.Log("RtmChannelEventHandler Released");
            if (channelEventHandlerPtr == IntPtr.Zero) {
                return;
            }
            channelEventHandlerDic.Remove(currentIdIndex);
            channel_event_handler_releaseEventHandler(channelEventHandlerPtr);
            channelEventHandlerPtr = IntPtr.Zero;
		}
	}
}