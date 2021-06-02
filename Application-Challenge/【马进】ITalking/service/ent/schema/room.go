package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/schema/edge"
	"entgo.io/ent/schema/field"
	"regexp"
	"time"
)

type Room struct {
	ent.Schema
}

func (Room) Fields() []ent.Field {
	return []ent.Field{
		field.String("id").
			Unique().
			MaxLen(5).
			NotEmpty().
			Match(regexp.MustCompile("^[a-zA-Z0-9]+$")),
		field.String("name").
			MaxLen(15 * 3).
			MinLen(2).
			NotEmpty().
			Match(regexp.MustCompile("^[\u4e00-\u9fa5_a-zA-Z0-9\\s]+$")),
		field.String("description").
			Optional().
			MaxLen(80 * 3),
		field.String("announcement").
			Optional(),
		field.Enum("type").
			Values("public", "private"),
		field.Time("created_at").
			Default(time.Now),
		field.Time("updated_at").
			Default(time.Now).
			UpdateDefault(time.Now),
	}
}

func (Room) Edges() []ent.Edge {
	return []ent.Edge{
		edge.To("spectators", User.Type),
		edge.To("speakers", User.Type),
		edge.To("creator", User.Type).
			Unique().
			Required(),
	}
}
