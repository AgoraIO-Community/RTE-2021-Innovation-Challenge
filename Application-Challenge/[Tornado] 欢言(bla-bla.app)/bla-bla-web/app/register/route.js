import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class RegisterRoute extends Route {
  @service authentication;
  @service router;

  beforeModel() {
    const isUserAlreadyLogin = this.authentication.isLogin();
    if (isUserAlreadyLogin) {
      this.router.transitionTo('authentication.home');
    }
  }
}
