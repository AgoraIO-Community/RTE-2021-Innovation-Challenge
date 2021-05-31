package com.kangaroo.studentedu.ui.activity

import android.app.Activity
import android.app.Dialog
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.EMCallBack
import com.hyphenate.chat.EMClient
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.tools.UUser.MATH
import com.kangaroo.studentedu.tools.UUser.STUDENT
import com.kangaroo.studentedu.tools.UUser.TEACHER
import com.kangaroo.studentedu.tools.UUser.TIYU
import com.kangaroo.studentedu.tools.UUser.YUWEN
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.data.model.SelectAnyModel
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.common.DialogPopupConfig
import com.kangraoo.basektlib.widget.dialog.LibListSelectDialog
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarListener
import com.qdedu.baselibcommon.widget.toolsbar.CommonToolBarOptions
import kotlinx.android.synthetic.main.activity_login.*
import java.util.ArrayList

/**
 * 自动生成：by WaTaNaBe on 2021-05-25 08:36
 * #登录#
 */
class LoginActivity : BActivity(){

    companion object{

        fun startFrom(activity: Activity) {
            LibActivityLauncher.instance
                .launch(activity, LoginActivity::class.java)
        }

    }

    override fun getLayoutId() = R.layout.activity_login
    var libListSelectDialog: LibListSelectDialog? = null
    var libListSelectDialog2: LibListSelectDialog? = null
//    val student = arrayOf("sdw","xiaoming")
//    val teacher = arrayOf("Teacher-Lee(普通老师，白板课)", "TiYu-Liu(体育老师，直播课)","Teacher-Wang(普通老师，白板课)")
    val selectModelList: MutableList<SelectAnyModel> = ArrayList<SelectAnyModel>().apply {
        add(SelectAnyModel("学生", 0))
        add(SelectAnyModel("老师", 1))
    }

    val student: MutableList<SelectAnyModel> = ArrayList<SelectAnyModel>().apply {
        add(SelectAnyModel("sdw", ::studentsdw))
        add(SelectAnyModel("xiaoming", ::studentxiaoming))
    }

    val teacher: MutableList<SelectAnyModel> = ArrayList<SelectAnyModel>().apply {
        add(SelectAnyModel("Teacher-Lee(普通老师，白板课)", ::teacherLee))
        add(SelectAnyModel("TiYu-Liu(体育老师，直播课)", ::tiYuLiu))
        add(SelectAnyModel("Teacher-Wang(普通老师，白板课)", ::teacherWang))
    }
    override fun onViewCreated(savedInstanceState: Bundle?) {
        immersionBar {
            statusBarDarkFont(true)
            statusBarColor(R.color.color_white)
        }
        val libToolBarOptions = CommonToolBarOptions()
        libToolBarOptions.titleString = "登录"
        libToolBarOptions.setNeedNavigate(false)
        setToolBar(R.id.toolbar, libToolBarOptions, object : CommonToolBarListener() {})


        libListSelectDialog = LibListSelectDialog(
            visitActivity(),
            null,
            DialogPopupConfig.build { width = UUi.getDialogWidth(SApplication.context()) }
        )
        libListSelectDialog!!.setTitle("选择器")
        libListSelectDialog!!.setCancle("取消")
        libListSelectDialog!!.gravity = (Gravity.CENTER)
        libListSelectDialog!!.onLibListDialogListener = object : LibListSelectDialog.OnLibListDialogListener {

            override fun onSureListener(dialog: Dialog?, v: View?, selectModel: SelectAnyModel?) {
                spinner.text = selectModel!!.title
                var i = selectModel!!.value as Int
                when(i){
                    0 ->{
                        spinner2.setOnClickListener {
                            libListSelectDialog2!!.show(student)
                        }
                    }
                    1 ->{
                        spinner2.setOnClickListener {
                            libListSelectDialog2!!.show(teacher)

                        }
                    }
                }
                dialog!!.dismiss()
            }
        }

        libListSelectDialog2 = LibListSelectDialog(
            visitActivity(),
            null,
            DialogPopupConfig.build { width = UUi.getDialogWidth(SApplication.context()) }
        )
        libListSelectDialog2!!.setTitle("选择器")
        libListSelectDialog2!!.setCancle("取消")
        libListSelectDialog2!!.gravity = (Gravity.CENTER)
        libListSelectDialog2!!.onLibListDialogListener = object : LibListSelectDialog.OnLibListDialogListener {

            override fun onSureListener(dialog: Dialog?, v: View?, selectModel: SelectAnyModel?) {
                spinner2.text = selectModel!!.title
                temp = selectModel.value as ()->Unit
                dialog!!.dismiss()
            }
        }

        spinner.setOnClickListener {
            libListSelectDialog!!.show(selectModelList)
        }
        spinner2.setOnClickListener {
            showToastMsg(Tip.Warning,"请先选择身份类型")
        }

        button.setOnClickListener {
            if(temp!=null){
                temp!!.invoke()
            }else{
                showToastMsg(Tip.Warning,"请先选择身份类型和姓名")
            }
        }

    }
    var temp: (()->Unit)? = null

    fun teacherWang() {
        login("Teacher-wang", "123456", TEACHER, YUWEN)
    }




    private fun login(user: String, pass: String, type: Int, teacherType: Int = 0) {
        showProgressingDialog()
        EMClient.getInstance().login(user, pass, object : EMCallBack {
            //回调
            override fun onSuccess() {
                dismissProgressDialog()
                UUser.login(user, pass, type, teacherType)

                EMClient.getInstance().groupManager().loadAllGroups()
                EMClient.getInstance().chatManager().loadAllConversations()

                showToastMsg(Tip.Success, "登录成功")
                finish()
                MainActivity.startFrom(visitActivity())

            }

            override fun onProgress(progress: Int, status: String) {}
            override fun onError(code: Int, message: String) {
                dismissProgressDialog()
                showToastMsg(Tip.Error, "登录失败")
            }
        })

    }



    fun teacherLee() {
        login("Teacher-Lee", "123456", TEACHER, MATH)
    }
    fun studentxiaoming() {
        login("xiaoming", "123456", STUDENT)
    }
    fun studentsdw() {
        login("sdw", "123456", STUDENT)
    }

    fun tiYuLiu() {
        login("tiyu-liu", "123456", TEACHER, TIYU)

    }

}
