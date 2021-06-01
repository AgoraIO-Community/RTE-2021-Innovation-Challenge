import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import {
  MESSAGE_PREFIXES,
  ROOM_STATE,
  USER_ROLE,
  USER_STATE,
} from 'bla-bla-web/utils/constants';
import {
  AVModelToTrackedObject,
  handleStreamMessage,
} from 'bla-bla-web/utils/data-helpers';
import { TrackedArray, TrackedMap } from 'tracked-built-ins';

export default class AuthenticationRoomController extends Controller {
  state = ROOM_STATE;
  userState = USER_STATE;
  userRole = USER_ROLE;

  @service authentication;
  @service liveQuery;
  @service roomNotice;
  @service router;
  @service call;

  @tracked currentState;
  @tracked isShowPoorNetworkQuality;
  @tracked message;

  @tracked roomUsers = new TrackedMap(); // <string UserId, TrackedObject RoomUser>
  @tracked messages = new TrackedArray(); // List of Object<user: RoomUser, string: message>

  /**
   * Views into roomUsers
   */
  get admins() {
    return Array.from(this.roomUsers.values()).filterBy(
      'role',
      USER_ROLE.ADMIN
    );
  }
  get hosts() {
    // Put admins first, then the rest of the hosts after
    return [
      ...this.admins,
      ...Array.from(this.roomUsers.values()).filterBy('role', USER_ROLE.HOST),
    ];
  }
  get guests() {
    return Array.from(this.roomUsers.values()).filterBy(
      'role',
      USER_ROLE.GUEST
    );
  }
  get me() {
    return this.getRoomUser(this.authentication.getUserId());
  }

  get canHostAGuest() {
    /**
     * Agora recommends limiting the number of users sending streams concurrently to 17 at most. We put an even smaller number.
     * Refer: https://docs.agora.io/en/Voice/faq/capacity
     */
    const HOST_USERS_THRESHOLD = 10;
    return this.hosts.length < HOST_USERS_THRESHOLD && this.isAdmin;
  }

  get isAdmin() {
    return this.me.role === USER_ROLE.ADMIN;
  }

  get isHost() {
    return this.me.role === USER_ROLE.ADMIN || this.me.role === USER_ROLE.HOST;
  }

  get isGuest() {
    return this.me.role === USER_ROLE.GUEST;
  }

  async syncRoomUsers() {
    await this.findRoomUsers();
    await this.createRoomUser();
  }

  async createRoomUser() {
    const userId = this.authentication.getUserId();
    const adminUserId = this.model.adminUser;
    const isAdminUser = adminUserId === userId;
    let roomUser = this.me;

    if (roomUser && !roomUser.isDestroyed) {
      roomUser.role === USER_ROLE.GUEST
        ? roomUser.set('state', USER_STATE.IDLE)
        : roomUser.set('state', USER_STATE.MUTED);
      roomUser.set('avatar', this.authentication.avatar);
      await roomUser.save();
      return;
    }

    if (isAdminUser) {
      roomUser = await this.liveQuery.createRoomUser({
        roomId: this.roomId,
        userId,
        username: this.authentication.getUsername(),
        avatar: this.authentication.avatar,
        role: USER_ROLE.ADMIN,
        state: USER_STATE.MUTED,
        isSelf: true,
      });
      this.call.setUserRole(USER_ROLE.ADMIN);
    } else {
      roomUser = await this.liveQuery.createRoomUser({
        roomId: this.roomId,
        userId,
        username: this.authentication.getUsername(),
        avatar: this.authentication.avatar,
        role: USER_ROLE.GUEST,
        state: USER_STATE.IDLE,
      });
      this.call.setUserRole(USER_ROLE.GUEST);
    }

    // Push ourselves onto the room users
    this.roomUsers.set(userId, AVModelToTrackedObject(roomUser));
    return;
  }

  async joinChat() {
    await Promise.all([this.setUpAgora(), this.setUpLiveQuery()]);
  }

