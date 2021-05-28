package com.kangraoo.basektlib.widget.emptypage

import android.content.Context
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.annotation.ColorRes
import androidx.annotation.DrawableRes

/**
 * 使用方法
 * emptyPage.setOnRefreshDelegate(new EmptyPageLayout.OnRefreshDelegate() {
 * @Override
 * public void onRefresh() {
 *
 * }
 * }); *
 *
 * emptyPage.setEmptyType(empty);
 * Created by 王二蛋 on 16/5/6.
 */
class EmptyPageLayout(context: Context, var mIemptyPage: IEmptyPage) : LinearLayout(context) {

    init {
        val view = LayoutInflater.from(context).inflate(mIemptyPage.layoutId, this, true)
        mIemptyPage.init(view)
    }

    fun setEmptyType(type: EmptyType) {
        mIemptyPage.setEmptyType(type)
    }

    fun buttonStyle(@DrawableRes background: Int, @ColorRes textColor: Int) {
        mIemptyPage.buttonStyle(background, textColor)
    }

    fun buttonText(text: String) {
        mIemptyPage.buttonText(text)
    }

    fun setButtonClickListener(onRefreshDelegate: AbsEmptyPage.OnRefreshDelegate) {
        mIemptyPage.setButtonClickListener(onRefreshDelegate)
    }
}
