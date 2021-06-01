import Service from '@ember/service';
import { inject as service } from '@ember/service';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';

export default class AuthenticationService extends Service {
  @service liveQuery;
  @service router;
  @tracked currentUser;

  constructor() {
    super(...arguments);
    this.User = this.liveQuery.AV.User;
    this.currentUser = this.User.current();

    /**
     * Because this.User.current() is cached on the client, it can not prove that the user still exsits. The following
     * code check whether the user still exist. In the case we delete a malicious user account from leancloud, the
     * `catch()` block will logout them.
     */
    this.currentUser?.fetch().catch(() => {
      this.logOut();
      this.router.transitionTo('login');
    });
  }

  async signUp(username, password, selectAvatar, email) {
    const user = new this.User();
    user.setUsername(username);
    user.setPassword(password);
    user.setEmail(email);
    user.set('avatar', selectAvatar);

    try {
      const userObj = await user.signUp();
      this.currentUser = user;
      return { status: 'succeed', userObj };
    } catch (error) {
      return { status: 'fail', message: error.rawMessage };
    }
  }

  async login(username, password) {
    try {
      const user = await this.User.logIn(username, password);
      this.currentUser = user;
      return { status: 'succeed', user };
    } catch (error) {
      return { status: 'fail', message: error.rawMessage };
    }
  }

  async resetPassword(email) {
    try {
      await this.User.requestPasswordReset(email);
      return { status: 'succeed' };
    } catch (error) {
      return { status: 'fail', message: error.rawMessage };
    }
  }

  logOut() {
    this.User.logOut();
  }

  isLogin() {
    return !!this.User.current();
  }

  get avatar() {
    return this.currentUser.get('avatar');
  }

  get email() {
    return this.currentUser.get('email');
  }

  get emailVerified() {
    return this.currentUser.get('emailVerified');
  }

  get language() {
    return this.currentUser.get('language');
  }

  get isAdmin() {
    return this.currentUser.get('role') === 'admin';
  }

  async setAvatar(avatar) {
    this.currentUser.set('avatar', avatar);
    await this.currentUser.save();
  }

  async setEmail(email) {
    this.currentUser.setEmail(email);
    await this.currentUser.save();
  }

  async setLanguage(lang) {
    this.currentUser.set('language', lang);
    await this.currentUser.save();
  }

  getUsername() {
    return this.currentUser.get('username');
  }

  getUserId() {
    return this.currentUser.get('objectId');
  }

  @action
  verifyEmail() {
    this.User.requestEmailVerify(this.email);
  }
}
