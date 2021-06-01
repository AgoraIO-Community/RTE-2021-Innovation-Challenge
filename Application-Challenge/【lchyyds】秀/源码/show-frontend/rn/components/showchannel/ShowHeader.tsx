import React from 'react';
import { View, } from 'react-native';
import { Text, Tile, Image, Avatar, } from 'react-native-elements';
import { DefaultAvatarURL } from '../../constants/DefaultImageURL';

import { ShowVO } from '../../services/show';
import { getFormattedTime } from '../../utils/time';
const DefaultPoster = require('../../resources/DefaultPoster.png');

interface ShowHeaderProps {
  show: ShowVO,
};

const ShowHeader: React.FC<ShowHeaderProps> = ({ show, }) => {
  return (
    <View style={{ height: '100%', backgroundColor: '#fff', position: 'relative' }}>
      <Image
        source={show.poster ? { uri: show.poster } : DefaultPoster}
        style={{ height: '100%', resizeMode: 'cover' }}
      ></Image>
      <View
        style={{
          paddingTop: 4,
          paddingLeft: 10,
          paddingRight: 10,
          position: 'absolute',
          left: 0,
          top: 0,
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
          width: '100%',
          backgroundColor: 'rgba(0, 0, 0, 0.5)',
        }}
      >
        <View style={{ paddingLeft: 30, flexDirection: 'row', alignItems: 'center', height: 40 }}>
          <Avatar
            source={{ uri: show.showCreator.avatar || DefaultAvatarURL }}
            avatarStyle={{ width: 30, height: 30, borderRadius: 5, resizeMode: 'cover' }}
          />
          <Text style={{ marginLeft: 10, color: '#fff' }}>{show.showCreator.nickname}</Text>
        </View>
        <View>
          <Text style={{ fontSize: 18, color: '#fff' }}>{show.title}</Text>
        </View>
      </View>
      <View
        style={{
          padding: 6,
          position: 'absolute',
          right: '10%',
          bottom: 4,
          width: '80%',
          maxHeight: 47,
          overflow: 'hidden',
          backgroundColor: 'rgba(0, 0, 0, 0.7)',
          borderRadius: 4,
        }}
      >
        <Text
          style={{ color: '#fff', textAlign: 'center', fontSize: 16, }}
        >
          {getFormattedTime(show.showTimeStart)}
        </Text>
        <Text
          style={{ color: '#fff', textAlign: 'center', fontSize: 12, }}
          numberOfLines={1}
        >
          {show.description}
        </Text>
      </View>
    </View>
  );
};

export default ShowHeader;