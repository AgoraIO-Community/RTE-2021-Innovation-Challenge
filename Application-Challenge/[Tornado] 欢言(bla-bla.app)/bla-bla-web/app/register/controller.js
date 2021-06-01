import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';

const STATE = {
  signUp: 'signUp',
  error: 'error',
  invalidInviteCode: 'invalidInviteCode',
};
export default class RegisterController extends Controller {
  state = STATE;

  @service authentication;
  @service liveQuery;
  @service router;

  @tracked currentState = this.state.signUp;
  @tracked username;
  @tracked password;
  @tracked retypePassword;
  @tracked email;
  @tracked errorMessage;
  @tracked isEnteredSamePassword;
  @tracked inviteCode = '';
  @tracked usernameExceedLimit;
  @tracked selectAvatar = 'big-stinky-pete';

  get isSignUpBtnDisabled() {
    const isSignUpBtnEnabled =
      !!this.username &&
      !!this.password &&
      !!this.retypePassword &&
      !!this.email &&
      !this.usernameExceedLimit &&
      this.isEnteredSamePassword
    return !isSignUpBtnEnabled;
  }

  @action
  async signUp(event, checkInviteCode = false) {
    let inviteCode;
    if (checkInviteCode) {
      try {
        [inviteCode] = await new this.liveQuery.AV.Query('InviteCode')
          .equalTo('inviteCode', this.inviteCode)
          .find();
        if (!inviteCode || inviteCode.get('isVested')) {
          this.currentState = this.state.invalidInviteCode;
          return;
        } else {
          inviteCode.set('isVested', true);
          await inviteCode.save();
        }
      } catch (e) {
        this.currentState = this.state.invalidInviteCode;
        return;
      }
    }

    const result = await this.authentication.signUp(
      this.username,
      this.password,
      this.selectAvatar,
      this.email
    );
    if (result.status === 'succeed') {
      this.router.transitionTo('authentication.home');
    } else if (result.status === 'fail') {
      if (checkInviteCode) {
        // reset inviteCode if register is not successful
        inviteCode.set('isVested', false);
        inviteCode.save();
      }

      this.currentState = this.state.error;
      this.errorMessage = result.message;
    }
  }

  @action
  reset() {
    this.errorMessage = '';
    this.username = '';
    this.password = '';
    this.retypePassword = '';
    this.currentState = this.state.signUp;
  }

  @action
  usernameKeyUp() {
    if (this.username?.length > 8) {
      this.usernameExceedLimit = true;
    } else {
      this.usernameExceedLimit = false;
    }
  }

  @action
  passwordKeyUp() {
    this.isEnteredSamePassword = this.password === this.retypePassword;
  }
}
