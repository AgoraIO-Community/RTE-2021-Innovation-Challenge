package com.dong.circlelive.cos

import com.dong.circlelive.appContext
import com.dong.circlelive.live.TokenResult
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Create by dooze on 2021/5/24  12:01 上午
 * Email: stonelavender@hotmail.com
 * Description:
 */
interface QiniuAuth {
    @GET("/qiniu/uploadAuth.php")
    fun uploadToken(
        @Query("bucket") bucket: String,
        @Query("expires") expires: Int = 3600
    ): Call<TokenResult>

    companion object
}

fun QiniuAuth.Companion.getToken(bucket: String, expires: Int = 3600): TokenResult? {
    return appContext.retrofit.create(QiniuAuth::class.java).uploadToken(bucket, expires).execute().body()
}