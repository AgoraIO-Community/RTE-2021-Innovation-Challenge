/**
 * 文字互动输入框
 */
import React, { useState, useEffect, } from 'react';

import { View, Keyboard, ToastAndroid, TextInput, } from 'react-native';
import Button from '../Button';
import { sendMessageService } from '../../services/showchannel';

const COMMENT_MAX = 50;

interface CommentInputProps {
  userId: string,
  showId: string,
};

const CommentInput: React.FC<CommentInputProps> = ({ userId, showId }) => {
  const [focused, setFocused] = useState(false);
  const [comment, setComment] = useState('');

  useEffect(() => {
    const keyboardDidHideHandler = () => {
      handleBlurInput();
    };
    Keyboard.addListener('keyboardDidHide', keyboardDidHideHandler);
    return () => {
      Keyboard.removeListener('keyboardDidHide', keyboardDidHideHandler);
    };
  }, []);

  const handleSudmit = () => {
    if (comment.length > COMMENT_MAX) {
      ToastAndroid.show(`最多输入${COMMENT_MAX}字`, ToastAndroid.LONG);
      return;
    }

    sendMessageService({
      userId,
      showId,
      content: comment,
    });
    setComment('');
    handleBlurInput();
  };

  const handleBlurInput = () => {
    setFocused(false);
    Keyboard.dismiss();
  };

  return (
    <View style={{ width: focused ? '100%' : 68, flexDirection: 'row', justifyContent: 'space-between', alignItems: 'center' }}>
      <TextInput
        placeholder='聊聊吧~'
        placeholderTextColor='rgba(255,255,255,0.4)'
        selectionColor='#fff'
        style={{
          flex: focused ? 0.75 : 1,
          paddingTop: 1,
          paddingBottom: 1,
          paddingLeft: 8,
          paddingRight: 8,
          borderWidth: 1,
          borderColor: '#fff',
          borderRadius: 20,
          fontSize: 14,
          color: '#fff',
        }}
        value={comment}
        onChangeText={(text: string) => setComment(text)}
        onFocus={() => setFocused(true)}
        onBlur={() => setFocused(false)}
      ></TextInput>
      {focused && (
        <View style={{ flex: 0.2, width: 30 }}>
          <Button title='发送' onPress={handleSudmit} color='yellow'></Button>
        </View>
      )}
    </View>
  );
};

export default CommentInput;