package com.kangraoo.basektlib.tools.tip

import android.widget.Toast
import androidx.annotation.NonNull
import androidx.annotation.StringRes

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc :
 */
interface ITipToast {

    fun tip(@StringRes message: Int): Toast

    fun tip(@StringRes message: Int, duration: Int): Toast

    fun tip(@NonNull message: CharSequence?): Toast

    fun tip(@NonNull message: CharSequence?, duration: Int): Toast
}
