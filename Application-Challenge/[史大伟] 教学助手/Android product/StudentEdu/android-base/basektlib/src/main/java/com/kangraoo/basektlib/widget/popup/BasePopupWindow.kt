//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package com.kangraoo.basektlib.widget.popup

import android.app.Activity
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.widget.PopupWindow
import butterknife.ButterKnife
import butterknife.Unbinder
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.UUi.becomeDark
import com.kangraoo.basektlib.tools.UUi.becomeNormal
import com.kangraoo.basektlib.widget.common.DialogPopupConfig

abstract class BasePopupWindow @JvmOverloads constructor(var context: Context, var iDialogPopup: DialogPopupConfig = DialogPopupConfig.build { }) : PopupWindow(context), PopupWindow.OnDismissListener {
    var unbinder: Unbinder? = null

    @Volatile var create = false

    private fun createView() {
        if (!create) {
            synchronized(this) {
                if (!create) {
                    initView()
                    create = true
                }
            }
        }
    }

    fun initView() {
        initWindowAttrs()
        contentView = LayoutInflater.from(context).inflate(windowLayoutId, null)
        unbinder = ButterKnife.bind(this, contentView)
        onViewCreated(contentView)
        setOnDismissListener(this)
    }

    private fun initWindowAttrs() {
        this.width = iDialogPopup.windowWidth
        this.height = iDialogPopup.windowHeight
        this.isOutsideTouchable = true
        this.isFocusable = true
        setBackgroundDrawable(ColorDrawable())
        setTouchInterceptor(OnTouchListener { v, event ->
            if (!iDialogPopup.enableOutsideTouch) {
                if (event.y >= 0 && event.x> 0 && v.height - event.y> 0 && v.width - event.x> 0) {
                    v.performClick()
                    false
                } else {
                    true
                }
            } else {
                v.performClick()
                false
            }
        })
        if (iDialogPopup.animationStyle != 0) {
            this.animationStyle = iDialogPopup.animationStyle
        }
        otherAttrs()
        this.update()
    }

    protected abstract val windowLayoutId: Int

    //  protected int getWindowWidth(){
    //    return UUi.getDialogWidth();
    //  }
    //
    //  protected abstract int getWindowHeight();
    protected abstract fun onViewCreated(var1: View)

    //  protected boolean isDarkModel(){
    //    return false;
    //  }
    protected abstract fun otherAttrs()

    //  protected boolean enableOutsideTouch() {
    //    return true;
    //  }
    fun showAtBottom(anchorView: View) {
        showAtLocation(anchorView, Gravity.BOTTOM, 0, 0)
    }

    fun showAtCenter(anchorView: View) {
        showAtLocation(anchorView, Gravity.CENTER, 0, 0)
    }

    override fun showAsDropDown(anchor: View) {
        createView()
        super.showAsDropDown(anchor)
        onShow()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int) {
        createView()
        super.showAsDropDown(anchor, xoff, yoff)
        onShow()
    }

    override fun showAsDropDown(anchor: View, xoff: Int, yoff: Int, gravity: Int) {
        createView()
        super.showAsDropDown(anchor, xoff, yoff, gravity)
        onShow()
    }

    override fun showAtLocation(parent: View, gravity: Int, x: Int, y: Int) {
        createView()
        super.showAtLocation(parent, gravity, x, y)
        onShow()
    }

    protected fun onShow() {
        if (iDialogPopup.isDarkModel) {
            becomeDark((contentView.context as Activity), 0.5f)
        }
        if (onLibPopupListener != null) {
            onLibPopupListener!!.onShow()
        }
    }

    fun lazyDismissForTime(time: Long) {
        HAction.mainHandler.postDelayed(Runnable {
            if (!isDestory && isShowing) {
                dismiss()
            }
        }, time)
    }

    private var isDestory = false
    open fun onDestory() {
        isDestory = true
        if (isShowing) {
            dismiss()
        }
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder!!.unbind()
            unbinder = null
        }
        if (onLibPopupListener != null) {
            onLibPopupListener = null
        }
    }

    override fun onDismiss() {
        if (iDialogPopup.isDarkModel) {
            becomeNormal((contentView.context as Activity))
        }
        if (onLibPopupListener != null) {
            onLibPopupListener!!.onDismiss()
        }
    }

    var onLibPopupListener: OnLibPopupListener? = null

    interface OnLibPopupListener {
        fun onShow()
        fun onDismiss()
    }
}
