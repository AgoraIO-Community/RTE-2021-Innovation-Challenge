package com.game.tingshuo.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class SplashViewPagerAdapter(fm: FragmentManager?, private val fragmentList: List<Fragment>?) : FragmentPagerAdapter(fm!!) {

    override fun getItem(position: Int): Fragment {
        return fragmentList!![position]
    }

    override fun getCount(): Int {
        return fragmentList?.size ?: 0
    }

}