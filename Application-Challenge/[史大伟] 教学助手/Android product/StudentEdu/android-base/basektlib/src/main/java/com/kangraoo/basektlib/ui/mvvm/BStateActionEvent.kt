package com.kangraoo.basektlib.ui.mvvm

import androidx.annotation.StringRes
import com.kangraoo.basektlib.tools.tip.ITipToast

sealed class BStateActionEvent {

    class ProgressingState(val isShow: Boolean) : BStateActionEvent()
    class ProgressingMessageState(val message: String) : BStateActionEvent()
    class ProgressingIntMessageState(val message: Int) : BStateActionEvent()

    class ToastMsgState(val message: String?) : BStateActionEvent()
    class ToastIntMsgState(@StringRes val message: Int) : BStateActionEvent()
    class ToastTipIntMsgState(val iTipToast: ITipToast, @StringRes val message: Int) : BStateActionEvent()
    class ToastTipMsgState(val iTipToast: ITipToast, val message: String?) : BStateActionEvent()
}
