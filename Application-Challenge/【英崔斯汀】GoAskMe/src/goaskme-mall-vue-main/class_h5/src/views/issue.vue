<template>
  <div class="issue_box">
    <van-field
      :border="false"
      v-model="title_name"
      label="课程标题"
      placeholder="请输入课程标题"
    />
    <div style="display: flex">
      <van-field
        :border="false"
        v-model="class_money"
        label="价格"
        placeholder="请输入价格"
      />
      <div class="jifen">积分</div>
    </div>
    <div class="display-flex">
      <div class="title_name class_category">课程类型</div>
      <div class="class_category_left" @click="showPicker = true">
        {{ class_category }}
      </div>
    </div>
    <div class="display-flex">
      <div class="title_name class_category">教室类型</div>
      <div class="class_category_left" @click="class_type_showPicker = true">
        {{ class_type }}
      </div>
    </div>
    <div class="display-flex class_logo">
      <div class="title_name">课程封面</div>
      <img
        class="cover_img"
        v-if="avatar != ''"
        :src="avatar"
        alt=""
        srcset=""
      />
      <input
        class="file_input"
        :class="avatar ? 'marleft1' : 'marleft'"
        type="file"
        accept="image/png, image/jpeg, image/gif, image/jpg"
        @change="changepic($event)"
      />
    </div>
    <van-field
      :border="false"
      v-model="textarea"
      label="课程简介"
      rows="1"
      autosize
      type="textarea"
      placeholder="请输入课程简介"
    />
    <van-field
      :border="false"
      v-model="roomname"
      label="教室名称"
      placeholder="请输入教室名称"
    />
    <div class="display-flex">
      <div class="title_name class_category">课程时间</div>
      <div class="class_category_left" @click="showPicker1 = true">
        {{ start_time }}
      </div>
      <div class="class_category_left1">至</div>
      <div class="class_category_left1" @click="showPicker2 = true">
        {{ end_time }}
      </div>
    </div>
    <div class="class_ingo">
      <div class="title_name">课程详细</div>
      <div>
        <vue-editor v-model="editor_content" />
      </div>
      <div class="submit" @click="submit">提交</div>
    </div>
    <!-- 课程选择 -->
    <van-popup v-model="showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns"
        @cancel="showPicker = false"
        @confirm="onConfirm"
      />
    </van-popup>
    <!-- 课程类别 -->
    <van-popup v-model="class_type_showPicker" round position="bottom">
      <van-picker
        show-toolbar
        :columns="columns1"
        @cancel="class_type_showPicker = false"
        @confirm="onChange"
      />
    </van-popup>
    <!-- 开始时间选择 -->
    <van-popup v-model="showPicker1" round position="bottom">
      <van-datetime-picker
        v-model="currentDate"
        type="datetime"
        title="选择完整时间"
        :min-date="minDate"
        :max-date="maxDate"
        @confirm="startTimeChoose"
      />
    </van-popup>
    <!-- 结束时间选择 -->
    <van-popup v-model="showPicker2" round position="bottom">
      <van-datetime-picker
        v-model="currentDate1"
        type="datetime"
        title="选择完整时间"
        :min-date="minDate"
        :max-date="maxDate"
        @confirm="endTimeChoose"
      />
    </van-popup>
  </div>
