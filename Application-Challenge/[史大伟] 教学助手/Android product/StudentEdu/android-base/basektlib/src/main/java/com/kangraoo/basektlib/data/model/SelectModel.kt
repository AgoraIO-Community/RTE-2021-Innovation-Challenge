package com.kangraoo.basektlib.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

/**
 * author : WaTaNaBe
 * e-mail : 297165331@qq.com
 * time : 2020/09/15
 * desc : 选择模型
 */
@Parcelize
class SelectModel<T : Parcelable>(val title: String, val value: T, var select: Boolean = false) : Parcelable


class SelectAnyModel(val title: String, val value: Any?, var select: Boolean = false)
