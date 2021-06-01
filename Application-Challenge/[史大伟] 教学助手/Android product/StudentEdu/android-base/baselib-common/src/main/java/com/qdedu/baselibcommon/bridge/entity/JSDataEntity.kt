package com.qdedu.baselibcommon.bridge.entity

import com.squareup.moshi.JsonClass

/**
 * Time:
 * 2020-06-19
 * Creator:
 * GuFanFan.
 * Description:
 * -与JS交互数据实体类.
 */

@JsonClass(generateAdapter = true)
data class JSDataEntity(
    val url: String?,
    val num: Int = 0,
    val data: String?,
    val event: String?,
    val toolbar: JSToolbarEntity?
)

@JsonClass(generateAdapter = true)
data class JSToolbarEntity(
    val visible: String?,
    val bgcolor: String?,
    val bounce: String?,
    val title: JSToolbarTitleEntity?,
    val btnleft: JSToolbarButtonEntity?,
    val btnright: JSToolbarButtonEntity?,
    val btnmore: List<JSToolbarButtonEntity>?
)

@JsonClass(generateAdapter = true)
data class JSToolbarTitleEntity(
    val content: String?,
    val textsize: String?,
    val textcolor: String?
)

@JsonClass(generateAdapter = true)
data class JSToolbarButtonEntity(
    val data: String?,
    val name: String?,
    val image: String?,
    val newpage: String?
)

@JsonClass(generateAdapter = true)
data class JSShareEntity(
    val type: String,
    val title: String,
    val des: String,
    val url: String
)

@JsonClass(generateAdapter = true)
data class JSReadingEntity(
    val id: String?,
    val bookId: Long?,
    val from: Int?,
    val bookName: String?,
    val from_pager: Int?
)
@JsonClass(generateAdapter = true)
data class JSNewPageEntity(
    val closenumpage: Int = 0,
    val hidetoolbar: Boolean = false,
    val data: String?
)