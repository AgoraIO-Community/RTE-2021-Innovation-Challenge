package middlewares

import "github.com/gin-gonic/gin"

func configRecovery(app *gin.Engine) {
	app.Use(gin.Recovery())
}
