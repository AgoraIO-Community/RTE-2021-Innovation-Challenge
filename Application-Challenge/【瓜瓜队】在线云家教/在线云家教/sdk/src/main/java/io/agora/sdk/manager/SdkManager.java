package io.agora.sdk.manager;

import android.content.Context;

import androidx.annotation.Nullable;

import java.util.Map;

import io.agora.base.callback.Callback;

/**
 * Agora SDK manager template
 */
public abstract class SdkManager<Sdk> {
    public static final String TOKEN = "token";
    public static final String CHANNEL_ID = "channelId";
    public static final String USER_ID = "userId";
    public static final String USER_EXTRA = "userExtra";

    private Sdk sdk;

    public final <T extends SdkManager<Sdk>> void init(Context context, String appId, @Nullable Callback<T> configSdk) {
        try {
            if (sdk != null) release();
            sdk = creakSdk(context, appId);
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
        configSdk();
        if (configSdk != null) configSdk.onSuccess((T) this);
    }

    protected abstract Sdk creakSdk(Context context, String appId) throws Exception;

    protected abstract void configSdk();

    public abstract void joinChannel(Map<String, String> data);

    public abstract void leaveChannel();

    protected abstract void destroySdk();

    public final void release() {
        leaveChannel();
        destroySdk();
        sdk = null;
    }

    protected Sdk getSdk() {
        if (sdk == null)
            throw new IllegalStateException(getClass().getSimpleName() + " is not initialized. Please call init() before use!");
        return sdk;
    }
}
