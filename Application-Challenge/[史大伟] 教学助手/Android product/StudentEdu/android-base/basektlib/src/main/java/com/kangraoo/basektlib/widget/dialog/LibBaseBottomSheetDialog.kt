package com.kangraoo.basektlib.widget.dialog

import android.content.Context
import android.content.DialogInterface
import android.os.Build
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.FrameLayout
import androidx.core.view.MotionEventCompat
import butterknife.ButterKnife
import butterknife.Unbinder
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.widget.common.DialogPopupConfig


/**
 * author : sdw
 * e-mail : shidawei@xiaohe.com
 * time : 2019/10/17
 * desc :
 * version: 1.0
 */
abstract class LibBaseBottomSheetDialog @JvmOverloads constructor(
        context: Context,
        var iDialogPopup: DialogPopupConfig = DialogPopupConfig.build { }
) :
    BottomSheetDialog(context, R.style.libBottomSheetDialogTheme), DialogInterface.OnDismissListener {
    var unbinder: Unbinder? = null
    protected abstract fun onViewCreated(contentView: View?)
    protected abstract val windowLayoutId: Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val contentView = layoutInflater.inflate(windowLayoutId, null)
        setContentView(contentView)
        unbinder = ButterKnife.bind(this, contentView)
        // // 注意：这里要给layout的parent设置peekHeight，而不是在layout里给layout本身设置，下面设置背景色同理，坑爹！！！
//        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from(((View) bottomSheetView.getParent()));
//        bottomSheetBehavior.setPeekHeight(730);

        setCanceledOnTouchOutside(iDialogPopup.enableOutsideTouch)

        if (window != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                window!!.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
                window!!.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            }
            window!!.setLayout(
                    iDialogPopup.windowWidth,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
        }

//        val lp = window!!.attributes
//        lp.width = iDialogPopup.windowWidth
//        lp.height = iDialogPopup.windowHeight
//        lp.alpha = windowAlpha()
//        window!!.attributes = lp
//        if (!iDialogPopup.isDarkModel) {
//            window!!.clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND)
//        }
        dismissWithAnimation = true

        onViewCreated(contentView)
        setOnDismissListener(this)
    }

    /**
     * 持久高度
     */
    protected fun holdHeight(contentView: View){
        val bottomSheet: FrameLayout? = findViewById(R.id.design_bottom_sheet)
        bottomSheet?.let {
            var behavior = BottomSheetBehavior.from(it)
            contentView.measure(0, 0)
            behavior.peekHeight = contentView.measuredHeight
        }
    }

    open fun windowAlpha(): Float {
        return 1.0f
    }


    override fun show() {
        super.show()
        onShow()
        if (onLibDialogListener != null) {
            onLibDialogListener!!.onShow()
        }
    }

    override fun onDismiss(dialog: DialogInterface) {
        if (onLibDialogListener != null) {
            onLibDialogListener!!.onDismiss(dialog)
        }
    }

    fun lazyDismissForTime(time: Long) {
        HAction.mainHandler.postDelayed({
            if (!isDestory && isShowing) {
                dismiss()
            }
        }, time)
    }

    protected abstract fun onShow()
    private var isDestory = false
    fun onDestory() {
        isDestory = true
        if (isShowing) {
            dismiss()
        }
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder!!.unbind()
            unbinder = null
        }
        if (onLibDialogListener != null) {
            onLibDialogListener = null
        }
        setOnDismissListener(null)
    }

    var onLibDialogListener: OnLibDialogListener? = null


}
