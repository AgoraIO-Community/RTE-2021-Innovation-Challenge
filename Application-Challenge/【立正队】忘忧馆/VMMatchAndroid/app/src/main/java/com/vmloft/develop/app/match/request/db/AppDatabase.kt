package com.vmloft.develop.app.match.request.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

import com.tencent.wcdb.database.SQLiteCipherSpec
import com.tencent.wcdb.room.db.WCDBOpenHelperFactory

import com.vmloft.develop.app.match.app.App
import com.vmloft.develop.app.match.common.Constants
import com.vmloft.develop.app.match.request.bean.Category
import com.vmloft.develop.app.match.request.bean.Profession

/**
 * Create by lzan13 on 2020/8/9 14:35
 * 描述：Room 数据库操作类
 */
@Database(entities = [Category::class, Profession::class], version = 1)
abstract class AppDatabase : RoomDatabase() {

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(): AppDatabase =
            instance ?: synchronized(this) {
                instance ?: buildDatabase().also { instance = it }
            }

        private fun buildDatabase(): AppDatabase {
            // 加密方式
            val cipher = SQLiteCipherSpec().setPageSize(4096).setKDFIteration(64000)
            // WCDB 打开数据库工厂类
            val factory = WCDBOpenHelperFactory()
                .passphrase(Constants.dbPass.toByteArray())
                .cipherSpec(cipher)
                .asyncCheckpointEnabled(true)

            // 实例化数据库
            return Room.databaseBuilder(App.appContext, AppDatabase::class.java, Constants.dbName)
                .openHelperFactory(factory)
//                .addMigrations(migration1To2)
                .build()
        }

//        val migration1To2: Migration = object : Migration(1, 2) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE category ADD is_male INTEGER NOT NULL DEFAULT 0")
//            }
//        }

//        val migration2To3: Migration = object : Migration(2, 3) {
//            override fun migrate(database: SupportSQLiteDatabase) {
//                database.execSQL("ALTER TABLE user ADD address TEXT")
//            }
//        }
    }

    // 获取分类数据操作 Dao
    abstract fun categoryDao(): CategoryDao

    // 获取职业数据操作 Dao
    abstract fun professionDao(): ProfessionDao

}