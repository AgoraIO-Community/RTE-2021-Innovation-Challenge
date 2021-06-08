package com.ml.doctor.call2;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import com.ml.doctor.CustomApplication;
import com.ml.doctor.R;

/**
 * Created by lenovo on 2017/10/26.
 */

public class CallSoundPlayer {
    private static final String TAG = "CallSoundPlayer";

    public enum RingerType {
        CONNECTING,
        NO_RESPONSE,
        PEER_BUSY,
        PEER_REJECT,
        RING,
        ;
    }
    private Context context;

    private SoundPool soundPool;
    private AudioManager audioManager;
    private int streamId;
    private int soundId;
    private boolean loop;
    private RingerType mRingerType;
    private boolean isRingModeRegister = false;
    private int ringMode = -1;

    @SuppressLint("StaticFieldLeak")
    private static CallSoundPlayer instance = null;
    private RingModeChangeReceiver ringModeChangeReceiver;

    public static CallSoundPlayer instance() {
        if(instance == null) {
            synchronized (CallSoundPlayer.class) {
                if(instance == null) {
                    instance = new CallSoundPlayer();
                }
            }
        }
        return instance;
    }

    public CallSoundPlayer() {
        this.context = CustomApplication.getInstance();
    }

    public synchronized void play(RingerType type) {
        Log.d(TAG, "play type->" + type.name());
        this.mRingerType = type;
        int ringId = 0;
        switch (type) {
            case NO_RESPONSE:
                ringId = R.raw.avchat_no_response;
                loop = false;
                break;
            case PEER_BUSY:
                ringId = R.raw.avchat_peer_busy;
                loop = false;
                break;
            case PEER_REJECT:
                ringId = R.raw.avchat_peer_reject;
                loop = false;
                break;
            case CONNECTING:
                ringId = R.raw.avchat_connecting;
                loop = true;
                break;
            case RING:
                ringId = R.raw.avchat_ring;
                loop = true;
                break;
        }

        if(ringId != 0) {
            play(ringId);
        }

    }

    public void stop() {
        Log.d(TAG, "stop");
        if (soundPool != null) {
            if (streamId != 0) {
                soundPool.stop(streamId);
                streamId = 0;
            }
            if (soundId != 0) {
                soundPool.unload(soundId);
                soundId = 0;
            }
        }
        if (isRingModeRegister) {
            registerVolumeReceiver(false);
        }
    }

    private void play(int ringId) {
        initSoundPool();
        if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
            soundId = soundPool.load(context, ringId, 1);
        }
    }

    private void initSoundPool() {
        stop();
        if (soundPool == null) {
            soundPool = new SoundPool(1, AudioManager.STREAM_RING, 0);
            soundPool.setOnLoadCompleteListener(onLoadCompleteListener);

            audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            ringMode = audioManager.getRingerMode();
        }
        registerVolumeReceiver(true);
    }

    SoundPool.OnLoadCompleteListener onLoadCompleteListener = new SoundPool.OnLoadCompleteListener() {
        @Override
        public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {
            if (soundId != 0 && status == 0) {
                if (audioManager.getRingerMode() == AudioManager.RINGER_MODE_NORMAL) {
                    int curVolume = audioManager.getStreamVolume(AudioManager.STREAM_RING);
                    streamId = soundPool.play(soundId, curVolume, curVolume, 1, loop ? -1 : 0, 1f);
                }
            }
        }
    };

    private void registerVolumeReceiver(boolean register){
        if (ringModeChangeReceiver == null) {
            ringModeChangeReceiver = new RingModeChangeReceiver() ;
        }

        if (register) {
            isRingModeRegister = true;
            IntentFilter filter = new IntentFilter() ;
            filter.addAction(AudioManager.RINGER_MODE_CHANGED_ACTION) ;
            context.registerReceiver(ringModeChangeReceiver, filter) ;
        } else {
            context.unregisterReceiver(ringModeChangeReceiver);
            isRingModeRegister = false;
        }
    }

    private class RingModeChangeReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (ringMode != -1 && ringMode != audioManager.getRingerMode()
                    && intent.getAction().equals(AudioManager.RINGER_MODE_CHANGED_ACTION)) {
                ringMode = audioManager.getRingerMode();
                play(mRingerType);
            }
        }
    }
}
