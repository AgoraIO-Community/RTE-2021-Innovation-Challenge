package com.kangraoo.basektlib.tools.tip

import com.kangraoo.basektlib.R

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc :
 */
class TipConfig(
    var errorBackGroundColor: Int,
    var errorIcon: Int,
    var infoBackGroundColor: Int,
    var infoIcon: Int,
    var warningBackGroundColor: Int,
    var warningIcon: Int,
    var successBackGroundColor: Int,
    var successIcon: Int,
    var normalBackGroundColor: Int,
    var normalIcon: Int
) {

    private constructor(builder: Builder) : this(builder.errorBackGroundColor, builder.errorIcon,
        builder.infoBackGroundColor, builder.infoIcon, builder.warningBackGroundColor, builder.warningIcon,
    builder.successBackGroundColor, builder.successIcon, builder.normalBackGroundColor, builder.normalIcon)

    class Builder {
        var errorBackGroundColor = R.color.libToastErrorColor

        var errorIcon = R.drawable.lib_toast_clear_white_24dp

        var infoBackGroundColor = R.color.libToastInfoColor

        var infoIcon = R.drawable.lib_toast_info_outline_white_24dp

        var warningBackGroundColor = R.color.libToastWarningColor

        var warningIcon = R.drawable.lib_toast_info_outline_white_24dp

        var successBackGroundColor = R.color.libToastSuccessColor

        var successIcon = R.drawable.lib_toast_success_white_24dp

        var normalBackGroundColor = 0

        var normalIcon = 0

        fun build() = TipConfig(this)
    }
    companion object {
        @JvmStatic inline fun build(block: Builder.() -> Unit) = Builder().apply(block).build()
    }
}
