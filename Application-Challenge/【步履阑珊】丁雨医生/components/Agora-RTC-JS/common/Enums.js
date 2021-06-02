"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.AudienceLatencyLevelType = exports.VoiceBeautifierPreset = exports.AudioEffectPreset = exports.AudioSessionOperationRestriction = exports.RtmpStreamingEvent = exports.StreamSubscribeState = exports.StreamPublishState = exports.VideoCodecType = exports.AudioChannel = exports.WarningCode = exports.VideoStreamType = exports.VideoRenderMode = exports.VideoRemoteStateReason = exports.VideoRemoteState = exports.VideoQualityAdaptIndication = exports.VideoOutputOrientationMode = exports.VideoMirrorMode = exports.BitRate = exports.VideoFrameRate = exports.VideoCodecProfileType = exports.UserPriority = exports.UserOfflineReason = exports.StreamFallbackOptions = exports.RtmpStreamingState = exports.RtmpStreamingErrorCode = exports.NetworkType = exports.NetworkQuality = exports.LogFilter = exports.LocalVideoStreamState = exports.LocalVideoStreamError = exports.LighteningContrastLevel = exports.LastmileProbeResultState = exports.InjectStreamStatus = exports.ErrorCode = exports.EncryptionMode = exports.DegradationPreference = exports.ConnectionStateType = exports.ConnectionChangedReason = exports.ClientRole = exports.ChannelProfile = exports.ChannelMediaRelayState = exports.ChannelMediaRelayEvent = exports.ChannelMediaRelayError = exports.CameraDirection = exports.CameraCaptureOutputPreference = exports.AudioVoiceChanger = exports.AudioScenario = exports.AudioSampleRateType = exports.AudioReverbType = exports.AudioReverbPreset = exports.AudioRemoteStateReason = exports.AudioRemoteState = exports.AudioRecordingQuality = exports.AudioProfile = exports.AudioOutputRouting = exports.AudioMixingStateCode = exports.AudioMixingErrorCode = exports.AudioLocalState = exports.AudioLocalError = exports.AudioEqualizationBandFrequency = exports.AudioCodecProfileType = exports.AreaCode = void 0;

/**
 * Regions for connection.
 */
let AreaCode;
/**
 * Audio codec profile.
 */

exports.AreaCode = AreaCode;

(function (AreaCode) {
  AreaCode[AreaCode["CN"] = 1] = "CN";
  AreaCode[AreaCode["NA"] = 2] = "NA";
  AreaCode[AreaCode["EU"] = 4] = "EU";
  AreaCode[AreaCode["AS"] = 8] = "AS";
  AreaCode[AreaCode["JP"] = 16] = "JP";
  AreaCode[AreaCode["IN"] = 32] = "IN";
  AreaCode[AreaCode["GLOB"] = -1] = "GLOB";
})(AreaCode || (exports.AreaCode = AreaCode = {}));

let AudioCodecProfileType;
/**
 * Audio equalization band frequency.
 */

exports.AudioCodecProfileType = AudioCodecProfileType;

(function (AudioCodecProfileType) {
  AudioCodecProfileType[AudioCodecProfileType["LCAAC"] = 0] = "LCAAC";
  AudioCodecProfileType[AudioCodecProfileType["HEAAC"] = 1] = "HEAAC";
})(AudioCodecProfileType || (exports.AudioCodecProfileType = AudioCodecProfileType = {}));

let AudioEqualizationBandFrequency;
/**
 * The error information of the local audio.
 */

exports.AudioEqualizationBandFrequency = AudioEqualizationBandFrequency;

(function (AudioEqualizationBandFrequency) {
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band31"] = 0] = "Band31";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band62"] = 1] = "Band62";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band125"] = 2] = "Band125";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band250"] = 3] = "Band250";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band500"] = 4] = "Band500";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band1K"] = 5] = "Band1K";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band2K"] = 6] = "Band2K";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band4K"] = 7] = "Band4K";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band8K"] = 8] = "Band8K";
  AudioEqualizationBandFrequency[AudioEqualizationBandFrequency["Band16K"] = 9] = "Band16K";
})(AudioEqualizationBandFrequency || (exports.AudioEqualizationBandFrequency = AudioEqualizationBandFrequency = {}));

let AudioLocalError;
/**
 * The state of the local audio.
 */

exports.AudioLocalError = AudioLocalError;

(function (AudioLocalError) {
  AudioLocalError[AudioLocalError["Ok"] = 0] = "Ok";
  AudioLocalError[AudioLocalError["Failure"] = 1] = "Failure";
  AudioLocalError[AudioLocalError["DeviceNoPermission"] = 2] = "DeviceNoPermission";
  AudioLocalError[AudioLocalError["DeviceBusy"] = 3] = "DeviceBusy";
  AudioLocalError[AudioLocalError["RecordFailure"] = 4] = "RecordFailure";
  AudioLocalError[AudioLocalError["EncodeFailure"] = 5] = "EncodeFailure";
})(AudioLocalError || (exports.AudioLocalError = AudioLocalError = {}));

let AudioLocalState;
/**
 * The error code of the audio mixing file.
 */

exports.AudioLocalState = AudioLocalState;

(function (AudioLocalState) {
  AudioLocalState[AudioLocalState["Stopped"] = 0] = "Stopped";
  AudioLocalState[AudioLocalState["Recording"] = 1] = "Recording";
  AudioLocalState[AudioLocalState["Encoding"] = 2] = "Encoding";
  AudioLocalState[AudioLocalState["Failed"] = 3] = "Failed";
})(AudioLocalState || (exports.AudioLocalState = AudioLocalState = {}));

let AudioMixingErrorCode;
/**
 * The state of the audio mixing file.
 */

exports.AudioMixingErrorCode = AudioMixingErrorCode;

(function (AudioMixingErrorCode) {
  AudioMixingErrorCode[AudioMixingErrorCode["CanNotOpen"] = 701] = "CanNotOpen";
  AudioMixingErrorCode[AudioMixingErrorCode["TooFrequentCall"] = 702] = "TooFrequentCall";
  AudioMixingErrorCode[AudioMixingErrorCode["InterruptedEOF"] = 703] = "InterruptedEOF";
  AudioMixingErrorCode[AudioMixingErrorCode["OK"] = 0] = "OK";
})(AudioMixingErrorCode || (exports.AudioMixingErrorCode = AudioMixingErrorCode = {}));

let AudioMixingStateCode;
/**
 * Audio output routing.
 */

exports.AudioMixingStateCode = AudioMixingStateCode;

(function (AudioMixingStateCode) {
  AudioMixingStateCode[AudioMixingStateCode["Playing"] = 710] = "Playing";
  AudioMixingStateCode[AudioMixingStateCode["Paused"] = 711] = "Paused";
  AudioMixingStateCode[AudioMixingStateCode["Stopped"] = 713] = "Stopped";
  AudioMixingStateCode[AudioMixingStateCode["Failed"] = 714] = "Failed";
})(AudioMixingStateCode || (exports.AudioMixingStateCode = AudioMixingStateCode = {}));

let AudioOutputRouting;
/**
 * Audio profile.
 */

exports.AudioOutputRouting = AudioOutputRouting;

(function (AudioOutputRouting) {
  AudioOutputRouting[AudioOutputRouting["Default"] = -1] = "Default";
  AudioOutputRouting[AudioOutputRouting["Headset"] = 0] = "Headset";
  AudioOutputRouting[AudioOutputRouting["Earpiece"] = 1] = "Earpiece";
  AudioOutputRouting[AudioOutputRouting["HeadsetNoMic"] = 2] = "HeadsetNoMic";
  AudioOutputRouting[AudioOutputRouting["Speakerphone"] = 3] = "Speakerphone";
  AudioOutputRouting[AudioOutputRouting["Loudspeaker"] = 4] = "Loudspeaker";
  AudioOutputRouting[AudioOutputRouting["HeadsetBluetooth"] = 5] = "HeadsetBluetooth";
})(AudioOutputRouting || (exports.AudioOutputRouting = AudioOutputRouting = {}));

let AudioProfile;
/**
 * Audio recording quality.
 */

exports.AudioProfile = AudioProfile;

(function (AudioProfile) {
  AudioProfile[AudioProfile["Default"] = 0] = "Default";
  AudioProfile[AudioProfile["SpeechStandard"] = 1] = "SpeechStandard";
  AudioProfile[AudioProfile["MusicStandard"] = 2] = "MusicStandard";
  AudioProfile[AudioProfile["MusicStandardStereo"] = 3] = "MusicStandardStereo";
  AudioProfile[AudioProfile["MusicHighQuality"] = 4] = "MusicHighQuality";
  AudioProfile[AudioProfile["MusicHighQualityStereo"] = 5] = "MusicHighQualityStereo";
})(AudioProfile || (exports.AudioProfile = AudioProfile = {}));

let AudioRecordingQuality;
/**
 * The state of the remote audio.
 */

