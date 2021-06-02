"use strict";

Object.defineProperty(exports, "__esModule", {
  value: true
});
exports.ClientRoleOptions = exports.EncryptionConfig = exports.ChannelMediaOptions = exports.CameraCapturerConfiguration = exports.LiveInjectStreamConfig = exports.WatermarkOptions = exports.Rectangle = exports.LastmileProbeConfig = exports.ChannelMediaRelayConfiguration = exports.ChannelMediaInfo = exports.LiveTranscoding = exports.Color = exports.TranscodingUser = exports.AgoraImage = exports.BeautyOptions = exports.VideoEncoderConfiguration = exports.VideoDimensions = void 0;

function _defineProperty(obj, key, value) { if (key in obj) { Object.defineProperty(obj, key, { value: value, enumerable: true, configurable: true, writable: true }); } else { obj[key] = value; } return obj; }

/**
 * The user information, including the user ID and user account.
 */

/**
 * The video resolution.
 */
class VideoDimensions {
  /**
   * The video resolution on the horizontal axis.
   */

  /**
   * The video resolution on the vertical axis.
   */
  constructor(width, height) {
    _defineProperty(this, "width", void 0);

    _defineProperty(this, "height", void 0);

    this.width = width;
    this.height = height;
  }

}
/**
 * Definition of VideoEncoderConfiguration.
 */


exports.VideoDimensions = VideoDimensions;

class VideoEncoderConfiguration {
  /**
   * The video frame dimensions (px), which is used to specify the video quality and measured by the total number of pixels along a
   * frame's width and height. The default value is 640 × 360.
   * You can customize the dimension, or select from the following list:
   * <ul>
   *         <li>120x120</li>
   *         <li>160x120</li>
   *         <li>180x180</li>
   *         <li>240x180</li>
   *         <li>320x180</li>
   *         <li>240x240</li>
   *         <li>320x240</li>
   *         <li>424x240</li>
   *         <li>360x360</li>
   *         <li>480x360</li>
   *         <li>640x360</li>
   *         <li>480x480</li>
   *         <li>640x480</li>
   *         <li>840x480</li>
   *         <li>960x720</li>
   *         <li>1280x720</li>
   * </ul>
   *
   * **Note**
   * <ul>
   *    <li> The value of the dimension does not indicate the orientation mode of the output ratio. For how to set the video orientation, see [<code>VideoOutputOrientationMode</code>]{@link VideoOutputOrientationMode}.</li>
   *    <li> Whether 720p+ can be supported depends on the device. If the device cannot support 720p, the frame rate will be lower than the one listed in the table.</li>
   * </ul>
   *
   */

  /**
   * The video frame rate (fps). The default value is 15. Users can either set the frame rate manually or choose from the following options.
   * We do not recommend setting this to a value greater than 30.
   */

  /**
   * The minimum video encoder frame rate (fps). The default value is Min(-1) (the SDK uses the lowest encoder frame rate).
   */

  /**
   * Bitrate of the video (Kbps). Refer to the table below and set your bitrate. If you set a bitrate beyond the proper range, the SDK automatically adjusts it to a value within the range.
   * You can also choose from the following options:
   *  - [`Standard`]{@link BitRate.Standard}: (Recommended) The standard bitrate mode. In this mode, the bitrates differ between the `LiveBroadcasting` and `Communication` profiles:
   *      - In the `Communication` profile, the video bitrate is the same as the base bitrate.
   *      - In the `LiveBroadcasting` profile, the video bitrate is twice the base bitrate.
   *  - [`Compatible`]{@link BitRate.Compatible}: The compatible bitrate mode. In this mode, the bitrate stays the same regardless of the profile. If you choose this mode for the `LiveBroadcasting` profile, the video frame rate may be lower than the set value.
   *
   * Agora uses different video codecs for different profiles to optimize the user experience. For example, the Communication profile prioritizes the smoothness while the `LiveBroadcasting` profile prioritizes the video quality (a higher bitrate). Therefore, We recommend setting this parameter as [`Standard`]{@link BitRate.Standard}.
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
   * **Note**
   *
   * The base bitrate in this table applies to the Communication profile.
   * The `LiveBroadcasting` profile generally requires a higher bitrate for better video quality.
   * We recommend setting the bitrate mode as [`Standard`]{@link BitRate.Standard}. You can also set the bitrate as the base bitrate value &times; 2.
   */

