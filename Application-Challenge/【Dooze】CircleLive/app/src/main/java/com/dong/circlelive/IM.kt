package com.dong.circlelive

import android.content.Context
import android.content.Intent
import android.os.Process
import cn.leancloud.AVUser
import com.dong.circlelive.base.Timber
import com.dong.circlelive.login.LoggedInUserView
import com.dong.circlelive.login.LogicActivity
import com.dong.circlelive.login.LoginResult
import com.dong.circlelive.model.emUsername
import com.hyphenate.EMCallBack
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

/**
 * Create by dooze on 5/3/21  7:34 PM
 * Email: stonelavender@hotmail.com
 * Description:
 */
class IM {

    private fun onUserException(exception: String) {
        Timber.e(TAG) { "onUserException: $exception" }
        val intent = Intent(appContext, LogicActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        intent.addFlags(Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED)
        intent.putExtra(exception, true)
        appContext.startActivity(intent)
    }

    fun setupListener() {
        emClient.addConnectionListener(object : EMConnectionListener {
            override fun onConnected() {

            }

            override fun onDisconnected(errorCode: Int) {
                when (errorCode) {
                    EMError.USER_REMOVED -> {
                        onUserException(getString(R.string.im_user_removed))
                    }
                    EMError.USER_LOGIN_ANOTHER_DEVICE -> {
                        onUserException(getString(R.string.im_user_conflict))
                    }
                    EMError.SERVER_SERVICE_RESTRICTED -> {
                        onUserException(getString(R.string.im_user_restricted))
                    }
                    EMError.USER_KICKED_BY_CHANGE_PASSWORD -> {
                        onUserException(getString(R.string.im_user_password_changed))
                    }
                    EMError.USER_KICKED_BY_OTHER_DEVICE -> {
                        onUserException(getString(R.string.im_user_kick_by_other_devices))
                    }
                }
            }
        })


    }

    companion object {

        const val TAG = "IM"

        fun init(context: Context) {
            val pid = Process.myPid()
            val processAppName = getAppName(context, pid)
            if (processAppName == null || !processAppName.equals(context.packageName, ignoreCase = true)) {
                // 则此application::onCreate 是被service 调用的，直接返回
                return
            }

            val options = EMOptions()
            // 默认添加好友时，是不需要验证的，改成需要验证
            options.acceptInvitationAlways = false
            // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
            options.autoTransferMessageAttachments = true
            // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
            options.setAutoDownloadThumbnail(true)
            //初始化
            emClient.init(context, options)
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            emClient.setDebugMode(true)
        }
    }
}

val emClient: EMClient
    get() = EMClient.getInstance()

suspend fun IM.Companion.login(avUser: AVUser) = suspendCancellableCoroutine<LoginResult> { ucont ->
    val username = avUser.emUsername
    val password = avUser.password
    emClient.login(username, password, object : EMCallBack {
        override fun onSuccess() {
            emClient.groupManager().loadAllGroups()
            emClient.chatManager().loadAllConversations()
            ucont.resume(LoginResult(success = LoggedInUserView(displayName = emClient.currentUser)))
        }

        override fun onError(code: Int, error: String?) {
            ucont.resume(LoginResult(error = error ?: code.toString(), code = code))
        }

        override fun onProgress(progress: Int, status: String?) {
        }
    })
}
