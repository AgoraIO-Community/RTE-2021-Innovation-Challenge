<template>
  <div class="wrapper">
    <el-container style="height:100%" direction="vertical">
      <el-container>
        <el-main>
          <div class="gameDiv">
            <div style="margin:0 auto;width:640px;height:640px;">
              <canvas
                width="640"
                ref="myCanvas"
                @mousedown="play($event)"
                height="640"
                >你的浏览器不支持HTML5 canvas，请使用 google chrome 浏览器 打开.
              </canvas>
            </div>
          </div>
        </el-main>
        <el-aside class="content">
          <div>
            <div class="myVideoDiv">
              <div class="tab-bar">
                  <p class="tab-bar-text">五子棋争霸赛</p>
              </div>
              <div class="agora-video" style="transform: rotateY(180deg)">
                <StreamPlayer
                  :stream="localStream"
                  :domId="localStream.getId()"
                  v-if="localStream"
                ></StreamPlayer>
              </div>
            </div>
            <p
              style="width:100%;text-align:center;font-size:25px;font-weight:700;"
            >
              VS
            </p>
            <div class="agora-video">
              <StreamPlayer
                :stream="remoteStreams[0]"
                :domId="remoteStreams[0].getId()"
                v-if="remoteStreams[0]"
              ></StreamPlayer>
            </div>
          </div>
        </el-aside>
      </el-container>
    </el-container>
  </div>
</template>

