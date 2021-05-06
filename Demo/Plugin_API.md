# 集成头文件

**Android**

1. 获取 Agora_Native_SDK_for_Android 安装包并解压。
2. 将 sdk/low_level_api/include 文件拷贝至你的扩展项目文件夹中。

**iOS**

1. 获取 Agora_Native_SDK_for_iOS 安装包并解压。
2. 打开 Xcode，进入 TARGETS > Project Name > General > Frameworks, Libraries, and Embedded Content 菜单，点击 + > Add Other... > Add Files，将安装包中的 AgoraRtcKit2.framework 文件添加至你的项目中。确保 Embed 属性设置为 Do Not Embed。

# 实现 IVideoFilter 接口

视频插件通过 IVideoFilter 接口实现。该接口位于 NGIAgoraMediaNode.h 文件中。你需要先实现这个接口类，并至少实现该类的如下方法：

```c++
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

### setEnabled

启用或关闭视频插件。

```c++
virtual void setEnabled(bool enable) {}
```

| 参数   | 描述                                      |
| :----- | :---------------------------------------- |
| enable | true: 启用视频插件。false: 关闭视频插件。 |

### isEnabled

检查视频插件是否已启用。

```c++
virtual bool isEnabled() { return true; }
```



| 参数   | 描述                                          |
| :----- | :-------------------------------------------- |
| enable | true: 视频插件已启用。false: 视频插件已关闭。 |

### **setProperty**

该方法设置提供的扩展服务。API 原型和描述如下：

```cpp
size_t setProperty(const char* key, const void* buf, size_t buf_size)
```



| 参数     | 描述                                                         |
| :------- | :----------------------------------------------------------- |
| key      | 扩展服务的 key。                                             |
| buf      | 扩展服务的 key 值对应的地址，数据形式为 json 字符串。我们推荐使用第三方的 nlohmann/json library 开源库，帮助实现 C++ 的 struct 和 json 字符串之前的序列和反序列化。 |
| buf_size | 代表扩展服务地址的 json 字符串的大小。                       |



### adaptVideoFrame

处理视频帧。该方法是 IVideoFilter 类的核心方法，处理 capturedFrame，返回 adaptedFrame。该方法目前仅支持输入输出 YUV I420 格式的视频数据。

```cpp
bool adaptVideoFrame(const media::base::VideoFrame& capturedFrame, media::base::VideoFrame& adaptedFrame)
```



| 参数          | 描述                       |
| :------------ | :------------------------- |
| capturedFrame | 输入参数。待处理的视频帧。 |
| adaptedFrame  | 输出参数。处理后的视频帧。 |

### 示例代码

```cpp
// 在收到需要处理的视频数据后，调用 adaptVideoFrame 方法，对收到的视频数据进行处理。
bool ExtensionVideoFilter::adaptVideoFrame(const agora::media::base::VideoFrame &caoturedFrame, agora::media::base::VideoFrame &adaptedFrame) {
  
  if (!isInitOpenGL) {
    isInitOpenGL = byteDanceProcessor_->initOpenGL();
  }
  byteDanceProcessor_->processFrame(capturedFrame);
  adaptedFrame = capturedFrame;
  return true;
}

// 调用 setProperty 设置视频插件参数。
size_t ExtensionVideoFilter::setProperty(const char *key, const void *buf, size_t buf_size) {
  return 0;
}

// 调用 getProperty 查询视频插件参数。
size_t ExtensionVideoFilter::getProperty(const char *key, void *buf, size_t buf_size) {
  return 0;
}


// 调用 setEnabled 开启视频插件。 
void ExtensionVideoFilter::setEnabled(bool enabled) {
}

