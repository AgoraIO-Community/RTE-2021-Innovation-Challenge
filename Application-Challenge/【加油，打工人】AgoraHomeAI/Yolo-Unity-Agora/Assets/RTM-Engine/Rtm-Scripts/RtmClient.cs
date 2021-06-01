using UnityEngine;
using System.Runtime.InteropServices;
using System.Collections.Generic;
using System.IO;
using System;
using AOT;

namespace agora_rtm {
    public sealed class RtmClient : IRtmApiNative, IDisposable {
        private Dictionary<string, RtmChannel> channelDic = new Dictionary<string, RtmChannel>();
        private List<RtmChannelAttribute> attributeList = new List<RtmChannelAttribute>();
        private IntPtr _rtmServicePtr = IntPtr.Zero;
        private RtmClientEventHandler _eventHandler;
        private RtmCallManager _rtmCallManager;
        private bool _disposed = false;
        /// <summary>
        /// create an #RtmClient instance.
        /// - The Agora RTM SDK supports creating multiple RtmClient instances.
        /// - All methods in the RtmClient class are executed asynchronously.
        /// </summary>
        /// <param name="appId">
        /// The App ID issued to you from the Agora Console. Apply for a new App ID from Agora if it is missing from your kit.
        /// </param>
        /// <param name="eventHandler">
        /// An RtmClientListener object that reports to the app on RTM SDK runtime events.
        /// </param>
        public RtmClient(string appId, RtmClientEventHandler eventHandler) {
            if (appId == null || eventHandler == null) {
                Debug.LogError("appId or eventHandler is null");
                return;
            }
            AgoraCallbackObject.GetInstance();
            _rtmServicePtr = createRtmService_rtm();
            _eventHandler = eventHandler;
            int ret = initialize_rtm(_rtmServicePtr, appId, eventHandler.GetRtmClientEventHandlerPtr());
            if (ret != 0) {
                Debug.LogError("RtmClient create fail, error:  " + ret);
            }
        }

        ~RtmClient() {
            Dispose(false);
        }

        /// <summary>
        /// Releases all resources used by the RtmClient instance.
        /// Note: Do not call this method in any of your callbacks.
        /// </summary>
        /// <param name="sync">
        /// wheather release rtm client async.
        /// </param>
        private void Release(bool sync) {
            Debug.Log("RtmClient Release");
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return;
			}

            foreach(KeyValuePair<string, RtmChannel>item in channelDic) {
                RtmChannel channel = item.Value;
                if (channel != null) {
                    channel.Dispose();
                }
            }
            channelDic.Clear();
            channelDic = null;

            foreach(RtmChannelAttribute item in attributeList) {
                if (item != null) {
                    item.Dispose();
                }
            }
            attributeList.Clear();
            attributeList = null;

            if (_rtmCallManager != null) {
                _rtmCallManager.Dispose();
                _rtmCallManager = null;
            }

            release_rtm(_rtmServicePtr, sync);
            _rtmServicePtr = IntPtr.Zero;
            _eventHandler.Release();
            _eventHandler = null;
        }

        /// <summary>
        /// Gets the SDK version.
        /// </summary>
        /// <returns>
        /// The current version of the Agora RTM SDK in the string format. For example, 1.0.0.
        /// </returns>
        public static string GetSdkVersion() {
            IntPtr sdkVersion = _getRtmSdkVersion_rtm();
            if (!ReferenceEquals(sdkVersion, IntPtr.Zero)) {
				return Marshal.PtrToStringAnsi(sdkVersion);
			} else {
				return "";
			}
        }

        /// <summary>
        /// Logs in the Agora RTM system.
        /// - If this method call succeeds, the local user receives the \ref agora_rtm.RtmClientEventHandler.OnLoginSuccessHandler "OnLoginSuccessHandler" callback.
        /// - If this method call fails, the local user receives the \ref agora_rtm.RtmClientEventHandler.OnLoginFailureHandler "OnLoginFailureHandler" callback. See #LOGIN_ERR_CODE for the error codes.
        /// @note
        ///   - If you login with the same user ID from a different instance, your previous login will be kicked and you will be removed from the previously joined channels.
        ///   - The call frequency limit for this method is 2 queries per second.
        ///   - Only after you successfully call this method and when the SDK is in the <CONNECTION_STATE_CONNECTED> state, can you call the key RTM methods except:
        ///     - #CreateChannel
        ///     - #CreateMessage
        ///     - \ref agora_rtm.IMessage.SetText "SetText"
        ///     - #GetRtmCallManager
        ///     - \ref agora_rtm.RtmCallManager.CreateLocalCallInvitation "CreateLocalCallInvitation"
        ///     - #CreateChannelAttribute
        /// </summary>
        /// <param name="token">
        /// The token used to log in the Agora RTM system and used when dynamic authentication is enabled. Set token as null in the integration and test stages.
        /// </param>
        /// <param name="userId">
        /// The user ID of the user logging in the Agora RTM system. The string length must be less than 64 bytes with the following character scope:
        ///     - All lowercase English letters: a to z
        ///     - All uppercase English letters: A to Z
        ///     - All numeric characters: 0 to 9
        ///     - The space character.
        ///     - Punctuation characters and other symbols, including: "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ","
        ///  A userId cannot be empty, null or "null".
        /// </param>
        /// <returns>
        ///   - 0: Success.
        ///   - ≠0: Failure. See #LOGIN_ERR_CODE for the error codes.
        /// </returns>
        public int Login(string token, string userId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return login_rtm(_rtmServicePtr, token, userId);
        }

