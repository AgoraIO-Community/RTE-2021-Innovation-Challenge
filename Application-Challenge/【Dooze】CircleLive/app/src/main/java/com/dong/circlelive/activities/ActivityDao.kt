package com.dong.circlelive.activities

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Create by dooze on 2021/5/24  10:56 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */

@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertActivity(activity: Activity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(activity: Activity)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun update(activities: List<Activity>)

    @Delete
    fun deleteActivity(activity: Activity)

    @Query("DELETE FROM Activity")
    fun deleteAll()

    @Query("SELECT * FROM Activity ORDER BY createdAt DESC")
    fun queryAll(): LiveData<List<Activity>>

    @Query("SELECT * FROM Activity WHERE status = :status ORDER BY createdAt DESC")
    fun queryAll(status: Int):List<Activity>

    @Query("SELECT COUNT('localId') FROM Activity where status < :status")
    fun unreadCount(status: Int): LiveData<Int>
}