package org.lql.movie_together.ui.face.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 *  * 在间隔处加一个标识符
 *  * 
 *  * @author wzw
 *  *
 *  
 */
@SuppressLint("AppCompatCustomView")
public class IntervalAddTagEditText extends EditText {

    /**
     *  * 位数
     *  
     */
    private int unit = 4;
    /**
     *  * 标识符
     *  
     */
    private String tag = "-";

    public IntervalAddTagEditText(Context context) {
        super(context);
        init();
    }

    public IntervalAddTagEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    void init() {
        addTextChangedListener(new FormatTextWatcher());
    }

    class FormatTextWatcher implements TextWatcher {

        int beforeTextLength = 0;
        int afterTextLength = 0;
        /**
         *  * 记录光标的位置
         *  
         */
        int location = 0;
        /**
         *  * 是否更换中
         *  
         */
        boolean isChanging = false;

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            beforeTextLength = s.length();
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void

        afterTextChanged(Editable s) {
            afterTextLength = s.length();
            if (isChanging) {
                return;
            }
            /**
             *  * 字符增加
             *  
             */
            if (beforeTextLength < afterTextLength) {
                location = getSelectionEnd();
                setFormatText(s.toString());
                if (location % (unit + 1) == 0) {
                    setSelection(getLocation(location + 1));
                } else {
                    setSelection(getLocation(location));
                }
                /**
                 *  * 字符减少
                 *  
                 */
            } else if (beforeTextLength > afterTextLength) {
                location = getSelectionEnd();
                setFormatText(s.toString());
                if (location % (unit + 1) == 0) {
                    setSelection(getLocation(location - 1));
                } else {
                    setSelection(getLocation(location));
                }
            }
        }

        private int getLocation(int location) {
            if (location < 0) {
                return 0;
            }
            if (location > afterTextLength) {
                return afterTextLength;
            }
            return location;
        }

        private void setFormatText(String str) {
            isChanging = true;
            setText(addTag(str));
            isChanging = false;
        }

        /**
         *  * 加上标识符
         *  * @param str
         *  * @return
         *  
         */
        private String addTag(String str) {
            str = replaceTag(str);
            StringBuilder sb = new StringBuilder();
            int index = 0;
            int strLength = str.length();
            while (index < strLength) {
                int increment = index + unit;
                sb.append(str.subSequence(index, index = (increment > strLength) ? strLength : increment));
                if (increment < strLength) {
                    sb.append(tag);
                }
            }
            return sb.toString();
        }
    }

    /**
     *  * 清除标识符
     *  * @param str
     *  * @return
     *  
     */
    public String replaceTag(String str) {
        if (str.indexOf(tag) != -1) {
            str = str.replace(tag, "");
        }
        return str;
    }

    @Override
    public String toString() {
        return replaceTag(getText().toString());
    }
}
