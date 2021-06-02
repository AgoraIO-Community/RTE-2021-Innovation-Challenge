README

![](https://tva1.sinaimg.cn/large/008i3skNly1gr47f1rdokj60u06y0hdw02.jpg)





## 介绍

都市探险家是一款利用地图LBS + 实施云信令 + 实施音视频构建的社交产品。为想要在都市旅游，探索却没有小伙伴的朋友们提供一个便捷，安全的社交环境，寻找拥有共同爱好的小伙伴们。

## Requirements

用到的声网SDK如下：

* 云信令SDK AgoraRtm_iOS
* 语音通讯SDK  AgoraAudio_iOS

运行程序只需要AppConfig中更换几个关键key 和音视频token即可

```swift
//
//  AppConfig.swift
//  UrbanExplorer_UIKit
//
//  Created by 叶孤城 on 2021/5/29.
//

import Foundation

struct AppConfig {
  // 云信令key
  static let messageKey = ""
  // 语音key
  static let agoraAudioKey = ""
  // 语音token
  static let audioToken = ""
  static let audioChannelName = "JoinTaskChatChannel"
}

```

效果演示图。

1. 登录app

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gr40yb5qi5j30u01sxqv5.jpg" style="zoom:33%;" />

选择用户头像，输入手机号和用户名称，点击登录即可。



2. 此时地图会自动更新您的位置

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gr40zpsw1uj30u01sx7wh.jpg" style="zoom:33%;" />

3. 点击发起任务即可发起一个探险任务

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gr41adb7plj30u01sxay5.jpg" style="zoom:33%;" />

输入探险地点，探险名称和人数等关键信息即可。

发送之后界面如下。

!<img src="https://tva1.sinaimg.cn/large/008i3skNly1gr41br2c14j30u01sxal4.jpg" style="zoom:33%;" />

此时会向附近的所有人广播任务。

4. 接收到任务之后，用户可选择加入。组队会实时更新此时加入的用户。

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gr41d2yss3j30u01sxk25.jpg" style="zoom:33%;" />

5. 实时显示用户加入，并且加入语音

<img src="https://tva1.sinaimg.cn/large/008i3skNly1gr41i4xnzaj30u01sxk15.jpg" style="zoom:33%;" />

语音沟通之后，如果满足可加入组队。

## 信令流程

1. 房主创建任务，通过云信令广播到附近区域
2. 附近区域如果有小伙伴，可以发现任务，发送信令加入
3. 房主收到加入请求之后，双方加入同一语音频道，开始语音

## 相关视频


https://user-images.githubusercontent.com/8814535/120471031-cc200400-c3d6-11eb-8066-bf043ca0a95c.mov


https://user-images.githubusercontent.com/8814535/120471064-d6420280-c3d6-11eb-8876-fd7dc3465ab7.mp4


https://user-images.githubusercontent.com/8814535/120471114-e528b500-c3d6-11eb-9e3e-b56685ffc56a.mov


https://user-images.githubusercontent.com/8814535/120471226-0d181880-c3d7-11eb-8720-5a222fa41fec.mov



