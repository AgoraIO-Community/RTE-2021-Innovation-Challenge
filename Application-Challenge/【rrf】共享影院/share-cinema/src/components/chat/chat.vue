<template>
  <div class="chat">
    <div class="chat-window" v-bind:style="height">
      <div
        v-for="item in chatMessage"
        class="chat-text"
        :key="item.time"
        :class="item.uid == uid ? 'right' : 'left'"
      >
        <div class="avatar">
          <el-avatar
            src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
          ></el-avatar>
        </div>
        <div class="aside">
          <div class="label">
            {{ item.uid }} {{ new Date(item.time).toLocaleTimeString("zh-CN") }}
          </div>
          <div class="text">
            <p>{{ item.text }}</p>
          </div>
        </div>
      </div>
    </div>
    <div class="input-area">
      <div class="grid-content">
        <el-input v-model="textarea" placeholder="真是太棒了！">
          <el-button
            slot="append"
            icon="el-icon-check"
            @click="sendMessageToRoom()"
            >发送</el-button
          >
        </el-input>
      </div>
    </div>
  </div>
</template>

<script>
import websdk from "easemob-websdk";
import config from "../../../config/WebIMConfig";
import axios from "axios";

export default {
  name: "ChatView",
  props: {
    height: {
      type: Number,
      required: false,
      default: NaN,
    },
    uid: {
      type: Number,
      required: true,
    },
    room: {
      type: String,
      required: true,
    },
  },
  data() {
    return {
      textarea: "",
      chatMessage: [],
      WebIM: {},
    };
  },
  mounted() {
    let WebIM = (this.WebIM = window.WebIM = websdk);
    let conn = {};
    WebIM.config = config;
    conn = WebIM.conn = new WebIM.connection({
      appKey: WebIM.config.appkey,
      isHttpDNS: WebIM.config.isHttpDNS,
      isMultiLoginSessions: WebIM.config.isMultiLoginSessions,
      https: WebIM.config.https,
      url: WebIM.config.socketServer,
      apiUrl: WebIM.config.restServer,
      isAutoLogin: WebIM.config.isAutoLogin,
      autoReconnectNumMax: WebIM.config.autoReconnectNumMax,
      autoReconnectInterval: WebIM.config.autoReconnectInterval,
      delivery: WebIM.config.delivery,
      useOwnUploadFun: WebIM.config.useOwnUploadFun,
    });
    // WebIM.config 为之前集成里介绍的WebIMConfig.js
    const that = this;

    conn.listen({
      onOpened: function (message) {}, //连接成功回调
      onClosed: function (message) {}, //连接关闭回调
      onTextMessage: function (message) {
        switch (message.type) {
          case "chatroom":
            console.log(message);
            let msg = {
              uid: message.from,
              room: message.to,
              time: message.ext.time,
              text: message.data,
            };
            that.chatMessage.push(msg);
            break;
          default:
            break;
        }
      }, //收到文本消息
      onEmojiMessage: function (message) {}, //收到表情消息
      onPictureMessage: function (message) {}, //收到图片消息
      onCmdMessage: function (message) {}, //收到命令消息
      onAudioMessage: function (message) {}, //收到音频消息
      onLocationMessage: function (message) {}, //收到位置消息
      onFileMessage: function (message) {}, //收到文件消息
      onVideoMessage: function (message) {}, //收到视频消息
      onPresence: function (message) {}, //处理“广播”或“发布-订阅”消息，如联系人订阅请求、处理群组、聊天室被踢解散等消息
      onRoster: function (message) {}, //处理好友申请
      onInviteMessage: function (message) {}, //处理群组邀请
      onOnline: function () {}, //本机网络连接成功
      onOffline: function () {}, //本机网络掉线
      onError: function (message) {}, //失败回调
      onBlacklistUpdate: function (list) {
        //黑名单变动
        // 查询黑名单，将好友拉黑，将好友从黑名单移除都会回调这个函数，list则是黑名单现有的所有好友信息
        console.log(list);
      },
      onRecallMessage: function (message) {}, //收到撤回消息回调
      onReceivedMessage: function (message) {}, //收到消息送达服务器回执
      onDeliveredMessage: function (message) {}, //收到消息送达客户端回执
      onReadMessage: function (message) {}, //收到消息已读回执
      onCreateGroup: function (message) {}, //创建群组成功回执（需调用createGroupNew）
      onMutedMessage: function (message) {}, //如果用户在A群组被禁言，在A群发消息会走这个回调并且消息不会传递给群其它成员
      onChannelMessage: function (message) {}, //收到整个会话已读的回执，在对方发送channel ack时会在这个回调里收到消息
    });

    var options = {
      username: this.uid,
      password: "123456",
      nickname: this.uid,
      appKey: WebIM.config.appkey,
      success: function () {
        console.log("register success!");
      },
      error: function (err) {
        let errorData = JSON.parse(err.data);
        if (errorData.error === "duplicate_unique_property_exists") {
          console.warn("用户已存在！");
        } else if (errorData.error === "illegal_argument") {
          if (errorData.error_description === "USERNAME_TOO_LONG") {
            console.warn("用户名超过64个字节！");
          } else {
            console.warn("用户名不合法！");
          }
        } else if (errorData.error === "unauthorized") {
          console.warn("注册失败，无权限！");
        } else if (errorData.error === "resource_limited") {
          console.warn("您的App用户注册数量已达上限,请升级至企业版！");
        }
      },
    };
    conn.registerUser(options);

    var options = {
      user: this.uid,
      pwd: "123456",
      appKey: WebIM.config.appkey,
      success: function () {
        console.log("login success!");
        axios
          .post("/server/addChatRoomUser", {
            channelName: that.room,
            user: String(that.uid),
          })
          .then((res) => {
            console.log(res);
            if (res.data.err == 1) {
              console.log("join success");
            } else {
              console.log("join failed");
            }
          })
          .catch((err) => console.log(err));
        //   let chatRoomoptions = {
        //     roomId: this.room, // 聊天室id
        //     success: function () {
        //       console.log("join success!");
        //     },
        //     error: function (err) {
        //       console.log("join failed", err);
        //     },
        //   };

        //   conn.joinChatRoom(chatRoomoptions);
      },
    };
    conn.open(options);

    console.log("roomid", this.room);
    // window.vue = this;
    // let socket = this.$socketio;
    // const that = this;
    // socket.on("chat message", function (msg, cb) {
    //   console.log("chat message", msg);
    //   that.chatMessage.push(msg);
    // });
  },
  methods: {
    sendMessageToRoom() {
      // let socket = this.$socketio;
      // socket.emit("chat message", {
      //   uid: this.uid,
      //   room: this.room,
      //   time: Date.now(),
      //   text: this.textarea,
      // });
      let conn = {};
      let WebIM = this.WebIM;
      conn = WebIM.conn;
      const that = this;
      let id = conn.getUniqueId(); // 生成本地消息id
      let msg = new WebIM.message("txt", id); // 创建文本消息
      let option = {
        msg: this.textarea, // 消息内容
        to: this.room, // 接收消息对象(聊天室id)
        chatType: "chatRoom", // 群聊类型设置为聊天室
        ext: {
          time: Date.now(),
        }, // 扩展消息
        success: function (e) {
          let msg = {
            uid: that.uid,
            room: option.to,
            time: option.ext.time,
            text: option.msg,
          };
          that.chatMessage.push(msg);
          console.log("send room text success", e);
        }, // 对成功的相关定义，sdk会将消息id登记到日志进行备份处理
        fail: function (e) {
          console.log("failed", e);
        }, // 对失败的相关定义，sdk会将消息id登记到日志进行备份处理
      };
      msg.set(option);
      conn.send(msg.body);
    },
  },
};
</script>

