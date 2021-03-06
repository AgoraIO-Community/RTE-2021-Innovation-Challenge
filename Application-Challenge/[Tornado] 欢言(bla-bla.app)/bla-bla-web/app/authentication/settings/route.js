import Route from '@ember/routing/route';

export default class SettingsRoute extends Route {
  resetController(controller, isExiting) {
    if (isExiting) {
      controller.reset();
    }
  }
}
