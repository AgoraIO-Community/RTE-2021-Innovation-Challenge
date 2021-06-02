package routes

import (
	"github.com/gin-gonic/gin"
	"italking.tomotoes.com/m/v1/controllers"
)

func addReservationGroups(group *gin.RouterGroup) {
	reservationGroup := group.Group("/reservation")
	reservationGroup.Use(controllers.UserAuthRequired)
	reservationGroup.GET("/list", controllers.GetReservationList)
	reservationGroup.PUT("/list", controllers.CreateReservation)
	reservationGroup.POST("/list/:id", controllers.UpdateReservation)
	reservationGroup.DELETE("/list/:id", controllers.DeleteReservation)
}
