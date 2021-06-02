var express = require('express');
var router = express.Router();
const RtmTokenBuilder = require('../DynamicKey/src/RtmTokenBuilder').RtmTokenBuilder;
const RtmRole = require('../DynamicKey/src/RtmTokenBuilder').Role;
const appID  = "";
const appCertificate = "";

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