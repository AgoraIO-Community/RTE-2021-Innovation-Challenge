package com.qdedu.baselibcommon.data.model.responses

import android.os.Parcelable
import com.squareup.moshi.JsonClass
import kotlinx.android.parcel.Parcelize

/**
 * +++++++++++++++++++++++++++++++++++++
 * Time:
 * 2020-06-02
 * Creator:
 * GuFanFan.
 * Description:
 * -.
 * +++++++++++++++++++++++++++++++++++++
 */
@JsonClass(generateAdapter = true)
@Parcelize
data class ApiPageEntity(
    val currentPage: Int,
    val pageSize: Int,
    val totalCount: Int,
    val pageCount: Int
): Parcelable