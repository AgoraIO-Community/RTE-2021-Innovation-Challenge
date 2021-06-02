# native部分
### 1. 实现agora::rtc::IExtensionProvider接口
```
class IExtensionProvider {
public:
  virtual PROVIDER_TYPE getProviderType();
  virtual void setExtensionControl(IExtensionControl* control);
  virtual agora_refptr<IVideoFilter> createVideoFilter(const char* id);
  virtual agora_refptr<IAudioFilter> createAudioFilter(const char* id);
  virtual agora_refptr<IVideoSinkBase> createVideoSink(const char* id);
};
```

1.1 PROVIDER_TYPE 是指插件在 video pipeline 中的位置，定义如下：
```
enum PROVIDER_TYPE {
  LOCAL_AUDIO_FILTER,
  REMOTE_AUDIO_FILTER,
  LOCAL_VIDEO_FILTER,
  REMOTE_VIDEO_FILTER,
  LOCAL_VIDEO_SINK,
  REMOTE_VIDEO_SINK,
  UNKNOWN,
};
```

当前SDK版本仅支持 LOCAL_VIDEO_FILTER、REMOTE_VIDEO_FILTER 和 LOCAL_AUDIO_FILTER 三种类型

当 getProviderType() 返回值为 LOCAL_VIDEO_FILTER/REMOTE_VIDEO_FILTER 时，该 provider 加载时 createVideoFilter() 方法会被调用，需要返回如下第2节介绍的 IVideoFilter 实例

当 getProviderType() 返回值为 LOCAL_AUDIO_FILTER 时，该 provider 加载时 createAudioFilter() 方法会被调用，需要返回如下第3节介绍的 IAudioFilter 实例

1.2 IExtensionControl 用于触发回调事件 & log能力
```
void ByteDanceProcessor::dataCallback(const char* data){
  if (control_ != nullptr) {
	control_->fireEvent(id_, "beauty", data);	
  }
}
```
app层创建RtcEngine时注册的 IMediaExtensionObserver 实例会收到此消息，详见Readme.md中的第2节

### 2. 实现agora::rtc::IVideoFilter接口

```
class IVideoFilter {
public:
  virtual bool adaptVideoFrame(const media::base::VideoFrame& capturedFrame,
                               media::base::VideoFrame& adaptedFrame);
  virtual void setEnabled(bool enable);
  virtual bool isEnabled();
  virtual size_t setProperty(const char* key, const void* buf, size_t buf_size);
  virtual size_t getProperty(const char* key, void* buf, size_t buf_size);
};
```

2.1 adaptVideoFrame函数通过处理 capturedFrame，返回 adaptedFrame，提供了 video 类型插件的核心功能

### 3. 实现agora::rtc::IAudioFilter接口

```
class IAudioFilter {
public:
  virtual bool adaptAudioFrame(const media::base::AudioPcmFrame& inAudioFrame,
                               media::base::AudioPcmFrame& adaptedFrame);
  virtual void setEnabled(bool enable);
  virtual bool isEnabled();
  virtual size_t setProperty(const char* key, const void* buf, size_t buf_size);
  virtual size_t getProperty(const char* key, void* buf, size_t buf_size);
  virtual const char * getName();
};
```

3.1 adaptAudioFrame 函数通过处理 inAudioFrame，返回 adaptedFrame，提供了 audio 类型插件的核心功能

3.2 现阶段受架构所限，getName 函数需要返回正确的 VENDOR_NAME，今后将会移除该函数

# Java部分
### 4. 指定一个类加载 native 库（.so文件），并在该类中提供 nativeGetExtensionProvider 方法以获取 native provider
```
public class ExtensionManager {
  public static final String VENDOR_NAME = "ByteDance";
  static {
	System.loadLibrary("native-lib");
  }
  public static native long nativeGetExtensionProvider(Context context, String vendor, int type);
}
```
