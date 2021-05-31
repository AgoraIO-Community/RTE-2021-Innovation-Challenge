package com.qgmodel.qggame.utils;

import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

/**
 * Created by HeYanLe on 2020/7/31 0031 14:27.
 * https://github.com/heyanLE
 */
public class ViewUtils {

    public static void setTitleCenter(Toolbar toolbar) {
        String title = "title";
        final CharSequence originalTitle = toolbar.getTitle();
        toolbar.setTitle(title);
        for (int i = 0; i < toolbar.getChildCount(); i++) {
            View view = toolbar.getChildAt(i);
            if (view instanceof TextView) {
                TextView textView = (TextView) view;
                if (title.contentEquals(textView.getText())) {
                    textView.setGravity(Gravity.CENTER);
                    Toolbar.LayoutParams params = new Toolbar.LayoutParams(Toolbar.LayoutParams.WRAP_CONTENT, Toolbar.LayoutParams.MATCH_PARENT);
                    params.gravity = Gravity.CENTER;
                    textView.setLayoutParams(params);
                }
            }
            toolbar.setTitle(originalTitle);
        }
    }

}
