
√1、房间主 host 限制为 user001，他来开启房间，其他人必须等他加入（创建）后才能加入  
√2、答题改为点击后，倒计时 3 秒作答，时间到关闭语音识别并 check 答案， 
√3、处理失败的文案提示  
√4、页面增加  
√4.1、登录页，房间固定（不可编辑）  
4.2、关于  
4.3、帮助  
√5、登录的toast 提示  
6、直播间的 UI  
6.1、布局  
√6.2、答题的大提示框  
√7、游戏结束的补充  
8、√计分，胜负：惩罚的补充（美颜，布局……）  
√9、logo  
10、文档（参考以往作品）  
√11、多题目形式  



进入liveVC 前，登录环信sdk，进入后连接聊天室，作为信令。

信令如下

开启声网，进入声网房间。模拟两人之后。

游戏流程

开始，向双方发送问题：“明朝的皇帝有谁”

有开启方先回答。

交互：
答题按钮
长按：此时进行声音数据获取并进行语音识别。
松手：判断文字是否包含答案

有：传给下一位
否：减分

减到零分结束游戏，判定为输

每次答题的时间为十秒

惩罚，使用拼图滤镜

公屏UI

将问题、答题、结果显示出来

大弹窗提醒玩家

讯飞语音识别

采用点击时识别，如果权限开放可以全程语音识别

扩展

更多人、主播与粉丝，输入来参加

~~~
audioRawData	AgoraAudioRawData *	0x280275fe0	0x0000000280275fe0
NSObject	NSObject	
_samples	int	480
_bytesPerSample	int	2
_channels	int	1
_samplesPerSec	int	48000
_bufferSize	int	960
_renderTimeMs	int64_t	-56295832872681635
_buffer	char *	""	0x00000001438ca000
~~~

* [RTE 2021 编程挑战赛](https://pages.segmentfault.com/rte-hackathon-2021?utm_source=shengwang&utm_medium=banner)
* [直播聊天室集成介绍 [IM开发文档]](https://docs-im.easemob.com/im/other/integrationcases/live-chatroom)
* [easemob/livestream_demo_ios: 直播demo iOS](https://github.com/easemob/livestream_demo_ios)
* [语音听写 iOS SDK 文档 | 讯飞开放平台文档中心](https://www.xfyun.cn/doc/asr/voicedictation/iOS-SDK.html#_1%E3%80%81%E7%AE%80%E4%BB%8B)
* [CMSampleBufferRef与byte*互相转换(audio) - 程序园](http://www.voidcn.com/article/p-gbhzsksq-ha.html)


----

可能由于未认证用户，识别时间限制很严重，建议直接说答案后停顿一下就stop。或者点击或倒计时作答，五秒左右吧。

~~~
2021-05-18 22:58:24.585700+0800 QHSpeakerGame[44164:14385297] [IFLYTEK] recog_result_callback resultStatus==5,result={"sn":1,"ls":true,"bg":0,"ed":0,"ws":[{"bg":1,"cw":[{"sc":0.0,"w":"Êú±ÂÖÉÁíã"}]},{"bg":77,"cw":[{"sc":0.0,"w":"Êú±Ê££"}]},{"bg":145,"cw":[{"sc":0.0,"w":"Êú±"}]},{"bg":169,"cw":[{"sc":0.0,"w":"Áûª"}]},{"bg":181,"cw":[{"sc":0.0,"w":"Âü∫"}]},{"bg":0,"cw":[{"sc":0.0,"w":"„ÄÇ"}]}]},resultLen=277
2021-05-18 22:58:24.585912+0800 QHSpeakerGame[44164:14385297] [IFLYTEK] -[ISREngine isVoiceChange],voiceChange=0
2021-05-18 22:58:24.586116+0800 QHSpeakerGame[44164:14385297] [IFLYTEK] -[ISRDataHander dataHander:parse:],params={"sn":1,"ls":true,"bg":0,"ed":0,"ws":[{"bg":1,"cw":[{"sc":0.0,"w":"Êú±ÂÖÉÁíã"}]},{"bg":77,"cw":[{"sc":0.0,"w":"Êú±Ê££"}]},{"bg":145,"cw":[{"sc":0.0,"w":"Êú±"}]},{"bg":169,"cw":[{"sc":0.0,"w":"Áûª"}]},{"bg":181,"cw":[{"sc":0.0,"w":"Âü∫"}]},{"bg":0,"cw":[{"sc":0.0,"w":"„ÄÇ"}]}]}
2021-05-18 22:58:24.586355+0800 QHSpeakerGame[44164:14385297] [IFLYTEK] -[ISRDataHander dataHander:parse:],resultString={"sn":1,"ls":true,"bg":0,"ed":0,"ws":[{"bg":1,"cw":[{"sc":0.0,"w":"朱元璋"}]},{"bg":77,"cw":[{"sc":0.0,"w":"朱棣"}]},{"bg":145,"cw":[{"sc":0.0,"w":"朱"}]},{"bg":169,"cw":[{"sc":0.0,"w":"瞻"}]},{"bg":181,"cw":[{"sc":0.0,"w":"基"}]},{"bg":0,"cw":[{"sc":0.0,"w":"。"}]}]},isParse=0
2021-05-18 22:58:24.586748+0800 QHSpeakerGame[44164:14385102] chen>>(
        {
        "{\"sn\":1,\"ls\":true,\"bg\":0,\"ed\":0,\"ws\":[{\"bg\":1,\"cw\":[{\"sc\":0.0,\"w\":\"\U6731\U5143\U748b\"}]},{\"bg\":77,\"cw\":[{\"sc\":0.0,\"w\":\"\U6731\U68e3\"}]},{\"bg\":145,\"cw\":[{\"sc\":0.0,\"w\":\"\U6731\"}]},{\"bg\":169,\"cw\":[{\"sc\":0.0,\"w\":\"\U77bb\"}]},{\"bg\":181,\"cw\":[{\"sc\":0.0,\"w\":\"\U57fa\"}]},{\"bg\":0,\"cw\":[{\"sc\":0.0,\"w\":\"\U3002\"}]}]}" = 100;
    }
)-1
~~~



~~~
中国最耀眼的明星
中国导弹之父
钱学森
中国原子弹之父
钱三强
中国氢弹之父
于敏
中国杂交水稻之父
袁隆平
中国卫星之父
孙家栋
中国现代数学之父
华罗庚
中国力学之父
钱伟长
中国石油之父
孙建初
中国核潜艇之父
黄旭华
中国铁路之父
詹天佑
中国近代建筑之父
梁思成
~~~
