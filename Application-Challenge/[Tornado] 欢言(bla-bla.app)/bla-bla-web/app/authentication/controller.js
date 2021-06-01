import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { FEATURE_LIST } from 'bla-bla-web/utils/constants';

export default class AuthenticationController extends Controller {
  @tracked isShowReleaseModal;

  @service call;
  @service genos;
  @service('router') routing;

  get showBottomNav() {
    return (
      this.call.inProgress &&
      this.routing.currentRouteName !== 'authentication.room'
    );
  }

  @action
  async closeFeatureModal() {
    this.isShowReleaseModal = false;
    this.genos.setFeature(FEATURE_LIST.SHOW_RELEASE_MODAL, false);
  }

  @action
  mute() {
    this.call.mute();
  }

  @action
  unmute() {
    this.call.unmute();
  }

  @action
  endCall() {
    this.call.end();
  }
}
