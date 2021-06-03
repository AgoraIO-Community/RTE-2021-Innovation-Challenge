package com.game.tingshuo.adapter

import android.util.SparseArray
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter

abstract class BaseViewPagerAdapter<T>(protected var mData: List<T>) : PagerAdapter() {

    private val mViews: SparseArray<View> = SparseArray(mData.size)

    override fun getCount(): Int {
        return mData.size
    }

    override fun isViewFromObject(view: View, any: Any): Boolean {
        return view === any
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        var view = mViews[position]
        if (view == null) {
            view = newView(position)
            mViews.put(position, view)
        }
        container.addView(view)
        return view
    }

    abstract fun newView(position: Int): View

    override fun destroyItem(container: ViewGroup, position: Int, any: Any) {
        container.removeView(mViews[position])
    }

    fun getItem(position: Int): T {
        return mData[position]
    }

}