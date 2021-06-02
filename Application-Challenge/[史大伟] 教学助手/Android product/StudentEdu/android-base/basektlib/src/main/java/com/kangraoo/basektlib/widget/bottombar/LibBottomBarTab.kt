package com.kangraoo.basektlib.widget.bottombar

import android.content.Context
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.tools.UFont
import com.kangraoo.basektlib.tools.UUi

sealed class LibBottomBarTab(context: Context, mBottomBarConfig: BottomBarConfig) : BaseBottomBarTab(context) {
    var colors: IntArray = intArrayOf(ContextCompat.getColor(getContext(), mBottomBarConfig.bottomBarUnSelect), ContextCompat.getColor(getContext(), mBottomBarConfig.bottomBarSelect))

    open fun setIcon(icon: Int): BaseBottomBarTab {
        return this
    }

    override fun setSelected(selected: Boolean) {
        super.setSelected(selected)
        if (selected) {
            select()
        } else {
            unSelect()
        }
    }

    protected abstract fun unSelect()

    protected abstract fun select()

    class ImageSelectBottomBarTab(context: Context, val iconSelect: Int, val iconUnselect: Int, val iconText: Int, mBottomBarConfig: BottomBarConfig) : LibBottomBarTab(context, mBottomBarConfig) {

        private val mImageIcon: ImageView
        private val mTextIcon: TextView

        init {
            val view = LayoutInflater.from(context).inflate(R.layout.lib_bottom_bar_image_select, null)
            mImageIcon = view.findViewById(R.id.bottom_image)
            mTextIcon = view.findViewById(R.id.bottom_text)
            val size: Int = UUi.dp2px(context, mBottomBarConfig.bottomBarImgSize.toFloat())
//            val textSize: Int = UUi.dp2px(context,mBottomBarConfig.bottomBarTextSize.toFloat())
//            val lParams = view.layoutParams as FrameLayout.LayoutParams
//            lParams.gravity = Gravity.CENTER

//            view.layoutParams = lParams

            val params = mImageIcon.layoutParams
            params.height = size
            params.width = size
            mImageIcon.layoutParams = params

            mImageIcon.setImageResource(iconUnselect)
            mTextIcon.setTextSize(TypedValue.COMPLEX_UNIT_SP, mBottomBarConfig.bottomBarTextSize.toFloat())
            mTextIcon.text = context.getString(iconText)
            addView(view)
        }

        override fun unSelect() {
            mImageIcon.setImageResource(iconUnselect)
            mTextIcon.setTextColor(colors[0])
        }

        override fun select() {
            mImageIcon.setImageResource(iconSelect)
            mTextIcon.setTextColor(colors[1])
        }
    }

    class ImageBottomBarTab(context: Context, mBottomBarConfig: BottomBarConfig) : LibBottomBarTab(context, mBottomBarConfig) {

        private var mImageIcon: ImageView = ImageView(context)

        init {
            val size: Int = UUi.dp2px(context, mBottomBarConfig.bottomBarImgSize.toFloat())
            val params = LayoutParams(size, size)
            params.gravity = Gravity.CENTER
            mImageIcon.layoutParams = params
            mImageIcon.setColorFilter(colors[0])
            addView(mImageIcon)
        }

        override fun setIcon(icon: Int): BaseBottomBarTab {
            mImageIcon.setImageResource(icon)
            return this
        }

        override fun unSelect() {
            mImageIcon.setColorFilter(colors[0])
        }

        override fun select() {
            mImageIcon.setColorFilter(colors[1])
        }
    }

    class FontBottomBarTab(context: Context, mBottomBarConfig: BottomBarConfig) : LibBottomBarTab(context, mBottomBarConfig) {

        private var fIcon = TextView(context)

        init {
            val params = LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            params.gravity = Gravity.CENTER
            fIcon.gravity = Gravity.CENTER
            fIcon.textSize = mBottomBarConfig.bottomBarFontSize.toFloat()
            fIcon.layoutParams = params
            fIcon.setTextColor(colors[0])
            addView(fIcon)
        }

        override fun setIcon(icon: Int): BaseBottomBarTab {
            UFont.setTextViewFont(fIcon, icon)
            return this
        }

        override fun unSelect() {
            fIcon.setTextColor(colors[0])
        }

        override fun select() {
            fIcon.setTextColor(colors[1])
        }
    }
}
