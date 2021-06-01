import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
export default class AuthenticationRoomRoute extends Route {
  @service liveQuery;

  async model({ room_id: roomId }) {
    const roomQuery = new this.liveQuery.AV.Query('Room');
    let room;

    const controller = this.controllerFor(this.routeName);

    // If re-entering the same room while maintaining a call
    // we shouldn't rejoin/reconnect to Agora
    this.shouldJoin =
      roomId !== controller.roomId ||
      controller.roomId != controller.call.roomId;

    // Only disconnect the user from an existing call when they enter a new room
    if (this.shouldJoin) {
      await controller.disconnectUser();
      controller.roomUsers.clear();
      controller.messages.clear();

      // Go ahead and start connecting to the new room
      controller.currentState = controller.state.LOADING;
      controller.roomId = roomId;
    }

    try {
      room = await roomQuery.get(roomId);
      controller.isLeanCloudError = false;
    } catch (e) {
      controller.isLeanCloudError = true;
      this.shouldJoin = false;
      return;
    }

    return room.toJSON();
  }

  async setupController(controller) {
    super.setupController(...arguments);
    // Always sync room users when joining just to make sure we're up to date
    await controller.syncRoomUsers();

    // Connect with Agora only if we aren't already in the call
    if (this.shouldJoin) {
      await controller.joinChat();
    }
  }
}
