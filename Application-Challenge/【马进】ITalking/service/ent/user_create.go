// Code generated by entc, DO NOT EDIT.

package ent

import (
	"context"
	"errors"
	"fmt"
	"time"

	"entgo.io/ent/dialect/sql/sqlgraph"
	"entgo.io/ent/schema/field"
	"italking.tomotoes.com/m/v1/ent/notification"
	"italking.tomotoes.com/m/v1/ent/reservation"
	"italking.tomotoes.com/m/v1/ent/room"
	"italking.tomotoes.com/m/v1/ent/user"
)

// UserCreate is the builder for creating a User entity.
type UserCreate struct {
	config
	mutation *UserMutation
	hooks    []Hook
}

// SetName sets the "name" field.
func (uc *UserCreate) SetName(s string) *UserCreate {
	uc.mutation.SetName(s)
	return uc
}

// SetPassword sets the "password" field.
func (uc *UserCreate) SetPassword(s string) *UserCreate {
	uc.mutation.SetPassword(s)
	return uc
}

// SetDescription sets the "description" field.
func (uc *UserCreate) SetDescription(s string) *UserCreate {
	uc.mutation.SetDescription(s)
	return uc
}

// SetNillableDescription sets the "description" field if the given value is not nil.
func (uc *UserCreate) SetNillableDescription(s *string) *UserCreate {
	if s != nil {
		uc.SetDescription(*s)
	}
	return uc
}

// SetSpeaking sets the "speaking" field.
func (uc *UserCreate) SetSpeaking(b bool) *UserCreate {
	uc.mutation.SetSpeaking(b)
	return uc
}

// SetNillableSpeaking sets the "speaking" field if the given value is not nil.
func (uc *UserCreate) SetNillableSpeaking(b *bool) *UserCreate {
	if b != nil {
		uc.SetSpeaking(*b)
	}
	return uc
}

// SetUnread sets the "unread" field.
func (uc *UserCreate) SetUnread(b bool) *UserCreate {
	uc.mutation.SetUnread(b)
	return uc
}

// SetNillableUnread sets the "unread" field if the given value is not nil.
func (uc *UserCreate) SetNillableUnread(b *bool) *UserCreate {
	if b != nil {
		uc.SetUnread(*b)
	}
	return uc
}

// SetCreatedAt sets the "created_at" field.
func (uc *UserCreate) SetCreatedAt(t time.Time) *UserCreate {
	uc.mutation.SetCreatedAt(t)
	return uc
}

// SetNillableCreatedAt sets the "created_at" field if the given value is not nil.
func (uc *UserCreate) SetNillableCreatedAt(t *time.Time) *UserCreate {
	if t != nil {
		uc.SetCreatedAt(*t)
	}
	return uc
}

// SetUpdatedAt sets the "updated_at" field.
func (uc *UserCreate) SetUpdatedAt(t time.Time) *UserCreate {
	uc.mutation.SetUpdatedAt(t)
	return uc
}

// SetNillableUpdatedAt sets the "updated_at" field if the given value is not nil.
func (uc *UserCreate) SetNillableUpdatedAt(t *time.Time) *UserCreate {
	if t != nil {
		uc.SetUpdatedAt(*t)
	}
	return uc
}

// SetID sets the "id" field.
func (uc *UserCreate) SetID(s string) *UserCreate {
	uc.mutation.SetID(s)
	return uc
}

// SetNillableID sets the "id" field if the given value is not nil.
func (uc *UserCreate) SetNillableID(s *string) *UserCreate {
	if s != nil {
		uc.SetID(*s)
	}
	return uc
}

// AddFollowerIDs adds the "followers" edge to the User entity by IDs.
func (uc *UserCreate) AddFollowerIDs(ids ...string) *UserCreate {
	uc.mutation.AddFollowerIDs(ids...)
	return uc
}

// AddFollowers adds the "followers" edges to the User entity.
func (uc *UserCreate) AddFollowers(u ...*User) *UserCreate {
	ids := make([]string, len(u))
	for i := range u {
		ids[i] = u[i].ID
	}
	return uc.AddFollowerIDs(ids...)
}

// AddFollowingIDs adds the "followings" edge to the User entity by IDs.
func (uc *UserCreate) AddFollowingIDs(ids ...string) *UserCreate {
	uc.mutation.AddFollowingIDs(ids...)
	return uc
}

// AddFollowings adds the "followings" edges to the User entity.
func (uc *UserCreate) AddFollowings(u ...*User) *UserCreate {
	ids := make([]string, len(u))
	for i := range u {
		ids[i] = u[i].ID
	}
	return uc.AddFollowingIDs(ids...)
}

