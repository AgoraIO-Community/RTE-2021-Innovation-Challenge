package config

import (
	"github.com/joho/godotenv"
	"log"
	"net/http"
	"os"
	"strconv"
)

func Init() {
	err := godotenv.Load()
	if err != nil {
		log.Fatal("Error loading .env file")
	}
}

func GetServerAddress() string {
	return os.Getenv("HOST") + os.Getenv("PORT")
}

func GetDBMaster() string {
	return os.Getenv("DBMaster")
}

func GetRedisAddress() string {
	return os.Getenv("REDIS_ADDRESS")
}

func GetRedisSecret() []byte {
	return []byte(os.Getenv("REDIS_SECRET"))
}

func GetRedisPassword() string {
	return os.Getenv("REDIS_PASSWORD")
}

func GetWebAddress() string {
	return os.Getenv("WEB_ADDRESS")
}

func GetCookieDomain() string {
	return os.Getenv("COOKIE_DOMAIN")
}

func GetSameSite() http.SameSite {
	sameSite, _ := strconv.Atoi(os.Getenv("SAME_SITE"))
	return http.SameSite(sameSite)
}

func GetAgoraAppId() string {
	return os.Getenv("AGORA_APP_ID")
}

func GetAgoraCertificate() string {
	return os.Getenv("AGORA_CERTIFICATE")
}

// Magic Number 专属于我
const SessionKey = "wbxhxll"

const CookieExpireTime = 60 * 60 * 24 * 30
const TokenExpireTime = 60 * 60 * 24

const DefaultUserDescription = "这家伙很懒，还没有写简介"

const WelcomeNotification = "欢迎来到 ITalking，去结识那些陌生而有趣的人吧~"
const FollowNotification = "关注了你~"

const AdminID = "管理员"
const SentryDSN = "https://cb59a36d8ab74143a0b1c5bd9790991b@o717267.ingest.sentry.io/5779800"
