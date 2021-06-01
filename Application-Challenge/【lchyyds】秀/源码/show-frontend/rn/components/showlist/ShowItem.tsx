import React from 'react';
import { View, Text, TouchableWithoutFeedback } from 'react-native';
import { NavigationProp } from '@react-navigation/core';
import { Image } from 'react-native-elements';

import { ShowVO } from '../../services/show';
import { HomeStackNav } from '../../constants/Nav';
const DefaultPoster = require('../../resources/DefaultPoster.png');

interface ShowItemProps {
  navigation: NavigationProp<any>,
  show: ShowVO,
};

const ShowItem: React.FC<ShowItemProps> = ({ navigation, show, }) => {
  const handlePressShow = () => {
    navigation.navigate(HomeStackNav.ShowChannel, { theShow: show });
  };
  return (
    <TouchableWithoutFeedback onPress={handlePressShow}>
      <View
        style={{
          marginBottom: 4,
          backgroundColor: '#fff',
        }}
      >
        <Image
          source={show.poster ? { uri: show.poster } : DefaultPoster}
          style={{
            width: '100%',
            height: 100,
            overflow: 'hidden',
            ...(show.poster ? {} : { borderColor: 'rgba(0,0,0,0.3)', borderBottomWidth: 0.3, })
          }}
        />
        <Text style={{ paddingLeft: 4, paddingRight: 4, height: 25, lineHeight: 25, }}>
          {show.title}
        </Text>
      </View>
    </TouchableWithoutFeedback>
  );
};

export default ShowItem;