  async setUpAgora() {
    try {
      await this.call.startCall(this.roomId, {
        'user-joined': async ({ uid }) => {
          // check to avoid duplicate push
          if (this.getRoomUser(uid)) {
            return;
          }

          const [roomUser] = await this.findRoomUsers(uid);

          this.roomNotice.push({
            displayTimeMs: 2000,
            type: 'is-primary',
            message: `@${roomUser.get('username')} joined the room`,
          });
        },

        /**
         * In the case of user close the browser tab(instead of click "leave" button), we can use Agora's 'user-left' event
         * to remove this user from the UI.
         */
        'user-left': async ({ uid }) => {
          const [roomUser] = await this.findRoomUsers(uid);

          this.roomNotice.push({
            displayTimeMs: 2000,
            type: '',
            message: `@${roomUser.get('username')} left the room`,
          });

          // remove user from the glimmer tracked array, so that it disappear from the UI
          this.roomUsers.delete(uid);
        },

        'network-quality': (stats) => {
          if (!this.me) {
            return;
          }
          const { downlinkNetworkQuality, uplinkNetworkQuality } = stats;

          if (downlinkNetworkQuality >= 3 || uplinkNetworkQuality >= 3) {
            this.isShowPoorNetworkQuality = true;
          } else {
            this.isShowPoorNetworkQuality = false;
          }
        },

        // User muted
        'user-unpublished': ({ uid }) => {
          const roomUser = this.getRoomUser(uid);
          if (roomUser) {
            roomUser.state = USER_STATE.MUTED;
          }
        },

        // User unmuted
        'user-published': ({ uid }) => {
          const roomUser = this.getRoomUser(uid);
          if (roomUser) {
            roomUser.state = USER_STATE.IDLE;
          }
        },

        'volume-indicator': (volumes) => {
          volumes.forEach((item) => {
            const { level, uid } = item;
            const roomUser = this.getRoomUser(uid);
            if (roomUser && roomUser.state !== USER_STATE.MUTED) {
              roomUser.state = level > 5 ? USER_STATE.SPEAK : USER_STATE.IDLE;
            }
          });
        },
        'stream-message': handleStreamMessage(
          MESSAGE_PREFIXES.MESSAGE,
          (uid, message) => {
            this.messages.unshift({
              user: this.getRoomUser(uid),
              message,
            });
          }
        ),
      });
      this.currentState = this.state.READY;
    } catch (e) {
      this.currentState = this.state.ERROR.AGORA_CONNECT_FAIL;
    }
  }

  async setUpLiveQuery() {
    const roomQuery = new this.liveQuery.AV.Query('Room').equalTo(
      'objectId',
      this.roomId
    );

    this._roomLiveQuery = await roomQuery.subscribe();
    this._roomLiveQuery.on('delete', async () => {
      this.currentState = this.state.CLOSED;
    });

    const roomUserQuery = new this.liveQuery.AV.Query('RoomUser').equalTo(
      'roomId',
      this.roomId
    );
    this._roomUserLiveQuery = await roomUserQuery.subscribe();
    this._roomUserLiveQuery.on('update', async (guest, [updateKey]) => {
      const userId = this.authentication.getUserId();
      const guestId = guest.get('userId');
      const isSelf = userId === guestId;
      let roomUser = this.getRoomUser(guestId);

      if (updateKey === 'role') {
        roomUser.role = guest.get('role');

        if (guest.get('role') === USER_ROLE.HOST) {
          roomUser.state = USER_STATE.MUTED;

          this.roomNotice.push({
            displayTimeMs: 2000,
            type: 'is-success',
            message: `@${guest.get('username')} becomes host`,
          });

          if (isSelf) {
            this.call.setUserRole(USER_ROLE.HOST);
          }
        } else if (guest.get('role') === USER_ROLE.GUEST) {
          roomUser.state = USER_STATE.IDLE;

          this.roomNotice.push({
            displayTimeMs: 2000,
            type: 'is-error',
            message: `@${guest.get('username')} becomes audience`,
          });

          if (isSelf) {
            this.call.setUserRole(USER_ROLE.GUEST);
            this.call.mute();
          }
        }
      }

      if (updateKey === 'state') {
        roomUser.state = guest.get('state');

        switch (guest.get('state')) {
          case USER_STATE.MUTED:
            {
              if (guest.get('userId') !== userId) {
                return;
              }

              this.call.mute();
            }
            break;
          case USER_STATE.RAISE_HAND:
            this.roomNotice.push({
              displayTimeMs: 5000,
              type: 'is-success',
              message: `@${guest.get('username')} is raising hand`,
            });
            break;
          case USER_STATE.SPEAK:
            /**
             * Due to we are pulling from Agora every 2s, set speak on LeanCloud model is expensive. So we decide to
             * set it in 'volume-indicator
             */
            break;
          case USER_STATE.IDLE: {
            if (
              guest.get('role') === USER_ROLE.ADMIN ||
              guest.get('role') === USER_ROLE.HOST
            ) {
              this.roomNotice.push({
                displayTimeMs: 2000,
                type: 'is-primary',
                message: `@${guest.get('username')} is unmuted`,
              });
            }
          }
        }
      }
    });
  }

