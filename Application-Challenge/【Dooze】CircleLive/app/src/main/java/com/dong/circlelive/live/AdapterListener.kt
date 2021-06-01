package com.dong.circlelive.live

import android.view.View

/**
 * Create by dooze on 2021/5/17  3:19 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

interface OnAdapterItemClickListener {
    fun onAdapterItemClick(view: View, position: Int)
}

interface OnAdapterItemLongClickListener {
    fun onAdapterItemLongClick(view: View, position: Int): Boolean
}