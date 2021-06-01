import Route from '@ember/routing/route';

export default class RedirectRoute extends Route {
  beforeModel() {
    this.replaceWith('login');
  }
}
