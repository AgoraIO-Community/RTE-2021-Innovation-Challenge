package com.kangraoo.basektlib.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import butterknife.ButterKnife
import com.qdedu.autopage.AutoJ

@Deprecated("lazyFramgnet have new to LifeCycle")
abstract class BLazyFragment : BFragment() {

    private var mSavedInstanceState: Bundle? = null
    private var layout: FrameLayout? = null
    protected var isInflated = false
    override fun onBaseCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (this.userVisibleHint && !this.isFInit) {
            this.isFInit = true
            mSavedInstanceState = savedInstanceState
            onCreateViewLazy(savedInstanceState)
        } else {
            layout = FrameLayout(getApplicationContext()!!)
            layout!!.layoutParams = ViewGroup.LayoutParams(-1, -1)
            setContentView(layout)
        }
        return mContentView
    }

    protected open fun onCreateViewLazy(savedInstanceState: Bundle?) {
        val contentView =
            LayoutInflater.from(this.activity).inflate(getLayoutId(), null as ViewGroup?)
        setContentView(contentView)
        ButterKnife.bind(this, contentView)
        this.onViewCreated()
        isInflated = true
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        AutoJ.inject(this)
    }

    protected abstract fun onViewCreated()

    open fun setContentView(contentView: View?) {
        if (getContentView() != null && getContentView()!!.parent != null) {
            layout!!.removeAllViews()
            layout!!.addView(contentView)
        }
        mContentView = contentView
    }

    override fun onFragmentDestroyView() {
        isInflated = false
        super.onFragmentDestroyView()
    }

    override fun setUserVisibleLazyHint(isVisibleToUser: Boolean) {
        super.setUserVisibleLazyHint(isVisibleToUser)
        if (isVisibleToUser && !this.isFInit && getContentView() != null) {
            this.isFInit = true
            onCreateViewLazy(mSavedInstanceState) // 调用懒加载
        }
    }
}