exports.AudioRecordingQuality = AudioRecordingQuality;

(function (AudioRecordingQuality) {
  AudioRecordingQuality[AudioRecordingQuality["Low"] = 0] = "Low";
  AudioRecordingQuality[AudioRecordingQuality["Medium"] = 1] = "Medium";
  AudioRecordingQuality[AudioRecordingQuality["High"] = 2] = "High";
})(AudioRecordingQuality || (exports.AudioRecordingQuality = AudioRecordingQuality = {}));

let AudioRemoteState;
/**
 * The reason of the remote audio state change.
 */

exports.AudioRemoteState = AudioRemoteState;

(function (AudioRemoteState) {
  AudioRemoteState[AudioRemoteState["Stopped"] = 0] = "Stopped";
  AudioRemoteState[AudioRemoteState["Starting"] = 1] = "Starting";
  AudioRemoteState[AudioRemoteState["Decoding"] = 2] = "Decoding";
  AudioRemoteState[AudioRemoteState["Frozen"] = 3] = "Frozen";
  AudioRemoteState[AudioRemoteState["Failed"] = 4] = "Failed";
})(AudioRemoteState || (exports.AudioRemoteState = AudioRemoteState = {}));

let AudioRemoteStateReason;
/**
 * The preset local voice reverberation option.
 */

exports.AudioRemoteStateReason = AudioRemoteStateReason;

(function (AudioRemoteStateReason) {
  AudioRemoteStateReason[AudioRemoteStateReason["Internal"] = 0] = "Internal";
  AudioRemoteStateReason[AudioRemoteStateReason["NetworkCongestion"] = 1] = "NetworkCongestion";
  AudioRemoteStateReason[AudioRemoteStateReason["NetworkRecovery"] = 2] = "NetworkRecovery";
  AudioRemoteStateReason[AudioRemoteStateReason["LocalMuted"] = 3] = "LocalMuted";
  AudioRemoteStateReason[AudioRemoteStateReason["LocalUnmuted"] = 4] = "LocalUnmuted";
  AudioRemoteStateReason[AudioRemoteStateReason["RemoteMuted"] = 5] = "RemoteMuted";
  AudioRemoteStateReason[AudioRemoteStateReason["RemoteUnmuted"] = 6] = "RemoteUnmuted";
  AudioRemoteStateReason[AudioRemoteStateReason["RemoteOffline"] = 7] = "RemoteOffline";
})(AudioRemoteStateReason || (exports.AudioRemoteStateReason = AudioRemoteStateReason = {}));

let AudioReverbPreset;
/**
 * Audio reverberation type.
 */

exports.AudioReverbPreset = AudioReverbPreset;

(function (AudioReverbPreset) {
  AudioReverbPreset[AudioReverbPreset["Off"] = 0] = "Off";
  AudioReverbPreset[AudioReverbPreset["Popular"] = 1] = "Popular";
  AudioReverbPreset[AudioReverbPreset["RnB"] = 2] = "RnB";
  AudioReverbPreset[AudioReverbPreset["Rock"] = 3] = "Rock";
  AudioReverbPreset[AudioReverbPreset["HipHop"] = 4] = "HipHop";
  AudioReverbPreset[AudioReverbPreset["VocalConcert"] = 5] = "VocalConcert";
  AudioReverbPreset[AudioReverbPreset["KTV"] = 6] = "KTV";
  AudioReverbPreset[AudioReverbPreset["Studio"] = 7] = "Studio";
  AudioReverbPreset[AudioReverbPreset["FX_KTV"] = 1048577] = "FX_KTV";
  AudioReverbPreset[AudioReverbPreset["FX_VOCAL_CONCERT"] = 1048578] = "FX_VOCAL_CONCERT";
  AudioReverbPreset[AudioReverbPreset["FX_UNCLE"] = 1048579] = "FX_UNCLE";
  AudioReverbPreset[AudioReverbPreset["FX_SISTER"] = 1048580] = "FX_SISTER";
  AudioReverbPreset[AudioReverbPreset["FX_STUDIO"] = 1048581] = "FX_STUDIO";
  AudioReverbPreset[AudioReverbPreset["FX_POPULAR"] = 1048582] = "FX_POPULAR";
  AudioReverbPreset[AudioReverbPreset["FX_RNB"] = 1048583] = "FX_RNB";
  AudioReverbPreset[AudioReverbPreset["FX_PHONOGRAPH"] = 1048584] = "FX_PHONOGRAPH";
  AudioReverbPreset[AudioReverbPreset["VIRTUAL_STEREO"] = 2097153] = "VIRTUAL_STEREO";
})(AudioReverbPreset || (exports.AudioReverbPreset = AudioReverbPreset = {}));

let AudioReverbType;
/**
 * Audio sample rate.
 */

exports.AudioReverbType = AudioReverbType;

(function (AudioReverbType) {
  AudioReverbType[AudioReverbType["DryLevel"] = 0] = "DryLevel";
  AudioReverbType[AudioReverbType["WetLevel"] = 1] = "WetLevel";
  AudioReverbType[AudioReverbType["RoomSize"] = 2] = "RoomSize";
  AudioReverbType[AudioReverbType["WetDelay"] = 3] = "WetDelay";
  AudioReverbType[AudioReverbType["Strength"] = 4] = "Strength";
})(AudioReverbType || (exports.AudioReverbType = AudioReverbType = {}));

let AudioSampleRateType;
/**
 * Audio scenario.
 */

exports.AudioSampleRateType = AudioSampleRateType;

(function (AudioSampleRateType) {
  AudioSampleRateType[AudioSampleRateType["Type32000"] = 32000] = "Type32000";
  AudioSampleRateType[AudioSampleRateType["Type44100"] = 44100] = "Type44100";
  AudioSampleRateType[AudioSampleRateType["Type48000"] = 48000] = "Type48000";
})(AudioSampleRateType || (exports.AudioSampleRateType = AudioSampleRateType = {}));

let AudioScenario;
/**
 * The preset audio voice configuration used to change the voice effect.
 */

exports.AudioScenario = AudioScenario;

(function (AudioScenario) {
  AudioScenario[AudioScenario["Default"] = 0] = "Default";
  AudioScenario[AudioScenario["ChatRoomEntertainment"] = 1] = "ChatRoomEntertainment";
  AudioScenario[AudioScenario["Education"] = 2] = "Education";
  AudioScenario[AudioScenario["GameStreaming"] = 3] = "GameStreaming";
  AudioScenario[AudioScenario["ShowRoom"] = 4] = "ShowRoom";
  AudioScenario[AudioScenario["ChatRoomGaming"] = 5] = "ChatRoomGaming";
  AudioScenario[AudioScenario["IOT"] = 6] = "IOT";
  AudioScenario[AudioScenario["MEETING"] = 8] = "MEETING";
})(AudioScenario || (exports.AudioScenario = AudioScenario = {}));

let AudioVoiceChanger;
/**
 * The camera capturer configuration.
 */

exports.AudioVoiceChanger = AudioVoiceChanger;

(function (AudioVoiceChanger) {
  AudioVoiceChanger[AudioVoiceChanger["Off"] = 0] = "Off";
  AudioVoiceChanger[AudioVoiceChanger["OldMan"] = 1] = "OldMan";
  AudioVoiceChanger[AudioVoiceChanger["BabyBoy"] = 2] = "BabyBoy";
  AudioVoiceChanger[AudioVoiceChanger["BabyGirl"] = 3] = "BabyGirl";
  AudioVoiceChanger[AudioVoiceChanger["ZhuBaJie"] = 4] = "ZhuBaJie";
  AudioVoiceChanger[AudioVoiceChanger["Ethereal"] = 5] = "Ethereal";
  AudioVoiceChanger[AudioVoiceChanger["Hulk"] = 6] = "Hulk";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_VIGOROUS"] = 1048577] = "BEAUTY_VIGOROUS";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_DEEP"] = 1048578] = "BEAUTY_DEEP";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_MELLOW"] = 1048579] = "BEAUTY_MELLOW";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_FALSETTO"] = 1048580] = "BEAUTY_FALSETTO";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_FULL"] = 1048581] = "BEAUTY_FULL";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_CLEAR"] = 1048582] = "BEAUTY_CLEAR";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_RESOUNDING"] = 1048583] = "BEAUTY_RESOUNDING";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_RINGING"] = 1048584] = "BEAUTY_RINGING";
  AudioVoiceChanger[AudioVoiceChanger["BEAUTY_SPACIAL"] = 1048585] = "BEAUTY_SPACIAL";
  AudioVoiceChanger[AudioVoiceChanger["GENERAL_BEAUTY_VOICE_MALE_MAGNETIC"] = 2097153] = "GENERAL_BEAUTY_VOICE_MALE_MAGNETIC";
  AudioVoiceChanger[AudioVoiceChanger["GENERAL_BEAUTY_VOICE_FEMALE_FRESH"] = 2097154] = "GENERAL_BEAUTY_VOICE_FEMALE_FRESH";
  AudioVoiceChanger[AudioVoiceChanger["GENERAL_BEAUTY_VOICE_FEMALE_VITALITY"] = 2097155] = "GENERAL_BEAUTY_VOICE_FEMALE_VITALITY";
})(AudioVoiceChanger || (exports.AudioVoiceChanger = AudioVoiceChanger = {}));

