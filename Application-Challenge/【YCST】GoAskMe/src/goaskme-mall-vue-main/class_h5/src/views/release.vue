<template>
  <div>
    <div class="class_list_root_box" v-if="class_list.length != 0">
      <div
        class="class_list_box1"
        v-for="(item, index) in class_list"
        :key="index"
        @click="go_detail(item.id)"
      >
        <div class="class_list_box">
          <div class="img_box">
            <img :src="item.img" class="course_img" alt="" srcset="" />
            <img
              v-if="item.verdictTime"
              src="../../public/images/yijieshu.png"
              class="class_img1"
              alt=""
            />
          </div>
          <div class="class_name_box">
            <div class="class_name">{{ item.name }}</div>
            <div class="class_name">{{ item.title }}</div>
          </div>
        </div>
      </div>
    </div>
    <div v-else class="nothave">没有相关数据</div>
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
.class_list_box1 {
  width: 50%;
  display: flex;
  justify-content: center;
}
.class_list_box {
  margin-top: 40px;
  width: 300px;
  border: 1px solid #d2d2d2;
}
.class_list_box .course_img {
  width: 300px;
  height: 220px;
  justify-content: center;
}
.class_name_box {
  padding: 0 6px;
}
.class_name {
  width: 300px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.class_list_root_box {
  display: flex;
  flex-wrap: wrap;
  /* justify-content: space-between; */
}
.nothave {
  display: flex;
  justify-content: center;
  margin-top: 20px;
  color: #d2d2d2;
  font-size: 32px;
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
