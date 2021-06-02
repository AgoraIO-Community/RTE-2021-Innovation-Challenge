import React from 'react';
import { View, Text, TouchableWithoutFeedback, } from 'react-native';

interface ButtonProps {
  width?: string | number,
  type?: 'primary' | 'default',
  size?: 'small' | 'large' | 'default' | 'superLarge',
  title?: string,
  color?: string,
  onPress?: () => void,
};

const Button: React.FC<ButtonProps> = ({ width, type = 'default', size = 'default', title, color, onPress }) => {
  const defaultColor = 'green';
  const realColor = color || defaultColor;

  const fontSizeMap = {
    'default': 14,
    'small': 12,
    'large': 18,
    'superLarge': 24,
  };

  return (
    <View
      style={{
        width: width || '100%',
        paddingTop: 4,
        paddingBottom: 4,
        paddingLeft: 10,
        paddingRight: 10,
        borderColor: realColor,
        borderWidth: 1,
        borderRadius: 4,
        backgroundColor: type === 'primary' ? realColor : 'rgba(0, 0, 0, 0)',
      }}
    >
      <TouchableWithoutFeedback onPress={() => onPress?.()}>
        <Text
          style={{
            textAlign: 'center',
            color: type === 'primary' ? '#fff' :realColor,
            fontSize: fontSizeMap[size],
          }}
        >{title || ''}</Text>
      </TouchableWithoutFeedback>

    </View>
  );
};

export default Button;