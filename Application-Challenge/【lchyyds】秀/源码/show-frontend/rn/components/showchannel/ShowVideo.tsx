import styles from './ShowVideoStyles';

import React, { useState, useEffect, useRef, } from 'react';
import {
  View,
  TouchableWithoutFeedback,
  ToastAndroid,
} from 'react-native';
import { Text, Icon, } from 'react-native-elements';
import RtcEngine, {
  RtcLocalView, RtcRemoteView, VideoRenderMode, ClientRole, ChannelProfile, VideoDimensions, VideoEncoderConfiguration, VideoOutputOrientationMode,
} from 'react-native-agora';
import Video from 'react-native-video';

import { Code as InitEngineErrorCode, Text as InitEngineErrorText, } from '../../constants/InitEngineError';
import {
  ShowChannelRole,
  ShowChannelInfoVO,
  ShowState,
} from '../../services/showchannel';
import { ShowVO } from '../../services/show';
import CameraAndAudioHOC from '../../enhancers/CameraAndAudioHOC';
import Button from '../../components/Button';

const RtcEngineCreateErrorMax = 5;

interface ShowVideoProps {
  show: ShowVO,
  showChannelInfo: ShowChannelInfoVO,
  userId: string,

  fullView: boolean,
  onChangeFullView: () => void,
};

