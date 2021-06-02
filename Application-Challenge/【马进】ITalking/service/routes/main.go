package routes

import "github.com/gin-gonic/gin"

func Init(app *gin.Engine) {
	v1 := app.Group("/v1")
	addEntryRoutes(v1)
	addUserRoutes(v1)
	addRoomRoutes(v1)
	addNotificationRoutes(v1)
	addReservationGroups(v1)
}
