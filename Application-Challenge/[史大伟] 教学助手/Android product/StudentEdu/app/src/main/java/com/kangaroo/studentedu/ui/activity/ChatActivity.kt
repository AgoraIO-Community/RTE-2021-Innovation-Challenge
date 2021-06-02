package com.kangaroo.studentedu.ui.activity

import android.app.Activity
import android.os.Bundle
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.easeui.modules.chat.EaseChatFragment
import com.kangaroo.studentedu.R
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.ui.BActivity
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions

/**
 * 自动生成：by WaTaNaBe on 2021-05-24 14:24
 * #聊天界面#
 */
class ChatActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity, conversationId: String,chatType:Int) {
            var bundle:Bundle = Bundle()
            bundle.putInt("chatType", chatType)
            bundle.putBoolean("isRoaming", true)
            bundle.putString("conversationId", conversationId)
            LibActivityLauncher.instance
                .launch(activity, ChatActivity::class.java, bundle)
        }

        fun startFromSingle(activity: Activity, conversationId: String) {
            var bundle:Bundle = Bundle()
            bundle.putInt("chatType", 1)
            bundle.putBoolean("isRoaming", true)
            bundle.putString("conversationId", conversationId)
            LibActivityLauncher.instance
                .launch(activity, ChatActivity::class.java, bundle)
        }
        fun startFromGroup(activity: Activity, conversationId: String) {
            var bundle:Bundle = Bundle()
            bundle.putInt("chatType", 2)
            bundle.putBoolean("isRoaming", true)
            bundle.putString("conversationId", conversationId)
            LibActivityLauncher.instance
                .launch(activity, ChatActivity::class.java, bundle)
        }
        fun startFromIMShi(activity: Activity, conversationId: String) {
            var bundle = Bundle()
            bundle.putInt("chatType", 3)
            bundle.putBoolean("isRoaming", true)
            bundle.putString("conversationId", conversationId)
            LibActivityLauncher.instance
                .launch(activity, ChatActivity::class.java, bundle)
        }
    }

    override fun getLayoutId() = R.layout.activity_chat


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = intent.getStringExtra("conversationId")
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {},false)
       var chatFragment = EaseChatFragment()
        chatFragment.arguments = intent.extras
        supportFragmentManager.beginTransaction().add(R.id.fl_main, chatFragment).commit()
    }

}
