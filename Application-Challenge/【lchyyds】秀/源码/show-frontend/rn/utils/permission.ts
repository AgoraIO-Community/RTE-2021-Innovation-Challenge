import { PermissionsAndroid } from 'react-native';

/**
 * @name requestCameraAndAudioPermission
 * @description Function to request permission for Audio and Camera
 */
export async function requestCameraAndAudioPermission() {
  try {
    const granted = await PermissionsAndroid.requestMultiple([
      PermissionsAndroid.PERMISSIONS.CAMERA,
      PermissionsAndroid.PERMISSIONS.RECORD_AUDIO,
    ]);
    if (
      granted['android.permission.RECORD_AUDIO'] ===
      PermissionsAndroid.RESULTS.GRANTED &&
      granted['android.permission.CAMERA'] ===
      PermissionsAndroid.RESULTS.GRANTED
    ) {
      console.log('You can use the cameras & mic');
    }
    else {
      console.log('Permission denied');
    }
  } catch (err) {
    console.warn(err);
  }
}

export async function requestPhoneNumberPermission() {
  try {
    const res = await PermissionsAndroid.request(
      PermissionsAndroid.PERMISSIONS.READ_PHONE_STATE
    );
    if (res === PermissionsAndroid.RESULTS.GRANTED) {
      console.log('You can read phone numbers');
      return '';
    }
    else {
      console.log('Permission denied');
      return Promise.reject('Permission denied');
    }
  } catch (err) {
    console.warn(err);
    return Promise.reject(err);
  }
}
