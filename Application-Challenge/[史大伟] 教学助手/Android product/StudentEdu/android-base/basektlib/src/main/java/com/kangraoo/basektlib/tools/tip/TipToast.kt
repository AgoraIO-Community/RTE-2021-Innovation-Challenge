package com.kangraoo.basektlib.tools.tip

import android.widget.Toast
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.SSystem

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/16
 * desc :
 */
object TipToast {

    @Volatile private var toast: Toast? = null

    fun tip(iTipToast: ITipToast, message: Int) {
        tip {
            show(iTipToast.tip(message))
        }
    }

    fun tip(iTipToast: ITipToast, message: Int, duration: Int) {
        tip {
            show(iTipToast.tip(message, duration))
        }
    }

    fun tip(iTipToast: ITipToast, message: CharSequence?) {
        tip {
            show(iTipToast.tip(message))
        }
    }

    fun tip(iTipToast: ITipToast, message: CharSequence?, duration: Int) {
        tip {
            show(iTipToast.tip(message, duration))
        }
    }

    private inline fun tip(crossinline block: () -> Unit) {
        if (SSystem.isMainThread()) {
            block()
        } else {
            HAction.mainHandler.post {
                block()
            }
        }
    }

    @Synchronized fun show(currentToast: Toast) {
        cancelToast()
        currentToast.show()
        toast = currentToast
    }

    @Synchronized fun cancelToast() {
        toast?.let {
            it.cancel()
            toast = null
        }
    }
}
