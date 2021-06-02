## 在线体验地址
### 3分钟就能完整体验当前插件功能
####【前台】
##### 教师端：https://minipro-course.todoen.com/agora_demo
##### 学生端：https://minipro-course.todoen.com/agora_demo/student.html
##### 学生端需要登录，请参照登录页面指示操作，目前支持3个测试账号

####【后台】
##### https://minipro-course.todoen.com/live_tools/
##### 后台需要登录，请参照页面支持操作，目前支持1个声网账号登录

## 项目背景
本插件为灵动课堂WEB端组件，用于实现直播过程中向客户端推送题目

根据我公司4年线上教育的经验，在直播过程中，通过推送题目的方式与客户产生交互

有利于提高公开课的转化率

目前上传的代码 前端部分为演示demo，插件均集成到后端，作为服务提供

本作品主要用于展示题目推送的功能，因此没有集成白板到学生端，还请见谅

## 插件功能
* 支持题型包括 单选、多选、判断
* 教师端支持选题+推送
* 学生端支持答题、判断答题对错
* 学生端支持多账户模拟登录演示
* 管理端支持配置直播间管理、题目管理
* 支持查看直播间答题数据、单个题目答题详情
* 接口端支持教师端和学生端的实时token获取
* 用户使用本系统，可以通过配置完成灵动课堂的使用和部署

## 技术栈
* 前端使用HTML+JS+CSS
* 后端使用PHP 5.6，目前部署生产环境为PHP 7.0

## 插件集成方法
1.教师端
```
1.引入css和js文件
<link rel="stylesheet" href="插件服务端根目录/public/css/coco_main.css?t=20210601">

2.引入jquery文件，版本大于2.0即可

3.引入js文件
<script src="插件服务端根目录/public/js/coco_lesson_teacher.js?t=20210601"></script>

3.初始化插件
coco_lesson_teacher.init({
    appid: "声网APPID",
    teacher_id: "老师ID（唯一）",
    room_uuid: "房间号（唯一）",
    base_api:"插件根目录地址"
});
```

2.学生端
```
1.引入css和js文件
<link rel="stylesheet" href="插件服务端根目录/public/css/coco_main.css?t=20210601">

2.引入jquery文件，版本大于2.0即可

3.引入js文件
<script src="插件服务端根目录/public/js/coco_lesson_student.js?t=20210601"></script>

3.初始化插件
coco_lesson_student.init({
    appid: "声网APPID",
    user_id: "学员ID（唯一）",
    room_uuid: "房间号（唯一）",
    base_api:"插件根目录地址"
});
```

## 参赛demo运行说明
1.前后端可以部署在同一个域名，也可以分开部署，接口支持跨域

2.前端 学生端配置，修改 ./student.html 第32行代码
```
/**********
 *  初始化coco插件
 *  appid：声网APPID
 *  user_id：用户ID（唯一）
 *  room_uuid：房间号（唯一）
 *  base_api：coco插件服务地址
 *
 *  理论上这些参数应对通过业务代码的接口端获取，这样会更加安全，也更加灵活
 *********/
coco_lesson_student.init({
    appid: "2306c08dc63b4e16834d1a5bb41621a6",
    user_id: coco_lesson_userid,
    room_uuid: "test_01",
    base_api:"https://minipro-course.todoen.com/live_tools/"
});
```

3.前端 教师端配置，修改 ./index.html 第31行代码

```
/**********
 *  初始化coco插件
 *  appid：声网APPID
 *  teacher_id：老师ID（唯一）
 *  room_uuid：房间号（唯一）
 *  base_api：coco插件服务地址
 *
 *  理论上这些参数应对通过业务代码的接口端获取，这样会更加安全，也更加灵活
 *********/
coco_lesson_teacher.init({
    appid: "2306c08dc63b4e16834d1a5bb41621a6",
    teacher_id: "teacher01",
    room_uuid: "test_01",
    base_api:"https://minipro-course.todoen.com/live_tools/"
});
```

4.后端 数据库配置，修改./config/config.php 中第37行代码，更新域名为您的域名，更新数据库名、数据库用户名和密码

## 系统的改进方向
* 答题报表统计
* 实时抽奖
* 产品推荐+购买
* 快速评论

## 关于作者
* coco
* 微信：pandary1314
* 使用过程中有问题请联系，欢迎交流