        /// <summary>
        /// Logs out of the Agora RTM system.
        /// The local user receives the \ref agora_rtm.RtmClientEventHandler.OnLogoutHandler "OnLogoutHandler" callback.
        /// </summary>
        /// <returns>
        ///   - 0: Success.
        ///   - ≠0: Failure. See #LOGOUT_ERR_CODE for the error codes.
        /// </returns>
        public int Logout() {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return logout_rtm(_rtmServicePtr);
        }
        
        /// <summary>
        /// Renews the RTM Token of the SDK.
        /// You are required to renew your RTM Token when receiving the \ref agora_rtm.RtmClientEventHandler.OnTokenExpiredHandler "OnTokenExpiredHandler" callback, and the \ref agora_rtm.RtmClientEventHandler.OnRenewTokenResultHandler "OnRenewTokenResultHandler" callback returns the result of this method call. The call frequency limit for this method is 2 calls per second.
        /// </summary>
        /// <param name="token">Your new RTM Token. You need to generate the RTM Token yourself. See *Generate an RTM Token*.</param>
        /// <returns>
        ///   - 0: Success.
        ///   - ≠0: Failure. See #RENEW_TOKEN_ERR_CODE for the error codes.
        /// </returns>
        public int RenewToken(string token) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return renewToken_rtm(_rtmServicePtr, token);
        }

        /// <summary>
        /// Sends an (offline) peer-to-peer message to a specified user (receiver).
        /// - The \ref agora_rtm.RtmChannelEventHandler.OnSendMessageResultHandler "OnSendMessageResultHandler" callback returns the result of this method call.
        /// - When the message arrives at the receiver, the receiver receives the \ref agora_rtm.RtmClientEventHandler.OnMessageReceivedFromPeerHandler "OnMessageReceivedFromPeerHandler" callback.
        /// - This method allows you to send a message to a specified user when he/she is offline. If you set a message as an offline message and the specified user is offline when you send it, the RTM server caches it. 
        /// @note 
        ///   - For now we only cache 200 offline messages for up to seven days for each receiver. When the number of the cached messages reaches this limit, the newest message overrides the oldest one.
        ///   - If you use this method to send off a text message that starts off with AGORA_RTM_ENDCALL_PREFIX_<channelId>_<your additional information>, then this method is compatible with the endCall method of the legacy Agora Signaling SDK. Replace <channelId> with the channel ID from which you want to leave (end call), and replace <your additional information> with any additional information. 
        ///   - You must not put any "_" (underscore" in your additional information but you can set \<your additional information\> as empty "".
        /// </summary>
        /// <param name="peerId">
        ///   - User ID of the receiver. The string must not exceed 64 bytes in length. It cannot be empty, null, or "null". Supported characters:
        ///   - The 26 lowercase English letters: a to z
        ///   - The 26 uppercase English letters: A to Z
        ///   - The 10 numbers: 0 to 9
        ///   - "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ","
        /// </param>
        /// <param name="message">
        /// The message to be sent. For information about creating a message, see \ref agora_rtm.IMessage "IMessage".
        /// </param>
        /// <param name="options">
        /// Options when sending the message to a peer. See \ref agora_rtm.SendMessageOptions "SendMessageOptions".
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #PEER_MESSAGE_ERR_CODE for the error codes.
        /// </returns>
        public int SendMessageToPeer(string peerId, IMessage message, SendMessageOptions options) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return sendMessageToPeer_rtm(_rtmServicePtr, peerId, message.GetPtr(), options.enableOfflineMessaging, options.enableHistoricalMessaging);
        }

        /// <summary>
        /// @note
        /// - We do not recommend using this method to send a peer-to-peer message. Use #SendMessageToPeer instead.
        /// - You can send messages, including peer-to-peer and channel messages, at a maximum frequency of 180 calls every three seconds.
        /// </summary>
        /// <param name="peerId">
        /// User ID of the receiver.
        /// </param>
        /// <param name="message">
        /// The message to be sent. For information about creating a message, see \ref agora_rtm.IMessage "IMessage".
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #PEER_MESSAGE_ERR_CODE for the error codes.
        /// </returns>
        public int SendMessageToPeer(string peerId, IMessage message) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return sendMessageToPeer2_rtm(_rtmServicePtr, peerId, message.GetPtr());
        }

        /// <summary>
        /// Downloads a file or image from the Agora server to the local memory by media ID.
        /// The SDK returns the result of this method call by the \ref agora_rtm.RtmClientEventHandler.OnMediaDownloadToMemoryResultHandler	"OnMediaDownloadToMemoryResultHandler" callback.
        /// @note
        /// - This method applies to scenarios requiring quick access to the downloaded file or image.
        /// - The SDK releases the downloaded file or image immediately after returning the \ref agora_rtm.RtmClientEventHandler.OnMediaDownloadToMemoryResultHandler "OnMediaDownloadToMemoryResultHandler" callback.
        /// </summary>
        /// <param name="mediaId">
        /// The media ID of the file or image on the Agora server.
        /// </param>
        /// <param name="requestId">
        /// The unique ID of this download request.
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure.See #DOWNLOAD_MEDIA_ERR_CODE for the error codes.
        /// </returns>
        public int DownloadMediaToMemory(string mediaId, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return downloadMediaToMemory_rtm(_rtmServicePtr, mediaId, requestId);
        }

        /// <summary>
        /// Downloads a file or image from the Agora server to a specified local directory by media ID.
        /// The SDK returns the result of this method call by the \ref agora_rtm.RtmClientEventHandler.OnMediaDownloadToFileResultHandler "OnMediaDownloadToFileResultHandler" callback.
        /// </summary>
        /// <param name="mediaId">The media ID of the file or image on the Agora server.</param>
        /// <param name="filePath">The full path to the downloaded file or image. Must be in UTF-8.</param>
        /// <param name="requestId">The unique ID of this download request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure.See #DOWNLOAD_MEDIA_ERR_CODE for the error codes.
        /// </returns>
        public int DownloadMediaToFile(string mediaId, string filePath, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return downloadMediaToFile_rtm(_rtmServicePtr, mediaId, filePath, requestId);
        }

        /// <summary>
        /// Cancels an ongoing file or image download task by request ID.
        /// The SDK returns the result of this method call with the \ref agora_rtm.RtmClientEventHandler.OnMediaCancelResultHandler "OnMediaCancelResultHandler" callback.
        /// @note You can only cancel an ongoing download task. After a download task completes, the corresponding request ID is no longer valid and hence you cannot cancel it.
        /// </summary>
        /// <param name="requestId">
        /// The unique ID of the download request to cancel.
        /// </param>
        /// <param name="requestId">The unique ID of this download request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #CANCEL_MEDIA_ERR_CODE for the error codes.
        /// </returns>
        public int CancelMediaDownload(Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return cancelMediaDownload_rtm(_rtmServicePtr, requestId);
        }

        /// <summary>
        /// Cancels an ongoing file or image upload task by request ID.
        /// The SDK returns the result of this method call with the \ref agora_rtm.RtmClientEventHandler.OnMediaCancelResultHandler "OnMediaCancelResultHandler" callback.
        /// @note You can only cancel an ongoing upload task. After an upload task completes, you cannot cancel it and the corresponding request ID is no longer valid.
        /// </summary>
        /// <param name="requestId">
        /// The unique ID of the upload request to cancel.
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #CANCEL_MEDIA_ERR_CODE for the error codes.
        /// </returns>
        public int CancelMediaUpload(Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return cancelMediaUpload_rtm(_rtmServicePtr, requestId);
        }

        /// <summary>
        /// Creates an Agora RTM channel.
        /// - If the method call succeeds, the SDK returns an \ref agora_rtm.RtmChannel "RtmChannel" instance.
        /// - If this method call fails, the SDK returns `null`.
        /// @note You can create multiple channels in an #RtmClient instance. But you can only join a maximum of 20 channels at the same time. As a good practice, Agora recommends calling the #Dispose method to release all resources of an RTM channel that you no longer use.
        /// </summary>
        /// <param name="channelId">
        /// The unique channel name. A channelId cannot be empty, null, or "null". Must be less than 64 bytes in length. Supported characters:
        ///  - All lowercase English letters: a to z
        ///  - All uppercase English letters: A to Z
        ///  - All numeric characters: 0 to 9
        ///  - The space character.
        ///  - Punctuation characters and other symbols, including: "!", "#", "$", "%", "&", "(", ")", "+", "-", ":", ";", "<", "=", ".", ">", "?", "@", "[", "]", "^", "_", " {", "}", "|", "~", ","
        /// </param>
        /// <param name="rtmChannelEventHandler">See RtmChannelEventHandler.</param>
        /// <returns>
        ///  - An \ref agora_rtm.RtmChannel "RtmChannel" object: Success. If a channel with the same `channelId` does not exist, the method returns the created channel instance. If a channel with the same `channelId` already exists, the method returns the existing channel instance.
        ///  - null: Failure. Possible reasons: The `channelId` is invalid or you have joined the maximum number of channels.
        /// </returns>
        public RtmChannel CreateChannel(string channelId, RtmChannelEventHandler rtmChannelEventHandler) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}

            if (rtmChannelEventHandler == null) 
            {
                Debug.LogError("rtmChannelEventHandler is null");
            }
            
            if (channelDic.ContainsKey(channelId)) {
                if (channelDic[channelId] != null) {
                    return channelDic[channelId];
                }
            }
            
            IntPtr _rtmChannelPtr = createChannel_rtm(_rtmServicePtr, channelId, rtmChannelEventHandler.GetChannelEventHandlerPtr());
            RtmChannel channel = new RtmChannel(_rtmChannelPtr, rtmChannelEventHandler);
            channelDic.Add(channelId, channel);
            return channel;
        }

        /// <summary>
        /// Creates an empty \ref agora_rtm.TextMessage "TextMessage" instance.
        /// @note 
        ///  - An \ref agora_rtm.TextMessage "TextMessage" instance can be used either for a channel message or for a peer-to-peer message.
        ///  - When you no longer need an \ref agora_rtm.TextMessage "TextMessage" instance, please call the \ref agora_rtm.RtmClient.Dispose "Dispose" method to release all resources that it is using.
        ///  - You can set the content of the text message at a later point using the \ref agora_rtm.IMessage.SetText "SetText" method, but please ensure that the size of the text message does not exceed 32 KB.
        /// </summary>
        /// <returns>
        /// An empty text \ref agora_rtm.TextMessage "TextMessage" instance.
        /// </returns>
        public TextMessage CreateMessage() {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr _MessagePtr = createMessage4_rtm(_rtmServicePtr);
            return new TextMessage(_MessagePtr, TextMessage.MESSAGE_FLAG.SEND);
        }

        /// <summary>
        /// Creates a text \ref agora_rtm.TextMessage "TextMessage" instance.
        /// @note 
        ///  - An \ref agora_rtm.TextMessage "TextMessage" instance can be used either for a channel message or for a peer-to-peer message.
        ///  - When you no longer need an \ref agora_rtm.TextMessage "TextMessage" instance, please call the \ref agora_rtm.RtmClient.Dispose "Dispose" method to release all resources that it is using.
        /// </summary>
        /// <param name="text">The text content of the message.</param>
        /// <returns>
        /// An text \ref agora_rtm.TextMessage "TextMessage" instance.
        /// </returns>
        public TextMessage CreateMessage(string text) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr _MessagePtr = createMessage3_rtm(_rtmServicePtr, text);
            return new TextMessage(_MessagePtr, TextMessage.MESSAGE_FLAG.SEND);
        }

        /// <summary>
        /// Creates a raw \ref agora_rtm.TextMessage "TextMessage" instance.
        /// @note 
        ///  - An \ref agora_rtm.TextMessage "TextMessage" instance can be used either for a channel message or for a peer-to-peer message.
        ///  - When you no longer need an \ref agora_rtm.TextMessage "TextMessage" instance, please call the \ref agora_rtm.RtmClient.Dispose "Dispose" method to release all resources that it is using.
        ///  - You can set a brief text description of the raw message at a later point using the \ref agora_rtm.IMessage.SetText "SetText" method, but please ensure that the size of the raw message and the description combined does not exceed 32 KB.
        /// </summary>
        /// <param name="rawData">The starting address of the raw message.</param>
        /// <returns>
        /// An raw \ref agora_rtm.TextMessage "TextMessage" instance.
        /// </returns>
        public TextMessage CreateMessage(byte[] rawData) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr _MessagePtr = createMessage2_rtm(_rtmServicePtr, rawData, rawData.Length);
            return new TextMessage(_MessagePtr, TextMessage.MESSAGE_FLAG.SEND);
        }
        
        /// <summary>
        /// Creates a raw \ref agora_rtm.TextMessage "TextMessage" instance and sets its description.
        ///  - An \ref agora_rtm.TextMessage "TextMessage" instance can be used either for a channel message or for a peer-to-peer message.
        ///  - When you no longer need an \ref agora_rtm.TextMessage "TextMessage" instance, please call the \ref agora_rtm.RtmClient.Dispose "Dispose" method to release all resources that it is using.
        ///  - You can set `description` as "" and add a brief text description of the raw message at a later point using the \ref agora_rtm.IMessage.SetText "SetText" method, but please ensure that the size of the raw message and the description combined does not exceed 32 KB.
        /// </summary>
        /// <param name="rawData">The starting address of the raw message.</param>
        /// <param name="description">A brief text description of the raw message. If you set a text description, ensure that the size of the raw message and the description combined does not exceed 32 KB.</param>
        /// <returns>
        /// An raw \ref agora_rtm.TextMessage "TextMessage" instance with a brief text description.
        /// </returns>
        public TextMessage CreateMessage(byte[] rawData, string description) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr _MessagePtr = createMessage_rtm(_rtmServicePtr, rawData, rawData.Length, description);
            return new TextMessage(_MessagePtr, TextMessage.MESSAGE_FLAG.SEND);
        }

        /// <summary>
        /// Creates an \ref agora_rtm.ImageMessage "ImageMessage" instance by media ID.
        /// - If you have at hand the media ID of an image on the Agora server, you can call this method to create an \ref agora_rtm.ImageMessage "ImageMessage" instance.
        /// - If you do not have a media ID, then you must call #CreateImageMessageByUploading to get a corresponding \ref agora_rtm.ImageMessage "ImageMessage" instance by uploading an image to the Agora RTM server.
        /// </summary>
        /// <param name="mediaId">The media ID of an uploaded image on the Agora server.</param>
        /// <returns>
        /// An \ref agora_rtm.ImageMessage "ImageMessage" instance.
        /// </returns>
        public ImageMessage CreateImageMessageByMediaId(string mediaId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr _MessagePtr = createImageMessageByMediaId_rtm(_rtmServicePtr, mediaId);
            return new ImageMessage(_MessagePtr, ImageMessage.MESSAGE_FLAG.SEND);
        }

        /// <summary>
        /// Gets an \ref agora_rtm.ImageMessage "ImageMessage" instance by uploading an image to the Agora server.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnImageMediaUploadResultHandler "OnImageMediaUploadResultHandler" callback. If success, this callback returns a corresponding ImageMessage instance.
        /// - If the uploaded image is in JPEG, JPG, BMP, or PNG format, the SDK calculates the width and height of the image. You can call \ref agora_rtm.ImageMessage.GetWidth "GetWidth" to get the calculated width.
        /// - Otherwise, you need to call \ref agora_rtm.ImageMessage.SetWidth "SetWidth" and \ref agora_rtm.ImageMessage.SetHeight "SetHeight" to set the width and height of the uploaded image by yourself.
        /// @note
        ///  - If you have at hand the media ID of an image on the Agora server, you can call \ref agora_rtm.RtmClient.CreateImageMessageByMediaId "CreateImageMessageByMediaId" to create an \ref agora_rtm.ImageMessage "ImageMessage" instance.
        ///  - To cancel an ongoing image upload task, call \ref agora_rtm.RtmClient.CancelMediaUpload "CancelMediaUpload". 
        /// </summary>
        /// <param name="filePath">The full path to the local image to upload. Must be in UTF-8.</param>
        /// <param name="requestId">The unique ID of the upload request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #UPLOAD_MEDIA_ERR_CODE for the error codes.
        /// </returns>
        public int CreateImageMessageByUploading(string filePath, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
            }
            return createImageMessageByUploading_rtm(_rtmServicePtr, filePath, requestId);
        }

        /// <summary>
        /// Creates an \ref agora_rtm.FileMessage "FileMessage" instance by media ID.
        /// - If you have at hand the media ID of a file on the Agora server, you can call this method to create an \ref agora_rtm.FileMessage "FileMessage" instance.
        /// - If you do not have a media ID, then you must call #CreateFileMessageByUploading to get a corresponding \ref agora_rtm.FileMessage "FileMessage" instance by uploading a file to the Agora RTM server.
        /// </summary>
        /// <param name="mediaId">
        /// The media ID of an uploaded file on the Agora server.
        /// </param>
        /// <returns>
        /// An \ref agora_rtm.FileMessage "FileMessage" instance.
        /// </returns>
        public FileMessage CreateFileMessageByMediaId(string mediaId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr _MessagePtr = createFileMessageByMediaId_rtm(_rtmServicePtr, mediaId);
            return new FileMessage(_MessagePtr, FileMessage.MESSAGE_FLAG.SEND);
        }

        /// <summary>
        /// Gets an \ref agora_rtm.FileMessage "FileMessage" instance by uploading a file to the Agora server.
        /// The SDK returns the result with the \ref agora_rtm.RtmClientEventHandler.OnFileMediaUploadResultHandler "OnFileMediaUploadResultHandler" callback. If success, this callback returns a corresponding \ref agora_rtm.FileMessage "FileMessage" instance.
        /// </summary>
        /// <param name="filePath">
        /// The full path to the local file to upload. Must be in UTF-8.
        /// </param>
        /// <param name="requestId">
        /// The unique ID of this upload request.
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #UPLOAD_MEDIA_ERR_CODE for the error codes.
        /// </returns>
        public int CreateFileMessageByUploading(string filePath, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return createFileMessageByUploading_rtm(_rtmServicePtr, filePath, requestId);
        }

        /// <summary>
        /// Creates an \ref agora_rtm.RtmChannelAttribute "RtmChannelAttribute" instance.
        /// </summary>
        /// <returns>
        /// An \ref agora_rtm.RtmChannelAttribute "RtmChannelAttribute" instance.
        /// </returns>
        public RtmChannelAttribute CreateChannelAttribute() {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return null;
			}
            IntPtr channelAttribute = createChannelAttribute_rtm(_rtmServicePtr);
            RtmChannelAttribute attribute = new RtmChannelAttribute(channelAttribute);
            attributeList.Add(attribute);
            return attribute;
        }
        
        /// <summary>
        /// Provides the technical preview functionalities or special customizations by configuring the SDK with JSON options.
        /// @note The JSON options are not public by default. Agora is working on making commonly used JSON options public in a standard way. Contact support@agora.io for more information.
        /// </summary>
        /// <param name="parameters">
        /// SDK options in the JSON format.
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure.
        /// </returns>
        public int SetParameters(string parameters) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return setParameters_rtm(_rtmServicePtr, parameters);
        }
        
        /// <summary>
        /// Queries the online status of the specified users.
        /// - Online: The user has logged in the Agora RTM system.
        /// - Offline: The user has logged out of the Agora RTM system.
        /// @note The frequency of calls is capped at 10 times every 5 seconds.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnQueryPeersOnlineStatusResultHandler "OnQueryPeersOnlineStatusResultHandler" callback.
        /// </summary>
        /// <param name="peerIds">An array of the specified user IDs. The maximum number of `peerIds` is 256.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #QUERY_PEERS_ONLINE_STATUS_ERR for the error codes.
        /// </returns>
        public int QueryPeersOnlineStatus(string [] peerIds, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return queryPeersOnlineStatus_rtm(_rtmServicePtr, peerIds, peerIds.Length, requestId);
        }

        /// <summary>
        /// Subscribes to the online status of the specified users.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnSubscriptionRequestResultHandler "OnSubscriptionRequestResultHandler" callback.
        /// - When the method call succeeds, the SDK returns the \ref agora_rtm.RtmClientEventHandler.OnPeersOnlineStatusChangedHandler "OnPeersOnlineStatusChangedHandler" callback to report the online status of peers, to whom you subscribe.
        /// - When the online status of the peers, to whom you subscribe, changes, the SDK returns the \ref agora_rtm.RtmClientEventHandler.OnPeersOnlineStatusChangedHandler "OnPeersOnlineStatusChangedHandler" callback to report whose online status has changed.
        /// - If the online status of the peers, to whom you subscribe, changes when the SDK is reconnecting to the server, the SDK returns the \ref agora_rtm.RtmClientEventHandler.OnPeersOnlineStatusChangedHandler "OnPeersOnlineStatusChangedHandler" callback to report whose online status has changed when successfully reconnecting to the server.
        /// @note
        ///  - When you log out of the Agora RTM system, all the status that you subscribe to will be cleared. To keep the original subscription after you re-log in the system, you need to redo the whole subscription process.
        ///  - When the SDK reconnects to the server from the state of being interupted, the SDK automatically subscribes to the peers and states before the interruption without human intervention.
        /// </summary>
        /// <param name="peerIds">An array of the specified user IDs.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        /// - 0: Success.
        /// - ≠0: Failure. See #PEER_SUBSCRIPTION_STATUS_ERR for the error codes.
        /// </returns>
        public int SubscribePeersOnlineStatus(string [] peerIds, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmService is null");
                return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
            }
            return subscribePeersOnlineStatus_rtm(_rtmServicePtr, peerIds, peerIds.Length, requestId);
        }

        /// <summary>
        /// Unsubscribes from the online status of the specified users.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnSubscriptionRequestResultHandler "OnSubscriptionRequestResultHandler" callback.
        /// </summary>
        /// <param name="peerIds">An array of the specified user IDs.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns> 
        ///  - 0: Success.
        ///  - ≠0: Failure. See #PEER_SUBSCRIPTION_STATUS_ERR for the error codes.
        /// </returns>
        public int UnsubscribePeersOnlineStatus(string [] peerIds, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmService is null");
                return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
            }
            return unsubscribePeersOnlineStatus_rtm(_rtmServicePtr, peerIds, peerIds.Length, requestId);
        }

        /// <summary>
        /// Gets a list of the peers, to whose specific status you have subscribed.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnQueryPeersBySubscriptionOptionResultHandler "OnQueryPeersBySubscriptionOptionResultHandler" callback.
        /// </summary>
        /// <param name="option">The status type, to which you have subscribed. </param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #QUERY_PEERS_BY_SUBSCRIPTION_OPTION_ERR for the error codes.
        /// </returns>
        public int QueryPeersBySubscriptionOption(PEER_SUBSCRIPTION_OPTION option, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmService is null");
                return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
            }
            return queryPeersBySubscriptionOption_rtm(_rtmServicePtr, option, requestId);
        }

        /// <summary>
        /// Sets the size of a single log file. The SDK has two log files with the same size.
        /// </summary>
        /// <param name="fileSizeInKBytes">The size of a single log file (KB).The default is 10240 (KB). The value range is [512 KB, 1 GB].</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure.
        /// </returns>
        public int SetLogFileSize(int fileSizeInKBytes) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return setLogFileSize_rtm(_rtmServicePtr, fileSizeInKBytes);
        }

        /// <summary>
        /// Sets the output log level of the SDK.
        /// You can use one or a combination of the filters. The log level follows the sequence of `OFF`, `CRITICAL`, `ERROR`, `WARNING`, and `INFO`. Choose a level to see the logs preceding that level. If, for example, you set the log level to `WARNING`, you see the logs within levels `CRITICAL`, `ERROR`, and `WARNING`.
        /// </summary>
        /// <param name="fileter">The log filter level. </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure.
        /// </returns>
        public int SetLogFilter(int fileter) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return setLogFilter_rtm(_rtmServicePtr, fileter);
        }

        /// <summary>
        /// Specifies the default path to the SDK log file.
        /// @note 
        ///  - Ensure that the directory holding the log file exists and is writable.
        ///  - Ensure that you call this method immediately after calling the \ref agora_rtm.RtmClient.RtmClient "RtmClient" method to create a \ref agora_rtm.RtmClient "RtmClient" instance, otherwise the output log may be incomplete.
        /// 
        /// </summary>
        /// <param name="logFilePath">
        /// The absolute file path to the log file. The string of `logFilePath` is in UTF-8.
        /// </param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure.
        /// </returns>
        public int SetLogFile(string logFilePath) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return setLogFile_rtm(_rtmServicePtr, logFilePath);
        }

        /// <summary>
        /// Gets the member count of specified channels.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnGetChannelMemberCountResultHandler "OnGetChannelMemberCountResultHandler" callback.
        /// @note
        ///  - The call frequency limit for this method is one query per second.
        ///  - We do not support getting the member counts of more than 32 channels in one method call.
        ///  - You do not have to join the specified channels to call this method.
        /// </summary>
        /// <param name="channelIds">An array of the specified channel IDs.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>        
        ///  - 0: Success.
        ///  - ≠0: Failure. See #GET_CHANNEL_MEMBER_COUNT_ERR_CODE for the error codes.
        /// </returns>
        public int GetChannelMemberCount(string[] channelIds, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return getChannelMemberCount_rtm(_rtmServicePtr, channelIds, channelIds.Length, requestId);
        }

        /// <summary>
        /// Gets the attributes of a specified channel by attribute keys.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnGetChannelAttributesResultHandler "OnGetChannelAttributesResultHandler" callback.
        /// @note
        ///  - You do not have to join the specified channel to get its attributes.
        ///  - For #GetChannelAttributes and #GetChannelAttributesByKeys taken together, the maximum call frequency is 10 calls every five seconds.
        /// </summary>
        /// <param name="channelId">The channel ID of the specified channel.</param>
        /// <param name="attributeKeys">An array of attribute keys.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int GetChannelAttributesByKeys(string channelId, string[] attributeKeys, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return getChannelAttributesByKeys_rtm(_rtmServicePtr, channelId, attributeKeys, attributeKeys.Length, requestId);
        }

        /// <summary>
        /// Gets all attributes of a specified channel.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnGetChannelAttributesResultHandler "OnGetChannelAttributesResultHandler" callback.
        /// </summary>
        /// <param name="channelId">The channel ID of the specified channel.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int GetChannelAttributes(string channelId, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmServicePtr is null");
                return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
            }
            return getChannelAttributes_rtm(_rtmServicePtr, channelId, requestId);
        }

        /// <summary>
        /// Clears all attributes of a specified channel.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnClearChannelAttributesResultHandler "OnClearChannelAttributesResultHandler" callback.
        /// @note
        ///  - You do not have to join the specified channel to clear its attributes.
        ///  - The attributes of a channel will be cleared if the channel remains empty (has no members) for a couple of minutes.
        ///  - For #SetChannelAttributes,  #DeleteChannelAttributesByKeys and #ClearChannelAttributes taken together: the maximum call frequency is 10 calls every five seconds.
        /// </summary>
        /// <param name="channelId">The channel ID of the specified channel.</param>
        /// <param name="enableNotificationToChannelMembers">Indicates whether or not to notify all channel members of a channel attribute change.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int ClearChannelAttributes(string channelId, bool enableNotificationToChannelMembers, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return clearChannelAttributes_rtm(_rtmServicePtr, channelId, enableNotificationToChannelMembers, requestId);
        }

        /// <summary>
        /// Deletes the attributes of a specified channel by attribute keys.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnDeleteChannelAttributesResultHandler "OnDeleteChannelAttributesResultHandler" callback.
        /// @note 
        ///  - You do not have to join the specified channel to update its attributes.
        ///  - The attributes of a channel will be cleared if the channel remains empty (has no members) for a couple of minutes.
        ///  - If more than one user can update the channel attributes, then Agora recommends calling #GetChannelAttributes to update the cache before calling this method.
        ///  - For #SetChannelAttributes,  #DeleteChannelAttributesByKeys and #ClearChannelAttributes taken together: the maximum call frequency is 10 calls every five seconds.
        /// </summary>
        /// <param name="channelId">The channel ID of the specified channel.</param>
        /// <param name="attributeKeys">An array of channel attribute keys.</param>
        /// <param name="enableNotificationToChannelMembers">Indicates whether or not to notify all channel members of a channel attribute change.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int DeleteChannelAttributesByKeys(string channelId, string [] attributeKeys, bool enableNotificationToChannelMembers, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return deleteChannelAttributesByKeys_rtm(_rtmServicePtr, channelId, attributeKeys, attributeKeys.Length, enableNotificationToChannelMembers, requestId);
        }

        /// <summary>
        /// Gets the attributes of a specified user by attribute keys.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnGetUserAttributesResultHandler "OnGetUserAttributesResultHandler" callback.
        /// @note For #GetUserAttributes and #GetUserAttributesByKeys taken together, the maximum call frequency is 40 calls every five seconds.
        /// </summary>
        /// <param name="userId">The user ID of the specified user.</param>
        /// <param name="attributeKeys">An array of the attribute keys.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int GetUserAttributesByKeys(string userId, string [] attributeKeys, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return getUserAttributesByKeys_rtm(_rtmServicePtr, userId, attributeKeys, attributeKeys.Length, requestId);
        }

        /// <summary>
        /// Gets all attributes of a specified user.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnGetUserAttributesResultHandler "OnGetUserAttributesResultHandler" callback.
        /// </summary>
        /// <param name="userId">The user ID of the specified user.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int GetUserAttributes(string userId, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return getUserAttributes_rtm(_rtmServicePtr, userId, requestId);
        }

        /// <summary>
        /// Clears all attributes of the local user.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnClearLocalUserAttributesResultHandler "OnClearLocalUserAttributesResultHandler" callback.
        /// @note For #DeleteLocalUserAttributesByKeys and #ClearLocalUserAttributes taken together: the maximum call frequency is 10 calls every five seconds.
        /// </summary>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int ClearLocalUserAttributes(Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return clearLocalUserAttributes_rtm(_rtmServicePtr, requestId);
        }

        /// <summary>
        /// Deletes the local user's attributes by attribute keys.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnDeleteLocalUserAttributesResultHandler "OnDeleteLocalUserAttributesResultHandler" callback.
        /// @note For #DeleteLocalUserAttributesByKeys and #ClearLocalUserAttributes taken together: the maximum call frequency is 10 calls every five seconds.
        /// </summary>
        /// <param name="attributeKeys">An array of the attribute keys to be deleted.</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int DeleteLocalUserAttributesByKeys(string [] attributeKeys, Int64 requestId) {
            if (_rtmServicePtr == IntPtr.Zero)
			{
                Debug.LogError("rtmServicePtr is null");
				return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
			}
            return deleteLocalUserAttributesByKeys_rtm(_rtmServicePtr, attributeKeys, attributeKeys.Length, requestId);
        }

        /// <summary>
        /// Resets the attributes of a specified channel.
        /// The SDK returns the result by the \ref agora_rtm.RtmClientEventHandler.OnSetChannelAttributesResultHandler "OnSetChannelAttributesResultHandler" callback.
        /// @note
        ///  - You do not have to join the specified channel to update its attributes.
        ///  - The attributes of a channel will be cleared if the channel remains empty (has no members) for a couple of minutes.
        ///  - If more than one user can update the channel attributes, then Agora recommends calling #GetChannelAttributes to update the cache before calling this method.
        ///  - For #DeleteLocalUserAttributesByKeys and #ClearLocalUserAttributes taken together: the maximum call frequency is 10 calls every five seconds.
        /// </summary>
        /// <param name="channelId">The channel ID of the specified channel.</param>
        /// <param name="attributes">An array of channel attributes. See \ref agora_rtm.RtmChannelAttribute "RtmChannelAttribute".</param>
        /// <param name="options">Options for this attribute operation. See \ref agora_rtm.ChannelAttributeOptions "ChannelAttributeOptions".</param>
        /// <param name="requestId">The unique ID of this request.</param>
        /// <returns>
        ///  - 0: Success.
        ///  - ≠0: Failure. See #ATTRIBUTE_OPERATION_ERR for the error codes.
        /// </returns>
        public int SetChannelAttributes(string channelId, RtmChannelAttribute [] attributes, ChannelAttributeOptions options, Int64 requestId)
        {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmService is null");
                return (int)COMMON_ERR_CODE.ERROR_NULL_PTR;
            }

            if (attributes != null && attributes.Length > 0) {
                Int64 [] attributeLists = new Int64[attributes.Length];
                for (int i = 0; i < attributes.Length; i++) {
                    attributeLists[i] = attributes[i].GetPtr().ToInt64();
                }
                return setChannelAttributes_rtm(_rtmServicePtr, channelId, attributeLists, attributes.Length, options.enableNotificationToChannelMembers, requestId);
            }
            return -7;
        } 

        /// <summary>
        /// Gets an RtmCallManager object.
        /// Each #RtmClient object corresponds to a unique \ref agora_rtm.RtmCallManager "RtmCallManager" object. That said, the \ref agora_rtm.RtmCallManager "RtmCallManager" object is #RtmClient-specific.
        /// @note When you do no use \ref agora_rtm.RtmCallManager "RtmCallManager", call the \ref agora_rtm.RtmCallManager.Dispose "Dispose" method to release all of its resources.
        /// </summary>
        /// <param name="eventHandler">	An \ref agora_rtm.RtmCallEventHandler "RtmCallEventHandler" object.</param>
        /// <returns>An \ref agora_rtm.RtmCallManager "RtmCallManager" object.</returns>
        public RtmCallManager GetRtmCallManager(RtmCallEventHandler eventHandler) {
            if (_rtmServicePtr == IntPtr.Zero)
            {
                Debug.LogError("rtmService is null");
                return null;
            }
            if (eventHandler == null) {
                Debug.LogError("eventHandler is null");
                return null;
            }
            IntPtr rtmCallManagerPtr = getRtmCallManager_rtm(_rtmServicePtr, eventHandler.GetPtr());
            if (_rtmCallManager == null) {
                _rtmCallManager = new RtmCallManager(rtmCallManagerPtr, eventHandler);
            }
            return _rtmCallManager;
        }

        public void Dispose() {
            Dispose(true);
            GC.SuppressFinalize(this);
        }

        private void Dispose(bool disposing) {
            if (_disposed) return;

            if (disposing) {}

            Release(true);
            _disposed = true;
        }
    }
}