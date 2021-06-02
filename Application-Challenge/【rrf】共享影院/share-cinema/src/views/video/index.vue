<template>
  <div>
    <video-player
      class="video-player vjs-custom-skin"
      ref="videoPlayer"
      :playsinline="true"
      :options="playerOptions"
    ></video-player>
  </div>
</template>

<script>
export default {
  name: "VideoPage",
  props: {
    src: {
      type: String,
      required: true,
    },
    content: {
      type: Object,
      required: true,
    },
    uid: {
      type: Number,
      required: true,
    },
  },
  data() {
    return {
      playerOptions: {
        autoplay: false,
        muted: false,
        loop: false,
        preload: "auto",
        language: "zh-CN",
        aspectRatio: "16:9",
        fluid: true,
        sources: [
          {
            type: "video/mp4",
            src: "",
          },
        ],
        techOrder: ["html5", "flash"],
        width: document.documentElement.clientWidth,
      },
      lock: false,
      localUid: this.uid,
    };
  },
  mounted() {
    let socket = this.$socketio;
    let channel = this.$route.query.channelName;
    let player = this.$refs.videoPlayer.player;
    let _this = this;
    console.log("why not?", socket, player, this.lock);
    // 进度条跳转
    socket.on("seeking_response", function (msg, cb) {
      if (msg.uid == _this.localUid) return;
      _this.lock = true;
      player.currentTime(msg.time);
      setTimeout(function () {
        _this.lock = false;
      }, 500);
    });
    player.on("seeking", function () {
      if (_this.lock) return;
      let time = this.currentTime();
      console.log("video_seeking", time);
      socket.emit("video_seeking", {
        room: channel,
        time: time,
        uid: _this.localUid,
      });
    });

    // 播放
    socket.on("play_response", function (msg, cb) {
      if (msg.uid == _this.localUid) return;
      _this.lock = true;
      player.play();
      setTimeout(function () {
        _this.lock = false;
      }, 500);
    });
    player.on("play", function () {
      if (_this.lock) return;
      socket.emit("video_play", { room: channel, uid: _this.localUid });
    });
    // 暂停
    socket.on("pause_response", function (msg, cb) {
      if (msg.uid == _this.localUid) return;
      _this.lock = true;
      player.pause();
      setTimeout(function () {
        _this.lock = false;
      }, 500);
    });
    player.on("pause", function () {
      if (_this.lock) return;
      console.log("video_pause");
      socket.emit("video_pause", { room: channel, uid: _this.localUid });
    });
  },
  watch: {
    src: {
      handler: function (newval, oldval) {
        this.playerOptions.sources[0].src = '/server/' + newval;
      },
      immediate: true,
    },
  },
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped lang="scss">
</style>
