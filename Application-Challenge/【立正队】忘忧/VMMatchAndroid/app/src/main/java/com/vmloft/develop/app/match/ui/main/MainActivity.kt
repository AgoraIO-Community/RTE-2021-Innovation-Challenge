package com.vmloft.develop.app.match.ui.main

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.vmloft.develop.app.match.R
import com.vmloft.develop.app.match.common.Constants
import com.vmloft.develop.app.match.common.SignManager
import com.vmloft.develop.app.match.databinding.ActivityMainBinding
import com.vmloft.develop.app.match.request.bean.User
import com.vmloft.develop.app.match.router.AppRouter
import com.vmloft.develop.app.match.ui.main.explore.ExploreFragment
import com.vmloft.develop.app.match.ui.main.home.HomeFragment
import com.vmloft.develop.app.match.ui.main.mine.MineFragment
import com.vmloft.develop.app.match.ui.main.msg.MsgFragment
import com.vmloft.develop.library.common.base.BVMActivity
import com.vmloft.develop.library.common.base.BViewModel
import com.vmloft.develop.library.common.common.PermissionManager
import com.vmloft.develop.library.common.event.LDEventBus
import com.vmloft.develop.library.common.router.CRouter
import kotlinx.android.synthetic.main.activity_main.*
import org.koin.androidx.viewmodel.ext.android.getViewModel

/**
 * Create by lzan13 on 2020/05/02 19:08
 * 描述：主界面
 */
@Route(path = AppRouter.appMain)
class MainActivity : BVMActivity<MainViewModel>() {

    private val currentTabKey = "currentTabKey"
    private val fragmentKeys = arrayListOf("homeKey", "exploreKey", "msgKey", "mineKey")
    private var currentTab = 0
    private var currentFragment: Fragment? = null

    private val fragmentList = arrayListOf<Fragment>()
    private lateinit var homeFragment: HomeFragment
    private lateinit var exploreFragment: ExploreFragment
    private lateinit var msgFragment: MsgFragment
    private lateinit var mineFragment: MineFragment

    @JvmField
    @Autowired
    var type: Int = 0

    override fun initVM(): MainViewModel = getViewModel()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!SignManager.instance.isSingIn()) {
            CRouter.go(AppRouter.appSignGuide)
            return finish()
        }
        if (savedInstanceState == null) {
            // 默认为0
            fragmentList.clear()
            initFragmentList()
        } else {
            //内存被回收了，fragments的list也被回收了，重新add进去
            currentTab = savedInstanceState.getInt(currentTabKey)
            fragmentList.clear()
            homeFragment = (supportFragmentManager.findFragmentByTag(fragmentKeys[0]) as HomeFragment?) ?: HomeFragment()
            exploreFragment = (supportFragmentManager.findFragmentByTag(fragmentKeys[1]) as ExploreFragment?) ?: ExploreFragment()
            msgFragment = (supportFragmentManager.findFragmentByTag(fragmentKeys[2]) as MsgFragment?) ?: MsgFragment()
            mineFragment = (supportFragmentManager.findFragmentByTag(fragmentKeys[3]) as MineFragment?) ?: MineFragment()
            fragmentList.run {
                add(homeFragment)
                add(exploreFragment)
                add(msgFragment)
                add(mineFragment)
            }
            currentFragment = fragmentList[currentTab]
        }
        switchFragment(currentTab)
    }

    override fun layoutId(): Int = R.layout.activity_main

    override fun initUI() {
        super.initUI()
        (mBinding as ActivityMainBinding).viewModel = mViewModel

        if (!SignManager.instance.isSingIn()) return

        initBottomNav()

        PermissionManager.instance.requestPermissions(this)
    }

    override fun initData() {
        ARouter.getInstance().inject(this)

        if (!SignManager.instance.isSingIn()) return

        mViewModel.getCurrUser()
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        setIntent(intent)
        ARouter.getInstance().inject(this)
        if (type == 1) {
            // 清空登录信息统一交给 Main 界面处理
            SignManager.instance.signOut()
        }
        if (!SignManager.instance.isSingIn()) {
            CRouter.go(AppRouter.appSignGuide)
            finish()
            return
        }
    }

    /**
     * 初始化底部导航
     */
    private fun initBottomNav() {
        // 如果导航是多色图标，需要取消 BottomNavigationView 的着色效果，自己去设置 selector
        // mainNav.itemIconTintList = null
        mainNav.setOnNavigationItemSelectedListener(onNavigationItemSelected)
    }

    /**
     * 导航监听
     */
    private val onNavigationItemSelected = BottomNavigationView.OnNavigationItemSelectedListener {
        when (it.itemId) {
            R.id.nav_home -> switchFragment(0)
            R.id.nav_explore -> switchFragment(1)
            R.id.nav_msg -> switchFragment(2)
            R.id.nav_mine -> switchFragment(3)
        }
        true
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // 保存当前 Tab
        outState.putInt(currentTabKey, currentTab)
    }

    /**
     * 界面切换
     */
    private fun switchFragment(position: Int) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        val oldFragment: Fragment = fragmentList[currentTab]
        val newFragment: Fragment = fragmentList[position]
        if (currentFragment == null) {
            transaction.add(R.id.mainContainerFL, newFragment, fragmentKeys[position]).commit()
        } else {
            if (newFragment.isAdded) {
                transaction.hide(oldFragment).show(newFragment).commit()
            } else {
                transaction.hide(oldFragment).add(R.id.mainContainerFL, newFragment, fragmentKeys[position]).commit()
            }
        }

        currentTab = position
        currentFragment = newFragment
    }

    /**
     * 初始化 Fragment 集合
     */
    private fun initFragmentList() {
        homeFragment = HomeFragment()
        exploreFragment = ExploreFragment()
        msgFragment = MsgFragment()
        mineFragment = MineFragment()

        fragmentList.run {
            add(homeFragment)
            add(exploreFragment)
            add(msgFragment)
            add(mineFragment)
        }
    }

    override fun onModelRefresh(model: BViewModel.UIModel) {
        if (model.type == "userInfo") {
            (model.data as? User)?.let {
                if (it.avatar.isNullOrEmpty() || it.nickname.isNullOrEmpty()) {
                    // 去设置用户信息
                    CRouter.go(AppRouter.appPersonalInfo)
                }
                LDEventBus.post(Constants.userInfoEvent, it)
            }
        }
    }
}
