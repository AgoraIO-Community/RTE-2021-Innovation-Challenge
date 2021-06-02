package io.agora.sdk.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.agora.rtc.Constants;

@IntDef({
        Constants.AUDIO_PROFILE_DEFAULT,
        Constants.AUDIO_PROFILE_SPEECH_STANDARD,
        Constants.AUDIO_PROFILE_MUSIC_STANDARD,
        Constants.AUDIO_PROFILE_MUSIC_STANDARD_STEREO,
        Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY,
        Constants.AUDIO_PROFILE_MUSIC_HIGH_QUALITY_STEREO,
})
@Retention(RetentionPolicy.SOURCE)
public @interface AudioProfile {
}
