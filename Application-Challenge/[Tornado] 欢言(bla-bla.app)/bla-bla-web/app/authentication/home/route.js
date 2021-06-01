import Route from '@ember/routing/route';
import { FEATURE_LIST } from 'bla-bla-web/utils/constants';
import { inject as service } from '@ember/service';
import { ONE_HOUR } from 'bla-bla-web/utils/constants';

export default class AuthenticationHomeRoute extends Route {
  @service genos;
  @service liveQuery;

  async model() {
    const isShowReleaseModal = await this.genos.showFeature(
      FEATURE_LIST.SHOW_RELEASE_MODAL
    );

    const twoHoursAgo = new Date(Date.now() - 2 * ONE_HOUR);
    /**
     * For regular room, get the ones that are created within the past two hours
     * ~Todo: run scheduled tasks using cloudfunction to destroy rooms that created two hours ago~
     * ~Refer: https://docs.leancloud.app/leanengine_cloudfunction_guide-node.html#hash-108242322~
     *
     * Update: we should NOT destroy rooms, instead, we can leverage it for tracking.
     * Here is a [WIP Tracking RFC](https://github.com/taoning2014/bla-bla-web/issues/28)
     */
    const roomQuery = new this.liveQuery.AV.Query('Room').greaterThanOrEqualTo(
      'createdAt',
      twoHoursAgo
    );
    // For scheduled room, get the ones that scheduled time are >= past two hours
    const scheduledRoomQuery = new this.liveQuery.AV.Query(
      'Room'
    ).greaterThanOrEqualTo('scheduledTime', Date.now() - 2 * ONE_HOUR);

    // If user run this repo first time, the following class does not exist and Leancould will throw error
    let rooms;
    try {
      rooms = await this.liveQuery.AV.Query.or(
        roomQuery,
        scheduledRoomQuery
      )
        .addDescending('scheduledTime')
        .addDescending('createdAt')
        .find();
    } catch(e) {
      rooms = [];
    }

    return {
      rooms: rooms.map((room) => room.toJSON()),
      isShowReleaseModal,
    };
  }

  setupController(controller, model) {
    super.setupController(controller, model);

    controller.isShowReleaseModal = model.isShowReleaseModal;
  }
}
