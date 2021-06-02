<template>
  <div class="wrapper">
    <div class="content">
      <!--画面div-->
      <div class="main-window-wrapper">
        <div class="gameDiv">
          <div class="flexDiv" v-if="showDivId === 1">
            <p style="font-size:30px;font-weight:600;margin:10px 0;">
              聚会游戏
            </p>
            <el-button
              type="primary"
              round
              class="game-button"
              @click="roundNum()"
              >随机数生成</el-button
            >
            <el-button
              type="primary"
              round
              class="game-button"
              @click="guessBigOrSmall()"
              >猜大小</el-button
            >
          </div>
          <div class="flexDiv" v-if="showDivId === 2">
            <p class="main-text">产生的随机数为 {{ roundNumValue }}</p>
            <el-button
              type="primary"
              round
              class="game-button"
              @click="goBackToFirst()"
              >返回</el-button
            >
          </div>
          <div class="flexDiv" v-if="showDivId === 3">
            <p class="main-text">
              点数为 {{ roundNumValue }} 点，结果为 {{ resultString }}
            </p>
            <el-button
              type="primary"
              round
              class="game-button"
              @click="goBackToFirst()"
              >返回</el-button
            >
          </div>
        </div>
        <div class="main-window" style="transform: rotateY(180deg)">
          <StreamPlayer
            :stream="localStream"
            :domId="localStream.getId()"
            v-if="localStream"
          ></StreamPlayer>
        </div>
      </div>
      <div class="sub-window-wrapper">
        <!--小画面div-->
        <template v-if="remoteStreams.length">
          <div
            class="sub-window"
            :key="index"
            v-for="(remoteStream, index) in remoteStreams"
          >
            <StreamPlayer
              :stream="remoteStream"
              :domId="remoteStream.getId()"
            ></StreamPlayer>
          </div>
        </template>
        <div v-else class="sub-window">
          <span class="loading-text">等待对方加入…</span>
        </div>
      </div>
    </div>
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
                showDivId: 1,
                roundNumValue: 0,
                resultString: ''
            };
        },
        created() {
            this.rtcCreated();
        },
        mounted() {
            this.option.appid = config.appid;
            this.option.channel = this.$route.query.channel.toString();

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
            getRoundNum(min, max) {
                return parseInt(Math.random() * (max - min + 1) + min, 10);
            },
            roundNum() {
                this.showDivId = 2;
                this.roundNumValue = this.getRoundNum(1, 100);
            },
            guessBigOrSmall() {
                this.roundNumValue = this.getRoundNum(1, 12);
                if (this.roundNumValue <= 6) {
                    this.resultString = '小';
                } else {
                    this.resultString = '大';
                }
                this.showDivId = 3;
            },
            goBackToFirst() {
                this.showDivId = 1;
            },
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
            }
        }
    };
</script>

<style scoped lang="less">
.wrapper {
  height: 100vh;
  background-image: linear-gradient(179deg, #141417 0%, #181824 100%);
  display: flex;
  flex-direction: column;

  .content {
    flex: 1;
    display: flex;
    flex-direction: row;

    .main-window-wrapper {
      display: flex;
      flex-direction: column;
      flex-grow: 1;
      .main-window {
        height: 35%;
        width: 52vh;
        //width: 37vw;
        //width: 427px;
        margin: 0 auto;
        background: #25252d;
        flex-grow: 0;
      }
      .gameDiv {
        flex-grow: 1;
        background-color: #dee1e6;
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        .flexDiv {
          display: flex;
          flex-direction: column;
          justify-content: center;
          align-items: center;
          .main-text {
            font-size: 25px;
            margin-bottom: 35px;
          }
        }
        .game-button {
          width: 180px;
          height: 45px;
          font-size: 20px;
          margin: 10px 0;
        }
      }
    }

    .sub-window-wrapper {
      top: 16px;
      right: 16px;
      z-index: 9;
      width: 165px;
      flex-grow: 0;
      margin-left: 20px;
    }

    .sub-window {
      background: #25252d;
      border: 1px solid #ffffff;
      margin-bottom: 20px;
      height: 120px;
      width: 160px;

      .loading-text {
        display: block;
        width: 100%;
        text-align: center;
        line-height: 90px;
        font-size: 12px;
        color: #fff;
        font-weight: 400;
      }
    }
  }
}
</style>
