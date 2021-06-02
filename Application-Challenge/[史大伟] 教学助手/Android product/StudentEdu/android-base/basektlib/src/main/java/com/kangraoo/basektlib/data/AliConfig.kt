package com.kangraoo.basektlib.data

/**
 * @author shidawei
 * 创建日期：2021/2/26
 * 描述：
 */
class AliConfig(var appKey: String? = null,
                       var rsaPublicKey: String? = null,
                       var appSecret: String? = null,var apm : Boolean,var tlog : Boolean,var carsh : Boolean){
    constructor(builder: Builder) : this(builder.appKey, builder.rsaPublicKey, builder.appSecret,builder.apm,builder.tlog,builder.crash)

    class Builder {
        var appKey: String? = null

        var rsaPublicKey: String? = null

        var appSecret: String? = null

        var apm : Boolean = false

        var tlog : Boolean = false

        var crash : Boolean = false

        fun build(): AliConfig = AliConfig(this)
    }

    companion object {
        @JvmStatic fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }
}