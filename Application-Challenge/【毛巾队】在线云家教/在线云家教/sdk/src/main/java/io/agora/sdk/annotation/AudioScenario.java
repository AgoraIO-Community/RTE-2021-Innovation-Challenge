package io.agora.sdk.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import io.agora.rtc.Constants;

@IntDef({
        Constants.AUDIO_SCENARIO_DEFAULT,
        Constants.AUDIO_SCENARIO_CHATROOM_ENTERTAINMENT,
        Constants.AUDIO_SCENARIO_EDUCATION,
        Constants.AUDIO_SCENARIO_GAME_STREAMING,
        Constants.AUDIO_SCENARIO_SHOWROOM,
        Constants.AUDIO_SCENARIO_CHATROOM_GAMING,
        Constants.AUDIO_SCENARIO_NUM,
})
@Retention(RetentionPolicy.SOURCE)
public @interface AudioScenario {
}
