package helper

import (
	rtctokenbuilder "github.com/AgoraIO/Tools/DynamicKey/AgoraDynamicKey/go/src/RtcTokenBuilder"
	rtmtokenbuilder "github.com/AgoraIO/Tools/DynamicKey/AgoraDynamicKey/go/src/RtmTokenBuilder"
	"italking.tomotoes.com/m/v1/config"
	"time"
)

func GenerateRTCToken(channelName string) (string, error) {
	appID := config.GetAgoraAppId()
	appCertificate := config.GetAgoraCertificate()

	currentTimestamp := uint32(time.Now().UTC().Unix())
	expireTimestamp := currentTimestamp + config.TokenExpireTime

	return rtctokenbuilder.BuildTokenWithUID(appID, appCertificate, channelName, 0, rtctokenbuilder.RolePublisher, expireTimestamp)
}

func GenerateRTMToken(uid string) (string, error) {
	appID := config.GetAgoraAppId()
	appCertificate := config.GetAgoraCertificate()

	currentTimestamp := uint32(time.Now().UTC().Unix())
	expireTimestamp := currentTimestamp + config.TokenExpireTime

	return rtmtokenbuilder.BuildToken(appID, appCertificate, uid, rtmtokenbuilder.RoleRtmUser, expireTimestamp)
}
