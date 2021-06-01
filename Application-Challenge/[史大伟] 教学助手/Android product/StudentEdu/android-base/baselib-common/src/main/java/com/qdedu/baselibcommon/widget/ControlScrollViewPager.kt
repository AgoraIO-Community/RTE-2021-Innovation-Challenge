package com.qdedu.baselibcommon.widget

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.viewpager.widget.ViewPager

/**
 * 禁止滑动ViewPager.
 */
class ControlScrollViewPager : ViewPager {

    private var isCanScroll = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    fun setScanScroll(isCanScroll: Boolean) {
        this.isCanScroll = isCanScroll
    }


    override fun onInterceptTouchEvent(event: MotionEvent): Boolean {
        return false
    }


    override fun onTouchEvent(ev: MotionEvent): Boolean {
        return false
    }
}
