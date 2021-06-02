import Controller from '@ember/controller';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { ONE_HOUR, ONE_DAY } from 'bla-bla-web/utils/constants';

const STATE = {
  createRoom: 'createRoom',
  verifyEmail: 'verifyEmail',
};
export default class AuthenticationCreateRoomController extends Controller {
  @service authentication;
  @service liveQuery;

  @tracked currentState = this.authentication.emailVerified
    ? this.state.createRoom
    : this.state.verifyEmail;
  @tracked title;
  @tracked isRoomCreateSucceed;
  @tracked isRoomCreateFail;
  @tracked isScheduledRoom;

  state = STATE;

  scheduledTo = new Date(this.scheduledFrom.getTime() + 7 * ONE_DAY);
  scheduledTime = this.scheduledFrom;

  get isCreateBtnDisabled() {
    return !this.title || this.isRoomCreateSucceed || this.isRoomCreateFail;
  }

  get scheduledFrom() {
    const scheduledFromDate = new Date(Date.now() + 2 * ONE_HOUR);
    scheduledFromDate.setMinutes(0, 0, 0);
    return scheduledFromDate;
  }

  @action
  async create() {
    const userId = this.authentication.getUserId();

    try {
      const roomObj = await this.liveQuery.createRoom({
        adminUser: userId,
        adminUserAvatar: this.authentication.avatar,
        adminUsername: this.authentication.getUsername(),
        description: { description: this.description },
        scheduledTime: this.isScheduledRoom ? this.scheduledTime : undefined,
        title: this.title,
      });
      this.roomId = roomObj.id;
    } catch (e) {
      this.isRoomCreateFail = true;
      return;
    }
    this.isRoomCreateSucceed = true;
  }

  @action
  updateDate([date]) {
    this.scheduledTime = date.getTime();
  }

  @action
  reset() {
    this.currentState = this.state.createRoom;
    this.title = '';
    this.description = '';
    this.isRoomCreateFail = false;
    this.isRoomCreateSucceed = false;
    this.isScheduledRoom = false;
    this.scheduledTime = this.scheduledFrom;
  }
}
