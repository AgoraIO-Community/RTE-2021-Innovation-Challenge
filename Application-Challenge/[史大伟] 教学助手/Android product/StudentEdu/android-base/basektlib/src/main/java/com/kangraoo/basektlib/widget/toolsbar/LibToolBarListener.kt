package com.kangraoo.basektlib.widget.toolsbar

import android.view.View
import com.kangraoo.basektlib.R

abstract class LibToolBarListener : OnLibToolBarListener {
    override fun onLeft(view: View, opt: Int) {
        when (view.id) {
            R.id.lib_toolbar_left_img1 -> leftImage1(view)
            R.id.lib_toolbar_left_img2 -> leftImage2(view)
            R.id.lib_toolbar_left_text1 -> onNavigate(view)
            R.id.lib_toolbar_left_text2 -> leftText2(view)
        }
    }

    abstract fun leftText2(view: View)

    abstract fun leftImage2(view: View)

    abstract fun leftImage1(view: View)

    override fun onRight(view: View, opt: Int) {
        when (view.id) {
            R.id.lib_toolbar_right_img1 -> rightImage1(view)
            R.id.lib_toolbar_right_img2 -> rightImage2(view)
            R.id.lib_toolbar_right_text1 -> rightText1(view)
            R.id.lib_toolbar_right_text2 -> rightText2(view)
        }
    }

    abstract fun rightText2(view: View)

    abstract fun rightText1(view: View)

    abstract fun rightImage2(view: View)

    abstract fun rightImage1(view: View)
}
