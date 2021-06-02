var express = require('express');
var router = express.Router();
var path = require('path');
const RtmTokenBuilder = require('../DynamicKey/src/RtmTokenBuilder').RtmTokenBuilder;
const RtmRole = require('../DynamicKey/src/RtmTokenBuilder').Role;
const Priviledges = require('../DynamicKey/src/AccessToken').priviledges;
const appID  = "7d98dc45c22c4d43aa30b9c7ca37a4c6";
const appCertificate = "8ad9aba841d14d95a5d4ecf50c6f1f5f";

router.get('/',function(req, res){
    var username = req.query.username;
    const expirationTimeInSeconds = 3600*24
    let currentTimestamp = Math.floor(Date.now() / 1000)
    
    let privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds
    
    let newToken = RtmTokenBuilder.buildToken(appID, appCertificate, username, RtmRole, privilegeExpiredTs);
    res.json({
        status:200,
        token:newToken
    })
})





module.exports = router;