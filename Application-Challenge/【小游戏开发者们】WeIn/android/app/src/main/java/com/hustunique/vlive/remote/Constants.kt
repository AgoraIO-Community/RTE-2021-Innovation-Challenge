package com.hustunique.vlive.remote

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
object Constants {

    const val BASE_URL = "https://vlive.uniqueandroid.com:12345/"

    /**
     * all post
     */
    const val USER_REG = "user/reg" // userName(string), male(bool)

    const val CHANNEL_JOIN = "channel/join" // uid(string), channelId(string)

    const val CHANNEL_LEAVE = "channel/leave" // uid, channelId

    const val CHANNEL_LIST = "channel/list" // void

    const val CHANNEL_CREATE = "channel/create"

}