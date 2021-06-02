import { pollTask, runTask } from 'ember-lifeline';
import Service from '@ember/service';
import { tracked } from '@glimmer/tracking';

const DISPLAY_TIME_IN_MS = 5000;
const IDLE_POLL_CYCLE = 1000;
export default class RoomNoticeService extends Service {
  @tracked notice;

  noticeList = [];

  constructor() {
    super(...arguments);
    this.pollToken = pollTask(this, (next) => {
      if (this.noticeList.length !== 0) {
        this.notice = this.noticeList.pop();
        runTask(this, next, this.notice.displayTimeMs || DISPLAY_TIME_IN_MS);
      } else {
        this.notice = undefined;
        runTask(this, next, IDLE_POLL_CYCLE);
      }
    });
  }

  /**
   * @param {Object} message
   * @param {Number} message.displayTimeMs
   * @param {String} message.type
   * @param {String} message.message
   */
  push(message) {
    this.noticeList.push(message);
  }
}
