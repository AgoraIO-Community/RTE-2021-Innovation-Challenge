<template>
  <div id="app">
    <div class="head_box">
      <div
          style="
          margin: 0 auto;
          width: 1200px;
          display: flex;
          justify-content: space-between;
        "
      >
        <div class="head_left_box">
          <img
              class="head_img"
              src="../public/images/logo-spcw3cyc.png"
              alt=""
              srcset=""
              @click="gohome"
          />
          <el-cascader
              :show-all-levels="false"
              class="cascader"
              ref="cascader"
              v-model="cascader"
              :options="options"
              :props="{ expandTrigger: 'hover' }"
              @change="handleChange"
              size="mini"
          ></el-cascader>
        </div>
        <div class="head_center">
          <div @click="go_luntan">GAM论坛</div>
          <div style="padding: 0 5px">|</div>
          <div @click="go_class">GAM云课堂</div>
        </div>
        <div class="head_center">
          {{ time }}
        </div>
        <div class="head_right_box">
          <div class="head_right_input_box">
            <el-input
                v-model="keyword"
                placeholder="请输入课程关键词"
                @keyup.enter.native="search"
            ></el-input>
            <img
                @click="class_seek"
                class="head_right_input_img"
                src="../public/images/sousuo.png"
                alt=""
            />
          </div>
          <div class="head_right_login_box" @click="showLogin">
            <div class="user_name">
              <p>{{ name }}</p>
            </div>
            <div style="display: flex; align-items: center">
              <img :src="headimg" alt="" @mouseover="show_menu"/>
              <div v-if="menu_show && exit_show" @mouseleave="hide_menu" id="my_menu"
                   style="position: fixed;top: 35px;z-index: 9999;background-color: whitesmoke;opacity:0.9;filter:alpha(opacity=90);text-align: center;margin-top:1px;padding: 0 10px;border-radius: 20px;">
                <div><span style="font-size: 16px;font-weight: bold">积分余额：{{ point }}</span></div>
                <div class="my_menu" @click="buy_class"><span style="font-size: 16px;">购买课程记录</span></div>
                <div class="my_menu" @click="classing"><span style="font-size: 16px;">正在进行课程</span></div>
                <div class="my_menu" @click="finish_class"><span style="font-size: 16px;">已完成课程</span></div>
                <div class="my_menu" @click="open_class" style="margin: 0 0 10px 0;"><a
                    style="font-size: 16px;">已发布课程</a></div>
              </div>
            </div>
            <div v-if="exit_show" class="exit" @click.stop="exit">退出</div>
          </div>
        </div>
      </div>
    </div>

    <div>
      <div style="text-align: center;margin-top:6px;">
        <img :src="adImg"
             style="width: 1000px;height: 120px;" @click="viewAd">
      </div>
      <div>
        <router-view :key="$route.fullPath" style="margin-top:-40px;"/>
      </div>
    </div>

    <div v-if="adView" style="position: fixed;left:28%;top:18%;" @click="hideAd">
      <video width="800" height="600" autoplay loop>
        <source :src="adViewVideo" type="video/mp4">
      </video>
    </div>

    <!-- 底部 -->
    <div style="height: 80px"></div>
    <div class="floor_box">
      <p>GoAskMe版权所有</p>
    </div>
    <!-- 登陆 -->
    <div>
      <el-dialog
          :visible.sync="this.$store.state.dialogVisible"
          :before-close="hideLogin"
          width="24%"
      >
        <div
            style="
            display: flex;
            justify-content: center;
            font-size: 18px;
            margin-bottom: 10px;
          "
        >
          登录
        </div>
        <div style="display: flex">
          <div
              style="
              white-space: nowrap;
              display: flex;
              align-items: center;
              margin-right: 10px;
            "
          >
            账号
          </div>
          <el-input placeholder="请输入内容" v-model="username"></el-input>
        </div>
        <div style="margin-top: 30px; display: flex">
          <div
              style="
              white-space: nowrap;
              display: flex;
              align-items: center;
              margin-right: 10px;
            "
          >
            密码
          </div>
          <el-input type="password" placeholder="请输入密码" v-model="password">
          </el-input>
        </div>
        <div style="margin-top: 30px; display: flex; justify-content: center">
          <div
              @click="login"
              style="
              background-color: #ff8c00;
              border-radius: 10px;
              color: #fff;
              display: flex;
              justify-content: center;
              padding: 10px 0;
              width: 50%;
            "
          >
            登陆
          </div>
        </div>
      </el-dialog>
    </div>
  </div>
</template>
<script>
import {login, search, getclassescate, get_point, get_ads, view_ad} from "./api/api";
import {Message} from "element-ui";
import {getTime} from "../utils/getTime";

