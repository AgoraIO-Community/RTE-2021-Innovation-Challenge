//
//  Agora SDK
//
//  Copyright (c) 2019 Agora.io. All rights reserved.
//

#pragma once  // NOLINT(build/header_guard)

#include "AgoraBase.h"
#include "AgoraRefPtr.h"
#include "IAgoraService.h"
#include "NGIAgoraMediaNode.h"
#include "IAgoraMediaPlayerSource.h"
#include "NGIAgoraCameraCapturer.h"
#include "NGIAgoraRemoteAudioMixerSource.h"
#include "NGIAgoraScreenCapturer.h"
#include "NGIAgoraVideoMixerSource.h"

namespace agora {
namespace media {
namespace base {
class IAudioFrameObserver;
} // namespace base
} // namespace media

namespace rtc {
/**
 * The `IMediaNodeFactory` class.
 */
class IMediaNodeFactory : public RefCountInterface {
 public:
  /**
   * Creates a PCM audio data sender.
   *
   * This method creates an `IAudioPcmDataSender` object, which can be used by \ref agora::base::IAgoraService::createCustomAudioTrack(agora_refptr< rtc::IAudioPcmDataSender > audioSource) "createCustomAudioTrack" to send PCM audio data.
   *
   * @return
   * - The pointer to \ref agora::rtc::IAudioPcmDataSender "IAudioPcmDataSender", if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IAudioPcmDataSender> createAudioPcmDataSender() = 0;

  /**
   * Creates an encoded audio data sender.
   *
   * This method creates an IAudioEncodedFrameSender object, which can be used by \ref agora::base::IAgoraService::createCustomAudioTrack(agora_refptr< rtc::IAudioEncodedFrameSender > audioSource, TMixMode mixMode) "createCustomAudioTrack" to send encoded audio data.
   *
   * @return
   * - The pointer to IAudioEncodedFrameSender, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IAudioEncodedFrameSender> createAudioEncodedFrameSender() = 0;

  /**
   * Creates a remote audio mixer source object and returns the pointer.
   *
   * @param type The type of audio mixer source you want to create.
   *
   * @return
   * - The pointer to \ref rtc::IRemoteAudioMixerSource "IRemoteAudioMixerSource", if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IRemoteAudioMixerSource> createRemoteAudioMixerSource() = 0;

  /**
   * Creates a camera capturer.
   *
   * Once a camera capturer object is created, you can use the video data captured by the camera as
   * the custom video source.
   *
   * @return
   * - The pointer to ICameraCapturer, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<ICameraCapturer> createCameraCapturer() = 0;

  /**
   * Creates a screen capturer.
   *
   * Once a screen capturer object is created, you can use the screen video data as the custom video
   * source.
   *
   * @return
   * - The pointer to IScreenCapturer, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IScreenCapturer> createScreenCapturer() = 0;
  
   /**
   * Creates a video mixer.
   *
   * Once a video mixer object is created, you can use the video mixer data as the custom video
   * source.
   *
   * @return
   * - The pointer to IVideoMixerSource, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IVideoMixerSource> createVideoMixer() = 0;

  /**
   * Creates a video transceiver.
   *
   * Once a video transceiver object is created, you can use the video transceiver data as the custom video
   * source.
   *
   * @return
   * - The pointer to IVideoFrameTransceiver, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IVideoFrameTransceiver> createVideoFrameTransceiver() = 0;

  /**
   * Creates a video frame sender.
   *
   * This method creates an `IVideoFrameSender` object, which can be used by \ref agora::base::IAgoraService::createCustomVideoTrack(agora_refptr< rtc::IVideoFrameSender > videoSource, bool syncWithAudioTrack) "createCustomVideoTrack" to
   * send the custom video data.
   *
   * @return
   * - The pointer to \ref agora::rtc::IVideoFrameSender "IVideoFrameSender", if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IVideoFrameSender> createVideoFrameSender() = 0;

  /**
   * Creates an encoded video image sender.
   *
   * This method creates an IVideoEncodedImageSender object, which can be used by \ref agora::base::IAgoraService::createCustomVideoTrack(agora_refptr< rtc::IVideoEncodedImageSender > videoSource, bool syncWithAudioTrack, TCcMode ccMode) "createCustomVideoTrack" to send the encoded video data.
   *
   * @return
   * - The pointer to `IVideoEncodedImageSender`, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IVideoEncodedImageSender> createVideoEncodedImageSender() = 0;

  /**
   * Creates a built-in video renderer.
   *
   * @param view The video window view.
   * @return
   * - The pointer to `IVideoRenderer`, if the method call succeeds.
   * - A null pointer, if the method call fails.
   *
   * @note IVideoRenderer is also an IVideoSinkBase except that it's not an extension sink on
   * purpose.
   */
  virtual agora_refptr<IVideoRenderer> createVideoRenderer() = 0;

  /**
   * Creates an audio filter for the extension.
   *
   * This method creates an `IAudioFilter` object, which can be used to filter the audio data from
   * inside extension.
   *
   * @param id The pointer to the extension id.
   * @return
   * - The pointer to IAudioFilter, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IAudioFilter> createAudioFilter(const char* id) = 0;

  /**
   * Creates a video filter for the extension.
   *
   * This method creates an IVideoFilter object, which can be used to filter the video from inside
   * extension.
   *
   * @param id The pointer to the extension id.
   * @return
   * - The pointer to IVideoFilter, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IVideoFilter> createVideoFilter(const char* id) = 0;

  /**
   * Creates a video sink for the extension.
   *
   * This method creates an IVideoSinkBase object, which can be used to receive the video from
   * inside extension.
   *
   * @param id The pointer to the extension id.
   * @return
   * - The pointer to IVideoSinkBase, if the method call succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IVideoSinkBase> createVideoSink(const char* id) = 0;

  /**
   * Creates a media player source object and returns the pointer.
   *
   * @param type The type of media player source you want to create.
   *
   * @return
   * - The pointer to \ref rtc::IMediaPlayerSource "IMediaPlayerSource", if the method call
   * succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IMediaPlayerSource> createMediaPlayerSource(media::base::MEDIA_PLAYER_SOURCE_TYPE type = agora::media::base::MEDIA_PLAYER_SOURCE_DEFAULT) = 0;
  
  /**
   * Creates a media packet sender object and returns the pointer.
   *
   * @return
   * - The pointer to \ref rtc::IMediaPacketSender "IMediaPacketSender", if the method call
   * succeeds.
   * - A null pointer, if the method call fails.
   */
  virtual agora_refptr<IMediaPacketSender> createMediaPacketSender() = 0;

  /**
   * Creates a audio device source object and returns the pointer.
   *
   * @return
   * - The pointer to \ref rtc::IRecordingDeviceSource "IRecordingDeviceSource", if the method call
   * succeeds.
   * - The empty pointer NULL, if the method call fails.
   */
  virtual agora_refptr<IRecordingDeviceSource> createRecordingDeviceSource(char deviceId[kDeviceIdSize]) = 0;

 protected:
  ~IMediaNodeFactory() {}
};
}  // namespace rtc
}  // namespace agora
