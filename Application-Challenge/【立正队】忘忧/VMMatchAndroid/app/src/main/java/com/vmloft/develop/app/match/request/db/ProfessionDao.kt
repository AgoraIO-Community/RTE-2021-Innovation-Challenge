package com.vmloft.develop.app.match.request.db

import androidx.room.*
import com.vmloft.develop.app.match.request.bean.Category
import com.vmloft.develop.app.match.request.bean.Profession

/**
 * Create by lzan13 on 2020/8/9 14:41
 * 描述：职业数据 Dao
 */
@Dao
interface ProfessionDao {

    @Insert
    suspend fun insert(vararg profession: Profession)

    @Delete
    suspend fun delete(profession: Profession)

    @Query("DELETE FROM profession")
    fun delete()

    @Update
    suspend fun update(profession: Profession)

    @Query("SELECT * FROM profession WHERE id = :id")
    suspend fun query(id: String): Profession?

    @Query("SELECT *  FROM profession")
    suspend fun all(): List<Profession>
}