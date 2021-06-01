import React, { useEffect, useReducer, } from 'react';

import { fetchUserInfoService, Role, UserVO } from './services/user';
import MyStorage from './utils/mystorage';
import Route from './Route';

const UserInitialState = {
  userId: '',
  phone: '',
  nickname: '',
  avatar: '',
  role: Role.Merchant,
};
const UserReducer = (state: UserVO, action: any) => {
  switch (action.type) {
    case 'update':
      return { ...state, ...action.payload };
    default:
      return state;
  }
};

const Entry = () => {
  const [userState, userDispatch] = useReducer(UserReducer, UserInitialState);

  useEffect(() => {
    MyStorage.getItem('userId')
      .then((userId) => {
        userId && userDispatch({ type: 'update', payload: { userId: userId } });
      });

    MyStorage.watchItem('userId', (newUserId) => {
      userDispatch({ type: 'update', payload: { userId: newUserId } });
    });
  }, []);

  useEffect(() => {
    if (userState?.userId) {
      fetchUserInfo(userState?.userId);
    }
  }, [userState?.userId]);

  const fetchUserInfo = (userId: string) => {
    fetchUserInfoService({ userId })
      .then((res) => {
        if (res.success) {
          userDispatch({
            type: 'update',
            payload: res.data,
          });
          MyStorage.setItem('userId', res.data.userId);
        }
      })
  };

  return (
    <Route user={userState} userDispatch={userDispatch}></Route>
  );
};

export default Entry;
