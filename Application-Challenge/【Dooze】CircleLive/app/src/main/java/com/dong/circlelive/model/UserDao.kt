package com.dong.circlelive.model

import androidx.room.*

/**
 * Create by dooze on 2021/5/25  4:09 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

@ProvidedAutoMigrationSpec
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(user: User)

    @Delete
    fun delete(user: User)

    @Query("SELECT  * FROM User WHERE objectId = :objectId")
    fun find(objectId: String): User?

    @Query("SELECT  * FROM User WHERE emUsername = :emUsername")
    fun findByEmUsername(emUsername: String): User?
}