package com.kangraoo.basektlib.widget.dialog

import android.content.DialogInterface

interface OnLibDialogListener {
    fun onShow()
    fun onDismiss(dialog: DialogInterface)
}
