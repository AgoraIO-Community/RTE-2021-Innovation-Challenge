const { RtmTokenBuilder, RtmRole } = require("agora-access-token");
const express = require("express");

const appID = process.env.AGORA_APP_ID;
const appCertificate = process.env.AGORA_APP_CERT;

const expirationTimeInSeconds = 3600;
const currentTimestamp = Math.floor(Date.now() / 1000);

const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;

const app = express();

app.get("/token/:account", (req, res) => {
  res.send({
    token: RtmTokenBuilder.buildToken(
      appID,
      appCertificate,
      req.params.account,
      RtmRole,
      privilegeExpiredTs
    ),
  });
});

const port = process.env.PORT || 8080;
app.listen(port, () => {
  console.log("token server ready at", port);
});
