import Application from 'bla-bla-web/app';
import config from 'bla-bla-web/config/environment';
import { setApplication } from '@ember/test-helpers';
import { start } from 'ember-qunit';

setApplication(Application.create(config.APP));

start();
