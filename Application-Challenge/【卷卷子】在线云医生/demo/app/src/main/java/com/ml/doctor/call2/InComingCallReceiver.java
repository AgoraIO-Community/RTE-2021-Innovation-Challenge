package com.ml.doctor.call2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.TelephonyManager;

public class InComingCallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        if (TelephonyManager.ACTION_PHONE_STATE_CHANGED.equals(action)) {
            final String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            PhoneStateObserver.getInstance().onPhoneStateChanged(state);
        }
    }
}
