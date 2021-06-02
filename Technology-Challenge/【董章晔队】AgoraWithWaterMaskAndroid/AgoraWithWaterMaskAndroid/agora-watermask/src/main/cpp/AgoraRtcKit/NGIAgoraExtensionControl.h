//
// Copyright (c) 2020 Agora.io. All rights reserved

// This program is confidential and proprietary to Agora.io.
// And may not be copied, reproduced, modified, disclosed to others, published
// or used, in whole or in part, without the express prior written permission
// of Agora.io.

#pragma once  // NOLINT(build/header_guard)
#include "AgoraBase.h"
#include "AgoraRefPtr.h"
#include "IAgoraLog.h"
#include "NGIAgoraVideoFrame.h"

namespace agora {
namespace rtc {
/**
 * Interface for handling agora extensions
 */
class IExtensionControl {
 public:
  /**
   * Agora Extension Capabilities
   */
  struct Capabilities {
    bool audio;
    bool video;

    Capabilities() : audio(false),
                     video(false) {}
  };

  /**
   * Get the capabilities of agora extensions
   * @param capabilities current supported agora extension features
   */
  virtual void getCapabilities(Capabilities& capabilities) = 0;

  /**
   * This method creates an IVideoFrame object with specified type, format, width and height
   * @return
   * - The pointer to \ref agora::rtc::IVideoFrame, if the method call succeeds
   * - The emply pointer NULL, if the method call fails
   */
  virtual agora_refptr<IVideoFrame> createVideoFrame(
      IVideoFrame::Type type, IVideoFrame::Format format, int width, int height) = 0;
  
  /**
   * This method creates a new IVideoFrame obj by copying from the source video frame
   * @return
   * - The pointer to \ref agora::rtc::IVideoFrame, if the method call succeeds
   * - The empty pointer NULL, if the method call fails
   */
  virtual agora_refptr<IVideoFrame> copyVideoFrame(agora_refptr<IVideoFrame> src) = 0;

  /**
   * This method recycle internal frame memory with specified type.
   * Deprecated video frames will be recycled automatically inside sdk. However,
   * user can invoke the following method to perform an immediate memory recycle.
   * @param type type of the frame memory to be recycled.
   */
  virtual void recycleVideoCache(IVideoFrame::Type type) = 0;

  /**
   * This method dumps the content of the video frame to the specified file.
   * @return
   * - 0: if succeeds
   * - <0: failure
   */
  virtual int dumpVideoFrame(agora_refptr<IVideoFrame> frame, const char* file) = 0;

  /**
   * write log into sdk.
   * @param level logging level
   * @param message logging message string
   * @return
   * - 0, if succeeds
   * - <0, if error happens
   */
  virtual int log(commons::LOG_LEVEL level, const char* message) = 0;

  virtual int fireEvent(const char* id, const char* event_key, const char* event_json_str) = 0;

 protected:
  virtual ~IExtensionControl() {}
};

}  // namespace rtc
}  // namespace agora
