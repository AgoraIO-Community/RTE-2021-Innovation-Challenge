package com.game.tingshuo.ui

import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.game.tingshuo.R
import com.game.tingshuo.adapter.BottomViewPagerAdapter
import com.game.tingshuo.app.BaseActivity
import com.game.tingshuo.databinding.ActivityMainBinding
import com.game.tingshuo.fragment.Tab1Fragment
import com.game.tingshuo.fragment.Tab2Fragment
import com.game.tingshuo.fragment.Tab3Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity<ActivityMainBinding>() {

    companion object {
        lateinit var main:MainActivity
    }

    override fun getLayout(): Int {
        return R.layout.activity_main
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        var tab = intent?.extras?.getInt("tab")
        if (tab == 2) {
            viewpager.setCurrentItem(2,true)
        } else if (tab ==0) {
            viewpager.setCurrentItem(0,true)
        } else {
            viewpager.setCurrentItem(1,true)
        }
    }

    override fun initEventAndData() {
        setSwipeBackEnable(false)
        main= this
        setContentView(R.layout.activity_main)
        initBottomBar()
    }

    private fun initBottomBar(){
        bottom_navigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.item_tab1 -> {
                    viewpager.currentItem = 0
                    true
                }
                R.id.item_tab2 -> {
                    viewpager.currentItem = 1
                    true
                }
                R.id.item_tab3 -> {
                    viewpager.currentItem = 2
                    true
                }
                else -> false
            }
        }

        setupViewPager(viewpager)


        viewpager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                bottom_navigation.menu.getItem(position).isChecked = true
            }
        })
    }


    private fun setupViewPager(viewPager: ViewPager2) {
        var fragments= mutableListOf<Fragment>()
        fragments.add(Tab1Fragment())
        fragments.add(Tab2Fragment())
        fragments.add(Tab3Fragment())
        var adapter = BottomViewPagerAdapter(this, fragments)
        viewPager.adapter = adapter
    }
}