let CameraCaptureOutputPreference;
/**
 * The camera direction.
 */

exports.CameraCaptureOutputPreference = CameraCaptureOutputPreference;

(function (CameraCaptureOutputPreference) {
  CameraCaptureOutputPreference[CameraCaptureOutputPreference["Auto"] = 0] = "Auto";
  CameraCaptureOutputPreference[CameraCaptureOutputPreference["Performance"] = 1] = "Performance";
  CameraCaptureOutputPreference[CameraCaptureOutputPreference["Preview"] = 2] = "Preview";
  CameraCaptureOutputPreference[CameraCaptureOutputPreference["Unkown"] = 3] = "Unkown";
})(CameraCaptureOutputPreference || (exports.CameraCaptureOutputPreference = CameraCaptureOutputPreference = {}));

let CameraDirection;
/**
 * The error code in AgoraChannelMediaRelayError.
 */

exports.CameraDirection = CameraDirection;

(function (CameraDirection) {
  CameraDirection[CameraDirection["Rear"] = 0] = "Rear";
  CameraDirection[CameraDirection["Front"] = 1] = "Front";
})(CameraDirection || (exports.CameraDirection = CameraDirection = {}));

let ChannelMediaRelayError;
/**
 * The event code in `ChannelMediaRelayEvent`.
 */

exports.ChannelMediaRelayError = ChannelMediaRelayError;

(function (ChannelMediaRelayError) {
  ChannelMediaRelayError[ChannelMediaRelayError["None"] = 0] = "None";
  ChannelMediaRelayError[ChannelMediaRelayError["ServerErrorResponse"] = 1] = "ServerErrorResponse";
  ChannelMediaRelayError[ChannelMediaRelayError["ServerNoResponse"] = 2] = "ServerNoResponse";
  ChannelMediaRelayError[ChannelMediaRelayError["NoResourceAvailable"] = 3] = "NoResourceAvailable";
  ChannelMediaRelayError[ChannelMediaRelayError["FailedJoinSourceChannel"] = 4] = "FailedJoinSourceChannel";
  ChannelMediaRelayError[ChannelMediaRelayError["FailedJoinDestinationChannel"] = 5] = "FailedJoinDestinationChannel";
  ChannelMediaRelayError[ChannelMediaRelayError["FailedPacketReceivedFromSource"] = 6] = "FailedPacketReceivedFromSource";
  ChannelMediaRelayError[ChannelMediaRelayError["FailedPacketSentToDestination"] = 7] = "FailedPacketSentToDestination";
  ChannelMediaRelayError[ChannelMediaRelayError["ServerConnectionLost"] = 8] = "ServerConnectionLost";
  ChannelMediaRelayError[ChannelMediaRelayError["InternalError"] = 9] = "InternalError";
  ChannelMediaRelayError[ChannelMediaRelayError["SourceTokenExpired"] = 10] = "SourceTokenExpired";
  ChannelMediaRelayError[ChannelMediaRelayError["DestinationTokenExpired"] = 11] = "DestinationTokenExpired";
})(ChannelMediaRelayError || (exports.ChannelMediaRelayError = ChannelMediaRelayError = {}));

let ChannelMediaRelayEvent;
/**
 * The state code in [`ChannelMediaRelayState`]{@link ChannelMediaRelayState}.
 */

exports.ChannelMediaRelayEvent = ChannelMediaRelayEvent;

(function (ChannelMediaRelayEvent) {
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["Disconnect"] = 0] = "Disconnect";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["Connected"] = 1] = "Connected";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["JoinedSourceChannel"] = 2] = "JoinedSourceChannel";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["JoinedDestinationChannel"] = 3] = "JoinedDestinationChannel";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["SentToDestinationChannel"] = 4] = "SentToDestinationChannel";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["ReceivedVideoPacketFromSource"] = 5] = "ReceivedVideoPacketFromSource";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["ReceivedAudioPacketFromSource"] = 6] = "ReceivedAudioPacketFromSource";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["UpdateDestinationChannel"] = 7] = "UpdateDestinationChannel";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["UpdateDestinationChannelRefused"] = 8] = "UpdateDestinationChannelRefused";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["UpdateDestinationChannelNotChange"] = 9] = "UpdateDestinationChannelNotChange";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["UpdateDestinationChannelIsNil"] = 10] = "UpdateDestinationChannelIsNil";
  ChannelMediaRelayEvent[ChannelMediaRelayEvent["VideoProfileUpdate"] = 11] = "VideoProfileUpdate";
})(ChannelMediaRelayEvent || (exports.ChannelMediaRelayEvent = ChannelMediaRelayEvent = {}));

let ChannelMediaRelayState;
/**
 * Channel profile.
 */

exports.ChannelMediaRelayState = ChannelMediaRelayState;

(function (ChannelMediaRelayState) {
  ChannelMediaRelayState[ChannelMediaRelayState["Idle"] = 0] = "Idle";
  ChannelMediaRelayState[ChannelMediaRelayState["Connecting"] = 1] = "Connecting";
  ChannelMediaRelayState[ChannelMediaRelayState["Running"] = 2] = "Running";
  ChannelMediaRelayState[ChannelMediaRelayState["Failure"] = 3] = "Failure";
})(ChannelMediaRelayState || (exports.ChannelMediaRelayState = ChannelMediaRelayState = {}));

let ChannelProfile;
/**
 * Client role in the `LiveBroadcasting` profile.
 */

exports.ChannelProfile = ChannelProfile;

(function (ChannelProfile) {
  ChannelProfile[ChannelProfile["Communication"] = 0] = "Communication";
  ChannelProfile[ChannelProfile["LiveBroadcasting"] = 1] = "LiveBroadcasting";
  ChannelProfile[ChannelProfile["Game"] = 2] = "Game";
})(ChannelProfile || (exports.ChannelProfile = ChannelProfile = {}));

let ClientRole;
/**
 * Reasons for the connection state change.
 */

exports.ClientRole = ClientRole;

(function (ClientRole) {
  ClientRole[ClientRole["Broadcaster"] = 1] = "Broadcaster";
  ClientRole[ClientRole["Audience"] = 2] = "Audience";
})(ClientRole || (exports.ClientRole = ClientRole = {}));

let ConnectionChangedReason;
/**
 * Connection states.
 */

exports.ConnectionChangedReason = ConnectionChangedReason;

(function (ConnectionChangedReason) {
  ConnectionChangedReason[ConnectionChangedReason["Connecting"] = 0] = "Connecting";
  ConnectionChangedReason[ConnectionChangedReason["JoinSuccess"] = 1] = "JoinSuccess";
  ConnectionChangedReason[ConnectionChangedReason["Interrupted"] = 2] = "Interrupted";
  ConnectionChangedReason[ConnectionChangedReason["BannedByServer"] = 3] = "BannedByServer";
  ConnectionChangedReason[ConnectionChangedReason["JoinFailed"] = 4] = "JoinFailed";
  ConnectionChangedReason[ConnectionChangedReason["LeaveChannel"] = 5] = "LeaveChannel";
  ConnectionChangedReason[ConnectionChangedReason["InvalidAppId"] = 6] = "InvalidAppId";
  ConnectionChangedReason[ConnectionChangedReason["InvalidChannelName"] = 7] = "InvalidChannelName";
  ConnectionChangedReason[ConnectionChangedReason["InvalidToken"] = 8] = "InvalidToken";
  ConnectionChangedReason[ConnectionChangedReason["TokenExpired"] = 9] = "TokenExpired";
  ConnectionChangedReason[ConnectionChangedReason["RejectedByServer"] = 10] = "RejectedByServer";
  ConnectionChangedReason[ConnectionChangedReason["SettingProxyServer"] = 11] = "SettingProxyServer";
  ConnectionChangedReason[ConnectionChangedReason["RenewToken"] = 12] = "RenewToken";
  ConnectionChangedReason[ConnectionChangedReason["ClientIpAddressChanged"] = 13] = "ClientIpAddressChanged";
  ConnectionChangedReason[ConnectionChangedReason["KeepAliveTimeout"] = 14] = "KeepAliveTimeout";
})(ConnectionChangedReason || (exports.ConnectionChangedReason = ConnectionChangedReason = {}));

let ConnectionStateType;
/**
 * The video encoding degradation preference under limited bandwidth.
 */

exports.ConnectionStateType = ConnectionStateType;

