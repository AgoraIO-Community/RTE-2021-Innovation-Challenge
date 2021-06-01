package middlewares

import (
	"github.com/gin-contrib/cors"
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/config"
)

func configCors(app *gin.Engine) {
	app.Use(cors.New(cors.Config{
		AllowOrigins:     []string{config.GetWebAddress()},
		AllowMethods:     []string{"POST", "GET", "PUT", "DELETE"},
		AllowHeaders:     []string{"Content-Type"},
		ExposeHeaders:    []string{"*"},
		AllowCredentials: true,
	}))
}
