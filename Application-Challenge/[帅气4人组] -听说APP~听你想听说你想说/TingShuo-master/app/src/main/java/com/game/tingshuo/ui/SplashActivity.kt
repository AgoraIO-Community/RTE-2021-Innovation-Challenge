package com.game.tingshuo.ui

import android.os.Bundle
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.game.tingshuo.R
import com.game.tingshuo.adapter.SplashViewPagerAdapter
import com.game.tingshuo.fragment.SplashFragment
import java.util.*

class SplashActivity : FragmentActivity() {
    private var ll_indicator: LinearLayout? = null
    private val fragmentList: MutableList<Fragment> = ArrayList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val viewPager: ViewPager = findViewById(R.id.viewPager)
        ll_indicator = findViewById(R.id.ll_indicator)
        //创建Fragment
        for (i in 0..2) {
            val fragment = SplashFragment()
            val bundle = Bundle()
            bundle.putInt("index", i)
            fragment.arguments = bundle
            fragmentList.add(fragment)
        }
        val adapter: PagerAdapter = SplashViewPagerAdapter(supportFragmentManager, fragmentList)
        viewPager.adapter = adapter
        viewPager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, v: Float, i1: Int) {
            }

            override fun onPageSelected(position: Int) {
                for (i in fragmentList.indices) {
                    ll_indicator!!.getChildAt(i).setBackgroundResource(
                        if (position == i) R.drawable.dot_focus else R.drawable.dot_normal
                    )
                }
            }

            override fun onPageScrollStateChanged(position: Int) {}
        })
        initIndicator()
    }

    //初始化指示器
    private fun initIndicator() {
        val width = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10f, resources.displayMetrics).toInt()
        val layoutParams = LinearLayout.LayoutParams(width, width)
        layoutParams.rightMargin = 2 * width
        for (i in fragmentList.indices) {
            val view = View(this)
            view.setBackgroundResource(if (i == 0) R.drawable.dot_focus else R.drawable.dot_normal)
            view.layoutParams = layoutParams
            ll_indicator!!.addView(view, i)
        }
    }
}