package com.kangraoo.basektlib.tools.crash

import android.os.Looper
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.data.model.SExpParceModel
import com.kangraoo.basektlib.tools.SSysStore
import com.kangraoo.basektlib.tools.UClear
import com.kangraoo.basektlib.tools.crash.CrashSaver.save
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.task.TaskManager
import com.kangraoo.basektlib.tools.tip.Tip

/**
 * Created by shidawei on 16/8/8.
 */
const val SYS_CRASH_NUM = "sysCrashNum"
const val SYS_CRASH_NUM_TIME = 60000L
class AndroidCrash private constructor() {

    companion object {
        val instance: AndroidCrash by lazy {
            AndroidCrash()
        }
    }
    private var mDefaultCrashHandler: Thread.UncaughtExceptionHandler

    fun saveException(ex: Throwable?, uncaught: Boolean) {
        save(SApplication.context(), ex!!, uncaught)
    }

    fun setUncaughtExceptionHandler(handler: Thread.UncaughtExceptionHandler?) {
        if (handler != null) {
            mDefaultCrashHandler = handler
        }
    }

    /**
     * 进行弹出框提示
     *
     * @param msg
     */
    private fun showToast(msg: String) {
        TaskManager.taskExecutor.execute(Runnable {
            Looper.prepare()
            Tip.Error.tip(msg).show()
            Looper.loop()
        })
    }



    init {
        // get default
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler()

        // install
        Thread.setDefaultUncaughtExceptionHandler { thread, ex -> // save log
            if (SApplication.instance().sConfiger.debugStatic) {
                saveException(ex, true)
                showToast(
                    SApplication.instance().getString(R.string.libExitErrorMessage)
                        .toString() + ".error:" + ex.message
                )
            } else {
                /**
                 * 容灾方案：1分钟内奔溃超过3次则进行清空数据，只在生产环境集成
                 */
                val extKey = SSysStore.instance.safeMmkvStore.get(SYS_CRASH_NUM,null, SExpParceModel::class.java)
                var num = 1
                if(extKey!=null){
                    ULog.d("extKey","不为空")
                    if(!extKey.isExpire()){
                        ULog.d("extKey","没超时")
                        num = extKey.key?.toInt()?:0
                        if(num>=3){
                            ULog.d("num","大于等于3次")
                            num = 1
                            UClear.cleanApplicationData(SApplication.context())
                        }else{
                            num +=1
                        }
                    }
                }

                SSysStore.instance.safeMmkvStore.put(
                    SYS_CRASH_NUM,
                    SExpParceModel(num.toString(),SYS_CRASH_NUM_TIME)
                )
                showToast(SApplication.instance().getString(R.string.libExitErrorMessage))
            }

            ULog.e(ex)
            // uncaught
            try {
                Thread.sleep(3500)
            } catch (e: InterruptedException) {
                ULog.e(e, e.message)
            }
            mDefaultCrashHandler.uncaughtException(thread, ex)
        }
    }
}
