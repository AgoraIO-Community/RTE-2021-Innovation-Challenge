package com.kangraoo.basektlib.tools

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

object UClipboard {
    /**
     * 拷贝文字到剪贴版
     * @param context
     * @param text 内容
     */
    fun clipboardCopyText(context: Context, text: CharSequence?) {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = ClipData.newPlainText("Label", text)
        cm.setPrimaryClip(clipData)
    }

    /**
     * 获取剪贴版里的文字长度
     * @param context
     * @return
     */
    fun clipboardTextLength(context: Context): Int {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = cm.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val text = clipData.getItemAt(0).text
            return text?.length ?: 0
        }
        return 0
    }

    /**
     * 获取剪贴版里的文字
     * @param context
     * @return
     */
    fun clipboardText(context: Context): String? {
        val cm = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clipData = cm.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            val text = clipData.getItemAt(0).text
            if (text != null) return text.toString()
        }
        return null
    }
}
