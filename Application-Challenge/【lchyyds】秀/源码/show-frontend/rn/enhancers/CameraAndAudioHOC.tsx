import React, { useEffect, useState, } from 'react';
import { Text, Platform, } from 'react-native';

import { requestCameraAndAudioPermission } from '../utils/permission';

// TODO: replace any
const CameraAndAudioHOC = (WrappedComponent: any) => {
  const EnhancedComponent = (props: any) => {
    const [permitted, setPermitted] = useState(Platform.OS !== 'android')

    useEffect(() => {
      if (Platform.OS === 'android') {
        // 设备授权
        requestCameraAndAudioPermission()
          .then(() => {
            console.log('设备已授权!');
            setPermitted(true);
          });
      }
    }, []);

    if (permitted) {
      return <WrappedComponent {...props}></WrappedComponent>;
    }

    return <Text style={{ marginTop: 50, textAlign: 'center' }}>演出需要设备授权😁</Text>;
  };

  return EnhancedComponent;
};

export default CameraAndAudioHOC;