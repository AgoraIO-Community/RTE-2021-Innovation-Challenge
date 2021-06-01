package com.kangaroo.studentedu.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * @author shidawei
 * 创建日期：2021/5/27
 * 描述：
 */
@Parcelize
data class HomeWork(val name:String,val img:Int,val content:String,val title:String,val teacher:String,val teacherContent:String,val teacherImg:Int,val student:String):Parcelable