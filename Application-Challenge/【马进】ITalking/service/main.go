package main

import (
	"github.com/getsentry/sentry-go"
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/config"
	"italking.tomotoes.com/m/v1/dao"
	"italking.tomotoes.com/m/v1/middlewares"
	"italking.tomotoes.com/m/v1/routes"
	"log"
	"time"
)

func main() {
	app := gin.Default()
	config.Init()
	middlewares.Init(app)
	routes.Init(app)
	dao.Init()
	defer dao.G.Close()
	_ = sentry.Init(sentry.ClientOptions{Dsn: config.SentryDSN})
	defer sentry.Flush(2 * time.Second)
	log.Fatal(app.Run(config.GetServerAddress()))
}
