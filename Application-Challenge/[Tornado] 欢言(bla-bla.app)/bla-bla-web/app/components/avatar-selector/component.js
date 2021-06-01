import Component from '@glimmer/component';
import { action } from '@ember/object';
import { tracked } from '@glimmer/tracking';

export default class AvatarComponent extends Component {
  @tracked avatar = this.args.currentAvatar;

  avatars = [
    'big-stinky-pete',
    'camel',
    'lineshooter',
    'shine-stein',
    'tight-fisted',
    'yardbird',
    'lora',
    'jonna',
  ];

  @action
  chooseAvatar(avatar) {
    this.avatar = avatar;
    this.args.chooseAvatar(avatar);
  }
}
