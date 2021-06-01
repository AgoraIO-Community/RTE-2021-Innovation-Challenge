package controllers

import (
	"fmt"
	"github.com/gin-contrib/sessions"
	"github.com/gin-gonic/gin"
	"golang.org/x/crypto/bcrypt"
	"italking.tomotoes.com/m/v1/config"
	. "italking.tomotoes.com/m/v1/dao"
	"italking.tomotoes.com/m/v1/ent/user"
	. "italking.tomotoes.com/m/v1/helper"
)

type UserInfo struct {
	Name        string `json:"name,omitempty"`
	Description string `json:"description,omitempty"`
	Password    string `json:"password,omitempty"`
}

func UserAuthRequired(c *gin.Context) {
	session := sessions.Default(c)
	authenticated := session.Get("authenticated")

	if authenticated == nil || !authenticated.(bool) {
		SendError(c, "Unauthorized User")
		return
	}
	c.Next()
}

func GetOwnInfo(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)

	source, err := G.User.Query().
		Where(user.IDEQ(uid)).
		WithFollowers().
		WithFollowings().
		First(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "UserNonExistent")
		return
	}

	SendData(c, source)
}

func GetUserInfo(c *gin.Context) {
	uid := c.Param("id")
	source, err := G.User.Query().
		Where(user.IDEQ(uid)).
		WithFollowers().
		WithFollowings().
		First(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "UserNonExistent")
		return
	}

	SendData(c, source)
}

func IsAuthedUser(c *gin.Context) {
	SendSuccess(c)
}

func UpdateUser(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	var info UserInfo
	if err := c.ShouldBindJSON(&info); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}
	if info.Description == "" {
		info.Description = config.DefaultUserDescription
	}
	_, err := G.User.UpdateOneID(uid).
		SetName(info.Name).
		SetDescription(info.Description).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToUpdate")
		return
	}
	SendSuccess(c)
}

func Speak(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	_, err := G.User.UpdateOneID(uid).SetSpeaking(true).Save(G_)

	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToSpeaking")
		return
	}
	SendSuccess(c)
}

func Mute(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	_, err := G.User.UpdateOneID(uid).SetSpeaking(false).Save(G_)

	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToMuting")
		return
	}
	SendSuccess(c)
}

func AddFollow(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	followedId := c.Param("id")
	_, _ = SendFollowNotificationByID(followedId, uid)

	_, err := G.User.UpdateOneID(uid).
		AddFollowingIDs(followedId).
		Save(G_)

	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToFollow")
		return
	}

	SendSuccess(c)
}

func CancelFollow(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	followedId := c.Param("id")

	_, err := G.User.UpdateOneID(uid).
		RemoveFollowingIDs(followedId).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToUnFollow")
		return
	}

	SendSuccess(c)
}

func GetRTMToken(c *gin.Context) {
	session := sessions.Default(c)
	userId := session.Get("userId").(string)
	rtmToken, err := GenerateRTMToken(userId)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToGenerateRTMToken")
		return
	}
	SendData(c, rtmToken)
}

func UpgradeSpeaker(c *gin.Context) {
	roomId := c.Param("roomId")
	session := sessions.Default(c)
	session.Set("hostRoomId", roomId)
	if err := session.Save(); err != nil {
		SendError(c, "FailedToUpgradeSpeaker")
	}
	SendSuccess(c)
}

func UpdatePassword(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	var info UserInfo
	if err := c.ShouldBindJSON(&info); err != nil {
		fmt.Println(err.Error())
		SendError(c, "JSONError")
		return
	}

	hash, _ := bcrypt.GenerateFromPassword([]byte(info.Password), bcrypt.DefaultCost)
	_, err := G.User.UpdateOneID(uid).
		SetPassword(string(hash)).
		Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToUpdatePassword")
		return
	}
	SendSuccess(c)
}

func Logout(c *gin.Context) {
	session := sessions.Default(c)
	session.Delete("userId")
	session.Set("authenticated", false)
	_ = session.Save()
	SendSuccess(c)
}