export default {
  data() {
    return {
      isTeacher: 0,
      point: 0,
      cascader: "",
      username: "",
      password: "",
      input: "",
      keyword: "",
      time: "",
      time1: "",
      name: localStorage.username || "GAM论坛账号登录",
      headimg: localStorage.headimg || require("../public/images/denglu.png"),
      // dialogVisible: false,
      options: [],
      exit_show: localStorage.token || "",
      menu_show: 0,
      // 广告列表
      ads: [], // 广告列表
      adKey: 0, // 广告key
      adImg: '', // 广告图片
      adVideo: '', // 广告视频地址
      adView: 0, // 广告显示
      adViewId: 0, // 看的广告id
      adViewVideo: '', // 看的广告视频地址
      adViewTime: 0, // 看广告累计秒数
      adViewTimeInterval: null // 计时器
    };
  },
  created() {
    this.time1 = setInterval(() => {
      this.time = getTime();
    }, 1000);
  },
  mounted() {
    // 获取课程分类
    getclassescate().then((res) => {
      // console.log("树数据", res);
      res.data.data.forEach((item) => {
        item.label = item.cate_name;
        item.children = item.child;
        item.value = item.id;
        item.children.forEach((items) => {
          items.label = items.cate_name;
          items.value = items.id;
        });
      });
      this.options = res.data.data;
    });
    // 获取积分
    get_point().then((res) => {
      if (res != null && res.data != null && res.data.data != null) {
        this.point = res.data.data.point;
        this.isTeacher = res.data.data.isTeacher;
      }
    });
    // 获取广告列表
    get_ads().then((res) => {
      this.ads = res.data.data.list;
      this.adImg = this.ads[0].img;
      this.adVideo = this.ads[0].video;
      setInterval(this.changeAd, 5000);
    });
  },
  methods: {
    // 关闭广告
    hideAd() {
      this.adView = 0;
      this.adViewVideo = '';
      clearInterval(this.adViewTimeInterval);
      // 看广告视频超过10秒，给予奖励
      if (this.adViewTime > 10) {
        let params = new FormData();
        params.append("ad_id", this.adViewId);
        view_ad({params}).then((res) => {
          if (res.data.status == 0) {
            Message.success("恭喜获得观看广告奖励5积分！");
            get_point().then((res) => {
              if (res != null && res.data != null && res.data.data != null) {
                this.point = res.data.data.point;
              }
            });
          }
        });
      }
      this.adViewId = 0;
      this.adViewTime = 0;
    },
    // 循环替换广告图
    changeAd() {
      let adLength = this.ads.length;
      let key = 0;
      if (this.adKey + 1 <= this.ads.length - 1) {
        key = this.adKey + 1;
      } else {
        key = 0;
      }
      this.adKey = key;
      this.adImg = this.ads[key].img;
      this.adVideo = this.ads[key].video;
    },
    // 看广告
    viewAd() {
      this.adView = 1;
      this.adViewVideo = this.adVideo;
      this.adViewId = this.ads[this.adKey].id;
      this.adViewTime = 0;
      this.adViewTimeInterval = setInterval(this.adViewTimesStart, 1000);
    },
    // 看广告计时函数
    adViewTimesStart() {
      this.adViewTime = this.adViewTime + 1;
    },

    // 显示菜单
    show_menu() {
      this.menu_show = 1;
    },
    // 隐藏菜单
    hide_menu() {
      this.menu_show = 0;
    },
    // 购买的课程
    buy_class() {
      this.$router.push({
        name: "ClassView",
        query: {type: 1},
      });
    },
    // 正在进行的课程
    classing() {
      this.$router.push({
        name: "ClassView",
        query: {type: 2},
      });
    },
    // 已完成的课程
    finish_class() {
      this.$router.push({
        name: "ClassView",
        query: {type: 3},
      });
    },
    // 已发布的课程
    open_class() {
      if (this.isTeacher == 0) {
        Message.error("抱歉，您不是教师组！");
        return 0;
      }
      this.$router.push({
        name: "ClassView",
        query: {type: 4},
      });
    },

    go_luntan() {
      window.location.href = "https://goaskme.app";
    },
    go_class() {
      window.location.href = "https://edu.goaskme.app";
    },
    exit() {
      localStorage.removeItem("token");
      localStorage.removeItem("headimg");
      localStorage.removeItem("username");
      this.name = "GAM论坛账号登录";
      this.headimg = require("../public/images/denglu.png");
      this.exit_show = false;
      // localStorage.token = res.data.data.user.token;
      // localStorage.headimg = res.data.data.user.headimg;
      // localStorage.username = res.data.data.user.username;
    },
    handleChange(e) {
      this.$router.push({
        path: "/release",
        query: {
          cate1: e[0],
          cate2: e[1],
        },
      });
      this.cascader = "";
    },
    search_list(data) {
      let params = null;
      if (!!data && data.keyword) {
        params = {
          keyword: this.$route.query.keyword,
        };
      } else {
        params = {
          cate1: this.$route.query.cate1,
          cate2: this.$route.query.cate2,
        };
      }

      search(params)
          .then((res) => {
            console.log(res);
            this.class_list = res.data.data;
          })
          .catch((err) => {
            console.log(err);
          });
    },
    showLogin() {
      // console.log(
      //   localStorage.token,
      //   localStorage.username,
      //   localStorage.headimg
      // );
      if (
          !localStorage.token ||
          !localStorage.username ||
          !localStorage.headimg
      ) {
        this.$store.commit("showLogin");
      }
    },
    hideLogin() {
      this.$store.commit("hideLogin");
    },
    gohome() {
      this.$router.replace("/index");
    },
    login() {
      let params = new FormData();
      params.append("username", this.username);
      params.append("password", this.password);
      login(params)
          .then((res) => {
            console.log(res);
            if (res.data.status == 0) {
              localStorage.token = res.data.data.user.token;
              localStorage.headimg = res.data.data.user.headimg;
              localStorage.username = res.data.data.user.username;
              this.headimg = res.data.data.user.headimg;
              this.name = res.data.data.user.username;
              this.$store.commit("hideLogin");
              this.exit_show = true;
              get_point().then((res) => {
                if (res != null && res.data != null && res.data.data != null) {
                  this.point = res.data.data.point;
                  this.isTeacher = res.data.data.isTeacher;
                }
              });
              // this.$router.go(0);
            } else {
              Message.error(res.data.message);
            }
          })
          .catch((err) => {
            console.log(err);
          });
    },
    search() {
      let obj = {};
      obj.stopPropagation = () => {
      };
      try {
        this.$refs.cascader.clearValue(obj);
      } catch (err) {
        this.$refs.cascader.handleClear(obj);
      }
      this.$router.push({
        path: "/search",
        query: {
          keyword: this.keyword,
        },
      });
    },
    class_seek() {
      //组织冒泡
      let obj = {};
      obj.stopPropagation = () => {
      };
      try {
        this.$refs.cascader.clearValue(obj);
      } catch (err) {
        this.$refs.cascader.handleClear(obj);
      }
      this.$router.push({
        path: "/search",
        query: {
          keyword: this.keyword,
        },
      });

      // console.log(1);
      // let params = new FormData();
      // params.append("keyword", this.keyword);
      // search(params)
      //   .then((res) => {
      //     console.log(res);
      //   })
      //   .catch((err) => {
      //     console.log(err);
      //   });
    },
    // handleClose1(done) {
    //   this.$confirm("确认关闭？")
    //     .then((_) => {
    //       done();
    //     })
    //     .catch((_) => {});
    // },
  },
};
</script>
<style scoped>
.head_box {
  background-color: #ff8c00;
  height: 60px;
  /* justify-content: space-between;
  display: flex; */
}

