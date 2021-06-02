The Agora RTM SDK provides a stable messaging mechanism for you to implement real-time messaging scenarios.

> This page lists the core APIs of the Agora RTM SDK. Unless otherwise specified, most of the core APIs should only be called after the \ref agora_rtm.RtmClient.Login "login" method call succeeds and after you receive the \ref agora_rtm.RtmClientEventHandler.OnLoginSuccessHandler "OnLoginSuccessHandler" callback.

Following are the core functionalities that the Agora RTM SDK provides.

- [Login and logout](#loginlogout)
- [Sending a peer-to-peer message](#peermessage)
- [Querying the online status of specified users](#onlinestatus)
- [Subscribing to or unsubscribing from the online status of specified users](#subscribe)
- [User attribute operations](#attributes)
- [Channel attribute operations](#channelattributes)
- [Retrieving channel member count of specified channels](#channelmembercount)
- [Uploading and downloading images or files](#multimedia)
- [Joining and leaving a Channel](#joinorleavechannel)
- [Sending a channel message](#channelmessage)
- [Retrieving a member list of the current channel](#memberlist)
- [Call invitation](#callinvitation)
- [Renewing the current RTM Token](#renewtoken)
- [Logfile settings and version check](#logfile)
- [Customized method](#customization)

<a name="loginlogout"></a>
## Login and logout

> You need to understand the connection state between the SDK and the Agora RTM system before developing an RTM app. For more information, see [Manage Connection States](https://docs.agora.io/en/Real-time-Messaging/reconnecting_cpp?platform=Linux%20CPP).

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.RtmClient "RtmClient"</td>
<td>Creates and returns <code>RtmClient</code> instance. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.Login "Login"</td>
<td>Logs in the Agora RTM system. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.Logout "Logout"</td>
<td>Logs out of the Agora RTM system. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.Dispose "Dispose"</td>
<td>Releases all resources that the <code>RtmClient</code> instance uses. </td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnLoginSuccessHandler "OnLoginSuccessHandler"</td>
<td>Occurs when a user logs in the Agora RTM system. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnLoginFailureHandler "OnLoginFailureHandler"</td>
<td>Occurs when a user fails to log in the Agora RTM system. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnLogoutHandler "OnLogoutHandler"</td>
<td>Occurs when a user logs out of the Agora RTM system. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnConnectionStateChangedHandler "OnConnectionStateChangedHandler"</td>
<td>Occurs when the connection state between the SDK and the Agora RTM system changes.</td>
</tr>
</table>

<a name="peermessage"></a>
## Sending a peer-to-peer message

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage() "CreateMessage"</td>
<td>Creates and returns an empty text <code>TextMessage</code> instance. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage(string text) "CreateMessage"</td>
<td>Creates and returns a text <code>TextMessage</code> instance.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage(byte[] rawData) "CreateMessage"</td>
<td>Creates and returns a raw <code>TextMessage</code> instance.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage(byte[] rawData, string description ) "CreateMessage"</td>
<td>Creates and returns a raw <code>TextMessage</code> instance and sets its description.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SendMessageToPeer(string peerId, IMessage message,	SendMessageOptions options) "SendMessageToPeer"</td>
<td>Sends an online or offline peer-to-peer message to a specified user (receiver). When used with a prefix, this method is similar to the <code>endCall</code> method of the Agora Signaling SDK.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.Dispose "Dispose"</td>
<td>Releases all resources that the <code>TextMessage</code> uses. </td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnSendMessageResultHandler "OnSendMessageResultHandler"</td>
<td>Returns the result of the <code>SendMessageToPeer(string peerId, IMessage message, SendMessageOptions options)</code> or <code>SendMessageToPeer(string peerId, IMessage message)</code> method call. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMessageReceivedFromPeerHandler "OnMessageReceivedFromPeerHandler"</td>
<td>Occurs when receiving a peer-to-peer message.</td>
</tr>
</table>


<a name="onlinestatus"></a>
## Querying the online status of the specified users

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.QueryPeersOnlineStatus "QueryPeersOnlineStatus"</td>
<td>Queries the online status of the specified users.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnQueryPeersOnlineStatusResultHandler "OnQueryPeersOnlineStatusResultHandler"</td>
<td> Returns the result of the <code>QueryPeersOnlineStatus</code> method call.</td>
</tr>
</table>

<a name="subscribe"></a>
## Subscribing to or unsubscribing from the online status of specified users

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SubscribePeersOnlineStatus "SubscribePeersOnlineStatus"</td>
<td>Subscribes to the online status of the specified users.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.UnsubscribePeersOnlineStatus "UnsubscribePeersOnlineStatus"</td>
<td>Unsubscribes from the online status of the specified users.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.QueryPeersBySubscriptionOption "QueryPeersBySubscriptionOption"</td>
<td>Gets a list of the peers, to whose specific status you have subscribed.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnSubscriptionRequestResultHandler "OnSubscriptionRequestResultHandler"</td>
<td>Reports the result of the <code>SubscribePeersOnlineStatus</code> or the <code>UnsubscribePeersOnlineStatus</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnPeersOnlineStatusChangedHandler "OnPeersOnlineStatusChangedHandler"</td>
<td>Occurs when the online status of the peers, to whom you subscribe, changes. See <code>PeerOnlineStatus</code> for the online status of a peer.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnQueryPeersBySubscriptionOptionResultHandler "OnQueryPeersBySubscriptionOptionResultHandler"</td>
<td>Returns the result of the <code>QueryPeersBySubscriptionOption</code> method call.</td>
</tr>
</table>


<a name="attributes"></a>
## User attribute operations

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateChannelAttribute "CreateChannelAttribute"</td>
<td>Creates and returns an <code>RtmChannelAttribute</code> instance.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.DeleteLocalUserAttributesByKeys "DeleteLocalUserAttributesByKeys"</td>
<td>Deletes the local user's attributes by attribute keys.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.ClearLocalUserAttributes "ClearLocalUserAttributes"</td>
<td>Clears all attributes of the local user.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetUserAttributes "GetUserAttributes"</td>
<td>Gets all attributes of a specified user.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetUserAttributesByKeys "GetUserAttributesByKeys"</td>
<td>Gets the attributes of a specified user by attribute keys.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnDeleteLocalUserAttributesResultHandler "OnDeleteLocalUserAttributesResultHandler"</td>
<td> Reports the result of the <code>DeleteLocalUserAttributesByKeys</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnClearLocalUserAttributesResultHandler "OnClearLocalUserAttributesResultHandler"</td>
<td> Reports the result of the <code>ClearLocalUserAttributes</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnGetUserAttributesResultHandler "OnGetUserAttributesResultHandler"</td>
<td> Reports the result of the <code>GetUserAttributes</code> or <code>GetUserAttributesByKeys</code> method call.</td>
</tr>
</table>

<a name="channelattributes"></a>
## Channel attribute operations

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SetChannelAttributes "SetChannelAttributes"</td>
<td>Sets the attributes of a specified channel with new ones.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.DeleteChannelAttributesByKeys "DeleteChannelAttributesByKeys"</td>
<td>Deletes the attributes of a specified channel by attribute keys.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.ClearChannelAttributes "ClearChannelAttributes"</td>
<td>Clears all attributes of a specified channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetChannelAttributes "GetChannelAttributes"</td>
<td>Gets all attributes of a specified channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetChannelAttributesByKeys "GetChannelAttributesByKeys"</td>
<td>Gets the attributes of a specified channel by attribute keys.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnSetChannelAttributesResultHandler "OnSetChannelAttributesResultHandler"</td>
<td> Reports the result of the <code>SetChannelAttributes</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnDeleteChannelAttributesResultHandler "OnDeleteChannelAttributesResultHandler"</td>
<td> Reports the result of the <code>DeleteChannelAttributesByKeys</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnClearChannelAttributesResultHandler "OnClearChannelAttributesResultHandler"</td>
<td> Reports the result of the <code>ClearChannelAttributes</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnGetChannelAttributesResultHandler "OnGetChannelAttributesResultHandler"</td>
<td> Reports the result of the <code>GetChannelAttributes</code> or <code>GetChannelAttributesByKeys</code> method call.</td>
</tr>
</table>

<table>
<tr>
<th>Event for all channel members</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnAttributesUpdatedHandler "OnAttributesUpdatedHandler"</td>
<td>Reports all attributes of the channel when the channel attributes are updated.</td>
</tr>
</table>


<a name="channelmembercount"></a>
## Retrieving channel member count of specified channels

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetChannelMemberCount "GetChannelMemberCount"</td>
<td>Gets the member count of specified channels. You do not have to join the specified channels to call this method. </td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnGetChannelMemberCountResultHandler "OnGetChannelMemberCountResultHandler"</td>
<td>Reports the result of the <code>GetChannelMemberCount</code> method call.</td>
</tr>
</table>

<a name="multimedia"></a>
## Uploading and downloading images or files

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateFileMessageByUploading "CreateFileMessageByUploading"</td>
<td>Gets an <code>FileMessage</code> instance by uploading a file to the Agora server.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateImageMessageByUploading "CreateImageMessageByUploading"</td>
<td>Gets an <code>ImageMessage</code> instance by uploading an image to the Agora server.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CancelMediaUpload "CancelMediaUpload"</td>
<td>Cancels an ongoing file or image upload task by request ID.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateFileMessageByMediaId "CreateFileMessageByMediaId"</td>
<td>Creates an <code>FileMessage</code> instance by media ID.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateImageMessageByMediaId "CreateImageMessageByMediaId"</td>
<td>Creates an <code>ImageMessage</code> instance by media ID.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.DownloadMediaToMemory "DownloadMediaToMemory"</td>
<td>Downloads a file or image from the Agora server to the local memory by media ID.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.DownloadMediaToFile "DownloadMediaToFile"</td>
<td>Downloads a file or image from the Agora server to the local storage by media ID.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CancelMediaDownload "CancelMediaDownload"</td>
<td>Cancels an ongoing file or image download task by request ID.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMediaUploadingProgressHandler "OnMediaUploadingProgressHandler"</td>
<td>Reports the progress of an ongoing uploading task.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMediaCancelResultHandler "OnMediaCancelResultHandler"</td>
<td>Reports the result of the <code>CancelMediaDownload</code> or <code>CancelMediaUpload</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnFileMediaUploadResultHandler "OnFileMediaUploadResultHandler"</td>
<td>Reports the result of the <code>CreateFileMessageByUploading</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnImageMediaUploadResultHandler "OnImageMediaUploadResultHandler"</td>
<td>Reports the result of the <code>CreateImageMessageByUploading</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnFileMessageReceivedFromPeerHandler "OnFileMessageReceivedFromPeerHandler"</td>
<td>Occurs when receiving a peer-to-peer file message.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnImageMessageReceivedFromPeerHandler "OnImageMessageReceivedFromPeerHandler"</td>
<td>Occurs when receiving a peer-to-peer image message.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnFileMessageReceivedHandler "OnFileMessageReceivedHandler"</td>
<td>Occurs when receiving a channel file message.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnImageMessageReceivedHandler "OnImageMessageReceivedHandler"</td>
<td>Occurs when receiving a channel image message.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMediaDownloadingProgressHandler "OnMediaDownloadingProgressHandler"</td>
<td>Reports the progress of an ongoing downloading task.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMediaCancelResultHandler "OnMediaCancelResultHandler"</td>
<td>Reports the result of the <code>CancelMediaDownload</code> or the <code>CancelMediaUpload</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMediaDownloadToMemoryResultHandler "OnMediaDownloadToMemoryResultHandler"</td>
<td>Reports the result of the <code>DownloadMediaToMemory</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnMediaDownloadToFileResultHandler "OnMediaDownloadToFileResultHandler"</td>
<td>Reports the result of the <code>DownloadMediaToFile</code> method call.</td>
</tr>
</table>


<a name="joinorleavechannel"></a>
## Joining or leaving a channel

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateChannel "CreateChannel"</td>
<td>Creates an <code>RtmChannel</code> object.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannel.Join "Join"</td>
<td>Joins a channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannel.Leave "Leave"</td>
<td>Leaves a channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannel.Dispose "Dispose"</td>
<td>Releases all resources used by the <code>RtmChannel</code> object.</td>
</tr>
</table>


<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnJoinSuccessHandler "OnJoinSuccessHandler"</td>
<td>Occurs when a user joins a channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnJoinFailureHandler "OnJoinFailureHandler"</td>
<td>Occurs when a user fails to join a channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnLeaveHandler "OnLeaveHandler"</td>
<td>Reports the result of the <code>Leave</code> method call. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnMemberJoinedHandler "OnMemberJoinedHandler"</td>
<td>Occurs when a user joins a channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnMemberLeftHandler "OnMemberLeftHandler"</td>
<td>Occurs when a channel member leaves a channel.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnMemberCountUpdatedHandler "OnMemberCountUpdatedHandler"</td>
<td>Occurs when the number of the channel members changes. This callback returns the new number.</td>
</tr>
</table>


<a name="channelmessage"></a>
## Channel message

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage() "CreateMessage"</td>
<td>Creates and returns an empty text <code>TextMessage</code> instance. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage(string text) "CreateMessage"</td>
<td>Creates and returns an text <code>IMessage</code> instance.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage(byte[] rawData) "CreateMessage"</td>
<td>Creates and returns an raw <code>IMessage</code> instance.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.CreateMessage(byte[] rawData, string description) "CreateMessage"</td>
<td>Creates and returns an <code>IMessage</code> instance.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannel.SendMessage(const IMessage message, const SendMessageOptions options) "sendMessage"</td>
<td>Allows a channel member to send a message to all members the channel. </td>
</tr>
<tr>
<td>\ref agora_rtm.IMessage.Release "Release"</td>
<td>Releases all resources that the <code>IMessage</code> uses. </td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnSendMessageResultHandler "OnSendMessageResultHandler"</td>
<td>Reports the result of the <code>SendMessage</code> method call.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnMessageReceivedHandler "OnMessageReceivedHandler"</td>
<td>Occurs when all remote users receive a channel message from a sender.</td>
</tr>
</table>


<a name="memberlist"></a>
## Retrieving a member list of the channel

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannel.GetMembers "GetMembers"</td>
<td>Retrieves the member list of a channel.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmChannelEventHandler.OnGetMembersHandler "OnGetMembersHandler"</td>
<td>Reports the result of the <code>GetMembers</code> method call. For the data structure of the channel member, see <code>ChannelMemberCount</code>. </td>
</tr>
</table>



<a name="callinvitation"></a>
## Managing call invitation

<table>
<tr>
<th>Method for Managing a Call Manager</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetRtmCallManager "GetRtmCallManager"</td>
<td>Gets an RTM call manager. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallManager.Dispose "Dispose"</td>
<td>Releases all resources used by the <code>RtmCallManager</code> instance.</td>
</tr>
</table>

<table>
<tr>
<th>Caller Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallManager.CreateLocalCallInvitation "CreateLocalCallInvitation"</td>
<td>Allows the caller to create and manage an <code>LocalInvitation</code> object. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallManager.SendLocalInvitation "SendLocalInvitation"</td>
<td>Allows the caller to send a call invitation to a remote user.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallManager.CancelLocalInvitation "CancelLocalInvitation"</td>
<td>Allows the caller to cancel a call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.LocalInvitation.Dispose "Dispose"</td>
<td>Releases all resources used by the <code>LocalInvitation</code> instance.</td>
</tr>
</table>

<table>
<tr>
<th>Callee Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallManager.AcceptRemoteInvitation "AcceptRemoteInvitation"</td>
<td>Allows the callee to accept a call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallManager.RefuseRemoteInvitation "RefuseRemoteInvitation"</td>
<td>Allows the callee to decline a call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RemoteInvitation.Dispose "Dispose"</td>
<td>Releases all resources used by the <code>RemoteInvitation</code> instance.</td>
</tr>
</table>


<table>
<tr>
<th>Caller Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnLocalInvitationReceivedByPeerHandler "OnLocalInvitationReceivedByPeerHandler"</td>
<td>Callback to the caller: occurs when the callee receives the call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnLocalInvitationCanceledHandler "OnLocalInvitationCanceledHandler"</td>
<td>Callback to the caller: occurs when the caller cancels a sent call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnLocalInvitationAcceptedHandler "OnLocalInvitationAcceptedHandler"</td>
<td>Callback to the caller: occurs when the callee accepts the call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnLocalInvitationRefusedHandler "OnLocalInvitationRefusedHandler"</td>
<td>Callback to the caller: occurs when the callee refuses the call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnLocalInvitationFailureHandler "OnLocalInvitationFailureHandler"</td>
<td>Callback to the caller: occurs when the life cycle of an outgoing call invitation ends in failure.</td>
</tr>
</table>

<table>
<tr>
<th>Callee Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnRemoteInvitationReceivedHandler "OnRemoteInvitationReceivedHandler"</td>
<td>Callback to the callee: occurs when the callee receives a call invitation. At the same time, the SDK returns an <code>RemoteInvitation</code> object to the callee. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnRemoteInvitationAcceptedHandler "OnRemoteInvitationAcceptedHandler"</td>
<td>Callback to the callee: occurs when the callee accepts a call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnRemoteInvitationRefusedHandler "OnRemoteInvitationRefusedHandler"</td>
<td>Callback to the callee: occurs when the callee declines a call invitation.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnRemoteInvitationCanceledHandler "OnRemoteInvitationCanceledHandler"</td>
<td>Callback to the callee: occurs when the caller cancels the call invitation. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmCallEventHandler.OnRemoteInvitationFailureHandler "OnRemoteInvitationFailureHandler"</td>
<td>Callback to the callee: occurs when the life cycle of the incoming call invitation ends in failure.</td>
</tr>
</table>



<a name="renewtoken"></a>
## Renew the Token


<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.RenewToken "RenewToken"</td>
<td>Renews the RTM Token of the SDK.</td>
</tr>
</table>

<table>
<tr>
<th>Event</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnTokenExpiredHandler "OnTokenExpiredHandler"</td>
<td>Occurs when the RTM server detects that the RTM token has passed the 24-hour validity period and when the SDK is in the <code>CONNECTION_STATE_RECONNECTING</code> state.</td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClientEventHandler.OnRenewTokenResultHandler "OnRenewTokenResultHandler"</td>
<td> Reports the result of the <code>RenewToken</code> method call.</td>
</tr>
</table>

<a name="logfile"></a>
## Log file settings and version check

> - You can perform log file related operations after creating the \ref agora_rtm.RtmClient "RtmClient" instance and before calling the \ref agora_rtm.RtmClient.Login "Login" method.
> - `GetSdkVersion` is a global method. You can call it before creating an \ref agora_rtm.RtmClient "RtmClient" instance.

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SetLogFile "SetLogFile"</td>
<td>Specifies the default path to the SDK log file. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SetLogFilter "SetLogFilter"</td>
<td>Sets the output log level of the SDK. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SetLogFileSize "SetLogFileSize"</td>
<td>Sets the size of a single log file. The SDK has two log files with the same size. </td>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.GetSdkVersion "GetSdkVersion"</td>
<td>Gets the version of the Agora RTM SDK. </td>
</tr>
</table>


<a name="customization"></a>
## Customized method

<table>
<tr>
<th>Method</th>
<th>Description</th>
</tr>
<tr>
<td>\ref agora_rtm.RtmClient.SetParameters "SetParameters"</td>
<td>Provides the technical preview functionalities or special customizations by configuring the SDK with JSON options.</td>
</tr>
</table>