(function (ConnectionStateType) {
  ConnectionStateType[ConnectionStateType["Disconnected"] = 1] = "Disconnected";
  ConnectionStateType[ConnectionStateType["Connecting"] = 2] = "Connecting";
  ConnectionStateType[ConnectionStateType["Connected"] = 3] = "Connected";
  ConnectionStateType[ConnectionStateType["Reconnecting"] = 4] = "Reconnecting";
  ConnectionStateType[ConnectionStateType["Failed"] = 5] = "Failed";
})(ConnectionStateType || (exports.ConnectionStateType = ConnectionStateType = {}));

let DegradationPreference;
/**
 * Encryption mode.
 */

exports.DegradationPreference = DegradationPreference;

(function (DegradationPreference) {
  DegradationPreference[DegradationPreference["MaintainQuality"] = 0] = "MaintainQuality";
  DegradationPreference[DegradationPreference["MaintainFramerate"] = 1] = "MaintainFramerate";
  DegradationPreference[DegradationPreference["Balanced"] = 2] = "Balanced";
})(DegradationPreference || (exports.DegradationPreference = DegradationPreference = {}));

let EncryptionMode;
/**
 * Error codes occur when the SDK encounters an error that cannot be recovered automatically without any app intervention.
 */

exports.EncryptionMode = EncryptionMode;

(function (EncryptionMode) {
  EncryptionMode[EncryptionMode["None"] = 0] = "None";
  EncryptionMode[EncryptionMode["AES128XTS"] = 1] = "AES128XTS";
  EncryptionMode[EncryptionMode["AES128ECB"] = 2] = "AES128ECB";
  EncryptionMode[EncryptionMode["AES256XTS"] = 3] = "AES256XTS";
  EncryptionMode[EncryptionMode["SM4128ECB"] = 4] = "SM4128ECB";
})(EncryptionMode || (exports.EncryptionMode = EncryptionMode = {}));

let ErrorCode;
/**
 * State of importing an external video stream in a live broadcast.
 */

exports.ErrorCode = ErrorCode;

(function (ErrorCode) {
  ErrorCode[ErrorCode["NoError"] = 0] = "NoError";
  ErrorCode[ErrorCode["Failed"] = 1] = "Failed";
  ErrorCode[ErrorCode["InvalidArgument"] = 2] = "InvalidArgument";
  ErrorCode[ErrorCode["NotReady"] = 3] = "NotReady";
  ErrorCode[ErrorCode["NotSupported"] = 4] = "NotSupported";
  ErrorCode[ErrorCode["Refused"] = 5] = "Refused";
  ErrorCode[ErrorCode["BufferTooSmall"] = 6] = "BufferTooSmall";
  ErrorCode[ErrorCode["NotInitialized"] = 7] = "NotInitialized";
  ErrorCode[ErrorCode["NoPermission"] = 9] = "NoPermission";
  ErrorCode[ErrorCode["TimedOut"] = 10] = "TimedOut";
  ErrorCode[ErrorCode["Canceled"] = 11] = "Canceled";
  ErrorCode[ErrorCode["TooOften"] = 12] = "TooOften";
  ErrorCode[ErrorCode["BindSocket"] = 13] = "BindSocket";
  ErrorCode[ErrorCode["NetDown"] = 14] = "NetDown";
  ErrorCode[ErrorCode["NoBufs"] = 15] = "NoBufs";
  ErrorCode[ErrorCode["JoinChannelRejected"] = 17] = "JoinChannelRejected";
  ErrorCode[ErrorCode["LeaveChannelRejected"] = 18] = "LeaveChannelRejected";
  ErrorCode[ErrorCode["AlreadyInUse"] = 19] = "AlreadyInUse";
  ErrorCode[ErrorCode["Abort"] = 20] = "Abort";
  ErrorCode[ErrorCode["InitNetEngine"] = 21] = "InitNetEngine";
  ErrorCode[ErrorCode["ResourceLimited"] = 22] = "ResourceLimited";
  ErrorCode[ErrorCode["InvalidAppId"] = 101] = "InvalidAppId";
  ErrorCode[ErrorCode["InvalidChannelId"] = 102] = "InvalidChannelId";
  ErrorCode[ErrorCode["NoServerResources"] = 103] = "NoServerResources";
  ErrorCode[ErrorCode["TokenExpired"] = 109] = "TokenExpired";
  ErrorCode[ErrorCode["InvalidToken"] = 110] = "InvalidToken";
  ErrorCode[ErrorCode["ConnectionInterrupted"] = 111] = "ConnectionInterrupted";
  ErrorCode[ErrorCode["ConnectionLost"] = 112] = "ConnectionLost";
  ErrorCode[ErrorCode["NotInChannel"] = 113] = "NotInChannel";
  ErrorCode[ErrorCode["SizeTooLarge"] = 114] = "SizeTooLarge";
  ErrorCode[ErrorCode["BitrateLimit"] = 115] = "BitrateLimit";
  ErrorCode[ErrorCode["TooManyDataStreams"] = 116] = "TooManyDataStreams";
  ErrorCode[ErrorCode["DecryptionFailed"] = 120] = "DecryptionFailed";
  ErrorCode[ErrorCode["ClientIsBannedByServer"] = 123] = "ClientIsBannedByServer";
  ErrorCode[ErrorCode["WatermarkParam"] = 124] = "WatermarkParam";
  ErrorCode[ErrorCode["WatermarkPath"] = 125] = "WatermarkPath";
  ErrorCode[ErrorCode["WatermarkPng"] = 126] = "WatermarkPng";
  ErrorCode[ErrorCode["WatermarkInfo"] = 127] = "WatermarkInfo";
  ErrorCode[ErrorCode["WatermarkAGRB"] = 128] = "WatermarkAGRB";
  ErrorCode[ErrorCode["WatermarkRead"] = 129] = "WatermarkRead";
  ErrorCode[ErrorCode["EncryptedStreamNotAllowedPublish"] = 130] = "EncryptedStreamNotAllowedPublish";
  ErrorCode[ErrorCode["InvalidUserAccount"] = 134] = "InvalidUserAccount";
  ErrorCode[ErrorCode["PublishStreamCDNError"] = 151] = "PublishStreamCDNError";
  ErrorCode[ErrorCode["PublishStreamNumReachLimit"] = 152] = "PublishStreamNumReachLimit";
  ErrorCode[ErrorCode["PublishStreamNotAuthorized"] = 153] = "PublishStreamNotAuthorized";
  ErrorCode[ErrorCode["PublishStreamInternalServerError"] = 154] = "PublishStreamInternalServerError";
  ErrorCode[ErrorCode["PublishStreamNotFound"] = 155] = "PublishStreamNotFound";
  ErrorCode[ErrorCode["PublishStreamFormatNotSuppported"] = 156] = "PublishStreamFormatNotSuppported";
  ErrorCode[ErrorCode["LoadMediaEngine"] = 1001] = "LoadMediaEngine";
  ErrorCode[ErrorCode["StartCall"] = 1002] = "StartCall";
  ErrorCode[ErrorCode["StartCamera"] = 1003] = "StartCamera";
  ErrorCode[ErrorCode["StartVideoRender"] = 1004] = "StartVideoRender";
  ErrorCode[ErrorCode["AdmGeneralError"] = 1005] = "AdmGeneralError";
  ErrorCode[ErrorCode["AdmJavaResource"] = 1006] = "AdmJavaResource";
  ErrorCode[ErrorCode["AdmSampleRate"] = 1007] = "AdmSampleRate";
  ErrorCode[ErrorCode["AdmInitPlayout"] = 1008] = "AdmInitPlayout";
  ErrorCode[ErrorCode["AdmStartPlayout"] = 1009] = "AdmStartPlayout";
  ErrorCode[ErrorCode["AdmStopPlayout"] = 1010] = "AdmStopPlayout";
  ErrorCode[ErrorCode["AdmInitRecording"] = 1011] = "AdmInitRecording";
  ErrorCode[ErrorCode["AdmStartRecording"] = 1012] = "AdmStartRecording";
  ErrorCode[ErrorCode["AdmStopRecording"] = 1013] = "AdmStopRecording";
  ErrorCode[ErrorCode["AdmRuntimePlayoutError"] = 1015] = "AdmRuntimePlayoutError";
  ErrorCode[ErrorCode["AdmRuntimeRecordingError"] = 1017] = "AdmRuntimeRecordingError";
  ErrorCode[ErrorCode["AdmRecordAudioFailed"] = 1018] = "AdmRecordAudioFailed";
  ErrorCode[ErrorCode["AdmPlayAbnormalFrequency"] = 1020] = "AdmPlayAbnormalFrequency";
  ErrorCode[ErrorCode["AdmRecordAbnormalFrequency"] = 1021] = "AdmRecordAbnormalFrequency";
  ErrorCode[ErrorCode["AdmInitLoopback"] = 1022] = "AdmInitLoopback";
  ErrorCode[ErrorCode["AdmStartLoopback"] = 1023] = "AdmStartLoopback";
  ErrorCode[ErrorCode["AdmNoPermission"] = 1027] = "AdmNoPermission";
  ErrorCode[ErrorCode["AudioBtScoFailed"] = 1030] = "AudioBtScoFailed";
  ErrorCode[ErrorCode["AdmNoRecordingDevice"] = 1359] = "AdmNoRecordingDevice";
  ErrorCode[ErrorCode["AdmNoPlayoutDevice"] = 1360] = "AdmNoPlayoutDevice";
  ErrorCode[ErrorCode["VdmCameraNotAuthorized"] = 1501] = "VdmCameraNotAuthorized";
  ErrorCode[ErrorCode["VcmUnknownError"] = 1600] = "VcmUnknownError";
  ErrorCode[ErrorCode["VcmEncoderInitError"] = 1601] = "VcmEncoderInitError";
  ErrorCode[ErrorCode["VcmEncoderEncodeError"] = 1602] = "VcmEncoderEncodeError";
  ErrorCode[ErrorCode["VcmEncoderSetError"] = 1603] = "VcmEncoderSetError";
})(ErrorCode || (exports.ErrorCode = ErrorCode = {}));

