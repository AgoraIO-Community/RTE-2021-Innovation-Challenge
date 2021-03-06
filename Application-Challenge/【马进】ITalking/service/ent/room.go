// Code generated by entc, DO NOT EDIT.

package ent

import (
	"fmt"
	"strings"
	"time"

	"entgo.io/ent/dialect/sql"
	"italking.tomotoes.com/m/v1/ent/room"
	"italking.tomotoes.com/m/v1/ent/user"
)

// Room is the model entity for the Room schema.
type Room struct {
	config `json:"-"`
	// ID of the ent.
	ID string `json:"id,omitempty"`
	// Name holds the value of the "name" field.
	Name string `json:"name,omitempty"`
	// Description holds the value of the "description" field.
	Description string `json:"description,omitempty"`
	// Announcement holds the value of the "announcement" field.
	Announcement string `json:"announcement,omitempty"`
	// Type holds the value of the "type" field.
	Type room.Type `json:"type,omitempty"`
	// CreatedAt holds the value of the "created_at" field.
	CreatedAt time.Time `json:"created_at,omitempty"`
	// UpdatedAt holds the value of the "updated_at" field.
	UpdatedAt time.Time `json:"updated_at,omitempty"`
	// Edges holds the relations/edges for other nodes in the graph.
	// The values are being populated by the RoomQuery when eager-loading is set.
	Edges        RoomEdges `json:"edges"`
	room_creator *string
}

// RoomEdges holds the relations/edges for other nodes in the graph.
type RoomEdges struct {
	// Spectators holds the value of the spectators edge.
	Spectators []*User `json:"spectators,omitempty"`
	// Speakers holds the value of the speakers edge.
	Speakers []*User `json:"speakers,omitempty"`
	// Creator holds the value of the creator edge.
	Creator *User `json:"creator,omitempty"`
	// loadedTypes holds the information for reporting if a
	// type was loaded (or requested) in eager-loading or not.
	loadedTypes [3]bool
}

// SpectatorsOrErr returns the Spectators value or an error if the edge
// was not loaded in eager-loading.
func (e RoomEdges) SpectatorsOrErr() ([]*User, error) {
	if e.loadedTypes[0] {
		return e.Spectators, nil
	}
	return nil, &NotLoadedError{edge: "spectators"}
}

// SpeakersOrErr returns the Speakers value or an error if the edge
// was not loaded in eager-loading.
func (e RoomEdges) SpeakersOrErr() ([]*User, error) {
	if e.loadedTypes[1] {
		return e.Speakers, nil
	}
	return nil, &NotLoadedError{edge: "speakers"}
}

// CreatorOrErr returns the Creator value or an error if the edge
// was not loaded in eager-loading, or loaded but was not found.
func (e RoomEdges) CreatorOrErr() (*User, error) {
	if e.loadedTypes[2] {
		if e.Creator == nil {
			// The edge creator was loaded in eager-loading,
			// but was not found.
			return nil, &NotFoundError{label: user.Label}
		}
		return e.Creator, nil
	}
	return nil, &NotLoadedError{edge: "creator"}
}

// scanValues returns the types for scanning values from sql.Rows.
func (*Room) scanValues(columns []string) ([]interface{}, error) {
	values := make([]interface{}, len(columns))
	for i := range columns {
		switch columns[i] {
		case room.FieldID, room.FieldName, room.FieldDescription, room.FieldAnnouncement, room.FieldType:
			values[i] = new(sql.NullString)
		case room.FieldCreatedAt, room.FieldUpdatedAt:
			values[i] = new(sql.NullTime)
		case room.ForeignKeys[0]: // room_creator
			values[i] = new(sql.NullString)
		default:
			return nil, fmt.Errorf("unexpected column %q for type Room", columns[i])
		}
	}
	return values, nil
}

// assignValues assigns the values that were returned from sql.Rows (after scanning)
// to the Room fields.
func (r *Room) assignValues(columns []string, values []interface{}) error {
	if m, n := len(values), len(columns); m < n {
		return fmt.Errorf("mismatch number of scan values: %d != %d", m, n)
	}
	for i := range columns {
		switch columns[i] {
		case room.FieldID:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field id", values[i])
			} else if value.Valid {
				r.ID = value.String
			}
		case room.FieldName:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field name", values[i])
			} else if value.Valid {
				r.Name = value.String
			}
		case room.FieldDescription:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field description", values[i])
			} else if value.Valid {
				r.Description = value.String
			}
		case room.FieldAnnouncement:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field announcement", values[i])
			} else if value.Valid {
				r.Announcement = value.String
			}
		case room.FieldType:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field type", values[i])
			} else if value.Valid {
				r.Type = room.Type(value.String)
			}
		case room.FieldCreatedAt:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field created_at", values[i])
			} else if value.Valid {
				r.CreatedAt = value.Time
			}
		case room.FieldUpdatedAt:
			if value, ok := values[i].(*sql.NullTime); !ok {
				return fmt.Errorf("unexpected type %T for field updated_at", values[i])
			} else if value.Valid {
				r.UpdatedAt = value.Time
			}
		case room.ForeignKeys[0]:
			if value, ok := values[i].(*sql.NullString); !ok {
				return fmt.Errorf("unexpected type %T for field room_creator", values[i])
			} else if value.Valid {
				r.room_creator = new(string)
				*r.room_creator = value.String
			}
		}
	}
	return nil
}

// QuerySpectators queries the "spectators" edge of the Room entity.
func (r *Room) QuerySpectators() *UserQuery {
	return (&RoomClient{config: r.config}).QuerySpectators(r)
}

// QuerySpeakers queries the "speakers" edge of the Room entity.
func (r *Room) QuerySpeakers() *UserQuery {
	return (&RoomClient{config: r.config}).QuerySpeakers(r)
}

// QueryCreator queries the "creator" edge of the Room entity.
func (r *Room) QueryCreator() *UserQuery {
	return (&RoomClient{config: r.config}).QueryCreator(r)
}

// Update returns a builder for updating this Room.
// Note that you need to call Room.Unwrap() before calling this method if this Room
// was returned from a transaction, and the transaction was committed or rolled back.
func (r *Room) Update() *RoomUpdateOne {
	return (&RoomClient{config: r.config}).UpdateOne(r)
}

// Unwrap unwraps the Room entity that was returned from a transaction after it was closed,
// so that all future queries will be executed through the driver which created the transaction.
func (r *Room) Unwrap() *Room {
	tx, ok := r.config.driver.(*txDriver)
	if !ok {
		panic("ent: Room is not a transactional entity")
	}
	r.config.driver = tx.drv
	return r
}

// String implements the fmt.Stringer.
func (r *Room) String() string {
	var builder strings.Builder
	builder.WriteString("Room(")
	builder.WriteString(fmt.Sprintf("id=%v", r.ID))
	builder.WriteString(", name=")
	builder.WriteString(r.Name)
	builder.WriteString(", description=")
	builder.WriteString(r.Description)
	builder.WriteString(", announcement=")
	builder.WriteString(r.Announcement)
	builder.WriteString(", type=")
	builder.WriteString(fmt.Sprintf("%v", r.Type))
	builder.WriteString(", created_at=")
	builder.WriteString(r.CreatedAt.Format(time.ANSIC))
	builder.WriteString(", updated_at=")
	builder.WriteString(r.UpdatedAt.Format(time.ANSIC))
	builder.WriteByte(')')
	return builder.String()
}

// Rooms is a parsable slice of Room.
type Rooms []*Room

func (r Rooms) config(cfg config) {
	for _i := range r {
		r[_i].config = cfg
	}
}