  getRoomUser(userId) {
    return userId && this.roomUsers.get(userId);
  }

  /**
   * @param {String} userId - (optional) user id. If not exists, return all users in this room
   * @returns
   */
  async findRoomUsers(userId) {
    // If we have the user already, return them and avoid a roundtrip to leancloud
    if (this.getRoomUser(userId)) {
      return [this.getRoomUser(userId)];
    }

    const roomUserQuery = new this.liveQuery.AV.Query('RoomUser');
    roomUserQuery.equalTo('roomId', this.roomId);

    if (userId) {
      roomUserQuery.equalTo('userId', userId);
    }

    // If user run this repo first time, the following class does not exist and Leancould will throw error
    let roomUsers;
    try {
      roomUsers = await roomUserQuery.find();
    } catch (e) {
      roomUsers = [];
    }

    // Update the TrackedMap roomUsers with all of the new records returned
    roomUsers.forEach((roomUser) => {
      // Update roomUsers map
      this.roomUsers.set(
        roomUser.get('userId'),
        AVModelToTrackedObject(roomUser)
      );
    });

    return roomUsers;
  }

  async disconnectUser() {
    if (!this.call.inProgress) {
      return;
    }

    // disconnect call
    await this.call.end();

    // remove roomUser from LeanCloud
    if (this.me) {
      this.me.destroy();
      this.me.isDestroyed = true;
    }

    // unsubscribe live query
    this._roomLiveQuery.unsubscribe();
    this._roomUserLiveQuery.unsubscribe();
  }

  @action
  async toggleHost(userId) {
    this.me.isSaving = true;

    const roomUser = this.getRoomUser(userId);
    if (!roomUser) {
      return;
    }

    const newRole =
      roomUser.role === USER_ROLE.HOST ? USER_ROLE.GUEST : USER_ROLE.HOST;

    roomUser.set('role', newRole);
    if (newRole === USER_ROLE.GUEST) {
      roomUser.set('state', USER_STATE.IDLE);
    } else {
      roomUser.set('state', USER_STATE.MUTED);
    }

    await roomUser.save();
    this.me.isSaving = false;
  }

  @action
  async toggleRaiseHand() {
    this.me.isSaving = true;

    const newState =
      this.me.state === USER_STATE.RAISE_HAND
        ? USER_STATE.IDLE
        : USER_STATE.RAISE_HAND;

    this.me.set('state', newState);
    await this.me.save();

    this.me.isSaving = false;
  }

  @action
  async closeRoom() {
    this.me.isSaving = true;

    await this.disconnectUser();
    // delete other roomUsers in this room
    const roomUsers = await this.findRoomUsers();
    await this.liveQuery.AV.Object.destroyAll(roomUsers);

    // delete room
    const roomQuery = new this.liveQuery.AV.Query('Room');
    const room = await roomQuery.get(this.roomId);
    await room.destroy();

    this.currentState = this.state.CLOSED;
    this.me.isSaving = false;
  }

  @action
  async leaveRoom() {
    this.me.isSaving = true;

    await this.disconnectUser();
    this.roomId = undefined;

    this.me.isSaving = false;
    this.router.transitionTo('authentication.home');
  }

  @action
  async toggleMute() {
    this.me.isSaving = true;

    const shouldSpeak = this.me.state === USER_STATE.MUTED;

    if (shouldSpeak) {
      await this.call.unmute();
    } else {
      await this.call.mute();
    }

    this.me.set('state', shouldSpeak ? USER_STATE.IDLE : USER_STATE.MUTED);
    await this.me.save();

    this.me.isSaving = false;
  }

  @action
  sendMessage(submitEvent) {
    submitEvent.preventDefault();
    this.call.sendMessage(MESSAGE_PREFIXES.MESSAGE, this.message);
    this.message = '';
  }
}