<style scoped lang="less">
.chat {
  width: 100%;
  display: flex;
  flex-direction: column;
  flex: 1;

  .chat-window {
    flex: 1;
    width: 100%;
    padding: 0;
    //width: 37vw;
    //width: 427px;
    margin: 0 auto;
    //background: #25252d;
    overflow: hidden auto;

    .chat-text {
      display: flex;
      padding: 10px;
      align-items: flex-end;

      .avatar {
        height: 100%;
        text-align: justify;
        //padding-right: 10px;
      }

      .aside {
        display: inline-flex;
        flex-direction: column;

        .label {
          width: 100%;
          height: 20px;
          font-size: 8px;
          color: gray;
        }

        .text {
          width: 100%;
          min-height: 50px;
          display: flex;

          align-content: space-around;
          align-items: center;

          p {
            position: relative;
            background: #3582e7;
            border-radius: 8px;
            width: fit-content;
            margin: 5px 10px 5px 10px;
            word-break: break-word;
            color: white;
            min-height: 18px;
            padding: 5px 10px 5px 10px;
            line-height: 18px;
            z-index: 0;
          }
          p::before {
            content: "";
            position: absolute;
            bottom: -2px;
            width: 0px;
            height: 20px;
            z-index: -1;
          }
          p::after {
            content: "";
            position: absolute;
            bottom: -2px;
            width: 0px;
            height: 20px;
            z-index: 1;
          }
        }
      }
    }

    .chat-text.left {
      .avatar {
        padding-right: 10px;
      }
      .aside {
        .text {
          justify-content: flex-start;

          p::before {
            left: -10px;
            border-left: 20px solid #3582e7;
            border-bottom-right-radius: 18px;
          }
          p::after {
            left: -20px;
            border-left: 20px solid #f2f5f6;
            border-bottom-right-radius: 18px;
          }
        }
      }
    }

    .chat-text.right {
      flex-direction: row-reverse;

      .avatar {
        padding-left: 10px;
      }

      .aside {
        .label {
          text-align: right;
        }

        .text {
          justify-content: flex-end;
          text-align: right;

          p {
            text-align: left;
          }
          p::before {
            right: -10px;
            border-right: 20px solid #3582e7;
            border-bottom-left-radius: 18px;
          }
          p::after {
            right: -20px;
            border-right: 20px solid #f2f5f6;
            border-bottom-left-radius: 18px;
          }
        }
      }
    }
  }

  .input-area {
    flex: 0;
    height: 54px;
    //background-image: linear-gradient(180deg, #292933 7%, #212129 100%);
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.3);
    list-style: none;
    display: flex;
    justify-content: space-around;
    align-items: center;
    color: #fff;

    .grid-content {
      border-radius: 4px;
      min-height: 36px;
      width: 100%;
    }
  }
}
</style>
