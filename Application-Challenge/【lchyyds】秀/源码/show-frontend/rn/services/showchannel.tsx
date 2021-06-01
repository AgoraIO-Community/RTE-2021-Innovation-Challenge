import { request } from '../utils/request';
import { WS } from '../utils/ws';
import { WSMessageType } from '../constants/WSConstants';

export enum ShowChannelRole {
  Publisher = 1,
  Subscriber = 2,
};

export enum ShowState {
  NotYet,   // 未开始
  Showing,  // 进行中
  End,      // 已结束
};

interface ShowChannelMetaVO {
  appId: string,
  channelName: string,
  token: string, // 连接的token
  uid: number, // 自己的uid
  publisherUid: number, // 主播的uid
  role: ShowChannelRole,
};

export interface ShowChannelInfoVO {
  showId: string,
  showState: ShowState, // 直播是否开始
  purchased: boolean, // 是否购买
  channelMetaVO: ShowChannelMetaVO,
};

interface FetchShowChannelInfoParams {
  showId: string,
  userId: string,
};

interface FetchShowChannelInfoResp {
  success: boolean,
  code: number,
  msg: string,
  data: ShowChannelInfoVO,
};

interface StartRecordParams {
  channelName: string,
};

interface StartRecordResp {
  success: boolean,
  code: number,
  msg: string,
  data: any,
};

interface StopRecordParams {
  channelName: string,
};

interface StopRecordResp {
  success: boolean,
  code: number,
  msg: string,
  data: any,
};

interface JoinShowParams {
  userId: string,
  showId: string,
};

interface LeaveShowParams {
  userId: string,
  showId: string,
};

interface SendMessageParams {
  userId: string,
  showId: string,
  content: string,
}

export const fetchShowChannelInfoService: (params: FetchShowChannelInfoParams) => Promise<FetchShowChannelInfoResp>
  = ({ showId, userId, }) => {
    return request(`/show/channel/info/${showId}/${userId}`);
  };

export const startRecordService: (params: StartRecordParams) => Promise<StartRecordResp>
  = ({ channelName }) => {
    return request(`/show/replay/start/${channelName}`, {
      method: 'POST',
    });
  };

export const stopRecordService: (params: StopRecordParams) => Promise<StopRecordResp>
  = ({ channelName }) => {
    return request(`/show/replay/stop/${channelName}`, {
      method: 'POST',
    });
  };

export const joinShowService: (params: JoinShowParams) => void
  = ({ userId, showId }) => {
    WS.sendMessage(
      userId,
      {
        type: WSMessageType.JoinShow,
        data: { showId },
      }
    );
  };

export const leaveShowService: (params: LeaveShowParams) => void
  = ({ userId, showId }) => {
    WS.sendMessage(
      userId,
      {
        type: WSMessageType.LeaveShow,
        data: { showId },
      }
    );
  };

export const sendMessageService: (params: SendMessageParams) => void
  = ({ userId, showId, content }) => {
    WS.sendMessage(
      userId,
      {
        type: WSMessageType.SendMsg,
        data: { showId, content },
      }
    );
  };