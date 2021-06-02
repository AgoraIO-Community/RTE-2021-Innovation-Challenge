import { ToastAndroid } from "react-native";
import { Code } from "../constants/Code";
import MyStorage from "./mystorage";
import { getServerAddress } from "./server";

interface SearchParams {
  [k: string]: string | number,
};
interface RequestOptions {
  method?: string,
  searchParams?: SearchParams,
  headers?: HeadersInit_,
  body?: any,
  bodyNoJSON?: boolean,
};

const getParamStr = (searchParams?: SearchParams) => {
  let res = '';
  if (searchParams) {
    Object.keys(searchParams).forEach((key) => {
      if (res === '') {
        res += `?${key}=${searchParams[key]}`;
      }
      else {
        res += `&${key}=${searchParams[key]}`;
      }
    });
  }
  return res;
};

export const request: (path: string, options?: RequestOptions, noAuth?: boolean) => Promise<any>
  = async (path, options, noAuth) => {
    let token = '';
    let userId = '';

    if (!noAuth) {
      try {
        token = await MyStorage.getItem('token');
        userId = await MyStorage.getItem('userId');
      }
      catch (err) {
        console.error('err in request get token', err);
      }
    }

    const url = `http://${getServerAddress()}${path}${getParamStr(options?.searchParams)}`;
    const init: RequestInit = {
      method: options?.method || 'GET',
      headers: {
        'Accept': 'application/json',
        'Content-Type': 'application/json',
        'Connection': "close",
        'token': token,
        'userId': userId,
        ...(options?.headers || {}),
      },
    };
    const noBody = (!options?.method || options?.method === 'GET' || options?.method === 'HEAD');
    !noBody && (init['body'] = options?.bodyNoJSON ? (options?.body || {}) : JSON.stringify(options?.body || {}));

    return fetch(url, init)
      .then((res) => res.json())
      .then((json) => {
        if (json.code === Code.RequestNoToken || json.code === Code.TokenInvalid) {
          MyStorage.setItem('token', '');
          MyStorage.setItem('userId', '');
        }
        return json;
      })
      .catch((err) => {
        console.error('请求失败：', err);
        ToastAndroid.show('请求失败' + JSON.stringify(err), ToastAndroid.LONG);
        return Promise.reject(err);
      });
  };