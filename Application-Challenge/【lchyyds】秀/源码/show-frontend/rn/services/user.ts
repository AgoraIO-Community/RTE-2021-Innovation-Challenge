import { request } from '../utils/request';

export enum Role {
  Audience,
  Merchant,
};
interface LoginForm {
  phone: string,
};
export interface UserVO {
  userId: string,
  phone: string,
  nickname: string,
  avatar: string,
  role: Role,
};
interface CertifyForm {
  phone: String,
  idNumber: string,
}
interface LoginParams {
  form: LoginForm,
};
interface LoginResp {
  success: boolean,
  code: number,
  msg: string,
  data: {
    token: string,
    userId: string,
  },
};
interface CertifyParams {
  form: CertifyForm,
};
interface CertifyResp {
  success: boolean,
  code: number,
  msg: string,
  data: UserVO,
};
interface FetchUserInfoParams {
  userId: string,
};
interface FetchUserInfoResp {
  success: boolean,
  code: number,
  msg: string,
  data: UserVO,
};
interface UpdateUserInfoParams {
  userId: string,
  form: {
    nickname?: string,
    avatar?: string,
  },
};
interface UpdateUserInfoResp {
  success: boolean,
  code: number,
  msg: string,
  data: UserVO,
};

export const loginService: (params: LoginParams) => Promise<LoginResp>
  = ({ form }) => {
    return request(
      '/user/login',
      {
        method: 'POST',
        body: form,
      },
      true
    );
  };

export const certifyService: (params: CertifyParams) => Promise<CertifyResp>
  = ({ form }) => {
    return request('/user/certification', {
      method: 'POST',
      body: form,
    })
  };

export const fetchUserInfoService: (params: FetchUserInfoParams) => Promise<FetchUserInfoResp>
  = ({ userId }) => {
    return request(`/user/info/id/${userId}`);
  };

export const updateUserInfoService: (params: UpdateUserInfoParams) => Promise<UpdateUserInfoResp>
  = ({ userId, form, }) => {
    return request(`/user/info/update/${userId}`, {
      method: 'POST',
      body: form,
    });
  };