  /**
   * The minimum encoding bitrate (Kbps). The Agora SDK automatically adjusts the encoding bitrate to adapt to the network conditions. Using a value greater than the default value forces the video encoder to output high-quality images but may cause more packet loss and hence sacrifice the smoothness of the video transmission. That said, unless you have special requirements for image quality,
   * Agora does not recommend changing this value.
   */

  /**
   * The orientation mode.
   */

  /**
   * The video encoding degradation preference under limited bandwidth.
   */

  /**
   * Sets the mirror mode of the published local video stream.
   */
  constructor(params) {
    _defineProperty(this, "dimensions", void 0);

    _defineProperty(this, "frameRate", void 0);

    _defineProperty(this, "minFrameRate", void 0);

    _defineProperty(this, "bitrate", void 0);

    _defineProperty(this, "minBitrate", void 0);

    _defineProperty(this, "orientationMode", void 0);

    _defineProperty(this, "degradationPrefer", void 0);

    _defineProperty(this, "mirrorMode", void 0);

    if (params) {
      this.dimensions = params.dimensions;
      this.frameRate = params.frameRate;
      this.minFrameRate = params.minFrameRate;
      this.bitrate = params.bitrate;
      this.minBitrate = params.minBitrate;
      this.orientationMode = params.orientationMode;
      this.degradationPrefer = params.degradationPrefer;
      this.mirrorMode = params.mirrorMode;
    }
  }

}
/**
 * Sets the image enhancement options.
 */


exports.VideoEncoderConfiguration = VideoEncoderConfiguration;

class BeautyOptions {
  /**
   * The lightening contrast level.
   */

  /**
   * The brightness level. The value ranges between 0.0 (original) and 1.0. The default value is 0.7.
   */

  /**
   * The sharpness level. The value ranges between 0.0 (original) and 1.0.
   * The default value is 0.5. This parameter is usually used to remove blemishes.
   */

  /**
   * The redness level. The value ranges between 0.0 (original) and 1.0.
   * The default value is 0.1. This parameter adjusts the red saturation level.
   */
  constructor(params) {
    _defineProperty(this, "lighteningContrastLevel", void 0);

    _defineProperty(this, "lighteningLevel", void 0);

    _defineProperty(this, "smoothnessLevel", void 0);

    _defineProperty(this, "rednessLevel", void 0);

    if (params) {
      this.lighteningContrastLevel = params.lighteningContrastLevel;
      this.lighteningLevel = params.lighteningLevel;
      this.smoothnessLevel = params.smoothnessLevel;
      this.rednessLevel = params.rednessLevel;
    }
  }

}
/**
 * Agora image properties. A class for setting the properties of the watermark and background images.
 */


exports.BeautyOptions = BeautyOptions;

class AgoraImage {
  /**
   * HTTP/HTTPS URL address of the image on the broadcasting video. The maximum length of this parameter is 1024 bytes.
   */

  /**
   * Position of the image on the upper left of the broadcasting video on the horizontal axis.
   */

  /**
   * Position of the image on the upper left of the broadcasting video on the vertical axis.
   */

  /**
   * Width of the image on the broadcasting video.
   */

  /**
   * Height of the image on the broadcasting video.
   */
  constructor(url, x, y, width, height) {
    _defineProperty(this, "url", void 0);

    _defineProperty(this, "x", void 0);

    _defineProperty(this, "y", void 0);

    _defineProperty(this, "width", void 0);

    _defineProperty(this, "height", void 0);

    this.url = url;
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

}
/**
 * The transcodingUser class, which defines the audio and video properties in the CDN live. Agora supports a maximum of 17 transcoding users in a CDN live streaming channel.
 */


exports.AgoraImage = AgoraImage;

class TranscodingUser {
  /**
   * ID of the user in the CDN live streaming.
   */

  /**
   * Horizontal position of the video frame of the user from the top left corner of the CDN live streaming.
   */

  /**
   * Vertical position of the video frame of the user from the top left corner of the CDN live streaming.
   */

  /**
   * Width of the video frame of the user on the CDN live streaming. The default value is 360.
   */

  /**
   * Height of the video frame of the user on the CDN live streaming. The default value is 640.
   */

  /**
   * Layer position of video frame of the user on the CDN live streaming. The value ranges between 0 and 100. From v2.3.0, Agora SDK supports setting zOrder as 0. The smallest value is 0 (default value), which means that the video frame is at the bottom layer. The biggest value is 100, which means that the video frame is at the top layer.
   */

  /**
   * The transparency of the video frame of the user in the CDN live streaming that ranges between 0.0 and 1.0. 0.0 means that the video frame is completely transparent and 1.0 means opaque. The default value is 1.0.
   */

