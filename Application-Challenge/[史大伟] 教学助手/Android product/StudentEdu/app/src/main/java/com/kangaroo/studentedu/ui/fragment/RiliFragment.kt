package com.kangaroo.studentedu.ui.fragment

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.kangaroo.openlive.activities.LiveActivity
import com.kangaroo.openlive.activities.MainActivity
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.app.appCe
import com.kangaroo.studentedu.app.appId
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.tools.UUser.TIYU
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.ui.BFragment
import io.agora.edu.launch.*
import kotlinx.android.synthetic.main.fragment_rili.view.*
import com.kangaroo.studentedu.tools.rtmtoken.RtmTokenBuilder
import com.kangraoo.basektlib.tools.HString
import com.kangraoo.basektlib.tools.UTime
import com.kangraoo.basektlib.tools.UTime.YMD
import com.kangraoo.basektlib.tools.encryption.MessageDigestUtils
import com.kangraoo.basektlib.widget.dialog.LibCheckDialog
import kotlinx.android.synthetic.main.fragment_me.*


/**
 * 自动生成：by WaTaNaBe on 2021-05-24 15:58
 * #日历#
 */
class RiliFragment : BFragment(){

    companion object{

        @JvmStatic
        fun newInstance() = RiliFragment()
        
    }

    override fun getLayoutId() = R.layout.fragment_rili


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
       super.onViewCreated(view, savedInstanceState)

        if(UUser.getType()== UUser.TEACHER){
            view.shuxuezb.text = "今天您有一节数学直播课，请您登录平台直播"
            view.yuwenzb.text = "今天您有一节语文直播课，请您登录平台直播"
            view.tiyuzb.text = "今天您有一节体育直播课，点击进行直播"
            val libCheckDialog = LibCheckDialog(visitActivity())

            libCheckDialog.title("提示")
            libCheckDialog.content("老师，请您登录Web平台直播")
            libCheckDialog.cancleVisable(View.GONE)
            libCheckDialog.sureVisable(View.GONE)
            libCheckDialog.onLibDialogListener = (null)
            view.yuwenzb.setOnClickListener{
                libCheckDialog.show()

            }

            view.shuxuezb.setOnClickListener{
                libCheckDialog.show()

            }
            if(UUser.getTeacherType()== UUser.MATH){
                view.yuwenzb.visibility = View.GONE
                view.tiyuzb.visibility = View.GONE
            }else if(UUser.getTeacherType() == UUser.YUWEN){
                view.shuxuezb.visibility = View.GONE
                view.tiyuzb.visibility = View.GONE
            }else if(UUser.getTeacherType() == UUser.TIYU){
                view.shuxuezb.visibility = View.GONE
                view.yuwenzb.visibility = View.GONE
                view.tiyuzb.setOnClickListener{
                    LiveActivity.teacherStart(visitActivity(),MessageDigestUtils.md5(UTime.nowDateTime(YMD)+TIYU))
                }
            }
        }else{
            view.yuwenzb.setOnClickListener{
                joinClass("语文课")
            }

            view.shuxuezb.setOnClickListener{
                joinClass("数学课")
            }

            view.tiyuzb.setOnClickListener{
                LiveActivity.studentStart(visitActivity(),MessageDigestUtils.md5(UTime.nowDateTime(YMD)+TIYU))

            }
        }

    }

    fun joinClass(roomName:String){
        showProgressingDialog("加入房间中")
        // 用户名
        val userName = UUser.getName()!!
// 用户 ID，需要与你生成 RTM Token 时使用的用户 ID 一致
        val userUuid = UUser.getName()!!
// 教室名称
        val roomName = roomName
// 教室 ID
        val roomUuid = MessageDigestUtils.md5(UTime.nowDateTime(YMD)+roomName)
// 用户角色
        val roleType = AgoraEduRoleType.AgoraEduRoleTypeStudent.value
// 课堂类型
        val roomType = AgoraEduRoomType.AgoraEduRoomTypeBig.value
//        val roomType =
//            AgoraEduRoomType.AgoraEduRoomType1V1.value / AgoraEduRoomType.AgoraEduRoomTypeSmall.value / AgoraEduRoomType.AgoraEduRoomTypeBig.value
// RTM Token
//            val rtmToken = "006287029cb003a4563ac8426b5785da676IABIGqW7pIVHiClwUuxQZTFic/exiJVqeO7LHR0QIICGgydFBmYAAAAAEAAnBbYm2u+tYAEA6APa761g"

        val rtmToken =  RtmTokenBuilder().buildToken(appId, appCe, userUuid,
            RtmTokenBuilder.Role.Rtm_User, 0)
//


// 课堂开始时间，单位为毫秒，以第一个进入教室的用户传入的参数为准
        val startTime = System.currentTimeMillis() + 100
// 课堂持续时间，单位为秒，以第一个进入教室的用户传入的参数为准
        val duration = 86400L
// 课堂所在区域，各客户端的区域必须一致，否则无法互通。
        val region: String = AgoraEduRegionStr.cn

        val agoraEduLaunchConfig = AgoraEduLaunchConfig(
            userName,
            userUuid,
            roomName,
            roomUuid,
            roleType,
            roomType,
            rtmToken,
            startTime,
            duration,
            region
        )
        val classRoom = AgoraEduSDK.launch(getApplicationContext()!!, agoraEduLaunchConfig) { state: AgoraEduEvent ->
            ULog.e( "launch-课堂状态:" + state.name)
            dismissProgressDialog()
        }
    }

    override fun onFragmentResume() {
        super.onFragmentResume()
        setTitle("课程表")
    }

}
