package com.kangraoo.basektlib.app

import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.data.AliConfig
import com.kangraoo.basektlib.tools.log.LogConfig
import com.kangraoo.basektlib.tools.okhttp.OkHttpConfig
import com.kangraoo.basektlib.tools.task.TaskConfig
import com.kangraoo.basektlib.tools.tip.TipConfig

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/08
 * desc :
 */
class SConfiger(
    var tipConfig: TipConfig,
    var logConfig: LogConfig,
    /**
     * 调试模式
     */
    var debugStatic: Boolean,
    var appSafeCode: String,
    var taskConfig: TaskConfig,
    /**
     * 存储根目录
     */
    var storageRoot: String?,
    /**
     * 文件缓存版本号（为空的话，默认同app版本号）
     */
    var diskCacheVersion: Int?,

    var okHttpConfig: OkHttpConfig,
    /**
     * 框架sharePreferences存储名称
     */
    var sysSharedpreferences: String,
    /**
     * console在应用外显示
     */
    var consoleOutwindow: Boolean,

    var applogo: Int,
    /**
     * 框架sharePreferences存储名称
     */
    var appSharedpreferences: String,
    /**
     * dns阿里云解析账户
     */
    var dnsAliEnable: Boolean,
    /**
     * 阿里配置
     */
    var aliConfig: AliConfig,

    /**
     * 定义项目宽高
     */
    var width: Int,
    var height : Int
) {

    private constructor(builder: Builder) : this(
        builder.tipConfig,
        builder.logConfig,
        builder.debugStatic,
        builder.appSafeCode,
        builder.taskConfig,
        builder.storageRoot,
        builder.diskCacheVersion,
        builder.okHttpConfig,
        builder.sysSharedpreferences,
        builder.consoleOutwindow,
        builder.applog,
        builder.appSharedpreferences,
        builder.dnsAliEnable,
        builder.aliConfig,
        builder.width,
        builder.height
    )

    class Builder {
        var tipConfig: TipConfig = TipConfig.build { }
        var logConfig = LogConfig.build { }
        var debugStatic: Boolean = true
        var appSafeCode: String = "DEFAULT_123456789"
        var sysSharedpreferences = "SYS_SHAREDPREFERENCES"
        var taskConfig = TaskConfig.build { }
        var storageRoot: String? = null
        var diskCacheVersion: Int? = null
        var okHttpConfig: OkHttpConfig = OkHttpConfig.build { }
        var consoleOutwindow = false
        var applog = R.drawable.ic_pages_lib_icon_100dp
        var appSharedpreferences = "APP_SHAREDPREFERENCES"
        var dnsAliEnable = false
        var aliConfig : AliConfig = AliConfig.build {  }

        /**
         * 默认宽高，以竖屏为基准
         */
        val height = 667
        val width = 375
        fun build() = SConfiger(this)
    }

    companion object {
        @JvmStatic inline fun build(block: SConfiger.Builder.() -> Unit) = SConfiger.Builder().apply(
            block
        ).build()
    }
}
