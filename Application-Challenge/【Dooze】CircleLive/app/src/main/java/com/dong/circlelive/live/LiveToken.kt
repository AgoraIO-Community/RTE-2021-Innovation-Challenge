package com.dong.circlelive.live

import androidx.annotation.Keep
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Create by dooze on 2021/5/14  1:48 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
interface LiveToken {

    @GET("/agora/RtcToken.php")
    fun createRtc(
        @Query("channel") channel: String,
        @Query("userId") userId: String,
        @Query("expire") expireSecond: Int = 3600
    ): Call<TokenResult>

    @GET("/agora/RtmToken.php")
    fun createRtm(
        @Query("channel") channel: String,
        @Query("userId") userId: String,
        @Query("expire") expireSecond: Int = 3600
    ): Call<TokenResult>
}

@Keep
data class TokenResult(val code: Int, val result: String)