import Service from '@ember/service';
import AgoraRTC from 'agora-rtc-sdk-ng';
import { inject as service } from '@ember/service';
import ENV from 'bla-bla-web/config/environment';
import { tracked } from '@glimmer/tracking';
import { assert } from '@ember/debug';
import { USER_ROLE } from 'bla-bla-web/utils/constants';

const LOG_LEVEL = {
  DEBUG: 0,
  INFO: 1,
  WARNING: 2,
  ERROR: 3,
  NONE: 4,
};

export default class CallService extends Service {
  @service authentication;

  /**
   * @property {boolean} muted
   * Is the call muted or not?
   */
  @tracked muted = false;

  /**
   * @property {LocalAudioTrack} - The basic interface for local audio tracks, providing main methods of local audio tracks.
   * @see {@link https://docs.agora.io/en/Voice/API%20Reference/web_ng/interfaces/ilocalaudiotrack.html}
   */
  localAudioTrack;

  /**
   * @property {AgoraRTCClient} - The local client with basic functions for a voice or video call, such as joining a channel, publishing tracks, or subscribing to tracks.
   * @see {@link https://docs.agora.io/en/Voice/API%20Reference/web_ng/interfaces/iagorartcclient.html}
   */
  client;

  /**
   * Returns true if there is a call in progress
   */
  @tracked inProgress = false;

  /**
   * Room ID of the call if we're in one
   */
  @tracked roomId;

  /**
   * The role of the user
   */
  @tracked role = USER_ROLE.GUEST;

  get isHost() {
    return this.role === USER_ROLE.ADMIN || this.role === USER_ROLE.HOST;
  }

  constructor() {
    super(...arguments);
    if (ENV.environment === 'development') {
      AgoraRTC.enableLogUpload();
      AgoraRTC.setLogLevel(LOG_LEVEL.DEBUG);
    } else {
      AgoraRTC.setLogLevel(LOG_LEVEL.NONE);
    }
  }

  /**
   * Join a call
   * @param {string} roomId
   * @param {Object<string : eventName, function: callback>} [events]
   */
  async startCall(roomId, events = {}) {
    // End previous call before starting a new one
    if (this.client) {
      this.end();
    }

    this.client = AgoraRTC.createClient({ mode: 'rtc', codec: 'vp8' });
    this.roomId = roomId;
    const token = await this._fetchToken(roomId);
    await this.client.join(
      ENV.AGORA_ENV.APP_ID,
      roomId,
      token,
      this.authentication.getUserId()
    );

    this.client.enableAudioVolumeIndicator();
    this._addCallEvents();
    this.addCustomEvents(events);

    this.inProgress = true;
  }

  /**
   * Add custom events from outside
   * @param {Map<string eventName, function callback>} events
   */
  addCustomEvents(events = {}) {
    assert(
      'Agora client must be created before custom events are added',
      !!this.client
    );

    for (const [eventName, eventCallback] of Object.entries(events)) {
      this.client.on(eventName, eventCallback);
    }
  }

  async unmute() {
    this.muted = false;
    // Publish audio track
    if (!this.localAudioTrack) {
      this.localAudioTrack = await AgoraRTC.createMicrophoneAudioTrack();
      await this.client.publish([this.localAudioTrack]);
    }
    this.localAudioTrack.setEnabled(true);
  }

  async mute() {
    this.muted = true;
    this.localAudioTrack?.setEnabled(false);
  }

  async end() {
    this.localAudioTrack?.close();
    this.localAudioTrack = undefined;
    await this.client?.leave();
    this.client = undefined;
    this.inProgress = false;
    this.roomId = undefined;
  }

  setUserRole(newRole) {
    this.role = newRole;
  }

  sendMessage(topic, message) {
    const _message = topic + message;
    this.client.sendStreamMessage(_message);
    // Send it locally too, since Agora won't send it back
    this.client.emit(
      'stream-message',
      this.client.uid,
      new TextEncoder().encode(_message)
    );
  }

  async _addCallEvents() {
    // Subscribe to new audio channel when a user joins
    this.client.on('user-published', async (user, mediaType) => {
      await this.client.subscribe(user, mediaType);
      if (mediaType === 'audio') {
        const remoteAudioTrack = user.audioTrack;
        remoteAudioTrack.play();
      }
    });

    // Refetch token when it's about to expire
    this.client.on('token-privilege-will-expire', async () => {
      const token = await this._fetchToken();
      if (!token) {
        return;
      }
      this.client.renewToken(token);
    });
  }

  async _fetchToken() {
    const userId = this.authentication.getUserId();
    try {
      const tokenResponse = await fetch(
        `${ENV.BLA_BLA_API}?channel=${this.roomId}&user=${userId}`,
        {
          mode: 'cors',
        }
      );
      const tokenObj = await tokenResponse.json();
      return tokenObj.token;
    } catch (e) {
      // TODO: Handle errors fetching token
      // this.currentState = this.state.ERROR.API_SERVER;
      return;
    }
  }
}
