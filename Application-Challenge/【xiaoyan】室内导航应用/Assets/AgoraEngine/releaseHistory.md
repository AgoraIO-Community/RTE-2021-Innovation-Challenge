## Revision History

Complete release note can be found online:
https://docs.agora.io/en/Video/release_unity_video?platform=Unity

v3.3.1.71
v3.3.1 (Agora Website) was released on March 12, 2021.
.71 is Asset Store version
Key Features
1. Native support for the M1 chip
2. Channel media options
3. Cloud proxy
4. Deep-learning noise reduction

## v3.2.1.71 / v3.2.1.72
Released: Feb 1, 2021
* 71 is the online download
* 72 is the asset store download
- Updated codec frameworks and audio profiles
- Optimized cloud proxy architecture
- Reinforced security and compliance
- Regional connection control (geo-fencing)
- More voice beautifier and audio effects

## v3.0.1.71 / v3.0.1.72
Released in Sept, 2020
Features:
- Specify the area of connection
- Multiple channel management
- Audio mixing pitch
- Adjusting the playback volume of the specified remote user
- Voice beautifier and audio effects
- Face detection
- ScreenSharing on Windows to support UWP Applications
- RGBA pixel format

Several important bug fixes including what's needed for Transcoding.

* This version has incorporated the iOS 14 Local network privacy pop-up issue.
* The policy for GEO fencing is consistent with Native 3.0.0.2: "When used in a non-designated area, the server will be selected from the server addresses in the area and the designated area to establish a connection."

See the link below for details
https://docs.agora.io/en/Interactive%20Broadcast/release_unity_video?platform=Unity#v301

## v2.9.2.2
Released in April, 2020
- minor demo bug fixes,
- include this history file

## v2.9.2.1
Released in March, 2020
- minor build script update

## v2.9.2

v2.9.2 is released on Feb 17, 2020.

- This release fixed some abnormal behaviors on Android devices.
- This release fixed the stuck behavior of using the Editor debug mode on Windows platform.

## v2.9.1

Agora Unity SDK is widely used in games, education, AR, VR and other scenarios.

v2.9.1 was released on December 23, 2019.

**Functions and features**

#### 1. Multi-platform support
Supports iOS, Android, macOS and Windows (x86/x86_64) platforms.

#### 2. Interoperability with the Agora Web SDK
Provides the `EnableWebSdkInteroperability` method for enabling interoperability with the Agora Web SDK in a live broadcast.

