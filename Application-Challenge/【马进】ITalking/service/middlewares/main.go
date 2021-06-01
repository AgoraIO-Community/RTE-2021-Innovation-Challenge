package middlewares

import "github.com/gin-gonic/gin"

func Init(app *gin.Engine) {
	configLogger(app)
	configRecovery(app)
	configSession(app)
	configCors(app)
}
