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
import kotlinx.android.synthetic.main.activity_qian_dao.*
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import android.app.Activity
import android.content.DialogInterface
import androidx.core.app.NotificationCompat.PRIORITY_MAX
import com.kangaroo.studentedu.tools.UUser
import com.kangraoo.basektlib.tools.HNotification
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.widget.dialog.LibCheckDialog
import kotlinx.android.synthetic.main.activity_qian_dao.button

/**
 * 自动生成：by WaTaNaBe on 2021-05-26 15:17
 * #签到#
 */
class QianDaoActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, QianDaoActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_qian_dao


    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "签到"
        libToolBarOptions.setNeedNavigate(true)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener(){})
        val libCheckDialog = LibCheckDialog(visitActivity())

        libCheckDialog.title("提示")
        libCheckDialog.content("确定签到吗")
        libCheckDialog.sureVisable(View.VISIBLE)
        libCheckDialog.cancleVisable(View.VISIBLE)
        libCheckDialog.sure("确定")
        libCheckDialog.cancle("取消")
        libCheckDialog.onLibDialogListener =
            (object : LibCheckDialog.OnLibCheckDialogListener {
                override fun onSure() {
                    showToastMsg("点击确定")
                    libCheckDialog.dismiss()
                    showToastMsg(Tip.Success,"签到已完成")
                    finish()
                    HNotification.show(contentText = UUser.getName()+"签到成功",contentTitle = "签到",smallIcon = R.mipmap.ic_launcher,ongoing = true,channelId = HNotification.CHANNEL_APP)
                }

                override fun onCancle() {
                }

                override fun onShow() {
                }

                override fun onDismiss(dialog: DialogInterface) {
                }

            })
        button.setOnClickListener {
            libCheckDialog.show()

        }
    }

}
