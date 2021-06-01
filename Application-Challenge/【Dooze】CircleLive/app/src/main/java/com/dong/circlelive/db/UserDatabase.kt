package com.dong.circlelive.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.dong.circlelive.activities.Activity
import com.dong.circlelive.activities.ActivityDao
import com.dong.circlelive.appContext
import com.dong.circlelive.model.User
import com.dong.circlelive.model.UserDao

/**
 * Create by dooze on 2021/5/24  10:48 下午
 * Email: stonelavender@hotmail.com
 * Description:
 */
@Database(entities = [Activity::class, User::class], version = 2, exportSchema = false)
abstract class UserDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    abstract fun userDao(): UserDao


    companion object {

        @Volatile
        private var INSTANCE: UserDatabase? = null

        val db: UserDatabase
            get() = INSTANCE!!

        fun init(userId: String): UserDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    appContext.applicationContext,
                    UserDatabase::class.java,
                    "database_$userId"
                ).addMigrations(
                    MIGRATION_1_2
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `User` (`objectId` TEXT NOT NULL, `emUsername` TEXT NOT NULL, `username` TEXT NOT NULL, `avatarUrl` TEXT NOT NULL, PRIMARY KEY(`objectId`))")
    }
}