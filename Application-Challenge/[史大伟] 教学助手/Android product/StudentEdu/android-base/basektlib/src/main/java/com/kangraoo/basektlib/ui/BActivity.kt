package com.kangraoo.basektlib.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import butterknife.ButterKnife
import butterknife.Unbinder
import com.gyf.immersionbar.ImmersionBar.getStatusBarHeight
import com.gyf.immersionbar.ImmersionBar.setFitsSystemWindows
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.tip.ITipToast
import com.kangraoo.basektlib.tools.tip.Tip
import com.kangraoo.basektlib.tools.tip.TipToast
import com.kangraoo.basektlib.tools.view.UView
import com.kangraoo.basektlib.widget.dialog.IProgressDialog
import com.kangraoo.basektlib.widget.toolsbar.ILibToolbar
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangraoo.basektlib.widget.toolsbar.ToolBarOptions
import com.qdedu.autopage.AutoJ
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel

abstract class BActivity : AppCompatActivity(), CoroutineScope by MainScope(), IBaseView {

    private var unbinder: Unbinder? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        UView.inject(this)
        super.onCreate(savedInstanceState)
        AutoJ.inject(this)
        this.setContentView(this.getLayoutId())
        unbinder = ButterKnife.bind(this)
        this.onViewCreatedBefore(savedInstanceState)
        this.onViewCreated(savedInstanceState)
    }

    open fun onViewCreatedBefore(savedInstanceState: Bundle?) {
    }

    abstract fun getLayoutId(): Int

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    private var mBaseToolbar: Toolbar? = null

    open fun setToolBar(toolBarId: Int, toolBarOptions: ToolBarOptions, onLibToolBarListener: OnLibToolBarListener?,padding:Boolean = true) {
        mBaseToolbar = findViewById(toolBarId)
        if (mBaseToolbar != null) {
            if (mBaseToolbar is ILibToolbar) {
                val iLibToolbar: ILibToolbar = mBaseToolbar as ILibToolbar
                iLibToolbar.setOptions(toolBarOptions)
                iLibToolbar.setOnLibToolBarListener(onLibToolBarListener)
            }
            if(padding){
                mBaseToolbar!!.setPadding(mBaseToolbar!!.paddingLeft,
                    mBaseToolbar!!.paddingTop + getStatusBarHeight(this),
                    mBaseToolbar!!.paddingRight,
                    mBaseToolbar!!.paddingBottom
                )
            }
            setSupportActionBar(mBaseToolbar)
            supportActionBar!!.setDisplayShowTitleEnabled(false)
        }
    }

    open fun getToolBar(): Toolbar? {
        return mBaseToolbar
    }

    @Deprecated("", ReplaceWith("titleVisible(false)"))
    open fun toolBarVisable() {
//        setFitsSystemWindows(this,true)
//        getToolBar()?.visibility = View.GONE
        titleVisible(false)
    }

    open fun getToolBarHeight(): Int {
        return if (mBaseToolbar != null) {
            mBaseToolbar!!.height
        } else 0
    }

    open fun setTitleBarTitle(title: String?) {
        if (mBaseToolbar != null) {
            if (mBaseToolbar is ILibToolbar) {
                val iLibToolbar: ILibToolbar = mBaseToolbar as ILibToolbar
                iLibToolbar.setTitle(title)
            }
        }
    }

    open fun titleVisible(boolean: Boolean) {
        if (mBaseToolbar != null) {
            if (boolean) {
                setFitsSystemWindows(this, false)
                mBaseToolbar!!.visibility = View.VISIBLE
            } else {
                setFitsSystemWindows(this, true)
                mBaseToolbar!!.visibility = View.GONE
            }
        }
    }
    override fun setTitle(title: CharSequence?) {
        setTitleBarTitle(title.toString())
    }

    open fun setToolbarOptions(toolBarOptions: ToolBarOptions, onLibToolBarListener: OnLibToolBarListener) {
        if (mBaseToolbar != null) {
            if (mBaseToolbar is ILibToolbar) {
                val iLibToolbar: ILibToolbar = mBaseToolbar as ILibToolbar
                iLibToolbar.setOptions(toolBarOptions)
                iLibToolbar.setOnLibToolBarListener(onLibToolBarListener)
            }
        }
    }

    /**
     * 点击外部关闭键盘
     */
    protected open fun hideKeyBoard(): Boolean {
        return false
    }

    override fun onDestroy() {
        super.onDestroy()
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder!!.unbind()
            unbinder = null
        }
        iProgressDialog?.destoryProgress()
        cancel()
    }

    override fun visitActivity(): Activity {
        return this
    }

    protected open fun getTipToast(): ITipToast {
        return Tip.Normal
    }

    override fun showToastMsg(msg: String?) {
        TipToast.tip(getTipToast(), msg)
    }

    override fun showToastMsg(@StringRes resId: Int) {
        TipToast.tip(getTipToast(), resId)
    }

    override fun showToastMsg(iTipToast: ITipToast, message: Int) {
        TipToast.tip(iTipToast, message)
    }

    override fun showToastMsg(iTipToast: ITipToast, message: String?) {
        TipToast.tip(iTipToast, message)
    }

    var iProgressDialog: IProgressDialog? = null

//    protected open fun getProgressDialog(): IProgressDialog {
//        return LibSimpleProgressDialog(visitActivity())
//    }

    override fun showProgressingDialog() {
        iProgressDialog?.showProgress()
    }

    override fun showProgressingDialog(@StringRes var1: Int) {
        iProgressDialog?.showProgress(var1)
    }

    override fun showProgressingDialog(var1: String) {
        iProgressDialog?.showProgress(var1)
    }

    override fun dismissProgressDialog() {
        iProgressDialog?.dismissProgress()
    }

    private val handler: Handler by lazy {
        HAction.mainHandler
    }

    override fun postRunnable(runnable: Runnable) {
        handler.post { runnable.run() }
    }

    override fun postDelayed(runnable: Runnable, delay: Long) {
        handler.postDelayed({ runnable.run() }, delay)
    }

    override fun removeCallbacks(r: Runnable) {
        handler.removeCallbacks(r)
    }

    /**
     * 点击外部隐藏软键盘的函数
     * @param ev
     * @return
     */
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (!hideKeyBoard()) {
            return super.dispatchTouchEvent(ev)
        }
        if (ev.action == MotionEvent.ACTION_DOWN) {
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(v!!.windowToken, 0)
            }
            return super.dispatchTouchEvent(ev)
        }
        // 必不可少，否则所有的组件都不会有TouchEvent了
        return if (window.superDispatchTouchEvent(ev)) {
            true
        } else onTouchEvent(ev)
    }

    private fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val leftTop = intArrayOf(0, 0)
            // 获取输入框当前的location位置
            v.getLocationInWindow(leftTop)
            val left = leftTop[0]
            val top = leftTop[1]
            val bottom = top + v.getHeight()
            val right = left + v.getWidth()
            return !(event.x > left && event.x < right && event.y > top && event.y < bottom)
        }
        return false
    }
}
