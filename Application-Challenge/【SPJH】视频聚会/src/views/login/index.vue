<template>
  <div class="mainDiv">
    <el-card
      shadow="hover"
      class="mainCard"
      body-style="height:100%;width:100%;padding:0px;display:flex;justify-content:center;align-items:center;"
    >
      <div class="roomDiv">
        <h class="headText">虚拟聚会</h>
        <el-input
          class="roomIdInput"
          v-model="input"
          placeholder="请输入房间号"
          maxlength="10"
          clearable
        ></el-input>
        <div class="buttonDiv">
          <el-button type="primary" round @click="joinRoom">加入房间</el-button>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
    export default {
        name: 'home',
        data() {
            return {
                input: ''
            };
        },
        methods: {
            checkInput() {
                const that = this;
                if (!/^[0-9]{1,10}$/.test(this.input)) {
                    this.$alert('请输入十位以内的数字作为房间号', '错误', {
                        confirmButtonText: '确定',
                        center: true,
                        type: 'error'
                    }).then(() => {
                        that.input = '';
                    });
                }
            },
            joinRoom() {
                this.checkInput();
                this.$router.push({
                    path: '/party',
                    query: { channel: this.input }
                });
            }
        }
    };
</script>

<style scoped lang="less">
.mainDiv {
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  .mainCard {
    width: 500px;
    height: 350px;
    border-radius: 10px;
    .roomDiv {
      width: 70%;
      height: 100%;
      display: flex;
      align-items: center;
      justify-content: center;
      flex-direction: column;
      .headText {
        font-family: 'Helvetica Neue', Helvetica, 'PingFang SC',
          'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
        margin-bottom: 30px;
        font-size: 28px;
        font-weight: 600;
      }
      .roomIdInput {
        width: 80%;
        margin-bottom: 30px;
      }
    }
  }
}
</style>