let InjectStreamStatus;
/**
 * The state of the probe test result.
 */

exports.InjectStreamStatus = InjectStreamStatus;

(function (InjectStreamStatus) {
  InjectStreamStatus[InjectStreamStatus["StartSuccess"] = 0] = "StartSuccess";
  InjectStreamStatus[InjectStreamStatus["StartAlreadyExists"] = 1] = "StartAlreadyExists";
  InjectStreamStatus[InjectStreamStatus["StartUnauthorized"] = 2] = "StartUnauthorized";
  InjectStreamStatus[InjectStreamStatus["StartTimedout"] = 3] = "StartTimedout";
  InjectStreamStatus[InjectStreamStatus["StartFailed"] = 4] = "StartFailed";
  InjectStreamStatus[InjectStreamStatus["StopSuccess"] = 5] = "StopSuccess";
  InjectStreamStatus[InjectStreamStatus["StopNotFound"] = 6] = "StopNotFound";
  InjectStreamStatus[InjectStreamStatus["StopUnauthorized"] = 7] = "StopUnauthorized";
  InjectStreamStatus[InjectStreamStatus["StopTimedout"] = 8] = "StopTimedout";
  InjectStreamStatus[InjectStreamStatus["StopFailed"] = 9] = "StopFailed";
  InjectStreamStatus[InjectStreamStatus["Broken"] = 10] = "Broken";
})(InjectStreamStatus || (exports.InjectStreamStatus = InjectStreamStatus = {}));

let LastmileProbeResultState;
/**
 * The lightening contrast level.
 */

exports.LastmileProbeResultState = LastmileProbeResultState;

(function (LastmileProbeResultState) {
  LastmileProbeResultState[LastmileProbeResultState["Complete"] = 1] = "Complete";
  LastmileProbeResultState[LastmileProbeResultState["IncompleteNoBwe"] = 2] = "IncompleteNoBwe";
  LastmileProbeResultState[LastmileProbeResultState["Unavailable"] = 3] = "Unavailable";
})(LastmileProbeResultState || (exports.LastmileProbeResultState = LastmileProbeResultState = {}));

let LighteningContrastLevel;
/**
 * The detailed error information of the local video.
 */

exports.LighteningContrastLevel = LighteningContrastLevel;

(function (LighteningContrastLevel) {
  LighteningContrastLevel[LighteningContrastLevel["Low"] = 0] = "Low";
  LighteningContrastLevel[LighteningContrastLevel["Normal"] = 1] = "Normal";
  LighteningContrastLevel[LighteningContrastLevel["High"] = 2] = "High";
})(LighteningContrastLevel || (exports.LighteningContrastLevel = LighteningContrastLevel = {}));

let LocalVideoStreamError;
/**
 * The state of the local video stream.
 */

exports.LocalVideoStreamError = LocalVideoStreamError;

(function (LocalVideoStreamError) {
  LocalVideoStreamError[LocalVideoStreamError["OK"] = 0] = "OK";
  LocalVideoStreamError[LocalVideoStreamError["Failure"] = 1] = "Failure";
  LocalVideoStreamError[LocalVideoStreamError["DeviceNoPermission"] = 2] = "DeviceNoPermission";
  LocalVideoStreamError[LocalVideoStreamError["DeviceBusy"] = 3] = "DeviceBusy";
  LocalVideoStreamError[LocalVideoStreamError["CaptureFailure"] = 4] = "CaptureFailure";
  LocalVideoStreamError[LocalVideoStreamError["EncodeFailure"] = 5] = "EncodeFailure";
})(LocalVideoStreamError || (exports.LocalVideoStreamError = LocalVideoStreamError = {}));

let LocalVideoStreamState;
/**
 * Output log filter level.
 */

exports.LocalVideoStreamState = LocalVideoStreamState;

(function (LocalVideoStreamState) {
  LocalVideoStreamState[LocalVideoStreamState["Stopped"] = 0] = "Stopped";
  LocalVideoStreamState[LocalVideoStreamState["Capturing"] = 1] = "Capturing";
  LocalVideoStreamState[LocalVideoStreamState["Encoding"] = 2] = "Encoding";
  LocalVideoStreamState[LocalVideoStreamState["Failed"] = 3] = "Failed";
})(LocalVideoStreamState || (exports.LocalVideoStreamState = LocalVideoStreamState = {}));

let LogFilter;
/**
 * Network quality.
 */

exports.LogFilter = LogFilter;

(function (LogFilter) {
  LogFilter[LogFilter["Off"] = 0] = "Off";
  LogFilter[LogFilter["Debug"] = 2063] = "Debug";
  LogFilter[LogFilter["Info"] = 15] = "Info";
  LogFilter[LogFilter["Warning"] = 14] = "Warning";
  LogFilter[LogFilter["Error"] = 12] = "Error";
  LogFilter[LogFilter["Critical"] = 8] = "Critical";
})(LogFilter || (exports.LogFilter = LogFilter = {}));

let NetworkQuality;
/**
 * Network type.
 */

exports.NetworkQuality = NetworkQuality;

(function (NetworkQuality) {
  NetworkQuality[NetworkQuality["Unknown"] = 0] = "Unknown";
  NetworkQuality[NetworkQuality["Excellent"] = 1] = "Excellent";
  NetworkQuality[NetworkQuality["Good"] = 2] = "Good";
  NetworkQuality[NetworkQuality["Poor"] = 3] = "Poor";
  NetworkQuality[NetworkQuality["Bad"] = 4] = "Bad";
  NetworkQuality[NetworkQuality["VBad"] = 5] = "VBad";
  NetworkQuality[NetworkQuality["Down"] = 6] = "Down";
  NetworkQuality[NetworkQuality["Unsupported"] = 7] = "Unsupported";
  NetworkQuality[NetworkQuality["Detecting"] = 8] = "Detecting";
})(NetworkQuality || (exports.NetworkQuality = NetworkQuality = {}));

let NetworkType;
/**
 * The detailed error information for streaming.
 */

exports.NetworkType = NetworkType;

(function (NetworkType) {
  NetworkType[NetworkType["Unknown"] = -1] = "Unknown";
  NetworkType[NetworkType["Disconnected"] = 0] = "Disconnected";
  NetworkType[NetworkType["LAN"] = 1] = "LAN";
  NetworkType[NetworkType["WIFI"] = 2] = "WIFI";
  NetworkType[NetworkType["Mobile2G"] = 3] = "Mobile2G";
  NetworkType[NetworkType["Mobile3G"] = 4] = "Mobile3G";
  NetworkType[NetworkType["Mobile4G"] = 5] = "Mobile4G";
})(NetworkType || (exports.NetworkType = NetworkType = {}));

let RtmpStreamingErrorCode;
/**
 * The RTMP or RTMPS streaming state.
 */

exports.RtmpStreamingErrorCode = RtmpStreamingErrorCode;

(function (RtmpStreamingErrorCode) {
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["OK"] = 0] = "OK";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["InvalidParameters"] = 1] = "InvalidParameters";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["EncryptedStreamNotAllowed"] = 2] = "EncryptedStreamNotAllowed";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["ConnectionTimeout"] = 3] = "ConnectionTimeout";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["InternalServerError"] = 4] = "InternalServerError";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["RtmpServerError"] = 5] = "RtmpServerError";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["TooOften"] = 6] = "TooOften";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["ReachLimit"] = 7] = "ReachLimit";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["NotAuthorized"] = 8] = "NotAuthorized";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["StreamNotFound"] = 9] = "StreamNotFound";
  RtmpStreamingErrorCode[RtmpStreamingErrorCode["FormatNotSupported"] = 10] = "FormatNotSupported";
})(RtmpStreamingErrorCode || (exports.RtmpStreamingErrorCode = RtmpStreamingErrorCode = {}));

