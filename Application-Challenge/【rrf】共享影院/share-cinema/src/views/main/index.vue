<template>
  <el-container>
    <el-header>
      <div>
        <el-avatar
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
        ></el-avatar>
      </div>
      <div>
        <el-input v-model="channelName" placeholder="请输入房间号">
          <el-button
            slot="append"
            icon="el-icon-search"
            @click="joinVideoRoom(channelName)"
            >确定</el-button
          >
        </el-input>
      </div>
    </el-header>
    <el-container>
      <el-aside width="auto">
        <div class="recommend-aside">
          <div><p>猜你想聊</p></div>
          <div class="user">
            <div class="avatar">
              <el-avatar
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              ></el-avatar>
            </div>
            <div class="aside">
              <div class="label">五婶萨达 <span>在线</span></div>
              <div class="info">
                <div>你们看过19部相同的作品</div>
                <div>
                  你们共同的标签 <el-tag type="info">运动</el-tag
                  ><el-tag type="info">青春</el-tag
                  ><el-tag type="info">冒险</el-tag>
                </div>
                <div>ta正在看<b>《排球少年》</b></div>
                <div class="join-button"><el-button>加入房间</el-button></div>
              </div>
            </div>
          </div>
          <div class="user">
            <div class="avatar">
              <el-avatar
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              ></el-avatar>
            </div>
            <div class="aside">
              <div class="label">乱武 <span>在线</span></div>
              <div class="info">
                <div>你们看过7部相同的作品</div>
                <div>
                  你们共同的标签 <el-tag type="info">动作</el-tag
                  ><el-tag type="info">冒险</el-tag
                  ><el-tag type="info">历史</el-tag>
                </div>
                <div>ta正在看<b>《觉醒年代》</b></div>
                <div class="join-button"><el-button>加入房间</el-button></div>
              </div>
            </div>
          </div>
          <div class="user">
            <div class="avatar">
              <el-avatar
                src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png"
              ></el-avatar>
            </div>
            <div class="aside">
              <div class="label">青231321dsa <span>在线</span></div>
              <div class="info">
                <div>你们看过3部相同的作品</div>
                <div>
                  你们共同的标签 <el-tag type="info">冒险</el-tag
                  ><el-tag type="info">革命</el-tag
                  ><el-tag type="info">悬疑</el-tag>
                </div>
                <div>ta正在看<b>《追踪疑犯》</b></div>
                <div class="join-button"><el-button>加入房间</el-button></div>
              </div>
            </div>
          </div>
          <div class="control">
            <el-button type="primary" icon="el-icon-refresh-right"
              >换一批</el-button
            >
            <el-divider></el-divider>
            <el-switch
              style="display: block"
              v-model="visiableSwitcher"
              active-color="#13ce66"
              inactive-color="#ff4949"
              active-text=""
              inactive-text="允许其他用户看到你"
            >
            </el-switch>
          </div>
        </div>
      </el-aside>
      <el-main>
        <div class="carousel">
          <el-carousel indicator-position="outside" type="card">
            <el-carousel-item v-for="item in carouselData" :key="item.id + 18">
              <div>
                <el-image fit="fit" :src="'/server/' + item.image">
                  <div slot="placeholder" class="image-slot">
                    加载中<span class="dot">...</span>
                  </div></el-image
                >
              </div>
            </el-carousel-item>
          </el-carousel>
        </div>
        <div class="title">热门影视</div>
        <div class="container">
          <div
            v-for="item in containerData.movie_list"
            :key="item.id"
            class="content"
          >
            <div @click="createVideoPage(item.id)">
              <el-image fit="fit" :src="'/server/' + item.image">
                <div slot="placeholder" class="image-slot">
                  加载中<span class="dot">...</span>
                </div></el-image
              >
              <div class="info">
                <div>
                  {{ item.name }}
                  <el-tag v-for="t in item.type" :key="t" type="info">{{
                    t
                  }}</el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div class="title">网易游戏</div>
        <div class="container">
          <div
            v-for="item in containerData.game_list"
            :key="item.id"
            class="content"
          >
            <div @click="createVideoPage(item.id)">
              <el-image fit="fit" :src="'/server/' + item.image">
                <div slot="placeholder" class="image-slot">
                  加载中<span class="dot">...</span>
                </div></el-image
              >
              <div class="info">
                <div>
                  {{ item.name }}
                  <el-tag v-for="t in item.type" :key="t" type="info">{{
                    t
                  }}</el-tag>
                </div>
              </div>
            </div>
          </div>
        </div>
      </el-main>
    </el-container>
  </el-container>