// SetRoomID sets the "room" edge to the Room entity by ID.
func (uc *UserCreate) SetRoomID(id string) *UserCreate {
	uc.mutation.SetRoomID(id)
	return uc
}

// SetNillableRoomID sets the "room" edge to the Room entity by ID if the given value is not nil.
func (uc *UserCreate) SetNillableRoomID(id *string) *UserCreate {
	if id != nil {
		uc = uc.SetRoomID(*id)
	}
	return uc
}

// SetRoom sets the "room" edge to the Room entity.
func (uc *UserCreate) SetRoom(r *Room) *UserCreate {
	return uc.SetRoomID(r.ID)
}

// SetHostID sets the "host" edge to the Room entity by ID.
func (uc *UserCreate) SetHostID(id string) *UserCreate {
	uc.mutation.SetHostID(id)
	return uc
}

// SetNillableHostID sets the "host" edge to the Room entity by ID if the given value is not nil.
func (uc *UserCreate) SetNillableHostID(id *string) *UserCreate {
	if id != nil {
		uc = uc.SetHostID(*id)
	}
	return uc
}

// SetHost sets the "host" edge to the Room entity.
func (uc *UserCreate) SetHost(r *Room) *UserCreate {
	return uc.SetHostID(r.ID)
}

// AddManageIDs adds the "manage" edge to the Room entity by IDs.
func (uc *UserCreate) AddManageIDs(ids ...string) *UserCreate {
	uc.mutation.AddManageIDs(ids...)
	return uc
}

// AddManage adds the "manage" edges to the Room entity.
func (uc *UserCreate) AddManage(r ...*Room) *UserCreate {
	ids := make([]string, len(r))
	for i := range r {
		ids[i] = r[i].ID
	}
	return uc.AddManageIDs(ids...)
}

// AddSendNotificationIDs adds the "sendNotifications" edge to the Notification entity by IDs.
func (uc *UserCreate) AddSendNotificationIDs(ids ...string) *UserCreate {
	uc.mutation.AddSendNotificationIDs(ids...)
	return uc
}

// AddSendNotifications adds the "sendNotifications" edges to the Notification entity.
func (uc *UserCreate) AddSendNotifications(n ...*Notification) *UserCreate {
	ids := make([]string, len(n))
	for i := range n {
		ids[i] = n[i].ID
	}
	return uc.AddSendNotificationIDs(ids...)
}

// AddReceivedNotificationIDs adds the "receivedNotifications" edge to the Notification entity by IDs.
func (uc *UserCreate) AddReceivedNotificationIDs(ids ...string) *UserCreate {
	uc.mutation.AddReceivedNotificationIDs(ids...)
	return uc
}

// AddReceivedNotifications adds the "receivedNotifications" edges to the Notification entity.
func (uc *UserCreate) AddReceivedNotifications(n ...*Notification) *UserCreate {
	ids := make([]string, len(n))
	for i := range n {
		ids[i] = n[i].ID
	}
	return uc.AddReceivedNotificationIDs(ids...)
}

// AddBookRoomIDs adds the "bookRooms" edge to the Reservation entity by IDs.
func (uc *UserCreate) AddBookRoomIDs(ids ...string) *UserCreate {
	uc.mutation.AddBookRoomIDs(ids...)
	return uc
}

// AddBookRooms adds the "bookRooms" edges to the Reservation entity.
func (uc *UserCreate) AddBookRooms(r ...*Reservation) *UserCreate {
	ids := make([]string, len(r))
	for i := range r {
		ids[i] = r[i].ID
	}
	return uc.AddBookRoomIDs(ids...)
}

// Mutation returns the UserMutation object of the builder.
func (uc *UserCreate) Mutation() *UserMutation {
	return uc.mutation
}

// Save creates the User in the database.
func (uc *UserCreate) Save(ctx context.Context) (*User, error) {
	var (
		err  error
		node *User
	)
	uc.defaults()
	if len(uc.hooks) == 0 {
		if err = uc.check(); err != nil {
			return nil, err
		}
		node, err = uc.sqlSave(ctx)
	} else {
		var mut Mutator = MutateFunc(func(ctx context.Context, m Mutation) (Value, error) {
			mutation, ok := m.(*UserMutation)
			if !ok {
				return nil, fmt.Errorf("unexpected mutation type %T", m)
			}
			if err = uc.check(); err != nil {
				return nil, err
			}
			uc.mutation = mutation
			node, err = uc.sqlSave(ctx)
			mutation.done = true
			return node, err
		})
		for i := len(uc.hooks) - 1; i >= 0; i-- {
			mut = uc.hooks[i](mut)
		}
		if _, err := mut.Mutate(ctx, uc.mutation); err != nil {
			return nil, err
		}
	}
	return node, err
}

