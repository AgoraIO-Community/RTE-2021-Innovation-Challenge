<template>
  <div class="page_box">
    <div
      style="display: flex"
      v-if="class_list.length != 0"
      class="course_class_box"
    >
      <div class="course_box" v-for="(item, index) in class_list" :key="index">
        <div class="img_box">
          <img
            class="course_img"
            :src="item.img"
            alt=""
            @click="go_detail(item.id)"
          />
          <img
            v-if="item.verdictTime"
            src="../../public/images/yijieshu.png"
            class="class_img1"
            alt=""
          />
        </div>
        <p class="course_name">{{ item.name }}</p>
        <p class="course_detail">{{ item.title }}</p>
      </div>
    </div>
    <div v-else class="nothas">没有数据</div>
  </div>
</template>
<script>
import { search } from "../api/api";
import verdictTime from "../../utils/time";
export default {
  name: "",
  data() {
    return {
      keyword: "",
      class_list: [],
    };
  },
  components: {},
  computed: {},
  beforeMount() {},
  mounted() {
    let params = null;
    if (!!this.$route.query.keyword) {
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
        // if (res.data.data.length == 0) {
        //   this.$router.go(-1);
        // } else {
        res.data.data.forEach((item) => {
          if (item.end_time) {
            item.verdictTime = verdictTime(item.end_time);
          }
        });
        this.class_list = res.data.data;
        // }
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    go_detail(id) {
      this.$router.push({
        name: "Course_detail",
        query: { id: id },
      });
    },
  },
  watch: {},
};
</script>
<style scoped>
.page_box {
  width: 1200px;
  margin: 0 auto;
}
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
  margin-left: 120px;
  align-items: center;
}
.head_right_login_box p {
  color: #fff;
  height: 100%;
  width: auto;
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
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  padding-left: 8px;
}
.course_detail {
  font-size: 14px;
  color: #6c6c6c;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
  padding-left: 8px;
}
.course_class_box {
  display: flex;
  flex-wrap: wrap;
}
.nothas {
  font-size: 30px;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 300px;
  color: #d2d2d2;
}
.img_box {
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
