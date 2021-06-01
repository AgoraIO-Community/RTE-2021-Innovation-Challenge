package routes

import (
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/controllers"
)

func addRoomRoutes(group *gin.RouterGroup) {
	roomGroup := group.Group("/room")
	roomGroup.Use(controllers.UserAuthRequired)

	roomGroup.PUT("/create", controllers.CreateRoom)
	roomGroup.PUT("/info/:id", controllers.GetRoomInfoAndJoin)
	roomGroup.GET("/list", controllers.GetRoomList)
	roomGroup.GET("/token/:id", controllers.GetRTCToken)
	roomGroup.DELETE("/leave/:id", controllers.LeaveRoom)

	authedRoomGroup := group.Group("/room")
	authedRoomGroup.Use(controllers.UserAuthRequired)
	authedRoomGroup.Use(controllers.RoomAuthRequired)

	authedRoomGroup.POST("/info", controllers.UpdateRoom)
	authedRoomGroup.PUT("/speaker/:uid", controllers.AddSpeaker)
	authedRoomGroup.DELETE("/dissolve", controllers.DissolveRoom)
}
