package com.kangraoo.basektlib.tools.text

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import com.kangraoo.basektlib.tools.HString

/**
 * EditText字符数限制 包含ASCII处理
 * Created by hzxuwen on 2015/5/22.
 */
class StringTextWatcher(private val length: Int, private val editText: EditText) : TextWatcher {
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    override fun afterTextChanged(s: Editable) {
        var editEnd = editText.selectionEnd
        editText.removeTextChangedListener(this)
        while (HString.counterChars(s.toString()) > length && editEnd > 0) {
            s.delete(editEnd - 1, editEnd)
            editEnd--
        }
        editText.setSelection(editEnd)
        editText.addTextChangedListener(this)
    }
}
