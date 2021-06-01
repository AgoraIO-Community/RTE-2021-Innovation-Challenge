package com.qdedu.baselibcommon.arouter.service

import android.content.Context
import com.alibaba.android.arouter.facade.template.IProvider

/**
 * Time:
 * 2020-06-19
 * Creator:
 * GuFanFan.
 * Description:
 * -.
 */
interface IReaderService : IProvider {

    fun openBook(
        context: Context,
        bookId: Long,
        formatType: String,
        url: String,
        bookName: String,
        dtoStr: String,
        readingTermStr: String,
        clickTime: String
    )
}