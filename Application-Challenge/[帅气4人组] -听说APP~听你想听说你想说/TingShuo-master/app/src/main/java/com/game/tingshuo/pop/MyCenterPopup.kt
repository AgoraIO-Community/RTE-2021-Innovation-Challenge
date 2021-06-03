package com.game.tingshuo.pop

import android.content.Context
import com.lxj.xpopup.core.CenterPopupView
import com.game.tingshuo.R

/**
 * 调用方式: XPopup.Builder(this).asCustom(MyCenterPopup(this)).show()
 */
class MyCenterPopup(context: Context) : CenterPopupView(context) {
    private val title: String? = null
    private val content: String? = null

    override fun getImplLayoutId(): Int {
        return R.layout.pop_my_center_demo
    }

    override fun onCreate() {
        super.onCreate()

    }
}