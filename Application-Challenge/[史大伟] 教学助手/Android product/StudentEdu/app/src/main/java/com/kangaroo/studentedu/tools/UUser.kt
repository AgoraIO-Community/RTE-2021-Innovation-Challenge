package com.kangaroo.studentedu.tools

import com.kangaroo.openlive.activities.BaseActivity
import com.kangaroo.studentedu.app.App
import com.kangaroo.studentedu.data.model.ChatRoom
import com.kangaroo.studentedu.data.model.Group
import com.kangraoo.basektlib.tools.store.MMKVStore
import kotlinx.android.synthetic.main.fragment_me.*

/**
 * @author shidawei
 * 创建日期：2021/5/25
 * 描述：
 */


object UUser {

    const val TEACHER = 1
    const val STUDENT = 2

    const val MATH = 1
    const val YUWEN = 2
    const val TIYU = 3

    fun getKe():String{
        return if(UUser.getTeacherType()== MATH){
            "数学课"
        }else if(UUser.getTeacherType() == YUWEN){
            "语文课"
        }else if (UUser.getTeacherType() == TIYU){
            "体育课"
        }else{
            ""
        }
    }

    const val USERNAME = "user_name"
    const val PASS_WORD = "password"
    const val TYPE = "type"
    const val TEACHER_TYPE = "teacher_type"

    val yuwenZb = ChatRoom("149355968397313","语文直播课交流群")
    val shuxueZb = ChatRoom("149355946377217","数学直播课交流群")
    val yuwenZy = ChatRoom("149355924357121","语文作业群")
    val shuxueZy = ChatRoom("149355901288449","数学作业群")

    val yuwen = Group("149355730370561","高二3班语文群")
    val shuxue = Group("149355697864705","高二3班数学群")

    val bgb = Group("149356125683713","班干部交流群")

    var mmkvStore = MMKVStore.instance("my_user")

    fun logout(){
        mmkvStore.clear()
    }

    fun login(user: String, pass: String, type: Int, teacherType: Int = 0){
        mmkvStore.put(USERNAME,user)
        mmkvStore.put(PASS_WORD,pass)
        mmkvStore.put(TYPE,type)
        mmkvStore.put(TEACHER_TYPE,teacherType)
        BaseActivity.agoraApplication.user = user
    }

    fun getName():String?{
        return mmkvStore.get(USERNAME,null,String::class.java)
    }

    fun getPass():String?{
        return mmkvStore.get(PASS_WORD,null,String::class.java)
    }

    fun getType():Int{
        return mmkvStore.get(TYPE,0,Int::class.java)!!
    }

    fun getTeacherType():Int{
        return mmkvStore.get(TEACHER_TYPE,0,Int::class.java)!!
    }
}