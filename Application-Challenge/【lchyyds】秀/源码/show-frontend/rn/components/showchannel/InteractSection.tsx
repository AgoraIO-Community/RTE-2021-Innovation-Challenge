import React, { useEffect, useRef, useState } from 'react';
import { View, ScrollView, } from 'react-native';

import { IReceiveMsgData } from '../../constants/WSConstants';
import { ShowVO } from '../../services/show';
import { ShowChannelInfoVO, ShowChannelRole, ShowState } from '../../services/showchannel';
import BuyButton from './BuyButton';
import Comment from './Comment';
import CommentInput from './CommentInput';

interface InteractSectionProps {
  show: ShowVO,
  showChannelInfo?: ShowChannelInfoVO,
  userId: string,
  comments: IReceiveMsgData[],
  onPurchased?: () => void,
}

const InteractSection: React.FC<InteractSectionProps> = ({ show, showChannelInfo, userId, comments, onPurchased }) => {

  const scrollViewRef = useRef<ScrollView>(null);

  useEffect(() => {
    scrollViewRef.current?.scrollToEnd();
  }, [comments]);

  const getIsPublisher = () => {
    return showChannelInfo?.channelMetaVO?.role === ShowChannelRole.Publisher;
  };

  const checkBuyButtonVisible = () => {
    // TODO: 后期允许回放，则去掉showState判断
    return !!showChannelInfo && !getIsPublisher() && showChannelInfo.showState !== ShowState.End;
  };

  return (
    <View
      style={{
        padding: 10,
        backgroundColor: 'rgba(0, 0, 0, 0.9)',
        height: '100%',
      }}
    >
      <ScrollView ref={scrollViewRef}>
        {comments?.map((comment, i) => (
          comment
            ? (
              <View key={comment.mid} style={{ marginBottom: 4 }}>
                <Comment comment={comment}></Comment>
              </View>
            )
            : null
        ))}
      </ScrollView>
      <View
        style={{
          margin: 10,
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <CommentInput userId={userId} showId={show.showId}></CommentInput>
        {/* TODO: 回放目前关闭。若开放，结束的演出也可以购买 */}
        {checkBuyButtonVisible() && (
          <View style={{ marginLeft: 50 }}>
            <BuyButton
              saleStart={show.ticketTimeStart}
              saleEnd={show.ticketTimeEnd}
              purchased={!!showChannelInfo?.purchased}
              amount={show.ticketPrice}
              userId={userId}
              showId={show.showId}
              onSuccess={() => onPurchased?.()}
            >
            </BuyButton>
          </View>
        )}
      </View>

    </View>
  );
};

export default InteractSection;