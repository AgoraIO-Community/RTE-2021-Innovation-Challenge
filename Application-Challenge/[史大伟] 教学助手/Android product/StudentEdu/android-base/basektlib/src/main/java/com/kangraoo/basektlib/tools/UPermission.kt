package com.kangraoo.basektlib.tools

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.annotation.Size
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.tools.log.ULog
import com.tbruyelle.rxpermissions2.RxPermissions

object UPermission {
    fun requestPermission(requestPermission: RequestPermission, rxPermissions: RxPermissions, vararg permissions: String) {
        if (permissions.isEmpty()) {
            return
        }

        val needRequest: MutableList<String> =
            ArrayList()
        for (permission in permissions) { // 过滤调已经申请过的权限
            if (!rxPermissions.isGranted(permission)) {
                needRequest.add(permission)
            }
        }
        if (needRequest.isEmpty()) { // 全部权限都已经申请过，直接执行操作
            requestPermission.onRequestPermissionSuccess()
        } else { // 没有申请过,则开始申请
            rxPermissions.requestEach(*needRequest.toTypedArray())
                .buffer(needRequest.size)
                .subscribe {
                    val failurePermissions: MutableList<String> =
                        ArrayList()
                    val askNeverAgainPermissions: MutableList<String> =
                        ArrayList()
                    for (p in it) {
                        if (!p.granted) {
                            if (p.shouldShowRequestPermissionRationale) {
                                failurePermissions.add(p.name)
                            } else {
                                askNeverAgainPermissions.add(p.name)
                            }
                        }
                    }
                    if (failurePermissions.size > 0) {
                        ULog.d("Request permissions failure")
                        requestPermission.onRequestPermissionFailure(failurePermissions)
                    }
                    if (askNeverAgainPermissions.size > 0) {
                        ULog.d("Request permissions failure with ask never again")
                        requestPermission.onRequestPermissionFailureWithAskNeverAgain(
                            askNeverAgainPermissions
                        )
                    }
                    if (failurePermissions.size == 0 && askNeverAgainPermissions.size == 0) {
                        ULog.d("Request permissions success")
                        requestPermission.onRequestPermissionSuccess()
                    }
                }
        }
    }

    /**
     * 请求摄像头权限
     */
    fun launchCamera(
        requestPermission: RequestPermission,
        rxPermissions: RxPermissions
    ) {
        requestPermission(
            requestPermission,
            rxPermissions,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CAMERA
        )
    }

    /**
     * 请求外部存储的权限
     */
    fun externalStorage(
        requestPermission: RequestPermission,
        rxPermissions: RxPermissions
    ) {
        requestPermission(
            requestPermission,
            rxPermissions,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    }

    /**
     * 请求发送短信权限
     */
    fun sendSms(
        requestPermission: RequestPermission,
        rxPermissions: RxPermissions
    ) {
        requestPermission(
            requestPermission,
            rxPermissions,
            Manifest.permission.SEND_SMS
        )
    }

    /**
     * 请求打电话权限
     */
    fun callPhone(
        requestPermission: RequestPermission,
        rxPermissions: RxPermissions
    ) {
        requestPermission(
            requestPermission,
            rxPermissions,
            Manifest.permission.CALL_PHONE
        )
    }

    /**
     * 请求获取手机状态的权限
     */
    fun readPhonestate(
        requestPermission: RequestPermission,
        rxPermissions: RxPermissions
    ) {
        requestPermission(
            requestPermission,
            rxPermissions,
            Manifest.permission.READ_PHONE_STATE
        )
    }

    interface RequestPermission {
        /**
         * 权限请求成功
         */
        fun onRequestPermissionSuccess()

        /**
         * 用户拒绝了权限请求, 权限请求失败, 但还可以继续请求该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onRequestPermissionFailure(permissions: List<String>?)

        /**
         * 用户拒绝了权限请求并且用户选择了以后不再询问, 权限请求失败, 这时将不能继续请求该权限, 需要提示用户进入设置页面打开该权限
         *
         * @param permissions 请求失败的权限名
         */
        fun onRequestPermissionFailureWithAskNeverAgain(permissions: List<String>?)
    }

    private fun hasPermissions(context: Context, @Size(min = 1) vararg perms: String): Boolean {
        // Always return true for SDK < M, let the system deal with the permissions
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true
        }
        for (perm in perms) {
            if (ContextCompat.checkSelfPermission(context, perm) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun hasStoragePermission(context: Context): Boolean =
        hasPermissions(
            context,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )

    fun hasAudioPermission(context: Context): Boolean =
        hasPermissions(context, Manifest.permission.RECORD_AUDIO)
}
