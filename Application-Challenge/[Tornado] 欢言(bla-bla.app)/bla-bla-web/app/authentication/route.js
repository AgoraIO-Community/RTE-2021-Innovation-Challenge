import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { LOCALSTORAGE_KEYS } from 'bla-bla-web/utils/constants';
export default class AuthenticationRoute extends Route {
  @service authentication;
  @service router;

  beforeModel(transition) {
    const isUserAlreadyLogin = this.authentication.isLogin();
    if (!isUserAlreadyLogin) {
      const loginController = this.controllerFor('login');
      loginController.previousTransition = transition;
      this.router.transitionTo('login');
    } else {
      const lang = localStorage.getItem(LOCALSTORAGE_KEYS.LANGUAGE)
      if (lang !== this.authentication.language) {
        localStorage.setItem(LOCALSTORAGE_KEYS.LANGUAGE, this.language);
      }
    }
  }
}
