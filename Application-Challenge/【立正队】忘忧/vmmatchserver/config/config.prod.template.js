/* eslint valid-jsdoc: "off" */

'use strict';

/**
 * @param {Egg.EggAppInfo} appInfo app info
 */
module.exports = appInfo => {
  /**
   * built-in config
   * @type {Egg.EggAppConfig}
   */
  const config = {};

  /**
   * 配置 alinode 监控，这里本地调试暂时随便填写，正式环境另外配置
   */
  config.alinode = {
    appid: 'alinode appId',
    secret: 'alinode secret',
  };

  /**
   * Easemob 配置，后台地址 https://console.easemob.com/app/im-service/detail
   */
  config.easemob = {
    host: 'http://a1.easemob.com', // 环信 API 请求接口，在环信后台查看
    orgName: 'orgName', // 环信 appKey 前半段
    appName: 'appName', // 环信 appkey 后半段
    clientId: 'client id', // 替换环信后台 clientId
    clientSecret: 'client secret', // 替换环信后台 clientSecret
  };

  /**
   * 邮箱配置
   */
  config.mail = {
    config: {
      host: 'smtp.exmail.qq.com', // 主机地址，这里使用的是腾讯企业邮箱
      port: 465,
      secure: true,
      auth: {
        user: '你自己的邮箱',
        pass: '邮箱密码', // 这个密码要根据邮箱后台确认是使用账户密码，还是客户端独有的密码
      },
    },
    from: 'VMLoft<notify@vmloft.com>', // from 表示邮件发送者，必须与认证账户相同
    activateSubject: '【社交系统】验证电子邮件地址',
    activateContent: '<div style="width:100%; background-color:#f6f7f8; margin: 0; padding: 0; font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 14px;"><div style="width: 100%; height: 36px;"></div><div style="min-width: 360px; max-width: 600px; margin: 20px auto; background-color: #fff; box-shadow: 0px 5px 15px #f8f8f8"><div style="font-size: 22px; text-align:center; border-bottom: #eee 1px solid; margin: 0 16px"><div style="padding: 24px;">【社交系统】验证电子邮件地址</div></div><div style="width: 100%; text-align: left;  background-color: #ffffff;"><div style="min-width: 320px; max-width: 500px; margin: auto; color: #232323; line-height: 1.5; padding: 8px 0;"><div style="font-size: 18px; font-weight: bold; padding: 24px 16px;">尊敬的{0}您好！</div><div style="padding: 16px;">感谢您使用我们的服务，这里需要验证下您的电子邮件地址，点击下方的按钮以验证电子邮箱：</div><div style="padding: 8px 16px;"><a href="{1}/sign/activate?verify={2}">验证电子邮件地址</a></div><div style="padding: 16px;">如果您并没进行此类操作请忽略此邮件！</div><div style="color:#999999; padding: 24px 16px; text-align: end">社交系统团队</div></div></div><div style="margin:0 16px; padding: 16px; color: #999999; text-align:center; border-top: #eee 1px solid">©️2021社交系统.</div></div><div style="width: 100%; height: 36px;"></div></div>',
    codeSubject: '【社交系统】验证码',
    codeContent: '<div style="width:100%; background-color:#f6f7f8; margin: 0; padding: 0; font-family: Roboto-Regular,Helvetica,Arial,sans-serif; font-size: 14px;"><div style="width: 100%; height: 36px;"></div><div style="min-width: 360px; max-width: 600px; margin: 20px auto; background-color: #fff; box-shadow: 0px 5px 15px #f8f8f8"><div style="font-size: 22px; text-align:center; border-bottom: #eee 1px solid; margin: 0 16px"><div style="padding: 24px;">【社交系统】验证码</div></div><div style="width: 100%; text-align: left;  background-color: #ffffff;"><div style="min-width: 320px; max-width: 500px; margin: auto; color: #232323; line-height: 1.5; padding: 8px 0;"><div style="font-size: 18px; font-weight: bold; padding: 24px 16px;">尊敬的{0}您好！</div><div style="padding: 16px;">感谢您使用我们的服务，您正在进行敏感操作，下边是您请求的验证码</div><div style="padding: 8px 16px;"><strong style="color: #ff7138; font-size: 20px;font-weight: bold;">{1}</strong></div><div style="padding: 16px;">如果您并没进行此类操作请忽略此邮件！</div><div style="color:#999999; padding: 24px 16px; text-align: end">社交系统团队</div></div></div><div style="margin:0 16px; padding: 16px; color: #999999; text-align:center; border-top: #eee 1px solid">©️2021社交系统.</div></div><div style="width: 100%; height: 36px;"></div></div>',
  };

  /**
   * Mongoose 配置，服务器数据库需要验证
   */
  config.mongoose = {
    client: {
      url: 'mongodb://127.0.0.1:27017/vmmatch', // 未开启验证的链接方式
      // url: 'mongodb://match:123456@127.0.0.1:27017/vmmatch', // 开启验证的链接方式
      options: {
        bufferMaxEntries: 0,
        useFindAndModify: false,
        useUnifiedTopology: true,
      },
    },
  };

  // 用户配置
  const dataConfig = {
    // 部署服务地址
    host: '自己的服务器域名地址',
    // 配置邮箱注册账户是否需要激活
    isNeedActivate: false,
  };
  return {
    ...config,
    ...dataConfig,
  };
};
