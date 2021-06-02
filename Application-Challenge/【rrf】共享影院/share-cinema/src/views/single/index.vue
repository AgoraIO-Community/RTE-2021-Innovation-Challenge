<template>
  <el-container style="height: 100%" direction="vertical">
    <el-header>
      <div>
        <el-avatar
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        ></el-avatar>
      </div>
      <div>
        <el-breadcrumb separator="/">
          <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
          <el-breadcrumb-item
            >房间 {{ $route.query.channelName }}</el-breadcrumb-item
          >
        </el-breadcrumb>
      </div>
    </el-header>
    <el-container>
      <el-main>
        <div class="video_chat">
          <div class="video">
            <VideoPage
              v-if="videoNow"
              :src="src"
              :content="content"
              :uid="this.localUid"
            ></VideoPage>
          </div>
          <div class="intro">
            <div class="title">
              <b>{{ content.name }}</b>
              <el-tag v-for="t in content.type" :key="t" type="info">{{
                t
              }}</el-tag>
            </div>
            <div style="margin-bottom: 10px; margin-top: 5px">
              <div
                style="
                  margin-right: 10px;
                  font-size: 14px;
                  font-weight: bold;
                  margin-bottom: 5px;
                "
              >
                {{ content.info }}
              </div>

              <div class="hor">
                <el-image
                  :src="'/server/' + content.image"
                  style="width: 240px; height: 135px"
                  fit="fit"
                ></el-image>
                <div style="flex: 1; margin-left: 10px">
                  {{ content.content }}
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="content">
          <div class="tab-bar">
            <el-button
              type="info"
              v-if="isSilence"
              icon="el-icon-turn-off-microphone"
              @click="setOrRelieveSilence"
            ></el-button>
            <el-button
              type="info"
              v-else
              icon="el-icon-microphone"
              @click="setOrRelieveSilence"
            ></el-button>
            <el-button
              type="danger"
              icon="el-icon-phone-outline"
              @click="handleOver"
            ></el-button>
            <el-button
              type="info"
              v-if="isStop"
              icon="el-icon-video-camera"
              @click="stopOrOpenVideo"
            ></el-button>
            <el-button
              type="info"
              v-else
              icon="el-icon-video-camera-solid"
              @click="stopOrOpenVideo"
            ></el-button>
          </div>
          <div ref="videoWindow" class="video-window"></div>
          <ChatView
            :uid="this.localUid"
            :room="this.$route.query.channelName"
          ></ChatView>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>
<script>
import { message } from "../../components/message";
import AgoraRTC from "agora-rtc-sdk-ng";
import config from "../../../config";
import { getToken } from "../../common";
import VideoPage from "../video/index.vue";
import axios from "axios";

