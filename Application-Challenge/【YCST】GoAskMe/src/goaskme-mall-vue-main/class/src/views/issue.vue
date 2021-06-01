<template>
  <div class="issue_box">
    <div class="display-flex">
      <div class="title_name">课程标题</div>
      <el-input v-model="title_name" placeholder="请输入标题内容"></el-input>
    </div>
    <div class="display-flex">
      <div class="title_name">价格</div>
      <el-input
        v-model="class_money"
        class="marleft"
        placeholder="请输入标题内容"
      ></el-input>
      <div style="font-size: 18px; color: #282828; margin-left: 10px">积分</div>
    </div>
    <div class="display-flex">
      <div class="title_name">课程类型</div>
      <el-dropdown>
        <span class="el-dropdown-link">
          {{ class_title_name1
          }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>

        <el-dropdown-menu slot="dropdown">
          <div
            @click="click_dro(index)"
            v-for="(item, index) in options1"
            :key="index"
          >
            <el-dropdown-item>{{ item.cate_name }}</el-dropdown-item>
          </div>
        </el-dropdown-menu>
      </el-dropdown>
      <el-dropdown v-if="options2.length != 0" style="margin-left: 10px">
        <span class="el-dropdown-link">
          {{ class_title_name2
          }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>
        <el-dropdown-menu slot="dropdown">
          <div
            @click="click_dro1(index1)"
            v-for="(items, index1) in options2"
            :key="index1"
          >
            <el-dropdown-item>{{ items.cate_name }}</el-dropdown-item>
          </div>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="display-flex">
      <div class="title_name">教室类型</div>
      <el-dropdown>
        <span class="el-dropdown-link">
          {{ class_title_type
          }}<i class="el-icon-arrow-down el-icon--right"></i>
        </span>

        <el-dropdown-menu slot="dropdown">
          <div
            @click="click_dro2(index)"
            v-for="(item, index) in options3"
            :key="index"
          >
            <el-dropdown-item>{{ item.label }}</el-dropdown-item>
          </div>
        </el-dropdown-menu>
      </el-dropdown>
    </div>
    <div class="display-flex">
      <div class="title_name">课程封面</div>
      <img
        class="cover_img"
        v-if="avatar != ''"
        :src="avatar"
        alt=""
        srcset=""
      />
      <input
        class="marleft file_input"
        type="file"
        accept="image/png, image/jpeg, image/gif, image/jpg"
        @change="changepic($event)"
      />
    </div>
    <div class="display-flex">
      <div class="title_name">教室名称</div>
      <el-input v-model="roomname" placeholder="请输入教室名称"></el-input>
    </div>
    <div class="display-flex">
      <div class="title_name">课程时间</div>
      <el-date-picker
        v-model="value1"
        type="datetimerange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        @blur="gettime"
      >
      </el-date-picker>
    </div>

    <div class="display-flex">
      <div class="title_name">课程简介</div>
      <el-input
        type="textarea"
        :rows="4"
        style="width: 520px"
        placeholder="请输入内容"
        v-model="textarea"
      >
      </el-input>
    </div>
    <div style="margin-top: 21px">
      <div class="title_name">课程详细</div>
      <div>
        <el-card style="height: 450px">
          <quill-editor
            v-model="content"
            ref="myQuillEditor"
            style="height: 360px"
            :options="editorOption"
            @blur="onEditorBlur($event)"
            @change="onEditorChange($event)"
          >
          </quill-editor>
        </el-card>
      </div>
      <div class="submit" @click="submit">提交</div>
    </div>
  </div>
</template>
<script>
import { getclassescate, addClass } from "../api/api";
import { quillEditor } from "vue-quill-editor";
import "quill/dist/quill.core.css";
import "quill/dist/quill.snow.css";
import "quill/dist/quill.bubble.css";
import { Message } from "element-ui";
export default {
  name: "",
  components: {
    quillEditor,
  },
  data() {
    return {
      class_title_name1: "请选择",
      class_title_name2: "请选择",
      content: null,
      editorOption: {},
      title_name: "", //name
      class_money: "", //price
      textarea: "", //title
      avatar: "", //img
      options1: [],
      options2: [],
      index1: "", //cate1
      index2: "", //cate2
      index3: "", //classtype
      editor: "",
      value1: [],
      roomname: "",
      class_title_type: "请选择",
      options3: [
        {
          value: "1",
          label: "一对一",
        },
        {
          value: "2",
          label: "小班课",
        },
        {
          value: "3",
          label: "大班课",
        },
      ],
    };
  },
  mounted() {
    getclassescate().then((res) => {
      this.options1 = res.data.data;
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
        // console.log(that.avatar);
      };
    },
    onEditorBlur(e) {
      // console.log(e);
    },
    onEditorChange(editor) {
      // this.content = editor.html;
      this.editor = editor.html;
    },
    gettime(e) {
      // console.log(e);
    },
    submit() {
      console.log(this.value1);

      if (
        !this.title_name ||
        !this.textarea ||
        !this.class_money ||
        this.class_title_name2 == "请选择" ||
        !this.editor ||
        !this.avatar ||
        this.value1.length == 0 ||
        !this.roomname ||
        this.class_title_type == "请选择"
      ) {
        Message.error("请完善信息");
      } else {
        this.$alert("是否确认发布？", {
          confirmButtonText: "确定",
          callback: (action) => {
            if (action === "confirm") {
              let start_time = new Date(this.value1[0]);
              let end_time = new Date(this.value1[1]);
              start_time =
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
              end_time =
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
              let params = new FormData();
              params.append("name", this.title_name);
              params.append("price", this.class_money);
              params.append("img", this.avatar);
              params.append("description", this.editor);
              params.append("title", this.textarea);
              params.append("cate1", this.options1[this.index1].id);
              params.append("cate2", this.options2[this.index2].id);
              params.append("start_time", start_time);
              params.append("end_time", end_time);
              params.append("roomname", this.roomname);
              params.append("classtype", this.index3);
              addClass(params)
                .then((res) => {
                  console.log(res);
                  if (res.data.status == 0) {
                    this.$alert(
                      `您开设的课程教室密码：${res.data.data.password}，教室名：${res.data.data.roomname}，教室类型：${res.data.data.roomtype}, 请自行记录好以上信息，按照课程时间至https://live.goaskme.app准时开课。`,
                      "发布成功",
                      {
                        confirmButtonText: "确定",
                        callback: (action) => {
                          this.$router.go(-1);
                        },
                      }
                    );
                  } else {
                    Message.error(res.data.message);
                  }
                })
                .catch((err) => {
                  console.log(err);
                });
            }
          },
        });
      }
    },
    click_dro(index) {
      console.log(index);
      this.class_title_name1 = this.options1[index].cate_name;
      this.options2 = this.options1[index].child;
      this.class_title_name2 = "请选择";
      this.index1 = index;
    },
    click_dro1(index) {
      console.log(index);
      this.class_title_name2 = this.options2[index].cate_name;
      this.index2 = index;
    },
    click_dro2(index) {
      console.log(index);
      this.class_title_type = this.options3[index].label;
      this.index3 = index + 1;
    },
  },
};
</script>
<style scoped>
.issue_box {
  width: 1200px;
  margin: 0 auto;
}
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
  margin-left: 36px;
  width: 120px;
  height: 120px;
}
.marleft {
  margin-left: 36px;
}
.title_name {
  margin-right: 50px;
  font-size: 18px;
  color: #282828;
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
  border-radius: 10px;
}
.el-dropdown-link {
  cursor: pointer;
  /* color: #409eff; */
}
.el-icon-arrow-down {
  font-size: 12px;
}
</style>
