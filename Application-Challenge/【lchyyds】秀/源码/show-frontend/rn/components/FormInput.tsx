import React from 'react';
import { View } from 'react-native';
import { Text, Input, InputProps, } from 'react-native-elements';

interface FormInputProps {
  label?: string,
  borderBottomWidth?: number,
  options: InputProps,
};

const FormInput: React.FC<FormInputProps> = ({ label, borderBottomWidth = 1, options = {} }) => {

  return (
    <View
      style={{
        marginLeft: 10,
        marginRight: 10,
        paddingLeft: 10,
        paddingRight: 10,
        borderBottomWidth,
        borderColor: 'rgb(237, 237, 237)',
        flexDirection: 'row',
        justifyContent: 'space-between',
        alignItems: 'center',
      }}
    >
      <Text style={{ fontSize: 17, color: 'rgb(80, 80, 80)' }}>
        {label || ''}
      </Text>
      <Input
        containerStyle={{
          paddingTop: 0,
          paddingRight: 0,
          paddingBottom: 0,
          paddingLeft: 0,
        }}
        inputContainerStyle={{
          borderBottomWidth: 0
        }}
        style={{
          paddingLeft: 20,
          paddingRight: 40,
          textAlign: 'right',
        }}
        errorStyle={{
          marginTop: 0,
          marginBottom: 0,
          height: 0,
        }}
        blurOnSubmit
        {...options}
      ></Input>
    </View>
  );
};

export default FormInput;