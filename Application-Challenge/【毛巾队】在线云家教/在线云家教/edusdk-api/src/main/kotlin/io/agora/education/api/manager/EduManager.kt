package io.agora.education.api.manager

import io.agora.education.api.BuildConfig
import io.agora.education.api.EduCallback
import io.agora.education.api.logger.DebugItem
import io.agora.education.api.logger.LogLevel
import io.agora.education.api.manager.listener.EduManagerEventListener
import io.agora.education.api.room.EduRoom
import io.agora.education.api.room.data.EduError
import io.agora.education.api.room.data.RoomCreateOptions
import io.agora.rtc.RtcEngine
import io.agora.rte.RteEngineImpl
import java.util.*
import kotlin.reflect.typeOf

abstract class EduManager(
        val options: EduManagerOptions
) {
    companion object {
        val TAG = EduManager::class.java.simpleName

        @JvmStatic
        fun init(options: EduManagerOptions, callback: EduCallback<EduManager>) {
            val cla = Class.forName("io.agora.education.impl.manager.EduManagerImpl")
            val eduManager = cla.getConstructor(EduManagerOptions::class.java).newInstance(options) as EduManager
            val methods = cla.methods
            val iterator = methods.iterator()
            while (iterator.hasNext()) {
                val element = iterator.next()
                if (element.name == "login") {
                    element.invoke(eduManager, options.userUuid, object : EduCallback<Unit> {
                        override fun onSuccess(res: Unit?) {
                            callback.onSuccess(eduManager)
                        }

                        override fun onFailure(code: Int, reason: String?) {
                            callback.onFailure(code, reason)
                        }
                    })
                }
            }
        }

        fun version(): String {
            return RtcEngine.getSdkVersion().plus(".").plus(BuildConfig.SDK_VERSION)
        }
    }

    var eduManagerEventListener: EduManagerEventListener? = null

    abstract fun createClassroom(config: RoomCreateOptions): EduRoom

    abstract fun release()

    abstract fun logMessage(message: String, level: LogLevel): EduError

    /**日志上传之后，会通过回调把serialNumber返回
     * serialNumber：日志序列号，可以用于查询日志*/
    abstract fun uploadDebugItem(item: DebugItem, callback: EduCallback<String>): EduError
}