</template>
<script>
import { VueEditor } from "vue2-editor";
import { getclassescate, addClass } from "../api/api";
import { Dialog } from "vant";
export default {
  name: "",
  components: {
    VueEditor,
  },
  data() {
    return {
      content: null,
      editorOption: {},
      input: "",

      editor_content: "", //富文本
      title_name: "", //name
      class_money: "", //price
      textarea: "", //title
      avatar: "", //img
      value: "",
      class_category: "请选择",
      index1: "", //cate1
      index2: "", //cate2
      columns: [],
      class_type_showPicker: false, //课程类别
      columns1: ["一对一", "小班课", "大班课"],
      class_type: "请选择",
      showPicker: false,
      showPicker1: false,
      showPicker2: false,
      start_time: "开始时间",
      end_time: "结束时间",
      minDate: new Date(2020, 0, 1),
      maxDate: new Date(2025, 10, 1),
      currentDate: new Date(),
      currentDate1: new Date(),
      roomname: "",
    };
  },
  mounted() {
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
    changepic(e) {
      var file = e.target.files[0];
      var reader = new FileReader();
      var that = this;
      reader.readAsDataURL(file);
      reader.onload = function (e) {
        that.avatar = this.result;
        console.log(that.avatar);
      };
    },
    submit() {
      if (
        !this.title_name ||
        !this.textarea ||
        !this.class_money ||
        this.class_category == "请选择" ||
        !this.editor_content ||
        !this.avatar ||
        this.start_time == "开始时间" ||
        this.end_time == "结束时间" ||
        !this.roomname ||
        this.class_type == "请选择"
      ) {
        console.log(
          this.title_name,
          this.textarea,
          this.class_money,
          this.class_category,
          this.editor_content,
          this.avatar
        );
        this.$toast("请完善信息");
      } else {
        Dialog.confirm({
          message: `是否确认发布？`,
        })
          .then(() => {
            let params = new FormData();
            params.append("name", this.title_name);
            params.append("price", this.class_money);
            params.append("img", this.avatar);
            params.append("description", this.class_category);
            params.append("title", this.textarea);
            params.append("cate1", this.index1);
            params.append("cate2", this.index2);
            params.append("start_time", this.start_time);
            params.append("end_time", this.end_time);
            params.append("roomname", this.roomname);
            if (this.class_type == "一对一") {
              params.append("classtype", 1);
            } else if (this.class_type == "小班课") {
              params.append("classtype", 2);
            } else if (this.class_type == "大班课") {
              params.append("classtype", 3);
            }
            addClass(params)
              .then((res) => {
                if (res.data.status == 0) {
                  Dialog.alert({
                    title: "发布成功",
                    message: `您开设的课程教室密码：${res.data.data.password}，教室名：${res.data.data.roomname}，教室类型：${res.data.data.roomtype}, 请自行记录好以上信息，按照课程时间至https://live.goaskme.app准时开课。`,
                  }).then(() => {
                    this.$router.go(-1);
                  });
                } else {
                  this.$toast(res.data.message);
                }
                // console.log(res);
                //
              })
              .catch((err) => {
                console.log(err);
              });
          })
          .catch(() => {});
      }
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
      this.index1 = cate1;
      this.index2 = cate2;
      this.showPicker = false;
      this.class_category = e[1];
      // this.head_class_name = e[1];
    },
    onChange(e) {
      // console.log(e);
      this.class_type = e;
      this.class_type_showPicker = false;
    },
    startTimeChoose(e) {
      this.showPicker1 = false;
      let start_time = new Date(e);
      this.start_time =
        start_time.getFullYear() +
        "-" +
        (start_time.getMonth() + 1) +
        "-" +
        start_time.getDate() +
        " " +
        start_time.getHours() +
        ":" +
        start_time.getMinutes() +
        ":" +
        start_time.getSeconds();
    },
    endTimeChoose(e) {
      this.showPicker2 = false;
      let end_time = new Date(e);
      this.end_time =
        end_time.getFullYear() +
        "-" +
        (end_time.getMonth() + 1) +
        "-" +
        end_time.getDate() +
        " " +
        end_time.getHours() +
        ":" +
        end_time.getMinutes() +
        ":" +
        end_time.getSeconds();
    },
  },
};
</script>
<style scoped>
.display-flex {
  display: flex;
  align-items: center;
  margin-top: 21px;
}
::v-deep .el-input__inner {
  width: 520px;
}
::v-deep .el-input {
  width: 520px;
}
.cover_img {
  margin-left: 120px;
  width: 120px;
  height: 120px;
}
.marleft {
  margin-left: 140px;
}
.marleft1 {
  margin-left: 20px;
}
.title_name {
  white-space: nowrap;
  font-size: 3.733333vw;
  font-weight: 400;
  margin-left: 30px;
  color: #646566;
}
/* .file_input{
  background: #FF8C00;
} */
.submit {
  width: 320px;
  margin: 0 auto;
  height: 50px;
  font-size: 20px;
  background: #ff8c00;
  color: #fff;
  display: flex;
  justify-content: center;
  align-items: center;
  margin-top: 30px;
  padding: 15px 30px;
  border-radius: 10px;
  margin-bottom: 20px;
}
.jifen {
  display: flex;
  width: 100px;
  justify-content: center;
  align-items: center;
  font-size: 3.733333vw;
  font-weight: 400;
  margin-left: 30px;
  color: #646566;
}
.class_ingo {
  margin-top: 40px;
}
.class_category {
  margin-top: 20px;
}
.class_category_left {
  margin-left: 90px;
  font-size: 3.733333vw;
  color: #646566;
  margin-top: 15px;
}
.class_category_left1 {
  font-size: 3.733333vw;
  color: #646566;
  margin-top: 15px;
}
.class_category_left1 {
  margin-left: 20px;
  font-size: 3.733333vw;
  color: #646566;
  margin-top: 15px;
}
.class_logo {
  margin: 40px 0;
}
</style>
