import { request } from '../utils/request';

interface PayForm {
  amount: number, // 支付钱数
  showId: string,
  userId: string,
}

interface PayParams {
  form: PayForm,
};

interface PayResp {
  success: boolean,
  code: number,
  msg: string,
  data: any,
};

export const payService: (params: PayParams) => Promise<PayResp>
  = ({ form }) => {
    return request('/order/pay', {
      method: 'POST',
      body: form,
    });
  };