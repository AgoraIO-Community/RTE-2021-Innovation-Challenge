import Controller from '@ember/controller';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { LOCALSTORAGE_KEYS } from 'bla-bla-web/utils/constants';

const STATE = {
  setting: 'setting',
  error: 'error',
};
export default class AuthenticationSettingsController extends Controller {
  @service authentication;

  @tracked currentState = this.state.setting;
  @tracked avatar = this.authentication.avatar;
  @tracked email = this.authentication.email;
  @tracked language = this.authentication.language;
  @tracked isNewLangSaved;
  @tracked isSaved;
  @tracked isVerifyEmailSend;

  state = STATE;

  constructor() {
    super(...arguments);
    // After email verification sent, need to fetch it on page load to get the latest data
    this.authentication?.currentUser?.fetch();
  }

  get isSaveBtnDisabled() {
    const isSaveBtnEnabled =
      this.email !== this.authentication.email ||
      this.avatar !== this.authentication.avatar ||
      this.language !== this.authentication.language;

    return !isSaveBtnEnabled;
  }

  reset() {
    this.isNewLangSaved = false;
    this.isSaved = false;
  }

  @action
  chooseAvatar(avatar) {
    this.isSaved = false;
    this.avatar = avatar;
  }

  @action
  verifyEmail() {
    this.authentication.verifyEmail();
    this.isVerifyEmailSend = true;
  }

  @action
  async save() {
    try {
      if (this.avatar !== this.authentication.avatar) {
        await this.authentication.setAvatar(this.avatar);
      }

      if (this.email !== this.authentication.email) {
        await this.authentication.setEmail(this.email);
      }

      if (this.language !== this.authentication.language) {
        localStorage.setItem(LOCALSTORAGE_KEYS.LANGUAGE, this.language);
        await this.authentication.setLanguage(this.language);
        this.isNewLangSaved = true;
      }
    } catch (e) {
      this.currentState = this.state.error;
    }
    this.isSaved = true;
  }

  @action
  async updateLanguage(event) {
    event.preventDefault();
    this.language = event.target.value;
  }
}
