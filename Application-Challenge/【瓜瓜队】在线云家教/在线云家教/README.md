> *其他语言版本：[简体中文](README.zh.md)*

This page introduces how to run the Android sample project.

## Prerequisites

- Make sure you have made the preparations mentioned in the [Agora e-Education Guide](../README.md#prerequisites).
- Prepare the development environment:
  - JDK
  - Android Studio 2.0  or later
- Real Android devices, such as Nexus 5X. We recommend using real devices because some function may not work well on simulators or you may encounter performance issues.

## Run the sample project

Follow these steps to run the sample project:

**1.Clone the repository to your local machine.**

```
git clone https://github.com/AgoraIO-Usecase/eEducation.git
```

**2.Enter the directory of the Android project.**

```
cd eEducation/education_Android/
```

**3.Open the Android project with Android Studio.**

**4.Configure keys.**

Pass the following parameters in `app/src/main/res/values/string_configs.xml`:
- The Agora App ID that you get.
- The Agora Customer ID that you get.
- The Agora Customer Secret that you get.
- The Netless AppIdentifier that you get.

```
<string name="agora_app_id" translatable="false">Your AppId</string>
<string name="agora_customer_id" translatable="false">Your customerId</string>
<string name="agora_customer_cer" translatable="false">Your customerCer</string>
<string name="whiteboard_app_id" translatable="false">Your whiteboard appId</string>
```

For details, see the [prerequisites](../README.md#prerequisites) in Agora E-education Guide.

**5.Run the project.**

## Connect us

- You can read the full set of documentations and API reference at [Agora Developer Portal](https://docs.agora.io/en/).
- You can ask for technical support by submitting tickets in [Agora Console](https://dashboard.agora.io/). 
- You can submit an [issue](https://github.com/AgoraIO-Usecase/eEducation/issues) if you find any bug in the sample project. 

## License

Distributed under the MIT License. See `LICENSE` for more information.