// SaveX calls Save and panics if Save returns an error.
func (uc *UserCreate) SaveX(ctx context.Context) *User {
	v, err := uc.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}

// defaults sets the default values of the builder before save.
func (uc *UserCreate) defaults() {
	if _, ok := uc.mutation.Description(); !ok {
		v := user.DefaultDescription
		uc.mutation.SetDescription(v)
	}
	if _, ok := uc.mutation.Speaking(); !ok {
		v := user.DefaultSpeaking
		uc.mutation.SetSpeaking(v)
	}
	if _, ok := uc.mutation.Unread(); !ok {
		v := user.DefaultUnread
		uc.mutation.SetUnread(v)
	}
	if _, ok := uc.mutation.CreatedAt(); !ok {
		v := user.DefaultCreatedAt()
		uc.mutation.SetCreatedAt(v)
	}
	if _, ok := uc.mutation.UpdatedAt(); !ok {
		v := user.DefaultUpdatedAt()
		uc.mutation.SetUpdatedAt(v)
	}
	if _, ok := uc.mutation.ID(); !ok {
		v := user.DefaultID()
		uc.mutation.SetID(v)
	}
}

// check runs all checks and user-defined validators on the builder.
func (uc *UserCreate) check() error {
	if _, ok := uc.mutation.Name(); !ok {
		return &ValidationError{Name: "name", err: errors.New("ent: missing required field \"name\"")}
	}
	if v, ok := uc.mutation.Name(); ok {
		if err := user.NameValidator(v); err != nil {
			return &ValidationError{Name: "name", err: fmt.Errorf("ent: validator failed for field \"name\": %w", err)}
		}
	}
	if _, ok := uc.mutation.Password(); !ok {
		return &ValidationError{Name: "password", err: errors.New("ent: missing required field \"password\"")}
	}
	if v, ok := uc.mutation.Password(); ok {
		if err := user.PasswordValidator(v); err != nil {
			return &ValidationError{Name: "password", err: fmt.Errorf("ent: validator failed for field \"password\": %w", err)}
		}
	}
	if v, ok := uc.mutation.Description(); ok {
		if err := user.DescriptionValidator(v); err != nil {
			return &ValidationError{Name: "description", err: fmt.Errorf("ent: validator failed for field \"description\": %w", err)}
		}
	}
	if _, ok := uc.mutation.Speaking(); !ok {
		return &ValidationError{Name: "speaking", err: errors.New("ent: missing required field \"speaking\"")}
	}
	if _, ok := uc.mutation.Unread(); !ok {
		return &ValidationError{Name: "unread", err: errors.New("ent: missing required field \"unread\"")}
	}
	if _, ok := uc.mutation.CreatedAt(); !ok {
		return &ValidationError{Name: "created_at", err: errors.New("ent: missing required field \"created_at\"")}
	}
	if _, ok := uc.mutation.UpdatedAt(); !ok {
		return &ValidationError{Name: "updated_at", err: errors.New("ent: missing required field \"updated_at\"")}
	}
	if v, ok := uc.mutation.ID(); ok {
		if err := user.IDValidator(v); err != nil {
			return &ValidationError{Name: "id", err: fmt.Errorf("ent: validator failed for field \"id\": %w", err)}
		}
	}
	return nil
}

func (uc *UserCreate) sqlSave(ctx context.Context) (*User, error) {
	_node, _spec := uc.createSpec()
	if err := sqlgraph.CreateNode(ctx, uc.driver, _spec); err != nil {
		if cerr, ok := isSQLConstraintError(err); ok {
			err = cerr
		}
		return nil, err
	}
	return _node, nil
}

