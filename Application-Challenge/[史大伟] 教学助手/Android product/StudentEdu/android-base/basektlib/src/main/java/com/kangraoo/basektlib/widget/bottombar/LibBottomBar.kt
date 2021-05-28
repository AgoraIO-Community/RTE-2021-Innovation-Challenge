package com.kangraoo.basektlib.widget.bottombar

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import com.kangraoo.basektlib.widget.listener.OnOnceClickListener

/**
 * Created by shidawei on 16/8/10.
 */
class LibBottomBar @JvmOverloads constructor(context: Context, attributeSet: AttributeSet? = null, defStyleAttr: Int = 0) : LinearLayout(context, attributeSet, defStyleAttr) {
    private var mTabLayout: LinearLayout
    private var mTabParams: LayoutParams
    private var mCurrentPosition = 0

    init {
        orientation = VERTICAL
        mTabLayout = LinearLayout(context)
        mTabLayout.setBackgroundColor(Color.WHITE)
        mTabLayout.orientation = HORIZONTAL
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mTabLayout.layoutParams = lp
        addView(mTabLayout)
        mTabParams = LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT)
        mTabParams.weight = 1f
    }

    fun addItem(tab: BaseBottomBarTab): LibBottomBar {
        tab.setOnClickListener(object : OnOnceClickListener() {
            override fun onOnceClick(view: View) {
                val position = tab.tabPosition
                if (mCurrentPosition != position) {
                    if (onClickItemMenu != null) {
                        onClickItemMenu!!.onClickItem(mCurrentPosition, position)
                    }
                    tab.isSelected = true
                    mTabLayout.getChildAt(mCurrentPosition).isSelected = false
                    mCurrentPosition = position
                }
            }
        })
        tab.tabPosition = mTabLayout.childCount
        if (mTabLayout.childCount == mCurrentPosition) {
            tab.isSelected = true
        }
        tab.layoutParams = mTabParams
        mTabLayout.addView(tab)
        return this
    }

    /**
     * 第一次进来是展示哪个
     */
    fun setCurrentPosition(mCurrentPosition: Int) {
        this.mCurrentPosition = mCurrentPosition
    }

    /**
     * 直接跳转某个item
     * @param position
     */
    fun setCurrentItem(position: Int) {
        mTabLayout.post { mTabLayout.getChildAt(position).performClick() }
    }

    var onClickItemMenu: OnClickItemMenu? = null

    interface OnClickItemMenu {
        fun onClickItem(nowPosition: Int, position: Int)
    }

    fun getmCurrentPosition(): Int {
        return mCurrentPosition
    }
}
