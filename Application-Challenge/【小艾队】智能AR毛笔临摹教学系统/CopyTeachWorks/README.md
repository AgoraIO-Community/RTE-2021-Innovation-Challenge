# CopyTeachWorks

#### 介绍
智能AR毛笔临摹教学系统

考虑到线下毛笔教学的不确定性（例如不允许大规模聚集、学生无法到校上课等情况）、局限性（传统毛笔教学教师演示，学生无法一一学习模仿，老师无法及时纠正），
将毛笔教学搬到线上，解决传统毛笔教学的痛点。线上教学也是一种新颖高效的教育方式。
基于声网 视频直播SDK 作为核心、智能绘制效果demo以及智能场景的搭建开发出了一款用于线上毛笔字教学的智能软件。
可用作为白板绘制、绘画、笔触教学、笔触研究等参考demo。
本系统结合自己申请的毛笔笔触专利进行修改，主要参考了内部的实现思路，运行的客户端从pc端转移至浏览器和Android端。


#### 功能包括

用户系统（教学老师端、临摹学生端）
房间系统（老师与学生可进行一对多的聊天，老师端可观看学生端的临摹视频进行指导）
绘制系统（老师端可利用电子书写板在米字格中进行绘制，绘制的同时在房间内的学生端可实时的观看，已达到身临其境的效果）本例中以鼠标绘制代替
临摹系统（学生端将摄像头对准将要绘制的A4纸，当老师将学生端实时观看老师绘制笔迹，）

#### 项目成员

小艾同学

JoeAllen

#### 配置

本项目演示版本没有使用token

Android端

APPID更改位置在 CopyTeach-Android\app\src\main\res\values\strings_config.xml的private_app_id

将项目导入Android Studio编译运行或者在项目导出APK安装即可，打开App输入老师房间号码，默认 123456 进入视频直播界面等待老师加入即可

Web端

APPID更改位置在 CopyTeach-WebService/src/main/resources/static/live/live.js中的 options.appid =

项目访问地址 https://ct.joeallen.top/  点击进入系统即可

[视频演示](https://ct.joeallen.top/video)![推荐使用](https://user-images.githubusercontent.com/49467845/120411692-ccdd7980-c387-11eb-96e7-56b46cb95435.png)




