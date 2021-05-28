package com.kangraoo.basektlib.tools.okhttp

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2019/07/30
 * desc :
 */
class OkHttpConfig(
    var maxRetry: Int,
    var connectTimeout: Int,
    var writeTimeout: Int,
    var readTimeout: Int
) {

    constructor(builder: Builder) : this(builder.maxRetry, builder.connectTimeout, builder.writeTimeout, builder.readTimeout)

    class Builder {
        var connectTimeout = 10
        var writeTimeout = 30
        var readTimeout = 30
        var maxRetry = 1

        fun build() = OkHttpConfig(this)
    }
    companion object {
        @JvmStatic inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }
}
