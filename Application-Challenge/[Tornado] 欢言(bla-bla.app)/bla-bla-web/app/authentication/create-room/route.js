import Route from '@ember/routing/route';

export default class CreateRoomRoute extends Route {
  resetController(controller, isExiting) {
    if (isExiting) {
      controller.reset();
    }
  }
}
