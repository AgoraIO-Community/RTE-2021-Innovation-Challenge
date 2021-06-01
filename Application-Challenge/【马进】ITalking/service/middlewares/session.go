package middlewares

import (
	"fmt"
	"github.com/gin-contrib/sessions"
	"github.com/gin-contrib/sessions/redis"
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/config"
)

func configSession(app *gin.Engine) {
	store, err := redis.NewStore(10, "tcp", config.GetRedisAddress(), config.GetRedisPassword(), config.GetRedisSecret())
	store.Options(sessions.Options{Path: "/", Domain: config.GetCookieDomain(), MaxAge: config.CookieExpireTime, Secure: true, HttpOnly: true, SameSite: config.GetSameSite()})
	if err != nil {
		fmt.Println("failed to connect redis")
		panic(err.Error())
	}
	app.Use(sessions.Sessions(config.SessionKey, store))
}
