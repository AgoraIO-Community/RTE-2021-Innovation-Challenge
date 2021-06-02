package com.kangraoo.basektlib.tools.listener

import android.telephony.PhoneStateListener
import android.telephony.TelephonyManager
import com.kangraoo.basektlib.tools.log.ULog

class LibPhoneCallListener(var listener: CallListener) : PhoneStateListener() {

    private val tag = "MyPhoneCallListener"

    /**
     * 返回电话状态
     *
     * CALL_STATE_IDLE 无任何状态时
     * CALL_STATE_OFFHOOK 接起电话时
     * CALL_STATE_RINGING 电话进来时
     */
    override fun onCallStateChanged(state: Int, incomingNumber: String?) {
        when (state) {
            TelephonyManager.CALL_STATE_IDLE -> {
                ULog.d(tag, "电话挂断...")
                listener.onCallFinish()
            }
            TelephonyManager.CALL_STATE_OFFHOOK -> {
                ULog.d(tag, "正在通话...")
                listener.onCallRinging()
            }
            TelephonyManager.CALL_STATE_RINGING -> {
                ULog.d(tag, "电话响铃")
                listener.onCallStart()
            }
        }
        super.onCallStateChanged(state, incomingNumber)
    }

    // 回调接口
    interface CallListener {
        fun onCallFinish()
        fun onCallRinging()
        fun onCallStart()
    }
}
