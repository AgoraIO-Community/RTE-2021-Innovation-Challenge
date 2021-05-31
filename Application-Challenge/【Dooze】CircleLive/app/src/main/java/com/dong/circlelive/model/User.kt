package com.dong.circlelive.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import cn.leancloud.AVException
import cn.leancloud.AVException.USERNAME_TAKEN
import cn.leancloud.AVQuery
import cn.leancloud.AVUser
import com.dong.circlelive.base.Timber
import com.dong.circlelive.db.UserDatabase
import com.dong.circlelive.emClient
import com.dong.circlelive.utils.md5

/**
 * Create by dooze on 2021/5/12  9:39 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

@Keep
@Entity(tableName = "User")
class User {

    @ColumnInfo(name = "objectId")
    @PrimaryKey
    var objectId: String = ""

    @ColumnInfo(name = "emUsername")
    var emUsername: String = ""

    @ColumnInfo(name = "username")
    var username: String = ""

    @ColumnInfo(name = "avatarUrl")
    var avatarUrl: String = ""

    companion object
}

var AVUser.emUsername: String
    get() = getString("emUsername")
    set(value) = put("emUsername", value)

val AVUser.uid: String
    get() = objectId

fun createAVUser(username: String, password: String): AVUser {
    return AVUser().apply {
        this.username = username
        this.password = password
        this.emUsername = username.md5
    }
}

fun AVUser.toUser(): User = User().apply {
    this.objectId = this@toUser.objectId
    this.emUsername = this@toUser.emUsername
    this.username = this@toUser.username
    this.avatarUrl = this@toUser.getAVFile("cover")?.url ?: ""
}

suspend fun AVUser.login(): AVUser {
    var signUp: Boolean
    val pwd = password
    try {
        signUp()
        signUp = true
        emClient.createAccount(emUsername, pwd)
    } catch (t: Throwable) {
        if (t is AVException && t.code == USERNAME_TAKEN || kotlin.run {
                val cause = t.cause
                cause is AVException && cause.code == USERNAME_TAKEN
            }) {
            signUp = true
        } else {
            throw t
        }
    }
    if (signUp) {
        AVUser.logIn(username, pwd).blockingFirst()
    }
    return AVUser.getCurrentUser()
}

suspend fun User.Companion.getByName(emUsername: String): AVUser {
    val query = AVQuery<AVUser>("_User")
    query.cachePolicy = AVQuery.CachePolicy.CACHE_ELSE_NETWORK
    query.whereEqualTo("emUsername", emUsername)
    val user =  query.find().firstOrNull() as AVUser
    UserDatabase.db.userDao().update(user.toUser())
    return user
}

