package com.kangraoo.basektlib.tools

import android.content.Context
import android.content.res.Resources
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.store.MMKVStore

/**
 * 键盘工具
 * Created by jing on 2016/8/19.
 */
object TKeybord {
    private const val TAG = "TKeybord"
    private const val SYS_KEYBOARD_h = "sys_keyboard_h"
    fun saveKeyBoardHeight(height: Int) {
        MMKVStore.instance(SApplication.instance().sConfiger.sysSharedpreferences)
            .put(
                SYS_KEYBOARD_h, height
            )
    }

    val keyBoardHeight: Int
        get() {
            val hs: Int =
                MMKVStore.instance(SApplication.instance().sConfiger.sysSharedpreferences)
                    .get(
                        SYS_KEYBOARD_h, -1, Int::class.java
                    )!!
            return if (hs == -1) {
                val resources: Resources = SApplication.context().resources
                val dm = resources.displayMetrics
                (dm.heightPixels / 2.5).toInt()
            } else {
                hs
            }
        }

    /**
     * 打开键盘
     * @param mEditText
     */
    fun openKeybord(mEditText: EditText?) {
        val imm = SApplication.context()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN)
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            InputMethodManager.HIDE_IMPLICIT_ONLY
        )
    }

    /**
     * 关闭软键盘
     * @param mEditText
     */
    fun closeKeybord(mEditText: EditText) {
        ULog.d("closeKeybord")
        val imm = SApplication.context()
            .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(mEditText.windowToken, 0)
    }
}
