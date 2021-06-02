package dao

import (
	"context"
	"fmt"
	"italking.tomotoes.com/m/v1/config"
	"italking.tomotoes.com/m/v1/ent"
	"italking.tomotoes.com/m/v1/ent/notification"
	"italking.tomotoes.com/m/v1/ent/user"
	"log"

	_ "github.com/go-sql-driver/mysql"
)

func GetAdmin() error {
	Admin, _ = G.User.Query().Where(user.NameEQ(config.AdminID)).First(G_)
	if Admin != nil {
		return nil
	}
	Admin, err = G.User.
		Create().
		SetName(config.AdminID).
		SetDescription("欢迎来到 ITalking").
		SetPassword("password").
		Save(G_)
	if err != nil {
		return fmt.Errorf("failed creating admin: %w", err)
	}
	log.Println("admin was created: ", Admin)
	return nil
}

func SendWelcomeNotification(receiver *ent.User) (*ent.Notification, error) {
	welcomeNotification, err := G.Notification.Create().
		SetContent(config.WelcomeNotification).
		SetSender(Admin).
		SetReceiver(receiver).
		SetType(notification.TypeOfficial).
		Save(G_)
	return welcomeNotification, err
}

func SendFollowNotification(receiver *ent.User, sender *ent.User) (*ent.Notification, error) {
	followNotification, err := G.Notification.Create().
		SetContent(config.FollowNotification).
		SetSender(sender).
		SetReceiver(receiver).
		SetType(notification.TypeFollow).
		Save(G_)
	return followNotification, err
}

func SendFollowNotificationByID(receiverID string, senderID string) (*ent.Notification, error) {
	followNotification, err := G.Notification.Create().
		SetContent(config.FollowNotification).
		SetSenderID(senderID).
		SetReceiverID(receiverID).
		SetType(notification.TypeFollow).
		Save(G_)
	return followNotification, err
}

var (
	G     *ent.Client
	G_    context.Context
	err   error
	Admin *ent.User
)

func Init() {
	G, err = ent.Open("mysql", config.GetDBMaster())
	if err != nil {
		log.Fatal(err)
	}
	G_ = context.Background()
	// Run the auto migration tool.
	if err := G.Schema.Create(G_); err != nil {
		log.Fatalf("failed creating schema resources: %v", err)
	}
	if err = GetAdmin(); err != nil {
		log.Fatal(err)
	}
}
