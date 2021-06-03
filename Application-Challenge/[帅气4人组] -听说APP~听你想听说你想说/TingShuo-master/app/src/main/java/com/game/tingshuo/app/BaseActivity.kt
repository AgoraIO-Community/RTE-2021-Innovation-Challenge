package com.game.tingshuo.app

import android.app.Activity
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import butterknife.BindView
import butterknife.ButterKnife
import butterknife.OnClick
import com.jaeger.library.StatusBarUtil
import com.game.tingshuo.R
import com.game.tingshuo.event.CommonEvent
import me.imid.swipebacklayout.lib.app.SwipeBackActivity
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

//如果你使用databinding,请继承这个基类,用法不变
abstract class BaseActivity<T: ViewDataBinding> : SwipeBackActivity() {
    protected var mContext: Activity? = null

    @BindView(R.id.back)
    lateinit var back: ImageButton

    @BindView(R.id.tv_back_text)
    lateinit var tv_back_text: TextView

    @BindView(R.id.ib_right)
    lateinit var ib_right: ImageButton

    @BindView(R.id.title)
    lateinit var title: TextView

    @BindView(R.id.tv_right)
    lateinit var tv_right: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = DataBindingUtil.setContentView(this,getLayout())
        EventBus.getDefault().register(this)
        StatusBarUtil.setLightMode(this)
        StatusBarUtil.setColor(this, resources.getColor(R.color.colorAccent), 25)
        ButterKnife.bind(this)
        mContext = this
        App.addActivity(this)
        initEventAndData()
    }

    @OnClick(R.id.back)
    fun back(view: View?) {
        finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    //物理返回键
    override fun onBackPressed() {
        super.onBackPressed()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this)
        }
        App.removeActivity(this)
    }

    @Subscribe
    open fun subscribe(event: CommonEvent){

    }

    protected lateinit var mBinding: T
    protected abstract fun getLayout(): Int
    protected abstract fun initEventAndData()
}