#### 3. Video rendering method
Supports multiple video rendering methods. You can choose any method in  **Auto Graphics API**.
![](https://web-cdn.agora.io/docs-files/1576826628073)

#### 4. Multithreaded rendering
Supports multithreaded rendering. You can click the **Multithreaded Rendering** option for rendering in multiple threads.

#### 5. Raw data
Supports raw audio data and raw video data in RGBA format. You can capture and process the raw data according to your needs. See details in [Raw Video Data](../../en/Interactive%20Broadcast/raw_data_video_unity.md).

#### 6. External data source
Provides APIs for accessing to the external data source. You can configure the external audio and video source, and push the data to the Agora Unity SDK. See details in [Custom Video Source and Renderer](../../en/Interactive%20Broadcast/custom_video_unity.md)

#### 7. Encryption
Supports encryption of audio and video streaming. The following table shows the information of the encryption libraries for the Android and iOS platforms. If you do not intend to use this function, you can remove the encryption libraries to decrease the SDK size.

   | Platform | Encryption libraries                          |
   | :------- | :-------------------------------------------- |
   | Android  | libagora-crypto.so                            |
   | iOS      | <ul><li>AgoraRtcCryptoLoader.framework <li>libcrypto.a</li></ul> |

## v2.2.1

v2.2.1 was released on Jul 23, 2019.

This release adds support for the arm64-v8a architecture on Android.

## v2.2.0

v2.2.0 was released on January 28, 2019. See below for new features and API changes.

### New Features

#### 1. Set the Voice-only Mode

Adds the `SetVoiceOnlyMode` method to set the voice-only mode. Once voice-only mode is enabled, the SDK transmits only the voice stream, but not other streams, like the sound of keyboard strokes. This function effectively optimizes the audio quality.

#### 2. Set the Audio Effect Playback Position

Adds the `SetRemoteVoicePosition` method to set the playback position and volume of the audio effects sent from the remote user.

#### 3. Enables/Disables the Local Audio Module

When an app joins a channel, the audio module is enabled by default. Added the `EnableLocalAudio` method to disable and re-enable the local audio capture, that is, to stop or restart local audio capturing and processing. The `OnMicrophoneEnabledHandler` callback is triggered once the local audio module is disabled or re-enabled. This method does not affect receiving or playing the remote audio streams, and is applicable to scenarios where the user wants to receive remote audio streams without sending any audio stream to other users in the channel.

#### 4. Sets Whether to Receive the Audio/Video Streams by Default

Added the `SetDefaultMuteAllRemoteAudioStreams` and `SetDefaultMuteAllRemoteVideoStreams` methods to set whether to receive the audio or video streams by default.

#### 5. Indicate the First Local Audio Frame is Received/Sent

Added the `OnFirstRemoteAudioFrameHandler` and `OnFirstLocalAudioFrameHandler` callbacks to indicate that the first remote audio frame is received or sent successfully.

#### 6. Indicate an API is Executed

Added the `OnApiExecutedHandler` callback to indicate that an API method is executed.

### API Changes

#### Added

- [EnableLocalAudio](../../en/API%20Reference/game_unity.md)
- [SetDefaultMuteAllRemoteAudioStreams](../../cn/API%20Reference/game_unity.md)
- [SetDefaultMuteAllRemoteVideoStreams](../../en/API%20Reference/game_unity.md)
- [OnMicrophoneEnabledHandler](../../en/API%20Reference/game_unity.md)
- [OnFirstRemoteAudioFrameHandler](../../en/API%20Reference/game_unity.md)
- [OnFirstLocalAudioFrameHandler](../../en/API%20Reference/game_unity.md)
- [OnApiExecutedHandler](../../cn/API%20Reference/game_unity.md)

#### Deleted

- `GetMediaEngineVersion`
- `GetParemeter`
- `Poll`

## v2.1.0

The version 2.1.0 was released on February 27, 2018. See below for new features, improvements, and issues fixed.

### New Features

<table>
<colgroup>
<col/>
<col/>
</colgroup>
<thead>
<tr><th>Function</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td>Set the Voice-only Mode</td>
<td>Added an interface to set the voice-only mode to optimize the audio quality.</td>
</tr>
<tr><td>Set the Audio Effect Position</td>
<td>Added an interface to set the position and volume of the audio effects sent from the remote user.</td>
</tr>
<tr><td>Set the Local Voice Pitch</td>
<td>Added an interface to set the pitch of the local voice.</td>
</tr>
<tr><td>Indicate a Role Change</td>
<td>Added a callback interface in the command mode to indicate a role change between the host and audience in the channel.</td>
</tr>
<tr><td>Indicate the Video is Muted</td>
<td>Added a callback interface to indicate whether the remote user muted/unmuted the video stream.</td>
</tr>
<tr><td>Update the API Names</td>
<td>Deleted <code>ForGaming</code> in all the API classes and enumerations.</td>
</tr>
</tbody>
</table>



### Improvements

<table>
<colgroup>
<col/>
<col/>
</colgroup>
<thead>
<tr><th>Improvement</th>
<th>Description</th>
</tr>
</thead>
<tbody>
<tr><td>Reduce the Bandwidth</td>
<td>
<ul>
<li>Before v2.1.0: If you muted the audio or video stream of the remote user, the network still sent the stream.</li>
<li>Starting from v2.1.0: If you mute the stream of the remote user, the network will not send the stream to reduce the bandwidth.</li>
</ul></td>
</tr>
</tbody>
</table>



### Fixed Issues

-   Occasional crashes under specific circumstances.
-   Unexpected behavior related to the audio routing.


## v2.0

The version 2.0 was released on August 26, 2017. See below for new features and issues fixed.

### New Features:

-   Added the video functions for Unity.
-   Added a function of adjusting the background music with the volume keys after the audio is muted.


### Fixed Issues:

-   Audio effect playback-related issues.
-   Low-volume with earphones on iOS devices.
-   Recording related issues on Android devices.


## v1.1

The version 1.1 was released on May 25, 2017.

- Native-iOS: Renamed the <code>joinChannel</code> API to <code>joinChannelByToken</code>.
- Native-iOS: Added the <code>startAudioRecording</code>and <code>stopAudioRecording</code> methods.
- Native-iOS: Added the <code>onWarning</code> message to indicate low recording volume.
- Native-Android: Added the <code>startAudioRecording</code> and <code>stopAudioRecording</code> methods.
- Android: Optimized the native <code>pause</code>/<code>resume</code> interfaces.
- Optimized the audio quality on certain cell phones.
- Fixed the recording noise when calling <code>startAudioRecording</code>.


## v1.0

The version 1.0 was released on May 3, 2017.

This is the first release of the SDK, which includes the following functions:

### 1. Provided the following APIs:


- Unity3D SDK (C# APIs).
- Cocos2d SDK (C++ APIs).
- Native SDK: Android (Java APIs) and iOS (Objective-C APIs).
- Multiple audio effect mixing, such as preload mode and panning.
- Virtual surround sound panning of each UID’s voice according to the in-game coordinates.
- Voice morphing feature.
- Noise block API: Block all non-vocal sounds to ensure a clear in-game chat.

### 2. Package size reduction.

### 3. Performance optimization.
