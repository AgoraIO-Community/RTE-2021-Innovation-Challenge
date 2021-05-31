package com.dong.circlelive.main

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.widget.Toolbar
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager
import cn.leancloud.AVUser
import com.dong.circlelive.*
import com.dong.circlelive.activities.ActivitiesFragment
import com.dong.circlelive.base.BaseActivity
import com.dong.circlelive.base.Timber
import com.dong.circlelive.base.viewBinding
import com.dong.circlelive.camera.Accelerometer
import com.dong.circlelive.databinding.ActivityMainBinding
import com.dong.circlelive.live.createchannel.CreateLiveChannelFragment
import com.dong.circlelive.posts.CreatePostFragment
import com.dong.circlelive.profile.ProfileFragment

class MainActivity : BaseActivity(), Toolbar.OnMenuItemClickListener {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ViewCompat.setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            imeHeight.value = insets.getInsets(WindowInsetsCompat.Type.ime()).bottom
            v.onApplyWindowInsets(insets.toWindowInsets())
            insets
        }

        setContentView(binding.root)
        Accelerometer(appContext).start()

        Live.initUserInfo()


        binding.mainPager.adapter = MainPagerAdapter(supportFragmentManager)
        binding.toolbar.inflateMenu(R.menu.main_tool_bar)
        binding.toolbar.setTitle(R.string.posts)
        binding.toolbar.setOnMenuItemClickListener(this)
        binding.mainPager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
            }

            override fun onPageSelected(position: Int) {
                when (position) {
                    0 -> {
                        binding.toolbar.setTitle(R.string.posts)
                        binding.bnvMain.selectedItemId = R.id.main_tab_posts
                    }
                    1 -> {
                        binding.toolbar.setTitle(R.string.live)
                        binding.bnvMain.selectedItemId = R.id.main_tab_live
                    }
                    2 -> {
                        binding.toolbar.setTitle(R.string.conversation)
                        binding.bnvMain.selectedItemId = R.id.main_tab_conversation
                    }
                }
            }

            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        binding.bnvMain.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.main_tab_posts -> binding.mainPager.currentItem = 0
                R.id.main_tab_live -> binding.mainPager.currentItem = 1
                R.id.main_tab_conversation -> binding.mainPager.currentItem = 2
            }
            true
        }

        viewModel.init()

        viewModel.unreadActivityCount.observe(this) { count ->
            Timber.d { "unread activity count $count" }
            binding.toolbar.menu.findItem(R.id.main_toolbar_notify)?.apply {
                this.setIcon(if (count > 0) R.drawable.ic_notify_new else R.drawable.ic_notify)
            }
        }

        viewModel.logout.observe(this) {
            if (it) {
                finish()
                start(LauncherActivity::class.java)
            }
        }
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.main_toolbar_notify -> {
                showFragment(ActivitiesFragment(), containerId = R.id.top_fragment_container)
            }
            R.id.main_toolbar_more -> {

            }
            R.id.main_toolbar_live_publish -> {
                showFragment(CreateLiveChannelFragment(), containerId = R.id.top_fragment_container)
            }
            R.id.main_toolbar_my -> {
                showFragment(ProfileFragment.newInstance(AVUser.currentUser().objectId), containerId = R.id.top_fragment_container)
            }
        }
        return true
    }

    companion object {
        val imeHeight = MutableLiveData<Int>()
    }

}