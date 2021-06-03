package com.game.tingshuo.util

import androidx.fragment.app.FragmentActivity
import com.blankj.utilcode.util.ToastUtils
import com.tbruyelle.rxpermissions3.RxPermissions

object RxPermissionsUtil {

    //request permissions
    fun requestPermission( context:FragmentActivity, vararg permissions:String,onGranted:()->Unit,onDined:()->Unit){
        var rxpermissions = RxPermissions(context)
        rxpermissions.request(*permissions).subscribe{ granted ->
            if (granted) {
                onGranted()
            } else {
                onDined()
            }
        }
    }

    fun requestPermission( context:FragmentActivity, vararg permissions:String,onGranted:()->Unit){
        var rxpermissions = RxPermissions(context)
        rxpermissions.request(*permissions).subscribe{ granted ->
            if (granted) {
                onGranted()
            } else {
                ToastUtils.showShort("please grant the permission")
            }
        }
    }

}