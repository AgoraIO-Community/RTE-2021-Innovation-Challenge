package com.qdedu.baselibcommon.data.model.responses

import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
open class BasicListResult{
    var page: ApiPageEntity? = null
}