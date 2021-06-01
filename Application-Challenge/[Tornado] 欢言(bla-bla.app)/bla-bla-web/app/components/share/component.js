import { action } from '@ember/object';
import Component from '@glimmer/component';
import { tracked } from '@glimmer/tracking';
import getBaseUrl from 'bla-bla-web/utils/get-base-url';

export default class ShareComponent extends Component {
  @tracked isCopyLinkFail;
  @tracked isCopyLinkSucceed;

  get shareText() {
    return `Bla-bla room is created for you, copy & open the following link in your browser and let's chat! ${getBaseUrl()}/room/${
      this.args.roomId
    }`;
  }

  @action
  async copyLink() {
    const result = await navigator.permissions.query({
      name: 'clipboard-write',
    });
    const { state } = result;

    if (state === 'granted' || state === 'prompt') {
      navigator.clipboard.writeText(this.shareText);
      this.isCopyLinkSucceed = true;
    } else {
      this.isCopyLinkFail = true;
    }
  }
}
