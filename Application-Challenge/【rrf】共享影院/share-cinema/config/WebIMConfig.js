export default {
  socketServer: "//im-api-v2.easemob.com/ws", // socket Server地址

  restServer: "//a1.easemob.com", // rest Server地址

  appkey: "1132210601094469#demo", // App key

  https: true, // 是否使用https

  isHttpDNS: true, // 3.0 SDK支持，防止DNS劫持从服务端获取XMPPUrl、restUrl

  isMultiLoginSessions: false, // 是否开启多页面同步收消息，注意，需要先联系商务开通此功能

  isDebug: false, // 打开调试，会自动打印log，在控制台的console中查看log

  autoReconnectNumMax: 2, // 断线重连最大次数

  heartBeatWait: 30000, // 心跳间隔（只在小程序中使用）

  delivery: false, // 是否发送已读回执

  useOwnUploadFun: false, // 是否使用自己的上传方式（如将图片文件等上传到自己的服务器，构建消息时只传url）
};
