package controllers

import (
	"fmt"
	"github.com/gin-contrib/sessions"
	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
	. "italking.tomotoes.com/m/v1/dao"
	"italking.tomotoes.com/m/v1/ent"
	"italking.tomotoes.com/m/v1/ent/room"
	"italking.tomotoes.com/m/v1/ent/user"
	. "italking.tomotoes.com/m/v1/helper"
	"strings"
)

type LoginInfo struct {
	Name     string `json:"name" binding:"required"`
	Password string `json:"password"`
}

func SignIn(c *gin.Context) {
	var source LoginInfo
	if err := c.ShouldBindJSON(&source); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}

	isUserExist, err := G.User.Query().
		Where(user.NameEQ(source.Name)).
		Exist(G_)
	if isUserExist || err != nil {
		SendError(c, "NameConflict")
		return
	}

	hash, _ := bcrypt.GenerateFromPassword([]byte(source.Password), bcrypt.DefaultCost)
	newUser, err := G.User.Create().
		SetName(source.Name).
		SetPassword(string(hash)).
		AddFollowers(Admin).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "DBFailure")
		return
	}

	_, _ = SendWelcomeNotification(newUser)
	_, _ = SendFollowNotification(newUser, Admin)

	session := sessions.Default(c)
	session.Set("userId", newUser.ID)
	session.Set("authenticated", true)
	_ = session.Save()
	SendSuccess(c)
}

func Login(c *gin.Context) {
	var source LoginInfo
	if err := c.ShouldBindJSON(&source); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}

	first, err := G.User.Query().Where(user.NameEQ(source.Name)).First(G_)
	if first == nil || err != nil {
		SendError(c, "UserNonExistent")
		return
	}

	if err := bcrypt.CompareHashAndPassword([]byte(first.Password), []byte(source.Password)); err != nil {
		fmt.Println(err.Error())
		SendError(c, "PasswordError")
		return
	}

	session := sessions.Default(c)
	session.Set("userId", first.ID)
	session.Set("authenticated", true)
	_ = session.Save()
	SendSuccess(c)
}

func Search(c *gin.Context) {
	searchText := strings.ToLower(c.Query("searchText"))
	if searchText == "" {
		SendError(c, "SearchTextCantNull")
		return
	}
	users, err := G.User.Query().Where(user.NameContains(searchText)).All(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToSearchUserList")
		return
	}
	rooms, err := G.Room.Query().
		Where(room.NameContains(searchText)).
		Where(room.TypeEQ(room.TypePublic)).
		Where(room.HasSpeakers()).
		Order(ent.Desc(room.FieldCreatedAt)).
		All(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToSearchRoomList")
		return
	}
	SendData(c, map[string]interface{}{"users": users, "rooms": rooms})
}

func Pong(c *gin.Context) {
	SendSuccess(c)
}