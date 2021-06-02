package com.kangraoo.basektlib.ui

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import butterknife.ButterKnife
import butterknife.Unbinder
import com.kangraoo.basektlib.R
import com.kangraoo.basektlib.tools.HAction
import com.kangraoo.basektlib.tools.log.ULog
import com.kangraoo.basektlib.tools.tip.ITipToast
import com.kangraoo.basektlib.widget.toolsbar.ILibToolbar
import com.kangraoo.basektlib.widget.toolsbar.OnLibToolBarListener
import com.kangraoo.basektlib.widget.toolsbar.ToolBarOptions
import com.qdedu.autopage.AutoJ

abstract class BFragment : Fragment(), IBaseView {
    lateinit var mApplicationContext: Context
    var mContainer: ViewGroup? = null
    var mContentView: View? = null
    var mInflater: LayoutInflater? = null
    var isFInit = false

    fun <T> findViewById(id: Int): T? {
        return if (this.mContentView != null) this.mContentView!!.findViewById<View>(id) as T else null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.mApplicationContext = requireActivity().applicationContext
    }

    fun getApplicationContext(): Context? {
        return this.mApplicationContext
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.mInflater = inflater
        this.mContainer = container
        return onBaseCreateView(inflater, container, savedInstanceState)
    }

    /**
     * 建议更改这个类来修改底层
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    open fun onBaseCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this.isFInit = true
        mContentView = inflater.inflate(this.getLayoutId(), container, false)
        return mContentView
    }

    abstract fun getLayoutId(): Int

    override fun onDestroyView() {
        super.onDestroyView()
        this.mContentView = null
        this.mContainer = null
        this.mInflater = null
        if (this.isFInit) {
            this.onFragmentDestroyView()
        }
        this.isFInit = false
        if (unbinder != null && unbinder != Unbinder.EMPTY) {
            unbinder!!.unbind()
            unbinder = null
        }
    }

    open fun onFragmentDestroyView() {}

    private var mBaseToolbar: Toolbar? = null

    open fun setFragmentToolBar(toolBarId: Int, toolBarOptions: ToolBarOptions, onLibToolBarListener: OnLibToolBarListener) {
        mBaseToolbar = findViewById(toolBarId)
        if (mBaseToolbar != null) {
            if (mBaseToolbar is ILibToolbar) {
                val iLibToolbar: ILibToolbar = mBaseToolbar as ILibToolbar
                iLibToolbar.setOptions(toolBarOptions)
                iLibToolbar.setOnLibToolBarListener(onLibToolBarListener)
            }
        }
    }

    open fun getFragmentToolBar(): Toolbar? {
        return mBaseToolbar
    }

    open fun getFragmentToolBarHeight(): Int {
        return if (mBaseToolbar != null) {
            mBaseToolbar!!.height
        } else 0
    }

    open fun setFragmentTitleBarTitle(title: String?) {
        if (mBaseToolbar != null) {
            if (mBaseToolbar is ILibToolbar) {
                val iLibToolbar: ILibToolbar = mBaseToolbar as ILibToolbar
                iLibToolbar.setTitle(title)
            }
        }
    }

    open fun fragmentTitleVisible(boolean: Boolean) {
        if (mBaseToolbar != null) {
            if (boolean) {
                mBaseToolbar!!.visibility = View.VISIBLE
            } else {
                mBaseToolbar!!.visibility = View.GONE
            }
        }
    }

    open fun setFragmentToolbarOptions(toolBarOptions: ToolBarOptions, onLibToolBarListener: OnLibToolBarListener) {
        if (mBaseToolbar != null) {
            if (mBaseToolbar is ILibToolbar) {
                val iLibToolbar: ILibToolbar = mBaseToolbar as ILibToolbar
                iLibToolbar.setOptions(toolBarOptions)
                iLibToolbar.setOnLibToolBarListener(onLibToolBarListener)
            }
        }
    }

    /**
     * 嵌套fragment时必须要重写 onDetach()
     * @return
     */
    override fun onDetach() {
        super.onDetach()
//        try {
//            Field e = Fragment.class.getDeclaredField("mChildFragmentManager");
//            e.setAccessible(true);
//            e.set(this, (Object)null);
//        } catch (NoSuchFieldException var2) {
//            throw new RuntimeException(var2);
//        } catch (IllegalAccessException var3) {
//            throw new RuntimeException(var3);
//        }
    }

