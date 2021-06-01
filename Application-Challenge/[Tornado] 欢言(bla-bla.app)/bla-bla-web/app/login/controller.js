import Controller from '@ember/controller';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';

const STATE = {
  login: 'login',
  error: 'error',
};
export default class LoginController extends Controller {
  state = STATE;

  @service authentication;
  @service router;

  @tracked currentState = this.state.login;
  @tracked email;
  @tracked errorMessage;
  @tracked username;
  @tracked password;
  @tracked isResetPasswordSucceed
  @tracked isShowResetPassword;

  get isLoginBtnDisabled() {
    const isLoginBtnEnabled = !!this.username && !!this.password;
    return !isLoginBtnEnabled;
  }

  get isResetEmailBtnDisabled() {
    return !this.email.length;
  }

  @action
  async resetPassword() {
    const result = await this.authentication.resetPassword(this.email);
    if (result.status === 'succeed') {
      this.isShowResetPassword = false;
      this.isResetPasswordSucceed = true;
    } else {
      this.currentState = this.state.error;
      this.errorMessage = result.message;
    }
  }

  @action
  async login() {
    this.currentState = this.state.login;
    const result = await this.authentication.login(this.username, this.password);
    if (result.status === 'succeed') {
      const previousTransition = this.previousTransition;
      if (previousTransition) {
        this.previousTransition = null;
        previousTransition.retry();
      } else {
        this.router.transitionTo('authentication.home');
      }
    } else if (result.status === 'fail') {
      this.currentState = this.state.error;
      this.errorMessage = result.message;
    }
  }

  @action
  reset() {
    this.errorMessage = '';
    this.email = '';
    this.isShowResetPassword = false;
    this.username = '';
    this.password = '';
    this.currentState = this.state.login
  }
}
