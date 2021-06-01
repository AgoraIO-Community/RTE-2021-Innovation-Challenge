import Route from '@ember/routing/route';
import { inject as service } from '@ember/service';
import { LOCALSTORAGE_KEYS } from 'bla-bla-web/utils/constants';

export default class ApplicationRoute extends Route {
  @service intl;

  beforeModel() {
    this.lang = localStorage.getItem(LOCALSTORAGE_KEYS.LANGUAGE)
    const isLanguageSupported = this.intl.get('locales').includes(this.lang);
    if (isLanguageSupported) {
      this.intl.setLocale([this.lang]);
    }
  }

  setupController(controller, model) {
    super.setupController(controller, model);

    controller.lang = this.lang;
  }

}
