package com.qdedu.baselibcommon.arouter.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider
import java.nio.file.Path

interface IToReadingService : IProvider {

    fun readingToInPlace(context: Context,path: String? = null)

}