package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
	"italking.tomotoes.com/m/v1/helper"
	"time"
)

type Notification struct {
	ent.Schema
}

func (Notification) Fields() []ent.Field {
	return []ent.Field{
		field.String("id").
			Immutable().
			Unique().
			NotEmpty().
			DefaultFunc(helper.GetNotificationId),
		field.String("content").
			NotEmpty().NotEmpty(),
		field.Enum("type").
			Values("official", "follow", "invite"),
		field.Time("created_at").
			Default(time.Now),
		field.Time("updated_at").
			Default(time.Now).
			UpdateDefault(time.Now),
	}
}

func (Notification) Edges() []ent.Edge {
	return []ent.Edge{
		edge.From("sender", User.Type).
			Ref("sendNotifications").
			Unique().
			Required(),
		edge.From("receiver", User.Type).
			Ref("receivedNotifications").
			Unique().
			Required(),
	}
}
