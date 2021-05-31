export enum Code  {
  NoChannelInfo = 'NoChannelInfo',
  RTCEngineCreateFailed = 'RTCEngineCreateFailed',
};

export const Text = {
  [Code.NoChannelInfo]: '频道信息不得为空',
  [Code.RTCEngineCreateFailed]: 'RTC Engine创建失败',
};