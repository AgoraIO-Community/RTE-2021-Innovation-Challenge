package io.agora.base.util;

import android.text.TextUtils;

import androidx.annotation.NonNull;

import java.util.UUID;

import io.agora.base.PreferenceManager;

public class UUIDUtil {
    private static final String KEY_SP = "uuid";

    @NonNull
    public static String getUUID() {
        String uuid = PreferenceManager.get(KEY_SP, "");
        if (TextUtils.isEmpty(uuid)) {
            uuid = UUID.randomUUID().toString();
            PreferenceManager.put(KEY_SP, uuid);
        }
        return uuid;
    }
}
