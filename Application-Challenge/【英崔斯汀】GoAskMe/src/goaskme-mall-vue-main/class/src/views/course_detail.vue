<template>
  <div>
    <div class="breadcrumb_box">
      <el-breadcrumb separator=">">
        <el-breadcrumb-item :to="{ path: '/' }">首页</el-breadcrumb-item>
        <el-breadcrumb-item>{{ class_detail.name }}</el-breadcrumb-item>
      </el-breadcrumb>
    </div>
    <div class="body_box">
      <div class="body_name_box display-flex">
        <div class="class_img_box">
          <img class="class_img" :src="class_detail.img" alt="" />
          <img
            v-if="class_detail.verdictTime"
            src="../../public/images/yijieshu.png"
            class="class_img1"
            alt=""
          />
        </div>
        <div style="margin-left: 30px">
          <div style="font-size: 30px; color: #282828">
            {{ class_detail.name }}
          </div>
          <div class="class_title" style="">
            {{ class_detail.title }}
          </div>
          <div class="class_title" style="">
            教室名称：{{ class_detail.roomname }}
          </div>
          <div class="class_title" style="">
            课程时间：{{ class_detail.start_time }} 至
            {{ class_detail.end_time }}
          </div>
          <div class="class_title" style="">
            教室类型：{{ class_detail.roomtype }}
          </div>
          <div class="display-flex price_box">
            <div style="font-size: 36px; color: #b20000">
              {{ class_detail.price
              }}<span style="font-size: 18px; color: #b20000">积分</span>
            </div>
            <div
              v-if="class_detail.verdictTime"
              style="
                width: 120px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #dddddd;
                color: #fff;
                margin-left: 25px;
                border-radius: 5px;
              "
            >
              已结束
            </div>
            <div
              v-else
              @click="change"
              style="
                width: 120px;
                height: 40px;
                display: flex;
                align-items: center;
                justify-content: center;
                background-color: #ff8c00;
                color: #fff;
                margin-left: 25px;
                border-radius: 5px;
              "
            >
              立即兑换
            </div>
          </div>
        </div>
      </div>
      <div class="body_nvg_box">
        <button :class="[active == 1 ? 'on_button' : '']" @click="active = 1">
          介绍
        </button>
        <button :class="[active == 2 ? 'on_button' : '']" @click="active = 2">
          评价
        </button>
      </div>

      <div
        v-if="active == 1 && class_detail.description != ''"
        style="padding: 44px 34px"
      >
        <!-- <textview name="">
          {{ class_detail.description }}
        </textview> -->
        <div v-html="class_detail.description"></div>
      </div>
      <div
        style="
          height: 200px;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 24px;
          color: #d2d2d2;
        "
        v-if="active == 1 && class_detail.description == ''"
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
            <p style="margin-top: 27px; font-size: 14px; color: #6b6b6b">
              {{ item.content }}
            </p>
            <div v-for="(items, index1) in item._child" :key="index1">
              <div style="padding: 36px 16px">
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
                <p style="margin-top: 27px; font-size: 14px; color: #6b6b6b">
                  {{ items.content }}
                </p>
              </div>
            </div>
          </div>
        </div>
      </div>
      <div
        style="
          height: 200px;
          display: flex;
          justify-content: center;
          align-items: center;
          font-size: 24px;
          color: #d2d2d2;
        "
        v-if="active == 2 && comment.length == 0"
      >
        没有相关评论
      </div>
    </div>
  </div>
</template>
<script>
import { getClass, exchange } from "@/api/api";
import { Message } from "element-ui";
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
        res.data.data.info.verdictTime = verdictTime(res.data.data.info.end_time);
        this.class_detail = res.data.data.info;
        this.comment = res.data.data.comment;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
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
              Message.error(res.data.message);
            } else {
              this.$alert(
                `您购买的课程教室密码：${res.data.data.password}，教室名：${res.data.data.roomname}，教室类型：${res.data.data.roomtype}, 请自行记录好以上信息，按照课程时间至https://live.goaskme.app准时上课。`,
                "购买成功",
                {
                  confirmButtonText: "确定",
                  callback: (action) => {
                    this.$router.go(-1);
                  },
                }
              );
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
.breadcrumb_box {
  /* margin-top: 80px; */
  width: 1200px;
  margin: 80px auto;
}
::v-deep .el-breadcrumb__inner.is-link {
  color: #8c8c8c;
}
::v-deep .el-breadcrumb__item:last-child .el-breadcrumb__inner {
  color: #8c8c8c;
}
.body_box {
  width: 1200px;
  margin: 0 auto;
  border: 1px solid #d2d2d2;
}
.display-flex {
  display: flex;
}
.body_name_box {
  padding: 10px;
}
.body_name_box .class_img {
  width: 280px;
  height: 210px;
}
.price_box {
  margin-top: 35px;
}
.body_nvg_box {
  height: 50px;
  background: #d2d2d2;
  display: flex;
  padding: 10px 14px;
}
/* .body_content_box {
} */
button {
  background-color: #d2d2d2;
  border: none;
  color: #6b6b6b;
  outline: none;
  padding: 0;
  text-align: center;
  text-decoration: none;
  display: inline-block;
  font-size: 14px;
  padding: 8px 13px;
}
.on_button {
  background-color: #282828;
  color: #fff;
  border-radius: 5px;
}
button:last-child {
  margin-left: 27px;
}
.t_comment_box {
  padding: 30px 15px;
}
.class_title {
  font-size: 14px;
  color: #6b6b6b;
  margin-right: 30px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  width: 800px;
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
