import React, { useEffect, useState, } from 'react';
import {
  View,
  ToastAndroid,
  Dimensions,
} from 'react-native';
import { Text, Image, } from 'react-native-elements';
import DeviceInfo from 'react-native-device-info';

import Button from '../../components/Button';
import { loginService } from '../../services/user';
import { requestPhoneNumberPermission } from '../../utils/permission';
import MyStorage from '../../utils/mystorage';
const Logo = require('../../resources/LogoWhiteBg.png');
const { width: screenWidth, height: screenHeight } = Dimensions.get('screen');

// TODO: login func is still weak now
const DEFAULT_PHONE_NUMBER = '12345678910';

const Login: React.FC = () => {
  const [phoneNumber, setPhoneNumber] = useState('');
  const [uniqueId, setUniqueId] = useState('');

  useEffect(() => {
    requestPhoneNumberPermission()
      .then(() => {
        DeviceInfo?.getPhoneNumber()
          .then((realPhoneNumber) => {
            const phoneNumberRegex = /\+?[0-9]+/;
            if (phoneNumberRegex.test(realPhoneNumber)) {
              setPhoneNumber(realPhoneNumber);
            }
            else {
              ToastAndroid.show('SIM1没读到😅用测试账号吧~', ToastAndroid.LONG);
              setUniqueId(DeviceInfo?.getUniqueId());
            }
          })
          .catch((err) => {
            ToastAndroid.show('SIM1读取失败😅用测试账号吧~', ToastAndroid.LONG);
            setUniqueId(DeviceInfo?.getUniqueId());
          });
      })
      .catch(() => {
        ToastAndroid.show('(///▽///)不授权只能用测试号哟~', ToastAndroid.LONG);
        setUniqueId(DeviceInfo?.getUniqueId());
      });
  }, []);

  const maskPhoneNumber = (thePhoneNumber: string) => {
    return `${thePhoneNumber.slice(0, -8)}****${thePhoneNumber.slice(-4)}`;
  };

  const handlePressLogin = () => {
    const form = {
      // TODO：目前登录功能较弱
      phone: phoneNumber || uniqueId,
    };
    loginService({ form })
      .then((res) => {
        if (res.success && res.data) {
          const { userId, token } = res.data;
          if (userId && token) {
            MyStorage.setItem('userId', userId);
            MyStorage.setItem('token', token);
          }
        }
        else {
          ToastAndroid.show(`登录失败${res?.msg || ''}`, ToastAndroid.LONG);
          console.error('loginService', res)
        }
      });
  };

  return (
    <View style={{ backgroundColor: '#fff', flex: 1, }}>
      <Image
        source={Logo}
        style={{
          marginTop: screenHeight / 4,
          marginLeft: screenWidth / 2 - 25,
          padding: 5,
          width: 50,
          height: 50,
        }}
        resizeMode='contain'
      />
      <View style={{ marginTop: 80 }}>
        <Text h3 style={{ textAlign: 'center' }}>
          {maskPhoneNumber(phoneNumber || DEFAULT_PHONE_NUMBER)}
        </Text>
      </View>
      <View style={{ margin: 30, alignItems: 'center' }}>
        <Button
          title={phoneNumber ? '本机号码一键登录' : '登录测试账号'}
          width={240}
          color='tomato'
          size='large'
          onPress={handlePressLogin}
        ></Button>
      </View>
    </View>
  );
};

export default Login;