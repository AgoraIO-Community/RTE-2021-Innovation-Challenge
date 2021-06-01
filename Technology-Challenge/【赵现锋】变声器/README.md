用于方便用户对声音进行设置

demo需要自行设置APPID和TOKEN

文件存放路径为src/main/java/io/agora/api/example/utils/VoiceUtils.java

用法举例

```
import io.agora.api.example.utils.VoiceUtils;
```

```
int effectOption1 = VoiceUtils.getPitch1Value(pitch1.getSelectedItem().toString());
int effectOption2 = VoiceUtils.getPitch2Value(pitch2.getSelectedItem().toString());
```

```
ngine.setAudioEffectPreset(VoiceUtils.getAudioEffectPreset(item));
```

接口说明

```
package io.agora.api.example.utils;

import static io.agora.rtc.Constants.AUDIO_EFFECT_OFF;
import static io.agora.rtc.Constants.CHAT_BEAUTIFIER_FRESH;
import static io.agora.rtc.Constants.CHAT_BEAUTIFIER_MAGNETIC;
import static io.agora.rtc.Constants.CHAT_BEAUTIFIER_VITALITY;
import static io.agora.rtc.Constants.PITCH_CORRECTION;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_3D_VOICE;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_ETHEREAL;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_KTV;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_PHONOGRAPH;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_SPACIAL;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_STUDIO;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_VIRTUAL_STEREO;
import static io.agora.rtc.Constants.ROOM_ACOUSTICS_VOCAL_CONCERT;
import static io.agora.rtc.Constants.STYLE_TRANSFORMATION_POPULAR;
import static io.agora.rtc.Constants.STYLE_TRANSFORMATION_RNB;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_CLEAR;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_DEEP;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_FALSETTO;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_FULL;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_MELLOW;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_RESOUNDING;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_RINGING;
import static io.agora.rtc.Constants.TIMBRE_TRANSFORMATION_VIGOROUS;
import static io.agora.rtc.Constants.VOICE_BEAUTIFIER_OFF;
import static io.agora.rtc.Constants.VOICE_CHANGER_BASS;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_BOY;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_GIRL;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_HULK;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_OLDMAN;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_PIGKING;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_SISTER;
import static io.agora.rtc.Constants.VOICE_CHANGER_EFFECT_UNCLE;
import static io.agora.rtc.Constants.VOICE_CHANGER_NEUTRAL;
import static io.agora.rtc.Constants.VOICE_CHANGER_SOLID;
import static io.agora.rtc.Constants.VOICE_CHANGER_SWEET;
import static io.agora.rtc.Constants.VOICE_CONVERSION_OFF;

public class VoiceUtils {
    public static int getPitch1Value(String str) {
        switch (str){
            case "自然小调":
                return 2;
            case "和声小调":
                return 3;
            default:
                return 1;
        }
    }

    public static int getPitch2Value(String str) {
        switch (str){
            case "A":
                return 1;
            case "A# ":
                return 2;
            case "B":
                return 3;
            case "C#":
                return 5;
            case "D ":
                return 6;
            case "D# ":
                return 7;
            case "E ":
                return 8;
            case "F ":
                return 9;
            case "F# ":
                return 10;
            case "G ":
                return 11;
            case "G# ":
                return 12;
            default:
                return 4;
        }
    }
    public static int getVoiceConversionValue(String label) {
        switch (label) {
            case "VOICE_CHANGER_NEUTRAL":
                return VOICE_CHANGER_NEUTRAL;
            case "VOICE_CHANGER_SWEET":
                return VOICE_CHANGER_SWEET;
            case "VOICE_CHANGER_SOLID":
                return VOICE_CHANGER_SOLID;
            case "VOICE_CHANGER_BASS":
                return VOICE_CHANGER_BASS;
            case "VOICE_CONVERSION_OFF":
            default:
                return VOICE_CONVERSION_OFF;
        }
    }

    public static int getVoiceBeautifierValue(String label) {
        int value;
        switch (label) {
            case "CHAT_BEAUTIFIER_MAGNETIC":
                value = CHAT_BEAUTIFIER_MAGNETIC;
                break;
            case "CHAT_BEAUTIFIER_FRESH":
                value = CHAT_BEAUTIFIER_FRESH;
                break;
            case "CHAT_BEAUTIFIER_VITALITY":
                value = CHAT_BEAUTIFIER_VITALITY;
                break;
            case "TIMBRE_TRANSFORMATION_VIGOROUS":
                value = TIMBRE_TRANSFORMATION_VIGOROUS;
                break;
            case "TIMBRE_TRANSFORMATION_DEEP":
                value = TIMBRE_TRANSFORMATION_DEEP;
                break;
            case "TIMBRE_TRANSFORMATION_MELLOW":
                value = TIMBRE_TRANSFORMATION_MELLOW;
                break;
            case "TIMBRE_TRANSFORMATION_FALSETTO":
                value = TIMBRE_TRANSFORMATION_FALSETTO;
                break;
            case "TIMBRE_TRANSFORMATION_FULL":
                value = TIMBRE_TRANSFORMATION_FULL;
                break;
            case "TIMBRE_TRANSFORMATION_CLEAR":
                value = TIMBRE_TRANSFORMATION_CLEAR;
                break;
            case "TIMBRE_TRANSFORMATION_RESOUNDING":
                value = TIMBRE_TRANSFORMATION_RESOUNDING;
                break;
            case "TIMBRE_TRANSFORMATION_RINGING":
                value = TIMBRE_TRANSFORMATION_RINGING;
                break;
            default:
                value = VOICE_BEAUTIFIER_OFF;
        }
        return value;
    }


    public static int getAudioEffectPreset(String label) {
        int value;
        switch (label) {
            case "ROOM_ACOUSTICS_KTV":
                value = ROOM_ACOUSTICS_KTV;
                break;
            case "ROOM_ACOUSTICS_VOCAL_CONCERT":
                value = ROOM_ACOUSTICS_VOCAL_CONCERT;
                break;
            case "ROOM_ACOUSTICS_STUDIO":
                value = ROOM_ACOUSTICS_STUDIO;
                break;
            case "ROOM_ACOUSTICS_PHONOGRAPH":
                value = ROOM_ACOUSTICS_PHONOGRAPH;
                break;
            case "ROOM_ACOUSTICS_VIRTUAL_STEREO":
                value = ROOM_ACOUSTICS_VIRTUAL_STEREO;
                break;
            case "ROOM_ACOUSTICS_SPACIAL":
                value = ROOM_ACOUSTICS_SPACIAL;
                break;
            case "ROOM_ACOUSTICS_ETHEREAL":
                value = ROOM_ACOUSTICS_ETHEREAL;
                break;
            case "ROOM_ACOUSTICS_3D_VOICE":
                value = ROOM_ACOUSTICS_3D_VOICE;
                break;
            case "VOICE_CHANGER_EFFECT_UNCLE":
                value = VOICE_CHANGER_EFFECT_UNCLE;
                break;
            case "VOICE_CHANGER_EFFECT_OLDMAN":
                value = VOICE_CHANGER_EFFECT_OLDMAN;
                break;
            case "VOICE_CHANGER_EFFECT_BOY":
                value = VOICE_CHANGER_EFFECT_BOY;
                break;
            case "VOICE_CHANGER_EFFECT_SISTER":
                value = VOICE_CHANGER_EFFECT_SISTER;
                break;
            case "VOICE_CHANGER_EFFECT_GIRL":
                value = VOICE_CHANGER_EFFECT_GIRL;
                break;
            case "VOICE_CHANGER_EFFECT_PIGKING":
                value = VOICE_CHANGER_EFFECT_PIGKING;
                break;
            case "VOICE_CHANGER_EFFECT_HULK":
                value = VOICE_CHANGER_EFFECT_HULK;
                break;
            case "STYLE_TRANSFORMATION_RNB":
                value = STYLE_TRANSFORMATION_RNB;
                break;
            case "STYLE_TRANSFORMATION_POPULAR":
                value = STYLE_TRANSFORMATION_POPULAR;
                break;
            case "PITCH_CORRECTION":
                value = PITCH_CORRECTION;
                break;
            default:
                value = AUDIO_EFFECT_OFF;
        }
        return value;
    }
}
```