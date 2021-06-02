<template>
  <div id="app">
    <div class="head_box display-flex space-between">
      <div class="display-flex head_left_box">
        <img
          @click="gohome"
          src="../public/images/logo-spcw3cyc.png"
          alt=""
          srcset=""
        />
        <div class="display-flex" @click="showPicker = true">
          <div class="classified">{{ head_class_name }}</div>
          <van-icon color="#fff" size="13" name="arrow-down" />
        </div>
        <div class="my_time">
          <div class="time_style">{{ time[0] }}</div>
          <div class="time_style">{{ time[1] }}</div>
        </div>
      </div>
      <!-- <div class="head_center">
        <div>goaskme论坛</div>
        <div>|</div>
        <div>goaskme云课堂</div>
      </div> -->
      <div class="display-flex head_right_box" @click="showLogin">
        <div style="" class="login">
          <p>{{ name }}</p>
        </div>
        <img :src="headimg" alt="" />
        <div class="exit" v-if="exit_show" @click.stop="exit">退出</div>
      </div>
    </div>
    <router-view :key="$route.fullPath" />
    <!-- 登陆 -->

    <van-popup
      :value="this.$store.state.loginStatus"
      @click-overlay="hideLogin"
    >
      <div class="login_box">
        <div class="login_title">登陆</div>
        <van-field
          style="width: 300px"
          v-model="username"
          label="账号"
          placeholder="请输入账号"
        />
        <van-field
          v-model="password"
          type="password"
          label="密码"
          placeholder="请输入密码"
        />

        <div class="login_button" @click="login">登陆</div>
      </div>
    </van-popup>
    <!-- 课程选择 -->
    <van-popup v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showPicker = false"
        @confirm="onConfirm"
      />
    </van-popup>
  </div>
</template>
<script>
import { login, search, getclassescate } from "./api/api";
import { getTime } from "../utils/getTime";
export default {
  data() {
    return {
      columns: [],
      value: "",
      showPicker: false,
      head_class_name: "课程分类",
      username: "",
      password: "",
      time: "",
      time1: "",
      name: localStorage.username || "GAM论坛账号登录",
      headimg: localStorage.headimg || require("../public/images/denglu.png"),
      exit_show: localStorage.token || "",
    };
  },
  created() {
    this.time1 = setInterval(() => {
      this.time = getTime().split(" ");
    }, 1000);
  },
  mounted() {
    // console.log(process.env);
    getclassescate().then((res) => {
      // console.log("树数据", res);
      res.data.data.forEach((item) => {
        item.text = item.cate_name;
        item.children = item.child;
        item.children.forEach((items) => {
          items.text = items.cate_name;
        });
      });
      this.columns = res.data.data;
    });
  },
  methods: {
    showLogin() {
      this.$store.commit("showLogin");
    },
    hideLogin() {
      // console.log(this.$store.state.loginStatus);
      this.$store.commit("hideLogin");
    },
    exit() {
      localStorage.removeItem("token");
      localStorage.removeItem("headimg");
      localStorage.removeItem("username");
      this.name = "GAM论坛账号登录";
      this.headimg = require("../public/images/denglu.png");
      this.exit_show = false;
    },
    onConfirm(e) {
      console.log(e);
      let cate1 = "",
        cate2 = "";
      this.columns.forEach((item) => {
        if (item.text === e[0]) {
          cate1 = item.id;
          item.children.forEach((items) => {
            if (items.text === e[1]) {
              cate2 = items.id;
              return;
            }
          });
        }
      });
      console.log(cate1, cate2);
      this.showPicker = false;
      this.$router.push({
        path: "/release",
        query: {
          cate1,
          cate2,
        },
      });
      // this.head_class_name = e[1];
    },
    login() {
      let params = new FormData();
      params.append("username", this.username);
      params.append("password", this.password);
      login(params)
        .then((res) => {
          if (res.data.status == 0) {
            localStorage.token = res.data.data.user.token;
            localStorage.headimg = res.data.data.user.headimg;
            localStorage.username = res.data.data.user.username;
            this.headimg = res.data.data.user.headimg;
            this.name = res.data.data.user.username;
            this.$store.commit("hideLogin");
            this.exit_show = true;
          } else {
            this.$toast(res.data.message);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    gohome() {
      this.$router.replace("/");
    },
  },
};
</script>
<style scoped>
#app {
  width: 100%;
  height: 100%;
}
.login {
  color: #fff;
  margin-right: 20px;
  font-size: 26px;
  width: 250px;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  display: flex;
  justify-content: flex-end;
}
.login p {
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}
.exit {
  color: #fff;
  margin-left: 20px;
  font-size: 26px;
}
.head_box {
  background-color: #ff8c00;
  height: 100px;
  padding: 0 20px;
  /* justify-content: space-between;
  display: flex; */
}
.display-flex {
  display: flex;
  align-items: center;
}
.space-between {
  justify-content: space-between;
}

.head_left_box img {
  width: 134px;
  height: 42px;
}
.head_right_box img {
  width: 45px;
  height: 45px;
  border-radius: 50px;
}
.classified {
  color: #fff;
  margin-left: 20px;
  font-size: 26px;
}
.login_box {
  padding: 30px 15px;
}
.login_button {
  display: flex;
  justify-content: center;
  align-items: center;
  color: #fff;
  background: #ff8c00;
  /* margin-left: 35%; */
  padding: 20px 30px;
  border-radius: 10px;
  margin-left: 30px;
  margin-right: 30px;
  margin-top: 60px;
}
.login_title {
  font-size: 40px;
  color: #333333;
  display: flex;
  justify-content: center;
}
.my_time {
  color: #fff;
  font-size: 12px;
  white-space: nowrap;
  margin-left: 10px;
}
.time_style {
  display: flex;
  font-size: 24px;
  justify-content: center;
}
</style>

