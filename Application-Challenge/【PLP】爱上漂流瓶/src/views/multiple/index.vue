<template>
  <div class="mainDiv">
    <el-container class="main-el-container">
      <el-main class="main-container">
        <div class="main-window">
          <div class="main-window-player" style="transform: rotateY(180deg)">
            <StreamPlayer
              :stream="localStream"
              :domId="localStream.getId()"
              v-if="localStream"
            ></StreamPlayer>
          </div>
        </div>
      </el-main>
      <el-aside class="main-aside" width="280px">
        <div class="sub-window">
          <div
            class="sub-window-player"
            :key="index"
            v-for="(remoteStream, index) in remoteStreams"
          >
            <StreamPlayer
              :stream="remoteStream"
              :domId="remoteStream.getId()"
            ></StreamPlayer>
          </div>
        </div>
      </el-aside>
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
                remoteStreams: []
            };
        },
        created() {
            this.rtcCreated();
        },
        mounted() {
            this.option.appid = config.appid;
            this.option.channel = 'PLP';

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
.mainDiv {
  height: 100%;
  width: 100%;
  background-color: #15151b;
  .main-el-container {
    height: 100%;
    .main-container {
      height: 100%;
      display: flex;
      justify-content: center;
      align-items: center;
      .main-window {
        height: 100%;
        width: 400px;
        background-color: #25252d;
        display: flex;
        justify-content: center;
        align-items: center;
        .main-window-player {
          width: 100%;
          height: 300px;
        }
      }
    }

    .main-aside {
      background-color: #25252d;
      height: 100%;
      .sub-window {
        height: 100%;
        display: flex;
        flex-direction: column;
        align-items: center;
        .sub-window-player {
          width: 236px;
          height: 177px;
        }
      }
    }
  }
}
</style>