export default {
  name: "single",
  components: { VideoPage },
  data() {
    return {
      isSilence: false,
      isDesc: true,
      isStop: false,
      desc: "等待对方进入...",
      rtc: {
        // 用来放置本地客户端
        client: null,
        // 用来放置本地音视频频轨道对象
        localAudioTrack: null,
        localVideoTrack: null,
      },
      localUid: Math.ceil(Math.random() * 1e5),
      videoData: [],
      src: "",
      description: "",
      type: [],
      name: "",
      img: "",
      content: "",
      chatHeight: 0,
      videoNow: false,
      popover_visible: false,
    };
  },
  mounted() {
    // 这段不能删
    let that = this;
    setTimeout(function () {
      that.videoNow = true;
    }, 500);

    // 获取后台数据
    window.self = this;
    axios
      .get("/server/video_list")
      .then((res) => {
        this.videoData = res.data.game_list.concat(res.data.movie_list);
        console.log("videoData", this.videoData);
        this.createVideoPage(this.$route.query.id);
      })
      .catch((err) => console.log(err));

    this.initRTC();
  },
  destroyed() {
    try {
      this.localStream.destroy();
      WebRTC2.destroy();
    } catch (e) {
      // 为了兼容低版本，用try catch包裹一下
    }
  },
  methods: {
    initRTC() {
      // 初始化音视频实例
      console.warn("初始化音视频sdk");

      this.rtc.client = AgoraRTC.createClient({ mode: "rtc", codec: "vp8" });
      //this.rtc.client.setClientRole('host');

      //监听事件
      this.rtc.client.on("user-published", async (user, mediaType) => {
        console.warn(`${user} 发布订阅`);

        // 开始订阅远端用户。
        await this.rtc.client.subscribe(user, mediaType);
        console.log("subscribe success");

        // 表示本次订阅的是视频。
        if (mediaType === "video") {
          // 订阅完成后，从 `user` 中获取远端视频轨道对象。
          const remoteVideoTrack = user.videoTrack;

          // 动态插入一个 DIV 节点作为播放远端视频轨道的容器。
          const videoWindow = this.$refs.videoWindow;
          const playerContainer = document.createElement("div");
          // 给这个 DIV 节点指定一个 ID，这里指定的是远端用户的 UID。
          playerContainer.id = user.uid.toString();
          playerContainer.style.width = videoWindow.clientWidth + "px";
          playerContainer.style.height =
            (videoWindow.clientWidth / 16) * 9 + "px";
          playerContainer.class = "main-window";
          videoWindow.append(playerContainer);

          // 订阅完成，播放远端音视频。
          // 传入 DIV 节点，让 SDK 在这个节点下创建相应的播放器播放远端视频。
          remoteVideoTrack.play(playerContainer);

          // 也可以只传入该 DIV 节点的 ID。
          // remoteVideoTrack.play(playerContainer.id);
        }

        // 表示本次订阅的是音频。
        if (mediaType === "audio") {
          // 订阅完成后，从 `user` 中获取远端音频轨道对象。
          const remoteAudioTrack = user.audioTrack;
          // 播放音频因为不会有画面，不需要提供 DOM 元素的信息。
          remoteAudioTrack.play();
        }
      });

      this.rtc.client.on("user-unpublished", (user, mediaType) => {
        console.warn(`${user} 停止订阅`);
        if (mediaType === "video") {
          // 获取刚刚动态创建的 DIV 节点。
          const playerContainer = document.getElementById(user.uid.toString());
          // 销毁这个节点。
          playerContainer.remove();
        }
      });

      // 将获取到的 Token 赋值给 join 方法中的 token 参数，然后加入频道
      this.fetchToken(
        this.localUid,
        String(this.$route.query.channelName)
      ).then((token) => {
        this.rtc.client
          .join(
            config.appid,
            String(this.$route.query.channelName),
            token,
            this.localUid
          )
          .then((uid) => {
            console.info("加入房间成功，开始初始化本地音视频流");
            this.initLocalStream();
          })
          .catch((error) => {
            console.error("加入房间失败：", error);
            message(`${error}: 请检查appkey或者token是否正确`);
            this.returnJoin();
          });
      });
    },
    fetchToken(uid, channelName) {
      return new Promise(function (resolve) {
        axios
          .post("/server/getToken", {
            uid: uid,
            channelName: channelName,
          })
          .then(function (response) {
            const token = response.data;
            console.log(response.data);
            resolve(token);
          })
          .catch(function (error) {
            console.log(error);
          });
      });
    },
    createVideoPage(id) {
      console.log(id);
      console.log(this.videoData[id - 1]);
      this.src = this.videoData[id - 1].url;
      this.content = this.videoData[id - 1];
      console.log("1231123", this.src);
    },
    returnJoin(time = 2000) {
      setTimeout(() => {
        this.$router.push({
          path: "/",
        });
      }, time);
    },
    initLocalStream() {
      // 获取所有音视频设备。
      AgoraRTC.getDevices().then((devices) => {
        const audioDevices = devices.filter(function (device) {
          return device.kind === "audioinput";
        });
        const videoDevices = devices.filter(function (device) {
          return device.kind === "videoinput";
        });

        if (videoDevices[0]) {
          var selectedCameraId = videoDevices[0].deviceId;

          AgoraRTC.createCameraVideoTrack({ cameraId: selectedCameraId })
            .then((videoTrack) => {
              console.log("create Camera success!");
              this.rtc.localVideoTrack = videoTrack;
              this.rtc.client.publish(this.rtc.localVideoTrack).then(() => {
                console.log("publish Camera success!");
                const videoWindow = this.$refs.videoWindow;
                const playerContainer = document.createElement("div");
                // 给这个 DIV 节点指定一个 ID，这里指定的是远端用户的 UID。
                playerContainer.id = this.localUid;
                playerContainer.style.width = videoWindow.clientWidth + "px";
                playerContainer.style.height =
                  (videoWindow.clientWidth / 16) * 9 + "px";
                playerContainer.class = "main-window";
                videoWindow.append(playerContainer);
                videoTrack.play(playerContainer);
                this.isStop = false;
              });
            })
            .catch((err) => {
              console.warn(err);
              this.isStop = true;
            });
        }
        if (audioDevices[0]) {
          var selectedMicrophoneId = audioDevices[0].deviceId;
          AgoraRTC.createMicrophoneAudioTrack({
            microphoneId: selectedMicrophoneId,
          })
            .then((audioTrack) => {
              console.log("create Audio success!");
              this.rtc.localAudioTrack = audioTrack;

              this.rtc.client.publish(this.rtc.localAudioTrack).then(() => {
                console.log("publish Audio success!");
                this.isSilence = false;
              });
            })
            .catch((err) => {
              console.warn(err);
              this.isSilence = true;
            });
        }
      });

      console.log("publish success!");
    },
    setOrRelieveSilence() {
      const { isSilence } = this;
      if (this.isSilence) {
        console.warn("关闭mic");
        this.rtc.client
          .unpublish(this.rtc.localAudioTrack)
          .then(() => {
            console.warn("关闭 mic sucess");
            this.isSilence = !isSilence;
          })
          .catch((err) => {
            console.warn("关闭 mic 失败: ", err);
            message("关闭 mic 失败");
          });
      } else {
        console.warn("打开mic");
        if (!this.rtc.localAudioTrack) {
          message("当前不能打开mic");
          return;
        }
        this.rtc.client
          .publish(this.rtc.localAudioTrack)
          .then(() => {
            console.warn("打开mic sucess");
            this.isSilence = !isSilence;
          })
          .catch((err) => {
            console.warn("打开mic失败: ", err);
            message("打开mic失败");
          });
      }
    },
    stopOrOpenVideo() {
      const { isStop } = this;
      if (this.isStop) {
        console.warn("关闭摄像头");
        this.rtc.client
          .unpublish(this.rtc.localVideoTrack)
          .then(() => {
            console.warn("关闭摄像头 sucess");
            // 获取刚刚动态创建的 DIV 节点。
            const playerContainer = document.getElementById(this.localUid);
            // 销毁这个节点。
            playerContainer.remove();
            this.isStop = !isStop;
          })
          .catch((err) => {
            console.warn("关闭摄像头失败: ", err);
            message("关闭摄像头失败");
          });
      } else {
        console.warn("打开摄像头");
        if (!this.rtc.localVideoTrack) {
          message("当前不能打开camera");
          return;
        }
        this.rtc.client
          .publish(this.rtc.localVideoTrack)
          .then(() => {
            const videoWindow = this.$refs.videoWindow;
            const playerContainer = document.createElement("div");
            // 给这个 DIV 节点指定一个 ID，这里指定的是远端用户的 UID。
            playerContainer.id = this.localUid;
            playerContainer.style.width = videoWindow.clientWidth + "px";
            playerContainer.style.height =
              (videoWindow.clientWidth / 16) * 9 + "px";
            playerContainer.class = "main-window";
            videoWindow.append(playerContainer);
            videoTrack.play(playerContainer);
            this.rtc.localVideoTrack = videoTrack;
            this.isStop = false;
          })
          .catch((err) => {
            console.warn("打开摄像头失败: ", err);
            message("打开摄像头失败");
          });
      }
    },
    handleOver() {
      console.warn("离开房间");
      this.rtc.client.leave();
      this.returnJoin(1);
    },
  },
};
</script>

