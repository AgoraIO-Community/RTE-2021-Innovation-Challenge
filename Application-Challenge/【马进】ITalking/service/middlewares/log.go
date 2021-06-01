package middlewares

import "github.com/gin-gonic/gin"

func configLogger(app *gin.Engine) {
	app.Use(gin.Logger())
}
