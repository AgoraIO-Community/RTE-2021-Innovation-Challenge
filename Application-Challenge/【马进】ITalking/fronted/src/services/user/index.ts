import getUserInfo from './getInfo'
import isAuthed from 'services/user/isAuthed'
import speak from 'services/user/speak'
import mute from 'services/user/mute'
import updateUserInfo from 'services/user/update'
import getOwnInfo from 'services/user/getOwnInfo'
import addFollow from 'services/user/addFollow'
import cancelFollow from 'services/user/cancelFollow'
import getGlobalToken from 'services/user/token'
import upgradeSpeaker from 'services/user/upgradeSpeaker'
import updatePassword from 'services/user/updatePassword'
import logout from 'services/user/logout'

const UserApi = {
  GetInfo: getUserInfo,
  GetOwnInfo: getOwnInfo,
  IsAuthed: isAuthed,
  Speak: speak,
  Mute: mute,
  UpdateInfo: updateUserInfo,
  AddFollow: addFollow,
  CancelFollow: cancelFollow,
  GetToken: getGlobalToken,
  UpgradeSpeaker: upgradeSpeaker,
  UpdatePassword: updatePassword,
  Logout: logout
}

export default UserApi
