package io.agora.sdk.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.agora.rtc.Constants;

@IntDef({
        Constants.AUDIO_ROUTE_DEFAULT,
        Constants.AUDIO_ROUTE_HEADSET,
        Constants.AUDIO_ROUTE_EARPIECE,
        Constants.AUDIO_ROUTE_HEADSETNOMIC,
        Constants.AUDIO_ROUTE_SPEAKERPHONE,
        Constants.AUDIO_ROUTE_LOUDSPEAKER,
        Constants.AUDIO_ROUTE_HEADSETBLUETOOTH,
})
@Retention(RetentionPolicy.SOURCE)
public @interface AudioRoute {
}
