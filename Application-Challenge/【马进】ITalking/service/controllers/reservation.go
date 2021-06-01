package controllers

import (
	"fmt"
	"github.com/gin-contrib/sessions"
	"github.com/gin-gonic/gin"
	. "italking.tomotoes.com/m/v1/dao"
	"italking.tomotoes.com/m/v1/ent"
	"italking.tomotoes.com/m/v1/ent/reservation"
	. "italking.tomotoes.com/m/v1/helper"
	"time"
)

type ReservationInfo struct {
	Description string `json:"description,omitempty"`
	Time        int    `json:"time"`
	Name        string `json:"name"`
}

func GetReservationList(c *gin.Context) {
	reservationList, err := G.Reservation.Query().
		Where(reservation.TimeGTE(int(time.Now().Unix()))).
		Order(ent.Asc(reservation.FieldTime)).
		WithCreator().
		All(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToFindReservationList")
		return
	}
	SendData(c, reservationList)
}

func CreateReservation(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	var source ReservationInfo
	if err := c.ShouldBindJSON(&source); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}
	_, err := G.Reservation.Create().
		SetName(source.Name).
		SetNillableDescription(&source.Description).
		SetTime(source.Time).
		SetCreatorID(uid).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToCreateReservation")
		return
	}

	SendSuccess(c)
}

func UpdateReservation(c *gin.Context) {
	reservationId := c.Param("id")
	var source ReservationInfo
	if err := c.ShouldBindJSON(&source); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}
	_, err := G.Reservation.UpdateOneID(reservationId).
		SetName(source.Name).
		SetNillableDescription(&source.Description).
		SetTime(source.Time).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToCreateReservation")
		return
	}
	SendSuccess(c)
}

func DeleteReservation(c *gin.Context) {
	reservationId := c.Param("id")
	err := G.Reservation.
		DeleteOneID(reservationId).
		Exec(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToDeleteReservation")
		return
	}
	SendSuccess(c)
}
