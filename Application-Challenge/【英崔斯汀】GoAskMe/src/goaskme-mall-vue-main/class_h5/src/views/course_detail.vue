<template>
  <div>
    <div class="navigation">
      <div @click="gohome">首页 ></div>
      <div
        style="
          white-space: nowrap;
          overflow: hidden;
          text-overflow: ellipsis;
          width: 50%;
        "
      >
        {{ class_detail.name }}
      </div>
    </div>
    <div style="padding: 25px">
      <div style="border: 1px solid #d2d2d2">
        <div class="class_info_box" style="display: flex">
          <div class="class_img_box">
            <img class="class_img" :src="class_detail.img" alt="" srcset="" />
            <img
              v-if="class_detail.verdictTime"
              src="../../public/images/yijieshu.png"
              class="class_img1"
              alt=""
            />
          </div>
          <div class="class_name_box">
            <div class="class_name">
              {{ class_detail.name }}
            </div>
            <div class="class_money">
              {{ class_detail.price }}<span class="jifen">积分</span>
            </div>
            <div class="conversion_box">
              <span class="conversion1" v-if="class_detail.verdictTime"
                >已结束</span
              >
              <span class="conversion" v-else @click="change">立即兑换</span>
            </div>
          </div>
        </div>
        <div class="class_referral">
          {{ class_detail.title }}
        </div>
        <div class="class_referral">教室名称：{{ class_detail.roomname }}</div>
        <div class="class_referral">
          课程时间：{{ class_detail.start_time }} 至 {{ class_detail.end_time }}
        </div>
        <div class="class_referral">教室类型：{{ class_detail.roomtype }}</div>
        <div class="body_nvg_box">
          <div
            :class="[active == 1 ? 'on_button' : 'button']"
            @click="active = 1"
          >
            介绍
          </div>
          <div
            :class="[active == 2 ? 'on_button' : 'button']"
            @click="active = 2"
          >
            评价
          </div>
        </div>
        <div
          v-if="active == 1 && !!class_detail.description"
          style="padding: 34px 16px"
        >
          <div v-html="class_detail.description"></div>
        </div>
        <div
          style="
            height: 100px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 16px;
            color: #d2d2d2;
          "
          v-if="active == 1 && !class_detail.description"
        >
          没有相关介绍
        </div>
        <div class="body_content_box">
          <div v-if="active == 2 && comment.length != 0" class="t_comment_box">
            <div v-for="(item, index) in comment" :key="index">
              <div style="display: flex">
                <img
                  style="width: 56px; height: 56px; border-radius: 50%"
                  :src="item.headimg"
                />
                <div
                  style="
                    display: flex;
                    align-items: center;
                    margin-left: 26px;
                    color: #ff8c00;
                  "
                >
                  {{ item.username }}
                </div>
                <div
                  style="display: flex; align-items: center; margin-left: 26px"
                >
                  {{ item.created_at }}
                </div>
              </div>
              <p style="margin-top: 10px; font-size: 14px; color: #6b6b6b">
                {{ item.content }}
              </p>
              <div v-for="(items, index1) in item._child" :key="index1">
                <div style="padding: 16px 16px">
                  <div style="display: flex">
                    <img
                      style="width: 56px; height: 56px; border-radius: 50%"
                      :src="items.headimg"
                    />
                    <div
                      style="
                        display: flex;
                        align-items: center;
                        margin-left: 26px;
                        color: #ff8c00;
                      "
                    >
                      {{ items.username }}
                    </div>
                    <div
                      style="
                        display: flex;
                        align-items: center;
                        margin-left: 26px;
                      "
                    >
                      {{ items.created_at }}
                    </div>
                  </div>
                  <p style="margin-top: 10px; font-size: 14px; color: #6b6b6b">
                    {{ items.content }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>
        <div
          style="
            height: 100px;
            display: flex;
            justify-content: center;
            align-items: center;
            font-size: 16px;
            color: #d2d2d2;
          "
          v-if="active == 2 && comment.length == 0"
        >
          没有相关评论
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import { getClass, exchange } from "@/api/api";
import { Dialog } from "vant";
import verdictTime from "../../utils/time";
export default {
  name: "",
  data() {
    return {
      active: 1,
      class_detail: {},
      comment: [],
    };
  },
  components: {},
  computed: {},
  beforeMount() {},
  mounted() {
    getClass(this.$route.query.id)
      .then((res) => {
        console.log(res);
        res.data.data.info.description = res.data.data.info.description.replace(
          /\<img/gi,
          '<img style="width:100%;height:auto" '
        );
        res.data.data.info.verdictTime = verdictTime(
          res.data.data.info.end_time
        );
        this.class_detail = res.data.data.info;
        this.comment = res.data.data.comment;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    gohome() {
      this.$router.replace("/index");
    },
    change() {
      // console.log(this.class_detail);
      if (!!localStorage.token) {
        let params = new FormData();
        params.append("type", 1);
        params.append("obj_id", this.class_detail.id);
        params.append("obj_name", this.class_detail.name);
        exchange(params)
          .then((res) => {
            console.log(res);
            if (res.data.status != 0) {
              this.$toast(res.data.message);
            } else {
              Dialog.alert({
                title: "购买成功",
                // message: `您购买的课程教室密码为${res.data.data.password}\n自行记录好教室密码\n请按照课程时间至edu.goaskme.app参与课程`,
                message: `您购买的课程教室密码：${res.data.data.password}，教室名：${res.data.data.roomname}，教室类型：${res.data.data.roomtype}, 请自行记录好以上信息，按照课程时间至https://live.goaskme.app准时上课。 `,
              }).then(() => {
                // on close
                this.$router.go(-1);
              });
            }
          })
          .catch((err) => {
            console.log(err);
          });
      } else {
        this.$store.commit("showLogin");
      }
    },
  },
  watch: {},
};
</script>
<style scoped>
.body_nvg_box {
  height: 70px;
  background: #d2d2d2;
  display: flex;
  align-items: center;
  padding-left: 20px;
}
.class_info_box {
  padding: 12px;
}
.navigation {
  display: flex;
  padding: 50px 50px 0 50px;
  font-size: 26px;
}
.on_button {
  background-color: #282828;
  color: #fff;
  border-radius: 5px;
  /* padding: 9px 15px; */
  width: 77px;
  height: 42px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.button {
  width: 77px;
  height: 42px;
  /* padding: 9px 15px; */
  display: flex;
  justify-content: center;
  align-items: center;
}
.t_comment_box {
  padding: 30px 15px;
}
.conversion_box {
  display: flex;
  /* justify-content: center; */
  align-items: center;
}
.conversion {
  background: #ff8c00;
  color: #fff;
  border-radius: 10px;
  padding: 6px 10px;
  font-size: 24px;
}
.conversion1 {
  background: #dddddd;
  color: #fff;
  border-radius: 10px;
  padding: 6px 10px;
  font-size: 24px;
}
.class_name {
  font-size: 36px;
  width: 300px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.class_img {
  width: 280px;
  height: 200px;
}
.class_money {
  color: #b20000;
  font-size: 40px;
  font-weight: bold;
  margin-top: 30px;
}
.jifen {
  font-size: 24px;
  color: #b20000;
}
.class_name_box {
  margin-left: 20px;
}
.class_referral {
  font-size: 24px;
  font-weight: 400;
  color: #6b6b6b;
  padding: 12px;
}
.class_img_box {
  position: relative;
  top: 0;
  left: 0;
}
.class_img1 {
  position: absolute;
  top: 0;
  right: 10px;
  width: 80px;
  height: 80px;
}
</style>
