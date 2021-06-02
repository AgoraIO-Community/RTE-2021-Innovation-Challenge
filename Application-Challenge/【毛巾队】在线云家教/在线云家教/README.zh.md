﻿> *Read this in another language: [English](README.md)*

本文指导你运行 Android 示例项目。

## 前提条件

- 准备工作：请确保你已经完成 [e-Education 项目指南](../README.zh.md)中的准备工作。
- 开发环境：
  - JDK
  - Android Studio 2.0  及以上
- Android 设备。部分模拟机可能存在功能缺失或者性能问题，所以推荐使用真机。

## 运行示例项目

参考以下步骤编译和运行示例项目：

**1. 将代码克隆到本地**

```
git clone https://github.com/AgoraIO-Usecase/eEducation.git
```

**2. 进入项目目录**

```
cd eEducation/education_Android/
```

**3. 使用 Android Studio 打开项目**

**4. 配置相关字段**

在 `app/src/main/res/values/string_configs.xml` 文件中配置以下字段：
- 你获取到的声网 App ID。
- 你获取到的声网 Customer ID。
- 你获取到的声网 Customer 密钥。
- 你获取到的白板 AppIdentifier。

```
<string name="agora_app_id" translatable="false">Your AppId</string>
<string name="agora_customer_id" translatable="false">Your customerId</string>
<string name="agora_customer_cer" translatable="false">Your customerCer</string>

<string name="whiteboard_app_id" translatable="false">Your whiteboard appId</string>
```

详见 Agora e-Education 项目指南中的[前提条件](../README.zh.md#prerequisites)。

**5. 运行项目**

## 联系我们

- 如需阅读完整的文档和 API 注释，你可以访问[声网开发者中心](https://docs.agora.io/cn/)。
- 如果在集成中遇到问题，你可以到[声网开发者社区](https://dev.agora.io/cn/)提问。
- 如果有售前咨询问题，你可以拨打 400 632 6626，或加入官方Q群 12742516 提问。
- 如果需要售后技术支持，你可以在 [Agora 控制台](https://dashboard.agora.io/)提交工单。
- 如果发现了示例代码的 bug，欢迎提交 [issue](https://github.com/AgoraIO/Rtm/issues)。

## 代码许可

The MIT License (MIT).