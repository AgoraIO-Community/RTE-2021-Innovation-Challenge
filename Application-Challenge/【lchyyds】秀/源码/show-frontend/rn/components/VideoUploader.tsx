import React, { useRef, useState, } from 'react';
import {
  View,
  TouchableWithoutFeedback,
  ToastAndroid,
  StyleSheet,
  ActivityIndicator,
} from 'react-native';
import { Icon, Text, } from 'react-native-elements';
import { ImageLibraryOptions, ImagePickerResponse, launchImageLibrary } from 'react-native-image-picker';
import Video, { LoadError, OnBufferData } from 'react-native-video';

import { uploadFileService } from '../services/file';

interface VideoUploaderProps {
  userId: string,

  url?: string,
  label?: string,
  onChange?: (url: string) => void,
};

const VideoUploader: React.FC<VideoUploaderProps> = ({ userId, url, label, onChange, }) => {
  const [uploading, setUploading] = useState(false);

  const videoRef = useRef(null);

  const handleVideoOnBuffer = (data: OnBufferData) => {
    console.log('videBuffer', data)
  };

  const handleVideoOnError = (error: LoadError) => {
    console.error('videoError', error);
  };

  const handleUplaod = () => {
    const options: ImageLibraryOptions = {
      mediaType: 'video',
      quality: 1,
    };
    const uploadCallback = (response: ImagePickerResponse) => {
      const {
        didCancel, errorCode, errorMessage,
        uri, type, fileName,
      } = response;
      if (didCancel) { return; }
      if (errorCode) {
        ToastAndroid.show(errorMessage || '未知错误', ToastAndroid.SHORT);
        return;
      }

      const formData = new FormData();
      formData.append('file', {
        uri,
        type: 'multipart/form-data',
        name: fileName,
      });
      setUploading(true);
      uploadFileService({ file: formData, userId: userId })
        .then((res) => {
          if (res.success) {
            onChange?.(res.data);
          }
          else {
            ToastAndroid.show('上传失败' + JSON.stringify(res), ToastAndroid.LONG);
          }
        })
        .catch((err) => {
          ToastAndroid.show('上传失败' + err, ToastAndroid.LONG);
        })
        .finally(() => {
          setUploading(false);
        });

    };
    launchImageLibrary(options, uploadCallback);
  };

  return (
    <TouchableWithoutFeedback onPress={handleUplaod}>
      <View
        style={{
          marginLeft: 10,
          marginRight: 10,
          borderBottomWidth: 1,
          borderColor: 'rgb(237, 237, 237)',
          padding: 10,
          flexDirection: 'row',
          justifyContent: 'space-between',
          alignItems: 'center',
        }}
      >
        <Text style={{ fontSize: 17, color: 'rgb(80, 80, 80)' }}>
          {label || '视频'}
        </Text>
        <View style={{ flexDirection: 'row', alignItems: 'center', height: 50 }}>
          {uploading
            ? <ActivityIndicator size="small" color="rgb(166, 176, 184)" />
            : (
              <>
                {url
                  ? (
                    <View style={styles.videoWrapper}>
                      <Video
                        style={styles.video}
                        source={{ uri: url }}
                        ref={videoRef}
                        onBuffer={handleVideoOnBuffer}
                        onError={handleVideoOnError}
                        resizeMode='cover'
                        muted
                      />
                    </View>
                  )
                  : null}
                <Icon type='antdesign' name='right' size={17} color='rgb(166, 176, 184)'></Icon>
              </>
            )
          }
        </View>
      </View>
    </TouchableWithoutFeedback>
  );
};

const styles = StyleSheet.create({
  videoWrapper: {
    position: 'relative',
    width: 50,
    height: 50,
    borderWidth: 1,
    borderColor: 'rgb(237, 237, 237)',
    borderRadius: 5,
    overflow: 'hidden',
  },
  video: {
    position: 'absolute',
    top: 0,
    left: 0,
    bottom: 0,
    right: 0,
  },
});

export default VideoUploader;