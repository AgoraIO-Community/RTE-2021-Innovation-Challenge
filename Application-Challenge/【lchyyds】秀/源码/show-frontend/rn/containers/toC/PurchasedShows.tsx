import React, { useEffect, useState, } from 'react';
import {
  View,
  ScrollView,
  ToastAndroid,
  Text,
  ActivityIndicator,
} from 'react-native';
import { NavigationProp } from '@react-navigation/core';
import { Header } from 'react-native-elements';

import { UserVO, } from '../../services/user';
import { fetchPurchasedShowsService, ShowVO } from '../../services/show';
import ShowItem from '../../components/showlist/ShowItem';

interface PurchasedShowsProps {
  navigation: NavigationProp<any>,
  user: UserVO,
};

const PurchasedShows: React.FC<PurchasedShowsProps> = ({ navigation, user, }) => {
  const [showList, setShowList] = useState<ShowVO[] | undefined>(undefined);

  useEffect(() => {
    const unsubscribe = navigation.addListener('focus', () => {
      fetchShowList();
    });
    return () => {
      navigation.removeListener('focus', unsubscribe);
    };
  }, [navigation]);

  const fetchShowList = () => {
    fetchPurchasedShowsService({ userId: user.userId })
      .then((res) => {
        if (res.success) {
          setShowList(res.data);
        }
        else {
          ToastAndroid.show('获取列表失败' + JSON.stringify(res), ToastAndroid.LONG);
        }
      });
  };

  const renderShowList = () => {
    if (showList === undefined) {

      return (
        <View style={{ flexDirection: 'row', justifyContent: 'center', margin: 50, }}>
          <ActivityIndicator size='small' color="rgb(166, 176, 184)" />
          <Text style={{ fontSize: 16, marginLeft: 10, color: 'rgb(166, 176, 184)' }}>加载中...</Text>
        </View>
      );
    }

    if (!showList?.length) {
      return (
        <View style={{ margin: 50, alignItems: 'center' }}>
          <Text style={{ textAlign: 'center', fontSize: 20, color: 'tomato' }}>😅您还未购买过演出~</Text>
        </View>
      );
    }

    return (
      <ScrollView>
        <View
          style={{
            marginTop: 4,
            paddingTop: 4,
            backgroundColor: '#f1f1f1',
            flexDirection: 'row',
            justifyContent: 'space-between',
          }}
        >
          {/* 左侧 */}
          <View style={{ width: '49.5%' }}>
            {showList?.map((show, i) => (
              !(i % 2)
                ? <ShowItem key={i} navigation={navigation} show={show}></ShowItem>
                : null
            ))}
          </View>
          {/* 右侧 */}
          <View style={{ width: '49.5%' }}>
            {showList?.map((show, i) => (
              !!(i % 2)
                ? <ShowItem key={i} navigation={navigation} show={show}></ShowItem>
                : null
            ))}
          </View>
        </View>
      </ScrollView>
    );
  };

  return (
    <View
      style={{
        height: '100%',
        backgroundColor: '#ffffff',
      }}
    >
      <Header
        backgroundColor='#f1f1f1'
        centerComponent={{ text: '已购演出', style: { color: 'black', fontSize: 18 } }}
      />
      {renderShowList()}
    </View>
  );
};

export default PurchasedShows;