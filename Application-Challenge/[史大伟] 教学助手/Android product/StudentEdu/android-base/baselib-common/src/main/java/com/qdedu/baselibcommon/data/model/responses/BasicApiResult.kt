package com.qdedu.baselibcommon.data.model.responses

import com.kangraoo.basektlib.data.model.BResponse
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
class BasicApiResult<T>(var code: String, var message: String, var page: ApiPageEntity?, var data: T?):BResponse()