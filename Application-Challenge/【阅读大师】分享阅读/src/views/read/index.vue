<template>
  <div class="wrapper">
    <div class="content">
      <!--画面div-->
      <div class="main-window-wrapper">
        <div class="gameDiv">
          <div class="flexDiv">
            <h1 class="main-text" style="text-align:center;margin:10px">
              {{ bookName }}
            </h1>
            <p
              class="main-text"
              v-for="(nowText, index) in bookData"
              :key="index"
            >
              {{ nowText }}
            </p>
          </div>
        </div>
      </div>
      <div class="sub-window-wrapper">
        <!--小画面div-->
        <div class="sub-window" style="transform: rotateY(180deg)">
          <StreamPlayer
            :stream="localStream"
            :domId="localStream.getId()"
            v-if="localStream"
          ></StreamPlayer>
        </div>
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
                bookData: [],
                bookId: null,
                bookName: null
            };
        },
        created() {
            this.rtcCreated();
        },
        mounted() {
            this.bookId = this.$route.query.id;
            this.bookName = this.$route.query.name;
            this.option.appid = config.appid;
            this.option.channel = 'readbook' + this.bookId.toString();

            this.sockets.subscribe('getBook', data => {
                this.bookData = data;
            });

            this.$socket.emit('getBook', { bookId: this.bookId });

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
          width: 80%;
          height: 100%;
          display: flex;
          flex-direction: column;
          .main-text {
            width: 100%;
          }
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
      display: flex;
      flex-direction: column;
      align-items: center;
    }

    .sub-window {
      background: #25252d;
      border: 1px solid #ffffff;
      margin-bottom: 20px;
      margin-right: 18px;
      width: 165px;
      height: 124px;

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
