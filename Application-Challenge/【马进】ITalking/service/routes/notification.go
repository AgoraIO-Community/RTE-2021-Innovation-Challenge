package routes

import (
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/controllers"
)

func addNotificationRoutes(group *gin.RouterGroup) {
	notificationGroup := group.Group("/notification")
	notificationGroup.Use(controllers.UserAuthRequired)
	notificationGroup.GET("/info", controllers.GetNotificationsAndRead)
}
