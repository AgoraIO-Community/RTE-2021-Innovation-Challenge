package routes

import (
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/controllers"
)

func addEntryRoutes(group *gin.RouterGroup) {
	entryGroup := group.Group("/entry")

	entryGroup.POST("/signIn", controllers.SignIn)
	entryGroup.POST("/login", controllers.Login)
	entryGroup.GET("/search", controllers.Search)

	entryGroup.GET("/ping", controllers.Pong)
}