let RtmpStreamingState;
/**
 * Stream fallback option.
 */

exports.RtmpStreamingState = RtmpStreamingState;

(function (RtmpStreamingState) {
  RtmpStreamingState[RtmpStreamingState["Idle"] = 0] = "Idle";
  RtmpStreamingState[RtmpStreamingState["Connecting"] = 1] = "Connecting";
  RtmpStreamingState[RtmpStreamingState["Running"] = 2] = "Running";
  RtmpStreamingState[RtmpStreamingState["Recovering"] = 3] = "Recovering";
  RtmpStreamingState[RtmpStreamingState["Failure"] = 4] = "Failure";
})(RtmpStreamingState || (exports.RtmpStreamingState = RtmpStreamingState = {}));

let StreamFallbackOptions;
/**
 * Reason for the user being offline.
 */

exports.StreamFallbackOptions = StreamFallbackOptions;

(function (StreamFallbackOptions) {
  StreamFallbackOptions[StreamFallbackOptions["Disabled"] = 0] = "Disabled";
  StreamFallbackOptions[StreamFallbackOptions["VideoStreamLow"] = 1] = "VideoStreamLow";
  StreamFallbackOptions[StreamFallbackOptions["AudioOnly"] = 2] = "AudioOnly";
})(StreamFallbackOptions || (exports.StreamFallbackOptions = StreamFallbackOptions = {}));

let UserOfflineReason;
/**
 * The priority of the remote user.
 */

exports.UserOfflineReason = UserOfflineReason;

(function (UserOfflineReason) {
  UserOfflineReason[UserOfflineReason["Quit"] = 0] = "Quit";
  UserOfflineReason[UserOfflineReason["Dropped"] = 1] = "Dropped";
  UserOfflineReason[UserOfflineReason["BecomeAudience"] = 2] = "BecomeAudience";
})(UserOfflineReason || (exports.UserOfflineReason = UserOfflineReason = {}));

let UserPriority;
/**
 * Self-defined video codec profile.
 */

exports.UserPriority = UserPriority;

(function (UserPriority) {
  UserPriority[UserPriority["High"] = 50] = "High";
  UserPriority[UserPriority["Normal"] = 100] = "Normal";
})(UserPriority || (exports.UserPriority = UserPriority = {}));

let VideoCodecProfileType;
/**
 * Video frame rate.
 */

exports.VideoCodecProfileType = VideoCodecProfileType;

(function (VideoCodecProfileType) {
  VideoCodecProfileType[VideoCodecProfileType["BaseLine"] = 66] = "BaseLine";
  VideoCodecProfileType[VideoCodecProfileType["Main"] = 77] = "Main";
  VideoCodecProfileType[VideoCodecProfileType["High"] = 100] = "High";
})(VideoCodecProfileType || (exports.VideoCodecProfileType = VideoCodecProfileType = {}));

let VideoFrameRate;
/**
 * Bitrate of the video (Kbps). Refer to the table below and set your bitrate.
 * If you set a bitrate beyond the proper range, the SDK automatically adjusts it to a value within the range.
 *
 * **Video Bitrate Table**
 * <table>
 *     <tr>
 *         <th>Resolution</th>
 *         <th>Frame rate<p>(fps)</th>
 *         <th>Base Bitrate<p>(Kbps, for Communication)</th>
 *         <th>Live Bitrate<p>(Kbps, for Live Broadcasting)</th>
 *     </tr>
 *     <tr>
 *         <td>160*120</td>
 *         <td>15</td>
 *         <td>65</td>
 *         <td>130</td>
 *     </tr>
 *     <tr>
 *         <td>120*120</td>
 *         <td>15</td>
 *         <td>50</td>
 *         <td>100</td>
 *     </tr>
 *     <tr>
 *         <td>320*180</td>
 *         <td>15</td>
 *         <td>140</td>
 *         <td>280</td>
 *     </tr>
 *     <tr>
 *         <td>180*180</td>
 *         <td>15</td>
 *         <td>100</td>
 *         <td>200</td>
 *     </tr>
 *     <tr>
 *         <td>240*180</td>
 *         <td>15</td>
 *         <td>120</td>
 *         <td>240</td>
 *     </tr>
 *     <tr>
 *         <td>320*240</td>
 *         <td>15</td>
 *         <td>200</td>
 *         <td>400</td>
 *     </tr>
 *     <tr>
 *         <td>240*240</td>
 *         <td>15</td>
 *         <td>140</td>
 *         <td>280</td>
 *     </tr>
 *     <tr>
 *         <td>424*240</td>
 *         <td>15</td>
 *         <td>220</td>
 *         <td>440</td>
 *     </tr>
 *     <tr>
 *         <td>640*360</td>
 *         <td>15</td>
 *         <td>400</td>
 *         <td>800</td>
 *     </tr>
 *     <tr>
 *         <td>360*360</td>
 *         <td>15</td>
 *         <td>260</td>
 *         <td>520</td>
 *     </tr>
 *     <tr>
 *         <td>640*360</td>
 *         <td>30</td>
 *         <td>600</td>
 *         <td>1200</td>
 *     </tr>
 *     <tr>
 *         <td>360*360</td>
 *         <td>30</td>
 *         <td>400</td>
 *         <td>800</td>
 *     </tr>
 *     <tr>
 *         <td>480*360</td>
 *         <td>15</td>
 *         <td>320</td>
 *         <td>640</td>
 *     </tr>
 *     <tr>
 *         <td>480*360</td>
 *         <td>30</td>
 *         <td>490</td>
 *         <td>980</td>
 *     </tr>
 *     <tr>
 *         <td>640*480</td>
 *         <td>15</td>
 *         <td>500</td>
 *         <td>1000</td>
 *     </tr>
 *     <tr>
 *         <td>480*480</td>
 *         <td>15</td>
 *         <td>400</td>
 *         <td>800</td>
 *     </tr>
 *     <tr>
 *         <td>640*480</td>
 *         <td>30</td>
 *         <td>750</td>
 *         <td>1500</td>
 *     </tr>
 *     <tr>
 *         <td>480*480</td>
 *         <td>30</td>
 *         <td>600</td>
 *         <td>1200</td>
 *     </tr>
 *     <tr>
 *         <td>848*480</td>
 *         <td>15</td>
 *         <td>610</td>
 *         <td>1220</td>
 *     </tr>
 *     <tr>
 *         <td>848*480</td>
 *         <td>30</td>
 *         <td>930</td>
 *         <td>1860</td>
 *     </tr>
 *     <tr>
 *         <td>640*480</td>
 *         <td>10</td>
 *         <td>400</td>
 *         <td>800</td>
 *     </tr>
 *     <tr>
 *         <td>1280*720</td>
 *         <td>15</td>
 *         <td>1130</td>
 *         <td>2260</td>
 *     </tr>
 *     <tr>
 *         <td>1280*720</td>
 *         <td>30</td>
 *         <td>1710</td>
 *         <td>3420</td>
 *     </tr>
 *     <tr>
 *         <td>960*720</td>
 *         <td>15</td>
 *         <td>910</td>
 *         <td>1820</td>
 *     </tr>
 *     <tr>
 *         <td>960*720</td>
 *         <td>30</td>
 *         <td>1380</td>
 *         <td>2760</td>
 *     </tr>
 * </table>
 *
 * Agora uses different video codecs for different profiles to optimize the user experience. For example,
 * the Communication profile prioritizes the smoothness while the LIVE_BROADCASTING profile prioritizes the
 * video quality (a higher bitrate). Therefore, We recommend setting this parameter as STANDARD_BITRATE = 0.
 */

exports.VideoFrameRate = VideoFrameRate;

(function (VideoFrameRate) {
  VideoFrameRate[VideoFrameRate["Min"] = -1] = "Min";
  VideoFrameRate[VideoFrameRate["Fps1"] = 1] = "Fps1";
  VideoFrameRate[VideoFrameRate["Fps7"] = 7] = "Fps7";
  VideoFrameRate[VideoFrameRate["Fps10"] = 10] = "Fps10";
  VideoFrameRate[VideoFrameRate["Fps15"] = 15] = "Fps15";
  VideoFrameRate[VideoFrameRate["Fps24"] = 24] = "Fps24";
  VideoFrameRate[VideoFrameRate["Fps30"] = 30] = "Fps30";
  VideoFrameRate[VideoFrameRate["Fps60"] = 60] = "Fps60";
})(VideoFrameRate || (exports.VideoFrameRate = VideoFrameRate = {}));

let BitRate;
/**
 * Video mirror mode.
 */

exports.BitRate = BitRate;

(function (BitRate) {
  BitRate[BitRate["Standard"] = 0] = "Standard";
  BitRate[BitRate["Compatible"] = -1] = "Compatible";
})(BitRate || (exports.BitRate = BitRate = {}));

