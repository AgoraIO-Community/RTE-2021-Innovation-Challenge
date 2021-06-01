import Component from '@glimmer/component';
import { tracked } from '@glimmer/tracking';
import { action } from '@ember/object';
import { inject as service } from '@ember/service';
import { MESSAGE_PREFIXES } from 'bla-bla-web/utils/constants';
import { handleStreamMessage } from 'bla-bla-web/utils/data-helpers';

function getRandomAnimation() {
  const animationList = [
    'bounce',
    'flash',
    'pulse',
    'rubberBand',
    'shakeX',
    'shakeY',
    'headShake',
    'swing',
    'tada',
    'wobble',
    'jello',
    'heartBeat',
  ];

  const index = Math.floor(Math.random() * animationList.length);
  return `animate__${animationList[index]}`;
}
export default class ReactionBoxComponent extends Component {
  @tracked openReactionsMenu = false;

  @service call;

  constructor() {
    super(...arguments);
    this.call.addCustomEvents({
      'stream-message': handleStreamMessage(
        MESSAGE_PREFIXES.REACTION,
        (userId, message) => {
          this.displayNewReaction(message);
        }
      ),
    });
  }

  displayNewReaction(reaction) {
    this.openReactionsMenu = false;

    const reactionContainerEl = document.getElementById(
      'mb-reaction-container'
    );
    const iEl = document.createElement('i');
    iEl.classList.add(
      reaction,
      'animate__animated',
      'animate__repeat-2',
      getRandomAnimation()
    );
    const roleAttr = document.createAttribute('role');
    roleAttr.value = 'button';
    iEl.setAttributeNode(roleAttr);

    reactionContainerEl.appendChild(iEl);

    iEl.addEventListener(
      'animationend',
      () => reactionContainerEl.removeChild(iEl),
      { once: true }
    );
  }

  @action
  chooseReaction(reaction) {
    this.call.sendMessage(MESSAGE_PREFIXES.REACTION, reaction);
  }
}
