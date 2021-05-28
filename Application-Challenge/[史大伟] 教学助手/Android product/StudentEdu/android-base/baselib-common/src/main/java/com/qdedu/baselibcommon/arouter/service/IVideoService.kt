package com.qdedu.baselibcommon.arouter.service

import android.content.Context
import android.content.Intent
import androidx.fragment.app.Fragment
import com.alibaba.android.arouter.facade.template.IProvider
import com.qdedu.baselibcommon.ui.iface.IVideoFragment

interface IVideoService : IProvider {
    fun openVideoPage(
        context: Context,
        url: String,
        title: String,
        poster:String?=null
    )

    fun videoFragment(
        context: Context,
        url: String?,
        title: String?,
        poster:String?=null
    ):IVideoFragment

    fun fullScreenVideoFragment(
        context: Context,
        url: String?,
        title: String?,
        poster: String?
    ): IVideoFragment
}