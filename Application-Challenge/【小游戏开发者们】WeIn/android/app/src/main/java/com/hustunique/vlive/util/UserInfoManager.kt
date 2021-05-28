package com.hustunique.vlive.util

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.hustunique.vlive.VLiveApplication
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

/**
 *    author : Yuxuan Xiao
 *    e-mail : qpalwo@qq.com
 *    date   : 2021/5/20
 */
object UserInfoManager {

    private const val UID = "uid"
    private const val UNAME = "uname"
    private const val FIRST_TIME = "first_time"

    var uid: String = ""
    var uname: String = ""

    private val uidKey by lazy { stringPreferencesKey(UID) }
    private val unameKey by lazy { stringPreferencesKey(UNAME) }
    private val firstKey by lazy { booleanPreferencesKey(FIRST_TIME) }

    fun refreshUid() {
        GlobalScope.launch {
            uid = VLiveApplication.application.dataStore.data.map { it[uidKey] }.first() ?: ""
            uname = VLiveApplication.application.dataStore.data.map { it[unameKey] }.first() ?: ""
        }
    }

    suspend fun blockRefreshUid() {
        uid = VLiveApplication.application.dataStore.data.map { it[uidKey] }.first() ?: ""
        uname = VLiveApplication.application.dataStore.data.map { it[unameKey] }.first() ?: ""
    }

    fun saveUid(uid: String, uname: String = "") {
        this.uid = uid
        this.uname = uname
        GlobalScope.launch {
            VLiveApplication.application.dataStore.edit {
                it[uidKey] = uid
                it[unameKey] = uname
            }
        }
    }

    suspend fun getFirstTime(): Boolean =
        VLiveApplication.application.dataStore.data.map { it[firstKey] }.first() ?: true

    suspend fun setFirstTime() {
        VLiveApplication.application.dataStore.edit {
            it[firstKey] = false
        }
    }

}