</template>

<script>
import axios from "axios";
import { message } from "../../components/message";

export default {
  name: "main",
  data() {
    return {
      carouselData: [],
      containerData: [],
      channelName: "",
      visiableSwitcher: true,
    };
  },
  mounted() {
    axios
      .get("/server/video_list")
      .then((res) => {
        this.containerData = res.data;
        this.carouselData = res.data.ad_list;
      })
      .catch((err) => console.log(err));
  },
  methods: {
    createVideoPage(id) {
      let channelName = Math.random().toFixed(5).slice(-5);
      // 连接同步视频socket
      let socket = this.$socketio;
      this.$socketio.on("my_response", function (msg, cb) {
        console.log("socket_response", msg);
      });
      this.$socketio.emit("join", { room: channelName, id: id });
      const { path = "single" } = this.$route.query;
      this.$router.push({
        path: `/${path}`,
        query: { channelName, id },
      });
    },
    joinVideoRoom(channelName) {
      console.log(channelName);
      let socket = this.$socketio;
      let that = this;
      this.$socketio.on("id_response", function (msg, cb) {
        console.log("id_response", msg);
        if (msg.err == 1) {
          const { path = "single" } = that.$route.query;
          let id = msg.id;
          that.$router.push({
            path: `/${path}`,
            query: { channelName, id },
          });
        } else {
          message("当前无该房间！");
        }
      });
      this.$socketio.emit("join", { room: channelName });
    },
  },
};
</script>

<style lang="less" scoped>
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
  width: unset;
  max-width: 28%;
}

.recommend-aside {
  margin: 10px 10px 10px 32px;
  padding: 0 20px 20px 20px;
  //background: white;
  border: solid gainsboro 1px;
  border-radius: 4px;
  .user {
    display: flex;
    padding: 10px;

    .avatar {
      height: 100%;
      text-align: justify;
      margin-right: 10px;
      //padding-right: 10px;
    }

    .aside {
      display: inline-flex;
      flex-direction: column;

      .label {
        width: 100%;
        height: 20px;
        font-size: 15px;
        color: gray;

        span {
          color: #409eff;
        }
      }
      .info {
        font-size: 13px;

        div {
          margin: 5px 0 5px 0;
        }

        .join-button {
          text-align: right;

          button {
            padding: 0;
            background: none;
            border: none;
            color: #409eff;
          }
        }
      }
    }
  }

  .control {
    .el-button {
      background: none;
      border: none;
      color: #409eff;
      padding: 0;
    }

    .el-divider {
      margin: 10px;
    }
    .el-switch {
      text-align: right;
    }
  }
}

.el-main {
  display: flex;
  align-items: flex-start;
  flex-direction: column;
  margin-left: 32px;
}
.carousel {
  display: block;
  width: 80%;
  min-width: 1200px;

  .el-carousel__item h3 {
    color: #475669;
    font-size: 18px;
    opacity: 0.75;
    line-height: 300px;
    margin: 0;
  }

  .el-carousel__item:nth-child(2n) {
    background-color: #99a9bf;
    border-radius: 8px;
  }

  .el-carousel__item:nth-child(2n + 1) {
    background-color: #d3dce6;
    border-radius: 8px;
  }
}

.title {
  font-size: 30px;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  margin: 50px 0 10px 0;
  align-self: auto;
  width: 80%;
  min-width: 1200px;
}

.container {
  display: flex;
  flex-wrap: wrap;
  width: 80%;
  min-width: 1200px;
  justify-content: space-between;

  .content {
    width: 28%;
    height: auto;
    margin: 0 0 20px 0;
    cursor: pointer;

    .el-image {
      border-radius: 8px;
    }

    .info {
      margin: 5px 0 0 0;
      .el-tag {
        border: none;
      }
    }
  }
}
</style>