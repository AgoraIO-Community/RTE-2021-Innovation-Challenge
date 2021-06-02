package com.qdedu.baselibcommon.widget

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter

class AdapterViewPager(fm: FragmentManager, var mList: List<Fragment>,var title:Array<String>? = null) : FragmentStatePagerAdapter(fm) {
    override fun getItem(position: Int) = mList[position]

    override fun getCount() = mList.size

    override fun getPageTitle(position: Int): CharSequence? {
        return if (title != null && position < title!!.size) {
            title!![position]
        } else super.getPageTitle(position)
    }

}