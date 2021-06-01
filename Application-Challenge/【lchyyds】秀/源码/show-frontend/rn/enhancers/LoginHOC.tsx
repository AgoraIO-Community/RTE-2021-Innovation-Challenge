import React, { useState, useEffect, } from 'react';
import SplashScreen from 'react-native-splash-screen';

import MyStorage from '../utils/mystorage';
import Login from '../containers/common/Login';
import { WS } from '../utils/ws';

const LoginHOC = (WrappedComponent: any) => {
  const Enhanced = (props: any) => {

    const [tokenChecked, setTokenChecked] = useState(false);
    const [hasToken, setHasToken] = useState(false);

    useEffect(() => {
      const handleTokenChange = (newToken: string) => {
        setHasToken(!!newToken);
      };
      MyStorage.watchItem('token', handleTokenChange)
        .then((token: string) => {
          setHasToken(!!token);
        })
        .finally(() => {
          setTimeout(() => {
            setTokenChecked(true);
            SplashScreen.hide();
          }, 1000);
        });

      const handleUserIdChange = (newUserId: string) => {
        newUserId && WS.init(newUserId);
      };
      MyStorage.watchItem('userId', handleUserIdChange);
    }, []);

    return (
      tokenChecked
        ? hasToken
          ? <WrappedComponent {...props}></WrappedComponent>
          : <Login {...props}></Login>
        : null
    );
  };
  return Enhanced;
};

export default LoginHOC;