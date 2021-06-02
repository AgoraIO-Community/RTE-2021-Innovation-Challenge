package com.kangaroo.studentedu.ui.activity

import android.Manifest
import android.app.Activity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.LabelVisibilityMode
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.easeui.modules.contact.EaseContactListFragment
import com.hyphenate.easeui.modules.conversation.EaseConversationListFragment
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.ui.fragment.*
import com.kangraoo.basektlib.tools.UFragment
import com.kangraoo.basektlib.tools.UPermission
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.BActivity
import com.qdedu.baselibcommon.arouter.ServiceProvider
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import com.tbruyelle.rxpermissions2.RxPermissions
import kotlinx.android.synthetic.main.activity_main.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-24 11:18
 * #首页#
 */
class MainActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, MainActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_main

    private var frameUseFragment: Fragment =
        ConversationListFragment()

    private var peopleFragment: Fragment =
        ContactListFragment()

    private var layoutTestFragment: Fragment =
        RiliFragment.newInstance()

    private var testFragment: Fragment =
        MeFragment.newInstance()

    private var moduleFragment: Fragment =
        ModuleFragment.newInstance()
    override fun onViewCreated(savedInstanceState: Bundle?) {
        switchDefaultFragment(savedInstanceState)
        UPermission.requestPermission(
            object : UPermission.RequestPermission {
                override fun onRequestPermissionSuccess() {
                }

                override fun onRequestPermissionFailure(permissions: List<String>?) {
                }

                override fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>?) {
                }
            }, RxPermissions(this),
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.INTERNET,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_NETWORK_STATE,
            Manifest.permission.WAKE_LOCK,
            Manifest.permission.CHANGE_NETWORK_STATE,
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
        )

        immersionBar {
            statusBarDarkFont(true)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "主页面"
        libToolBarOptions.setNeedNavigate(false)
        setToolBar(R.id.toolbar, libToolBarOptions, null)

        bottombar.setTextSize(12f)
        bottombar.enableAnimation(false)
        bottombar.labelVisibilityMode = LabelVisibilityMode.LABEL_VISIBILITY_LABELED
        bottombar.isItemHorizontalTranslationEnabled = false
        bottombar.selectedItemId = bottombar.selectedItemId
        bottombar.setOnNavigationItemSelectedListener {

            when (it.title) {
                getString(R.string.message_list) -> {
                    switchFragment(mFragment, frameUseFragment)
                }
                getString(R.string.people) -> {
                    switchFragment(mFragment, peopleFragment)
                }
                getString(R.string.ke_cheng) -> {
                    switchFragment(mFragment, layoutTestFragment)
                }
                getString(R.string.me) -> {
                    switchFragment(mFragment, testFragment)
                }
                getString(R.string.module) -> {
                    switchFragment(mFragment, moduleFragment)
                }
            }
            selectedItemId = it.itemId
            true
        }
    }
    private fun switchDefaultFragment(savedInstanceState: Bundle?) {
        if (savedInstanceState != null) {
            val fragments = supportFragmentManager.fragments
            fragments.forEach { fragment ->
                when (fragment) {
                    is ConversationListFragment -> {
                        frameUseFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, frameUseFragment)
                        }
                    }
                    is ContactListFragment -> {
                        peopleFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, peopleFragment)
                        }
                    }
                    is RiliFragment -> {
                        layoutTestFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, layoutTestFragment)
                        }
                    }
                    is MeFragment -> {
                        testFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, testFragment)
                        }
                    }
                    is ModuleFragment -> {
                        moduleFragment = fragment
                        if (!fragment.isHidden) {
                            switchFragment(mFragment, moduleFragment)
                        }
                    }
                }
            }
        } else {
            switchFragment(mFragment, moduleFragment)
        }
    }

    var mFragment: Fragment? = null
    var selectedItemId: Int = R.id.navigation_home

    private fun <T : Fragment> switchFragment(
        from: T?,
        to: T
    ) {
        var switchFragment =
            UFragment.switchFragment(supportFragmentManager, R.id.fl_main, from, to)
        mFragment = switchFragment

    }

    override fun onBackPressed() {
        if(doubleClickToExit()) super.onBackPressed()
    }
    private var lastClickTime = 0L
    private fun doubleClickToExit(): Boolean {
        val clickTime = UTime.currentTimeMillis()
        return if (lastClickTime != 0L && clickTime - lastClickTime < 1000) {
            true
        } else {
            showToastMsg(Tip.Info, "再按一次退出应用")
            lastClickTime = clickTime
            false
        }
    }
}
