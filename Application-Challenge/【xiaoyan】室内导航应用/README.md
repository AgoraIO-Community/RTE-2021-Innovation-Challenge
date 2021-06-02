# 室内导航应用
---
## 项目背景

室内的人机交互场景（仓储的内部交流的工业应用场景）
WebRTC的工业应用的场景应用。工业场景的应用不光涉及到人与人，还涉及到物体与人
而定位和调度是机器与人重要的信息交流，具体需求包含

1.机器和人员的定位（人员的定位可以采用佩戴的传感）

2.指派机器到固定的地点完成任务

3.指派相关人员和机器一起到固定地点完成任务

4.地图可视化和局部和全局导航的功能以及实时图像的传输。
总的涉及到物与物（佩戴的传感，机器AGV，AMR的自带的传感）之间的通讯，
以及人和物之间，还有人与人之间。

---
## 应用场景
智能仓储。

### 主要实现功能
1. 左侧pannel打开chatroom，

    1.1 chatroom的头部带有设备实时的power，当前地点信息，还有navigate to的按钮（指派人员或机器到其地址）

    1.2 chatroom的基本聊天功能。通过rtmchannel
 
2. 右侧pannel有进入在线人员列表，
 
    2.1 列表的子模板内也包含navigate 按钮，指派人员或机器到其地址
 
    2.2 列表的头部包含你的设备当前的在线信息和power和地址信息
 
3 .chatroom input field里，可以通过命令行进行指派，比如
 
   ` # user1 # user2 # user3 @ user4`
 
   `# 包含指派的人员， @是指派的目的地， #和@ 都会tirgger自动的下拉菜单选择相应的人员或设备` （还在开发中）
  
![Advanced-Demos 2021_6_1 20_42_25](https://user-images.githubusercontent.com/12082873/120325242-0ecfd680-c31a-11eb-939e-718eee2c14aa.png)

### Demo
https://tbxy09test.oss-cn-beijing.aliyuncs.com/share/indoorNav%202021-06-02%2023-01-16.mp4
https://tbxy09test.oss-cn-beijing.aliyuncs.com/share/indoorNav%202021-06-02%2023-03-58.mp4




