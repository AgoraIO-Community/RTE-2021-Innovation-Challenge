import React from 'react';
import {
  View,
  ToastAndroid,
} from 'react-native';

import Button from '../Button';
import { payService } from '../../services/order';
import { SaleStatus } from '../../constants/SaleStatus';
import { getFormattedTime } from '../../utils/time';

interface BuyButtonProps {
  saleStart: number,
  saleEnd: number,
  purchased: boolean,
  amount: number,
  showId: string,
  userId: string,
  onSuccess?: () => void,
};

const BuyButton: React.FC<BuyButtonProps> = ({ saleStart, saleEnd, purchased, amount, showId, userId, onSuccess }) => {
  const getSaleStatus = () => {
    // TODO: use server time
    const now = Date.now();
    return (
      now >= saleEnd
        ? SaleStatus.Discontinued
        : now >= saleStart
          ? SaleStatus.OnSale
          : SaleStatus.NotForSale
    );
  };
  const getButtonTitle = () => {
    const saleStatus = getSaleStatus();
    return (
      saleStatus === SaleStatus.NotForSale
        ? `${getFormattedTime(saleStart)} 开售`
        : saleStatus === SaleStatus.Discontinued
          ? '停售'
          : purchased ? '已购' : '购买'
    );
  };
  const handlePressBuy = () => {
    const canBuy = getSaleStatus() === SaleStatus.OnSale && !purchased;
    if (!canBuy) { return; }

    const form = { amount, showId, userId, };
    payService({ form })
      .then((res) => {
        if (res.success) {
          ToastAndroid.show('购买成功!', ToastAndroid.LONG);
          onSuccess?.();
        }
      });
  };
  return (
    <View>
      <Button
        title={getButtonTitle()}
        color='tomato'
        onPress={handlePressBuy}
      ></Button>
    </View>
  );
};

export default BuyButton;