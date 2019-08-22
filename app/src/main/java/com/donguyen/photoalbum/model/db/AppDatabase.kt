package com.donguyen.photoalbum.model.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Created by DoNguyen on 9/3/19.
 *
 * I used Room database instead of Realm intentionally to show how great it is when working with other libraries
 * in the Architecture Components.
 * This is something Google is pushing hard on, so expect to have more great features in the future.
 */
@Database(
    entities = [PhotoData::class],
    version = 1, exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun photoDao(): PhotoDao

    companion object {
        // these variables can change based on network condition
        var PAGE_SIZE = 30
        var PREFETCH_DISTANCE = PAGE_SIZE
        var INITIAL_LOAD_SIZE = 3 * PAGE_SIZE

        @Volatile
        private var instance: AppDatabase? = null

        @Synchronized
        fun getInstance(context: Context): AppDatabase {
            return instance ?: buildDatabase(context).also { instance = it }
        }

        private fun buildDatabase(context: Context): AppDatabase {
            return Room.databaseBuilder(context, AppDatabase::class.java, "album-db").build()
        }
    }
}