    var unbinder: Unbinder? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        unbinder = ButterKnife.bind(this, view)
        AutoJ.inject(this)
        super.onViewCreated(view, savedInstanceState)
    }

    override fun visitActivity(): Activity {
        return requireActivity()
    }

    override fun showToastMsg(msg: String?) {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showToastMsg(msg)
        }
    }

    fun setTitle(title: CharSequence?) {
        visitActivity().title = title.toString()
    }

    override fun showToastMsg(@StringRes resId: Int) {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showToastMsg(resId)
        }
    }

    override fun showToastMsg(iTipToast: ITipToast, msg: String?) {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showToastMsg(iTipToast, msg)
        }
    }

    override fun showToastMsg(iTipToast: ITipToast, @StringRes resId: Int) {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showToastMsg(iTipToast, resId)
        }
    }

    override fun showProgressingDialog() {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showProgressingDialog()
        }
    }

    override fun dismissProgressDialog() {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).dismissProgressDialog()
        }
    }

    override fun showProgressingDialog(resId: Int) {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showProgressingDialog(resId)
        }
    }

    override fun showProgressingDialog(msg: String) {
        if (visitActivity() is IBaseView) {
            (visitActivity() as IBaseView).showProgressingDialog(msg)
        }
    }

   override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        onLibResume()
    }

    open fun onLibResume() {
        if (this.isFInit && !this.isResume) {
            if (mMyHidden != null && !mMyHidden!!) {
                this.isResume = true
                this.onFragmentResume()
            } else if (mMyHidden == null && this.getUserVisibleHint()) {
                this.isResume = true
                this.onFragmentResume()
            }
        }
    }

    /**
     * 只有在展示的fragment 才会调用该方法，不展示的fragment 不会调用
     */
    open fun onFragmentResume() {
        ULog.i("(" + javaClass.simpleName + ".kt :" + 1 + ")", "(" + javaClass.simpleName + ".java :" + 1 + ")", getString(R.string.libFragmentShow))
    }

    //  private boolean isPause = true;
    var isResume = false

    /**
     * 需要修改
     */
    override fun onPause() {
        super.onPause()
        onLibPause()
    }

    open fun onLibPause() {
        if (this.isFInit && this.isResume) {
            this.isResume = false
            this.onFragmentPause()
        }
    }

    /**
     * 只有在展示的fragment 才会调用该方法，不展示的fragment 不会调用
     */
    open fun onFragmentPause() {
        ULog.i("(" + javaClass.simpleName + ".kt :" + 1 + ")", "(" + javaClass.simpleName + ".java :" + 1 + ")", getString(R.string.libFragmentClose))
    }

    fun getContentView(): View? {
        return this.mContentView
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        setUserVisibleLazyHint(isVisibleToUser)
        if (this.isFInit && this.getContentView() != null) {
            if (isVisibleToUser) {
                this.onLibResume()
            } else {
                this.onLibPause()
            }
        }
    }

    var mMyHidden: Boolean? = null

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (this.isFInit) {
            mMyHidden = hidden
            if (hidden) {
                onLibPause()
            } else {
                onLibResume()
            }
        }
    }

    /**
     * lazy fragmet 使用
     * @param isVisibleToUser
     */
    open fun setUserVisibleLazyHint(isVisibleToUser: Boolean) {}

    fun getHandler(): Handler {
        return HAction.mainHandler
    }

    override fun postRunnable(runnable: Runnable) {
        getHandler().post(Runnable {
            if (!isAdded) {
                return@Runnable
            }
            runnable.run()
        })
    }

    override fun postDelayed(runnable: Runnable, delay: Long) {
        getHandler().postDelayed(Runnable {
            if (!isAdded) {
                return@Runnable
            }
            runnable.run()
        }, delay)
    }

    override fun removeCallbacks(r: Runnable) {
        getHandler().removeCallbacks(r)
    }
}
