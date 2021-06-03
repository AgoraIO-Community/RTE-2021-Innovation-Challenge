package com.game.tingshuo.app

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import butterknife.BindView
import butterknife.ButterKnife
import com.jaeger.library.StatusBarUtil
import com.game.tingshuo.R
import com.game.tingshuo.event.CommonEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

//如果你使用databinding,请继承这个基类,用法不变
abstract class BaseFragment<T:ViewDataBinding> : Fragment() {
    protected var mActivity: Activity? = null
    protected var mContext: Context? = null
    protected lateinit var mBinding: T
    /**
     * 是否对用户可见
     */
    protected var mIsVisible = false

    /**
     * 是否加载完成
     * 当执行完oncreatview,View的初始化方法后方法后即为true
     */
    protected var mIsPrepare = false

    @JvmField @BindView(R.id.back) var back: ImageButton? = null
    @JvmField @BindView(R.id.ib_login) var ib_login: ImageButton? = null
    @JvmField @BindView(R.id.tv_back_text) var tv_back_text: TextView? = null
    @JvmField @BindView(R.id.ib_right) var ib_right: ImageButton? = null
    @JvmField @BindView(R.id.title) var title: TextView? = null
    @JvmField @BindView(R.id.tv_right) var tv_right: TextView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater,getLayoutId(),container, false)
        EventBus.getDefault().register(this)
        getBundleData(arguments)
        initTitle()
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        StatusBarUtil.setColor(mActivity, resources.getColor(R.color.colorAccent), 25)
        ButterKnife.bind(this, view)
        initView(view, savedInstanceState)
        mIsPrepare = true
        reqData()
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as Activity
        mContext = context
    }

    protected fun initTitle() {
        back?.visibility = View.VISIBLE
        title?.visibility = View.VISIBLE
        ib_login?.visibility = View.GONE
        tv_back_text?.visibility = View.GONE
        title?.text = getString(R.string.app_name)
        ib_right?.visibility = View.GONE
        tv_right?.visibility = View.GONE
    }

    /**
     * 该抽象方法就是 onCreateView中需要的layoutID
     *
     * @return
     */
    protected abstract fun getLayoutId():Int

    /**
     * 该抽象方法就是 初始化view
     *
     * @param view
     * @param savedInstanceState
     */
    protected abstract fun initView(view: View?, savedInstanceState: Bundle?
    )

    /**
     * 执行数据的加载
     */
    protected fun getBundleData(arguments: Bundle?) {}

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        mIsVisible = isVisibleToUser
        if (isVisibleToUser) {
            onVisibleToUser()
        }
    }

    /**
     * 用户可见时执行的操作
     */
    protected fun onVisibleToUser() {
        if (mIsPrepare && mIsVisible) {
            reqData()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
    }

    @Subscribe
    open fun subscribe(event: CommonEvent){

    }

    /**
     * 懒加载，仅当用户可见切view初始化结束后才会执行
     */
    open fun reqData() {}

}