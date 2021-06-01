package com.kangraoo.basektlib.widget.emptypage

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/08/03
 * desc :
 */
interface IEmptyPage {
    val layoutId: Int
    fun init(view: View)
    fun setEmptyType(type: EmptyType)
    fun buttonStyle(@DrawableRes background: Int, @ColorRes textColor: Int)
    fun buttonText(text: String)
    fun setButtonClickListener(onRefreshDelegate: AbsEmptyPage.OnRefreshDelegate)
}
