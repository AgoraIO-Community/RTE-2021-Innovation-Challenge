package com.qdedu.baselibcommon.arouter.service

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import com.alibaba.android.arouter.facade.template.IProvider
import com.kangraoo.basektlib.app.SApplication

/**
 * Creator:
 * GuFanFan.
 * Date:
 * Created on 07-07-2020.
 * Description:
 * -.
 */
const val LINK = "link"
const val BITMAP = "bitmap"
const val VIDEO = "video"
const val AUDIO = "audio"

interface IShareService : IProvider {

    fun shareOnActivityResult(context: Context,requestCode : Int, resultCode : Int, data:Intent?)

    fun shareRelease(context : Context)

    fun shareUrl(context: Context, url: String, title: String, desc: String, shareType:String = LINK, bitmap: Bitmap? = null,thumbData:Any? = SApplication.instance().sConfiger.applogo)

}