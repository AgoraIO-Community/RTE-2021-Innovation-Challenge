const express = require('express');
const { RtcTokenBuilder, RtcRole } = require('agora-access-token');

const { APP_ID, APP_CERTIFICATE } = process.env;
const router = express.Router();

if (!(APP_ID && APP_CERTIFICATE)) {
  throw new Error('You must define an APP_ID and APP_CERTIFICATE');
}

function authCheck(req, res, next) {
  if (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'debug') {
    next();
    return;
  }

  if (req.get('origin') === 'https://bla-bla.app') {
    next();
    return;
  }

  res.status(401).end();
}

function nocache(req, res, next) {
  res.header('Cache-Control', 'private, no-cache, no-store, must-revalidate');
  res.header('Expires', '-1');
  res.header('Pragma', 'no-cache');
  next();
}

function generateAccessToken(req, resp) {
  if (process.env.NODE_ENV === 'development' || process.env.NODE_ENV === 'debug') {
    resp.header('Access-Control-Allow-Origin', '*');
  } else {
    resp.header('Access-Control-Allow-Origin', 'https://bla-bla.app');
  }

  const { channel } = req.query;
  if (!channel) {
    return resp.status(500).json({ error: 'channel name is required' });
  }

  let { uid } = req.query;
  if (!uid) {
    uid = 0;
  }

  const expirationTimeInSeconds = 3600;
  const currentTimestamp = Math.floor(Date.now() / 1000);
  const privilegeExpiredTs = currentTimestamp + expirationTimeInSeconds;

  return resp.json({
    token: RtcTokenBuilder.buildTokenWithUid(
      APP_ID,
      APP_CERTIFICATE,
      channel,
      uid,
      RtcRole.PUBLISHER,
      privilegeExpiredTs,
    ),
  });
}

router.get('/', authCheck, nocache, generateAccessToken);

module.exports = router;