  /**
   * The audio channel ranging between 0 and 5. The default value is 0.
   *
   * - 0: (default) Supports dual channels. Depends on the upstream of the broadcaster.
   * - 1: The audio stream of the broadcaster uses the FL audio channel. If the broadcaster’s upstream uses multiple audio channels, these channels are mixed into mono first.
   * - 2: The audio stream of the broadcaster uses the FC audio channel. If the broadcaster’s upstream uses multiple audio channels, these channels are mixed into mono first.
   * - 3: The audio stream of the broadcaster uses the FR audio channel. If the broadcaster’s upstream uses multiple audio channels, these channels are mixed into mono first.
   * - 4: The audio stream of the broadcaster uses the BL audio channel. If the broadcaster’s upstream uses multiple audio channels, these channels are mixed into mono first.
   * - 5: The audio stream of the broadcaster uses the BR audio channel. If the broadcaster’s upstream uses multiple audio channels, these channels are mixed into mono first.
   *
   * **Note**
   *
   * Special players are needed if `audioChannel` is not set as 0.
   */
  constructor(uid, x, y, params) {
    _defineProperty(this, "uid", void 0);

    _defineProperty(this, "x", void 0);

    _defineProperty(this, "y", void 0);

    _defineProperty(this, "width", void 0);

    _defineProperty(this, "height", void 0);

    _defineProperty(this, "zOrder", void 0);

    _defineProperty(this, "alpha", void 0);

    _defineProperty(this, "audioChannel", void 0);

    this.uid = uid;
    this.x = x;
    this.y = y;

    if (params) {
      this.width = params.width;
      this.height = params.height;
      this.zOrder = params.zOrder;
      this.alpha = params.alpha;
      this.audioChannel = params.audioChannel;
    }
  }

}
/**
 * Color.
 */


exports.TranscodingUser = TranscodingUser;

class Color {
  /**
   * Red.
   */

  /**
   * Green.
   */

  /**
   * Blue.
   */
  constructor(red, green, blue) {
    _defineProperty(this, "red", void 0);

    _defineProperty(this, "green", void 0);

    _defineProperty(this, "blue", void 0);

    this.red = red;
    this.green = green;
    this.blue = blue;
  }

}
/**
 * A class for managing user-specific CDN live audio/video transcoding settings.
 */


exports.Color = Color;

class LiveTranscoding {
  /**
   * Width (pixel) of the video. The default value is 360. If you push video streams to the CDN, set the value of width × height to at least 64 × 64, or the SDK adjusts it to 64 x 64.
   * If you push audio streams to the CDN, set the value of width × height to 0 × 0.
   */

  /**
   * Height (pixel) of the video. The default value is 640. If you push video streams to the CDN, set the value of width × height to at least 64 × 64, or the SDK adjusts it to 64 x 64.
   * If you push audio streams to the CDN, set the value of width × height to 0 × 0.
   */

  /**
   * Bitrate (Kbps) of the CDN live output video stream. The default value is 400. Set this parameter according to the Video Bitrate Table. If you set a bitrate beyond the proper range,
   * the SDK automatically adapts it to a value within the range.
   */

  /**
   * Frame rate (fps) of the CDN live output video stream.
   * The value range is [0,30]. The default value is 15. Agora adjusts all values over 30 to 30.
   */

  /**
   * @deprecated
   * - `true`: Low latency with unassured quality.
   * - `false`: (Default) High latency with assured quality.
   */

  /**
   * Gop of the video frames in the CDN live stream. The default value is 30 fps.
   */

  /**
   * The watermark image added to the CDN live publishing stream. Ensure that the format of the image is PNG. Once a watermark image is added,
   * the audience of the CDN live publishing stream can see it.
   */

  /**
   * The background image added to the CDN live publishing stream. Once a background image is added,
   * the audience of the CDN live publishing stream can see it.
   */

  /**
   * Self-defined audio-sample rate: AudioSampleRateType.
   */

  /**
   * Bitrate (Kbps) of the CDN live audio output stream. The default value is 48 and the highest value is 128.
   */

  /**
   * The number of audio channels for the CDN live stream.
   *
   * Agora recommends choosing 1 (mono), or 2 (stereo) audio channels. Special players are required if you choose 3, 4, or 5.
   * - 1: (Default) Mono
   * - 2: Stereo
   * - 3: Three audio channels
   * - 4: Four audio channels
   * - 5: Five audio channels
   */

