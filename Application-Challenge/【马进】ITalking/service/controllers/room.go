package controllers

import (
	"fmt"
	"github.com/gin-contrib/sessions"
	"github.com/gin-gonic/gin"
	. "italking.tomotoes.com/m/v1/dao"
	"italking.tomotoes.com/m/v1/ent"
	"italking.tomotoes.com/m/v1/ent/room"
	"italking.tomotoes.com/m/v1/ent/user"
	. "italking.tomotoes.com/m/v1/helper"
)

type RoomInfo struct {
	Name         string    `json:"name" binding:"required"`
	Description  string    `json:"description,omitempty"`
	Announcement string    `json:"announcement,omitempty"`
	Type         room.Type `json:"type"`
}

func RoomAuthRequired(c *gin.Context) {
	session := sessions.Default(c)
	hostRoomId := session.Get("hostRoomId")

	if hostRoomId == nil {
		SendError(c, "Unauthorized User")
		return
	}
	c.Next()
}

func CreateRoom(c *gin.Context) {
	var source RoomInfo
	if err := c.ShouldBindJSON(&source); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}
	var roomId string
	for {
		roomId = RandStringBytes(5)
		isRoomExist, _ := G.Room.Query().
			Where(room.IDEQ(roomId)).
			Exist(G_)
		if !isRoomExist {
			break
		}
	}

	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	session.Set("hostRoomId", roomId)
	_ = session.Save()

	_, err := G.Room.Create().
		SetName(source.Name).
		SetNillableDescription(&source.Description).
		SetNillableAnnouncement(&source.Announcement).
		SetType(source.Type).
		SetID(roomId).
		SetCreatorID(uid).
		AddSpeakerIDs(uid).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToCreateRoom")
		return
	}

	SendData(c, roomId)
}

func GetRoomInfoAndJoin(c *gin.Context) {
	roomId := c.Param("id")
	source, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		Where(room.HasSpeakers()).
		First(G_)
	if source == nil || err != nil {
		SendError(c, "RoomNonExistent")
		return
	}
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	hostRoomId := session.Get("hostRoomId")

	if hostRoomId != nil && hostRoomId.(string) == roomId {
		_, err = G.User.UpdateOneID(uid).
			ClearRoom().
			SetHost(source).
			SetSpeaking(true).
			Save(G_)
	} else {
		_, err = G.User.UpdateOneID(uid).
			ClearHost().
			SetRoom(source).
			SetSpeaking(true).
			Save(G_)
	}
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToJoinRoom")
		return
	}
	roomInfo, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		WithCreator().
		WithSpeakers().
		WithSpectators().
		First(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToGetRoomInfo")
		return
	}
	SendData(c, roomInfo)
}

func AddSpeaker(c *gin.Context) {
	uid := c.Param("uid")
	session := sessions.Default(c)
	roomId := session.Get("hostRoomId").(string)

	source, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		Where(room.HasSpeakers()).
		First(G_)
	if source == nil || err != nil {
		SendError(c, "RoomNonExistent")
		return
	}

	_, err = G.User.UpdateOneID(uid).
		ClearRoom().
		SetHost(source).
		SetSpeaking(true).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToUpgradeSpeaker")
		return
	}

	SendSuccess(c)
}

func UpdateRoom(c *gin.Context) {
	session := sessions.Default(c)
	roomId := session.Get("hostRoomId").(string)

	source, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		Where(room.HasSpeakers()).
		First(G_)
	if source == nil || err != nil {
		SendError(c, "RoomNonExistent")
		return
	}

	var info RoomInfo
	if err := c.ShouldBindJSON(&info); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}

	_, err = source.Update().
		SetName(info.Name).
		SetNillableDescription(&info.Description).
		SetNillableAnnouncement(&info.Announcement).
		SetType(info.Type).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToUpdateRoom")
		return
	}
	SendSuccess(c)
}

func GetRoomList(c *gin.Context) {
	roomList, err := G.Room.Query().
		Where(room.TypeEQ(room.TypePublic)).
		Where(room.HasSpeakers()).
		Order(ent.Desc(room.FieldCreatedAt)).
		WithCreator().
		WithSpeakers().
		WithSpectators().
		All(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToFindRoomLint")
	}

	SendData(c, roomList)
}

func GetRTCToken(c *gin.Context) {
	roomId := c.Param("id")
	isRoomExist, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		Exist(G_)
	if !isRoomExist || err != nil {
		SendError(c, "RoomNonExistent")
		return
	}
	rtcToken, err := GenerateRTCToken(roomId)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToGenerateRTCToken")
		return
	}
	SendData(c, rtcToken)
}

func DissolveRoom(c *gin.Context) {
	session := sessions.Default(c)
	roomId := session.Get("hostRoomId").(string)
	session.Delete("hostRoomId")
	_ = session.Save()

	source, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		Where(room.HasSpeakers()).
		First(G_)
	if source == nil || err != nil {
		SendError(c, "RoomNonExistent")
		return
	}

	_, err = G.User.Update().
		Where(user.HasRoomWith(room.IDEQ(roomId))).
		ClearRoom().
		ClearHost().
		ClearManage().
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToDissolveRoom")
		return
	}

	err = G.Room.DeleteOne(source).Exec(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToDeleteRoom")
		return
	}

	SendSuccess(c)
}

func LeaveRoom(c *gin.Context) {
	roomId := c.Param("id")
	source, err := G.Room.Query().
		Where(room.IDEQ(roomId)).
		First(G_)
	if source == nil || err != nil {
		SendError(c, "RoomNonExistent")
		return
	}
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	_, err = G.User.UpdateOneID(uid).
		ClearHost().
		ClearRoom().
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToExitRoom")
		return
	}
	SendSuccess(c)
}
