import React, { Dispatch, useState, useRef } from 'react';
import {
  View,
  Keyboard,
  TouchableWithoutFeedback,
  ToastAndroid,
} from 'react-native';
import { Icon, Text, Header, } from 'react-native-elements';
import Button from '../../components/Button';

import FormInput from '../../components/FormInput';
import ImageUploader from '../../components/ImageUploader';
import { DefaultAvatarURL } from '../../constants/DefaultImageURL';
import { Role, updateUserInfoService, UserVO, certifyService } from '../../services/user';
import MyStorage from '../../utils/mystorage';

interface MineProps {
  user: UserVO,
  userDispatch: Dispatch<any>,
};

const Mine: React.FC<MineProps> = ({ user, userDispatch }) => {
  const [nickname, setNickname] = useState(user.nickname);

  const handleChangeAvatar = (url: string) => {
    updateUserInfoService({ userId: user.userId, form: { avatar: url } })
      .then((res) => {
        if (res.success) {
          userDispatch({
            type: 'update',
            payload: {
              avatar: url,
            },
          });
        }
      });
  };
  const handleChangeNicknameInput = (text: string) => setNickname(text);

  const handleBlurNicknameInput = () => {
    updateUserInfoService({ userId: user.userId, form: { nickname } })
      .then((res) => {
        if (res.success) {
          userDispatch({
            type: 'update',
            payload: {
              nickname: nickname,
            },
          });
        }
      });
  };

  const handlePressCertifyMerchant = () => {
    if (user.role === Role.Merchant) {
      ToastAndroid.show('您已经是商户', ToastAndroid.SHORT);
      return;
    }
    const form = {
      phone: user.phone,
      // TODO: 企业认证
      idNumber: '123',
    };
    certifyService({ form })
      .then((res) => {
        if (res.success) {
          ToastAndroid.show('认证成功', ToastAndroid.SHORT);
          console.log('certifyService', res.data)

          userDispatch({ type: 'update', payload: res.data });
        }
      });
  };

  const handlePressLogout = () => {
    MyStorage.setItem('token', '');
    MyStorage.setItem('userId', '');
  };

  return (
    <TouchableWithoutFeedback
      style={{ width: '100%', height: '100%', }}
      onPress={() => Keyboard.dismiss()}
    >
      <View>
        <Header
          backgroundColor='#f1f1f1'
          centerComponent={{ text: '个人信息', style: { color: 'black', fontSize: 18 } }}
        />
        <View
          style={{
            backgroundColor: '#f1f1f1',
          }}
        >
          <View style={{ backgroundColor: '#fff', }}>
            <ImageUploader
              userId={user.userId}
              url={user.avatar || DefaultAvatarURL}
              label='头像'
              onChange={handleChangeAvatar}
              onPress={() => Keyboard.dismiss()}
            ></ImageUploader>

            <FormInput
              label='昵称'
              borderBottomWidth={0}
              options={{
                defaultValue: user.nickname,
                onChangeText: handleChangeNicknameInput,
                onBlur: handleBlurNicknameInput,
              }}
            ></FormInput>
          </View>

          <View style={{ marginTop: 10, backgroundColor: '#fff', }}>
            <TouchableWithoutFeedback onPress={handlePressCertifyMerchant}>
              <View
                style={{
                  marginLeft: 10,
                  marginRight: 10,
                  paddingLeft: 10,
                  paddingRight: 10,
                  paddingTop: 12,
                  paddingBottom: 12,
                  borderBottomWidth: 1,
                  borderColor: 'rgb(237, 237, 237)',
                  flexDirection: 'row',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
              >
                <Text style={{ fontSize: 17, color: 'rgb(80, 80, 80)' }}>
                  {user.role === Role.Merchant ? '已认证商户' : '认证商户'}
                </Text>
                <Icon type='antdesign' name='right' size={17} color='rgb(166, 176, 184)'></Icon>
              </View>
            </TouchableWithoutFeedback>
          </View>

          <View style={{ marginTop: 10, backgroundColor: '#fff' }}>
            <TouchableWithoutFeedback onPress={handlePressLogout}>
              <View
                style={{
                  marginLeft: 10,
                  marginRight: 10,
                  paddingLeft: 10,
                  paddingRight: 10,
                  paddingTop: 12,
                  paddingBottom: 12,
                  borderBottomWidth: 1,
                  borderColor: 'rgb(237, 237, 237)',
                  flexDirection: 'row',
                  justifyContent: 'space-between',
                  alignItems: 'center',
                }}
              >
                <Text style={{ fontSize: 17, color: 'rgb(80, 80, 80)' }}>
                  退出登录
                </Text>
                <Icon type='antdesign' name='right' size={17} color='rgb(166, 176, 184)'></Icon>
              </View>
            </TouchableWithoutFeedback>
          </View>
        </View>
      </View>
    </TouchableWithoutFeedback>
  );
};

export default Mine;