  /**
   * Audio codec profile type: AudioCodecProfileType. Set it as LC-AAC or HE-AAC. The default value is LC-AAC.
   */

  /**
   * Video codec profile type: VideoCodecProfileType. Set it as BASELINE, MAIN, or HIGH (default). If you set this parameter to other values, Agora adjusts it to the default value HIGH.
   */

  /**
   * Sets the background color.
   */

  /**
   * Reserved property. Extra user-defined information to send the Supplemental Enhancement Information (SEI) for the H.264/H.265 video stream to the CDN live client. Maximum length: 4096 Bytes.
   */

  /**
   * An TranscodingUser object managing the user layout configuration in the CDN live stream. Agora supports a maximum of 17 transcoding users in a CDN live stream channel.
   */
  constructor(transcodingUsers, params) {
    _defineProperty(this, "width", void 0);

    _defineProperty(this, "height", void 0);

    _defineProperty(this, "videoBitrate", void 0);

    _defineProperty(this, "videoFramerate", void 0);

    _defineProperty(this, "lowLatency", void 0);

    _defineProperty(this, "videoGop", void 0);

    _defineProperty(this, "watermark", void 0);

    _defineProperty(this, "backgroundImage", void 0);

    _defineProperty(this, "audioSampleRate", void 0);

    _defineProperty(this, "audioBitrate", void 0);

    _defineProperty(this, "audioChannels", void 0);

    _defineProperty(this, "audioCodecProfile", void 0);

    _defineProperty(this, "videoCodecProfile", void 0);

    _defineProperty(this, "backgroundColor", void 0);

    _defineProperty(this, "userConfigExtraInfo", void 0);

    _defineProperty(this, "transcodingUsers", void 0);

    if (params) {
      this.width = params.width;
      this.height = params.height;
      this.videoBitrate = params.videoBitrate;
      this.videoFramerate = params.videoFramerate;
      this.lowLatency = params.lowLatency;
      this.videoGop = params.videoGop;
      this.watermark = params.watermark;
      this.backgroundImage = params.backgroundImage;
      this.audioSampleRate = params.audioSampleRate;
      this.audioBitrate = params.audioBitrate;
      this.audioChannels = params.audioChannels;
      this.audioCodecProfile = params.audioCodecProfile;
      this.videoCodecProfile = params.videoCodecProfile;
      this.backgroundColor = params.backgroundColor;
      this.userConfigExtraInfo = params.userConfigExtraInfo;
    }

    this.transcodingUsers = transcodingUsers;
  }

}
/**
 * The ChannelMediaInfo class.
 */


exports.LiveTranscoding = LiveTranscoding;

class ChannelMediaInfo {
  /**
   * The channel name.
   */

  /**
   * The token that enables the user to join the channel.
   */

  /**
   * The user ID.
   */
  constructor(uid, params) {
    _defineProperty(this, "channelName", void 0);

    _defineProperty(this, "token", void 0);

    _defineProperty(this, "uid", void 0);

    if (params) {
      this.channelName = params.channelName;
      this.token = params.token;
    }

    this.uid = uid;
  }

}
/**
 * The ChannelMediaRelayConfiguration class.
 */


exports.ChannelMediaInfo = ChannelMediaInfo;

class ChannelMediaRelayConfiguration {
  /**
   * The information of the source channel: [`ChannelMediaInfo`]{@link ChannelMediaInfo}. It contains the following members:
   * - `channelName`: The name of the source channel. The default value is null, which means the SDK applies the name of the current channel.
   * - `uid`: ID of the host whose media stream you want to relay. The default value is 0, which means the SDK generates a random UID. You must set it as 0.
   * - `token`: The token for joining the source channel. It is generated with the `channelName` and `uid` you set in `srcInfo`.
   *  - If you have not enabled the App Certificate, set this parameter as the default value null, which means the SDK applies the App ID.
   *  - If you have enabled the App Certificate, you must use the token generated with the `channelName` and `uid`, and the `uid` must be set as 0.
   */

