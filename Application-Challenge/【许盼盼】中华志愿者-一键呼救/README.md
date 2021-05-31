### **强烈推荐您通过PDF图文介绍了解本项目  [单击查看](https://github.com/asmite/RTE-2021-Innovation-Challenge/blob/master/Application-Challenge/%E3%80%90%E8%AE%B8%E7%9B%BC%E7%9B%BC%E3%80%91%E4%B8%AD%E5%8D%8E%E5%BF%97%E6%84%BF%E8%80%85-%E4%B8%80%E9%94%AE%E5%91%BC%E6%95%91/%E7%B3%BB%E7%BB%9F%E4%BB%8B%E7%BB%8D.pdf)**

## 在线体验
体验地址：https://vivo.asplay.cn/demo/

## 项目背景
本系统为2020年11月立项开发的一套微信公众号平台系统，系统面向中华志愿者协会应急救援志愿者委员会2000万会员，提供资讯、活动组织、成员招募、时长认证、福利发放、一键呼救等功能

由于本次大赛需要源码开源，为便于展示功能模块，我们重新开发了面向比赛的简单版本，主要展示了声网语音通话功能在一键呼救场景下的使用

一键呼救的想法，是我们去年就讨论过的一个想法，但因为项目进度原因，一直没有去开发这块功能，也许是缘分吧，因为一个非常巧合的机会，接触到了本次大赛，本着学习和测试的初心，我们在原系统的设计上升级出了当前版本，在功能开发完毕后我们组织了内部的演示和汇报，收获了各级领导的认同和好评

该功能的出现，弥补了应急救援中的一块空白，使得我们的救援服务更加的立体化，同时也打开了我们的思路，让我们看到信息化时代下应急救援更多的方法和抓手，万分感谢！

## 项目说明
本项目采用PHP+HTML+JS+CSS开发
* 前端使用原生HTML+JS+CSS开发
* 使用JS Lib：jQuery、AgoraRTC_N-4.4.0
* 后端使用PHP原生开发，使用了Lib：AgoraDynamicKey库，用于生成token

## 运行说明
1.运行环境PHP5.6+，部署到任意目录，浏览器访问该目录即可，入口文件为index.html

2.进入后，单击一键呼救，进入呼救页面

3.单击拨号键，等待电话接通，在此过程中可以根据求救人的需要更改求助类型、地址、定位信息

## 目录说明
* assets 静态资源文件
    *  audio 音频资源，拨号时的电话盲音
    *  css 页面样式文件
    *  img 图片文件
    *  js JavaScript脚本文件
    
* lib PHP类库
* api.php 获取token接口
* help.html 一键呼救页面
* index.html 首页

## 参考文档
* 实现语音对话 https://docs.agora.io/cn/Voice/start_call_audio_web_ng?platform=Web
* 生成token https://docs.agora.io/cn/Interactive%20Broadcast/token_server
* SDK https://github.com/AgoraIO/Tools/tree/master/DynamicKey/AgoraDynamicKey/php
* SDK（实际我用的是这个5年前的，github的实在下载不下来，(╥╯^╰╥)） https://gitee.com/tongxinwudi/AgoraDynamicKey?_from=gitee_search

## 感激
非常感谢声网提供的比赛机会，让我们接触到了这么棒的产品，对行业有了更多的认识，也增加了我们对各种产品研发的想象空间
##### 首先它真的非常棒，性能稳定、体验极佳
##### 另外它的文档合SDK确实相当的健全，超乎我的想象
##### 最后他的接入非常简单，才能让我们如此高效的完成了此次任务
##### 通过阅读文档，我们花费了不到2个小时完成了前端的接入
##### 花费了30分钟完成了后端token的生成，一切都很顺利

## 一点不成熟的建议
目前声网提供的git库只有github的来源，建议提供码云或者私有Git库等国内源，下载SDK确实让我花了不少功夫

## 关于作者

```javascript
姓名：许盼盼
公司：南京都享教育科技有限公司
手机：156 5170 8886
```