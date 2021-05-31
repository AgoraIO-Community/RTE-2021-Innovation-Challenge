package com.kangraoo.basektlib.tools.log

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/16
 * desc :
 */
class LogConfig(
    var showThreadInfo: Boolean,
    var methodCount: Int,
    var methodOffset: Int,
    var tag: String
) {

    constructor(builder: Builder) : this(builder.showThreadInfo, builder.methodCount, builder.methodOffset, builder.tag)

    class Builder {
        var showThreadInfo = false

        var methodCount = 2

        var methodOffset = 2

        var tag = "LIB-LOG"

        fun build(): LogConfig = LogConfig(this)
    }

    companion object {
        @JvmStatic fun build(block: LogConfig.Builder.() -> Unit) = LogConfig.Builder().apply(block).build()
    }
}
