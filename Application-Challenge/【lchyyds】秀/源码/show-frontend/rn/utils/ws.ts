import { getServerAddress } from "./server";
import { IWSMessage, IWSReceiveMessage, ResVO, WSMessageType } from '../constants/WSConstants';
import { Code } from '../constants/Code';
import { WSReadyState } from '../constants/WSConstants';
import MyStorage from "./mystorage";

export class WS {
  private static instance: WebSocket;
  private static receiveMsgListenerID = -1;
  private static receiveMsgListenerMap: Map<number, Function>;

  static init(userId: string) {
    WS.instance = new WebSocket(`ws://${getServerAddress()}/im/${userId}`);

    WS.instance.onopen = () => {
      console.log('=================ws on open', WS.instance?.readyState);
    };

    WS.instance.onmessage = (e) => {
      console.log('=================ws on message', e);
      try {
        const msg = JSON.parse(e.data);
        WS.handleWSMessage(msg);
      }
      catch (err) {
        console.error('ws msg data is not JSON', err)
      }
    };

    WS.instance.onerror = (e) => {
      console.error('=================ws on error', e, WS.instance?.readyState);
      // ws意外断开，尝试重新连接
      if (WS.instance?.readyState !== WSReadyState.Open) {
        console.log('retry init ws')
        MyStorage.getItem('userId')
          .then((data) => {
            WS.init(data);
          });
      }
    };

    WS.instance.onclose = (e) => {
      console.log('=================ws on close', e);
    };

  };

  static getInstance(userId: string) {
    if (!WS.instance) {
      WS.init(userId);
    }
    return WS.instance;
  };

  static getReceiveMsgListenerID() {
    if (!(WS.receiveMsgListenerID >= 0)) {
      WS.receiveMsgListenerID = 0;
    }
    return WS.receiveMsgListenerID;
  };

  static getReceiveMsgListenerMap() {
    if (!WS.receiveMsgListenerMap) {
      WS.receiveMsgListenerMap = new Map();
    }
    return WS.receiveMsgListenerMap;
  };

  static updateReceiveMsgListenerID() {
    WS.receiveMsgListenerID += 1;
  };

  static addReceiveMsgListener: (listener: Function) => number
    = (listener) => {
      const id = WS.getReceiveMsgListenerID();

      WS.getReceiveMsgListenerMap().set(id, listener);
      WS.updateReceiveMsgListenerID();

      return id;
    };

  static removeReceiveMsgListener: (id: number) => void
    = (id) => {
      WS.getReceiveMsgListenerMap().delete(id);
    };

  static async sendMessage(userId: string, wsMessage: IWSMessage) {
    try {
      const token = await MyStorage.getItem('token');
      const message = {
        ...wsMessage,
        token,
      };
      WS.getInstance(userId).send(JSON.stringify(message));
    }
    catch (err) {
      console.error('========================= send msg err', err);
    }
  };

  private static handleWSMessage = (msg: IWSReceiveMessage) => {
    switch (msg.type) {
      case -1:
        // 出错
        const resVO: ResVO = msg.voData;
        if (
          resVO.code === Code.RequestNoToken ||
          resVO.code === Code.TokenInvalid
        ) {
          // TODO:
          console.error('websocket token problem')
          MyStorage.setItem('token', '');
          MyStorage.setItem('userId', '');
        }
        break;

      case WSMessageType.ReceiveMsg:
        WS.getReceiveMsgListenerMap()
          .forEach((listener) => listener?.(msg.voData));
        break;
      default:
        break;
    };
  };

  static close(userId: string) {
    WS.getInstance(userId).close();
  };
};