let VideoMirrorMode;
/**
 * Video output orientation mode.
 */

exports.VideoMirrorMode = VideoMirrorMode;

(function (VideoMirrorMode) {
  VideoMirrorMode[VideoMirrorMode["Auto"] = 0] = "Auto";
  VideoMirrorMode[VideoMirrorMode["Enabled"] = 1] = "Enabled";
  VideoMirrorMode[VideoMirrorMode["Disabled"] = 2] = "Disabled";
})(VideoMirrorMode || (exports.VideoMirrorMode = VideoMirrorMode = {}));

let VideoOutputOrientationMode;
/**
 * Quality change of the local video in terms of target frame rate and target bit rate since last count.
 */

exports.VideoOutputOrientationMode = VideoOutputOrientationMode;

(function (VideoOutputOrientationMode) {
  VideoOutputOrientationMode[VideoOutputOrientationMode["Adaptative"] = 0] = "Adaptative";
  VideoOutputOrientationMode[VideoOutputOrientationMode["FixedLandscape"] = 1] = "FixedLandscape";
  VideoOutputOrientationMode[VideoOutputOrientationMode["FixedPortrait"] = 2] = "FixedPortrait";
})(VideoOutputOrientationMode || (exports.VideoOutputOrientationMode = VideoOutputOrientationMode = {}));

let VideoQualityAdaptIndication;
/**
 * The state of the remote video.
 */

exports.VideoQualityAdaptIndication = VideoQualityAdaptIndication;

(function (VideoQualityAdaptIndication) {
  VideoQualityAdaptIndication[VideoQualityAdaptIndication["AdaptNone"] = 0] = "AdaptNone";
  VideoQualityAdaptIndication[VideoQualityAdaptIndication["AdaptUpBandwidth"] = 1] = "AdaptUpBandwidth";
  VideoQualityAdaptIndication[VideoQualityAdaptIndication["AdaptDownBandwidth"] = 2] = "AdaptDownBandwidth";
})(VideoQualityAdaptIndication || (exports.VideoQualityAdaptIndication = VideoQualityAdaptIndication = {}));

let VideoRemoteState;
/**
 * The reason of the remote video state change.
 */

exports.VideoRemoteState = VideoRemoteState;

(function (VideoRemoteState) {
  VideoRemoteState[VideoRemoteState["Stopped"] = 0] = "Stopped";
  VideoRemoteState[VideoRemoteState["Starting"] = 1] = "Starting";
  VideoRemoteState[VideoRemoteState["Decoding"] = 2] = "Decoding";
  VideoRemoteState[VideoRemoteState["Frozen"] = 3] = "Frozen";
  VideoRemoteState[VideoRemoteState["Failed"] = 4] = "Failed";
})(VideoRemoteState || (exports.VideoRemoteState = VideoRemoteState = {}));

let VideoRemoteStateReason;
/**
 * Video display mode.
 */

exports.VideoRemoteStateReason = VideoRemoteStateReason;

(function (VideoRemoteStateReason) {
  VideoRemoteStateReason[VideoRemoteStateReason["Internal"] = 0] = "Internal";
  VideoRemoteStateReason[VideoRemoteStateReason["NetworkCongestion"] = 1] = "NetworkCongestion";
  VideoRemoteStateReason[VideoRemoteStateReason["NetworkRecovery"] = 2] = "NetworkRecovery";
  VideoRemoteStateReason[VideoRemoteStateReason["LocalMuted"] = 3] = "LocalMuted";
  VideoRemoteStateReason[VideoRemoteStateReason["LocalUnmuted"] = 4] = "LocalUnmuted";
  VideoRemoteStateReason[VideoRemoteStateReason["RemoteMuted"] = 5] = "RemoteMuted";
  VideoRemoteStateReason[VideoRemoteStateReason["RemoteUnmuted"] = 6] = "RemoteUnmuted";
  VideoRemoteStateReason[VideoRemoteStateReason["RemoteOffline"] = 7] = "RemoteOffline";
  VideoRemoteStateReason[VideoRemoteStateReason["AudioFallback"] = 8] = "AudioFallback";
  VideoRemoteStateReason[VideoRemoteStateReason["AudioFallbackRecovery"] = 9] = "AudioFallbackRecovery";
})(VideoRemoteStateReason || (exports.VideoRemoteStateReason = VideoRemoteStateReason = {}));

let VideoRenderMode;
/**
 * Video stream type.
 */

exports.VideoRenderMode = VideoRenderMode;

(function (VideoRenderMode) {
  VideoRenderMode[VideoRenderMode["Hidden"] = 1] = "Hidden";
  VideoRenderMode[VideoRenderMode["Fit"] = 2] = "Fit";
  VideoRenderMode[VideoRenderMode["Adaptive"] = 3] = "Adaptive";
  VideoRenderMode[VideoRenderMode["FILL"] = 4] = "FILL";
})(VideoRenderMode || (exports.VideoRenderMode = VideoRenderMode = {}));

let VideoStreamType;
/**
 * Warning codes occur when the SDK encounters an error that may be recovered automatically.
 * These are only notifications, and can generally be ignored. For example, when the SDK loses connection to the server,
 * the SDK reports the [`OpenChannelTimeout(106)`]{@link WarningCode.OpenChannelTimeout} warning and tries to reconnect automatically.
 */

exports.VideoStreamType = VideoStreamType;

(function (VideoStreamType) {
  VideoStreamType[VideoStreamType["High"] = 0] = "High";
  VideoStreamType[VideoStreamType["Low"] = 1] = "Low";
})(VideoStreamType || (exports.VideoStreamType = VideoStreamType = {}));

let WarningCode;
/**
 * The audio channel of the sound.
 */

exports.WarningCode = WarningCode;

(function (WarningCode) {
  WarningCode[WarningCode["InvalidView"] = 8] = "InvalidView";
  WarningCode[WarningCode["InitVideo"] = 16] = "InitVideo";
  WarningCode[WarningCode["Pending"] = 20] = "Pending";
  WarningCode[WarningCode["NoAvailableChannel"] = 103] = "NoAvailableChannel";
  WarningCode[WarningCode["LookupChannelTimeout"] = 104] = "LookupChannelTimeout";
  WarningCode[WarningCode["LookupChannelRejected"] = 105] = "LookupChannelRejected";
  WarningCode[WarningCode["OpenChannelTimeout"] = 106] = "OpenChannelTimeout";
  WarningCode[WarningCode["OpenChannelRejected"] = 107] = "OpenChannelRejected";
  WarningCode[WarningCode["SwitchLiveVideoTimeout"] = 111] = "SwitchLiveVideoTimeout";
  WarningCode[WarningCode["SetClientRoleTimeout"] = 118] = "SetClientRoleTimeout";
  WarningCode[WarningCode["SetClientRoleNotAuthorized"] = 119] = "SetClientRoleNotAuthorized";
  WarningCode[WarningCode["OpenChannelInvalidTicket"] = 121] = "OpenChannelInvalidTicket";
  WarningCode[WarningCode["OpenChannelTryNextVos"] = 122] = "OpenChannelTryNextVos";
  WarningCode[WarningCode["AudioMixingOpenError"] = 701] = "AudioMixingOpenError";
  WarningCode[WarningCode["AdmRuntimePlayoutWarning"] = 1014] = "AdmRuntimePlayoutWarning";
  WarningCode[WarningCode["AdmRuntimeRecordingWarning"] = 1016] = "AdmRuntimeRecordingWarning";
  WarningCode[WarningCode["AdmRecordAudioSilence"] = 1019] = "AdmRecordAudioSilence";
  WarningCode[WarningCode["AdmPlaybackMalfunction"] = 1020] = "AdmPlaybackMalfunction";
  WarningCode[WarningCode["AdmRecordMalfunction"] = 1021] = "AdmRecordMalfunction";
  WarningCode[WarningCode["AdmInterruption"] = 1025] = "AdmInterruption";
  WarningCode[WarningCode["AdmCategoryNotPlayAndRecord"] = 1029] = "AdmCategoryNotPlayAndRecord";
  WarningCode[WarningCode["AdmRecordAudioLowlevel"] = 1031] = "AdmRecordAudioLowlevel";
  WarningCode[WarningCode["AdmPlayoutAudioLowlevel"] = 1032] = "AdmPlayoutAudioLowlevel";
  WarningCode[WarningCode["AdmRecordIsOccupied"] = 1033] = "AdmRecordIsOccupied";
  WarningCode[WarningCode["AdmNoDataReadyCallback"] = 1040] = "AdmNoDataReadyCallback";
  WarningCode[WarningCode["AdmInconsistentDevices"] = 1042] = "AdmInconsistentDevices";
  WarningCode[WarningCode["ApmHowling"] = 1051] = "ApmHowling";
  WarningCode[WarningCode["AdmGlitchState"] = 1052] = "AdmGlitchState";
  WarningCode[WarningCode["ApmResidualEcho"] = 1053] = "ApmResidualEcho";
  WarningCode[WarningCode["SuperResolutionStreamOverLimitation"] = 1610] = "SuperResolutionStreamOverLimitation";
  WarningCode[WarningCode["SuperResolutionUserCountOverLimitation"] = 1611] = "SuperResolutionUserCountOverLimitation";
  WarningCode[WarningCode["SuperResolutionDeviceNotSupported"] = 1612] = "SuperResolutionDeviceNotSupported";
})(WarningCode || (exports.WarningCode = WarningCode = {}));