<script>
    import RTCClient from '../../sdk/agora-rtc-client';
    import StreamPlayer from '../../components/stream-player';
    import { log } from '../../utils/utils';
    import config from '../../../config';

    export default {
        components: {
            StreamPlayer
        },
        data() {
            return {
                option: {
                    appid: '',
                    token: '',
                    uid: null,
                    channel: ''
                },
                disableJoin: false,
                localStream: null,
                remoteStreams: [],
                canvas: null, //五子棋
                context: null,
                isWell: false, //设置该局棋盘是否赢了，如果赢了就不能再走了
                imgB: new Image(),
                imgW: new Image(),
                chessData: null,
                isTurn: null,
                deskId: null,
                role: null
            };
        },
        created() {
            this.rtcCreated();
        },
        mounted() {
            this.option.appid = config.appid;
            this.option.channel = this.$route.query.channelName.toString();
            this.deskId = this.$route.query.channelName;
            this.role = this.$route.query.role;
            if (this.$route.query.role === 1) {
                this.isTurn = true;
            } else {
                this.isTurn = false;
            }

            this.sockets.subscribe('serverClickPiece', data => {
                if (this.role === 1) this.drawChess(2, data.x, data.y);
                else this.drawChess(1, data.x, data.y);
                this.isTurn = true;
            });

            this.imgB.src = require('../../assets/images/b.png'); //白棋图片
            this.imgW.src = require('../../assets/images/w.png'); //黑棋图片
            this.chessData = new Array(15); //这个为棋盘的二维数组用来保存棋盘信息，初始化0为没有走过的，1为白棋走的，2为黑棋走的
            for (let x = 0; x < 15; x++) {
                this.chessData[x] = new Array(15);
                for (let y = 0; y < 15; y++) {
                    this.chessData[x][y] = 0;
                }
            }

            this.drawRect();

            this.rtc
                .joinChannel(this.option)
                .then(() => {
                    this.$message({
                        message: 'Join Success',
                        type: 'success'
                    });
                    this.rtc
                        .publishStream()
                        .then(stream => {
                            this.$message({
                                message: 'Publish Success',
                                type: 'success'
                            });
                            this.localStream = stream;
                        })
                        .catch(err => {
                            this.$message.error('Publish Failure');
                            log('publish local error', err);
                        });
                })
                .catch(err => {
                    this.$message.error('Join Failure');
                    log('join channel error', err);
                });
        },
        destroyed() {},
        methods: {
            rtcCreated() {
                this.rtc = new RTCClient();
                const rtc = this.rtc;
                rtc.on('stream-added', evt => {
                    const { stream } = evt;
                    log('[agora] [stream-added] stream-added', stream.getId());
                    rtc.client.subscribe(stream);
                });

                rtc.on('stream-subscribed', evt => {
                    const { stream } = evt;
                    log('[agora] [stream-subscribed] stream-added', stream.getId());
                    if (!this.remoteStreams.find(it => it.getId() === stream.getId())) {
                        this.remoteStreams.push(stream);
                    }
                });

                rtc.on('stream-removed', evt => {
                    const { stream } = evt;
                    log('[agora] [stream-removed] stream-removed', stream.getId());
                    this.remoteStreams = this.remoteStreams.filter(
                        it => it.getId() !== stream.getId()
                    );
                });

                rtc.on('peer-online', evt => {
                    this.$message(`Peer ${evt.uid} is online`);
                });

                rtc.on('peer-leave', evt => {
                    this.$message(`Peer ${evt.uid} already leave`);
                    this.remoteStreams = this.remoteStreams.filter(
                        it => it.getId() !== evt.uid
                    );
                });
            },
            drawRect() {
                //页面加载完毕调用函数，初始化棋盘
                //this.canvas = document.getElementById('canvas');
                this.canvas = this.$refs.myCanvas;
                console.log(this.canvas);
                this.context = this.canvas.getContext('2d');

                const bacImg = new Image();
                bacImg.src = require('../../assets/images/back.jpg');
                bacImg.onload = imgfn; //图片加载完在执行
                const that = this;
                function imgfn() {
                    const bg = that.context.createPattern(bacImg, 'no-repeat'); //createPattern() 方法在指定的方向内重复指定的元素。
                    that.context.fillStyle = bg; //fillStyle 属性设置或返回用于填充绘画的颜色、渐变或模式。
                    that.context.fillRect(0, 0, that.canvas.width, that.canvas.height); //绘制已填充矩形fillRect(左上角x坐标, 左上角y坐标, 宽, 高)

                    for (var i = 0; i <= 640; i += 40) {
                        //绘制棋盘的线
                        that.context.beginPath();
                        that.context.moveTo(0, i);
                        that.context.lineTo(640, i);
                        that.context.closePath();
                        that.context.stroke();
                        that.context.beginPath();
                        that.context.moveTo(i, 0);
                        that.context.lineTo(i, 640);
                        that.context.closePath();
                        that.context.stroke();
                    }
                }
            },

            play(e) {
                if (this.isWell === true) {
                    this.$alert('游戏结束，请返回主页重新开始', '错误', {
                        confirmButtonText: '确定',
                        center: true,
                        type: 'error'
                    }).then(() => {
                        this.$router.push({
                            path: '/'
                        });
                        location.reload();
                    });
                    return;
                }
                if (!this.isTurn) {
                    this.$alert('请等待对手下棋', '错误', {
                        confirmButtonText: '确定',
                        center: true,
                        type: 'error'
                    });
                    return;
                }
                console.log(e);
                //鼠标点击时发生
                const x = parseInt((e.offsetX - 20) / 40); //计算鼠标点击的区域，如果点击了（65，65），那么就是点击了（1，1）的位置
                const y = parseInt((e.offsetY - 20) / 40);
                if (this.chessData[x][y] !== 0) {
                    //判断该位置是否被下过了
                    this.$alert('这个位置已经有棋子了', '错误', {
                        confirmButtonText: '确定',
                        center: true,
                        type: 'error'
                    });
                    return;
                }
                this.drawChess(this.role, x, y);
                this.$socket.emit('clickPiece', {
                    role: this.role,
                    deskId: this.deskId,
                    x: x,
                    y: y
                });
                this.isTurn = false;
            },

            drawChess(chess, x, y) {
                //参数为，棋（1为白棋，2为黑棋），数组位置
                if (x >= 0 && x < 15 && y >= 0 && y < 15) {
                    if (chess === 1) {
                        this.context.drawImage(this.imgW, x * 40 + 20, y * 40 + 20); //绘制白棋
                        this.chessData[x][y] = 1;
                    } else {
                        this.context.drawImage(this.imgB, x * 40 + 20, y * 40 + 20);
                        this.chessData[x][y] = 2;
                    }
                    this.judge(x, y, chess);
                }
            },

            judge(x, y, chess) {
                //判断该局棋盘是否赢了
                let count1 = 0;
                let count2 = 0;
                let count3 = 0;
                let count4 = 0;
                //左右判断
                let i;
                let j;
                for (i = x; i >= 0; i--) {
                    if (this.chessData[i][y] !== chess) {
                        break;
                    }
                    count1++;
                }
                for (i = x + 1; i < 15; i++) {
                    if (this.chessData[i][y] !== chess) {
                        break;
                    }
                    count1++;
                }
                //上下判断
                for (i = y; i >= 0; i--) {
                    if (this.chessData[x][i] !== chess) {
                        break;
                    }
                    count2++;
                }
                for (i = y + 1; i < 15; i++) {
                    if (this.chessData[x][i] !== chess) {
                        break;
                    }
                    count2++;
                }
                //左上右下判断
                j = y;
                for (i = x; i >= 0; i--) {
                    if (this.chessData[i][j] !== chess) {
                        break;
                    }
                    count3++;
                    j--;
                    if (j < 0) break;
                }
                j = y + 1;
                for (i = x + 1; i < 15; i++) {
                    if (this.chessData[i][j] !== chess) {
                        break;
                    }
                    count3++;
                    j++;
                    if (j >= 15) break;
                }
                //右上左下判断
                j = y;
                for (i = x; i >= 0; i--) {
                    if (this.chessData[i][j] !== chess) {
                        break;
                    }
                    count4++;
                    j++;
                    if (j >= 15) break;
                }
                j = y - 1;
                for (i = x + 1; i < 15; i++) {
                    if (this.chessData[i][j] !== chess) {
                        break;
                    }
                    count4++;
                    j--;
                    if (j < 0) break;
                }

                if (count1 >= 5 || count2 >= 5 || count3 >= 5 || count4 >= 5) {
                    let messageNow = '';
                    if (chess === 1) {
                        messageNow = '白棋获胜';
                    } else {
                        messageNow = '黑棋获胜';
                    }
                    this.isWell = true; //设置该局棋盘已经赢了，不可以再走了
                    this.$alert(messageNow, '游戏结束', {
                        confirmButtonText: '确定',
                        center: true
                    });
                }
            }
        }
    };
</script>

<style scoped lang="less">
.wrapper {
  height: 100vh;
  display: flex;
  flex-direction: column;

  .el-aside {
    width: 15%;
    background-color: #e1e1e1;
  }
  .main-window {
    width: 100%;
    height: 200px;
  }
  .agora-video {
    width: 256px;
    height: 192px;
    margin: 20px;
  }
  .tab-bar {
    height: 54px;
    background-image: linear-gradient(180deg, #292933 7%, #212129 100%);
    box-shadow: 0 0 0 0 rgba(255, 255, 255, 0.3);
    list-style: none;
    display: flex;
    justify-content: center;
    align-items: center;
    color: #fff;
  }
}
</style>
