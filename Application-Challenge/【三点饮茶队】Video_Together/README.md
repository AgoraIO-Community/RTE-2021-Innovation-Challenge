---
typora-copy-images-to: ScreenShorts
---

## Video Together
------

### 项目说明

- 此项目主要参考[Agora GitHub示例项目](https://github.com/AgoraIO/Basic-Video-Call/tree/master/Group-Video/OpenVideoCall-Android)中群组视频通话
- 集成[Agora Video SDK v3.4.2](https://docs.agora.io/cn/Video/downloads?platform=Android)
- 通过 JNI (Java Native Interface) 使用Java调用[ Agora 的 C++ 接口](https://docs.agora.io/cn/Video/raw_data_video_android?platform=Android)，采集原始视频数据
- 集成[百度AI开放平台人脸离线识别SDK v6.0](https://ai.baidu.com/ai-doc/FACE/pk37c1mqu)
- 使用[MPAndroid](https://github.com/PhilJay/MPAndroidChart)

### 项目背景

在刷b站的时候，经常看到会有一些视频，“不同国家人看《XXX》（某部电影）预告片时的反应”类的视频，如下图（截图来源于[众老外看《复仇者联盟3：无限战争》预告片时的反应](https://www.bilibili.com/video/BV1sW411a7Ca?from=search&seid=14916927644342390513)）

<img src=".\ScreenShorts\image-1.png" alt="image-1" style="zoom: 50%;" />

此项目就是以此为灵感，可以邀请一群兴趣相近的朋友，选择感兴趣的视频，加入到一个“房间”（Channel），同步在每个人的设备上播放视频；

加入视频通话一起讨论视频，还可以对"房间"成员拖拽进行排序同时可以观察对方的反应；

同时集成了表情识别，获取原始视频数据，记录视频进度时的表情变化(目前设置每5%进度更新一次记录表情表格，只记录本地用户表情)，展示观众对此视频的反馈。

### 截图

<img src=".\ScreenShorts\image-2.jpg" alt="image-2" style="zoom: 33%;" />

（实例视频来源于网络）

## 运行说明
------

### 开发环境

Android Studio 4.1.3、API 30： Android 11(R)、并在SDK Manager -> SDK Tools中下载支持NDK、CMake、NDK Version：21.1.6352462

### 运行

1. 替换自己应用**agora_app_id**，路径为 app\src\main\res\values\strings 
2. 替换自己申请的临时token，**agora_access_token**，路径同为app\src\main\res\values\strings，[申请临时token点击这里](https://docs.agora.io/cn/Interactive%20Broadcast/token#get-a-temporary-token)，注意申请临时token的时候记下自己填入的临时ChannelName，在首页中填写
3. 再导入两台设备时，需要**使用不同的序列号**对百度人脸离线识别SDK进行设备授权，修改face_license_id，路径为app\src\main\res\values\strings，两个序列号分别为"HHQS-CLMN-PUGK-WGGM"，"KPDX-DRDC-Y6XU-F8ZX"
4. 导入项目运行    **注意：请务必在真机上运行此项目！！！（由于百度人脸识别SDK提供的.so库版本不支持x86型号，模拟器上运行会出错）**
5. 输入申请临时tokenChannelName，设置选项加入房间

