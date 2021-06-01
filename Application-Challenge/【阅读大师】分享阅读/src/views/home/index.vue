<template>
  <div class="mainDiv">
    <el-card
      shadow="hover"
      class="mainCard"
      body-style="height:100%;width:100%;padding:0px;display:flex;justify-content-div:center;align-items:center;"
    >
      <div class="roomDiv">
        <h class="headText">小说吐槽大师</h>
        <el-divider></el-divider>
        <div class="bookDiv">
          <div class="grid-content-div" @click="readBook(0, '红楼梦')">
            <img src="../../assets/book/0.jpg" width="105px" height="150px" />
            <p class="book-title">红楼梦</p>
          </div>
          <div class="grid-content-div" @click="readBook(1, '蛙')">
            <img src="../../assets/book/1.jpg" width="105px" height="150px" />
            <p class="book-title">蛙</p>
          </div>
          <div class="grid-content-div" @click="readBook(2, '活着')">
            <img src="../../assets/book/2.jpg" width="105px" height="150px" />
            <p class="book-title">活着</p>
          </div>
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
            readBook(id, name) {
                console.log(id, name);
                this.$router.push({
                    path: '/read',
                    query: { id: id, name: name }
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
    width: 95%;
    height: 95%;
    border-radius: 10px;
    .roomDiv {
      width: 100%;
      height: 100%;
      display: flex;
      align-items: center;
      flex-direction: column;
      .headText {
        font-family: 'Helvetica Neue', Helvetica, 'PingFang SC',
          'Hiragino Sans GB', 'Microsoft YaHei', '微软雅黑', Arial, sans-serif;
        margin-top: 50px;
        margin-bottom: 10px;
        font-size: 28px;
        font-weight: 600;
      }
      .roomIdInput {
        width: 80%;
        margin-bottom: 30px;
      }
      .bookDiv {
        display: flex;
        flex-direction: row;
        padding: 0 30px;
        width: 100%;
        .grid-content-div {
          display: flex;
          flex-direction: column;
          align-items: center;
          justify-content: center;
          margin: 10px;
          .book-title {
            font-size: 15px;
            font-weight: 600;
          }
        }
      }
    }
  }
}
</style>
