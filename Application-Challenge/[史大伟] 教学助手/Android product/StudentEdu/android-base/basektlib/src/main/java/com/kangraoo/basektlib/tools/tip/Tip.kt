package com.kangraoo.basektlib.tools.tip

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.CheckResult
import androidx.annotation.ColorRes
import androidx.annotation.NonNull
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc :
 */
enum class Tip : ITipToast, ITipStyle {
    Normal {
        override fun getTipBackGroundColor(): Int {
            return tipConfig.normalBackGroundColor
        }

        override fun getIcon(): Int {
            return tipConfig.normalIcon
        }
    },
    Info {
        override fun getTipBackGroundColor(): Int {
            return tipConfig.infoBackGroundColor
        }

        override fun getIcon(): Int {
            return tipConfig.infoIcon
        }
    },
    Success {
        override fun getTipBackGroundColor(): Int {
            return tipConfig.successBackGroundColor
        }

        override fun getIcon(): Int {
            return tipConfig.successIcon
        }
    },
    Warning {
        override fun getTipBackGroundColor(): Int {
            return tipConfig.warningBackGroundColor
        }

        override fun getIcon(): Int {
            return tipConfig.warningIcon
        }
    },
    Error {
        override fun getTipBackGroundColor(): Int {
            return tipConfig.errorBackGroundColor;
        }

        override fun getIcon(): Int {
            return tipConfig.errorIcon;
        }
    };

    protected var tipConfig: TipConfig
    private var mContext: Context
    private var toastDrawable: GradientDrawable
    private var iconDrawable: Drawable? = null

    init {
        mContext = SApplication.context()
        tipConfig = SApplication.instance().sConfiger.tipConfig
        toastDrawable =
            ContextCompat.getDrawable(mContext, R.drawable.lib_toast_solid) as GradientDrawable
        if (getTipBackGroundColor() != 0) {
            val porterDuffColorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(mContext, getTipBackGroundColor()),
                PorterDuff.Mode.SRC_IN
            )
            toastDrawable.setColorFilter(porterDuffColorFilter)
        }
        if (getIcon() != 0) {
            iconDrawable = ContextCompat.getDrawable(mContext, getIcon())
            val porterDuffColorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(mContext, getTextColor()),
                PorterDuff.Mode.SRC_IN
            )
            iconDrawable!!.setColorFilter(porterDuffColorFilter)
        }
    }

    override fun tip(@StringRes message: Int): Toast {
        return tip(message, Toast.LENGTH_SHORT)
    }

    override fun tip(@StringRes message: Int, duration: Int): Toast {
        return tip(getString(message), duration)
    }

    override fun tip(@NonNull message: CharSequence?): Toast {
        return tip(message, Toast.LENGTH_SHORT)
    }

    override fun tip(@NonNull message: CharSequence?, duration: Int): Toast {
        return createToast(message, duration)
    }

    @CheckResult
    private fun createToast(@NonNull message: CharSequence?, duration: Int): Toast {
        val currentToast = Toast.makeText(mContext, "", duration)
        val toastLayout =
            (mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater).inflate(
                R.layout.lib_toast,
                null
            )

        with(toastLayout) {
            val toastIcon: ImageView = findViewById(R.id.lib_toast_icon)
            val toastTextView: TextView = findViewById(R.id.lib_toast_text)
            setBackground(toastDrawable)
            if (iconDrawable != null) {
                toastIcon.setImageDrawable(iconDrawable)
            } else {
                toastIcon.setVisibility(View.GONE)
            }
            toastTextView.let {
                it.setText(message)
                it.setTextColor(ContextCompat.getColor(mContext, getTextColor()))
                it.setTextSize(TypedValue.COMPLEX_UNIT_SP, getTextSize())
            }
        }

        currentToast.setView(toastLayout)
        return currentToast
    }

    @ColorRes
    override fun getTextColor(): Int {
        return R.color.color_white
    }

    override fun getTextSize(): Float {
        return 16f
    }

    fun getString(@StringRes resId: Int): String {
        return mContext.getString(resId)
    }
}
