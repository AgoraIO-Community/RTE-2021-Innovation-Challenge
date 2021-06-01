package com.qdedu.baselibcommon.widget.toolsbar

import android.view.View
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener

abstract class CommonToolBarListener: OnLibToolBarListener {
    override fun onLeft(view: View, opt: Int) {
        when(view.id){
            R.id.lib_toolbar_left_img1 -> onNavigate(view)
//            R.id.lib_toolbar_left_img2 -> leftImage2(view)
//            R.id.lib_toolbar_left_text1 ->
            R.id.lib_toolbar_left_text1 -> onLeft(view)
        }
    }

    override fun onRight(view: View, opt: Int) {
        when(view.id){
            R.id.lib_toolbar_right_img1 ->  onRightImage(view)
//            R.id.lib_toolbar_right_img2 ->  rightImage2(view)
            R.id.lib_toolbar_right_text1 -> onRight(view)
//            R.id.lib_toolbar_right_text2 -> onRight(view)
        }
    }

    open fun onRightImage(view: View){

    }

    override fun onNavigate(view: View) {
        ActivityLifeManager.getCurrentActivity()!!.onBackPressed()
    }

    open fun onRight(view: View){

    }

    open fun onLeft(view: View){

    }

}