<style scoped lang="less">
.el-container {
  /*设置内部填充为0，几个布局元素之间没有间距*/
  padding: 0px;
  /*外部间距也是如此设置*/
  margin: 0px;
  /*统一设置高度为100%*/
  height: 100%;
}

.el-header {
  display: flex;
  align-items: center;
  padding: 0 20px;
  background: #c5daeb;

  div {
    margin: 0 10px 0 10px;
  }
}

.el-aside {
  background-color: #e9eef3;
  color: white;
  line-height: 30px;
  display: flex;
  flex-direction: column;
}

.el-main {
  display: flex;
  flex-direction: row;
  justify-content: center;
  background-color: #f2f5f6;

  .content {
    width: 20%;
    margin: 0 20px 0 20px;
    background: #f2f5f6;
    border: solid gainsboro 1px;
    border-radius: 4px;
    display: flex;
    flex-direction: column;
  }
}

.video-window {
  display: flex;
  flex-direction: column;

  .main-window {
    flex: 1;
    flex-grow: 1;
    //width: 37vw;
    //width: 427px;
    margin: 0 auto;
    //background: #25252d;
  }
}

.video_chat {
  display: flex;
  flex-direction: column;
  justify-content: center;
  width: 60%;

  .video {
  }
}
.intro {
  padding: 10px;
  background: white;
  border-width: 2px;
  border-radius: 4px;
  //width: 80%;

  .title {
    b {
      font-size: 25px;
      margin-right: 10px;
    }
    .el-tag {
      border: none;
      background-color: white;
    }
  }
}

