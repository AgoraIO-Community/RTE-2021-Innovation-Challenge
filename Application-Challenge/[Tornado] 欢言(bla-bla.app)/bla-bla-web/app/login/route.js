import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';

export default class LoginRoute extends Route {
  @service authentication;
  @service router;

  beforeModel() {
    const isUserAlreadyLogin = this.authentication.isLogin();
    if (isUserAlreadyLogin) {
      this.router.transitionTo('authentication.home');
    }
  }
}
