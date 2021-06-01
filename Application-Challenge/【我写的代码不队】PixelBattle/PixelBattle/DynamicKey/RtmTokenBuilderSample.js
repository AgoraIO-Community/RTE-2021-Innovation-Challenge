const RtmTokenBuilder = require('./src/RtmTokenBuilder').RtmTokenBuilder;
const RtmRole = require('./src/RtmTokenBuilder').Role;
const Priviledges = require('./src/AccessToken').priviledges;
const appID  = "7d98dc45c22c4d43aa30b9c7ca37a4c6";
const appCertificate = "8ad9aba841d14d95a5d4ecf50c6f1f5f";
const account = "aaa";

const expirationTimeInSeconds = 3600*24
const currentTimestamp = Math.floor(Date.now() / 1000)

const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds

const token = RtmTokenBuilder.buildToken(appID, appCertificate, account, RtmRole, privilegeExpiredTs);
console.log("Rtm Token: " + token);
