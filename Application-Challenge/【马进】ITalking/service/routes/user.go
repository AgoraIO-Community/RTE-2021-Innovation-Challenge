package routes

import (
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/controllers"
)

func addUserRoutes(group *gin.RouterGroup) {
	userGroup := group.Group("/user")
	userGroup.Use(controllers.UserAuthRequired)

	userGroup.GET("/isAuthed", controllers.IsAuthedUser)
	userGroup.GET("/info/:id", controllers.GetUserInfo)
	userGroup.GET("/ownInfo", controllers.GetOwnInfo)
	userGroup.POST("/ownInfo", controllers.UpdateUser)
	userGroup.POST("/speak", controllers.Speak)
	userGroup.POST("/mute", controllers.Mute)
	userGroup.POST("/follow/:id", controllers.AddFollow)
	userGroup.DELETE("/follow/:id", controllers.CancelFollow)
	userGroup.GET("/token", controllers.GetRTMToken)
	userGroup.POST("/upgrade/:roomId", controllers.UpgradeSpeaker)
	userGroup.POST("/password", controllers.UpdatePassword)
	userGroup.DELETE("/logout", controllers.Logout)
}
