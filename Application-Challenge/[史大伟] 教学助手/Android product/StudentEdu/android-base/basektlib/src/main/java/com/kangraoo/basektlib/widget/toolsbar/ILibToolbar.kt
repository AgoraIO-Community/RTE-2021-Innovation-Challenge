package com.kangraoo.basektlib.widget.toolsbar

import android.view.View

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/09
 * desc :
 * version: 1.0
 */
interface ILibToolbar {
    fun setOptions(options: ToolBarOptions)
    fun setTitle(title: String?)
    fun setOnLibToolBarListener(onLibToolBarListener: OnLibToolBarListener?)
}

interface OnLibToolBarListener {
    fun onLeft(view: View, opt: Int)
    fun onRight(view: View, opt: Int)
    fun onNavigate(view: View)
}
