import React, { useState } from 'react';
import {
  View,
  TouchableWithoutFeedback,
  Platform,
} from 'react-native';
import { Icon, Text, } from 'react-native-elements';
import DateTimePicker from '@react-native-community/datetimepicker';

import { getFormattedTime } from '../utils/time';

interface MyDateTimePickerProps {
  id: string,
  label: string,
  value: number,
  onChange?: (dateTime: number | undefined) => void,
};

const MyDateTimePicker: React.FC<MyDateTimePickerProps> = ({ id, label, value, onChange, }) => {
  const [datePickerVisible, setDatePickerVisible] = useState(false);
  const [timePickerVisible, setTimePickerVisible] = useState(false);

  const [date, setDate] = useState<number | undefined>(undefined);

  const handleChangeDate = (event: Event, date?: Date) => {
    setDatePickerVisible(false);
    if (date) {
      setDate(date.valueOf());
      setTimePickerVisible(true);
    }
  };

  const handleChangeTime = (event: Event, timeDate?: Date) => {
    setTimePickerVisible(false);
    if (timeDate && date) {
      const res = new Date();
      res.setTime(timeDate.valueOf());

      const dateHelper = new Date();
      dateHelper.setTime(date);

      res.setFullYear(dateHelper.getFullYear(), 0, 1);
      res.setMonth(dateHelper.getMonth(), 1);
      res.setDate(dateHelper.getDate());

      onChange?.(res.valueOf());
    }
  };

  const renderTigger = () => {
    return (
      <TouchableWithoutFeedback
        onPress={() => {
          !datePickerVisible &&
          !timePickerVisible &&
          setDatePickerVisible(true);
        }}
      >
        <View
          style={{
            marginLeft: 10,
            marginRight: 10,
            borderBottomWidth: 1,
            borderColor: 'rgb(237, 237, 237)',
            padding: 10,
            flexDirection: 'row',
            justifyContent: 'space-between',
            alignItems: 'center',
            height: 50,
          }}
        >
          <Text style={{ fontSize: 17, color: 'rgb(80, 80, 80)' }}>
            {label || '选择时间'}
          </Text>
          <View style={{ flexDirection: 'row', alignItems: 'center' }}>
            <Text>{getFormattedTime(value)}</Text>
            <Icon type='antdesign' name='right' size={17} color='rgb(166, 176, 184)'></Icon>
          </View>
        </View>
      </TouchableWithoutFeedback>
    );
  };

  return (
    <View>
      {renderTigger()}

      {datePickerVisible && (
        <DateTimePicker
          testID={`datePicker_${id}`}
          is24Hour={true}
          mode='date'
          display="default"
          value={value ? new Date(value) : new Date()}
          onChange={handleChangeDate}
        />
      )}
      {timePickerVisible && (
        <DateTimePicker
          testID={`timePicker_${id}`}
          is24Hour={true}
          mode='time'
          display="default"
          value={value ? new Date(value) : new Date()}
          onChange={handleChangeTime}
        />
      )}
    </View>
  );
};

export default MyDateTimePicker;