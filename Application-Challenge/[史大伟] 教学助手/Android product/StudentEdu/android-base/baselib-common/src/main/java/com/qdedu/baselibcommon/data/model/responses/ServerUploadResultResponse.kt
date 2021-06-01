package com.qdedu.baselibcommon.data.model.responses

import com.squareup.moshi.JsonClass


/**
 * +++++++++++++++++++++++++++++++++++++
 * Time:
 * 2020-06-09
 * Creator:
 * Liu li.
 * Description:
 * -.
 * +++++++++++++++++++++++++++++++++++++
 */
@JsonClass(generateAdapter = true)
data class ServerUploadResultResponses (
    var uuid:String?,
    var save_file:String?,
    var save_path:String?,
    var friendly_url:String?,
    var relativePath:String?,
    var path:String?,
    var imageZip:String?,
    var imageFile:List<ImageFileEntity>?
)

@JsonClass(generateAdapter = true)
data class ImageFileEntity(
    var imagePath:String?,
    var imageUrl:String?
)