.hor {
  display: flex;
  flex-direction: row;
  align-items: center;

  .el-button {
    padding: 0;
    line-height: unset;
  }
}

.tab-bar {
  flex: inherit;
  height: 54px;
  background-color: #c9dee4;
  box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.3);
  list-style: none;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;

  li {
    height: 54px;
    width: 125px;
    cursor: pointer;
    //静音
    &.silence {
      background: url("../../assets/img/icon/silence.png") no-repeat center;
      background-size: 60px 54px;

      &:hover {
        background: url("../../assets/img/icon/silence-hover.png") no-repeat
          center;
        background-size: 60px 54px;
      }

      &:active {
        background: url("../../assets/img/icon/silence-click.png") no-repeat
          center;
        background-size: 60px 54px;
      }

      &.isSilence {
        //已经开启静音
        background: url("../../assets/img/icon/relieve-silence.png") no-repeat
          center;
        background-size: 60px 54px;

        &:hover {
          background: url("../../assets/img/icon/relieve-silence-hover.png")
            no-repeat center;
          background-size: 60px 54px;
        }

        &:active {
          background: url("../../assets/img/icon/relieve-silence-click.png")
            no-repeat center;
          background-size: 60px 54px;
        }
      }
    }

    //结束按钮
    &.over {
      background: url("../../assets/img/icon/over.png") no-repeat center;
      background-size: 68px 36px;

      &:hover {
        background: url("../../assets/img/icon/over-hover.png") no-repeat center;
        background-size: 68px 36px;
      }

      &:active {
        background: url("../../assets/img/icon/over-click.png") no-repeat center;
        background-size: 68px 36px;
      }
    }

    // 停止按钮
    &.stop {
      background: url("../../assets/img/icon/stop.png") no-repeat center;
      background-size: 60px 54px;

      &:hover {
        background: url("../../assets/img/icon/stop-hover.png") no-repeat center;
        background-size: 60px 54px;
      }

      &:active {
        background: url("../../assets/img/icon/stop-click.png") no-repeat center;
        background-size: 60px 54px;
      }

      //已经是停止状态
      &.isStop {
        background: url("../../assets/img/icon/open.png") no-repeat center;
        background-size: 60px 54px;

        &:hover {
          background: url("../../assets/img/icon/open-hover.png") no-repeat
            center;
          background-size: 60px 54px;
        }

        &:active {
          background: url("../../assets/img/icon/open-click.png") no-repeat
            center;
          background-size: 60px 54px;
        }
      }
    }
  }
}
</style>