.head_left_box {
  display: flex;
}

.head_right_box {
  display: flex;
  /* margin-right: 360px; */
  line-height: 60px;
  width: 500px;
}

.head_img {
  /* padding: 13px 360px; */
  margin-top: 13px;
  /* margin-left: 360px; */
  width: 140px;
  height: 34px;
}

.cascader {
  margin-left: 32px;
  line-height: 60px;
  background-color: transparent;
}

::v-deep .el-cascader .el-input .el-input__inner {
  background: transparent;
  border: none;
  color: #fff;
  width: 120px;
}

::v-deep .el-input__suffix {
  color: #fff;
}

::v-deep .el-input__inner::-webkit-input-placeholder {
  color: #fff;
}

.head_right_input_box {
  width: 279px;
  background: #cc7000;
  height: 34px;
  margin-top: 15px;
  border-radius: 50px;
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
}

.head_right_box input {
  /* border-radius: 50%; */
  background: transparent;
  width: 100%;
  height: 100%;
  margin-left: 25px;
  margin-right: 34px;
  color: #fff;
  border: 0;
  outline: none;
}

.head_right_box input::-webkit-input-placeholder {
  color: #fff;
}

.head_right_input_img {
  width: 33px;
  height: 33px;
  border-radius: 50%;
  position: absolute;
  top: 0;
  right: 0;
}

.head_right_login_box {
  display: flex;
  height: 60px;
  margin-left: 80px;
  align-items: center;
}

.head_right_login_box .user_name {
  color: #fff;
  height: 100%;
  width: 110px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  justify-content: flex-end;
}

.head_right_login_box .user_name p {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.head_right_login_box img {
  width: 33px;
  height: 33px;
  border-radius: 50%;
  margin-left: 11px;
}

.body_box {
  width: 1200px;
  margin: 0 auto;
}

.course_box {
  border: 1px solid #d2d2d2;
  width: 280px;
  margin-top: 50px;
  margin-left: 10px;
}

.course_img {
  width: 280px;
  height: 200px;
}

.floor_box {
  height: 60px;
  width: 100%;
  background-color: #ff8c00;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  position: fixed;
  left: 0;
  bottom: 0;
}

.body_box h1 {
  font-size: 20px;
  font-weight: 600;
  color: #232323;
}

.course_name {
  font-size: 16px;
  color: #282828;
}

.course_detail {
  font-size: 14px;
  color: #6c6c6c;
}

.course_class_box {
  display: flex;
  flex-wrap: wrap;
}

/* .floor_box::after{
  clear: both;
} */
.exit {
  color: #fff;
  margin-left: 10px;
  white-space: nowrap;
}

.head_center {
  display: flex;
  align-items: center;
  color: #fff;
  font-family: "test";
}

.my_menu:hover {
  background-color: #232323;
  color: white;
}
</style>