  /**
   * The information of the destination channel: [`ChannelMediaInfo`]{@link ChannelMediaInfo}. It contains the following members:
   * - `channelName`: The name of the destination channel.
   * - `uid`: ID of the host in the destination channel. The value ranges from 0 to (2<sup>32</sup>-1). To avoid UID conflicts, this uid must be different from any other UIDs in the destination channel. The default value is 0, which means the SDK generates a random UID.
   * - `token`: The token for joining the source channel. It is generated with the `channelName` and `uid` you set in `destInfo`.
   *  - If you have not enabled the App Certificate, set this parameter as the default value null, which means the SDK applies the App ID.
   *  - If you have enabled the App Certificate, you must use the token generated with the `channelName` and `uid`, and the `uid` must be set as 0.
   */
  constructor(srcInfo, destInfos) {
    _defineProperty(this, "srcInfo", void 0);

    _defineProperty(this, "destInfos", void 0);

    this.srcInfo = srcInfo;
    this.destInfos = destInfos;
  }

}
/**
 * Lastmile probe configuration.
 */


exports.ChannelMediaRelayConfiguration = ChannelMediaRelayConfiguration;

class LastmileProbeConfig {
  /**
   * Whether to probe uplink of lastmile. i.e., audience don't need probe uplink bandwidth.
   */

  /**
   * Whether to probe downlink of lastmile.
   */

  /**
   * The expected maximum sending bitrate in bps in range of [100000,5000000]. It is recommended to set this value according to the required bitrate of selected video profile.
   */

  /**
   * The expected maximum receive bitrate in bps in range of [100000,5000000].
   */
  constructor(probeUplink, probeDownlink, expectedUplinkBitrate, expectedDownlinkBitrate) {
    _defineProperty(this, "probeUplink", void 0);

    _defineProperty(this, "probeDownlink", void 0);

    _defineProperty(this, "expectedUplinkBitrate", void 0);

    _defineProperty(this, "expectedDownlinkBitrate", void 0);

    this.probeUplink = probeUplink;
    this.probeDownlink = probeDownlink;
    this.expectedUplinkBitrate = expectedUplinkBitrate;
    this.expectedDownlinkBitrate = expectedDownlinkBitrate;
  }

}
/**
 * The position and size of the watermark image.
 */


exports.LastmileProbeConfig = LastmileProbeConfig;

class Rectangle {
  /**
   * The horizontal offset from the top-left corner.
   */

  /**
   * The vertical offset from the top-left corner.
   */

  /**
   * The width (pixels) of the watermark image.
   */

  /**
   * The height (pixels) of the watermark image.
   */
  constructor(x, y, width, height) {
    _defineProperty(this, "x", void 0);

    _defineProperty(this, "y", void 0);

    _defineProperty(this, "width", void 0);

    _defineProperty(this, "height", void 0);

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;
  }

}
/**
 * Agora watermark options. A class for setting the properties of watermark.
 */


exports.Rectangle = Rectangle;

class WatermarkOptions {
  /**
   * Sets whether or not the watermark image is visible in the local video preview:
   * - `true`: (Default) The watermark image is visible in preview.
   * - `false`: The watermark image is not visible in preview.
   */

  /**
   * The watermark position in the landscape mode.
   */

  /**
   * The watermark position in the portrait mode.
   */
  constructor(positionInLandscapeMode, positionInPortraitMode, visibleInPreview) {
    _defineProperty(this, "visibleInPreview", void 0);

    _defineProperty(this, "positionInLandscapeMode", void 0);

    _defineProperty(this, "positionInPortraitMode", void 0);

    this.visibleInPreview = visibleInPreview;
    this.positionInLandscapeMode = positionInLandscapeMode;
    this.positionInPortraitMode = positionInPortraitMode;
  }

}
/**
 * Configuration of the imported live interactive voice or video stream.
 */


exports.WatermarkOptions = WatermarkOptions;

class LiveInjectStreamConfig {
  /**
   * Width (pixels) of the added stream to the live interactive streaming. The default value is 0, which is the same width as the original stream.
   */

  /**
   * Height (pixels) of the added stream to the live interactive streaming. The default value is 0, which is the same height as the original stream.
   */

  /**
   * Video GOP of the added stream to the live interactive streaming. The default value is 30 frames.
   */

  /**
   * Video frame rate of the added stream to the live interactive streaming. The default value is 15 fps.
   */

  /**
   * Video bitrate of the added stream to the live interactive streaming. The default value is 400 Kbps.
   *
   * **Note**
   *
   * The setting of the video bitrate is closely linked to the resolution. If you set the video bitrate beyond a reasonable range, the SDK sets it within a reasonable range instead.
   */

  /**
   * Audio sample rate of the added stream to the live interactive streaming. The default value is 44100 Hz.
   *
   * **Note**
   *
   * We recommend you use the default value and not reset it.
   */

  /**
   * Audio bitrate of the added stream to the live interactive streaming. The default value is 48 Kbps.
   *
   * **Note**
   *
   * We recommend you use the default value and not reset it.
   */

