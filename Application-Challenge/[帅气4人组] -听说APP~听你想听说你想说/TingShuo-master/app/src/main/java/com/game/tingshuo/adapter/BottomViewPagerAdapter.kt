package com.game.tingshuo.adapter

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//使用新的viewpager2
class BottomViewPagerAdapter(@NonNull fragmentActivity: FragmentActivity?, private val mFragments: List<Fragment>) : FragmentStateAdapter(fragmentActivity!!) {
    @NonNull
    override fun createFragment(position: Int): Fragment {
        return mFragments[position]
    }

    override fun getItemCount(): Int {
        return mFragments.size
    }
}