let AudioChannel;
/**
 * Video codec types.
 */

exports.AudioChannel = AudioChannel;

(function (AudioChannel) {
  AudioChannel[AudioChannel["Channel0"] = 0] = "Channel0";
  AudioChannel[AudioChannel["Channel1"] = 1] = "Channel1";
  AudioChannel[AudioChannel["Channel2"] = 2] = "Channel2";
  AudioChannel[AudioChannel["Channel3"] = 3] = "Channel3";
  AudioChannel[AudioChannel["Channel4"] = 4] = "Channel4";
  AudioChannel[AudioChannel["Channel5"] = 5] = "Channel5";
})(AudioChannel || (exports.AudioChannel = AudioChannel = {}));

let VideoCodecType;
/**
 * The publishing state.
 *
 * @since v3.1.2.
 */

exports.VideoCodecType = VideoCodecType;

(function (VideoCodecType) {
  VideoCodecType[VideoCodecType["VP8"] = 1] = "VP8";
  VideoCodecType[VideoCodecType["H264"] = 2] = "H264";
  VideoCodecType[VideoCodecType["EVP"] = 3] = "EVP";
  VideoCodecType[VideoCodecType["E264"] = 4] = "E264";
})(VideoCodecType || (exports.VideoCodecType = VideoCodecType = {}));

let StreamPublishState;
/**
 * The subscribing state.
 *
 * @since v3.1.2.
 */

exports.StreamPublishState = StreamPublishState;

(function (StreamPublishState) {
  StreamPublishState[StreamPublishState["Idle"] = 0] = "Idle";
  StreamPublishState[StreamPublishState["NoPublished"] = 1] = "NoPublished";
  StreamPublishState[StreamPublishState["Publishing"] = 2] = "Publishing";
  StreamPublishState[StreamPublishState["Published"] = 3] = "Published";
})(StreamPublishState || (exports.StreamPublishState = StreamPublishState = {}));

let StreamSubscribeState;
/**
 * Events during the RTMP or RTMPS streaming.
 */

exports.StreamSubscribeState = StreamSubscribeState;

(function (StreamSubscribeState) {
  StreamSubscribeState[StreamSubscribeState["Idle"] = 0] = "Idle";
  StreamSubscribeState[StreamSubscribeState["NoSubscribed"] = 1] = "NoSubscribed";
  StreamSubscribeState[StreamSubscribeState["Subscribing"] = 2] = "Subscribing";
  StreamSubscribeState[StreamSubscribeState["Subscribed"] = 3] = "Subscribed";
})(StreamSubscribeState || (exports.StreamSubscribeState = StreamSubscribeState = {}));

let RtmpStreamingEvent;
/**
 * Audio session restriction.
 */

exports.RtmpStreamingEvent = RtmpStreamingEvent;

(function (RtmpStreamingEvent) {
  RtmpStreamingEvent[RtmpStreamingEvent["FailedLoadImage"] = 1] = "FailedLoadImage";
})(RtmpStreamingEvent || (exports.RtmpStreamingEvent = RtmpStreamingEvent = {}));

let AudioSessionOperationRestriction;
/**
 * The options for SDK preset audio effects.
 */

exports.AudioSessionOperationRestriction = AudioSessionOperationRestriction;

(function (AudioSessionOperationRestriction) {
  AudioSessionOperationRestriction[AudioSessionOperationRestriction["None"] = 0] = "None";
  AudioSessionOperationRestriction[AudioSessionOperationRestriction["SetCategory"] = 1] = "SetCategory";
  AudioSessionOperationRestriction[AudioSessionOperationRestriction["ConfigureSession"] = 2] = "ConfigureSession";
  AudioSessionOperationRestriction[AudioSessionOperationRestriction["DeactivateSession"] = 4] = "DeactivateSession";
  AudioSessionOperationRestriction[AudioSessionOperationRestriction["All"] = 128] = "All";
})(AudioSessionOperationRestriction || (exports.AudioSessionOperationRestriction = AudioSessionOperationRestriction = {}));

let AudioEffectPreset;
/**
 * The options for SDK preset voice beautifier effects.
 */

exports.AudioEffectPreset = AudioEffectPreset;

(function (AudioEffectPreset) {
  AudioEffectPreset[AudioEffectPreset["AudioEffectOff"] = 0] = "AudioEffectOff";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsKTV"] = 33620224] = "RoomAcousticsKTV";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsVocalConcert"] = 33620480] = "RoomAcousticsVocalConcert";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsStudio"] = 33620736] = "RoomAcousticsStudio";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsPhonograph"] = 33620992] = "RoomAcousticsPhonograph";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsVirtualStereo"] = 33621248] = "RoomAcousticsVirtualStereo";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsSpacial"] = 33621504] = "RoomAcousticsSpacial";
  AudioEffectPreset[AudioEffectPreset["RoomAcousticsEthereal"] = 33621760] = "RoomAcousticsEthereal";
  AudioEffectPreset[AudioEffectPreset["RoomAcoustics3DVoice"] = 33622016] = "RoomAcoustics3DVoice";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectUncle"] = 33685760] = "VoiceChangerEffectUncle";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectOldMan"] = 33686016] = "VoiceChangerEffectOldMan";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectBoy"] = 33686272] = "VoiceChangerEffectBoy";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectSister"] = 33686528] = "VoiceChangerEffectSister";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectGirl"] = 33686784] = "VoiceChangerEffectGirl";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectPigKing"] = 33687040] = "VoiceChangerEffectPigKing";
  AudioEffectPreset[AudioEffectPreset["VoiceChangerEffectHulk"] = 33687296] = "VoiceChangerEffectHulk";
  AudioEffectPreset[AudioEffectPreset["StyleTransformationRnB"] = 33751296] = "StyleTransformationRnB";
  AudioEffectPreset[AudioEffectPreset["StyleTransformationPopular"] = 33751552] = "StyleTransformationPopular";
  AudioEffectPreset[AudioEffectPreset["PitchCorrection"] = 33816832] = "PitchCorrection";
})(AudioEffectPreset || (exports.AudioEffectPreset = AudioEffectPreset = {}));

let VoiceBeautifierPreset;
/**
 * The latency level of an audience member in interactive live streaming.
 *
 * **Note**
 *
 * Takes effect only when the user role is `Broadcaster`.
 */

exports.VoiceBeautifierPreset = VoiceBeautifierPreset;

(function (VoiceBeautifierPreset) {
  VoiceBeautifierPreset[VoiceBeautifierPreset["VoiceBeautifierOff"] = 0] = "VoiceBeautifierOff";
  VoiceBeautifierPreset[VoiceBeautifierPreset["ChatBeautifierMagnetic"] = 16843008] = "ChatBeautifierMagnetic";
  VoiceBeautifierPreset[VoiceBeautifierPreset["ChatBeautifierFresh"] = 16843264] = "ChatBeautifierFresh";
  VoiceBeautifierPreset[VoiceBeautifierPreset["ChatBeautifierVitality"] = 16843520] = "ChatBeautifierVitality";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationVigorous"] = 16974080] = "TimbreTransformationVigorous";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationDeep"] = 16974336] = "TimbreTransformationDeep";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationMellow"] = 16974592] = "TimbreTransformationMellow";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationFalsetto"] = 16974848] = "TimbreTransformationFalsetto";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationFull"] = 16975104] = "TimbreTransformationFull";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationClear"] = 16975360] = "TimbreTransformationClear";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationResounding"] = 16975616] = "TimbreTransformationResounding";
  VoiceBeautifierPreset[VoiceBeautifierPreset["TimbreTransformationRinging"] = 16975872] = "TimbreTransformationRinging";
})(VoiceBeautifierPreset || (exports.VoiceBeautifierPreset = VoiceBeautifierPreset = {}));

let AudienceLatencyLevelType;
exports.AudienceLatencyLevelType = AudienceLatencyLevelType;

(function (AudienceLatencyLevelType) {
  AudienceLatencyLevelType[AudienceLatencyLevelType["LowLatency"] = 1] = "LowLatency";
  AudienceLatencyLevelType[AudienceLatencyLevelType["UltraLowLatency"] = 2] = "UltraLowLatency";
})(AudienceLatencyLevelType || (exports.AudienceLatencyLevelType = AudienceLatencyLevelType = {}));
//# sourceMappingURL=Enums.js.map