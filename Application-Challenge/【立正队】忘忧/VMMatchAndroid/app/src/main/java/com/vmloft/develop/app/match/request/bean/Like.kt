package com.vmloft.develop.app.match.request.bean

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

/**
 * Create by lzan13 on 2020/7/30 16:15
 * 描述：喜欢数据 Bean
 */
@Parcelize
data class Like(
    @SerializedName("_id")
    var id: String,
    var owner: String = "",
    var user: User? = null,
    var post: Post? = null,
    var comment: Comment? = null,
    var type: Int = 0,
    var createdAt: String = ""
) : Parcelable {
}