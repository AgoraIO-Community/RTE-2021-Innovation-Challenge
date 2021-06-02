<template>
  <div class="mainDiv">
    <el-card
      shadow="hover"
      class="mainCard"
      body-style="height:100%;width:100%;padding:0px;display:flex;justify-content:center;align-items:center;"
    >
      <div class="roomDiv">
        <p class="headText">面对面棋王争霸</p>
        <div class="userNameDiv" v-if="!isLogin">
          <el-input
            type="text"
            class="roomIdInput"
            v-model="input"
            placeholder="输入您的用户名(不超过10个字符)"
            maxlength="10"
            show-word-limit
            clearable
          ></el-input>
          <el-button type="primary" round @click="createUser"
            >创建用户</el-button
          >
        </div>
        <div class="userNameDiv" v-else>
          <p class="userNameText">欢迎您，{{ userName }}，祝您玩的开心！</p>
        </div>
        <div class="tableDiv">
          <el-table
            :data="onlineUser"
            style="width: 100%; height: 350px"
            max-height="350"
            highlight-current-row
            border
            empty-text="创建用户后可查看数据"
          >
            <el-table-column
              prop="name"
              label="在线用户"
              width="200"
            ></el-table-column>
            <el-table-column label="操作" width="150">
              <template slot-scope="scope">
                <el-button @click="startGame(scope.row.name)" type="text"
                  >发起对战</el-button
                >
              </template>
            </el-table-column>
          </el-table>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script>
    export default {
        data() {
            return {
                input: '',
                userName: '',
                onlineUser: [],
                isLogin: false
            };
        },
        mounted() {
            this.sockets.subscribe('okResult', data => {
                this.userName = data;
                console.log('login ', this.userName);
                this.isLogin = true;
            });

            this.sockets.subscribe('updateClients', data => {
                console.log('updateClients!!');
                this.onlineUser = data;
            });

            this.sockets.subscribe('errorResult', data => {
                this.$alert(data.Message, '错误', {
                    confirmButtonText: '确定',
                    center: true,
                    type: 'error'
                });
            });

            this.sockets.subscribe('deskBegin', data => {
                this.$message({
                    type: 'success',
                    message: '正在进入对战!'
                });
                this.$router.push({
                    path: '/game',
                    query: { channelName: data.deskId, role: data.role }
                });
            });

            this.sockets.subscribe('applyConnect', data => {
                this.$confirm(data.Message + '请求与您对战', '对战请求', {
                    confirmButtonText: '同意',
                    cancelButtonText: '拒绝',
                    type: 'info',
                    center: true
                })
                    .then(() => {
                        this.$message({
                            type: 'success',
                            message: '同意对战!'
                        });
                        this.$socket.emit('agreeConnect', data.Message);
                    })
                    .catch(() => {
                        this.$message({
                            type: 'info',
                            message: '拒绝对战!'
                        });
                    });
            });

            this.$socket.emit('initClients');
            this.$socket.emit('tryGetUserName');
        },
        methods: {
            createUser() {
                if (this.input.length === 0) {
                    this.$alert('用户名不能为空', '错误', {
                        confirmButtonText: '确定',
                        center: true,
                        type: 'error'
                    }).then(() => {
                        this.input = '';
                    });
                    return;
                }
                this.$socket.emit('setName', this.input);
            },
            startGame(name) {
                console.log(name);
                if (this.userName === name) {
                    this.$alert('不能与自己对战！', '错误', {
                        confirmButtonText: '确定',
                        center: true,
                        type: 'error'
                    });
                    return;
                }

                this.$alert('对战已发起，请等待对方同意', '请等待', {
                    confirmButtonText: '确定',
                    center: true,
                    type: 'info'
                });

                this.$socket.emit('applyConnect', name);
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
    width: 90%;
    height: 90%;
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
      .userNameDiv {
        width: 100%;
        height: 70px;
        display: flex;
        align-items: center;
        justify-content: center;
        text-align: center;
        margin-bottom: 30px;
        .roomIdInput {
          width: 50%;
          margin-right: 30px;
        }
        .userNameText {
          font-size: 20px;
        }
      }
      .tableDiv {
      }
    }
  }
}
</style>
