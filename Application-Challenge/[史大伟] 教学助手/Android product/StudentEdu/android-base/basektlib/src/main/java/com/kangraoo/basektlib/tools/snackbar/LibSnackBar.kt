package com.kangraoo.basektlib.tools.snackbar

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.graphics.drawable.Drawable
import android.util.TypedValue
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.app.SApplication
import com.kangraoo.basektlib.tools.UUi
import com.kangraoo.basektlib.tools.tip.TipConfig

enum class LibSnackBar : ISnackBar, ISnackBarStyle {
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

    protected var mContext: Context
    private var bgColor = R.color.color_666666
    protected var tipConfig: TipConfig
    var iconDrawable: Drawable? = null

    init {
        mContext = SApplication.context()
        tipConfig = SApplication.instance().sConfiger.tipConfig
        if (getTipBackGroundColor() != 0) {
            bgColor = getTipBackGroundColor()
        }
        if (getIcon() != 0) {
            iconDrawable = ContextCompat.getDrawable(mContext, getIcon())
            val porterDuffColorFilter = PorterDuffColorFilter(
                ContextCompat.getColor(mContext, getTextColor()),
                PorterDuff.Mode.SRC_IN
            )
            iconDrawable!!.colorFilter = porterDuffColorFilter
        }
    }

    @ColorRes
    override fun getTextColor(): Int {
        return R.color.color_white
    }

    @ColorRes
    override fun getActionTextColor(): Int {
        return R.color.lib_color_whitesmoke
    }

    override fun getTextSize(): Float {
        return 16f
    }

    override fun getActionTextSize(): Float {
        return 16f
    }

    override fun tip(view: View, message: Int): Snackbar {
        return tip(view, message, Snackbar.LENGTH_SHORT)
    }

    override fun tip(view: View, message: Int, duration: Int): Snackbar {
        return tip(view, message, duration, -1, null)
    }

    override fun tip(
        view: View,
        message: Int,
        actionText: Int,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        return tip(view, message, Snackbar.LENGTH_SHORT, actionText, listener)
    }

    override fun tip(
        view: View,
        message: Int,
        duration: Int,
        actionText: Int,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        return tip(view,
            getString(message),
            duration,
            if (actionText == -1) null else getString(actionText),
            listener
        )
    }

    override fun tip(view: View, message: CharSequence): Snackbar {
        return tip(view, message, Toast.LENGTH_SHORT)
    }

    override fun tip(view: View, message: CharSequence, duration: Int): Snackbar {
        return tip(view, message, duration, null, null)
    }

    override fun tip(
        view: View,
        message: CharSequence,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        return tip(view, message, Snackbar.LENGTH_SHORT, actionText, listener)
    }

    override fun tip(
        view: View,
        message: CharSequence,
        duration: Int,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        return createSnackbar(view, message, duration, -1, actionText, listener)
    }

    override fun tipIndefinite(view: View, message: Int, duration: Int): Snackbar {
        return tipIndefinite(view, getString(message), duration)
    }

    override fun tipIndefinite(
        view: View,
        message: Int,
        duration: Int,
        actionText: Int,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        return tipIndefinite(view,
            getString(message),
            duration,
            if (actionText == -1) null else getString(actionText),
            listener
        )
    }

    override fun tipIndefinite(view: View, message: CharSequence, duration: Int): Snackbar {
        return tipIndefinite(view, message, duration, null, null)
    }

    override fun tipIndefinite(
        view: View,
        message: CharSequence,
        duration: Int,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        return createSnackbar(view, message, Snackbar.LENGTH_INDEFINITE, duration, actionText, listener)
    }

    private fun getString(@StringRes resId: Int): String {
        return mContext.getString(resId)
    }

    private fun createSnackbar(
        view: View,
        text: CharSequence,
        duration: Int,
        time: Int,
        actionText: CharSequence?,
        listener: SnackBarOnClickListener?
    ): Snackbar {
        val snackbar = Snackbar.make(view, text, duration)
        if (duration == Snackbar.LENGTH_INDEFINITE) {
            if (time > 0) {
                snackbar.duration = time
            }
        }
        val view = snackbar.view
        val snackbarText = view.findViewById<TextView>(R.id.snackbar_text)
        if (iconDrawable != null) {
            iconDrawable!!.setBounds(
                0,
                0,
                iconDrawable!!.minimumWidth,
                iconDrawable!!.minimumHeight
            )
            snackbarText.setCompoundDrawables(iconDrawable, null, null, null)
            snackbarText.compoundDrawablePadding = UUi.dp2px(mContext, 4f)
        }
        val snackbarAction = view.findViewById<Button>(R.id.snackbar_action)
        snackbarText.setTextColor(ContextCompat.getColor(mContext, getTextColor()))
        snackbarText.setTextSize(TypedValue.COMPLEX_UNIT_SP, getTextSize())
        view.setBackgroundColor(ContextCompat.getColor(mContext, bgColor))
        if (actionText != null && actionText.isNotEmpty() && listener != null) {
            snackbar.setActionTextColor(ContextCompat.getColor(mContext, getActionTextColor()))
            snackbarAction.setTextSize(TypedValue.COMPLEX_UNIT_SP, getActionTextSize())
            val l: SnackBarOnClickListener = listener
            snackbar.setAction(
                actionText
            ) { l.onSnackBarClick(snackbar) }
        }
        return snackbar
    }
}

interface SnackBarOnClickListener {
    fun onSnackBarClick(snackbar: Snackbar)
}
