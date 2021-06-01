import EmberRouter from '@ember/routing/router';
import config from 'bla-bla-web/config/environment';

export default class Router extends EmberRouter {
  location = config.locationType;
  rootURL = config.rootURL;
}

Router.map(function () {
  this.route('login');
  this.route('register');

  this.route('authentication', { path: '/' }, function () {
    this.route('admin');
    this.route('home', { path: '/' });
    this.route('room', { path: '/room/:room_id' });
    this.route('create-room');
    this.route('settings');
  });

  this.route('redirect', { path: '/*' });
});
