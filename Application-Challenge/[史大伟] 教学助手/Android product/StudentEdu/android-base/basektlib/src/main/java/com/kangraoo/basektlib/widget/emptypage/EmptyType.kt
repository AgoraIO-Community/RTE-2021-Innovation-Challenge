package com.kangraoo.basektlib.widget.emptypage

import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication

sealed class EmptyType(var message: String, var image: Int, var button: Boolean) {

    class DiyPageType(message: String, image: Int, button: Boolean) : EmptyType(message, image, button)

    class DiyImgPageType(message: String, image: Int, button: Boolean) : EmptyType(message, image, button)

    class EmptyPageType : EmptyType(SApplication.context().getString(R.string.libEmptyPageNoData), R.string.lib_icon_confused, false)

    class NetWorkErrorType : EmptyType(SApplication.context().getString(R.string.libEmptyPageNetError), R.string.lib_icon_crying, true)
}