// 调用 isEnabled 查询视频插件开启状态。
bool ExtensionVideoFilter::isEnabled() {
  return true;
}
```

# 封装扩展服务

封装扩展服务通过 IExtensionProvider 接口类实现。该接口位于 NGIAgoraExtensionProvider.h 文件中。你需要先实现这个接口类，并至少实现该类的如下接口：

```cpp
class IExtensionProvider {
public:
  virtual PROVIDER_TYPE getProviderType();
  virtual void setExtensionControl(IExtensionControl* control);
  virtual agora_refptr<IVideoFilter> createVideoFilter(const char* id);
};
```

### getProviderType

指定你想要封装的扩展服务类型。

```cpp
virtual PROVIDER_TYPE getProviderType()
```

收到该回调后，你需要通过返回值指定你想要封装的扩展服务类型。其中 PROVIDER_TYPE 是指插件在音视频 Pipeline 中的位置，定义如下：

```cpp
enum PROVIDER_TYPE {
  // 本地音频插件
  LOCAL_AUDIO_FILTER,
  // 尚未实现
  REMOTE_AUDIO_FILTER
  // 本地视频插件
  LOCAL_VIDEO_FILTER,
  // 远端视频插件
  REMOTE_VIDEO_FILTER,
  // 尚未实现
  LOCAL_VIDEO_SINK,
  // 尚未实现
  REMOTE_VIDEO_SINK,
  // 预留参数
  UNKNOWN,
};
```

如果你在 getProviderType 方法中返回的插件类型为 LOCAL_VIDEO_FILTER 或 REMOTE_VIDEO_FILTER，则 IExtensionProvider 对象会在生命周期内调用 createVideoFilter 方法。你需要在该方法的返回值中传入 IVideoFilter 实例。

### setExtensionControl

设置扩展控制。

```cpp
virtual void setExtensionControl(IExtensionControl* control)
```

成功调用该方法后，你需要保存 SDK 在该方法中返回的 IExtensionControl 对象。该对象用于触发事件或日志，实现插件与 app 之间的交互。例如，如果你调用了 IExtensionControl 中的 fireEvent 方法：

```cpp
void ByteDanceProcessor::dataCallback(const char* data){
  if (control_ != nullptr) {
	control_->fireEvent(id_, "beauty", data);	
  }
}
```

app 层在初始化 RtcEngine 时注册的 IMediaExtensionObserver 实例会收到该信息，并在 app 层触发如下回调：

```cpp
@Override
public void onEvent(String vendor, String key, String value) {
// 其中的 vendor 为注册插件时的 VENDOR_NAME，key/value是插件消息的键值对
......
}
```

### createVideoFilter

创建视频插件。

```cpp
virtual agora_refptr<IVideoFilter> createVideoFilter()
```

成功创建 Video Filter 实例后，扩展服务会在合适的时机通过 IVideoFilter 类对输入的视频数据进行处理。

### 示例代码

```cpp

agora-refptr<agora::rtc::IVideoFilter> ExtensionProvider::createVideoFilter(const char* id) {
  byteDanceProcessor_->setExtensionVendor(id);
  auto videoFilter = new agora::RefcountedObject<agora::extension::ExtensionVideoFilter>(byteDanceProcessor_);
  return videoFilter;
}

ExtensionProvider::PROVIDER_TYPE ExtensionProvider::getProviderType() {
  return ExtensionProvider::PROVIDER_TYPE::LOCAL_VIDEO_FILTER;
}

void eXTENSIONprovider::setExtensionControl(rtc::IExtensionControl* control){
  byteDanceProcessor_->setExtensionControl(control);
}
```

# 示例项目

详细的代码逻辑可以参考如下链接：

<https://github.com/AgoraIO-Community/RTE-2021-Innovation-Challenge/tree/master/Demo>

# 打包扩展

**Android**

1. 扩展是 .aar 格式的文件。

2. Java 提供一个公开的静态方法：nativeGetExtensionProvider。你需要参考如下行调用 jni 方法：

   ```cpp
   extern "C" JNIEXPORT jlong JNICALL
   Java_io_agora_extension_ExtensionManager_nativeGetExtensionProvider(
           JNIEnv* env,
    jclass clazz, jobject context)
   ```

   该方法会返回一个 IExtensionProvider 接口，并向 app 触发一个事件句柄。

**iOS**

1. 扩展是 .framework 格式的库文件。

2. 扩展文件通过 mediaFilterExtension 方法提供一个 BDVideoExtensionObject 类，其中实现了 AgoraMediaFilterExtensionDelegate 协议。

   ```cpp
   - (BDVideoExtensionObject *)mediaFilterExtension {
   BDVideoExtensionObject *obj = [BDVideoExtensionObject new];
   obj.vendorName = kVendorName;
   
   if (_bdProvider) {
   obj.mediaFilterProvider = _bdProvider.get();
   } else {
   obj.mediaFilterProvider = nullptr;
   }
   
   return obj;
   }
   ```

