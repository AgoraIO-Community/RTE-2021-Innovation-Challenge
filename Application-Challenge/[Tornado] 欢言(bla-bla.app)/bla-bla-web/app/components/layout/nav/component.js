import Component from '@glimmer/component';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { getOwner } from '@ember/application';

export default class LayoutNavComponent extends Component {
  @service authentication;
  @service router;

  get username() {
    return this.authentication.getUsername();
  }



  @action
  logout() {
    const owner = getOwner(this);
    const roomController = owner.lookup('controller:authentication.room');
    roomController.disconnectUser();
    this.authentication.logOut();
    this.router.transitionTo('login');
  }
}
