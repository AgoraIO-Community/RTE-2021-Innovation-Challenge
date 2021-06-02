package com.kangaroo.studentedu.app

import android.content.Context
import androidx.multidex.MultiDex
import com.hyphenate.EMConnectionListener
import com.hyphenate.EMError
import com.hyphenate.chat.EMClient
import com.hyphenate.chat.EMOptions
import com.hyphenate.easeui.EaseIM
import com.kangaroo.openlive.AgoraApplication
import com.kangaroo.openlive.activities.BaseActivity
import com.kangaroo.studentedu.BuildConfig
import com.kangaroo.studentedu.R
import com.kangaroo.studentedu.tools.UUser
import com.kangaroo.studentedu.ui.activity.LoginActivity
import com.kangraoo.basektlib.app.ActivityLifeManager
import com.kangraoo.basektlib.app.BaseLibActivityLifecycleCallbacks
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.app.SConfiger
import com.kangraoo.basektlib.app.init.DownLoadInit
import com.kangraoo.basektlib.app.init.IInit
import com.kangraoo.basektlib.tools.log.LogConfig
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.qdedu.baselibcommon.app.init.ArouterInit
import io.agora.edu.launch.AgoraEduSDK
import io.agora.edu.launch.AgoraEduSDKConfig


const val LOAD_NEW_PATH = "loadNewPatch"
const val LOAD_NEW_PATH_TIME = 4320000L
const val appId = "287029cb003a4563ac8426b5785da676"
const val appCe = "f1f0e078948549a9840dbcf3bba074a8"

class App : SApplication() {
    override fun configer(): SConfiger = SConfiger.build {
        //crash 采用bugly
        logConfig = LogConfig.build {
            tag = "APP"
        }
        appSafeCode = "APP_CODE_123456"
        consoleOutwindow = false
        applog = R.mipmap.ic_launcher
        debugStatic = BuildConfig.IS_DEV
    }



    override fun init() {
        //EaseIM初始化
        if(EaseIM.getInstance().init(this, EMOptions())){
            //在做打包混淆时，关闭debug模式，避免消耗不必要的资源
            EMClient.getInstance().setDebugMode(true);
            //EaseIM初始化成功之后再去调用注册消息监听的代码 ...

            EMClient.getInstance().addConnectionListener(object : EMConnectionListener{
                override fun onConnected() {

                }

                override fun onDisconnected(errorCode: Int) {
                    when (errorCode) {
                        EMError.USER_REMOVED -> {
                        }
                        EMError.USER_LOGIN_ANOTHER_DEVICE -> {
                            TipToast.tip(Tip.Warning,"账号在其他设备登录，您被挤掉线了哦")
                            UUser.logout()
                            var activity = ActivityLifeManager.getCurrentActivity()
                            ActivityLifeManager.finishAllActivity()
                            activity?.let {
                                LoginActivity.startFrom(it)
                            }
                        }
                        EMError.SERVER_SERVICE_RESTRICTED -> {
                        }
                        EMError.USER_KICKED_BY_CHANGE_PASSWORD -> {
                        }
                        EMError.USER_KICKED_BY_OTHER_DEVICE -> {
                        }
                    }
                }

            })

        }
        /** 进行全局配置 */
// Agora App ID
// 是否开启护眼模式
        val eyeCare = 0
        AgoraEduSDK.setConfig(AgoraEduSDKConfig(appId, eyeCare))
        agora = AgoraApplication()
        agora!!.appId = appId
        agora!!.appCe = appCe
        BaseActivity.agoraApplication = agora
        agora?.init()
    }

    override fun onTerminate() {
        super.onTerminate()
        agora?.onTerminate()
    }

    var agora : AgoraApplication? = null

    override fun appInit(): IInit {
        return AppInit(ArouterInit(DownLoadInit(super.appInit())))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        /**
         * 加入热修复后提前到热修复的application中
         */
        MultiDex.install(this)
    }

    override fun getActivityLifecycleCallbacks(): MutableList<ActivityLifecycleCallbacks> {
        var list = super.getActivityLifecycleCallbacks()
        list.add(BaseLibActivityLifecycleCallbacks())
        return list
    }

}
