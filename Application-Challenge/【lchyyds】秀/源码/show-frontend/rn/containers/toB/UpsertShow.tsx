import React, { useState, useEffect, } from 'react';
import {
  View,
  ScrollView,
  ToastAndroid,
  Text,
} from 'react-native';
import { Header } from 'react-native-elements';
import { NavigationProp } from '@react-navigation/core';

import ImageUploader from '../../components/ImageUploader';
import VideoUploader from '../../components/VideoUploader';
import MyDateTimePicker from '../../components/MyDateTimePicker';
import Button from '../../components/Button';
import { UserVO } from '../../services/user';
import { ShowForm, createShowService } from '../../services/show';
import { HomeStackNav } from '../../constants/Nav';
import FormInput from '../../components/FormInput';

interface UpsertShowProps {
  navigation: NavigationProp<any>,
  user: UserVO,
};

const UpsertShow: React.FC<UpsertShowProps> = ({ navigation, user }) => {

  const initialForm = {
    userId: user.userId,
    title: '',
    description: '',
    poster: '',
    preview: '',
    showTimeStart: 0,
    showTimeEnd: 0,
    ticketTimeStart: 0,
    ticketTimeEnd: 0,
    ticketPrice: undefined,
  };
  const [form, setForm] = useState<ShowForm>(initialForm);

  useEffect(() => {
    const unsubscribe = navigation.addListener('focus', () => {
      setForm(initialForm);
    });
    return () => {
      navigation.removeListener('focus', unsubscribe);
    };
  }, [navigation]);

  const handlePressSubmit = () => {
    console.log('submit', form)
    const title = form.title.trim();
    // 名称不得为空
    if (!title) {
      ToastAndroid.show('名称不得为空', ToastAndroid.LONG);
      return;
    }
    // 名称最多50个字符
    if (!(title.length <= 50)) {
      ToastAndroid.show('名称不得超过50字符', ToastAndroid.LONG);
      return;
    }
    const description = form.description.trim();
    // 简介最多140个字符
    if (description.length > 140) {
      ToastAndroid.show('简介不得超过140字符', ToastAndroid.LONG);
      return;
    }
    const { showTimeStart, showTimeEnd, ticketTimeStart, ticketTimeEnd, ticketPrice } = form;
    // 演出时间不得为空
    if (!showTimeStart) {
      ToastAndroid.show('演出开始时间不得为空', ToastAndroid.LONG);
      return;
    }
    if (!showTimeEnd) {
      ToastAndroid.show('演出结束时间不得为空', ToastAndroid.LONG);
      return;
    }
    // 演出结束时间不得早于开始时间
    if (showTimeEnd < showTimeStart) {
      ToastAndroid.show('演出结束时间不得早于开始时间', ToastAndroid.LONG);
      return;
    }
    // 票单价不得为空
    if (!ticketPrice) {
      ToastAndroid.show('票单价不得为空', ToastAndroid.LONG);
      return;
    }
    // 票单价需为正整数
    if (isNaN(Number(ticketPrice)) || !(ticketPrice >= 0)) {
      ToastAndroid.show('票单价需为正整数', ToastAndroid.LONG);
      return;
    }
    // 售票时间不得为空
    if (!ticketTimeStart) {
      ToastAndroid.show('售票开始时间不得为空', ToastAndroid.LONG);
      return;
    }
    if (!ticketTimeEnd) {
      ToastAndroid.show('售票结束时间不得为空', ToastAndroid.LONG);
      return;
    }
    // 售票结束时间不得早于开始时间
    if (ticketTimeEnd < ticketTimeStart) {
      ToastAndroid.show('售票结束时间不得早于开始时间', ToastAndroid.LONG);
      return;
    }

    createShowService({ form: { ...form, title, description, ticketPrice: Number(ticketPrice) } })
      .then((res) => {
        if (res.success) {
          ToastAndroid.show('发布成功', ToastAndroid.SHORT);
          navigation.navigate(HomeStackNav.AllShows);
        }
        else {
          ToastAndroid.show('发布失败' + JSON.stringify(res), ToastAndroid.LONG);
        }
      });
  };

  return (
    <View style={{ flex: 1, backgroundColor: '#f1f1f1' }}>
      <Header
        backgroundColor='#f1f1f1'
        centerComponent={{ text: '发布演出', style: { color: 'black', fontSize: 18 } }}
      />
      <ScrollView>
        <View style={{ backgroundColor: '#ffffff' }}>
          <View style={{ position: 'relative' }}>
            <Text style={{ position: 'absolute', left: 10, top: 18, color: 'tomato' }}>*</Text>
            <FormInput
              label='名称'
              options={{
                value: form.title,
                onChangeText: (text) => setForm({ ...form, title: text })
              }}
            >
            </FormInput>
          </View>

          <View>
            <FormInput
              label='简介'
              options={{
                value: form.description,
                onChangeText: (text) => setForm({ ...form, description: text })
              }}
            ></FormInput>
          </View>

          <ImageUploader
            userId={user.userId}
            url={form.poster}
            label='封面 (200M)'
            onChange={(url) => setForm({ ...form, poster: url })}
          ></ImageUploader>

          <VideoUploader
            userId={user.userId}
            url={form.preview}
            label='预告 (200M)'
            onChange={(url) => setForm({ ...form, preview: url })}
          ></VideoUploader>

          <View style={{ position: 'relative' }}>
            <Text style={{ position: 'absolute', left: 10, top: 18, color: 'tomato' }}>*</Text>
            <MyDateTimePicker
              id='1'
              label='开始时间'
              value={form.showTimeStart}
              onChange={(t) => t && setForm({ ...form, showTimeStart: t })}
            ></MyDateTimePicker>
          </View>

          <View style={{ position: 'relative' }}>
            <Text style={{ position: 'absolute', left: 10, top: 18, color: 'tomato' }}>*</Text>
            <MyDateTimePicker
              id='2'
              label='结束时间'
              value={form.showTimeEnd}
              onChange={(t) => t && setForm({ ...form, showTimeEnd: t })}
            ></MyDateTimePicker>
          </View>

        </View>

        <View style={{ marginTop: 10, backgroundColor: '#ffffff' }}>
          <View style={{ position: 'relative' }}>
            <Text style={{ position: 'absolute', left: 10, top: 18, color: 'tomato' }}>*</Text>
            <FormInput
              label='票单价'
              options={{
                value: form.ticketPrice === undefined ? '' : '' + form.ticketPrice,
                keyboardType: 'numeric',
                onChangeText: (text) => setForm({ ...form, ticketPrice: text }),
              }}
            ></FormInput>
          </View>

          <View style={{ position: 'relative' }}>
            <Text style={{ position: 'absolute', left: 10, top: 18, color: 'tomato' }}>*</Text>
            <MyDateTimePicker
              id='3'
              label='开售时间'
              value={form.ticketTimeStart}
              onChange={(t) => t && setForm({ ...form, ticketTimeStart: t })}
            ></MyDateTimePicker>
          </View>

          <View style={{ position: 'relative' }}>
            <Text style={{ position: 'absolute', left: 10, top: 18, color: 'tomato' }}>*</Text>
            <MyDateTimePicker
              id='4'
              label='停售时间'
              value={form.ticketTimeEnd}
              onChange={(t) => t && setForm({ ...form, ticketTimeEnd: t })}
            ></MyDateTimePicker>
          </View>
        </View>

        <View style={{ marginTop: 15, alignItems: 'center' }}>
          <Button
            width={300}
            title='确认发布'
            size='large'
            color='tomato'
            type='primary'
            onPress={handlePressSubmit}
          ></Button>
        </View>
      </ScrollView>
    </View>
  );
};



export default UpsertShow;