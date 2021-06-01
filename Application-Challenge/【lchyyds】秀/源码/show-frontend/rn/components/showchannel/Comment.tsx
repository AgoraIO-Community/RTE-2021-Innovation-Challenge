import React from 'react';
import { View, Text } from 'react-native';
import { IReceiveMsgData } from '../../constants/WSConstants';

interface CommentProps {
  comment: IReceiveMsgData,
}

const Comment: React.FC<CommentProps> = ({ comment }) => {
  if (!comment) { return null; }

  return (
    <View
      style={{
        paddingTop: 4,
        paddingBottom: 4,
        paddingLeft: 8,
        paddingRight: 8,
        borderWidth: 1,
        borderRadius: 30,
        flexDirection: 'row',
        flexWrap: 'wrap',
        backgroundColor: '#000'
      }}
    >
      <Text style={{ color: 'pink', fontSize: 14, }}>
        {comment.senderUserVO?.nickname || ''}
        <Text style={{ color: '#fff', fontSize: 14, }}>
          &nbsp;&nbsp;
          {comment.content || ''}
      </Text>
      </Text>
    </View>
  );
};

export default Comment;