<template>
  <div>
    <van-search
      v-model="search"
      shape="round"
      @search="go_search"
      placeholder="请输入搜索关键词"
    />
    <div v-for="(item, index) in class_list" :key="index">
      <h1 class="title_box">{{ item.cate_name }}</h1>
      <div class="class_list_root_box" v-if="item.classes.length != 0">
        <div
          class="class_list_box1"
          v-for="(items, index1) in item.classes"
          :key="index1"
          @click="go_detail(index, index1)"
        >
          <div class="class_list_box">
            <div class="img_box">
              <img :src="items.img" class="class_img" alt="" srcset="" />
              <img
                v-if="items.verdictTime"
                src="../../public/images/yijieshu.png"
                class="class_img1"
                alt=""
              />
            </div>
            <div class="class_name_box">
              <div class="class_name">{{ items.name }}</div>
              <div class="class_name">{{ items.title }}</div>
            </div>
          </div>
        </div>
      </div>
      <div v-else class="nothave">没有相关数据</div>
    </div>
    <div class="issue_box" @click="go_issue">
      <img src="../../public/images/fabu.jpg" alt="" />
    </div>
  </div>
</template>
<script>
import { getClassList } from "@/api/api";
import verdictTime from "../../utils/time";
export default {
  name: "",
  data() {
    return {
      search: "",
      value: [],
      class_list: [],
    };
  },
  components: {},
  computed: {},
  beforeMount() {},

  mounted() {
    getClassList()
      .then((res) => {
        console.log(res);
        res.data.data.forEach((item) => {
          if (item.classes) {
            item.classes.forEach((element) => {
              // console.log(verdictTime(element.end_time));
              element.verdictTime = verdictTime(element.end_time);
            });
          }
        });
        this.class_list = res.data.data;
      })
      .catch((err) => {
        console.log(err);
      });
  },
  methods: {
    // handleChange(value) {
    //   console.log(value);
    // },
    go_detail(index, indexs) {
      this.$router.push({
        name: "Course_detail",
        query: { id: this.class_list[index].classes[indexs].id },
      });
    },
    go_issue() {
      if (!!localStorage.token) {
        this.$router.push("/issue");
      } else {
        this.$store.commit("showLogin");
      }
    },
    go_search() {
      this.$router.push({
        path: "/search",
        query: {
          keyword: this.search,
        },
      });
    },
  },
  watch: {},
};
</script>
<style scoped>
.class_list_box {
  margin-top: 40px;
  width: 300px;
  border: 1px solid #d2d2d2;
}
.class_list_box1 {
  width: 50%;
  display: flex;
  justify-content: center;
}
.class_list_box .class_img {
  width: 300px;
  height: 220px;
  justify-content: center;
}
.class_list_root_box {
  display: flex;
  flex-wrap: wrap;
  /* justify-content: space-between; */
}
.issue_box {
  position: fixed;
  left: 80%;
  top: 60%;
}
.issue_box img {
  width: 110px;
  height: 110px;
}
.class_name_box {
  padding: 0 6px;
}
.title_box {
  font-size: 40px;
  font-weight: 400;
  color: #232323;
  margin-left: 20px;
  margin-top: 40px;
}
/* .title_box:first-child {
  margin-top: 0;
} */
.class_name {
  width: 300px;
  overflow: hidden;
  white-space: nowrap;
  text-overflow: ellipsis;
}
.nothave {
  display: flex;
  justify-content: center;
  margin-top: 10px;
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