func (uc *UserCreate) createSpec() (*User, *sqlgraph.CreateSpec) {
	var (
		_node = &User{config: uc.config}
		_spec = &sqlgraph.CreateSpec{
			Table: user.Table,
			ID: &sqlgraph.FieldSpec{
				Type:   field.TypeString,
				Column: user.FieldID,
			},
		}
	)
	if id, ok := uc.mutation.ID(); ok {
		_node.ID = id
		_spec.ID.Value = id
	}
	if value, ok := uc.mutation.Name(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeString,
			Value:  value,
			Column: user.FieldName,
		})
		_node.Name = value
	}
	if value, ok := uc.mutation.Password(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeString,
			Value:  value,
			Column: user.FieldPassword,
		})
		_node.Password = value
	}
	if value, ok := uc.mutation.Description(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeString,
			Value:  value,
			Column: user.FieldDescription,
		})
		_node.Description = value
	}
	if value, ok := uc.mutation.Speaking(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeBool,
			Value:  value,
			Column: user.FieldSpeaking,
		})
		_node.Speaking = value
	}
	if value, ok := uc.mutation.Unread(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeBool,
			Value:  value,
			Column: user.FieldUnread,
		})
		_node.Unread = value
	}
	if value, ok := uc.mutation.CreatedAt(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeTime,
			Value:  value,
			Column: user.FieldCreatedAt,
		})
		_node.CreatedAt = value
	}
	if value, ok := uc.mutation.UpdatedAt(); ok {
		_spec.Fields = append(_spec.Fields, &sqlgraph.FieldSpec{
			Type:   field.TypeTime,
			Value:  value,
			Column: user.FieldUpdatedAt,
		})
		_node.UpdatedAt = value
	}
	if nodes := uc.mutation.FollowersIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.M2M,
			Inverse: true,
			Table:   user.FollowersTable,
			Columns: user.FollowersPrimaryKey,
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: user.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.FollowingsIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.M2M,
			Inverse: false,
			Table:   user.FollowingsTable,
			Columns: user.FollowingsPrimaryKey,
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: user.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.RoomIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.M2O,
			Inverse: true,
			Table:   user.RoomTable,
			Columns: []string{user.RoomColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: room.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_node.room_spectators = &nodes[0]
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.HostIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.M2O,
			Inverse: true,
			Table:   user.HostTable,
			Columns: []string{user.HostColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: room.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_node.room_speakers = &nodes[0]
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.ManageIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.O2M,
			Inverse: true,
			Table:   user.ManageTable,
			Columns: []string{user.ManageColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: room.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.SendNotificationsIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.O2M,
			Inverse: false,
			Table:   user.SendNotificationsTable,
			Columns: []string{user.SendNotificationsColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: notification.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.ReceivedNotificationsIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.O2M,
			Inverse: false,
			Table:   user.ReceivedNotificationsTable,
			Columns: []string{user.ReceivedNotificationsColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: notification.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	if nodes := uc.mutation.BookRoomsIDs(); len(nodes) > 0 {
		edge := &sqlgraph.EdgeSpec{
			Rel:     sqlgraph.O2M,
			Inverse: false,
			Table:   user.BookRoomsTable,
			Columns: []string{user.BookRoomsColumn},
			Bidi:    false,
			Target: &sqlgraph.EdgeTarget{
				IDSpec: &sqlgraph.FieldSpec{
					Type:   field.TypeString,
					Column: reservation.FieldID,
				},
			},
		}
		for _, k := range nodes {
			edge.Target.Nodes = append(edge.Target.Nodes, k)
		}
		_spec.Edges = append(_spec.Edges, edge)
	}
	return _node, _spec
}

// UserCreateBulk is the builder for creating many User entities in bulk.
type UserCreateBulk struct {
	config
	builders []*UserCreate
}

// Save creates the User entities in the database.
func (ucb *UserCreateBulk) Save(ctx context.Context) ([]*User, error) {
	specs := make([]*sqlgraph.CreateSpec, len(ucb.builders))
	nodes := make([]*User, len(ucb.builders))
	mutators := make([]Mutator, len(ucb.builders))
	for i := range ucb.builders {
		func(i int, root context.Context) {
			builder := ucb.builders[i]
			builder.defaults()
			var mut Mutator = MutateFunc(func(ctx context.Context, m Mutation) (Value, error) {
				mutation, ok := m.(*UserMutation)
				if !ok {
					return nil, fmt.Errorf("unexpected mutation type %T", m)
				}
				if err := builder.check(); err != nil {
					return nil, err
				}
				builder.mutation = mutation
				nodes[i], specs[i] = builder.createSpec()
				var err error
				if i < len(mutators)-1 {
					_, err = mutators[i+1].Mutate(root, ucb.builders[i+1].mutation)
				} else {
					// Invoke the actual operation on the latest mutation in the chain.
					if err = sqlgraph.BatchCreate(ctx, ucb.driver, &sqlgraph.BatchCreateSpec{Nodes: specs}); err != nil {
						if cerr, ok := isSQLConstraintError(err); ok {
							err = cerr
						}
					}
				}
				mutation.done = true
				if err != nil {
					return nil, err
				}
				return nodes[i], nil
			})
			for i := len(builder.hooks) - 1; i >= 0; i-- {
				mut = builder.hooks[i](mut)
			}
			mutators[i] = mut
		}(i, ctx)
	}
	if len(mutators) > 0 {
		if _, err := mutators[0].Mutate(ctx, ucb.builders[0].mutation); err != nil {
			return nil, err
		}
	}
	return nodes, nil
}

// SaveX is like Save, but panics if an error occurs.
func (ucb *UserCreateBulk) SaveX(ctx context.Context) []*User {
	v, err := ucb.Save(ctx)
	if err != nil {
		panic(err)
	}
	return v
}
