package controllers

import (
	"fmt"
	"github.com/gin-contrib/sessions"
	"github.com/gin-gonic/gin"
	. "italking.tomotoes.com/m/v1/dao"
	"italking.tomotoes.com/m/v1/ent/notification"
	"italking.tomotoes.com/m/v1/ent/user"
	. "italking.tomotoes.com/m/v1/helper"
)

func GetNotificationsAndRead(c *gin.Context) {
	session := sessions.Default(c)
	uid := session.Get("userId").(string)
	notificationList, err := G.Notification.Query().
		Where(notification.HasReceiverWith(user.IDEQ(uid))).
		WithSender().
		All(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToFindNotification")
		return
	}
	_, err = G.User.UpdateOneID(uid).SetUnread(false).Save(G_)
	if err != nil {
		fmt.Println(err.Error())
		SendError(c, "FailedToRead")
		return
	}
	SendData(c, notificationList)
}
