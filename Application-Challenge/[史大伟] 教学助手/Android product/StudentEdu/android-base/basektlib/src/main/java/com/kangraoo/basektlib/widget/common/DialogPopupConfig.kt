package com.kangraoo.basektlib.widget.common

import android.view.ViewGroup
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi.getDialogWidth

/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/08/29
 * desc :
 * version: 1.0
 */
class DialogPopupConfig(val windowWidth: Int, val windowHeight: Int, val isDarkModel: Boolean, var enableOutsideTouch: Boolean, val animationStyle: Int, val cancelable: Boolean) {

    constructor(builder: Builder) : this(builder.width, builder.height, builder.dark, builder.enableOutsideTouch, builder.animationStyle, builder.cancelable)

    class Builder {
        var width = getDialogWidth(SApplication.context())
        var height = ViewGroup.LayoutParams.WRAP_CONTENT
        var dark = true
        var enableOutsideTouch = true
        var animationStyle = 0
        var cancelable = true
        fun build(): DialogPopupConfig {
            return DialogPopupConfig(this)
        }
    }

    companion object {
        @JvmStatic inline fun build(block: DialogPopupConfig.Builder.() -> Unit) = DialogPopupConfig.Builder().apply(block).build()
    }
}
