package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
	"italking.tomotoes.com/m/v1/helper"
	"regexp"
	"time"
)

type Reservation struct {
	ent.Schema
}

func (Reservation) Fields() []ent.Field {
	return []ent.Field{
		field.String("id").
			Immutable().
			Unique().
			NotEmpty().
			DefaultFunc(helper.GetReservationId),
		field.String("name").
			MaxLen(15 * 3).
			MinLen(2).
			NotEmpty().
			Match(regexp.MustCompile("^[\u4e00-\u9fa5_a-zA-Z0-9\\s]+$")),
		field.String("description").
			Optional().
			MaxLen(80 * 3),
		field.Int("time").
			Positive(),
		field.Time("created_at").
			Default(time.Now),
		field.Time("updated_at").
			Default(time.Now).
			UpdateDefault(time.Now),
	}
}

func (Reservation) Edges() []ent.Edge {
	return []ent.Edge{
		edge.From("creator", User.Type).
			Ref("bookRooms").
			Unique().
			Required(),
	}
}
