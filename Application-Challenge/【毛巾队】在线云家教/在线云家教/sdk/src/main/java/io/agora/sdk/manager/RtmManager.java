package io.agora.sdk.manager;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import io.agora.base.callback.Callback;
import io.agora.base.callback.ThrowableCallback;
import io.agora.log.LogManager;
import io.agora.rtm.ChannelAttributeOptions;
import io.agora.rtm.ErrorInfo;
import io.agora.rtm.ResultCallback;
import io.agora.rtm.RtmChannel;
import io.agora.rtm.RtmChannelAttribute;
import io.agora.rtm.RtmChannelListener;
import io.agora.rtm.RtmChannelMember;
import io.agora.rtm.RtmClient;
import io.agora.rtm.RtmClientListener;
import io.agora.rtm.RtmFileMessage;
import io.agora.rtm.RtmImageMessage;
import io.agora.rtm.RtmMediaOperationProgress;
import io.agora.rtm.RtmMessage;
import io.agora.rtm.RtmStatusCode;
import io.agora.sdk.annotation.ConnectionState;
import io.agora.sdk.listener.RtmEventListener;

public final class RtmManager extends SdkManager<RtmClient> implements RtmClientListener, RtmChannelListener {
    private final LogManager log = new LogManager(this.getClass().getSimpleName());

    private List<RtmEventListener> listeners;
    private RtmChannel rtmChannel;
    @ConnectionState
    private int connectionState;
    private int loginUid;

    private static RtmManager instance;

    private RtmManager() {
        listeners = new ArrayList<>();
    }

    public static RtmManager instance() {
        if (instance == null) {
            synchronized (RtmManager.class) {
                if (instance == null)
                    instance = new RtmManager();
            }
        }
        return instance;
    }

    public void reset() {
        connectionState = 0;
        loginUid = 0;
    }

    @Override
    protected RtmClient creakSdk(Context context, String appId) throws Exception {
        return RtmClient.createInstance(context, appId, this);
    }

    @Override
    protected void configSdk() {
        getSdk().setLogFile(new File(LogManager.getPath(), "agorartm.log").getAbsolutePath());
    }

