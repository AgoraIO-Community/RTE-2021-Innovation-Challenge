#盲水印插件简介#

	计划为视频添加图片或文字类型的盲水印，或者编码之后的图片或文字。
	盲水印添加后，用户不能直接看到视频中的水印信息，
	如果需要恢复视频中的水印信息，可使用解析视频盲水印功能，通常用于视频版权追溯、认证防伪等场景。

	本次作品仅完成在实时视频中叠加文字类型盲水印的功能，用于演示。
***
#目录结构#
##Android加水印主程序：##
###1、	插件程序###
	工程是AgoraWithWaterMaskAndroid /agora-watermask
	配置如下：
		插件代码：src/main/cpp/plugin_source_code
		cpp接口头文件：src/main/cpp/AgroaRTCKit
		cpp库文件：src/main/jniLibs/arm*/libopencv_java4.so
###2、	演示用例###
	工程是AgoraWithWaterMaskAndroid /app
	配置如下：
		Demo代码：src/main/java/*/FullscreenActivity.java
		Java库文件：libs/agora-rtc-sdk.jar		
		Cpp库文件：src/main/jniLibs/arm*/libagora-rtc-sdk-jni.so
其中*appId*的位置*"#YOUR APP ID#"*，需要替换
##Windows解水印辅程序：##
	工程是decPic，在win10，vs2017以上版本可运行，依赖opencv库
	配置如下：
		源文件：decPic.cpp
		头文件： include
		静态库路径： lib
		静态库：opencv_world452.lib
		动态库： opencv_world452.dll
		环境：64位 release版本 
opencv开源库详见：（<https://opencv.org/releases/>）
***
#使用方法#
	1、	编译部署apk，名称是WaterMaskTest
	2、	运行apk，在文本输入框定义文字类型水印的内容
	3、	点击“ADD WATERMASK”的按钮，完成实时视频的水印添加
	4、	随意截图，同时按“菜单键+音量-”完成视频截图
	5、	将截图传到windows机器上，保存，例如1.jpg
	6、	通过windows机器解水印程序输出，在decPic\x64\Release目录下，
		通过CMD命令行，输入：decPic.exe 1.jpg
	7、	得到水印输出图像，decodeWaterMask.jpg
***
#机制原理#
##加水印：##
	原始图像->DFT（离散傅里叶变换）->原始频域+文字->加水印图像
##解水印：##
	加水印图像->IDFT（逆离散傅里叶变换）->获取文字
##参数解释：##
	plugin.watermask.wmEffectEnabled：是否开启水印
	plugin.watermask.wmStr：水印内容字符串
