package com.ml.doctor.call2;

import android.arch.lifecycle.Observer;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lenovo on 2017/10/20.
 */

public class PhoneStateObserver {
    public enum PhoneState {
        IDLE,           // 空闲
        INCOMING_CALL,  // 有来电
        DIALING_OUT,    // 呼出电话已经接通
        DIALING_IN      // 来电已接通
    }

    private final String TAG = "PhoneStateObserver";

    private int phoneState = TelephonyManager.CALL_STATE_IDLE;
    private PhoneState state = PhoneState.IDLE;

    private List<Observer<Integer>> autoHangUpObservers = new ArrayList<>(1); // 与本地电话互斥的挂断监听

    private static class Holder {
        private final static PhoneStateObserver INSTANCE = new PhoneStateObserver();
    }

    private PhoneStateObserver() {

    }

    public static PhoneStateObserver getInstance() {
        return Holder.INSTANCE;
    }

    public void onPhoneStateChanged(String state) {
        Log.i(TAG, "onPhoneStateChanged, now state =" + state);

        this.state = PhoneState.IDLE;
        if (TelephonyManager.EXTRA_STATE_IDLE.equals(state)) {
            phoneState = TelephonyManager.CALL_STATE_IDLE;
            this.state = PhoneState.IDLE;
        } else if (TelephonyManager.EXTRA_STATE_RINGING.equals(state)) {
            phoneState = TelephonyManager.CALL_STATE_RINGING;
            this.state = PhoneState.INCOMING_CALL;
        } else if (TelephonyManager.EXTRA_STATE_OFFHOOK.equals(state)) {
            int lastPhoneState = phoneState;
            phoneState = TelephonyManager.CALL_STATE_OFFHOOK;
            if (lastPhoneState == TelephonyManager.CALL_STATE_IDLE) {
                this.state = PhoneState.DIALING_OUT;
            } else if (lastPhoneState == TelephonyManager.CALL_STATE_RINGING) {
                this.state = PhoneState.DIALING_IN;
            }
        }

        handleLocalCall();
    }

    /**
     * 处理本地电话与网络通话的互斥
     */
    public void handleLocalCall() {
        Log.i(TAG, "notify call state changed, state=" + state.name());

        if (state != PhoneState.IDLE) {

        }
    }

    public PhoneState getCallState() {
        return state;
    }


    private <T> void registerObservers(List<Observer<T>> observers, final Observer<T> observer, boolean register) {
        if (observers == null || observer == null) {
            return;
        }

        if (register) {
            observers.add(observer);
        } else {
            observers.remove(observer);
        }
    }

    /**
     * 监听网络通话发起，接听或正在进行时有本地来电的通知
     * 网络通话发起或者正在接通时，需要监听是否有本地来电（用户接通本地来电）。
     * 若有本地来电，目前Demo中示例代码的处理是网络通话自动拒绝或者挂断，开发者可以自行灵活处理。
     */
    public void observeAutoHangUpForLocalPhone(Observer<Integer> observer, boolean register) {
        Log.i(TAG, "observeAutoHangUpForLocalPhone->" + observer + "#" + register);
        registerObservers(this.autoHangUpObservers, observer, register);
    }
}
