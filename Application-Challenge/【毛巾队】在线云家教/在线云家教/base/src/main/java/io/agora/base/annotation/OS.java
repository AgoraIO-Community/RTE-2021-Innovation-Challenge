package io.agora.base.annotation;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@IntDef({OS.IOS, OS.ANDROID})
@Retention(RetentionPolicy.SOURCE)
public @interface OS {
    /**
     * iOS
     */
    int IOS = 1;
    /**
     * Android
     */
    int ANDROID = 2;
}
