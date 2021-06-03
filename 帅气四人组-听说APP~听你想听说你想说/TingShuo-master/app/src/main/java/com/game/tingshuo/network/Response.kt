package com.game.tingshuo.network

/**
 * User: ljx
 * Date: 2018/10/21
 * Time: 13:16
 */
data class Response<T>(
    var code: String,
    var msg: String,
    var data: T
)

