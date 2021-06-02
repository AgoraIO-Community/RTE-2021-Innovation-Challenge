import Service from '@ember/service';
import AV from 'leancloud-storage/live-query';
import ENV from 'bla-bla-web/config/environment';
export default class LiveQueryService extends Service {
  constructor() {
    super(...arguments);
    AV.init({
      appId: ENV.LEANCLOUD_ENV.APP_ID,
      appKey: ENV.LEANCLOUD_ENV.APP_KEY,
      masterKey: ENV.LEANCLOUD_ENV.APP_MASTER_KEY,
    });

    // Turn on debug to output details of every request made by the SDK to the browser console
    if (ENV.environment === 'development') {
      AV.debug.enable();
    }

    this.AV = AV;

    // Record classes
    this.Room = this.AV.Object.extend('Room');
    this.RoomUser = this.AV.Object.extend('RoomUser');
  }

  async createRoomUser({
    roomId,
    userId,
    username,
    avatar,
    role,
    state,
    isSelf,
  } = {}) {
    const roomUser = new this.RoomUser();
    roomUser.set('roomId', roomId);
    roomUser.set('userId', userId);
    roomUser.set('username', username);
    roomUser.set('avatar', avatar);
    roomUser.set('role', role);
    roomUser.set('state', state);
    roomUser.set('isSelf', isSelf);
    const roomUserObj = await roomUser.save();

    return roomUserObj;
  }

  async createRoom({
    adminUser,
    adminUserAvatar,
    adminUsername,
    description,
    scheduledTime,
    title,
  }) {
    const room = new this.Room();
    room.set('adminUser', adminUser);
    room.set('adminUserAvatar', adminUserAvatar);
    room.set('adminUsername', adminUsername);
    room.set('description', description);
    room.set('scheduledTime', scheduledTime);
    room.set('title', title);
    const roomObj = await room.save();

    return roomObj;
  }
}
