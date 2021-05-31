package com.kangaroo.studentedu.ui.fragment

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.gyf.immersionbar.ktx.immersionBar
import com.hyphenate.chat.EMClient
import com.kangraoo.basektlib.ui.BActivity
import com.kangraoo.basektlib.widget.toolsbar.LibToolBarOptions
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangaroo.studentedu.R;
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.tools.UUser.MATH
import com.kangaroo.studentedu.tools.UUser.TEACHER
import com.kangaroo.studentedu.tools.UUser.TIYU
import com.kangaroo.studentedu.tools.UUser.YUWEN
import com.kangaroo.studentedu.ui.activity.LoginActivity
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.tools.launcher.LibActivityLauncher
import com.kangraoo.basektlib.ui.BFragment
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * 自动生成：by WaTaNaBe on 2021-05-24 16:20
 * #我的#
 */
class MeFragment : BFragment(){

    companion object{

        @JvmStatic
        fun newInstance() = MeFragment()
        
    }

    override fun getLayoutId() = R.layout.fragment_me


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)
        logout.setOnClickListener {
            EMClient.getInstance().logout(true)
            UUser.logout()
            ActivityLifeManager.finishAllActivity()
            LoginActivity.startFrom(visitActivity())
        }

        username.text = "账号 ： ${UUser.getName()}"
        if(UUser.getType()==TEACHER){
            role.text = "权限 ： 老师"
            role_type.visibility = View.VISIBLE
            if(UUser.getTeacherType()== MATH){
                role_type.text = "老师类型 ： 数学"
            }else if(UUser.getTeacherType() == YUWEN){
                role_type.text = "老师类型 ： 语文"
            }else if(UUser.getTeacherType() == TIYU){
                role_type.text = "老师类型 ： 体育"
            }
        }else{
            role_type.visibility = View.GONE
            role.text = "权限 ： 学生"
        }
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        setTitle("我的")
    }

}
