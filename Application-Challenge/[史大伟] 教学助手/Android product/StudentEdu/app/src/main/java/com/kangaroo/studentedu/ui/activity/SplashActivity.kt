package com.kangaroo.studentedu.ui.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.studentedu.R;
import kotlinx.android.synthetic.main.activity_splash.*
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity
import com.kangaroo.studentedu.tools.UUser
import com.kangraoo.basektlib.tools.UFont

/**
 * 自动生成：by WaTaNaBe on 2021-05-24 11:18
 * #启动页#
 */
class SplashActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, SplashActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_splash


    override fun onViewCreated(savedInstanceState: Bundle?) {
        UFont.setTextViewFont(icon, R.string.lib_icon_github)
        immersionBar {
            statusBarDarkFont(true)
        }
    }

    var runnable = Runnable {
        if(UUser.getName()!=null){
            MainActivity.startFrom(visitActivity())
        }else{
            LoginActivity.startFrom(visitActivity())
        }

        overridePendingTransition(0, 0)
        finish()
    }

    override fun onPause() {
        super.onPause()
        removeCallbacks(runnable)
    }

    override fun onResume() {
        super.onResume()
        postDelayed(runnable, 1000)
    }

}
