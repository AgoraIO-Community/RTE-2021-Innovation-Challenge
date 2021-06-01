export const ONE_MIN = 60 * 1000;
export const ONE_HOUR = 60 * ONE_MIN;
export const ONE_DAY = 24 * ONE_HOUR;

export const INVITATION_CODE_LENGTH = 10;

export const FEATURE_LIST = {
  SHOW_RELEASE_MODAL: 'showReleaseModal',
};

export const LEAN_CLOUD_ERROR_CODE = {
  CLASS_DOES_NOT_EXIST: 101,
};

export const ROOM_STATE = {
  READY: 'ready',
  LOADED: 'loaded',
  LOADING: 'loading',
  ERROR: {
    API_SERVER: 'API server error',
    LEAN_CLOUD: 'LeanCloud error',
    AGORA_CONNECT_FAIL: 'agora error',
  },
  CLOSED: 'closed',
};

export const USER_ROLE = {
  ADMIN: 'admin',
  HOST: 'host',
  GUEST: 'guest',
};

export const USER_STATE = {
  IDLE: 'idle',
  MUTED: 'muted',
  RAISE_HAND: 'raiseHand',
  SPEAK: 'speak',
};

export const MESSAGE_PREFIXES = {
  MESSAGE: 'MESSAGE:',
  REACTION: 'REACTION:',
};

export const LOCALSTORAGE_KEYS = {
  LANGUAGE: 'mb-language',
};
