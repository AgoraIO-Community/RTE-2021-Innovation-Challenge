import { UserVO } from "../services/user";

export enum WSReadyState {
  Connecting = 0,
  Open = 1,
  Closing = 2,
  Closed = 3,
};


export enum WSMessageType {
  JoinShow = 0,   //加入show房间
  LeaveShow = 1,  //离开show房间
  SendMsg = 2,    //在show房间发送消息
  ReceiveMsg = 3,  //在show房间收到消息
};

export interface IWSMessage {
  type: WSMessageType,
  data: any,
};

export interface IWSReceiveMessage {
  type: WSMessageType,
  voData: ResVO | any,
};

export interface IReceiveMsgData {
  mid: string,
  showId: string,
  senderUserVO: UserVO,
  content: string,
};

export interface ResVO {
  success: boolean,
  code: number,
  message: string,
  data: any,
}