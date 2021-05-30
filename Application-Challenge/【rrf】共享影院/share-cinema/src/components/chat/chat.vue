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
          <div class="label">{{ item.uid }} {{ new Date(item.time).toLocaleTimeString("zh-CN") }}</div>
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
    };
  },
  mounted() {
    window.vue = this;
    let socket = this.$socketio;
    const that = this;
    socket.on("chat message", function (msg, cb) {
      console.log("chat message", msg);
      that.chatMessage.push(msg);
    });
  },
  methods: {
    sendMessageToRoom() {
      let socket = this.$socketio;
      socket.emit("chat message", {
        uid: this.uid,
        room: this.room,
        time: Date.now(),
        text: this.textarea,
      });
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