  /**
   * Audio channels to add into the live streaming. The value ranges between 1 and 2. The default value is 1.
   *
   * **Note**
   *
   * We recommend you use the default value and not reset it.
   */
  constructor(params) {
    _defineProperty(this, "width", void 0);

    _defineProperty(this, "height", void 0);

    _defineProperty(this, "videoGop", void 0);

    _defineProperty(this, "videoFramerate", void 0);

    _defineProperty(this, "videoBitrate", void 0);

    _defineProperty(this, "audioSampleRate", void 0);

    _defineProperty(this, "audioBitrate", void 0);

    _defineProperty(this, "audioChannels", void 0);

    if (params) {
      this.width = params.width;
      this.height = params.height;
      this.videoGop = params.videoGop;
      this.videoFramerate = params.videoFramerate;
      this.videoBitrate = params.videoBitrate;
      this.audioSampleRate = params.audioSampleRate;
      this.audioBitrate = params.audioBitrate;
      this.audioChannels = params.audioChannels;
    }
  }

}
/**
 * The definition of CameraCapturerConfiguration.
 */


exports.LiveInjectStreamConfig = LiveInjectStreamConfig;

class CameraCapturerConfiguration {
  /**
   * The camera capturer configuration.
   */

  /**
   * The camera direction.
   */
  constructor(preference, cameraDirection) {
    _defineProperty(this, "preference", void 0);

    _defineProperty(this, "cameraDirection", void 0);

    this.preference = preference;
    this.cameraDirection = cameraDirection;
  }

}
/**
 * The channel media options.
 */


exports.CameraCapturerConfiguration = CameraCapturerConfiguration;

class ChannelMediaOptions {
  /**
   * Determines whether to subscribe to audio streams when the user joins the channel.
   * - `true`: (Default) Subscribe.
   * - `false`: Do not subscribe.
   *
   * This member serves a similar function to the [`muteAllRemoteAudioStreams`]{@link RtcEngine.muteAllRemoteAudioStreams} method. After joining the channel, you can call the `muteAllRemoteAudioStreams` method to set whether to subscribe to audio streams in the channel.
   */

  /**
   * Determines whether to subscribe to video streams when the user joins the channel.
   * - `true`: (Default) Subscribe.
   * - `false`: Do not subscribe.
   *
   * This member serves a similar function to the [`muteAllRemoteVideoStreams`]{@link RtcEngine.muteAllRemoteVideoStreams} method. After joining the channel, you can call the `muteAllRemoteVideoStreams` method to set whether to subscribe to audio streams in the channel.
   */
  constructor(autoSubscribeAudio, autoSubscribeVideo) {
    _defineProperty(this, "autoSubscribeAudio", void 0);

    _defineProperty(this, "autoSubscribeVideo", void 0);

    this.autoSubscribeAudio = autoSubscribeAudio;
    this.autoSubscribeVideo = autoSubscribeVideo;
  }

}
/**
 * Definition of `EncryptionConfig`.
 *
 * @since v3.1.2.
 */


exports.ChannelMediaOptions = ChannelMediaOptions;

class EncryptionConfig {
  /**
   * Encryption mode. The default encryption mode is `AES128XTS`. See [`EncryptionMode`]{@link EncryptionMode}.
   */

  /**
   * Encryption key in string type.
   *
   * **Note**
   *
   * If you do not set an encryption key or set it as null, you cannot use the built-in encryption, and the SDK returns [`InvalidArgument(2)`]{@link ErrorCode.InvalidArgument}.
   */
  constructor(encryptionMode, encryptionKey) {
    _defineProperty(this, "encryptionMode", void 0);

    _defineProperty(this, "encryptionKey", void 0);

    this.encryptionMode = encryptionMode;
    this.encryptionKey = encryptionKey;
  }

}
/**
 * Statistics of the call.
 */


exports.EncryptionConfig = EncryptionConfig;

/**
 * The detailed options of a user.
 *
 * @since v3.2.0.
 */
class ClientRoleOptions {
  /**
   * The latency level of an audience member in a live interactive streaming. See {@link AudienceLatencyLevelType}.
   */
  constructor(audienceLatencyLevel) {
    _defineProperty(this, "audienceLatencyLevel", void 0);

    this.audienceLatencyLevel = audienceLatencyLevel;
  }

}

exports.ClientRoleOptions = ClientRoleOptions;
//# sourceMappingURL=Classes.js.map