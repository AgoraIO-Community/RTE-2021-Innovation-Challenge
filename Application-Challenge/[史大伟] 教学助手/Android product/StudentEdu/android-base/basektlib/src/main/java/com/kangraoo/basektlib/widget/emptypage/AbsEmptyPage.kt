package com.kangraoo.basektlib.widget.emptypage

import android.view.View

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/08/03
 * desc :
 */
abstract class AbsEmptyPage : IEmptyPage {

    fun <T : View> findViewById(view: View, id: Int): T {
        return view.findViewById<View>(id) as T
    }

    interface OnRefreshDelegate {
        fun onRefresh()
    }

    protected var onRefreshDelegate: OnRefreshDelegate? = null

    override fun setButtonClickListener(onRefreshDelegate: OnRefreshDelegate) {
        this.onRefreshDelegate = onRefreshDelegate
    }
}
