package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
	"italking.tomotoes.com/m/v1/config"
	"italking.tomotoes.com/m/v1/helper"
	"regexp"
	"time"
)

type User struct {
	ent.Schema
}

func (User) Fields() []ent.Field {
	return []ent.Field{
		field.String("id").
			Immutable().
			Unique().
			NotEmpty().
			DefaultFunc(helper.GetUserId),
		field.String("name").
			Unique().
			MaxLen(10 * 3).
			MinLen(2).
			NotEmpty().
			Match(regexp.MustCompile("^[\u4e00-\u9fa5_a-zA-Z0-9\\s]+$")),
		field.String("password").
			Sensitive().
			NotEmpty(),
		field.String("description").
			Optional().
			Default(config.DefaultUserDescription).
			MaxLen(30 * 3),
		field.Bool("speaking").
			Default(true),
		field.Bool("unread").
			Default(true),
		field.Time("created_at").
			Default(time.Now),
		field.Time("updated_at").
			Default(time.Now).
			UpdateDefault(time.Now),
	}
}

func (User) Edges() []ent.Edge {
	return []ent.Edge{
		edge.To("followings", User.Type).
			From("followers"),
		edge.From("room", Room.Type).
			Ref("spectators").
			Unique(),
		edge.From("host", Room.Type).
			Ref("speakers").
			Unique(),
		edge.From("manage", Room.Type).
			Ref("creator"),
		edge.To("sendNotifications", Notification.Type),
		edge.To("receivedNotifications", Notification.Type),
		edge.To("bookRooms", Reservation.Type),
	}
}
