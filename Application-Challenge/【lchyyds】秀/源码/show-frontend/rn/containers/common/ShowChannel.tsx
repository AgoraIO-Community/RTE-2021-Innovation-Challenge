import React, { useEffect, useState, } from 'react';
import { View, Text, TouchableWithoutFeedback, BackHandler, } from 'react-native';
import { NavigationProp, RouteProp } from '@react-navigation/core';
import { Icon } from 'react-native-elements';
import Orientation from 'react-native-orientation-locker';

import { ShowVO } from '../../services/show';
import { UserVO } from '../../services/user';
import ShowHeader from '../../components/showchannel/ShowHeader';
import ShowVideo from '../../components/showchannel/ShowVideo';
import InteractSection from '../../components/showchannel/InteractSection';
import { joinShowService, leaveShowService } from '../../services/showchannel';
import { IReceiveMsgData } from '../../constants/WSConstants';
import { WS } from '../../utils/ws';
import {
  ShowChannelInfoVO,
  fetchShowChannelInfoService,
} from '../../services/showchannel';

interface ShowChannelProps {
  route: RouteProp<Record<string, { theShow: ShowVO } | undefined>, "演出现场">,
  navigation: NavigationProp<any>,
  user: UserVO,
  onChangeTabBar: (visible: boolean) => void,
};

const ShowChannel: React.FC<ShowChannelProps> = ({ route, navigation, user, onChangeTabBar, }) => {
  const showVO = route.params?.theShow;

  const [showChannelInfo, setShowChannelInfo] = useState<ShowChannelInfoVO>();
  const [fullView, setFullView] = useState(false);
  const [comments, setComments] = useState<IReceiveMsgData[]>([]);

  useEffect(() => {
    initShowChannelInfo();
  }, []);

  useEffect(() => {
    user.userId &&
      showVO?.showId &&
      joinShowService({ userId: user.userId, showId: showVO?.showId })
  }, [user.userId, showVO?.showId])

  useEffect(() => {
    const handler = (data: IReceiveMsgData) => {
      if (data && data.showId === showVO?.showId) {
        const newComments = comments?.filter((c) => c.mid !== data.mid);
        newComments?.push(data);
        setComments(newComments);
      }
    };

    const id = WS.addReceiveMsgListener(handler);
    return () => {
      WS.removeReceiveMsgListener(id);
    };
  }, [comments]);

  useEffect(() => {
    onChangeTabBar?.(false);

    return () => {
      Orientation.lockToPortrait();
      onChangeTabBar?.(true);

      showVO?.showId && leaveShowService({ userId: user.userId, showId: showVO?.showId })
    };
  }, []);

  useEffect(() => {
    fullView && Orientation.lockToLandscape();
    !fullView && Orientation.lockToPortrait();

    const backAction = () => {
      if (fullView) {
        setFullView(false);
        return true;
      }
      return false;
    };
    BackHandler.addEventListener('hardwareBackPress', backAction);

    return () => {
      BackHandler.removeEventListener('hardwareBackPress', backAction);
    };
  }, [fullView]);

  const initShowChannelInfo = () => {
    if (showVO && user.userId) {
      fetchShowChannelInfoService({ userId: user.userId, showId: showVO.showId })
        .then((res) => {
          if (res.success) {
            setShowChannelInfo(res.data);
          }
        });
    }
  };

  const renderGoBack = () => {
    return (
      <View
        style={{
          position: 'absolute',
          left: 10,
          top: 10,
          zIndex: 999,
          borderRadius: 15,
        }}
      >
        <TouchableWithoutFeedback
          onPress={() => {
            if (fullView) {
              setFullView(false);
            }
            else {
              navigation.goBack();
            }
          }}
        >
          <Icon type='antdesign' name='left' size={22} color='rgba(255, 255, 255, 0.6)'></Icon>
        </TouchableWithoutFeedback>
      </View>
    );
  };

  return (
    <View style={{ marginTop: 30, position: 'relative' }}>
      {renderGoBack()}
      {showVO
        ? (
          <View style={{ height: '100%' }}>
            {!fullView && (
              <View style={{ flex: 1.5 }}>
                <ShowHeader show={showVO}></ShowHeader>
              </View>
            )}
            <View style={{ flex: fullView ? 1 : 4.5 }}>
              <ShowVideo
                show={showVO}
                showChannelInfo={showChannelInfo}
                userId={user.userId}
                fullView={fullView}
                onChangeFullView={() => setFullView(!fullView)}
              ></ShowVideo>
            </View>
            {!fullView && (
              <View style={{ flex: 4 }}>
                <InteractSection
                  show={showVO}
                  showChannelInfo={showChannelInfo}
                  userId={user.userId}
                  comments={comments}
                  onPurchased={initShowChannelInfo}
                ></InteractSection>
              </View>
            )}
          </View>
        )
        : <Text>暂无演出信息</Text>
      }
    </View>
  );
};

export default ShowChannel;