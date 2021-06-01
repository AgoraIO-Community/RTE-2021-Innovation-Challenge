import { request } from '../utils/request';
import { UserVO } from './user';

export interface ShowForm {
  userId: string, // 创建商户userId

  title: string, // 演出名称
  description: string, // 演出简介

  poster: string, // 海报
  preview: string, // 预告片

  showTimeStart: number, // 演出开始时间
  showTimeEnd: number, //  演出结束时间

  ticketTimeStart: number, //  售票开始时间
  ticketTimeEnd: number, // 售票结束时间

  ticketPrice?: number | string, // 票单价
};

export interface ShowVO {
  showId: string, // 演出id
  showCreator: UserVO,

  title: string, // 演出title
  description: string, // 演出简介
  replayList: string[], // 演出回放

  poster: string, // 海报
  preview: string, // 预告片

  showTimeStart: number, // 演出开始时间
  showTimeEnd: number, // 演出结束时间

  ticketTimeStart: number, // 售票开始时间
  ticketTimeEnd: number, // 售票结束时间

  ticketPrice: number, // 票单价
};

interface CreateShowParams {
  form: ShowForm,
};

interface CreateShowResp {
  success: boolean,
  msg: string,
  data: ShowVO,
};

interface FetchShowListResp {
  success: boolean,
  msg: string,
  data: ShowVO[],
};

interface FetchPurchasedShowsParams {
  userId: string,
};

interface FetchPurchasedShowsResp {
  success: boolean,
  msg: string,
  data: ShowVO[],
};

interface SearchShowParams {
  keyword: string,
  pageNumber: number,
  pageSize: number,
};

interface SearchShowResp {
  success: boolean,
  msg: string,
  data: {
    hasNext: boolean,
    showVOList: ShowVO[],
  },
};

export const createShowService: (params: CreateShowParams) => Promise<CreateShowResp>
  = ({ form }) => {
    return request('/show/create', {
      method: 'POST',
      body: form,
    })
  };

export const fetchShowListService: () => Promise<FetchShowListResp>
  = () => {
    return request('/show/all');
  };

export const fetchPurchasedShowsService: (params: FetchPurchasedShowsParams) => Promise<FetchPurchasedShowsResp>
  = ({ userId }) => {
    return request(`/show/paid/${userId}`);
  };

export const searchShowService: (params: SearchShowParams) => Promise<SearchShowResp>
  = ({ keyword, pageNumber, pageSize, }) => {
    return request('/show/search', {
      searchParams: {
        keyword,
        pageNumber,
        pageSize,
      }
    });
  };