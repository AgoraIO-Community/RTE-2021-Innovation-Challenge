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
import kotlinx.android.synthetic.main.activity_kao_qing.*
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity

/**
 * 自动生成：by WaTaNaBe on 2021-05-26 15:17
 * #学员考勤#
 */
class KaoQingActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, KaoQingActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_kao_qing


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "学员考勤"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener(){})

    }

    fun gaoEr3(view: View) {
        KaoQingListActivity.startFrom(visitActivity())
    }

}