    @Override
    public void joinChannel(Map<String, String> data) {
        String channelId = data.get(CHANNEL_ID);
        rtmChannel = getSdk().createChannel(channelId, this);
        rtmChannel.join(new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                log.d("join success %s", channelId);
                for (RtmEventListener listener : listeners) {
                    listener.onJoinChannelSuccess(channelId);
                }
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
            }
        });
    }

    public void login(String rtmToken, int userId, @Nullable Callback<Void> callback) {
        if (isConnected()) {
            if (loginUid == userId && connectionState != RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED) {
                if (callback != null) {
                    callback.onSuccess(null);
                }
                return;
            }
            getSdk().logout(new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    login(rtmToken, userId, new RtmCallback(callback, () -> loginUid = userId));
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                    login(rtmToken, userId, new RtmCallback(callback, () -> loginUid = userId));
                }
            });
        } else {
            login(rtmToken, userId, new RtmCallback(callback, () -> loginUid = userId));
        }
    }

    private void login(String rtmToken, int userId, ResultCallback<Void> callback) {
        getSdk().login(rtmToken, String.valueOf(userId), callback);
    }

    public boolean isConnected() {
        return connectionState == RtmStatusCode.ConnectionState.CONNECTION_STATE_CONNECTED
                || connectionState == RtmStatusCode.ConnectionState.CONNECTION_STATE_ABORTED;
    }

    @Override
    public void leaveChannel() {
        if (rtmChannel != null) {
            rtmChannel.leave(null);
            rtmChannel.release();
            rtmChannel = null;
        }
    }

    @Override
    protected void destroySdk() {
        getSdk().release();
    }

    public void registerListener(RtmEventListener listener) {
        listeners.add(listener);
    }

    public void unregisterListener(RtmEventListener listener) {
        listeners.remove(listener);
    }

    public void queryPeersOnlineStatus(Set<String> set, @NonNull Callback<Map<String, Boolean>> callback) {
        getSdk().queryPeersOnlineStatus(set, new ResultCallback<Map<String, Boolean>>() {
            @Override
            public void onSuccess(Map<String, Boolean> stringBooleanMap) {
                callback.onSuccess(stringBooleanMap);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                if (callback instanceof ThrowableCallback) {
                    ((ThrowableCallback<Map<String, Boolean>>) callback).onFailure(new Throwable(errorInfo.toString()));
                }
            }
        });
    }

    public void getChannelAttributes(String channelId, @NonNull Callback<List<RtmChannelAttribute>> callback) {
        getSdk().getChannelAttributes(channelId, new ResultCallback<List<RtmChannelAttribute>>() {
            @Override
            public void onSuccess(List<RtmChannelAttribute> rtmChannelAttributes) {
                callback.onSuccess(rtmChannelAttributes);
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
                if (callback instanceof ThrowableCallback) {
                    ((ThrowableCallback<List<RtmChannelAttribute>>) callback).onFailure(new Throwable(errorInfo.toString()));
                }
            }
        });
    }

    public void addOrUpdateChannelAttributes(String channelId, List<RtmChannelAttribute> attributes, @Nullable Callback<Void> callback) {
        getSdk().addOrUpdateChannelAttributes(
                channelId,
                attributes,
                new ChannelAttributeOptions(true),
                new RtmCallback(callback, () -> log.d("addOrUpdateChannelAttributes success"))
        );
    }

    public void deleteChannelAttributesByKeys(String channelId, List<String> keys, @Nullable Callback<Void> callback) {
        getSdk().deleteChannelAttributesByKeys(
                channelId,
                keys,
                new ChannelAttributeOptions(true),
                new RtmCallback(callback, () -> log.d("deleteChannelAttributesByKeys success")));
    }

    public void sendMessageToPeer(String userId, String message) {
        getSdk().sendMessageToPeer(userId, getSdk().createMessage(message), null, new ResultCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                log.d("sendMessageToPeer success");
            }

            @Override
            public void onFailure(ErrorInfo errorInfo) {
            }
        });
    }

    public void sendMessage(String message) {
        if (rtmChannel != null) {
            rtmChannel.sendMessage(getSdk().createMessage(message), new ResultCallback<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    log.d("sendMessage success");
                }

                @Override
                public void onFailure(ErrorInfo errorInfo) {
                }
            });
        }
    }

    @Override
    public void onMemberCountUpdated(int i) {
        for (RtmEventListener listener : listeners) {
            listener.onMemberCountUpdated(i);
        }
    }

    @Override
    public void onAttributesUpdated(List<RtmChannelAttribute> list) {
        for (RtmEventListener listener : listeners) {
            listener.onAttributesUpdated(list);
        }
    }

    @Override
    public void onMessageReceived(RtmMessage rtmMessage, RtmChannelMember rtmChannelMember) {
        log.i("onChannelMessageReceived %s from %s", rtmMessage.getText(), rtmChannelMember.getUserId());
        for (RtmEventListener listener : listeners) {
            listener.onMessageReceived(rtmMessage, rtmChannelMember);
        }
    }

    @Override
    public void onImageMessageReceived(RtmImageMessage rtmImageMessage, RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onFileMessageReceived(RtmFileMessage rtmFileMessage, RtmChannelMember rtmChannelMember) {

    }

    @Override
    public void onMemberJoined(RtmChannelMember rtmChannelMember) {
        log.i("onMemberJoined %s", rtmChannelMember.getUserId());
        for (RtmEventListener listener : listeners) {
            listener.onMemberJoined(rtmChannelMember);
        }
    }

    @Override
    public void onMemberLeft(RtmChannelMember rtmChannelMember) {
        log.i("onMemberLeft %s", rtmChannelMember.getUserId());
        for (RtmEventListener listener : listeners) {
            listener.onMemberLeft(rtmChannelMember);
        }
    }

    @Override
    public void onConnectionStateChanged(int i, int i1) {
        log.i("onConnectionStateChanged %d %d", i, i1);
        if (connectionState == RtmStatusCode.ConnectionState.CONNECTION_STATE_RECONNECTING
                && i == RtmStatusCode.ConnectionState.CONNECTION_STATE_CONNECTED) {
            for (RtmEventListener listener : listeners) {
                listener.onReJoinChannelSuccess(rtmChannel.getId());
            }
        }
        connectionState = i;
    }

    @Override
    public void onMessageReceived(RtmMessage rtmMessage, String s) {
        log.i("onPeerMessageReceived %s from %s", rtmMessage.getText(), s);
        for (RtmEventListener listener : listeners) {
            listener.onMessageReceived(rtmMessage, s);
        }
    }

    @Override
    public void onImageMessageReceivedFromPeer(RtmImageMessage rtmImageMessage, String s) {

    }

    @Override
    public void onFileMessageReceivedFromPeer(RtmFileMessage rtmFileMessage, String s) {

    }

    @Override
    public void onMediaUploadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onMediaDownloadingProgress(RtmMediaOperationProgress rtmMediaOperationProgress, long l) {

    }

    @Override
    public void onTokenExpired() {
        for (RtmEventListener listener : listeners) {
            listener.onTokenExpired();
        }
    }

    @Override
    public void onPeersOnlineStatusChanged(Map<String, Integer> map) {
        for (RtmEventListener listener : listeners) {
            listener.onPeersOnlineStatusChanged(map);
        }
    }

    private class RtmCallback implements ResultCallback<Void> {
        @Nullable
        private Callback<Void> callback;
        @Nullable
        private Runnable runnable;

        RtmCallback(@Nullable Callback<Void> callback, @Nullable Runnable runnable) {
            this.callback = callback;
            this.runnable = runnable;
        }

        @Override
        public void onSuccess(Void aVoid) {
            if (runnable != null) {
                runnable.run();
            }
            if (callback != null) {
                callback.onSuccess(aVoid);
            }
        }

        @Override
        public void onFailure(ErrorInfo errorInfo) {
            if (errorInfo.getErrorCode() == 102) {
                connectionState = RtmStatusCode.ConnectionState.CONNECTION_STATE_DISCONNECTED;
            }
            if (callback instanceof ThrowableCallback) {
                ((ThrowableCallback<Void>) callback).onFailure(new Throwable(errorInfo.toString()));
            }
        }
    }
}
