import Route from '@ember/routing/route';

export default class AuthenticationAdminRoute extends Route {
  resetController(controller, isExiting) {
    if (isExiting) {
      controller.reset();
    }
  }
}
