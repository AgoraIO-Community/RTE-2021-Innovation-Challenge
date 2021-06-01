'use strict';

module.exports = function (environment) {
  let ENV = {
    modulePrefix: 'bla-bla-web',
    environment,
    rootURL: '/',
    locationType: 'auto',
    EmberENV: {
      FEATURES: {
        // Here you can enable experimental features on an ember canary build
        // e.g. EMBER_NATIVE_DECORATOR_SUPPORT: true
      },
      EXTEND_PROTOTYPES: {
        // Prevent Ember Data from overriding Date.parse.
        Date: false,
      },
    },

    APP: {
      // Here you can pass flags/options to your application instance
      // when it is created
    },

    AGORA_ENV: {
      APP_ID: '439598106cfb4649954a3279ec3a5ac6',
    },

    LEANCLOUD_ENV: {
      APP_ID: 'RCViGhUUBpEd3Ln1sMTV52f6-MdYXbMMI',
      APP_KEY: 'ga3CGW8PpBXSOTnpDIDql92E',
    },
  }

  if (environment === 'development') {
    // ENV.APP.LOG_RESOLVER = true;
    // ENV.APP.LOG_ACTIVE_GENERATION = true;
    // ENV.APP.LOG_TRANSITIONS = true;
    // ENV.APP.LOG_TRANSITIONS_INTERNAL = true;
    // ENV.APP.LOG_VIEW_LOOKUPS = true;
    ENV.BLA_BLA_API = 'http://localhost:3000/access-token/';
    ENV['ember-cli-mirage'] = {
      enabled: false,
    };
  }

  if (environment === 'test') {
    // Testem prefers this...
    ENV.locationType = 'none';

    // keep test console output quieter
    ENV.APP.LOG_ACTIVE_GENERATION = false;
    ENV.APP.LOG_VIEW_LOOKUPS = false;

    ENV.APP.rootElement = '#ember-testing';
    ENV.APP.autoboot = false;
  }

  // if (environment === 'production') {}

  return ENV;
};
