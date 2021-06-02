<template>
  <div class="mainDiv">
    <el-card
      shadow="hover"
      class="mainCard"
      body-style="height:100%;width:100%;padding:0px;display:flex;justify-content:center;align-items:center;"
    >
      <div class="mainDiv">
        <div class="roomDiv">
          <p class="headText">火热漂流瓶</p>
          <div class="sendMessageDiv">
            <el-input
              type="text"
              class="messageInput"
              v-model="input"
              placeholder="写上您想说的话吧"
              clearable
            ></el-input>
            <el-button type="primary" round @click="sendMessage"
              >扔漂流瓶</el-button
            >
          </div>
          <el-button class="get-button" type="primary" round @click="getMessage"
            >捞漂流瓶</el-button
          >
          <el-button class="get-button" type="primary" round @click="startVideo"
            >开启视频聊天</el-button
          >
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                input: ''
            };
        },
        mounted() {
            this.sockets.subscribe('sendMessage', data => {
                this.$alert(data, '捞到一个漂流瓶', {
                    confirmButtonText: '确定',
                    center: true,
                    type: 'success'
                });
            });
        },
        methods: {
            sendMessage() {
                this.$socket.emit('sendMessage', this.input);
                this.$alert('您成功放飞了一个漂流瓶~', '漂流瓶', {
                    confirmButtonText: '确定',
                    center: true,
                    type: 'success'
                });
                this.input = '';
            },
            getMessage() {
                this.$socket.emit('getMessage');
            },
            startVideo() {
                this.$router.push({
                    path: '/multiple'
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
  flex-direction: column;
  .mainCard {
    width: 600px;
    height: 400px;
    border-radius: 10px;
    .roomDiv {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      flex-direction: column;
      text-align: center;
      .headText {
        font-family: 'Helvetica Neue', Helvetica, 'PingFang SC',
          'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
        margin-top: 30px;
        margin-bottom: 30px;
        font-size: 28px;
        font-weight: 600;
      }
      .sendMessageDiv {
        width: 100%;
        height: 70px;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        margin-bottom: 30px;
        .messageInput {
          width: 50%;
          margin-right: 30px;
        }
      }
      .get-button {
        width: 200px;
        height: 50px;
        font-size: 25px;
        margin-top: 20px;
      }
    }
  }
}
</style>
