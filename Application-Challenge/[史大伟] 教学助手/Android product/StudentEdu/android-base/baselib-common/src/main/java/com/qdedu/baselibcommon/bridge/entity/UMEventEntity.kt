package com.qdedu.baselibcommon.bridge.entity

import com.squareup.moshi.JsonClass

/**
 * Creator:
 * GuFanFan.
 * Date:
 * Created on 08-07-2020.
 * Description:
 * -.
 */
@JsonClass(generateAdapter = true)
data class UMEventEntity(
    val eventId: String?,
    val courseId: String?,
    val courseName: String?,
    val courseTag: String?,
    val buyTag: Boolean?,
    val fromTag: String?,
    val time: String?,
    val payType: Int?
)