const ShowVideo: React.FC<ShowVideoProps> = ({ show, showChannelInfo, userId, fullView, onChangeFullView, }) => {

  const [peerIds, setPeerIds] = useState<number[]>([]);
  const [showStarted, setShowStarted] = useState(false);
  const [joinSucceed, setJoinSuccess] = useState(false);
  const [currentReplayUrlIndex, setCurrentReplayUrlIndex] = useState(0);

  const [showReplay, setShowReplay] = useState(false);
  const [forceReplayPaused, setForceReplayPaused] = useState(false);

  const _engine = useRef<RtcEngine>();
  const rtcEngineCreateErrorCount = useRef<number>(0);

  const getIsPublisher = () => {
    return showChannelInfo?.channelMetaVO?.role === ShowChannelRole.Publisher;
  };

  useEffect(() => {
    return () => {
      // TODO: use enhancer
      // setShowStarted(false);
      // 关闭直播
      _engine.current?.leaveChannel();
      // // 停止录制(回放相关)
      // showChannelInfo?.channelMetaVO &&
      //   getIsPublisher() &&
      //   stopRecordService({ channelName: showChannelInfo?.channelMetaVO?.channelName });
      // 关摄像头
      getIsPublisher() && _engine.current?.disableVideo();
    };
  }, [showChannelInfo]);

  useEffect(() => {
    if (
      !getIsPublisher() &&
      showChannelInfo?.purchased &&
      showChannelInfo.showState === ShowState.Showing &&
      !showStarted
    ) {
      // 观众
      handlePressStartShow();
    }
  }, [showChannelInfo, showStarted]);

  const initEngine = async () => {
    // 频道信息
    if (!showChannelInfo) {
      return Promise.reject({
        code: InitEngineErrorCode.NoChannelInfo,
        data: InitEngineErrorText[InitEngineErrorCode.NoChannelInfo],
      });
    }
    // 创建Engine
    try {
      _engine.current = await RtcEngine.create(showChannelInfo?.channelMetaVO?.appId);
    }
    catch (err) {
      return Promise.reject({
        code: InitEngineErrorCode.RTCEngineCreateFailed,
        data: InitEngineErrorText[InitEngineErrorCode.RTCEngineCreateFailed],
      });
    }
    // 启用视频模块
    await _engine.current.enableVideo();
    // 开启本地视频预览
    await _engine.current.startPreview();
    // 将频道场景设为直播
    await _engine.current.setChannelProfile(ChannelProfile.LiveBroadcasting);
    // 判断主播
    getIsPublisher() && (await _engine.current.setClientRole(ClientRole.Broadcaster));
    // 设置横屏流
    await _engine.current?.setVideoEncoderConfiguration(
      new VideoEncoderConfiguration({ orientationMode: VideoOutputOrientationMode.FixedLandscape })
    );
    // 监听异常
    _engine.current.addListener('Warning', (warn) => {
      console.warn('Warning', warn);
    });
    _engine.current.addListener('Error', (err) => {
      console.error('Error', err);
    });

    // 有人进入直播间
    // 远端用户成功加入频道时，会触发该回调，并返回该用户的 id。
    _engine.current.addListener('UserJoined', (uid, elapsed) => {
      console.log('有人进入直播间：', uid, elapsed);
      if (peerIds.indexOf(uid) === -1) {
        setPeerIds([...peerIds, uid]);
      }
    });
    // 有人离开直播间
    // 远端用户离开频道时，会触发该回调，并返回该用户的 id。
    _engine.current.addListener('UserOffline', (uid, reason) => {
      console.log('有人离开直播间：', uid, reason);
      setPeerIds(peerIds.filter((id) => id !== uid));
    });

    // 我进入直播间
    // 本地用户成功加入频道时，会触发该回调。
    _engine.current.addListener('JoinChannelSuccess', (channel, uid, elapsed) => {
      console.log('================JoinChannelSuccess', channel, uid, elapsed);
      setJoinSuccess(true);
    });
  };

  const startCall = async () => {
    if (!showChannelInfo) { return; }

    await _engine.current?.joinChannel(
      showChannelInfo?.channelMetaVO?.token,
      showChannelInfo?.channelMetaVO?.channelName,
      null,
      showChannelInfo?.channelMetaVO?.uid,
    );
  };

  const handlePressStartShow = async () => {
    try {
      await initEngine();
      console.log('👏 👏 👏 int engine successfully')

      getIsPublisher() && onChangeFullView();

      await startCall();
      setShowStarted(true);
    }
    catch (err) {
      if (err?.code === InitEngineErrorCode.NoChannelInfo) {
        console.error(err?.data);
        // TODO: 频道信息无
      }
      else if (err?.code === InitEngineErrorCode.RTCEngineCreateFailed) {
        console.error(err?.data);
        // 创建Engine错误
        rtcEngineCreateErrorCount.current += 1;
        if (rtcEngineCreateErrorCount.current < RtcEngineCreateErrorMax) {
          handlePressStartShow();
        }
      }
      else {
        console.error('start show failed - -', err);
      }
    }
  };

  const handlePressSwitchCamera = async () => {
    if (!getIsPublisher()) {
      ToastAndroid.show('未知错误', ToastAndroid.LONG);
      return;
    }

    try {
      await _engine.current?.switchCamera();
    }
    catch (err) {
      ToastAndroid.show('摄像头切换失败，请稍后再试', ToastAndroid.LONG);
    }

  };

  const handlePressFullView = () => {
    onChangeFullView?.();
  };

  const handleReplayEnd = () => {
    // 只播放一遍
    const replayLen = show.replayList?.length;
    const nextReplayIndex = currentReplayUrlIndex + 1;
    if (replayLen > 0 && nextReplayIndex < replayLen) {
      setCurrentReplayUrlIndex(nextReplayIndex);
    }
  };

  const renderFullViewTigger = () => {
    return (
      <View
        style={{
          position: 'absolute',
          right: 10,
          bottom: 10,
          zIndex: 1000,
          padding: 1,
          backgroundColor: 'rgba(255, 255, 255, 0.5)',
          borderRadius: 4,
        }}
      >
        <TouchableWithoutFeedback onPress={handlePressFullView}>
          <Icon type='font-awesome' name={fullView ? 'compress' : 'expand'} size={25} color='rgba(0, 0, 0, 0.5)'></Icon>
        </TouchableWithoutFeedback>
      </View>
    );
  };

  const renderLocalHostView = () => {
    if (!showChannelInfo) { return null; }

    return (
      <View style={styles.max}>
        {joinSucceed
          ? (
            <View style={styles.fullView}>
              <RtcLocalView.SurfaceView
                style={styles.max}
                channelId={showChannelInfo?.channelMetaVO?.channelName}
                // 将视频渲染模式设为 Hidden, 即优先保证视窗被填满。
                renderMode={VideoRenderMode.Hidden}
              />
              <View
                style={{
                  position: 'absolute',
                  right: 10,
                  top: 10,
                  zIndex: 1000,
                  padding: 1,
                  backgroundColor: 'rgba(255, 255, 255, 0.5)',
                  borderRadius: 4,
                }}
              >
                <TouchableWithoutFeedback onPress={handlePressSwitchCamera}>
                  <Icon type='font-awesome' name='exchange' color='rgba(0, 0, 0, 0.5)' size={20} />
                </TouchableWithoutFeedback>
              </View>
              {renderFullViewTigger()}
            </View>
          )
          : (
            <Text h3 style={{ color: '#fff', textAlign: 'center', lineHeight: 50 }}>
              loading...
            </Text>
          )
        }
      </View>
    );
  };

  const renderRemoteHostView = () => {
    if (!showChannelInfo) { return null; }

    return (
      <View style={styles.max}>
        {joinSucceed
          ? (
            <View style={styles.fullView}>
              <RtcRemoteView.SurfaceView
                style={styles.max}
                uid={showChannelInfo?.channelMetaVO?.publisherUid}
                channelId={showChannelInfo?.channelMetaVO?.channelName}
                // 将视频渲染模式设为 Hidden, 即优先保证视窗被填满。
                renderMode={VideoRenderMode.Hidden}
                zOrderMediaOverlay={true}
              />
              {renderFullViewTigger()}
            </View>
          )
          : (
            <Text h3 style={{ color: '#fff', textAlign: 'center', lineHeight: 50 }}>
              loading...
            </Text>
          )
        }
      </View>
    );
  };

  const renderNotYet = () => {
    return (
      <View style={{ backgroundColor: '#000', height: '100%', position: 'relative' }}>
        {show.preview
          ? (
            <>
              <View
                style={{
                  position: 'absolute',
                  left: fullView ? 40 : 5,
                  top: 6,
                  zIndex: 10,
                  borderRadius: 10,
                  backgroundColor: 'rgba(0, 0, 0, 0.5)',
                }}
              >
                <Text style={{ margin: 5, color: '#fff', }}>先导片</Text>
              </View>
              <View
                style={{
                  padding: 4,
                  position: 'absolute',
                  right: 10,
                  top: 6,
                  zIndex: 10,
                  borderRadius: 10,
                  backgroundColor: 'rgba(0, 0, 0, 0.5)',
                }}
              >
                <TouchableWithoutFeedback onPress={handlePressFullView}>
                  <Icon type='font-awesome' name={fullView ? 'compress' : 'expand'} size={20} color='rgba(255, 255, 255, 0.7)'></Icon>
                </TouchableWithoutFeedback>
              </View>
              <View
                style={{
                  position: 'relative',
                  width: '100%',
                  height: '100%',
                  overflow: 'hidden',
                }}
              >
                <Video
                  style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    bottom: 0,
                    right: 0,
                  }}
                  source={{ uri: show.preview }}
                  // ref={videoRef}
                  // onBuffer={handleVideoOnBuffer}
                  // onError={handleVideoOnError}
                  onEnd={handleReplayEnd}
                  controls
                  resizeMode='cover'
                  fullscreen={fullView}
                />
              </View>
            </>
          )
          : (
            <View style={{ flex: 1, justifyContent: 'center' }}>
              <Text h3 style={{ color: '#fff', textAlign: 'center', lineHeight: 50 }}>演出未开始</Text>
            </View>
          )
        }
      </View>
    );
  };

  const renderPublisherShowing = () => {
    return (
      showStarted
        ? renderLocalHostView()
        : (
          <View style={{ backgroundColor: '#000', height: '100%', position: 'relative', }}>
            <View style={{ flex: 1, justifyContent: 'center' }}>
              <View>
                <Text h3 style={{ color: '#fff', textAlign: 'center', lineHeight: 50 }}>演出进行中</Text>
              </View>
              <View style={{ marginTop: 20, alignItems: 'center' }}>
                <Button
                  width={140}
                  title='横屏开播'
                  color='tomato'
                  type='primary'
                  onPress={handlePressStartShow}
                ></Button>
              </View>
            </View>
          </View>
        )
    );
  };

  const renderSubscriberShowing = () => {
    return (
      showChannelInfo?.purchased
        ? renderRemoteHostView()
        : (
          <View style={{ backgroundColor: '#000', height: '100%', position: 'relative', }}>
            <View style={{ flex: 1, justifyContent: 'center' }}>
              <View>
                <Text h3 style={{ color: '#fff', textAlign: 'center', lineHeight: 50 }}>演出进行中</Text>
              </View>
              <View>
                <Text style={{ color: 'tomato', textAlign: 'center', lineHeight: 50 }}>点击右下方购买，即可畅享演出</Text>
              </View>
            </View>
          </View>
        )
    );
  };

  const renderShowing = () => {
    return (
      getIsPublisher()
        ? renderPublisherShowing()
        : renderSubscriberShowing()
    );
  };

  const renderEnd = () => {
    return (
      <View style={{ backgroundColor: '#000', height: '100%', position: 'relative', }}>
        {showReplay && show.replayList?.[currentReplayUrlIndex]
          ? (
            <>
              <View
                style={{
                  position: 'absolute',
                  left: fullView ? 40 : 5,
                  top: 6,
                  zIndex: 10,
                  borderRadius: 10,
                  backgroundColor: 'rgba(0, 0, 0, 0.5)',
                }}
              >
                <Text style={{ margin: 5, color: '#fff', }}>回放</Text>
              </View>
              <View
                style={{
                  padding: 4,
                  position: 'absolute',
                  right: 10,
                  top: 6,
                  zIndex: 10,
                  borderRadius: 10,
                  backgroundColor: 'rgba(0, 0, 0, 0.5)',
                }}
              >
                <TouchableWithoutFeedback onPress={handlePressFullView}>
                  <Icon type='font-awesome' name={fullView ? 'compress' : 'expand'} size={20} color='rgba(255, 255, 255, 0.7)'></Icon>
                </TouchableWithoutFeedback>
              </View>
              <View
                style={{
                  position: 'relative',
                  width: '100%',
                  height: '100%',
                  overflow: 'hidden',
                }}
              >
                <Video
                  style={{
                    position: 'absolute',
                    top: 0,
                    left: 0,
                    bottom: 0,
                    right: 0,
                  }}
                  source={{ uri: show.replayList[currentReplayUrlIndex] }}
                  // ref={videoRef}
                  // onBuffer={handleVideoOnBuffer}
                  // onError={handleVideoOnError}
                  onEnd={handleReplayEnd}
                  controls
                  resizeMode='cover'
                  fullscreen={fullView}
                />
              </View>
            </>
          )
          : (
            <View style={{ flex: 1, justifyContent: 'center' }}>
              <View>
                <Text h3 style={{ color: '#fff', textAlign: 'center', lineHeight: 50 }}>演出已结束</Text>
              </View>
              {/* TODO: 流量费太贵了😭，该功能先关闭 */}
              {/* {getIsPublisher() || showChannelInfo?.purchased
                ? (
                  <View style={{ marginTop: 20, alignItems: 'center' }}>
                    <Button
                      width={140}
                      title='查看回放'
                      color='tomato'
                      type='primary'
                      onPress={() => setShowReplay(true)}
                    ></Button>
                  </View>
                )
                : null
              } */}
            </View>
          )
        }
      </View >
    );
  };

  const renderContent = () => {
    if (showChannelInfo?.showState === ShowState.End) {
      return renderEnd();
    }

    if (showChannelInfo?.showState === ShowState.Showing) {
      return renderShowing();
    }

    if (showChannelInfo?.showState === ShowState.NotYet) {
      return renderNotYet();
    }

    return null;
  };

  return (
    <View style={styles.max}>
      {showChannelInfo ? renderContent() : null}
    </View>
  );
};

export default CameraAndAudioHOC(ShowVideo);